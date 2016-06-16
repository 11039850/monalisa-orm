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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.core.parser.java.Java;
import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CompilePackage{
	private String javaSourceDir;
	private String classOutputDir;
	
	private Map<String,AgentJavaFile> hjfs=null;
	 
	public CompilePackage(String javaSourceDir,String classOutputDir){
		this.javaSourceDir =FileHelper.combinePath(javaSourceDir);
		this.classOutputDir=FileHelper.combinePath(classOutputDir);
	}
	
	public synchronized void scan(){
		if(hjfs==null){
			hjfs=new LinkedHashMap<String, AgentJavaFile>();
		}
		
		List<AgentJavaFile> jfs=new ArrayList<AgentJavaFile>();
		
		File dir=new File(javaSourceDir);
		if(dir.exists()){
			fetchJavaFiles(dir,jfs);
		}
		
		Set<String> rs=new HashSet<String>(); 
		for(AgentJavaFile j:jfs){
			String key=j.getJavaFile().getAbsolutePath();
			hjfs.put(key, j); 
			
			rs.add(key);
		}
		
		Set<String> rm=new HashSet<String>(); 
		for(String x:hjfs.keySet()){
			if(!rs.contains(x)){
				rm.add(x);
			}
		}
		
		for(String key:rm){
			hjfs.remove(key);
		}
	}
	
	protected void fetchJavaFiles(File f,List<AgentJavaFile> jfs){
		if(f.isFile()){
			if(f.getName().endsWith(".java")){
				long lastModified=f.lastModified();
				
				String key=f.getAbsolutePath();
				AgentJavaFile jf=hjfs.get(key);
				if(jf==null || jf.getLastModified()<lastModified){
					Java java=new Java(f);
					String classpath=java.getPackageName().replace(".","/")+"/"+java.getName();
					long version=java.getVersion(); 
					
					
					String className=classpath.replace("/",".");
					
					String classFilePath=FileHelper.combinePath(classOutputDir,classpath+".class");
					File   classFile=new File(classFilePath);
					
					int p=classFilePath.lastIndexOf("/");
					File  classDir=new File(classFilePath.substring(0,p));
					if(!classDir.exists()){
						classDir.mkdirs();
					}
					
					jfs.add(new AgentJavaFile(className,version,lastModified,f,classFile));
				}
			}
		}else if(f.isDirectory()){
			for(File x:f.listFiles()){
				fetchJavaFiles(x,jfs);
			}
		}
	}
	
	public String getJavaSourceDir() {
		return javaSourceDir;
	}
	 
	public String getClassOutputDir() {
		return classOutputDir;
	} 
	
	public Collection<AgentJavaFile> getJavaFiles() {
		if(hjfs==null){
			scan();
		}
		return hjfs.values();
	} 
}