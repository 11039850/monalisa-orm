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
package com.tsc9526.monalisa.core.agent;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.core.datasource.DBTasks;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassPathHelper;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JsonHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentClass {
	static Logger logger=Logger.getLogger(AgentClass.class);
	
	static Map<String, AgentArgs.AgentArgClassInfo> hAgentClasses=new ConcurrentHashMap<String, AgentArgs.AgentArgClassInfo>();
	
	public static void premain(String agentArgs, Instrumentation inst){
		//agentmain(agentArgs,inst);
	} 
	
	public static void agentmain(String agentArgs, Instrumentation inst) {
		try {
			AgentArgs args=JsonHelper.getGson().fromJson(agentArgs, AgentArgs.class);
			
			printAgentInfo(args);
		 	 
			for(AgentArgs.AgentArgClassInfo ci:args.getClasses()){
				String classFilePath=getClassFilePath(args.getClassFilePathRoot(),ci.className);
				
				byte[] classBytes = FileHelper.readFile(classFilePath); 
	
				redefineClass(inst,ci.className,classBytes);
				
				hAgentClasses.put(ci.className, ci);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void printAgentInfo(AgentArgs args){
		StringBuffer sb=new StringBuffer();
		for(AgentArgs.AgentArgClassInfo ci:args.getClasses()){
			AgentArgs.AgentArgClassInfo old=hAgentClasses.get(ci.className);
			 
			long oldVersion     =old!=null?old.version:ClassHelper.getVersion(ci.className);
		 	long oldLastModified=old!=null?old.lastModified:ClassPathHelper.getClassOrJarFile(ci.className).lastModified();
			 	
			String oldTs=Helper.toDateString(oldLastModified,"yyyyMMddHHmmss");
			String newTs=Helper.toDateString(ci.lastModified,"yyyyMMddHHmmss");
			
			if(old==null){
				if(ci.version<oldVersion){
					sb.append("<!Error!>");
				}else if(ci.lastModified<oldLastModified){
					sb.append("<Warn!!!>");
				}else{
					sb.append("<Replace>");
				}
			}
		 	sb.append("class: "+ci.className);
		 	sb.append(", version: "   +oldVersion+(ci.version     ==oldVersion     ?" == ":" -> ")+ci.version  );
			sb.append(", timestamp:"  +oldTs     +(ci.lastModified==oldLastModified?" == ":" -> ")+newTs       );
			   
			sb.append("\r\n"); 
		}
		
		logger.info("Reload classes("+args.getClasses().length+"), Class-Path: "+new File(args.getClassFilePathRoot()).getAbsolutePath()
				+"\r\n****************************************************************************"
				+"\r\n"+sb.toString()
				+    "****************************************************************************");
	}
	
	public static AgentArgs.AgentArgClassInfo getAgentLoadClassInfo(String className) {
		return hAgentClasses.get(className);
	}
	
	private static void redefineClass(Instrumentation inst,String className,byte[] classBytes)throws Exception{
		ClassDefinition reporterDef = new ClassDefinition(Class.forName(className), classBytes);
		inst.redefineClasses(reporterDef);	 
	}
	
	private static String getClassFilePath(String classFilePathRoot,String className){
		String path=classFilePathRoot;
		if(!path.endsWith("/")){
			path+="/";
		}
		
		path+=className.replace(".","/")+".class";
		
		return path;
	}
	 
	private static boolean initilized=false;
	private synchronized static void initAgentClasses(){
		if(!initilized){
			AgentJar.loadCglibClass();
			
			reloadClasses();
			 
			long delay=DbProp.CFG_RELOAD_CLASS_INTERVAL*1000;
			DBTasks.schedule("ClassReloadTask", new TimerTask() {
				public void run() {
					reloadClasses();
				}
			}, delay, delay);
			
			initilized=true;
		}
	}
	
	private static CompilePackage compilePackage; 
	public synchronized static void reloadClasses(){
		if(compilePackage==null){
			compilePackage=new CompilePackage(DbProp.CFG_SQL_PATH,DbProp.TMP_WORK_DIR_JAVA);
			
			if (ClassLoader.getSystemClassLoader() != AgentClass.class.getClassLoader()){
	            ClassPathHelper.appendToSystemPath(AgentClass.class);
	        }
		}
		 
		try{
			Compiler.compile(compilePackage);
			
			List<AgentArgs.AgentArgClassInfo> cis=new ArrayList<AgentArgs.AgentArgClassInfo>();
			for(AgentJavaFile j:compilePackage.getJavaFiles()){
				if(j.isReloadRequired()){
					cis.add(new AgentArgs.AgentArgClassInfo(j.getClassName(),j.getVersion(),j.getLastModified()));
				}
			} 
			
			if(cis.size()>0){
				AgentArgs args=new AgentArgs(DbProp.TMP_WORK_DIR_JAVA,cis.toArray(new AgentArgs.AgentArgClassInfo[0]));
				String agentArgs=JsonHelper.getGson().toJson(args);
			    AgentJar.loadAgentClass(AgentClass.class.getName(), agentArgs); 
			} 
			
		}catch(Exception e){
			logger.error("Reload exception: "+e,e);
		}
	}
	
	 
	public static <T> T createAgent(Class<T> theClass){
		if(Modifier.isFinal(theClass.getModifiers())){
			throw new RuntimeException("Agent class: "+theClass.getName()+" cannot be marked by final!");
		}
	 	
		try {
			if(!initilized){
				initAgentClasses();
			}
			
			T value=AgentEnhancer.createProxyInstance(theClass);
 
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
