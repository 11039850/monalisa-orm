package com.tsc9526.monalisa.core.parser.executor;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.parser.query.QueryStatement;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

public class SQLClass implements Closeable{
	static Log logger=LogFactory.getLog(SQLClass.class.getName());
	
	public static String WORK_DIR  ="target/monalisa/sqlfile";
	public static String PACKAGE_PREFIX="_sql";
	
	private URLClassLoader loader;
	private String packageName;
	private String className;
	
	private Object runObject;
	 
	private Map<String, QueryStatement> hQueryStatements=new LinkedHashMap<String, QueryStatement>();
	
	private File sqlFile;
	private long lastModified;
	
	public Query createQuery(String id,Args args){
		checkAndCompile();
		
		Method m=hQueryStatements.get(id).getMethod();
		if(m==null){
			throw new RuntimeException("Query id: "+id+" not found: "+hQueryStatements.keySet());
		}
		
		try{
			Query query=new Query();
			m.invoke(runObject, query,args);
			return query;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Collection<QueryStatement> getStatements(){
		return hQueryStatements.values();
	}

	public SQLClass(File sqlFile){
		this.sqlFile=sqlFile;
	}
	
	protected synchronized void checkAndCompile(){
		try{
			if(lastModified<sqlFile.lastModified()){
				compile();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public synchronized void compile()throws Exception{
		lastModified=sqlFile.lastModified();
		
		Jsp jsp=new Jsp(sqlFile);
	 
		
		QueryPackage pkg=new QueryPackage(jsp);
		packageName=pkg.getPackageName();
		className=pkg.getClassName();
		 
		String dirSrc    =WORK_DIR+"/"+packageName+"."+className+"/src/"+PACKAGE_PREFIX+"/"+packageName.replace(".","/");
		String dirClasses=WORK_DIR+"/"+packageName+"."+className+"/classes";
		
		FileHelper.mkdirs(dirSrc);
		File classes=FileHelper.mkdirs(dirClasses);
		
		JavaWriter writer=JavaWriter.getBufferedWriter(1);
		pkg.write(writer);
		 
		FileHelper.write(new File(dirSrc,className+".java"),writer.getContent().getBytes("utf-8"));

		ByteArrayOutputStream debug=new ByteArrayOutputStream();
		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		String classpath = System.getProperty("java.class.path");
		int r=javac.run(System.in, debug,debug, "-encoding", "utf-8","-nowarn"
				,"-classpath",classpath
				,"-d",dirClasses
				,dirSrc+"/"+className+".java");
		
		if(r==0){
			logger.info("Load OK: "+dirSrc+"/"+className+".java");
			 
			close();
			
			for(QueryStatement qs:pkg.getStatements()){	
				hQueryStatements.put(qs.getId(), qs);
			}
			
			loader=new URLClassLoader(new URL[]{classes.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
			Class<?> qClazz=loader.loadClass(PACKAGE_PREFIX+"."+packageName+"."+className);
			  
			for(Method m:qClazz.getMethods()){
				Class<?>[] pTypes= m.getParameterTypes();
				if(pTypes.length==2 && pTypes[0]==Query.class && pTypes[1]==Args.class){
					hQueryStatements.get(m.getName()).setMethod(m);
				}
			}
			
			runObject=qClazz.newInstance();
		
			logger.info("Loaded namespace: "+packageName+"."+className+", id: "+hQueryStatements.keySet());
		}else{
			throw new RuntimeException("Compile fail: "+dirSrc+"/"+className+".java\r\n"+new String(debug.toByteArray()));
		}
	}
	
	public void close(){
		if(loader!=null){
			CloseQuietly.close(loader);
			loader=null;
		}
		
		runObject=null;
		hQueryStatements.clear();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName(){
		return this.className;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
	public File getSqlFile(){
		return sqlFile;
	}
}
