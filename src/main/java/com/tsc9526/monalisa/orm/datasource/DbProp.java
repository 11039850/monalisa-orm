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

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.tsc9526.monalisa.orm.dialect.Dialect;

/**
 * Database properties: <br><ul>
   <li><b>debug = false </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; If show running SQL statements
 * 
   <li><b>url = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; the JDBC url, for example: jdbc:mysql://127.0.0.1:3306/world 
 * 
   <li><b>driver    = com.mysql.jdbc.Driver</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; the JDBC driver class 
 * 
 * <li><b>username    = root</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  the database username 
 * 
 * <li><b>password    = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  the database password 
 * 
 * <li><b>catalog    = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  the database catalog 
 * 
 * <li><b>schema    = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  the database schema 
 * 
 * <li><b>tables    = %</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Indicates the table names to generate the model classes, "%"： means all of tables. <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  For example: pre_%: all tables with the prefix: "pre_"  
 * 
 * <li><b>mapping    = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Table name's mapping, For example: table_123=ModelX;table123=ModelY...  
 * 
 * <li><b>partitions = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Define partition tables 

 * <li><b>datasourceClass = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Data source class, the value can be C3p0DataSource or DruidDataSource<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  or other class which implementations of the class:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  {@link com.tsc9526.monalisa.orm.datasource.PooledDataSource}
 *
 * <li><b>datasourceDelayClose = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  After database configuration reload, delay closing data source that has been opened   
 * 
 * <li><b>history.db = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The history table is saved in the database.
 * 
 * <li><b>history.prefix.table = history_</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Prefix of history tables
 * 
 * <li><b>history.prefix.column = history_</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Column's prefix in the history tables
 * 
 * <li><b>history.tables = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Those tables need to record changes in history. %: means all of tables
 * 
 * <li><b>multi.resultset.deepth = 100</b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Useful when only the SQL query return multiple results
 * 
 * <li><b>cache.tables = </b> [scope: DB]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Which tables will be cached 
 * 
 * <li><b>version.name = version</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The version field's name 
 *   
 * <li><b>modelClass = </b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;   A base mode class, which inherits from  {@link com.tsc9526.monalisa.orm.model.Model}
 * 
 * <li><b>modelListener = </b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  A listen class when model changed 
 * 
 * <li><b>validate = false</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  Whether you need to verify the data before you save it
 * 
 * <li><b>validator = </b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The model validator class
 * 
 * <li><b>cache.class = </b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The cache class
 * 
 * <li><b>cache.name = default</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The name of cache setting
 * 
 * <li><b>cache.eviction = LRU</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  The cache eviction's algorithm
 * 
 * <li><b>auto.create_time = create_time</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  When calling Model.save(), auto set model field's value if exists: create_time<br>
 * 
 * <li><b>auto.update_time = update_time </b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  When calling Model.update(), auto set model field's value if exists: update_time<br>
 * 
 * <li><b>auto.create_by = create_by</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  When calling Model.save(), auto set model field's value if exists: create_by<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  You need to set up thread context by call: <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <code>Tx.put(CONTEXT_CURRENT_USERID,"the_operate_user_id");</code>
 * 
 * <li><b>auto.update_by = update_by</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  When calling Model.update(), auto set model field's value if exists: update_by<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  You need to set up thread context by call: <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; <code>Tx.put(CONTEXT_CURRENT_USERID,"the_operate_user_id");</code>
 * 
 * <li><b>exception_if_set_field_not_found = false</b> [scope: TABLE]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;  If true, throw a exception if set a not exists field in a model, otherwise false.
 *
 * </ul>
 * 
 * <br><br>scope:<ul>
 * <li><b>DB</b><br>
 * &nbsp;&nbsp;&nbsp;&nbsp; Meaning: this property is a configuration item for a database scope level 
 * <li><b>TABLE</b><br>
 * &nbsp;&nbsp;&nbsp;&nbsp; Meaning: this property is a configuration item for a table scope level, <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; you can specify a value for each table. for example:<br>
 * <code>
 * &nbsp;&nbsp;&nbsp; DB.cfg.modelClass &nbsp;&nbsp;&nbsp;&nbsp; = MyBaseModelClassName <br>
 * &nbsp;&nbsp;&nbsp; DB.cfg.modelClass.user = MyUserModelClassName <br>
 * </code> 
 * &nbsp;&nbsp;&nbsp;&nbsp; All generated model classes which inherits from MyBaseModelClassName except for model: "user",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; the model: "user" will inherits from MyUserModelClassName 
 * </ul>
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DbProp {
	public static boolean ProcessingEnvironment=false;
	
	static long lastestCfgTime = 0L;
	private static Properties dbCfgProps = null;
	public static Properties getDbCfgProps() {
		return dbCfgProps;
	}
	
	public static Properties setDbCfgProps(Properties dbCfgProps) {
		DbProp.dbCfgProps = dbCfgProps;
		lastestCfgTime    = System.currentTimeMillis();		
		return dbCfgProps;
	}
	
	public final static DbProp PROP_DB_SQL_DEBUG 		= new DbProp("debug",false,"sql.debug");
 	
	public final static DbProp PROP_DB_URL	   			= new DbProp("url");
	public final static DbProp PROP_DB_DRIVER  			= new DbProp("driver");
	public final static DbProp PROP_DB_CATALOG  		= new DbProp("catalog");
	public final static DbProp PROP_DB_SCHEMA   		= new DbProp("schema");
	public final static DbProp PROP_DB_USERNAME 		= new DbProp("username");
	public final static DbProp PROP_DB_PASSWORD 		= new DbProp("password");
	public final static DbProp PROP_DB_DBS 		        = new DbProp("dbs");
	public final static DbProp PROP_DB_DBS_EXCEPTION    = new DbProp("dbs.exception");
	public final static DbProp PROP_DB_DBS_AUTH_USERS   = new DbProp("dbs.auth.users");
	public final static DbProp PROP_DB_TABLES   		= new DbProp("tables");
	public final static DbProp PROP_DB_MAPPING          = new DbProp("mapping");
	public final static DbProp PROP_DB_PARTITIONS       = new DbProp("partitions");			
	public final static DbProp PROP_DB_DATASOURCE_CLASS = new DbProp("datasourceClass");
	
	public final static DbProp PROP_DB_TABLE_TYPE   		  = new DbProp("tableType","TABLE");
	
	public final static DbProp PROP_DB_DATASOURCE_DELAY_CLOSE = new DbProp("datasourceDelayClose",30);
	
	public final static DbProp PROP_DB_HISTORY_DB             = new DbProp("history.db");
	public final static DbProp PROP_DB_HISTORY_PREFIX_TABLE   = new DbProp("history.prefix.table", "history_");
	public final static DbProp PROP_DB_HISTORY_PREFIX_COLUMN  = new DbProp("history.prefix.column","history_");
	public final static DbProp PROP_DB_HISTORY_TABLES         = new DbProp("history.tables");
	
	public final static DbProp PROP_DB_MULTI_RESULTSET_DEEPTH = new DbProp("multi.resultset.deepth",100);
  
	public final static DbProp PROP_DB_CACHE_TABLES	  		  = new DbProp("cache.tables");
	 
	public final static DbProp PROP_TABLE_VERSION_FIELD       = new DbProp("version.name","version");
	 
	public final static DbProp PROP_TABLE_MODEL_CLASS         = new DbProp("modelClass");
	public final static DbProp PROP_TABLE_MODEL_LISTENER      = new DbProp("modelListener");
	
	public final static DbProp PROP_TABLE_VALIDATE 		      = new DbProp("validate",false);
	public final static DbProp PROP_TABLE_VALIDATOR		      = new DbProp("validator");
	public final static DbProp PROP_TABLE_VIEW_INDEXES		  = new DbProp("view.indexes",false);
	
	public final static DbProp PROP_TABLE_SEQ		          = new DbProp("seq");
	public final static DbProp PROP_TABLE_PRIMARY_KEYS		  = new DbProp("primaryKeys");
	
	
	public final static DbProp PROP_TABLE_CACHE_CLASS         = new DbProp("cache.class");
	public final static DbProp PROP_TABLE_CACHE_NAME	  	  = new DbProp("cache.name","default");
	public final static DbProp PROP_TABLE_CACHE_EVICTION      = new DbProp("cache.eviction","LRU");
	 
	public final static DbProp PROP_TABLE_AUTO_SET_CREATE_TIME=new DbProp("auto.create_time","create_time");
	public final static DbProp PROP_TABLE_AUTO_SET_UPDATE_TIME=new DbProp("auto.update_time","update_time");
	public final static DbProp PROP_TABLE_AUTO_SET_CREATE_BY  =new DbProp("auto.create_by","create_by");
	public final static DbProp PROP_TABLE_AUTO_SET_UPDATE_BY  =new DbProp("auto.update_by","update_by");
	  
	public final static DbProp PROP_TABLE_EXCEPTION_IF_SET_FIELD_NOT_FOUND = new DbProp("exception_if_set_field_not_found",false);
	public final static DbProp PROP_TABLE_CLEAR_CHANGES_AFTER_LOAD         = new DbProp("event.load.clear_changes"        ,false);
	
	public final static DbProp PROP_TABLE_DBS_MAX_ROWS  =new DbProp("dbs.max.rows",10000);
	
	
	public static String CFG_FIELD_VERSION        ="$VERSION";
	
	public static String CFG_DATATABLE_KEY_SPLIT  ="&";   
	
	/**
	 * <code>CFG_PATH= ".";</code> <br>
	 * The file path for DB.configFile() is :<br>
	 * <code>System.getProperty("DB@"+DB.key(),CFG_PATH)+"/"+configFile;</code> 
	 */
	public static String CFG_ROOT_PATH          = System.getProperty("monalisa.path",".");
	
	
	/**
	 * Reloadable java directory
	 */
	public static String  CFG_AGENT_PATH        = CFG_ROOT_PATH+"/monalisa/agent";
	public static String  CFG_LIB_PATH          = CFG_ROOT_PATH+"/monalisa/lib";
	 
	public static String TMP_ROOT_PATH          = CFG_ROOT_PATH+"/monalisa/tmp";
	public static String TMP_WORK_DIR_JSP       = TMP_ROOT_PATH+"/_jsp";
	public static String TMP_WORK_DIR_JAVA      = TMP_ROOT_PATH+"/_java";
	public static String TMP_WORK_DIR_METATABLE = TMP_ROOT_PATH+"/_meta";
	public static String TMP_WORK_DIR_GEN       = TMP_ROOT_PATH+"/_gen";
	
	public static int   CFG_RELOAD_CLASS_INTERVAL =15; 
	
	public static int 	CFG_RELOAD_MODEL_INTERVAL =15;
	  
	/**
	 * 默认连接空闲1分钟时，执行保持连接检查的SQL
	 */
	public static int     CFG_CONNECT_IDLE_INTERVALS= 60;
	
	
	public static boolean CFG_LOG_JARLOCATION_DETAIL=true;
	 
	public static String SET_CFG_ROOT_PATH(String cfgRootPath) {
		CFG_ROOT_PATH=cfgRootPath;
		
		CFG_AGENT_PATH  = CFG_ROOT_PATH+"/monalisa/agent";
		CFG_LIB_PATH    = CFG_ROOT_PATH+"/monalisa/lib";
		
		SET_TMP_ROOT_PATH(CFG_ROOT_PATH+"/monalisa/tmp");
		
		return CFG_ROOT_PATH;
	};
	
	public static String SET_TMP_ROOT_PATH(String tmpRootPath){
		TMP_ROOT_PATH=tmpRootPath;
		
		TMP_WORK_DIR_JSP       = TMP_ROOT_PATH+"/_jsp";
		TMP_WORK_DIR_JAVA      = TMP_ROOT_PATH+"/_java";
		TMP_WORK_DIR_METATABLE = TMP_ROOT_PATH+"/_meta";
		TMP_WORK_DIR_GEN       = TMP_ROOT_PATH+"/_gen";
		
		return TMP_ROOT_PATH;
	}
	
	
	private String key;
	private String value;
	
	private Set<String> alias= new HashSet<String>();
	
	public DbProp(String key){
		this.key=key;
	}
	
	public DbProp(String key,String value,String ... alias){
		this.key=key;
		this.value=value;
		
		for(String x:alias) {
			this.alias.add(x);
		}
	}
	
	public DbProp(String key,boolean value,String ... alias){
		this.key=key;
		this.value=value?"true":"false";
		
		for(String x:alias) {
			this.alias.add(x);
		}
	}
	 
	public DbProp(String key,int value,String ... alias){
		this.key=key;
		this.value=""+value;
		
		for(String x:alias) {
			this.alias.add(x);
		}
	}
	
	public String getKey(){
		return key;
	}
	
	public String getFullKey(){
		return DBConfig.PREFIX_DB+"."+DBConfig.CFG_DEFAULT_NAME+"."+key;
	}
	
	public String getFullKey(String configName){
		return DBConfig.PREFIX_DB+"."+configName+"."+key;
	}
	
	public String getValue(DBConfig db){
		String ret = db.getCfg().getProperty(key);
		
		if(ret==null && alias.size()>0 ) {
			for(String aKey:alias) {
				ret = db.getCfg().getProperty(aKey);
				if(ret!=null) {
					break;
				}
			}
		}
		
		if(ret == null) {
			ret =value;
		}
		
		return ret;
	}
	

	public int getIntValue(DBConfig db,int defaultValue){
		String v=getValue(db);
		if(v!=null && v.trim().length()>0){
			return Integer.parseInt(v);
		}else{
			return defaultValue;
		}
	}
	 
	public int getIntValue(DBConfig db,String tableName, int defaultValue){
		if(tableName==null || tableName.length()==0){
			return getIntValue(db,defaultValue);
		}else{
			String v=getValue(db, tableName);
			if(v!=null && v.trim().length()>0){
				return Integer.parseInt(v);
			}else{
				return defaultValue;
			}
		}
	}
	
	public String getValue(DBConfig db,String tableName,String defaultValue){
		String v=getValue(db, tableName);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public String getValue(DBConfig db,String theTableName){
		String tableName=Dialect.getRealname(theTableName);
		String v=db.getCfg().getProperty(key+"."+tableName.toUpperCase());
		if(v!=null){
			return v;
		}else{
			return getValue(db);
		}
	}
	
	
}
