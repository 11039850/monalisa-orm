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
package com.tsc9526.monalisa.orm.datasource;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.annotation.Table;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.model.ModelEvent;
import com.tsc9526.monalisa.orm.model.ModelMeta;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.tools.PkgNames;
import com.tsc9526.monalisa.tools.cache.Cache;
import com.tsc9526.monalisa.tools.cache.CacheManager;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpLib;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpEclipse;
import com.tsc9526.monalisa.tools.string.MelpString;
import com.tsc9526.monalisa.tools.template.VarTemplate;

/** 
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBConfig implements Closeable{ 	
	static Logger logger=Logger.getLogger(DBConfig.class);
	
	public final static String PREFIX_DB       ="DB";
	public final static String CFG_DEFAULT_NAME="cfg";
	
	private String[] prefixs=new String[]{PREFIX_DB+"."+CFG_DEFAULT_NAME};
	
	protected CFG _cfg=new CFG();
	 
	private DSI dsi;
	
	private DBConfig owner;
	
	private boolean initialized=false;
 		
	private DBConfig(){
	}
	
	DBConfig(String key,DB db,Class<?> cfgAnnotationClass){	 
		this._cfg.db=db;
		this._cfg.key=key;	
		this._cfg.annotationClass=cfgAnnotationClass;
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
				r=new DBConfig(dbKey, getCfg().db,getCfg().annotationClass);
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
	
	public String getSchema(){
		String schema=getCfg().schema;
		if(schema==null || schema.length()==0){
			schema=getDialect().getSchema(getCfg().url);
		}
		return schema;
	}
	
	public String getKey(){
		return getCfg().getKey();
	}
	
	public DB getDb(){
		return getCfg().getDb();
	}
	
	protected synchronized void delayClose(final DataSource ds,int delay){
		ModelMeta.clearReloadModelMetas(getKey());
		
		MelpClose.delayClose(ds,delay);
	}
	 		
	public synchronized DataSource getDataSource(){
		CFG cfg=getCfg();
		
		tryLoadDriverLib(cfg.getDriver()); 
		
		if(cfg.isCfgFileChanged()){
			init(cfg.db);
			
			if(dsi!=null){
				DSI other=new DSI();
			
				if(!dsi.equals(other)){
					if(dsi.ds!=null){
						int delay=DbProp.PROP_DB_DATASOURCE_DELAY_CLOSE.getIntValue(this, 30);
						logger.info("DBCfg:" +cfg.key+" changed, delay "+delay+"s close exists datasouce: "+dsi.url);
						delayClose(dsi.ds,delay);
					}
					dsi=other;
				}
			}
		}
 	
		if(dsi==null){
			dsi=new DSI();
		}
		
		return dsi.getDataSource();
	}
	
	protected void tryLoadDriverLib(String driverClass){		
		if(MelpLib.hLibClasses.containsKey(driverClass)){
			MelpLib.loadClass(driverClass);
		}
	}
	 
	 
	protected void finalize()throws Throwable{
		close();
		
		super.finalize();
	}
	
	public synchronized void close(){		
		if(dsi!=null){
			try{
				if(dsi.ds!=null){
					MelpClose.close(dsi.ds);	
				}
			}finally{			
				dsi=null;
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
	
	public List<DataMap> selectList(int limit, int offset,String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.getList(limit, offset);
	}
	
	public int execute(String sql,Object... args){
		Query query=createQuery().add(sql,args);
		return query.execute();
	}
		
	public int[] executeBatch(String[] sqls){
		Query query=createQuery();
		return query.executeBatch(sqls);
	}
	
	public int[] executeBatch(String sql,List<Object[]> args){
		Query query=createQuery();
		query.add(sql);
		
		for(Object[] os:args){
			query.addBatch(os);
		}
		return query.executeBatch();
	}
	
	
	public int[] batchInsert(List<? extends Model<?>> models){
		return batchOpModels(models, ModelEvent.INSERT); 
	}
	
	public int[] batchReplace(List<? extends Model<?>> models){
		return batchOpModels(models, ModelEvent.REPLACE); 
	}
	
	public int[] batchUpdate(List<? extends Model<?>> models){
		return batchOpModels(models, ModelEvent.UPDATE); 
	}
	 
	public int[] batchDelete(List<? extends Model<?>> models){
		return batchOpModels(models, ModelEvent.DELETE); 
	}
	
	protected int[] batchOpModels(final List<? extends Model<?>> models,final ModelEvent op){
		return com.tsc9526.monalisa.orm.Tx.execute(new com.tsc9526.monalisa.orm.Tx.Atom<int[]>(){
			public int[] execute() throws Throwable {
				int[] rs=new int[models.size()];
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
				return rs;
			}
		});
	}
	
	public boolean tableExist(String name){
		return tableExist(name, false);
	}
	
	public boolean tableExist(String name,boolean includeView){
		return getDialect().tableExist(this, name, includeView);
	}
	
	public Set<String> getTables(){
		return getTables(false);
	}
	
	public Set<String> getTables(boolean includeView){
		String[] types=includeView? new String[]{"TABLE","VIEW"}  : new String[]{"TABLE"};
		
		Connection conn=null;
		ResultSet rs=null;
		try{
			conn=getDataSource().getConnection();
			 
			Set<String> tables=new LinkedHashSet<String>();
			 
			DatabaseMetaData dbm=conn.getMetaData();
			rs=dbm.getTables(null, null,"%",types );
			while(rs.next()){
				String table=rs.getString("TABLE_NAME");
				table=Dialect.getRealname(table);
				
				tables.add(table);
			}
			return tables;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			MelpClose.close(rs,conn);
		}
	}
	
	public static enum Level{
		ONLY_READ, ONLY_WRITE, READ_AND_WRITE
	}
	
	public static DBConfig fromClass(Class<?> clazzWithDBAnnotation){
		return DataSourceManager.getInstance().getDBConfig(clazzWithDBAnnotation);
	}
	
	public DBConfig fromDB(String dbKey,DB db){
		return DataSourceManager.getInstance().getDBConfig(dbKey,db,false);
	}
	 
	public static DBConfig fromJdbcUrl(String jdbcUrl,String username,String password){
		final String dbKey=jdbcUrl+"&username="+username+"&password="+password;
		DB db=createDB(jdbcUrl,username,password);
		return DataSourceManager.getInstance().getDBConfig(dbKey, db);
	}
	
	public static DB createDB(final String jdbcUrl,final String username,final String password){
		final String dbKey=jdbcUrl+"&username="+username+"&password="+password;
		final String driverClass=DataSourceManager.getInstance().getDialect(jdbcUrl).getDriver();
		
		return new DB(){
			public Class<? extends Annotation> annotationType() {
				return DB.class;
			}
			 
			public String url() {
				return jdbcUrl;
			}
	 	 
			public String driver() {
				return driverClass;
			}
			 
			public String catalog() {
				return "";
			}
	
			public String schema() {
				return "";
			}
	
			public String username() {
				return username;
			}
	
			public String password() {
				return password;
			}
	
			public String dbs(){
				return "";
			}
			
			public String dbsAuthUsers(){
				return "";
			}
			
			public String tables() {
				return "%";
			}
	
			public String partitions() {
				return null;
			}
	
			public String mapping() {
				return null;
			}
	
			public String modelClass() {
				return null;
			}
	
			public String modelListener() {
				return null;
			}
	
			public String datasourceClass() {
				return null;
			}
	
			public String key() {
				return dbKey;
			}
	 	 
			public String configName() {
				return null;
			}
			 
			public String configFile() {
				return null;
			}
	
			 
			public Class<? extends ConfigClass> configClass() {
				return null;
			}
			
			public String[] properties(){
				return null;
			}
		};
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
			
			MelpClass.copy(DBConfig.this._cfg, dbcfg._cfg);
			 
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
		protected String cfgBasePath =null;
		
		private Class<?> annotationClass;
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
		private String dbs;
		private String dbsAuthUsers;
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
			this.dbs             = getValue(p,DbProp.PROP_DB_DBS.getKey(),               db.dbs(),            prefixs);
			this.dbsAuthUsers    = getValue(p,DbProp.PROP_DB_DBS_AUTH_USERS.getKey(),    db.dbsAuthUsers(),   prefixs);
			
			this.tables          = getValue(p,DbProp.PROP_DB_TABLES.getKey(),            db.tables(),         prefixs);
			this.partitions      = getValue(p,DbProp.PROP_DB_PARTITIONS.getKey(),        db.partitions(),     prefixs);			
			this.mapping         = getValue(p,DbProp.PROP_DB_MAPPING.getKey(),           db.mapping(),        prefixs);
			this.datasourceClass = getValue(p,DbProp.PROP_DB_DATASOURCE_CLASS.getKey(),  db.datasourceClass(),prefixs);
			
			this.modelClass      = getValue(p,DbProp.PROP_TABLE_MODEL_CLASS.getKey(),    db.modelClass(),     prefixs);
			this.modelListener   = getValue(p,DbProp.PROP_TABLE_MODEL_LISTENER.getKey(), db.modelListener(),  prefixs);
			 	
			this.cacheTables	 = getValue(p,DbProp.PROP_DB_CACHE_TABLES.getKey(), "",  prefixs);
			 
			if(this.url==null || this.url.trim().length()<1){
				if(cfgFile!=null || cfgFileLocal!=null){
					
					throw new RuntimeException("DBCfg: "+key+", missing property: "+prefixs[0]+".url, annotationClass: "+annotationClass
							+ ( cfgFile!=null     ?("\r\nfile: "+cfgFile.cfgFile.getAbsolutePath()): "") 
							+ ( cfgFileLocal!=null?("\r\nfile: "+cfgFileLocal.cfgFile.getAbsolutePath()): "") 
							);
				}else{
					throw new RuntimeException("DBCfg: "+key+", missing property: url, annotationClass: "+annotationClass);
				}
			}
		 	
			processUrlHosts();
		}
		
		public void setProperty(String key,String value){
			p.setProperty(key, value);
		}
		
		public void resetProperties(Properties p){
			this.p.clear();
			this.p.putAll(p);
		}
		
		private void loadProperties(){
			if(cfgBasePath!=null){
				loadCfgFromFile();
			}else{
				Class<? extends ConfigClass> clazz=DBGeneratorProcessing.getDBConfigClass(db); 
				String cff=db.configFile();
				
				if(clazz!=null && clazz != ConfigClass.class){
					try{
						configClass=clazz.newInstance();
						this.p=configClass.getConfigProperties();
					}catch(Exception e){
						throw new RuntimeException("Load config exception, class: "+clazz.getName()+", "+e, e);
					}
				}else if(cff!=null && cff.startsWith("classpath:")){
					String resource=cff.substring("classpath:".length());
					loadCfgFromClassResource(clazz,resource);
				}else{
					loadCfgFromFile();
				}
			}
			
			String[] props=db.properties();
			if(props!=null){
				for(String pv:props){
					if(pv!=null){
						int x=pv.indexOf("=");
						if(x>0){
							String name=pv.substring(0,x).trim();
							String value=pv.substring(x+1).trim();
							if(!p.containsKey(name)){
								p.put(name, value);
							}
						}
					}
				}
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
	  
		protected void loadCfgFromClassResource(Class<?> clazz,String resource) {
			try{
				InputStream in=clazz.getResourceAsStream(resource);
				if(in==null && annotationClass!=null){
					in=annotationClass.getResourceAsStream(resource);
				}
				
				//try: /resource
				if(in==null && !resource.startsWith("/")){
					resource="/"+resource;
					in=clazz.getResourceAsStream(resource);
					if(in==null && annotationClass!=null){
						in=annotationClass.getResourceAsStream(resource);
					}
				}
				
				InputStreamReader reader=new InputStreamReader(in,"utf-8");					 
				Properties prop=new Properties();
				prop.load(reader);
				reader.close();
				
				this.p=prop;
			}catch(Exception e){
				throw new RuntimeException("Failed to load classpath resource: "+resource,e);
			}
		}
		
		protected void loadCfgFromFile() {
			configFile=db.configFile();
			 
			boolean definedCfgFile= configFile!=null && configFile.trim().length()>0;
			
			if(definedCfgFile){
				configFile=configFile.trim();
				
				if(configFile.startsWith("file:")){
					configFile=configFile.substring("file:".length());
				}
				
				if(configFile.startsWith("classpath:")){
					configFile=configFile.substring("classpath:".length());
				}
			}else{
				configFile=key+".cfg";
			}
				 
			configFile=findConfigFile(configFile);
			 	
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
		
		protected String findConfigFile(String configFile){	
			if(cfgBasePath!=null){
				configFile=MelpFile.combinePath(cfgBasePath,configFile);
				
				logger.info("find("+key+") cfg base path by cfgBasePath: "+cfgBasePath+", file: "+configFile);
			}else{
				if(configFile.startsWith("/")==false){
					if(configFile.length()>1 && configFile.charAt(1)==':'){
						//Windows ROOT C: D: E: ...
						logger.info("find("+key+") cfg base path by win-root config file: "+configFile); 
					}else{
						configFile=MelpFile.combinePath(findCfgBasePath(configFile),configFile);
					}				
				}else{
					logger.info("find("+key+") cfg base path by root config file: "+configFile); 
				}
			}
			
			return configFile;
		}
		
		protected String findCfgBasePath(String configFile){
			String basepath = System.getProperty("DB@"+key);
			 
			if(basepath == null && key.indexOf('.') >= 0){
				String pkey = key.substring(0,key.lastIndexOf('.'));
				basepath    = System.getProperty("DB@"+pkey);
			}
			
			if(basepath==null){
				String defpath=MelpFile.combinePath(DbProp.CFG_ROOT_PATH,configFile);
				if(new File(defpath).exists()==false && annotationClass!=null){
					basepath=MelpEclipse.findCfgBasePathByClass(annotationClass,configFile);
					if(basepath!=null){
						logger.info("Search("+key+") base path from class: "+annotationClass.getName()+", path: "+basepath+", file: "+configFile);
						
						return basepath;
					}else{
						logger.info("Search("+key+") base path use default path: "+DbProp.CFG_ROOT_PATH+", file: "+configFile);
						
						return DbProp.CFG_ROOT_PATH;
					}
				}else{
					logger.info("Search("+key+") base path by default path: "+DbProp.CFG_ROOT_PATH+", file: "+configFile);
					
					return DbProp.CFG_ROOT_PATH;
				}
			}else{
				logger.info("Search("+key+") base path by system property: DB@"+key+", path: "+basepath+", file: "+configFile);
				
				return basepath;
			}
		} 
		
		protected boolean loadCfg(CfgFile cf,Properties prop){
			if(cf.cfgFile.exists()){
				logger.info("Found ("+key+") base path from: "+cf.cfgFile.getAbsolutePath());				
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
		
		public String parseFilePath(String path){
			VarTemplate vt=new VarTemplate(getVarProperties());
			vt.setThrowExceptionOnVarNotFound(true);
			return vt.getValue(path);
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
		
		public String getPropertyOfDB(String key){				
			return this.getValue(p, key, null, prefixs.length>1?new String[]{prefixs[0]}:prefixs);		
		}
		
		public String getPropertyOfDB(String key,String defaultValue){		 
			return this.getValue(p, key, defaultValue, prefixs.length>1?new String[]{prefixs[0]}:prefixs);
		}
		
		public int getPropertyOfDB(String key,int defaultValue){		 		
			String v=getPropertyOfDB(key);
			if(v==null || v.trim().length()==0){
				return defaultValue;
			}else{
				return Integer.parseInt(v.trim());
			}
		}
		
		public Properties getPoolProperties(){
			return getPropertiesByPrefix("pool");
		}
		
		public Properties getVarProperties(){
			return getPropertiesByPrefix("var");
		}
		
		public Properties getPropertiesByPrefix(String prefix){
			Properties dbps=new Properties();
		 
			for(Object o:p.keySet()){
				String key=o.toString();
				
				for(String px:prefixs){
					String flag=px+"."+prefix+".";
					if(key.startsWith(flag)){
						dbps.put(key.substring(flag.length()), p.get(key));
					}
				}
			}
			
			return dbps;
		}
		
		public Properties getDbsProperties(){
			Properties dbps=new Properties();
			  
			if(!MelpString.isEmpty(dbs)){
				dbps.put("name", dbs);
				dbps.put("auth.users", dbsAuthUsers);
				
				for(Object o:p.keySet()){
					String key=o.toString();
				 	
					for(String px:prefixs){
						String flag=px+".dbs.";
						if(key.startsWith(flag)){
							dbps.put(key.substring(flag.length()), p.get(key));
						}
					}
				}
			}
			return dbps;
		}
		
		public boolean isCfgFileChanged(){
			if(configClass!=null){
				return configClass.isCfgChanged();
			}else if(configFile!=null){
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
				for(String name:MelpString.splits(cacheTables)){
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
		
		public Class<?> getAnnotationClass(){
			return annotationClass;
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
			if(driver==null||driver.length()<1){
				driver=getDialect().getDriver();
			}
			
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
		
		public String getDbs(){
			return dbs;
		}
		
		public String getDbsAuthUsers(){
			return dbsAuthUsers;
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
	
	class DSI{
		private DataSource ds=null;
		
		private String     datasourceClass;
		private String     validationgQuery;
		
		private String     driver;
		private String     password;
		private String     url;
		private String     username;
		
		private Properties poolProps=new Properties();
		 
		
		public DSI(){
			CFG cfg=getCfg();
			
			datasourceClass=cfg.getDatasourceClass();
			if(datasourceClass!=null && datasourceClass.trim().length()>0){	
				datasourceClass=datasourceClass.trim();
				
				if(datasourceClass.indexOf(".")<0){
					datasourceClass=PkgNames.ORM_DATASOURCE+"."+datasourceClass;
				}
			}
			 
			validationgQuery=getDialect().getIdleValidationQuery();
			 
			driver   = cfg.getDriver();
			password = cfg.getPassword();
			url      = cfg.getUrl();
			username = cfg.getUsername();
			
			poolProps.putAll(cfg.getPoolProperties());
		}
		
		public boolean equals(Object other){
			return toString().equals(other.toString());
		}
		
		public String toString(){
			StringBuilder sb=new StringBuilder();
			
			sb.append("datasourceClass:").append(datasourceClass);
			sb.append(",validationgQuery:").append(validationgQuery);
	
			sb.append(",driver:").append(driver);
			sb.append(",password:").append(password);
			sb.append(",url:").append(url);
			sb.append(",username:").append(username);
			
			TreeMap<Object, Object> tree=new TreeMap<Object, Object>();
			for(Object key:poolProps.keySet()){
				tree.put(key, poolProps.get(key));
			}
			
			for(Object key:tree.keySet()){
				sb.append(","+key+":").append(tree.get(key));
			}
			
			return sb.toString();
		}
		
		
		public DataSource getDataSource(){
			if(ds==null){
				if(!DbProp.ProcessingEnvironment){
					ds=getDataSourceFromConfigClass();
				}
				
				if(ds==null){
					ds=new SimpleDataSource(DBConfig.this);
				}
			}
			
			return ds;
		}
		
		private DataSource getDataSourceFromConfigClass(){
			String cc=datasourceClass;
			if(cc!=null && cc.trim().length()>0){	
				cc=cc.trim();
				
				if(cc.indexOf(".")<0){
					cc=PkgNames.ORM_DATASOURCE+"."+cc;
				}
			 	
				try{	
					Object obj=instanceDataSource(cc); 	
					if(obj instanceof PooledDataSource){
						PooledDataSource pds=(PooledDataSource)obj;
						
						if(validationgQuery!=null && validationgQuery.trim().length()>0){
							pds.setIdleValidationQuery(DbProp.CFG_CONNECT_IDLE_INTERVALS,validationgQuery.trim());
						}
						
						pds.setProperties(poolProps);
						
						pds.setDriver(driver);
						pds.setPassword(password);
						pds.setUrl(url);
						pds.setUsername(username);
						 
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
		
		protected Object instanceDataSource(String clazz)throws Exception{
			if(clazz.equals(PkgNames.ORM_DS_C3p0)){
				return MelpLib.createC3p0DataSource();
			}else if(clazz.equals(PkgNames.ORM_DS_Durid)){
				return MelpLib.createDruidDataSource();
			}else{
				return MelpClass.forName(clazz).newInstance();	
			}
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
