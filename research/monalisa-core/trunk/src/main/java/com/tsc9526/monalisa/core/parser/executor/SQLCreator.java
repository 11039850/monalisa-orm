package com.tsc9526.monalisa.core.parser.executor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;


public class SQLCreator {
	private static SQLCreator instance=new SQLCreator();
	
	public static SQLCreator getInstance(){
		return instance;
	}
	
	private Map<String, SQLClass> sqlClasses=new ConcurrentHashMap<String, SQLClass>();
	
	private SQLCreator(){
	}
	
	public Query createQuery(String queryId,Args args){
		String packageName=SQLClass.DEFAULT_PACKAGE_NAME;
		String id=queryId;
		
		int x=queryId.lastIndexOf(".");
		if(x>0){
			packageName=queryId.substring(0,x).trim();
			id=queryId.substring(x+1).trim();
		}
		
		SQLClass clazz=sqlClasses.get(packageName);
		if(clazz!=null){
			return clazz.createQuery(id, args);
		}else{
			throw new RuntimeException("Query package not found: "+packageName+", Exists query packages: "+sqlClasses.keySet());
		}
	}
	
	public synchronized void loadSqlFiles(File sqlFile,String ext,boolean recursive){
		if(ext!=null && ext.startsWith(".")){
			ext=ext.substring(1);
		}
		
		if(sqlFile.isFile() && (ext==null || sqlFile.getName().endsWith("."+ext)) ){
			addSqlFile(sqlFile);
		}else if(sqlFile.isDirectory() && recursive){
			for(File f:sqlFile.listFiles()){
				loadSqlFiles(f,ext,recursive);
			}
		}
	}
	
	private void addSqlFile(File sqlFile){
		try{
			SQLClass sqlClass=new SQLClass(sqlFile);
			sqlClass.compile();
			
			sqlClasses.put(sqlClass.getPackageName(),sqlClass);
		}catch(Exception e){
			throw new RuntimeException("Compile sql file fail: "+sqlFile+"\r\n"+e.getMessage(),e);
		}
	}
	
}
