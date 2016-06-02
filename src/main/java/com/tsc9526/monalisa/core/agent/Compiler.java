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

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Compiler {
	static Logger logger=Logger.getLogger(Compiler.class);
	
	public static void compile(CompilePackage pkg) {
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
			String classpath = System.getProperty("java.class.path");
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
	        this.content = FileHelper.readToString(jf.getJavaFile(), "utf-8");  
	    }  
	  
	    public CharSequence getCharContent(boolean ignoreEncodingErrors) {  
	        return this.content;  
	    }  
	}  
}
