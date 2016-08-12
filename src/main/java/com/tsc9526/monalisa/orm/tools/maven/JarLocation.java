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
package com.tsc9526.monalisa.orm.tools.maven;

import java.io.File;
import java.io.IOException;

import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.tools.helper.FileHelper;
import com.tsc9526.monalisa.orm.tools.helper.HttpHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JarLocation {
	static Logger logger=Logger.getLogger(JarLocation.class);
	 
	private String group;
	private String artifact;
	private String version;
	
	private String jarfile;
	
	private String baseUrl="https://repo1.maven.org/maven2";
	
	private String GAV;
	
	public JarLocation(String GAV){
		super();
		
		this.GAV=GAV;
		
		String[] vs=GAV.split(":");
		group   =vs[0];
		artifact=vs[1];
		version =vs[2];
		
		jarfile=artifact + "-" + version + ".jar";
	}
	
	public JarLocation(String group, String artifact, String version) {
		super();
		
		this.GAV=group+":"+artifact+":"+version;
		
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
				logger.info(">>> Locate artifact: "+GAV+", from "+jarFromMaven.getAbsolutePath());
				 
				FileHelper.copy(jarFromMaven, jar);
			}else{
				String baseurl=getBaseUrl();
				if(!baseurl.endsWith("/")){
					baseurl+="/";
				}
				String downUrl=baseurl+group.replaceAll("\\.","/")+"/"+artifact+"/"+version+"/"+jarfile;
				
				download(downUrl,jar);
			}
		}else{
			logger.info(">>> Locate artifact: "+GAV+", from "+jar.getAbsolutePath());
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
		
		logger.debug(">>> Maven repository: "+localRepository);	
		
		String pathfile=FileHelper.combinePath(localRepository,group.replaceAll("\\.","/"),artifact,version,jarfile);
			  
	    return new File(pathfile);
    }
	
	protected boolean download(String url,File jar){
		logger.info(">>> Locating artifact: "+GAV+", from "+url);
		try{ 
			HttpHelper.download(url, jar);
			
			logger.info(">>> Dowloaded artifact: "+GAV+" -> "+jar.getAbsolutePath());
			
			return true;
		}catch(Exception e){
			logger.error(">>> Download artifact: "+GAV+" exception: "+e+", URL: "+url,e);
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
