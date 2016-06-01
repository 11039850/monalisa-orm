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
package com.tsc9526.monalisa.core.parser.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Java {
	public static String DEFAULT_PAGE_ENCODING ="utf-8";
	 	
	protected final static String REGX_VAR="\\$[a-zA-Z_]+[a-zA-Z_0-9]*";
	protected Pattern patternVar = Pattern.compile(REGX_VAR);
	
	protected String filePath;
	protected String body;
	protected long lastModified;
	
	protected String packageName;
	protected String name;
	 
	protected StringBuffer java=new StringBuffer();
	
	protected long version=-1;
	
	public Java(File javaFile) {
		parseFile(javaFile);
	}
	
	public Java(String body){
		if(body.indexOf("\n")<0 && new File(body).exists()){
			parseFile(new File(body));
		}else{
			parseBody(body);
		}
	}
	
	protected void parseFile(File javaFile){
		try{
			this.filePath=javaFile.getAbsolutePath();
			this.lastModified=javaFile.lastModified();
			
			String body=FileHelper.readToString(new FileInputStream(filePath), DEFAULT_PAGE_ENCODING);
			
			parseBody(body);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	public String getJavaCode(){
		return java.toString();
	}
	
	protected void parseBody(String body){
		this.body=body;
		
		Pattern v = Pattern.compile("static\\s+long\\s+\\$VERSION\\s*=\\s*[0-9]+L?;");
		Matcher m=v.matcher(body);
		if(m.find()){
			int p1=m.start();
			int p2=body.indexOf("=",p1);
			int p3=body.indexOf(";",p2);
			
			String s=body.substring(p2+1,p3).trim();
			if(s.endsWith("L")){
				s=s.substring(0,s.length()-1);
			}
			version=Long.parseLong(s);
		}
		
		java.setLength(0);
		
		parsePackage();
	
		int len=body.length();
		for(int i=0;i<len;i++){
			char c=body.charAt(i);
			if(c=='<' && i<len-2 && body.charAt(i+1)=='<' && (body.charAt(i+2)=='-' || body.charAt(i+2)=='~')){
				i=parseBlock(i,false);
			}else if(c=='>' && i<len-2 && body.charAt(i+1)=='>' && (body.charAt(i+2)=='-' || body.charAt(i+2)=='~')){
				i=parseBlock(i,true);
			}else{
				java.append(c);
			}
		}
	}
	
	protected int parseBlock(int i,boolean output){
		char f=body.charAt(i+2);
		
		StringBuffer block=new StringBuffer();
		i=readBlock(body, i+3,block);
		
		StringBuffer padding=new StringBuffer();
		if(f=='~'){
			for(int k=0;k<block.length();k++){
				char x=block.charAt(k);
				if(x==' ' || x=='\t'){
					padding.append(x);
				}else{
					break;
				}
			}
		}
		
		String left=padding.toString();
		String[] lines=block.toString().split("\\n");
		for(int n=0;n<lines.length;n++){
			String line=lines[n];
			
			if(line.startsWith(left)){
				line=line.substring(left.length());
			}
			if(line.endsWith("\r")){
				line=line.substring(0,line.length()-1);
			}
			 
			java.append("\r\n");
			 
			java.append(left);
			
			List<String> vars=new ArrayList<String>();
			Matcher m=patternVar.matcher(line);
			while(m.find()){
				String var=m.group();
				vars.add(var.substring(1));
			}
			
			String s=line.replaceAll(REGX_VAR, "?");
			s=s.replaceAll("\"", "\\\\\"");
		 	
			if(output){
				
				java.append("add(\"").append(s).append("\"");
				if(vars.size()>0){
					for(String v:vars){
						java.append(","+v);
					}
				}
				java.append(").add(\"\\r\\n\");");
				
			}else{
				if(n==0){
					java.append(" ");
				}else{
					java.append("+");
				}
				 
				java.append("\"");
				java.append(s);
				java.append("\\r\\n");
				 
				java.append("\"");
				
				if(n==lines.length-1){
					java.append(";");
				}
			}
		}
		
		return i;
	}
	
	protected void parsePackage(){
		Pattern patternPackage = Pattern.compile("package\\s+[a-z0-9A-Z\\._]+\\s*;");
		Pattern patternClass   = Pattern.compile("class\\s+[a-z0-9A-Z\\_]+");
		
		Matcher m=patternPackage.matcher(body);
		if(m.find()){
			String x=m.group();
			packageName=x.substring(7,x.length()-1).trim();
		}
		
		if(packageName==null){
			throw new RuntimeException("Keyword package not found!");
		}
		
		m=patternClass.matcher(body);
		if(m.find()){
			String x=m.group();
			name=x.substring(5,x.length()).trim();
		}
		
		if(name==null){
			throw new RuntimeException("Keyword class not found!");
		}
	}
	
	private int readBlock(String body,int from,StringBuffer buf){
		int x=from;
		StringBuffer flag=new StringBuffer();
		for(int i=from;i<body.length();i++){
			char c=body.charAt(i);
			if(c!=' ' && c!='\t' && c!='\r' && c!='\n'){
				flag.append(c);
			}else if(flag.length()>0){
				while(c!='\n' && (c==' '|| c=='\t' || c=='\r')){
					i++;
					c=body.charAt(i);
				}
				
				int y=-1;
				if(flag.toString().equals("/*")){
					y=body.indexOf("*/",i);
				}else{
					y=body.indexOf(flag.toString(),i);
				} 
				
				if(y<0){
					throw new RuntimeException("String segment flag not found: "+flag.toString());
				}
				
				int z=y-1;
				c=body.charAt(z);
				while(c!='\n' && (c==' '|| c=='\t' || c=='\r')){
					z--;
					c=body.charAt(z);
				}
				if(c=='\n' && body.charAt(z-1)=='\r'){
					z--;
				}
				
				String block=body.substring(i+1,z+1);
				buf.append(block);
				
				return y+flag.length()-1; 
			}
		}
		
		return x;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getVersion() {
		return version;
	}
}
