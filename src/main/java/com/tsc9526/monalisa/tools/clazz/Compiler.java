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
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import com.tsc9526.monalisa.tools.agent.AgentJavaFile;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Compiler {
	static Logger logger=Logger.getLogger(Compiler.class);
	
	public static Set<String> hClassPath=new LinkedHashSet<String>();
	
	static{
		String classpath = System.getProperty("java.class.path");
		for(String p:MelpClasspath.splitClassPaths(classpath)){
			hClassPath.add(p.replace("\\","/"));
		}
		
		URL url=Compiler.class.getClassLoader().getResource("");
		String filepath=MelpClasspath.getFilePathfromResourceUrl(url);
		if(filepath!=null){
			if(filepath.endsWith("/")){
				filepath=filepath.substring(0,filepath.length()-1);
			}
			
			hClassPath.add(filepath);
			
			if(filepath.endsWith("/classes")){
				String libpath=filepath.substring(0,filepath.length()-8)+"/lib";
				File dir=new File(libpath);
				if(dir.exists() && dir.isDirectory()){
					for(File lib:dir.listFiles()){
						if(lib.getName().endsWith(".jar")){
							hClassPath.add(libpath+"/"+lib.getName());
						}
					}
				}
			}
		}
	}
	 
	 
	private static String getClassPath(){
		StringBuffer sb=new StringBuffer();
		for(String p:hClassPath){
			if(sb.length()>0){
				sb.append(File.pathSeparator);
			}
			sb.append(p);
		}
		
		return sb.toString();
	}

	public synchronized static void compile(CompilePackage pkg) {
		pkg.scan();			 
	   
		StringBuffer sb=new StringBuffer();
		List<JavaFileObject> fos=new ArrayList<JavaFileObject>();
		for(AgentJavaFile j:pkg.getJavaFiles()){
			 if(j.isCompileRequired()){
				fos.add(new StringFileObject(j));
				 
				if(sb.length()>0){
					sb.append("\r\n");
				}
				sb.append(j.getJavaFile().getAbsolutePath())
				  .append(" -> ")
				  .append(j.getClassFile().getAbsolutePath());
			}
		}
		
		if(fos.size()>0){
			StringWriter out=new StringWriter();
			String classpath = getClassPath();
			List<String> options=Arrays.asList("-encoding", "utf-8","-nowarn","-classpath",classpath,"-d",pkg.getClassOutputDir());
			
			logger.debug("Compile files: \r\n"+sb.toString());
			
			JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
			CompilationTask task = javac.getTask(out, null, null, options, null,fos); 
			  
			if (!task.call()) {
			    throw new RuntimeException("Compile error: "+out.toString());  
			}
		}
	}
	
	
	  	
	public static class StringFileObject extends SimpleJavaFileObject {  
	    private String content;  
	  
	    public StringFileObject(AgentJavaFile jf){  
	        super(URI.create("string:///" + jf.getClassName().replace('.', '/') + ".java"), JavaFileObject.Kind.SOURCE);  
	        this.content = MelpFile.readToString(jf.getJavaFile(), "utf-8");  
	    }  
	  
	    public CharSequence getCharContent(boolean ignoreEncodingErrors) {  
	        return this.content;  
	    }  
	}  
}
