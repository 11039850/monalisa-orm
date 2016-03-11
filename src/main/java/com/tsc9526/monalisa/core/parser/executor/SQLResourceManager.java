package com.tsc9526.monalisa.core.parser.executor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.parser.query.QueryStatement;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;


public class SQLResourceManager {
	static Log logger=LogFactory.getLog(SQLResourceManager.class.getName());
	
	private static SQLResourceManager instance;
	
	private static File defaultSQLFiles=new File("sql");
	
	public static synchronized SQLResourceManager getInstance(){
		if(instance==null){
			instance=new SQLResourceManager();
			instance.loadSqlFiles(defaultSQLFiles , ".jsp", true);
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
	
	public void writeQueryClass(String srcDir){
		try{
			
			for(SQLClass cs:sqlClasses.values()){
				File dir=new File(srcDir,cs.getPackageName().replace(".","/"));
				FileHelper.mkdirs(dir);
				
				File java=new File(dir,cs.getClassName()+".java");
				logger.info("Parse "+cs.getSqlFile().getAbsolutePath()+" to "+java.getAbsolutePath());
				JavaWriter writer=new JavaWriter(java);
				
				writer.write("package "+cs.getPackageName()+";\r\n\r\n");
				writer.write("import "+Query.class.getName()+";\r\n\r\n");
				writer.write("public class "+cs.getClassName()+"{\r\n");
				for(QueryStatement qs:cs.getStatements()){
					String xs1="", xs2="";
					List<String> args =qs.getArgs();
					for(String s:args){
						int x=s.indexOf("=");
						String v1=s.substring(0,x).trim();
						String v2=v1.split("\\s+")[1];
						
						if(xs1.length()>0){
							xs1+=", ";
						}
						xs1+=v1;
						
						xs2+=","+v2;
					}
					
					
					String queryId=cs.getPackageName()+"."+cs.getClassName()+"."+qs.getId();
					
					String comments=qs.getComments();
					if(comments!=null){
						comments=comments.replace("*/", "==");
					}
					if(comments!=null){
						writer.write("\t/**\r\n");
						writer.write(comments);
						writer.write("\t*/\r\n");
					}
					writer.write("\tpublic final static String "+qs.getId()+"=\""+queryId+"\";\r\n");
					
					if(comments!=null){
						writer.write("\t/**\r\n");
						writer.write(comments);
						writer.write("\t*/\r\n");
					}
					writer.write("\tpublic static Query "+qs.getId()+"("+xs1+"){\r\n");
					writer.write("\t\t return Query.create("+qs.getId()+xs2+"); \r\n");
					
					writer.write("\t}\r\n\r\n");
				}
				writer.write("}");
				 
				writer.close();
			}
		
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
