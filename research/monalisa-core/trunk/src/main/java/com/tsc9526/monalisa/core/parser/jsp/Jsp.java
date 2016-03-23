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
package com.tsc9526.monalisa.core.parser.jsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Jsp{
	public static String DEFAULT_PAGE_ENCODING="utf-8";
	
	private String filePath;
	private String body;
	private List<JspElement> elements=new ArrayList<JspElement>();
	
	private long lastModified;
	
	public Jsp(File jspFile)throws IOException {
		this.filePath=jspFile.getAbsolutePath();
		this.lastModified=jspFile.lastModified();
		
		String body=FileHelper.readToString(new FileInputStream(filePath), DEFAULT_PAGE_ENCODING);
		
		parseBody(body);
		
		String encoding=getPageEncoding();
		if(!DEFAULT_PAGE_ENCODING.equalsIgnoreCase(encoding)){
			elements.clear();
			body=FileHelper.readToString(new FileInputStream(filePath), encoding);			
			parseBody(body);
		}
	}
	
	public Jsp(String body){
		parseBody(body);
	}
	
	public String getPageEncoding(){
		String pageEncoding=null;
		for(JspElement e:elements){
			if(e instanceof JspPage){
				pageEncoding=((JspPage)e).getPageEncoding();
				if(pageEncoding!=null){
					return pageEncoding;
				}
			}
		}
		return DEFAULT_PAGE_ENCODING;
	}
	
	protected void parseBody(String body){
		this.body=body;
		
		int len=body.length();
		for(int i=0;i<len;i++){
			char c=body.charAt(i);
			if(c=='<' && i<len-1 && body.charAt(i+1)=='%'){
				int k=body.indexOf("%>",i);
				 
				if(i<len-2 && body.charAt(i+2)=='@'){
					String page=body.substring(i+3,k).trim();
					if(page.startsWith("page")){
						page=page.substring(4).trim();
						add(new JspPage(this,i,k+2-i).parseCode(page));
					}
				}else if(i<len-2 && body.charAt(i+2)=='!'){
					add(new JspFunction(this,i,k+2-i).parseCode(body.substring(i+3,k)));
				}else if(i<len-2 && body.charAt(i+2)=='='){
					add(new JspEval(this,i,k+2-i).parseCode(body.substring(i+3,k)));
				}else{
					add(new JspCode(this,i,k+2-i).parseCode(body.substring(i+2,k))); 
				}
				
				i=k+1; 
			}else{
				int pos=i;
				
				int k=body.indexOf("<%",i);
				 
				String text="";
				if(k<0){
					text=body.substring(i);
					i=body.length()-1;
				}else{
					text=body.substring(i,k);
					
					i=k-1;
				} 	
			 
				add(new JspText(this,pos,k-pos).parseCode(text));
			}
		}
	}
	
	
	public void writeToJava(PrintWriter w,String packageName,String javaName,String comments)throws IOException{
		w.println("package "+packageName+";\r\n\r\n");
		
		w.println("import "+PrintWriter.class.getName()+";");
		w.println("import "+JspContext.class.getName()+";");
		for(JspElement e:elements){
			if(e instanceof JspPage){
				JspPage page=(JspPage)e;
				
				for(String s:page.getImports()){
					w.println("import "+s+";");
				}
			}
		}
		
		if(comments!=null){
			w.println("/**"+comments+" */");
		}
		
		w.println("public class "+javaName+"{");
		
		for(JspElement e:elements){
			if(e instanceof JspFunction){
				w.write(e.getCode());
			}
		}
		
		w.write("\tpublic void service(JspContext request,PrintWriter out){\r\n");
		for(JspElement e:elements){
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
	
	private JspElement add(JspElement e){
		e.setIndex(elements.size());
		elements.add(e);
		return e;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
 
	public String getBody() {
		return body;
	}

	public JspElement getElement(int index){
		return elements.get(index);
	}
	
	public List<JspElement> getElements() {
		return elements;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}