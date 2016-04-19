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
package com.tsc9526.monalisa.core.parser.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.parser.jsp.JspCode;
import com.tsc9526.monalisa.core.parser.jsp.JspElement;
import com.tsc9526.monalisa.core.parser.jsp.JspEval;
import com.tsc9526.monalisa.core.parser.jsp.JspText;
import com.tsc9526.monalisa.core.tools.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class QueryStatement { 
	static Logger logger=Logger.getLogger(QueryStatement.class.getName());
	
	
	private QueryPackage queryPackage;
	
	private String comments;
	private String id;
	private String db;
	private String resultClass;
	
	private List<JspElement> elements=new ArrayList<JspElement>();
	 
	private final static String REGX_VAR="\\$[a-zA-Z_]+[a-zA-Z_0-9]*";
	private final static String REGX_ARG="[_a-z_A-Z]+[_a-z_A-Z0-9]*\\s+[_a-z_A-Z]+[_a-z_A-Z0-9]*\\s*=\\s*args.pop\\s*\\(.*;";
	
	private Pattern patternVar = Pattern.compile(REGX_VAR);
	private Pattern patternArg = Pattern.compile(REGX_ARG);
	
	private Method method;
	
	public void write(JavaWriter writer){
		writer.append("public void ").append(id).append("(Query q,Args args){\r\n");
		writeUseDb(writer); 
		writer.append("JspPageOut out=new JspPageOut(q.getPrintWriter());\r\n");
		writeElements(writer);
		writer.append("}\r\n\r\n");
	}
	 
	
	public void add(JspElement e){
		elements.add(e);
	}
	
	public List<JspElement> getElements(){
		return elements;
	}
	
	public List<String> getArgs(){
		List<String> args=new ArrayList<String>();
		
		for(JspElement e:elements){
			if(e instanceof JspCode){
				String code=e.getCode(); 
	
				Matcher m=patternArg.matcher(code);
				while(m.find()){
					String var=m.group();
					 
					args.add(var);
				}
			}
		}
		
		return args;
	}
	
	protected void writeElements(JavaWriter writer){
		for(JspElement e:elements){
			if(e instanceof JspText){
				String code=e.getCode();
			 	
				String[] lines=code.split("\n");
				boolean lastLN=code.endsWith("\n");
				
				for(int i=0;i<lines.length;i++){
					String line=lines[i];
					 
					List<String> vars=new ArrayList<String>();
					Matcher m=patternVar.matcher(line);
					while(m.find()){
						String var=m.group();
						vars.add(var.substring(1));
					}
					
					String s=line.replaceAll(REGX_VAR, "?");
					writer.append("q.add(\"").append(s.replaceAll("\"", "\\\\\"")).append("\"");
					if(vars.size()>0){
						for(String v:vars){
							writer.append(","+v);
						}
					}
					
					if(lastLN || i< lines.length-1){
						writer.append(").add(\"\\r\\n\");\r\n");
					}else{
						writer.append(");\r\n");
					}
				}
			}else if(e instanceof JspEval){
				String code=e.getCode();
				writer.append("q.add(").append(code).append(");\r\n");
			}else if(e instanceof JspCode){
				writer.append(e.getCode());
			}
		}
	}
	
	private void writeUseDb(JavaWriter writer){
		String db=this.db;
		if(db==null || db.length()<1){
			db=queryPackage.getDb();
		}
		 
		if(db!=null && db.length()>0){
			if(db.endsWith(".class")){
				writer.append("q.use(DBConfig.fromClass("+db+"));\r\n");
			}else if(db.endsWith(".DB")){
				writer.append("q.use("+db+");\r\n");
			}else{
				int x=db.indexOf(".class.getName");
				if(x>0){
					writer.append("q.use(DBConfig.fromClass("+db.substring(0,x+6)+"));\r\n");
				}else{
					writer.append("q.use(DBConfig.fromClass("+db+".class));\r\n");
				}
			}
		}
	}

	public QueryPackage getQueryPackage() {
		return queryPackage;
	}

	public void setQueryPackage(QueryPackage queryPackage) {
		this.queryPackage = queryPackage;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		if(db!=null)db=db.trim();
		
		this.db = db;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getResultClass() {
		return resultClass;
	}

	public void setResultClass(String resultClass) {
		if(resultClass!=null && resultClass.endsWith(".class")){
			resultClass=resultClass.substring(0,resultClass.length()-6);
		}
		this.resultClass = resultClass;
	}
	 
}
