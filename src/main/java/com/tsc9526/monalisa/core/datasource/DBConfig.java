package com.tsc9526.monalisa.core.datasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.Properties;

import javax.sql.DataSource;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

public class DBConfig implements com.tsc9526.monalisa.core.annotation.DB{ 	 
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
	private String partitionTables;
	private String mapping;		
	private String configFile;
	private String configName;
 	 
	private Properties p=new Properties();
	
	DBConfig(String key,DB db){	 
		this.db=db;
		this.key=key;
		
		init(this.db);
	}
	 
	public void init(DB db){
		this.db=db;
		
		configFile=db.configFile();
		
		if(configFile!=null && configFile.trim().length()>0){
			configFile=configFile.trim();
			
			if(configFile.startsWith("/")==false){
				if(configFile.length()>1 && configFile.charAt(1)==':'){
					//Windows ROOT C: D: E: ...
				}else{
					configFile=System.getProperty("DB@"+key,DEFAULT_PATH)+"/"+configFile;
				}				
			}
			
			File file=new File(configFile);
			if(file.exists()){
				System.out.println("Load DB("+key+") config from: "+file.getAbsolutePath());
				
				try{
					InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"utf-8");
					p.load(reader);
					reader.close();
				}catch(IOException e){
					throw new RuntimeException("Load db config file: "+file.getAbsolutePath()+", exception: "+e,e);
				}
			}else{
				throw new RuntimeException("DB config file: "+file.getAbsolutePath()+" not found!");
			}
		}		
		
		this.configName      = db.configName();
		
		String[] prefixs=new String[]{"DB"};		
		if(configName!=null && configName.trim().length()>0){
			prefixs=new String[]{"DB."+configName.trim(), "DB"};
		}
		
		this.modelClass      = getValue(p,"modelClass", db.modelClass(),prefixs);
		this.datasourceClass = getValue(p,"datasourceClass", db.datasourceClass(),prefixs);
		this.url             = getValue(p,"url", db.url(),prefixs);
		this.driver          = getValue(p,"driver", db.driver(),prefixs);
		this.catalog         = getValue(p,"catalog", db.catalog(),prefixs);
		this.schema          = getValue(p,"schema", db.schema(),prefixs);
		this.username        = getValue(p,"username", db.username(),prefixs);
		this.password        = getValue(p,"password", db.password(),prefixs);
		this.tables          = getValue(p,"tables", db.tables(),prefixs);
		this.partitionTables = getValue(p,"partitionTables", db.partitionTables(),prefixs);		 
		this.mapping         = getValue(p,"mapping", db.mapping(),prefixs);				 
	}
	
	private String getValue(Properties p,String key,String defaultValue,String[] prefixs){
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
	
	
	private DataSource ds;
	public synchronized DataSource getDataSource(){
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
		return this.modelClass;
	}
	
	public String datasourceClass(){
		return this.datasourceClass;
	}
 
	public String url(){
		return this.url;
	}	
	 
	public String driver(){
		return this.driver;
	}
	
	public String catalog(){
		return this.catalog;
	}
	
	public String schema(){
		return this.schema;
	}
   
	public String username() {		 
		return this.username;
	}
	
	public String password(){
		return this.password;
	}
		 
	public String tables(){
		return this.tables;
	}
	
	 
	public String mapping(){
		return this.mapping;
	}	
  
	public String partitionTables(){
		return this.partitionTables;
	}
	
	public DB getDb() {
		return db;
	} 
	
	public String key() {
		return key;
	} 
	 
	public String configName() {
		return this.configName;
	}
 
	public String configFile(){
		return this.configFile;
	} 
	
	public String getProperty(String key){
		return getProperty(key,null);
	}
	
	public String getProperty(String key,String defaultValue){
		return p.getProperty(key, defaultValue);
	}

	 
	public Class<? extends Annotation> annotationType() {		 
		return DB.class;
	}
 	
}
