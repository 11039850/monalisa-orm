package com.tsc9526.monalisa.core.datasource;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBConfig implements com.tsc9526.monalisa.core.annotation.DB, Closeable{ 	
	/**
	 * <code>DEFAULT_PATH= ".";</code> <br>
	 * The file path for DB.configFile() is :<br>
	 * <code>System.getProperty("DB@"+DB.key(),DEFAULT_PATH)+"/"+configFile;</code> 
	 */
	public static String DEFAULT_PATH=".";
	
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
	
	private DataSource ds;
	
	private DBConfig owner; 
	
	private String[] prefixs=new String[]{"DB.cfg"};		
	
	private boolean initialized=false;
	
	private File cfgFile;
	
	private long cfgFileTime=0L;	 
	
	private List<MetaPartition> metaPartitions;
	private DBConfig(){
	}
	
	DBConfig(String key,DB db){	 
		this.db=db;
		this.key=key;				 
	}
	
	public synchronized List<MetaPartition> getPartitions(){
		if(metaPartitions==null){
			metaPartitions=new ArrayList<MetaPartition>();
			String pts=partitions();
			if(pts!=null && pts.trim().length()>0){
				String[] ps=pts.trim().split(";");
				for(String p:ps){
					p=p.trim();
					if(p.length()>0){
						metaPartitions.add(new MetaPartition(p));
					}
				}
			}
		}
		return metaPartitions;
	}
	
	public MetaPartition getPartition(String tablePrefix){
		for(MetaPartition p:getPartitions()){
			if(p.getTablePrefix().equalsIgnoreCase(tablePrefix)){
				return p;
			}
		}
		return null;
	}
	
	public MetaPartition getPartition(Model<?> m){
		String tablePrefix=m.getClass().getAnnotation(Table.class).name();
		return getPartition(tablePrefix);
	}
	
	public DB getDb() {		
		return db;
	} 
	
	public String key() {		 
		return key;
	} 
	
	public DBConfig getOwner(){
		checkInit();
		
		return owner;
	}
	
	public boolean isCfgFileChanged(){
		if(cfgFile!=null && cfgFileTime>0){
			return cfgFileTime < cfgFile.lastModified();				 
		}
		return false;
	}
	
	private void checkInit(){		 	
		if(!initialized){
			synchronized (this) {
				if(!initialized){
					init(db);
				}
			}			
		}
	}
	
	synchronized void init(DB db){
		this.db=db;
		
		loadCfgFromFile();	
		
		this.configName      = db.configName();
		
		
		if(configName!=null && configName.trim().length()>0){
			prefixs=new String[]{"DB."+configName.trim(), "DB.cfg"};
		}
		
		this.modelClass      = getValue(p,"modelClass",      db.modelClass(),     prefixs);
		this.datasourceClass = getValue(p,"datasourceClass", db.datasourceClass(),prefixs);
		this.url             = getValue(p,"url",             db.url(),            prefixs);
		this.driver          = getValue(p,"driver",          db.driver(),         prefixs);
		this.catalog         = getValue(p,"catalog",         db.catalog(),        prefixs);
		this.schema          = getValue(p,"schema",          db.schema(),         prefixs);
		this.username        = getValue(p,"username",        db.username(),       prefixs);
		this.password        = getValue(p,"password",        db.password(),       prefixs);
		this.tables          = getValue(p,"tables",          db.tables(),         prefixs);
		this.partitions      = getValue(p,"partitions",      db.partitions(),     prefixs);	
		this.modelListener   = getValue(p,"modelListener",   db.modelListener(),  prefixs);
		this.mapping         = getValue(p,"mapping",         db.mapping(),        prefixs);			
			
		processUrlHosts();
		
		initialized=true;
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
	
	public List<Host> getHosts(){
		checkInit();
		
		return this.dbHosts;
	}
	 		
	public synchronized DataSource getDataSource(){
		checkInit();
		
		if(ds!=null){
			String cc=datasourceClass();
			
			if(cc==null || cc.trim().length()==0){
				cc=SimpleDataSource.class.getName();
			}
			
			if(ds.getClass().getName().equals(cc.trim()) == false){
				close();
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
		String cc=datasourceClass();
		if(cc!=null && cc.trim().length()>0){			
			try{				
				Object obj=Class.forName(cc.trim()).newInstance();	
				if(obj instanceof PooledDataSource){
					PooledDataSource pds=(PooledDataSource)obj;
					
					pds.setProperties(getPoolProperties());
					
					pds.setDriver(driver());
					pds.setPassword(password());
					pds.setUrl(url());
					pds.setUsername(username());
					
					
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
	
	private Properties getPoolProperties(){
		Properties dbps=new Properties();
		
		for(Object o:p.keySet()){
			String key=o.toString();
			
			for(String px:prefixs){
				String flag=px+".pool.";
				if(key.startsWith(flag)){
					dbps.put(key.substring(flag.length()), p.get(key));
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
	
	public String modelClass(){
		checkInit();
		
		return this.modelClass;
	}
	
	public String datasourceClass(){
		checkInit();
		
		return this.datasourceClass;
	}
 
	public String url(){
		checkInit();
		
		return this.url;
	}	
	 
	public String driver(){
		checkInit();
		
		return this.driver;
	}
	
	public String catalog(){
		checkInit();
		
		return this.catalog;
	}
	
	public String schema(){
		checkInit();
		
		return this.schema;
	}
   
	public String username() {
		checkInit();
		
		return this.username;
	}
	
	public String password(){
		checkInit();
		
		return this.password;
	}
		 
	public String tables(){
		checkInit();
		
		return this.tables;
	}
	
	 
	public String mapping(){
		checkInit();
		
		return this.mapping;
	}	
  
	public String partitions(){
		checkInit();
		
		return this.partitions;
	}
	
	public String modelListener(){
		checkInit();
		
		return this.modelListener;
	}	
	 
	public String configName() {
		checkInit();
		
		return this.configName;
	}
 
	public String configFile(){
		checkInit();
		
		return this.configFile;
	} 
	
	public String getProperty(String key){
		checkInit();
		
		return this.getValue(p, key, null, prefixs);
		
	}
	
	public String getProperty(String key,String defaultValue){
		checkInit();
		
		return this.getValue(p, key, defaultValue, prefixs);
	}

	
	public int getProperty(String key,int defaultValue){
		checkInit();
		
		String v=getProperty(key);
		if(v==null || v.trim().length()==0){
			return defaultValue;
		}else{
			return Integer.parseInt(v.trim());
		}
	}
	 
	public Query createQuery(){		
		return new Query(this);
	}
	
	public Query createQuery(Class<?> resultClass){
		return new Query(this,resultClass);
	}
	
	public Class<? extends Annotation> annotationType() {		 
		return DB.class;
	}
 	
	public static enum Level{
		ONLY_READ, ONLY_WRITE, READ_AND_WRITE
	}
	
	public class Host{
		public String   NAME;
		public Level    LEVEL = Level.READ_AND_WRITE;
		public String   HOST_PORT;
		public String   URL;
		
		private DBConfig cfg;
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
			cfg=new DBConfig();
			cfg.initialized=true;
			cfg.owner=DBConfig.this;
			
			
			cfg.db=DBConfig.this.db;
			cfg.modelClass=DBConfig.this.modelClass;
			cfg.datasourceClass=DBConfig.this.datasourceClass;			 
			cfg.driver=DBConfig.this.driver;
			cfg.catalog=DBConfig.this.catalog;
			cfg.schema=DBConfig.this.schema;
			cfg.username=DBConfig.this.username;
			cfg.password=DBConfig.this.password;
			cfg.tables=DBConfig.this.tables;
			cfg.partitions=DBConfig.this.partitions;
			cfg.modelListener=DBConfig.this.modelListener;
			cfg.mapping=DBConfig.this.mapping;		
			cfg.configFile=DBConfig.this.configFile;
			cfg.configName=DBConfig.this.configName;
			cfg.p=DBConfig.this.p;	
			cfg.prefixs=DBConfig.this.prefixs;
			cfg.url=URL;			
			
			String x=DBConfig.this.key;
			
			if(LEVEL==Level.ONLY_READ){
				x="-"+x;
			}else if(LEVEL==Level.ONLY_WRITE){
				x="+"+x;
			}
			
			if(NAME!=null){
				x=NAME+"@"+x;
			}
			
			cfg.key="#"+x;						
		}
		
		public DBConfig getConfig(){
			return cfg;
		}
	}
	 
	public static DBConfig fromClass(Class<?> clazzWithDBAnnotation){
		return DataSourceManager.getInstance().getDBConfig(clazzWithDBAnnotation);
	}
}
