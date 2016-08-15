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
import java.net.URLConnection;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.tools.helper.FileHelper;
import com.tsc9526.monalisa.orm.tools.helper.HttpHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JarLocation implements HttpHelper.DownloadListener{
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
	
	public File findJar()throws IOException{
		String callAt="";
		if(theCaller!=null){
			callAt=", Call at: "+theCaller.getClassName()+"."+theCaller.getMethodName()+"("+theCaller.getFileName()+":"+theCaller.getLineNumber()+")";
		}
		String gav=paddingRight(GAV,35);
		
		File jar=new File(DbProp.CFG_LIB_PATH,jarfile);
		if(!jar.exists()){
			Respository respository=new Respository();
			
			String localRepository=respository.getLocalRepository();
			logger.debug(">>> Maven local repository: "+localRepository);	
			
			String pathfile=FileHelper.combinePath(localRepository,group.replaceAll("\\.","/"),artifact,version,jarfile);
				
			File jarFromMaven=new File(pathfile);
			if(jarFromMaven.exists()){
				logger.info(">>> Located  artifact: "+gav+", from "+jarFromMaven.getAbsolutePath()+callAt);
				 
				FileHelper.copy(jarFromMaven, jar);
			}else{
				List<String> urls=respository.getRemoteRepositoryUrls();
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
						logger.info(">>> Locating artifact: "+gav+", from "+downUrl+callAt);
					}else{
						logger.info(">>> Locating artifact: "+gav+", try another site("+(i+1)+"/"+urls.size()+"): "+downUrl);
					}
					
					if(download(downUrl,jar)){
						break;
					}
				}
			}
		}else{
			logger.info(">>> Located  artifact: "+gav+", from "+jar.getAbsolutePath()+callAt);
		}
		
		return jar;
	}
	 
	protected boolean download(String url,File jar){
		try{ 
			HttpHelper.download(url, jar,this);
			
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
	 

	public StackTraceElement getTheCaller() {
		return theCaller;
	}

	public void setTheCaller(StackTraceElement theCaller) {
		this.theCaller = theCaller;
	}

	private static String paddingRight(String s,int length){
		StringBuilder sb=new StringBuilder(s);
		int len=length-sb.length();
		for(int i=0;i<len;i++){
			sb.append(" ");
		}
		return sb.toString();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
