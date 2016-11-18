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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.dialect.MysqlDialect;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataSourceManager {
	static Logger logger=Logger.getLogger(DataSourceManager.class);
	
	private static DataSourceManager dm=new DataSourceManager();
	
	public static DataSourceManager getInstance(){
		return dm;
	}	
	
	public static void shutdown(){
		try{
			DBTasks.shutdown();
			
			for(DBConfig cfg:dm.dss.values()){
				cfg.close();
			}
			
			shutdownMysqlThreads();
		}finally{
			dm.dss.clear();
		}
	}
	
	private static void shutdownMysqlThreads(){
		try {
		    Class<?> cls=ClassHelper.forClassName("com.mysql.jdbc.AbandonedConnectionCleanupThread");
		    Method   mth=(cls==null ? null : cls.getMethod("shutdown"));
		    if(mth!=null) {
		        mth.invoke(null);
		    }
		}catch(ClassNotFoundException e){
			//Do nothing
		}catch (Throwable t) {
			logger.error(""+t,t);
		}
	}
	
	private Map<String, DBConfig> dss=new ConcurrentHashMap<String,DBConfig>();
	
	private Map<String, Dialect> dialects=new ConcurrentHashMap<String,Dialect>();
	
	
	private DataSourceManager(){		
		registerDialect(new MysqlDialect());
	}	
	
	@SuppressWarnings("unchecked")
	public void registerDialect(String dialectClass)throws Exception{
		Class<Dialect> cd=(Class<Dialect>)ClassHelper.forClassName(dialectClass);
		registerDialect(cd.newInstance());
	}
	
	public void registerDialect(Dialect dialect){
		dialects.put(dialect.getUrlPrefix(), dialect);
	}
	
	public void unregisterDialect(Dialect dialect){
		dialects.remove(dialect.getUrlPrefix());
	}
	
	public  DBConfig getDBConfig(Class<?> clazzWithDBAnnotation) {
		DB db=clazzWithDBAnnotation.getAnnotation(DB.class);
		if(db==null){
			throw new RuntimeException("Class without @DB: "+clazzWithDBAnnotation);
		}
		
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=clazzWithDBAnnotation.getName();
		}
		
		return getDBConfig(dbKey,db);
	}
	
	public DBConfig getDBConfig(String dbKey,DB db,boolean reInit) {		  		
		DBConfig cfg=dss.get(dbKey);
		if(cfg==null){			
			cfg=getDBConfig(dbKey,db);
		}else if(reInit){
			cfg.init(db);			
		}
		return cfg;
	}
 
	
	public synchronized DBConfig getDBConfig(String dbKey,DB db){
		DBConfig cfg=dss.get(dbKey);
		
		if(cfg==null && db!=null){
			cfg=new DBConfig(dbKey,db);
			putDBConfig(dbKey, cfg);
		}
		
		return cfg;
	}
	
	void putDBConfig(String dbKey,DBConfig cfg){
		dss.put(dbKey, cfg);
	}
	
	public DataSource getDataSource(DBConfig db){
		if(db==null){
			throw new RuntimeException("DB cannot be null");
		}
	 
		return db.getDataSource();
	}
	
	public Dialect getDialect(DBConfig db){
		if(db==null){
			return null;
		}
		  
		String jdbcUrl=db.getCfg().getUrl();

		return getDialect(jdbcUrl);
	}
	
	public Dialect getDialect(String jdbcUrl){
		if(jdbcUrl==null){
			return null;
		}
		
		for(String key:dialects.keySet()){
			if(jdbcUrl.startsWith(key)){
				return dialects.get(key);
			}
		}		
		throw new RuntimeException("JDBC-URL: "+jdbcUrl+", dialect not found in: "+dialects.keySet()+", use registerDialect() first!");
	}
	
}
