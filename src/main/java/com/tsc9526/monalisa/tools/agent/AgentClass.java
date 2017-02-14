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
package com.tsc9526.monalisa.tools.agent;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.orm.datasource.DBTasks;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.resources.PkgNames;
import com.tsc9526.monalisa.tools.clazz.CompilePackage;
import com.tsc9526.monalisa.tools.clazz.Compiler;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClasspath;
import com.tsc9526.monalisa.tools.clazz.MelpLib;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpException;
import com.tsc9526.monalisa.tools.string.MelpDate;
import com.tsc9526.monalisa.tools.string.MelpJson;

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
  	
	public static void agentmain(String agentArgs,Instrumentation inst) {
		try{
			Class.forName(PkgNames.ORM_JSONHELPER);
			
		}catch(ClassNotFoundException e){
			delegateAgent(agentArgs,inst);
			return;
		}catch(NoClassDefFoundError e){
			delegateAgent(agentArgs,inst);
			return;
		}
		
		callAgent(agentArgs,inst);
	}
	
	private static void delegateAgent(final String agentArgs,final Instrumentation inst){
		try{
			for(Class<?> c:inst.getAllLoadedClasses()){
				if(c.getName().equals(PkgNames.ORM_AGENTCLASS)){
					Method m=c.getMethod("agentmain", String.class,Instrumentation.class);
					m.invoke(null, agentArgs,inst);
					break;
				}
			}
		}catch(Exception e){
			MelpException.throwRuntimeException(e);
		}
	}
	
	private static void callAgent(final String agentArgs,final Instrumentation inst){
		try {
			AgentArgs args=MelpJson.getGson().fromJson(agentArgs, AgentArgs.class);
  		
			printAgentInfo(args);
		 	 
			for(AgentArgs.AgentArgClassInfo ci:args.getClasses()){
				String classFilePath=getClassFilePath(args.getClassFilePathRoot(),ci.className);
				
				byte[] classBytes = MelpFile.readFile(classFilePath); 
	
				redefineClass(inst,ci.className,classBytes);
				
				hAgentClasses.put(ci.className, ci);
			}
			
		} catch (Exception e) {
			MelpException.throwRuntimeException(e);
		}
	}
	
	private static void printAgentInfo(AgentArgs args){
		StringBuffer sb=new StringBuffer();
		for(AgentArgs.AgentArgClassInfo ci:args.getClasses()){
			AgentArgs.AgentArgClassInfo old=hAgentClasses.get(ci.className);
			 
			long oldVersion     =old!=null?old.version:MelpClass.getVersion(ci.className);
		 	long oldLastModified=old!=null?old.lastModified:MelpClasspath.getClassOrJarFile(ci.className).lastModified();
			 	
			String oldTs=MelpDate.toString(new Date(oldLastModified),"yyyyMMddHHmmss");
			String newTs=MelpDate.toString(new Date(ci.lastModified),"yyyyMMddHHmmss");
			
			if(old==null){
				if(ci.version<oldVersion){
					sb.append("<!Error!> ");
				}else if(ci.lastModified<oldLastModified){
					sb.append("<Warn!!!> ");
				}else{
					sb.append("<Replace> ");
				}
			}
			
			String sv=(ci.version     ==oldVersion     ?" == ":(ci.version     >oldVersion     ?" -> ":" -< "));
			String st=(ci.lastModified==oldLastModified?" == ":(ci.lastModified>oldLastModified?" -> ":" -< "));
			 
		 	sb.append("class: "+ci.className);
		 	sb.append(", version: "   +oldVersion+sv+ci.version  );
			sb.append(", timestamp: "  +oldTs     +st+newTs       );
			   
			sb.append("\r\n"); 
		}
		
		logger.info("Reload classes("+args.getClasses().length+"), Class-Path: "+new File(args.getClassFilePathRoot()).getAbsolutePath()
				+"\r\n***************************************************************************************************"
				+"\r\n"+sb.toString()
				+    "***************************************************************************************************");
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
	            MelpClasspath.appendToSystemPath(AgentClass.class);
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
				String agentArgs=MelpJson.getGson().toJson(args);
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
				MelpLib.tryLoadGson();
				
				initAgentClasses();
			}
			
			T value=MelpLib.createAgentEnhancer().createProxyInstance(theClass);
 
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
