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
package com.tsc9526.monalisa.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.tsc9526.monalisa.tools.io.JavaWriter;
import com.tsc9526.monalisa.tools.template.jsp.Jsp;
import com.tsc9526.monalisa.tools.template.jsp.JspCode;
import com.tsc9526.monalisa.tools.template.jsp.JspContext;
import com.tsc9526.monalisa.tools.template.jsp.JspElement;
import com.tsc9526.monalisa.tools.template.jsp.JspEval;
import com.tsc9526.monalisa.tools.template.jsp.JspFunction;
import com.tsc9526.monalisa.tools.template.jsp.JspPage;
import com.tsc9526.monalisa.tools.template.jsp.JspText;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */ 
public class CodeAssistMain {
	public static void main(String[] args)throws Exception{
		CodeAssistMain jwc=new CodeAssistMain();
		jwc.createDBModelWriter();
		jwc.createDBSelectWriter();
		
	}
	public void createDBModelWriter()throws Exception{
		File jspFile=new File("./src/main/resources/com/tsc9526/monalisa/orm/resources/template/model.jsp");
		Jsp jsp=new Jsp(jspFile);
		
		FileOutputStream fos=new FileOutputStream("src/main/java/com/tsc9526/monalisa/orm/generator/DBWriterModel.java");
		writeToJava(jsp,new JavaWriter(fos), "com.tsc9526.monalisa.orm.generator", "DBWriterModel");
		
	}
	
	public void createDBSelectWriter()throws Exception{
		File jspFile=new File("./src/main/resources/com/tsc9526/monalisa/orm/resources/template/select.jsp");
		Jsp jsp=new Jsp(jspFile);
		
		FileOutputStream fos=new FileOutputStream("src/main/java/com/tsc9526/monalisa/orm/generator/DBWriterSelect.java");
		writeToJava(jsp,new JavaWriter(fos), "com.tsc9526.monalisa.orm.generator", "DBWriterSelect");
		
	}
	
	public void writeToJava(Jsp jsp,PrintWriter w,String packageName,String javaName)throws IOException{
		w.println("/*******************************************************************************************");
		w.println(" *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)");
		w.println(" * ");
		w.println(" *   Monalisa is free software: you can redistribute it and/or modify");
		w.println(" *	it under the terms of the GNU Lesser General Public License as published by");
		w.println(" *	the Free Software Foundation, either version 3 of the License, or");
		w.println(" *	(at your option) any later version.");
		w.println("");
		w.println(" *	This program is distributed in the hope that it will be useful,");
		w.println(" *	but WITHOUT ANY WARRANTY; without even the implied warranty of");
		w.println(" *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
		w.println(" *	GNU Lesser General Public License for more details.");
		w.println("");
		w.println(" *	You should have received a copy of the GNU Lesser General Public License");
		w.println(" *	along with this program.  If not, see <http://www.gnu.org/licenses/>.");
		w.println(" *******************************************************************************************/");
		
		w.println("package "+packageName+";\r\n\r\n");
		
		w.println("import "+PrintWriter.class.getName()+";");
		w.println("import "+JspContext.class.getName()+";");
		for(JspElement e:jsp.getElements()){
			if(e instanceof JspPage){
				JspPage page=(JspPage)e;
				
				for(String s:page.getImports()){
					w.println("import "+s+";");
				}
			}
		}
		
		String basepath=new File(".").getAbsolutePath();
		basepath=jsp.getFilePath().substring(basepath.length()+1);
		
		w.println("");
		w.println(""+/**~!{*/""
			+ "/**"
			+ "\r\n * Auto generate code from jsp: " +((basepath))+ ""
			+ "\r\n * "
			+ "\r\n * @author zzg.zhou(11039850@qq.com)  "
			+ "\r\n */"
		+ "\r\n"/**}*/);
		w.println("public class "+javaName+"{");
		
		for(JspElement e:jsp.getElements()){
			if(e instanceof JspFunction){
				w.write(e.getCode());
			}
		}
		
		w.write("\tpublic void service(JspContext request,PrintWriter out){\r\n");
		for(JspElement e:jsp.getElements()){
			if(e instanceof JspText){
				String code=e.getCode();
				
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<code.length();i++){
					char c=code.charAt(i);
					
					
					if(c=='\n'){
						if(sb.length()>0 && sb.charAt(sb.length()-1)=='\r'){
							sb.delete(sb.length()-1,sb.length());
						}
						w.println("\t\tout.println(\""+sb.toString().replace("\"", "\\\"")+"\");");
						sb.delete(0, sb.length());
					}else{
						sb.append(c);
					}
				}
				if(sb.length()>0){
					w.println("\t\tout.print(\""+sb.toString().replace("\"", "\\\"")+"\");");
				}
			}else if(e instanceof JspEval){
				w.println("\t\tout.print("+e.getCode()+");");
			}else if(e instanceof JspCode){
				w.print("\t\t"+e.getCode());
			}
		}
		w.println("\tout.flush();\r\n");
		w.println("\t}");
		
		w.print("}");
		
		w.close();
	}
	
}
