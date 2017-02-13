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
package com.tsc9526.monalisa.tools.maven;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpHttp;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JarLocation implements MelpHttp.DownloadListener{
	static Logger logger=Logger.getLogger(JarLocation.class);
	 
	private String group;
	private String artifact;
	private String version;
	
	private String jarfile;
 
	private String GAV;
	
	private StackTraceElement theCaller;
	
	private String baseUrl;
	
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
	
	
	private String getCallDetailMessage(boolean withTips){
		String callMessage="";
		if(DbProp.CFG_LOG_JARLOCATION_DETAIL){
			if(withTips){
				if(theCaller!=null){
					callMessage="\r\nCall at: "+theCaller.getClassName()+"."+theCaller.getMethodName()+"("+theCaller.getFileName()+":"+theCaller.getLineNumber()+")";
				}
				callMessage+="\r\n"+/**~!{*/""
					+ "If you don't want to see this message, please add the jar: " +(jarfile)+ " to your class path. Here is an example for maven: "
					+ "\r\n<dependency>"
					+ "\r\n	<groupId>" +(group)+ "</groupId>"
					+ "\r\n	<artifactId>" +(artifact)+ "</artifactId>"
					+ "\r\n	<version>" +(version)+ "</version>"
					+ "\r\n</dependency>  "
					+ "\r\n"
					+ "\r\nOR first call: com.tsc9526.monalisa.orm.datasource.DbProp.CFG_LOG_JARLOCATION_DETAIL = false;"
				+ "\r\n"/**}*/;
			}else{
				if(theCaller!=null){
					callMessage=", Call at: "+theCaller.getClassName()+"."+theCaller.getMethodName()+"("+theCaller.getFileName()+":"+theCaller.getLineNumber()+")";
				}
			}
		}
		
		return callMessage;
	}
	
	public File findJar()throws IOException{
		File jar=new File(DbProp.CFG_LIB_PATH,jarfile);
		if(!jar.exists()){
			Repository repository=new Repository();
			
			String localRepository=repository.getLocalRepository();
			logger.debug(">>> Maven repository: "+localRepository);	
			
			String pathfile=MelpFile.combinePath(localRepository,group.replaceAll("\\.","/"),artifact,version,jarfile);
				
			File jarFromMaven=new File(pathfile);
			if(jarFromMaven.exists()){
				logger.info(">>> Located  artifact: "+GAV+", from "+jarFromMaven.getAbsolutePath()+getCallDetailMessage(true));
				 
				MelpFile.copy(jarFromMaven, jar);
			}else{
				List<String> urls=repository.getRemoteRepositoryUrls();
				if(this.baseUrl!=null && this.baseUrl.length()>0){
					urls.clear();
					urls.add(this.baseUrl);
				}
				
				for(int i=0;i<urls.size();i++){
					String downUrl=urls.get(i);
					if(!downUrl.endsWith("/")){
						downUrl+="/";
					}
					downUrl+=group.replaceAll("\\.","/")+"/"+artifact+"/"+version+"/"+jarfile;
					
					if(i==0){
						logger.info(">>> Locating artifact: "+GAV+", from "+downUrl+getCallDetailMessage(true));
					}else{
						logger.info(">>> Locating artifact: "+GAV+", try another site("+(i+1)+"/"+urls.size()+"): "+downUrl);
					}
					
					if(download(downUrl,jar)){
						break;
					}
				}
			}
		}else{
			logger.info(">>> Located  artifact: "+GAV+", from "+jar.getAbsolutePath()+getCallDetailMessage(false));
		}
		
		return jar;
	}
	 
	protected boolean download(String url,File jar){
		try{ 
			MelpHttp.download(url, jar,this);
			
			logger.info(">>> Dowloaded: "+GAV+" to "+jar.getAbsolutePath());
			return true;
		}catch(Exception e){
			logger.error(">>> Download: "+GAV+" exception: "+e+", URL: "+url,e);
			return false;
		}
		
	}
	
	public void onConnected(URLConnection conn) {
		logger.info(">>> Connected, total bytes: "+conn.getContentLength());
	}

	private long lastLogTime=0L;
	public void onProgress(long receivedBytes, long totalBytes) {
		long tm=System.currentTimeMillis();
		if( (tm-lastLogTime) >= 2000 || receivedBytes>= totalBytes){
			lastLogTime=tm;
			
			logger.info(">>> Dowloading: "+GAV+",  "+receivedBytes+"/"+totalBytes);	
		}
	}
	 
	public void onMessage(String message){
		logger.info(">>> "+message);	
	}

	public StackTraceElement getTheCaller() {
		return theCaller;
	}

	public void setTheCaller(StackTraceElement theCaller) {
		this.theCaller = theCaller;
	}
 

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
