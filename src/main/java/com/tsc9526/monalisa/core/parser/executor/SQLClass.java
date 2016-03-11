package com.tsc9526.monalisa.core.parser.executor;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

public class SQLClass implements Closeable{
	static Log logger=LogFactory.getLog(SQLClass.class.getName());
	
	public static String WORK_DIR  ="work";
	
	public final static String DEFAULT_PACKAGE_NAME="DEFAULT";
	public final static String SQL_CLASS_NAME="__SQL__";
	 
	private URLClassLoader loader;
	private String packageName;
	
	private Object runObject;
	private Map<String, Method> hQueryMethods=new ConcurrentHashMap<String, Method>();
	
	private File sqlFile;
	private long lastModified;
	
	public Query createQuery(String queryId,Args args){
		checkAndCompile();
		
		Method m=hQueryMethods.get(queryId);
		if(m==null){
			throw new RuntimeException("Query id: "+queryId+" not found: "+hQueryMethods.keySet());
		}
		
		try{
			Query query=new Query();
			m.invoke(runObject, query,args);
			return query;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
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
		if(packageName==null){
			packageName=DEFAULT_PACKAGE_NAME;
			pkg.setPackageName(DEFAULT_PACKAGE_NAME);
		}
		
		String dirSrc    =WORK_DIR+"/"+packageName+"/src/"+packageName.replace(".","/");
		String dirClasses=WORK_DIR+"/"+packageName+"/classes";
		
		FileHelper.mkdirs(dirSrc);
		File classes=FileHelper.mkdirs(dirClasses);
		
		JavaWriter writer=JavaWriter.getBufferedWriter(1);
		pkg.write(writer);
		 
		FileHelper.write(new File(dirSrc,SQL_CLASS_NAME+".java"),writer.getContent().getBytes("utf-8"));

		ByteArrayOutputStream debug=new ByteArrayOutputStream();
		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		String classpath = System.getProperty("java.class.path");
		int r=javac.run(System.in, debug,debug, "-encoding", "utf-8","-nowarn"
				,"-classpath",classpath
				,"-d",dirClasses
				,dirSrc+"/"+SQL_CLASS_NAME+".java");
		
		if(r==0){
			logger.info("Compile OK: "+dirSrc+"/"+SQL_CLASS_NAME+".java");
			 
			close();
			
			loader=new URLClassLoader(new URL[]{classes.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
			Class<?> qClazz=loader.loadClass(packageName+"."+SQL_CLASS_NAME);
			
			for(Method m:qClazz.getMethods()){
				Class<?>[] pTypes= m.getParameterTypes();
				if(pTypes.length==2 && pTypes[0]==Query.class && pTypes[1]==Args.class){
					hQueryMethods.put(m.getName(),m);
				}
			}
			
			runObject=qClazz.newInstance();
		
			logger.info("Loaded query package:"+packageName+", id: "+hQueryMethods.keySet());
		}else{
			throw new RuntimeException("Compile fail: "+dirSrc+"/"+SQL_CLASS_NAME+".java\r\n"+new String(debug.toByteArray()));
		}
	}
	
	public void close(){
		if(loader!=null){
			CloseQuietly.close(loader);
			loader=null;
		}
		
		runObject=null;
		hQueryMethods.clear();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public long getLastModified() {
		return lastModified;
	}
}
