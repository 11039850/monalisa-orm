/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.core.datasource;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.cache.Cache;
import com.tsc9526.monalisa.core.query.cache.CacheManager;
import com.tsc9526.monalisa.core.query.datatable.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelEvent;
import com.tsc9526.monalisa.core.query.model.Record;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBConfig implements Closeable{ 	
	static Logger logger=Logger.getLogger(DBConfig.class);
	
	public final static String PREFIX_DB       ="DB";
	public final static String CFG_DEFAULT_NAME="cfg";
	
	private String[] prefixs=new String[]{PREFIX_DB+"."+CFG_DEFAULT_NAME};
	
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
	
	public synchronized DBConfig getByConfigName(String configName){
		DataSourceManager dsm=DataSourceManager.getInstance();
		
		String dbKey=this._cfg.key+"#"+configName;		 
		DBConfig r=dsm.getDBConfig(dbKey, null);
		if(r==null){
			String cfgDBUrl=getCfg().p.getProperty(PREFIX_DB+"."+configName+".url");
			if(cfgDBUrl!=null){
				r=new DBConfig(dbKey, getCfg().db);
				r._cfg.configName=configName;
				r.init();
				
				dsm.putDBConfig(dbKey, r);
			}else{
				throw new RuntimeException("Config not found: "+configName+", DB: "+this.getKey());
			}
		}
		
		return r;
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
	
	public Dialect getDialect(){
		return DataSourceManager.getInstance().getDialect(this);
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
	
	protected synchronized void delayClose(final DataSource ds,int delay){
		CloseQuietly.delayClose(ds,delay);
	}
	 		
	public synchronized DataSource getDataSource(){
		CFG cfg=getCfg();
		
		if(cfg.isCfgFileChanged()){
			init(cfg.db);
			
			if(ds!=null){
				int delay=DbProp.PROP_DB_DATASOURCE_DELAY_CLOSE.getIntValue(this, 30);
				delayClose(ds,delay);
				ds=null;
			}
		}
 	
		if(ds==null){
			if(!DbProp.ProcessingEnvironment){
				ds=getDataSourceFromConfigClass();
			}
			
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
			cc=cc.trim();
			
			if(cc.indexOf(".")<0){
				cc="com.tsc9526.monalisa.core.datasource."+cc;
			}
			
			try{	
				Object obj=ClassHelper.forClassName(cc).newInstance();	
				if(obj instanceof PooledDataSource){
					PooledDataSource pds=(PooledDataSource)obj;
					
					String validationgQuery=getDialect().getIdleValidationQuery();
					if(validationgQuery!=null && validationgQuery.trim().length()>0){
						pds.setIdleValidationQuery(DbProp.CFG_CONNECT_IDLE_INTERVALS,validationgQuery.trim());
					}
					
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
			
			for(Host host: getCfg().getDbHosts()){
				host.getConfig().close();
			}
		}
	}	 
	
	public String toString(){
		return "KEY: "+this._cfg.key;
	}
	 
	public Record createRecord(String tableName,String ... primaryKeys){		
		Record m=new Record(tableName,primaryKeys);
		m.use(this);
		return m;
	}
	
	public Query createQuery(){		
		return new Query(this);
	}
	 
	public DataMap selectOne(String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.getResult();
	}
	
	public DataTable<DataMap> select(String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.getList();
	}
	
	public Page<DataMap> select(int limit, int offset,String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.getPage(limit, offset);
	}
	
	public int execute(String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.execute();
	}
		
	public int[] executeBatch(String sql,List<Object[]> args){
		Query query=createQuery();
		query.add(sql);
		
		for(Object[] os:args){
			query.addBatch(os);
		}
		return query.executeBatch();
	}
	
	
	public int[] batchInsert(List<Model<?>> models){
		return batchOpModels(models, ModelEvent.INSERT); 
	}
	
	public int[] batchReplace(List<Model<?>> models){
		return batchOpModels(models, ModelEvent.REPLACE); 
	}
	
	public int[] batchUpdate(List<Model<?>> models){
		return batchOpModels(models, ModelEvent.UPDATE); 
	}
	 
	public int[] batchDelete(List<Model<?>> models){
		return batchOpModels(models, ModelEvent.DELETE); 
	}
	
	protected int[] batchOpModels(final List<Model<?>> models,final ModelEvent op){
		final int[] rs=new int[models.size()];
		
		com.tsc9526.monalisa.core.query.Tx.execute(new com.tsc9526.monalisa.core.query.Tx.Atom(){
			public int execute() throws Throwable {
				int i=0;
				for(Model<?> m:models){
					int r=0;
					
					if(op==ModelEvent.INSERT){
						r=m.save();
					}else if(op==ModelEvent.REPLACE){
						r=m.saveOrUpdate();
					}else if(op==ModelEvent.UPDATE){
						r=m.update();
					}else if(op==ModelEvent.DELETE){
						r=m.delete();
					}
					
					rs[i++]=r;
				}
				
				return 0;
			}
		});
		
		return rs;
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
			
			DataSourceManager.getInstance().putDBConfig(dbcfg._cfg.key, dbcfg);
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
				
		private CfgFile cfgFile;		 
		private CfgFile cfgFileLocal;
		 
		private List<MetaPartition> metaPartitions;
		
		private ConfigClass configClass;
	 	
		private Map<String,String> cacheModels=new ConcurrentHashMap<String,String>();
		private String      cacheTables;
		
		synchronized void init(){
			loadProperties();
			
			if(configName==null){
				configName=db.configName();
			}
			
			if(configName!=null && configName.trim().length()>0){
				if(configName.equals(CFG_DEFAULT_NAME)){
					throw new RuntimeException("Invalid property configName: cfg! \"cfg\" is the common config for all databases. ");
				}
				
				prefixs=new String[]{PREFIX_DB+"."+configName.trim(), PREFIX_DB+"."+CFG_DEFAULT_NAME};
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
			 	
			this.cacheTables	 = getValue(p,DbProp.PROP_DB_CACHE_TABLES.getKey(), "",  prefixs);
				
			processUrlHosts();						 
		}
		
		private void loadProperties(){
			Class<? extends ConfigClass> clazz=DBGeneratorProcessing.getDBConfigClass(db); 
			
			if(clazz!=null && clazz != ConfigClass.class){
				try{
					configClass=clazz.newInstance();
					this.p=configClass.getConfigProperties();
				}catch(Exception e){
					throw new RuntimeException("Load config exception, class: "+clazz.getName()+", "+e, e);
				}
			}else{
				loadCfgFromFile();
			}
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
					configFile=System.getProperty("DB@"+key,DbProp.CFG_ROOT_PATH)+"/"+configFile;
				}				
			}
			
			Properties prop=new Properties();
			
			cfgFile=new CfgFile(configFile);
			if(!loadCfg(cfgFile,prop)){
				cfgFile=null;
			}
			 
			cfgFileLocal=new CfgFile(configFile+".local");
			if(!loadCfg(cfgFileLocal,prop)){
				cfgFileLocal=null;
			}
			
			if(definedCfgFile && (cfgFile==null && cfgFileLocal==null) ){
				throw new RuntimeException("DB config file: "+new File(configFile).getAbsolutePath()+" not found!");				
			} 		
			
			this.p=prop;
		}
		
		protected boolean loadCfg(CfgFile cf,Properties prop){
			if(cf.cfgFile.exists()){
				logger.info("Load DB("+key+") config from: "+cf.cfgFile.getAbsolutePath());				
				try{
					cf.lastModified=cf.cfgFile.lastModified();
					 
					InputStreamReader reader=new InputStreamReader(new FileInputStream(cf.cfgFile),"utf-8");					 
					prop.load(reader);
					reader.close();
					
					return true;
				}catch(IOException e){
					throw new RuntimeException("Load db config file: "+cf.cfgFile.getAbsolutePath()+", exception: "+e,e);
				}
			}
			return false;
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
			if(configClass!=null){
				return configClass.isCfgChanged();
			}else{
				if(cfgFile!=null && cfgFile.lastModified>0){
					if( cfgFile.lastModified < cfgFile.cfgFile.lastModified()){
						return true;
					}
				}else if(cfgFile==null && new File(configFile).exists()){
					return true;
				}
				
				if(cfgFileLocal!=null && cfgFileLocal.lastModified>0){
					if( cfgFileLocal.lastModified < cfgFileLocal.cfgFile.lastModified()){
						return true;
					}
				}else if(cfgFileLocal==null && new File(configFile+".local").exists()){
					return true;
				}
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
		
		public Cache getCache(Model<?> m){
			if(cacheTables!=null && cacheTables.trim().length()>0 && cacheModels.size()==0){
				for(String name:Helper.splits(cacheTables)){
					String ckey=name.trim();
					String cname=DbProp.PROP_TABLE_CACHE_NAME.getValue(DBConfig.this, ckey);
					 
					ckey=getDialect().getTableName(ckey).toLowerCase();
					 
					cacheModels.put(ckey, cname);
				}
			}

			String tableName=m.table().name();
			
			String cacheName=getDialect().getTableName(tableName).toLowerCase();
			if(cacheModels.containsKey(cacheName)){
				String name=cacheModels.get(cacheName);
				
				String cacheClass=DbProp.PROP_TABLE_CACHE_CLASS.getValue(DBConfig.this, tableName);
				String eviction  =DbProp.PROP_TABLE_CACHE_EVICTION.getValue(DBConfig.this, tableName);
				
				return CacheManager.getInstance().getCache(cacheClass,eviction,name);
			}else{
				return null;
			}
		}
	 	
		public Cache getCache(){
			String cacheClass=DbProp.PROP_TABLE_CACHE_CLASS.getValue(DBConfig.this);
			String eviction  =DbProp.PROP_TABLE_CACHE_EVICTION.getValue(DBConfig.this);
			String name      =DbProp.PROP_TABLE_CACHE_NAME.getValue(DBConfig.this);
			
			return CacheManager.getInstance().getCache(cacheClass,eviction,name);
		}
		
		public Cache getCache(String name){
			String cacheClass=DbProp.PROP_TABLE_CACHE_CLASS.getValue(DBConfig.this);
			String eviction  =DbProp.PROP_TABLE_CACHE_EVICTION.getValue(DBConfig.this);
			 
			return CacheManager.getInstance().getCache(cacheClass,eviction,name);
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
		public Properties getProperties() {
			return p;
		}
		public List<Host> getDbHosts() {
			return dbHosts;
		}
		 
		public File[] getCfgFiles() {
			List<File> fs=new ArrayList<File>();
			if(cfgFile!=null){
				fs.add(cfgFile.cfgFile);
			}
			
			if(cfgFileLocal!=null){
				fs.add(cfgFileLocal.cfgFile);
			}
			
			return fs.toArray(new File[]{});
		}
	}
	
	
	private static class CfgFile{
		private File cfgFile;
		private long lastModified;
		
		CfgFile(String filePath){
			cfgFile=new File(filePath);
		}				
	}
	
}
