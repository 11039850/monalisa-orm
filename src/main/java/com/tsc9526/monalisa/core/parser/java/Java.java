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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Java {
	public static String DEFAULT_PAGE_ENCODING ="utf-8";
	 
	public final static String REGX_VERSION ="static\\s+(final\\s+)?(long|Long)\\s+\\$VERSION\\$?\\s*=\\s*[0-9]+L?;";
	 
	protected String filePath;
	protected String body;
	protected long lastModified;
	
	protected String packageName;
	protected String name;
	 
	protected long version=-1;
	
	protected int pVersion1=0;
	protected int pVersion2=0;
	protected boolean naturalIncreasing=false;
	
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
	 
	
	protected void parseBody(String body){
		this.body=body;
		
		Pattern v= Pattern.compile(REGX_VERSION);
		Matcher m=v.matcher(body);
		if(m.find()){
			int p1=m.start();
			int p2=body.indexOf("=",p1);
			int p3=body.indexOf(";",p2);
			
			pVersion1=p2+1;
			pVersion2=p3;
			
			if(body.substring(p1, p2).trim().endsWith("$")){
				naturalIncreasing=true;
			}
			
			String s=body.substring(p2+1,p3).trim();
			if(s.endsWith("L")){
				s=s.substring(0,s.length()-1);
			}
			version=Long.parseLong(s);
		}
		
		 
		parsePackage();
	 
	}
	
	public long increaseVersion(){
		if(version>=0){
			if(naturalIncreasing){
				version++;
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				long newVersion=Long.parseLong(sdf.format(new Date())+"00");
					
				if((version/100) ==(newVersion/100) ){
					version++;
				}else if(newVersion > version){
					version=newVersion;
				}else{
					version++;
				}
			}
		} 
		
		return this.version;
	}
	
	public String replace(String ... fts){
		String c= body;
		if(version>=0){
			c= body.substring(0,pVersion1)+" "+version+"L;"+body.substring(pVersion2+1);
		}
		
		for(int i=0;i<fts.length;i+=2){
			c=c.replace(fts[0], fts[1]);
		}
		return c;
	}
	
	public void replaceAndSave(String ... fts){
		String c=replace(fts);
		
		FileHelper.writeUTF8(new File(filePath),c);
	}
	
	 
	 
	 
	protected void parsePackage(){
		Pattern patternPackage = Pattern.compile("package\\s+[a-z0-9A-Z\\._]+\\s*;");
		Pattern patternClass   = Pattern.compile("(class|interface)\\s+[a-z0-9A-Z\\_]+");
		
		Matcher m=patternPackage.matcher(body);
		if(m.find()){
			String x=m.group();
			packageName=x.substring(7,x.length()-1).trim();
		}
	 
		
		m=patternClass.matcher(body);
		if(m.find()){
			String x=m.group();
			name=x.substring(5,x.length()).trim();
		}
		 
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

	public int getpVersion1() {
		return pVersion1;
	}

	public int getpVersion2() {
		return pVersion2;
	}

	public boolean isNaturalIncreasing() {
		return naturalIncreasing;
	}
}
