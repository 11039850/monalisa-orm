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

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.generator.DBGenerator;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.parser.query.QueryStatement;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SQLClass implements Closeable{
	static Logger logger=Logger.getLogger(SQLClass.class.getName());
	
	public static String PACKAGE_PREFIX="_sql";
	
	private String packageName;
	private String className;
	
	private transient QueryLoader queryLoader;
	 
	private File sqlFile;
	private long lastModified;
	
	public Query createQuery(String id,Args args){
		checkAndCompile();
		
		return queryLoader.createQuery(id, args);
	}
	
	public Collection<QueryStatement> getStatements(){
		return queryLoader.hQueryStatements.values();
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
		 
		String workDir  =FileHelper.combinePath(DBConfig.DEFAULT_PATH,DBGenerator.PROJECT_TMP_PATH+"/sqlfile");
		
		String dirSrc    =workDir+"/"+packageName+"."+className+"/src/"+PACKAGE_PREFIX+"/"+packageName.replace(".","/");
		String dirClasses=workDir+"/"+packageName+"."+className+"/classes";
		
		FileHelper.mkdirs(dirSrc);
		File classes=FileHelper.mkdirs(dirClasses);
		
		File classFile=new File(workDir+"/"+packageName+"."+className+"/classes/"+PACKAGE_PREFIX+"/"+packageName.replace(".","/")+"/"+className+".class");
		if(!classFile.exists() || classFile.lastModified()<lastModified){
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
				logger.info("Compile OK: "+dirSrc+"/"+className+".java");
			}else{
				throw new RuntimeException("Compile fail: "+dirSrc+"/"+className+".java\r\n"+new String(debug.toByteArray()));
			}
		}else{
			logger.info("Load OK: "+classFile.getAbsolutePath());
		}
		 
		Map<String, QueryStatement> hQueryStatements=new LinkedHashMap<String, QueryStatement>();
		for(QueryStatement qs:pkg.getStatements()){	
			hQueryStatements.put(qs.getId(), qs);
		}
		
		URLClassLoader loader=new URLClassLoader(new URL[]{classes.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
		Class<?> qClazz=loader.loadClass(PACKAGE_PREFIX+"."+packageName+"."+className);
		  
		for(Method m:qClazz.getMethods()){
			Class<?>[] pTypes= m.getParameterTypes();
			if(pTypes.length==2 && pTypes[0]==Query.class && pTypes[1]==Args.class){
				hQueryStatements.get(m.getName()).setMethod(m);
			}
		}
		
		Object runObject=qClazz.newInstance();
	
		if(this.queryLoader!=null){
			CloseQuietly.delayClose(this.queryLoader, 15);
		}
		
		this.queryLoader=new QueryLoader(runObject, loader, hQueryStatements);
		
		logger.info("Loaded namespace: "+packageName+"."+className+", id: "+hQueryStatements.keySet());	
		 
	}
	
	public void close(){
		CloseQuietly.close(this.queryLoader);
		this.queryLoader=null;
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
	
	public class QueryLoader implements Closeable{
		private Object runObject;
		private URLClassLoader loader;
		private Map<String, QueryStatement> hQueryStatements;
		
		QueryLoader(Object runObject,URLClassLoader loader,Map<String, QueryStatement> hQueryStatements){
			this.runObject=runObject;
			this.loader=loader;
			this.hQueryStatements=hQueryStatements;
		}
		
		Query createQuery(String id,Args args){
			Method m=hQueryStatements.get(id).getMethod();
			if(m==null){
				throw new RuntimeException("Query id: "+id+" not found: "+queryLoader.hQueryStatements.keySet());
			}
			
			try{
				Query query=new Query();
				m.invoke(queryLoader.runObject, query,args);
				return query;
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		public void close(){
			hQueryStatements.clear();
			CloseQuietly.close(loader);
		
			loader=null;
			runObject=null;
		}
	}
}
