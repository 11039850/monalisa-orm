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
package com.tsc9526.monalisa.orm.tools.dependency;

import java.io.File;

import com.tsc9526.monalisa.orm.tools.helper.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JarLocate {

	
	public File locateJar(String group, String artifact, String version) {
		String localRepository=getRepository();
		
		String jarfile=artifact + "-" + version + ".jar";
		
		jarfile=FileHelper.combinePath(localRepository,group.replaceAll("\\.","/"),artifact,version,jarfile);
		  
        return new File(jarfile);
    }
	
	public void download(String group, String artifact, String version){
		
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
