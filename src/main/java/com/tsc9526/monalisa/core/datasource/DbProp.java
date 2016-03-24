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

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DbProp {
	public final static DbProp PROP_DB_SQL_DEBUG 		= new DbProp("sql.debug",false);
	public final static DbProp PROP_DB_SQL_PATH 		= new DbProp("sql.path","sql");
	
	
	public final static DbProp PROP_DB_URL	   			= new DbProp("url");
	public final static DbProp PROP_DB_DRIVER  			= new DbProp("driver");
	public final static DbProp PROP_DB_CATALOG  		= new DbProp("catalog");
	public final static DbProp PROP_DB_SCHEMA   		= new DbProp("schema");
	public final static DbProp PROP_DB_USERNAME 		= new DbProp("username");
	public final static DbProp PROP_DB_PASSWORD 		= new DbProp("password");
	public final static DbProp PROP_DB_TABLES   		= new DbProp("tables");
	public final static DbProp PROP_DB_MAPPING          = new DbProp("mapping");
	public final static DbProp PROP_DB_PARTITIONS       = new DbProp("partitions");			
	public final static DbProp PROP_DB_DATASOURCE_CLASS = new DbProp("datasourceClass");
	
	public final static DbProp PROP_DB_DATASOURCE_DELAY_CLOSE = new DbProp("datasourceDelayClose",30);
	
	public final static DbProp PROP_DB_HISTORY_DB             = new DbProp("history.db");
	public final static DbProp PROP_DB_HISTORY_PREFIX_TABLE   = new DbProp("history.prefix.table", "history_");
	public final static DbProp PROP_DB_HISTORY_PREFIX_COLUMN  = new DbProp("history.prefix.column","history_");
	public final static DbProp PROP_DB_HISTORY_TABLES         = new DbProp("history.tables");
  
	
	public final static DbProp PROP_TABLE_MODEL_CLASS   =new DbProp("modelClass");
	public final static DbProp PROP_TABLE_MODEL_LISTENER=new DbProp("modelListener");
	
	public final static DbProp PROP_TABLE_VALIDATE 		=new DbProp("validate",false);
	public final static DbProp PROP_TABLE_VALIDATOR		=new DbProp("validator");	
	 
	public final static DbProp PROP_TABLE_AUTO_SET_CREATE_TIME=new DbProp("auto.create_time","create_time");
	public final static DbProp PROP_TABLE_AUTO_SET_UPDATE_TIME=new DbProp("auto.update_time","update_time");
	public final static DbProp PROP_TABLE_AUTO_SET_CREATE_BY  =new DbProp("auto.create_by","create_by");
	public final static DbProp PROP_TABLE_AUTO_SET_UPDATE_BY  =new DbProp("auto.update_by","update_by");
	
	public final static DbProp PROP_TABLE_EXCEPTION_IF_SET_FIELD_NOT_FOUND=new DbProp("exception_if_set_field_not_found",false);
	
	
	private String key;
	private String value;
	public DbProp(String key){
		this.key=key;
	}
	
	public DbProp(String key,String value){
		this.key=key;
		this.value=value;
	}
	
	public DbProp(String key,boolean value){
		this.key=key;
		this.value=value?"true":"false";
	}
	
	public DbProp(String key,int value){
		this.key=key;
		this.value=""+value;
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
		return db.getCfg().getProperty(key, value);
	}
	

	public int getIntValue(DBConfig db,int defaultValue){
		String v=db.getCfg().getProperty(key, value);
		if(v!=null && v.trim().length()>0){
			return Integer.parseInt(v);
		}else{
			return defaultValue;
		}
	}
	
	public String getValue(DBConfig db,String tableName){
		String v=db.getCfg().getProperty(key+"."+tableName);
		if(v!=null){
			return v;
		}else{
			return db.getCfg().getProperty(key, value);
		}
	}
	
	

	public int getIntValue(DBConfig db,String tableName, int defaultValue){
		String v=getValue(db, tableName);
		if(v!=null && v.trim().length()>0){
			return Integer.parseInt(v);
		}else{
			return defaultValue;
		}
	}
}
