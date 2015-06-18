package com.tsc9526.monalisa.core.datasource;

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
import com.tsc9526.monalisa.core.tools.CloseQuietly;

public class DBConfig implements com.tsc9526.monalisa.core.annotation.DB{ 	
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
	
	
	private DBConfig(){
	}
	
	public DBConfig(String key,DB db){	 
		this.db=db;
		this.key=key;
		
		init(this.db);
	}
	 
	public void init(DB db){
		this.db=db;
		
		loadCfgFromFile();	
		
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
		this.partitions      = getValue(p,"partitions", db.partitions(),prefixs);	
		this.modelListener   = getValue(p,"modelListener", db.modelListener(),prefixs);
		this.mapping         = getValue(p,"mapping", db.mapping(),prefixs);			
			
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
					p.clear();
					p.load(reader);
					reader.close();
				}catch(IOException e){
					throw new RuntimeException("Load db config file: "+file.getAbsolutePath()+", exception: "+e,e);
				}
			}else{
				throw new RuntimeException("DB config file: "+file.getAbsolutePath()+" not found!");
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
		return this.dbHosts;
	}
	 		
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
  
	public String partitions(){
		return this.partitions;
	}
	
	public String modelListener(){
		return this.modelListener;
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
									 
			cfg.db=DBConfig.this.db;
			cfg.key=DBConfig.this.key;
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
			
			cfg.url=URL;
		}
		
		public DBConfig getConfig(){
			return cfg;
		}
	}
}
