package com.tsc9526.monalisa.core.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.dialect.MysqlDialect;

public class DataSourceManager {
	private static DataSourceManager dm=new DataSourceManager();
	
	public static DataSourceManager getInstance(){
		return dm;
	}	
	
	private Map<String, DBConfig> dss=new ConcurrentHashMap<String,DBConfig>();
	
	private Map<String, Dialect> dialects=new ConcurrentHashMap<String,Dialect>();
	
	
	private DataSourceManager(){		
		registerDialect(new MysqlDialect());
	}	
	
	@SuppressWarnings("unchecked")
	public void registerDialect(String dialectClass)throws Exception{
		Class<Dialect> cd=(Class<Dialect>)Class.forName(dialectClass);
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
	
	public  DBConfig getDBConfig(TypeElement typeElement,boolean reInit) {
		DB db=typeElement.getAnnotation(DB.class);
		if(db==null){
			throw new RuntimeException("TypeElement without @DB: "+typeElement.toString());
		}
		
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=typeElement.toString();
		}
		
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
			dss.put(dbKey, cfg);
		}else if(cfg!=null && cfg.isCfgFileChanged()){
			dss.remove(dbKey);			 
			cfg=null;
			
			cfg=new DBConfig(dbKey,db);			 
			dss.put(dbKey, cfg);	
		}
		
		return cfg;
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
		  
		String jdbcUrl=db.url();
		
		for(String key:dialects.keySet()){
			if(jdbcUrl.startsWith(key)){
				return dialects.get(key);
			}
		}		
		throw new RuntimeException("JDBC-URL: "+jdbcUrl+", dialect not found in: "+dialects.keySet()+", use registerDialect() first!");
	}
	
	public void shutdown(){
		try{
			for(DBConfig cfg:dss.values()){
				cfg.close();
			}
		}finally{
			dss.clear();
		}
	}
	
}
