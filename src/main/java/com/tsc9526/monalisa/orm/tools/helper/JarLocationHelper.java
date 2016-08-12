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
package com.tsc9526.monalisa.orm.tools.helper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.tools.logger.Logger;
import com.tsc9526.monalisa.orm.tools.resources.PkgNames;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JarLocationHelper {
	static Logger logger=Logger.getLogger(JarLocationHelper.class);
	 
	public static void loadClass(String clazz) {
		try {
			Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			try {
				String[] GAV_URL=PkgNames.hLibClasses.get(clazz);
				if(GAV_URL==null){
					throw new RuntimeException("Can't locate class: "+clazz+", please setup com.tsc9526.monalisa.orm.tools.resources.PkgNames.hLibClasses",e);
				}
				
				JarLocationHelper location=new JarLocationHelper(GAV_URL[0]);
				if(GAV_URL.length>1){
					location.setBaseUrl(GAV_URL[1]);
				}
				
				File jar=location.findJar();
				if(jar.exists()){
					logger.info("Located class: "+clazz+" from: "+jar.getAbsolutePath());
					  
					addJarToClassPath(JarLocationHelper.class.getClassLoader(), jar);
				}else{
					throw new RuntimeException("Can't locate class: "+clazz+", GAV: "+GAV_URL[0]+", File not found: "+jar.getAbsolutePath());
				}
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	}
	
	protected static void addJarToClassPath(ClassLoader loader,File jar){
		try {
		    URL url = jar.toURI().toURL();
		    
		    URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		    if(loader instanceof URLClassLoader){
		    	classLoader=(URLClassLoader)loader;
		    }
		    
		    logger.info("Add jar: "+jar.getAbsolutePath()+", to classloader: "+classLoader);
		    
		    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		    method.setAccessible(true);
		    method.invoke(classLoader, url);
		} catch (Exception e) {
		    logger.error("Add jar:"+jar.getAbsolutePath()+" to class path("+loader+") exception: "+e,e);
		}
	}
   
	
	
	private String group;
	private String artifact;
	private String version;
	
	private String jarfile;
	
	private String baseUrl="https://repo1.maven.org/maven2";
	
	public JarLocationHelper(String GAV){
		super();
		
		String[] vs=GAV.split(":");
		group   =vs[0];
		artifact=vs[1];
		version =vs[2];
		
		jarfile=artifact + "-" + version + ".jar";
	}
	
	public JarLocationHelper(String group, String artifact, String version) {
		super();
		
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		
		jarfile=artifact + "-" + version + ".jar";
	}
	
	public File findJar()throws IOException{
		File jar=new File(DbProp.CFG_LIB_PATH,jarfile);
		
		if(!jar.exists()){
			File jarFromMaven=locateJarFromMaven();
			
			if(jarFromMaven.exists()){
				logger.info("Copy jar: "+jarfile+" "+jarFromMaven.getAbsolutePath()+" -> "+jar.getAbsolutePath());
				FileHelper.copy(jarFromMaven, jar);
			}else{
				String baseurl=getBaseUrl();
				if(!baseurl.endsWith("/")){
					baseurl+="/";
				}
				String downUrl=baseurl+group.replaceAll("\\.","/")+"/"+artifact+"/"+artifact+"/"+jarfile;
				
				download(downUrl,jar);
			}
		}
		
		return jar;
	}
	
	public String getBaseUrl(){
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl){
		this.baseUrl=baseUrl;
	}
	 
	protected File locateJarFromMaven() {
		String localRepository=getRepository();
			 
		String pathfile=FileHelper.combinePath(localRepository,group.replaceAll("\\.","/"),artifact,version,jarfile);
			  
	    return new File(pathfile);
    }
	
	protected boolean download(String url,File jar){
		logger.info("Dowloading artifact: "+artifact + "-" + version + ".jar, from "+url);
		try{ 
			HttpHelper.download(url, jar);
			
			logger.info("Dowload artifact ok: "+artifact + "-" + version + ".jar");
			return true;
		}catch(Exception e){
			logger.error("Download artifact exception: "+e+", URL: "+url,e);
			return false;
		}
		
	}
	
	protected String getRepository(){
		String repository=System.getProperty("user.home")+"/.m2/repository";
		
		String home=System.getenv("MAVEN_HOME");
		if(home==null){
			home=System.getProperty("MAVEN_HOME");
		}
		if(home!=null){
			String settings=home+"/conf/settings.xml";
			
			File pom=new File(settings);
			if(pom.exists()){
				String xml=FileHelper.readToString(pom,"utf-8");
				
				int p1=xml.indexOf("<localRepository>");
				int p2=xml.indexOf("</localRepository>");
				if(p2>p1 && p1>0){
					repository=xml.substring(p1+"<localRepository>".length(),p2).trim();
				}
			}
		}
		
		return repository;
	}
}
