package com.tsc9526.monalisa.core.parser.executor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.tsc9526.monalisa.core.parser.query.QueryStatement;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.JavaWriter;


public class SQLResourceManager {
	private static SQLResourceManager instance=new SQLResourceManager();
	
	public static SQLResourceManager getInstance(){
		return instance;
	}
	
	private Map<String, SQLClass> sqlClasses=new ConcurrentHashMap<String, SQLClass>();
	
	private SQLResourceManager(){
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
	
	public void writeQueryIdToClass(String path){
		try{
			JavaWriter writer=new JavaWriter(new File(path));
			writer.write("package resouces;\r\n");
			
			writer.write("public interface Resource{\r\n");
			for(SQLClass cs:sqlClasses.values()){
				for(QueryStatement qs:cs.getStatements()){
					String idName =cs.getPackageName()+"$"+qs.getId();
					String idValue=cs.getPackageName()+"."+qs.getId();
					List<String> args =qs.getArgs();
				}
			}
			writer.write("}");
			 
			writer.close();
		}catch(Exception e){
			throw new RuntimeException(e);
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
