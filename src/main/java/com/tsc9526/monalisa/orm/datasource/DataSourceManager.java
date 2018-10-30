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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.dialect.MysqlDialect;
import com.tsc9526.monalisa.orm.dialect.OracleDialect;
import com.tsc9526.monalisa.orm.dialect.PostgresDialect;
import com.tsc9526.monalisa.orm.dialect.SQLServerDialect;
import com.tsc9526.monalisa.tools.Tasks;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.logger.Logger;

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
	 
	/**
	 * close data source, daemon thread ...
	 */
	public static void shutdown(){
		Tasks.instance.destory();
	}
	 
	private Map<String, DBConfig> dss=new ConcurrentHashMap<String,DBConfig>();
	
	private Map<String, Dialect> dialects=new ConcurrentHashMap<String,Dialect>();
	
	
	private DataSourceManager(){
		registerShutdown();
		
		registerDialect(new MysqlDialect());
		registerDialect(new SQLServerDialect());
		registerDialect(new OracleDialect());
		registerDialect(new PostgresDialect());
	}
	
	private void registerShutdown(){
		Tasks.instance.addShutdown(new Runnable() {
			public void run(){
				Set<String> jdbsURLs=new LinkedHashSet<String>();
				jdbsURLs.add("jdbc:relique:csv:monalisa-memory");
				
				for(DBConfig cfg:dss.values()){
					String url=cfg.getCfg().getUrl();
					jdbsURLs.add(url);
					
					cfg.close();
				}
				
				for(String url:jdbsURLs){
					try{
						DriverManager.deregisterDriver(DriverManager.getDriver(url));
					}catch(SQLException e){
						MelpClose.close(e);
					}
				}
				 
				shutdownMysqlThreads();
			}
		});
	}
	
	private void shutdownMysqlThreads(){
		try {
		    Class<?> cls=MelpClass.forName("com.mysql.jdbc.AbandonedConnectionCleanupThread");
		    Method   mth=(cls==null ? null : cls.getMethod("shutdown"));
		    if(mth!=null) {
		        mth.invoke(null);
		    }
		}catch(ClassNotFoundException e){
			MelpClose.close(e);
		}catch (Exception t) {
			logger.error(""+t,t);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void registerDialect(String dialectClass)throws Exception{
		Class<Dialect> cd=(Class<Dialect>)MelpClass.forName(dialectClass);
		registerDialect(cd.newInstance());
	}
	
	public void registerDialect(Dialect dialect){
		dialects.put(dialect.getUrlPrefix(), dialect);
	}
	
	public void unregisterDialect(Dialect dialect){
		dialects.remove(dialect.getUrlPrefix());
	}
	
	public  DBConfig getDBConfig(Class<?> clazzWithDBAnnotation) {
		Class<?> dbClass=MelpClass.findClassWithAnnotation(clazzWithDBAnnotation, DB.class);
		if(dbClass==null){
			throw new RuntimeException("Class without @DB: "+clazzWithDBAnnotation);
		}
		
		DB db=dbClass.getAnnotation(DB.class);
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=dbClass.getName();
		}
		
		return getDBConfig(dbKey,db,dbClass);
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
	
	public DBConfig getDBConfig(String dbKey,DB db,String cfgBasePath) {		  		
		DBConfig cfg=dss.get(dbKey);
		if(cfg==null){			
			cfg=getDBConfig(dbKey,db,(Class<?>)null);
		}
		cfg._cfg.cfgBasePath=cfgBasePath;
		return cfg;
	}
 
	public DBConfig getDBConfig(String dbKey,DB db){
		return getDBConfig(dbKey,db,(Class<?>)null);
	}
	
	public synchronized DBConfig getDBConfig(String dbKey,DB db,Class<?> clazzWithDBAnnotation){
		DBConfig cfg=dss.get(dbKey);
		
		if(cfg==null && db!=null){
			cfg=new DBConfig(dbKey,db,clazzWithDBAnnotation);
			putDBConfig(dbKey, cfg);
		}		
		return cfg;
	}
	
	public DBConfig findDbConfig(String dbKey){
		return dss.get(dbKey);
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
