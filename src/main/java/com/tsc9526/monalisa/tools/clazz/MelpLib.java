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
package com.tsc9526.monalisa.tools.clazz;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tsc9526.monalisa.orm.datasource.C3p0DataSource;
import com.tsc9526.monalisa.orm.datasource.DruidDataSource;
import com.tsc9526.monalisa.tools.PkgNames;
import com.tsc9526.monalisa.tools.agent.AgentEnhancer;
import com.tsc9526.monalisa.tools.csv.Csv;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.maven.JarLocation;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpLib {
	private static Logger logger=Logger.getLogger(MelpLib.class);
	
	public final static String libGsonClass      = "com.google.gson.Gson";
	
	public final static String libCglibClass     = "net.sf.cglib.proxy.Enhancer";
	public final static String libAsmClass       = "org.objectweb.asm.ClassWriter";
	public final static String libC3p0Class      = "com.mchange.v2.c3p0.ComboPooledDataSource";
	public final static String libDruidClass     = "com.alibaba.druid.pool.DruidDataSource";
	public final static String libCsvjdbcClass   = "org.relique.jdbc.csv.CsvDriver";
	
	public final static String libMysqlClass     = "com.mysql.jdbc.Driver";
	public final static String libOracleClass    = "oracle.jdbc.driver.OracleDriver";
	public final static String libSqlServerClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public final static String libPostgresClass  = "org.postgresql.Driver";
	 
 	public static Map<String, String[]> hLibClasses=new LinkedHashMap<String, String[]>(){
 		private static final long serialVersionUID = 1L;
		{
			put(libGsonClass,        new String[]{"com.google.code.gson:gson:2.3.1"});
			 
 			put(libCglibClass,       new String[]{"cglib:cglib:3.2.0"});
 			put(libAsmClass,         new String[]{"org.ow2.asm:asm:5.0.3"});
 			put(libC3p0Class,        new String[]{"c3p0:c3p0:0.9.1.2"});
 			put(libDruidClass,       new String[]{"com.alibaba:druid:0.2.9"});
 			
 			put(libMysqlClass,       new String[]{"mysql:mysql-connector-java:5.1.24"});
 			put(libOracleClass,      new String[]{"com.oracle:ojdbc14:10.2.0.4.0"});
 			put(libSqlServerClass,   new String[]{"com.microsoft.sqlserver:sqljdbc4:4.0"});
 			put(libPostgresClass,    new String[]{"org.postgresql:postgresql:42.1.4"});
 			
 			put(libCsvjdbcClass,     new String[]{"net.sourceforge.csvjdbc:csvjdbc:1.0.28"});
 			
 		}
 	};
 	
 	private static boolean init_libGsonClass   =false;
	private static boolean init_libCglibClass  =false;
	private static boolean init_libCsvjdbcClass=false;
	private static boolean init_libC3p0Class   =false;
	private static boolean init_libDruidClass  =false;
	 
	public static AgentEnhancer createAgentEnhancer(){
		if(!init_libCglibClass){
			loadClass(libAsmClass);
			loadClass(libCglibClass);
			
			init_libCglibClass=true;
		}
		
		return new AgentEnhancer();
	}
	
	public static Csv createCsv(){
		if(!init_libCsvjdbcClass){
			loadClass(libCsvjdbcClass);
			init_libCsvjdbcClass=true;
		}
		
		return new Csv();
	}
	
	public static C3p0DataSource createC3p0DataSource(){
		if(!init_libC3p0Class){
			loadClass(libC3p0Class);
			init_libC3p0Class=true;
		}
		
		return new C3p0DataSource();
	}
	
	public static DruidDataSource createDruidDataSource(){
		if(!init_libDruidClass){
			loadClass(libDruidClass);
			init_libDruidClass=true;
		}
		
		return new DruidDataSource();
	}
	
	public static void tryLoadGson(){
		if(!init_libGsonClass){
			try{
				MelpLib.loadClass(libGsonClass);
			}catch(Exception e){
				logger.error("Exception load gson: "+e,e);
			}
			
			init_libGsonClass=true;
		}
	}
	  
	public static boolean loadClass(String clazz) {
		try {
			Class.forName(clazz);
			return true;
		} catch (ClassNotFoundException e) {
			return appendClass(clazz,e);	
		}
	}
	
	private synchronized static boolean appendClass(String clazz,ClassNotFoundException e){
		try {
			Class.forName(clazz);
			return true;
		}catch(ClassNotFoundException cnfe){}
		
		StackTraceElement theCaller=findTheCaller(e);			
		
		String[] GAV_URL=hLibClasses.get(clazz);
		if(GAV_URL==null){
			throw new RuntimeException("Can't locate class: "+clazz+", add the jar to classpath OR setup MelpLib.hLibClasses",e);
		}
		
		JarLocation location=new JarLocation(GAV_URL[0]);
		location.setTheCaller(theCaller);
		if(GAV_URL.length>1){
			location.setBaseUrl(GAV_URL[1]);
		}
		
		try {
			File jar=location.findJar();
			if(jar.exists()){
				addJarToClassPath(MelpLib.class.getClassLoader(), jar);
				return true;
			}
		} catch(Exception ioe) {
			throw new RuntimeException("Class not found: "+clazz,ioe);
		}
			
		throw new RuntimeException("Class not found: "+clazz+", can't locate GAV: "+GAV_URL[0]);
	}
	
	private static StackTraceElement findTheCaller(Exception e){
		boolean begin=false;
		for(StackTraceElement ste:e.getStackTrace()){
			String mname=ste.getClassName()+"."+ste.getMethodName();
			if(mname.equals(PkgNames.ORM_LOADERCLASS+".loadClass")){
				begin=true;
			}else if(begin && !mname.startsWith(PkgNames.ORM_PACKAGE+".") && !mname.startsWith(PkgNames.ORM_TOOLS+".")){
				return ste;
			}
		}
		return null;
	}
	
	public static void addJarToClassPath(ClassLoader loader,File jar){
		try {
		    URL url = jar.toURI().toURL();
		    
		    URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		    if(loader instanceof URLClassLoader){
		    	classLoader=(URLClassLoader)loader;
		    }
		    
		    logger.debug("Add jar: "+jar.getAbsolutePath()+", to classloader: "+classLoader);
		    
		    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		    method.setAccessible(true);
		    method.invoke(classLoader, url);
		} catch (Exception e) {
		    logger.error("Add jar:"+jar.getAbsolutePath()+" to class path("+loader+") exception: "+e,e);
		}
	}
   
}
