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
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.core.datasource.DBTasks;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.ClassPathHelper;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JsonHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class AgentClass {
	static Logger logger=Logger.getLogger(AgentClass.class);
	
	static Map<String, Long> hAgentClasses=new ConcurrentHashMap<String, Long>();
	
	public static void premain(String agentArgs, Instrumentation inst){
		 
	} 
	
	public static void agentmain(String agentArgs, Instrumentation inst) {
		try {
			AgentArgs args=JsonHelper.getGson().fromJson(agentArgs, AgentArgs.class);
			
			StringBuffer sb=new StringBuffer();
			String[] classNames=args.getClassNames();
			String[][] ccs=new String[classNames.length][2];
			for(int i=0;i<classNames.length;i++){
				String classFilePath=getClassFilePath(args.getClassFilePathRoot(),classNames[i]);
				
				ccs[i][0]=classNames[i];
				ccs[i][1]=classFilePath;
				sb.append(classNames[i]+" <- "+new File(classFilePath).getAbsolutePath());
			}
			logger.info("Reload classes:\r\n"+sb.toString());
			
			for(String[] cc:ccs){
				String className    =cc[0];
				String classFilePath=cc[1];
				
				long lastestLoadTime=new File(classFilePath).lastModified();
				byte[] classBytes = FileHelper.readFile(classFilePath); 
	
				redefineClass(inst,className,classBytes);
				
				hAgentClasses.put(className, lastestLoadTime);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static long getAgentClassLoadTime(String className) {
		Long time= hAgentClasses.get(className);
		if(time==null){
			time=0L;
		}
		return time;
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
	 
	private static CompilePackage compilePackage; 
	private synchronized static void checkClasses(){
		if(compilePackage==null){
			reloadClasses();
			 
			long delay=DbProp.CFG_RELOAD_CLASS_INTERVAL*1000;
			DBTasks.schedule("ClassReloadTask", new TimerTask() {
				public void run() {
					reloadClasses();
				}
			}, delay, delay);
		}
	}
	
	public synchronized static void reloadClasses(){
		if(compilePackage==null){
			compilePackage=new CompilePackage(DbProp.CFG_SQL_PATH,DbProp.TMP_WORK_DIR_JAVA);
			
			if (ClassLoader.getSystemClassLoader() != AgentClass.class.getClassLoader()){
	            ClassPathHelper.appendToSystemPath(AgentClass.class);
	        }
		}
		 
		try{
			Compiler.compile(compilePackage);
			
			List<String> classNames=new ArrayList<String>();
			for(AgentJavaFile j:compilePackage.getJavaFiles()){
				if(j.isReloadRequired()){
					classNames.add(j.getClassName());
				}
			} 
			
			if(classNames.size()>0){
				AgentArgs args=new AgentArgs(DbProp.TMP_WORK_DIR_JAVA,classNames.toArray(new String[0]));
				String agentArgs=JsonHelper.getGson().toJson(args);
			    AgentJar.loadAgentClass(AgentClass.class.getName(), agentArgs); 
			} 
			
		}catch(Exception e){
			logger.error("Reload exception: "+e,e);
		}
	}
	
	private static Map<Class<?>, Object> hCachedObjects=new ConcurrentHashMap<Class<?>, Object>();
	
	public static <T> T createAgent(Class<T> theClass){
		try {
			checkClasses();   
			
			T value=(T)hCachedObjects.get(theClass);
			
			if(value==null){
				Constructor<T> c=theClass.getDeclaredConstructor();
				
				if(Modifier.isPrivate(c.getModifiers())){
					synchronized (theClass) {
						if(!hCachedObjects.containsKey(theClass)){
							c.setAccessible(true);
							
							value= c.newInstance();
							hCachedObjects.put(theClass, value);
						}else{
							value=(T)hCachedObjects.get(theClass);
						}
					} 
				}else{
					value=c.newInstance();
				}
			}
			
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
