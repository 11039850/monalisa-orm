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
package com.tsc9526.monalisa.orm.mqs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.generator.DBExchange;
import com.tsc9526.monalisa.orm.generator.DBMetadata;
import com.tsc9526.monalisa.orm.generator.DBWriterSelect;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.mqs.query.QueryStatement;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.io.JavaWriter;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.template.Args;
import com.tsc9526.monalisa.tools.template.jsp.JspContext;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SQLGenerator {
	static Logger logger=Logger.getLogger(SQLGenerator.class.getName());
	
	private SQLResourceManager sqlResourceManager=SQLResourceManager.getInstance();
	
	private String srcDir;
	
	public SQLGenerator(String srcDir,String resourceDir){
		this.srcDir=srcDir;
	}
	
	public void generateFiles(){
		try{
			writeQueryInterface();
			
			writeQueryResultClass();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private void writeQueryInterface()throws Exception{
		for(SQLClass cs:sqlResourceManager.getSqlClasses().values()){
			File dir=new File(srcDir,cs.getPackageName().replace(".","/"));
			MelpFile.mkdirs(dir);
			
			File java=new File(dir,cs.getClassName()+".java");
			logger.info("Parse "+cs.getSqlFile().getAbsolutePath()+" to "+java.getAbsolutePath());
			
			JavaWriter writer=new JavaWriter(java);
			
			writer.write("package "+cs.getPackageName()+";\r\n\r\n");
			writer.write("import "+Query.class.getName()+";\r\n\r\n");
			writer.write("import "+SQLResourceManager.class.getName()+";\r\n\r\n");
			writer.write("public class "+cs.getClassName()+"{\r\n");
			
			String sqlrm=SQLResourceManager.class.getSimpleName();
			writer.write("\tprivate static "+sqlrm+" SQLRM="+sqlrm+".getInstance();\r\n");
			
			for(QueryStatement qs:cs.getStatements()){
				String xs1="", xs2="";
				List<String> args =qs.getArgs();
				for(String s:args){
					int x=s.indexOf("=");
					String v1=s.substring(0,x).trim();
					String v2=v1.split("\\s+")[1];
					
					if(xs1.length()>0){
						xs1+=", ";
					}
					xs1+=v1;
					
					xs2+=","+v2;
				}
				
				
				String queryId=cs.getPackageName()+"."+cs.getClassName()+"."+qs.getId();
				
				String comments=qs.getComments();
				if(comments!=null){
					comments=comments.replace("*/", "==");
				}
				if(comments!=null){
					writer.write("\t/**\r\n");
					writer.write(comments);
					writer.write("\t*/\r\n");
				}
				writer.write("\tpublic final static String "+qs.getId()+"=\""+queryId+"\";\r\n");
				
				if(comments!=null){
					writer.write("\t/**\r\n");
					writer.write(comments);
					writer.write("\t*/\r\n");
				}
				writer.write("\tpublic static Query "+qs.getId()+"("+xs1+"){\r\n");
				writer.write("\t\t return SQLRM.createQuery("+qs.getId()+xs2+"); \r\n");
				
				writer.write("\t}\r\n\r\n");
			}
			writer.write("}");
			 
			writer.close();
		}
	}
	
	private void writeQueryResultClass()throws Exception{
		Map<String,String> hrs=new HashMap<String, String>();
		
		//从SQL查询语句生成Java结果类
		for(SQLClass cs:sqlResourceManager.getSqlClasses().values()){
			for(QueryStatement qs:cs.getStatements()){
				String resultClass=qs.getResultClass();
				if(resultClass!=null && resultClass.length()>0){
					File target=new File(srcDir,resultClass.replace(".","/")+".java");
					
					String sqlSource=hrs.get(target.getAbsolutePath());
					if(sqlSource!=null){
						logger.warn("Ignore result class: "+cs.getSqlFile().getAbsolutePath()+"::"+qs.getId()+" {Exist: "+sqlSource+"}");
						continue;
					}
					
					if(target.exists()){
						String source=MelpFile.readToString(new FileInputStream(target), "utf-8");
						if(source.indexOf("FINGERPRINT")<0){
							logger.info("Ignore result class: "+resultClass+"{"+cs.getSqlFile().getAbsolutePath()+"::"+qs.getId()+"}, FINGERPRINT not found in the file: "+target.getAbsolutePath());
							continue;
						}
					}
					
					String queryId=cs.getPackageName()+"."+cs.getClassName()+"."+qs.getId();
					
					Args args=new Args();
					int arg_num=qs.getArgs().size();
					for(int i=0;i<arg_num;i++){
						args.push(null);
					}
					
					Query q=sqlResourceManager.createQuery(queryId, args); 
					DBExchange.setExchange(new DBExchange());
					q.getResult();
					 
					DBExchange exchange=DBExchange.getExchange(true);
					if(exchange.getErrorString()==null){
						createResultJavaCode(srcDir,exchange,resultClass);
						
						hrs.put(target.getAbsolutePath(),cs.getSqlFile().getAbsolutePath()+"::"+qs.getId());
					}else{
						throw new RuntimeException(exchange.getErrorString());
					}
					
				}
			}
		}
	}
	
	
	private void createResultJavaCode(String srcDir,DBExchange exchange,String resultClass)throws IOException{	
		int x=resultClass.lastIndexOf(".");
		String packageName=resultClass.substring(0,x);
		String javaName=resultClass.substring(x+1);
		 
		MetaTable table=exchange.getTable();
		table.setJavaPackage(packageName);
		
		Set<String> imps=new LinkedHashSet<String>();
		boolean importColumn=false;
		
		for(MetaColumn c:table.getColumns()){
			String tableName=c.getTable().getName();
			MetaTable columnTable=DBMetadata.getTable(exchange.getDbKey(),tableName);
			c.setTable(columnTable);
			if(columnTable!=null){
				MetaColumn cd=columnTable.getColumn(c.getName());
				if(cd!=null){
					c.setAuto(cd.isAuto());
					 
					c.setJdbcType(cd.getJdbcType());
					 
					c.setKey(cd.isKey());
					c.setLength(cd.getLength());
					c.setNotnull(cd.isNotnull());
					c.setRemarks(cd.getRemarks());
					c.setValue(cd.getValue());	
					
					String javaType=cd.getJavaType();
					if(cd.isEnum()){
						if(javaType.indexOf(".")<0){
							javaType=cd.getTable().getJavaName()+"."+javaType;
						}
					}
					c.setJavaType(javaType);
					
					imps.add(columnTable.getJavaPackage()+"."+columnTable.getJavaName());
					imps.addAll(c.getImports());
					
					importColumn=true;
				}else{
					c.setTable(null);
				}
			}
		}		
		table.setJavaName(javaName);
		
		if(importColumn){
			imps.add(Column.class.getName());
		}
		
		try{
			JspContext request = new JspContext();
			 
			request.setAttribute("table", table);
	        request.setAttribute("imports", imps);
	        request.setAttribute("see", "");
	        request.setAttribute("fingerprint", "");
	          
	        ByteArrayOutputStream bos=new ByteArrayOutputStream();
	        
	        DBWriterSelect dbs=new DBWriterSelect();
	        dbs.service(request,new PrintWriter(new OutputStreamWriter(bos,"utf-8")));
		      
	        File dir=new File(srcDir,packageName.replace(".","/"));
			MelpFile.mkdirs(dir);
				
			File java=new File(dir,javaName+".java");
			logger.info("Write result class: "+resultClass+" to "+java.getAbsolutePath());
			MelpFile.write(java, bos.toByteArray()); 
        }catch(Exception e){
       	 	throw new RuntimeException(e);
        }
		
	}	 
}
