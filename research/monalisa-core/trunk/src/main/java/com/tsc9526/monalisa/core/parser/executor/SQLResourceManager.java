package com.tsc9526.monalisa.core.parser.executor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SQLResourceManager {
	static Log logger=LogFactory.getLog(SQLResourceManager.class.getName());
	
	/**
	 * SQL资源文件缺省放置目录： sql
	 */
	public static String SQL_FILES_ROOT="sql";
	
	private static SQLResourceManager instance;
 
	public static synchronized SQLResourceManager getInstance(){
		if(instance==null){
			instance=new SQLResourceManager();
			instance.loadSqlFiles(new File(SQL_FILES_ROOT) ,".jsp|.mql", true);
		}
		return instance;
	}
	
	private Map<String, SQLClass> sqlClasses=new ConcurrentHashMap<String, SQLClass>();
	 
	
	private SQLResourceManager(){
		
	}
	 
	public Query createQuery(String queryId,Args args){
		String namespace=QueryPackage.DEFAULT_PACKAGE_NAME+"."+QueryPackage.DEFAULT_CLASS_NAME;
		String id=queryId;
		
		int x=queryId.lastIndexOf(".");
		if(x>0){
			namespace=queryId.substring(0,x).trim();
			id=queryId.substring(x+1).trim();
		}
		
		SQLClass clazz=sqlClasses.get(namespace);
		if(clazz!=null){
			return clazz.createQuery(id, args);
		}else{
			throw new RuntimeException("Query namespace not found: "+namespace+", Exist namespace: "+sqlClasses.keySet());
		}
	}
	
	
	public synchronized void loadSqlFiles(File sqlFile,String exts,boolean recursive){
		String name=sqlFile.getName();
		
		boolean matched=false;
		for(String s:exts.split("\\|")){
			s=s.trim();
			if(name.endsWith(s)){
				matched=true;
				break;
			}
		}
		
		if(sqlFile.isFile() && matched ){
			addSqlFile(sqlFile);
		}else if(sqlFile.isDirectory() && recursive){
			for(File f:sqlFile.listFiles()){
				loadSqlFiles(f,exts,recursive);
			}
		}
	}
	
	Map<String, SQLClass> getSqlClasses(){
		return this.sqlClasses;
	}
	
	private void addSqlFile(File sqlFile){
		logger.info("Load sql file: "+sqlFile.getAbsolutePath());
		
		SQLClass sqlClass=new SQLClass(sqlFile);
		try{
			sqlClass.compile();
		}catch(Exception e){
			sqlClass.close();
			
			throw new RuntimeException("Compile sql file fail: "+sqlFile+"\r\n"+e.getMessage(),e);
		}
		
		String namepace=sqlClass.getPackageName()+"."+sqlClass.getClassName();
		if(!sqlClasses.containsKey(namepace)){
			sqlClasses.put(namepace,sqlClass);
		}else{
			sqlClass.close();
			
			throw new RuntimeException("Namespace: "+namepace+" exists: "+sqlFile.getAbsolutePath()+", "+sqlClass.getSqlFile().getAbsolutePath());
		}
	}
	
}
