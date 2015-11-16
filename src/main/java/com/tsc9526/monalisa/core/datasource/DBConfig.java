package com.tsc9526.monalisa.core.datasource;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelTable;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBConfig implements Closeable{ 	
	/**
	 * <code>DEFAULT_PATH= ".";</code> <br>
	 * The file path for DB.configFile() is :<br>
	 * <code>System.getProperty("DB@"+DB.key(),DEFAULT_PATH)+"/"+configFile;</code> 
	 */
	public static String DEFAULT_PATH=".";
	
	public static String[] prefixs=new String[]{"DB.cfg"};
	
	private CFG _cfg=new CFG();
	 
	private DataSource ds;
	
	private DBConfig owner;
	
	private boolean initialized=false;
	
	private DBConfig(){
	}
	
	DBConfig(String key,DB db){	 
		this._cfg.db=db;
		this._cfg.key=key;				 
	}
	
	public CFG getCfg(){
		if(!initialized){
			init();			 
		}
		return _cfg;
	}
	 
	synchronized void init(){
		if(!initialized){
			_cfg.init();
		}
		initialized=true;
	}
	
	synchronized void init(DB db){
		_cfg.db=db;
		_cfg.init();		 
		initialized=true;
	}
		
	
	public DBConfig getOwner(){
		getCfg();
		
		return owner;
	}
	
	public String getKey(){
		return getCfg().getKey();
	}
	
	public DB getDb(){
		return getCfg().getDb();
	}
	
	protected synchronized void delayClose(final DataSource ds){
		int delay=DbProp.PROP_DB_DATASOURCE_DELAY_CLOSE.getIntValue(this, 30);
		
		new Timer(true).schedule(new TimerTask() {
			public void run() {
				CloseQuietly.close(ds);
			}
		}, delay*1000);
	}
	 		
	public synchronized DataSource getDataSource(){
		CFG cfg=getCfg();
		
		if(cfg.isCfgFileChanged()){
			init(cfg.db);
			
			if(ds!=null){
				delayClose(ds);
				ds=null;
			}
		}
 	
		if(ds==null){
			ds=getDataSourceFromConfigClass();
			
			if(ds==null){			
				ds=new SimpleDataSource(this);
			}						 
		}		
		return ds;
	}
	
	private DataSource getDataSourceFromConfigClass(){
		CFG cfg=getCfg();
		
		String cc=cfg.getDatasourceClass();
		if(cc!=null && cc.trim().length()>0){			
			try{				
				Object obj=Class.forName(cc.trim()).newInstance();	
				if(obj instanceof PooledDataSource){
					PooledDataSource pds=(PooledDataSource)obj;
					
					pds.setProperties(getPoolProperties());
					
					pds.setDriver(cfg.getDriver());
					pds.setPassword(cfg.getPassword());
					pds.setUrl(cfg.getUrl());
					pds.setUsername(cfg.getUsername());
					
					
					return pds;
				}else if(obj instanceof DataSource){				 
					return(DataSource)obj;
				}
			}catch(Exception e){
				throw new RuntimeException("Create datasource exception: "+e,e);
			}
		}
		return null;
	}
	
	protected Properties getPoolProperties(){
		Properties dbps=new Properties();
		
		CFG cfg=getCfg();
		for(Object o:cfg.p.keySet()){
			String key=o.toString();
			
			for(String px:prefixs){
				String flag=px+".pool.";
				if(key.startsWith(flag)){
					dbps.put(key.substring(flag.length()), cfg.p.get(key));
				}
			}
		}
		
		return dbps;
	}
	
	protected void finalize()throws Throwable{
		close();
		
		super.finalize();
	}
	
	public synchronized void close(){		
		if(ds!=null){
			try{
				CloseQuietly.close(ds);	
			}finally{			
				ds=null;
			}
		}
	}	 
	
	
	 
	public ModelTable createModel(String tableName,String ... primaryKeys){		
		ModelTable m=new ModelTable(tableName,primaryKeys);
		m.use(this);
		return m;
	}
	
	public Query createQuery(){		
		return new Query(this);
	}
	
	public Query createQuery(Class<?> resultClass){
		return new Query(this,resultClass);
	}
	
	public static DBConfig fromClass(Class<?> clazzWithDBAnnotation){
		return DataSourceManager.getInstance().getDBConfig(clazzWithDBAnnotation);
	}
	 
	public static enum Level{
		ONLY_READ, ONLY_WRITE, READ_AND_WRITE
	}	
	
	public class Host{
		public String   NAME;
		public Level    LEVEL = Level.READ_AND_WRITE;
		public String   HOST_PORT;
		public String   URL;
		
		private DBConfig dbcfg;
		private Host(String host,String prefix,String suffix){
			host=host.trim();
			
			int x=host.indexOf("@");
			if(x>0){
				NAME=host.substring(0,x);
				host=host.substring(x+1);
			}
			
			if(host.startsWith("+")){
				LEVEL=Level.ONLY_WRITE;
				
				host=host.substring(1);
			}else if(host.startsWith("-")){
				LEVEL=Level.ONLY_READ;
				
				host=host.substring(1);
			}
			
			HOST_PORT=host;
			
			URL=prefix+HOST_PORT+suffix;
			
			
			initDBConfig();
		}
		
		private void initDBConfig(){
			dbcfg=new DBConfig();
			
			ClassHelper.copy(DBConfig.this._cfg, dbcfg._cfg);
			
			
			
			dbcfg.owner=DBConfig.this;
			dbcfg._cfg.url=URL;			
			
			String x=DBConfig.this._cfg.key;
			if(LEVEL==Level.ONLY_READ){
				x="-"+x;
			}else if(LEVEL==Level.ONLY_WRITE){
				x="+"+x;
			}
			if(NAME!=null){
				x=NAME+"@"+x;
			}
			dbcfg._cfg.key="#"+x;		
			
			dbcfg.initialized=true;
		}
		
		public DBConfig getConfig(){
			return dbcfg;
		}
	}
	
	
	
	public class CFG{
		private DB     db;
		private String key;
		
		private String modelClass;
		private String datasourceClass;
		private String url;
		private String driver;
		private String catalog;
		private String schema;
		private String username;
		private String password;
		private String tables;
		private String partitions;
		private String modelListener;
		private String mapping;		
		private String configFile;
		private String configName;
		
		private Properties p=new Properties();
		private List<Host> dbHosts=new ArrayList<Host>();
				
		private File cfgFile;
		private long cfgFileTime=0L;	 
		private List<MetaPartition> metaPartitions;
		
		synchronized void init(){
			loadCfgFromFile();	
			
			this.configName      = db.configName();
			
			
			if(configName!=null && configName.trim().length()>0){
				prefixs=new String[]{"DB."+configName.trim(), "DB.cfg"};
			}
			
		
			this.url             = getValue(p,DbProp.PROP_DB_URL.getKey(),               db.url(),            prefixs);
			this.driver          = getValue(p,DbProp.PROP_DB_DRIVER.getKey(),            db.driver(),         prefixs);
			this.catalog         = getValue(p,DbProp.PROP_DB_CATALOG.getKey(),           db.catalog(),        prefixs);
			this.schema          = getValue(p,DbProp.PROP_DB_SCHEMA.getKey(),            db.schema(),         prefixs);
			this.username        = getValue(p,DbProp.PROP_DB_USERNAME.getKey(),          db.username(),       prefixs);
			this.password        = getValue(p,DbProp.PROP_DB_PASSWORD.getKey(),          db.password(),       prefixs);
			this.tables          = getValue(p,DbProp.PROP_DB_TABLES.getKey(),            db.tables(),         prefixs);
			this.partitions      = getValue(p,DbProp.PROP_DB_PARTITIONS.getKey(),        db.partitions(),     prefixs);			
			this.mapping         = getValue(p,DbProp.PROP_DB_MAPPING.getKey(),           db.mapping(),        prefixs);
			this.datasourceClass = getValue(p,DbProp.PROP_DB_DATASOURCE_CLASS.getKey(),  db.datasourceClass(),prefixs);
			
			this.modelClass      = getValue(p,DbProp.PROP_TABLE_MODEL_CLASS.getKey(),    db.modelClass(),     prefixs);
			this.modelListener   = getValue(p,DbProp.PROP_TABLE_MODEL_LISTENER.getKey(), db.modelListener(),  prefixs);
			 	
			processUrlHosts();						 
		}
		
		protected void processUrlHosts() {
			int x1=url.indexOf("[");
			if(x1>0){
				int x2=url.indexOf("]",x1);
				
				String prefix=url.substring(0,x1);
				String suffix=url.substring(0,x2+1);
				
				String[] hosts=url.substring(x1+1,x2).split(",");
				for(String h:hosts){
					Host dbh=new Host(h,prefix,suffix);
					dbHosts.add(dbh);
				}
				
				this.url=dbHosts.get(0).URL;
			}
		}
	  
		protected void loadCfgFromFile() {
			configFile=db.configFile();
			
			boolean definedCfgFile= configFile!=null && configFile.trim().length()>0;
			
			if(definedCfgFile){
				configFile=configFile.trim();
			}else{
				configFile=key;
			}
				 
			
			if(configFile.startsWith("/")==false){
				if(configFile.length()>1 && configFile.charAt(1)==':'){
					//Windows ROOT C: D: E: ...
				}else{
					configFile=System.getProperty("DB@"+key,DEFAULT_PATH)+"/"+configFile;
				}				
			}
			
			cfgFile=new File(configFile);
			if(cfgFile.exists()){
				System.out.println("Load DB("+key+") config from: "+cfgFile.getAbsolutePath());				
				try{
					cfgFileTime=cfgFile.lastModified();
					 
					InputStreamReader reader=new InputStreamReader(new FileInputStream(cfgFile),"utf-8");
					p.clear();
					p.load(reader);
					reader.close();					 
				}catch(IOException e){
					throw new RuntimeException("Load db config file: "+cfgFile.getAbsolutePath()+", exception: "+e,e);
				}
			}else{
				if(definedCfgFile){
					throw new RuntimeException("DB config file: "+cfgFile.getAbsolutePath()+" not found!");
				}else{
					cfgFile=null;
				}
			} 							 
		}
	 
		 
		protected String getValue(Properties p,String key,String defaultValue,String[] prefixs){
			String r=null;
			if(prefixs.length>0){
				for(String prefix:prefixs){
					String v=p.getProperty(prefix+"."+key);
					if(v!=null){
						r=v;
						break;
					}
				}
			}
			if(r==null){
				r= p.getProperty(key, defaultValue);
			}
			
			if(r!=null){
				r=r.trim();
			}
			return r;
			
		}
		
		public String getProperty(String key){				
			return this.getValue(p, key, null, prefixs);		
		}
		
		public String getProperty(String key,String defaultValue){		 
			return this.getValue(p, key, defaultValue, prefixs);
		}
		
		public int getProperty(String key,int defaultValue){		 		
			String v=getProperty(key);
			if(v==null || v.trim().length()==0){
				return defaultValue;
			}else{
				return Integer.parseInt(v.trim());
			}
		}
		
		public boolean isCfgFileChanged(){
			if(cfgFile!=null && cfgFileTime>0){
				return cfgFileTime < cfgFile.lastModified();				 
			}
			return false;
		}
		

		public synchronized List<MetaPartition> getMetaPartitions(){
			if(metaPartitions==null){			
				String pts=getPartitions();
				metaPartitions=MetaPartition.parsePartitions(pts);
			}
			return metaPartitions;
		}
		
		public MetaPartition getPartition(String tablePrefix){
			for(MetaPartition p:getMetaPartitions()){
				if(p.getTablePrefix().equalsIgnoreCase(tablePrefix)){
					return p;
				}
			}
			return null;
		}
		
		public MetaPartition getPartition(Model<?> m){
			Table table=m.getClass().getAnnotation(Table.class);
			if(table!=null){
				String tablePrefix=table.name();
				return getPartition(tablePrefix);
			}else{
				throw new RuntimeException("Model class: "+m.getClass().getName()+" without annotation Table");
			}
		}
		 
		
		public DB getDb() {
			return db;
		}
		public String getKey() {
			return key;
		}
		public String getModelClass() {
			return modelClass;
		}
		public String getDatasourceClass() {
			return datasourceClass;
		}
		public String getUrl() {
			return url;
		}
		public String getDriver() {
			return driver;
		}
		public String getCatalog() {
			return catalog;
		}
		public String getSchema() {
			return schema;
		}
		public String getUsername() {
			return username;
		}
		public String getPassword() {
			return password;
		}
		public String getTables() {
			return tables;
		}
		public String getPartitions() {
			return partitions;
		}
		public String getModelListener() {
			return modelListener;
		}
		public String getMapping() {
			return mapping;
		}
		public String getConfigFile() {
			return configFile;
		}
		public String getConfigName() {
			return configName;
		}
		public Properties getP() {
			return p;
		}
		public List<Host> getDbHosts() {
			return dbHosts;
		}
		 
		public File getCfgFile() {
			return cfgFile;
		}		 		 
	}
	
	
}
