package com.tsc9526.monalisa.core.parser.executor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.generator.DBMetadata;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.parser.query.QueryStatement;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.resources.Freemarker;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SQLGenerator {
	static Log logger=LogFactory.getLog(SQLGenerator.class.getName());
	
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
			FileHelper.mkdirs(dir);
			
			File java=new File(dir,cs.getClassName()+".java");
			logger.info("Parse "+cs.getSqlFile().getAbsolutePath()+" to "+java.getAbsolutePath());
			JavaWriter writer=new JavaWriter(java);
			
			writer.write("package "+cs.getPackageName()+";\r\n\r\n");
			writer.write("import "+Query.class.getName()+";\r\n\r\n");
			writer.write("public class "+cs.getClassName()+"{\r\n");
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
				writer.write("\t\t return Query.create("+qs.getId()+xs2+"); \r\n");
				
				writer.write("\t}\r\n\r\n");
			}
			writer.write("}");
			 
			writer.close();
		}
	}
	
	private void writeQueryResultClass()throws Exception{
		//从SQL查询语句生成Java结果类
		for(SQLClass cs:sqlResourceManager.getSqlClasses().values()){
			for(QueryStatement qs:cs.getStatements()){
				String resultClass=qs.getResultClass();
				if(resultClass!=null && resultClass.length()>0){
					
					File target=new File(srcDir,resultClass.replace(".","/")+".java");
					if(target.exists()){
						String source=FileHelper.readToString(new FileInputStream(target), "utf-8");
						if(source.indexOf("FINGERPRINT")<0){
							logger.info("Ignore generate result class: "+resultClass+", FINGERPRINT not found in the file: "+target.getAbsolutePath());
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
					
					DBExchange exchange=new DBExchange();
					DBExchange.setExchange(exchange);
					q.getResult();
					
					exchange.getSql();
					
					exchange=DBExchange.getExchange();
					createResultJavaCode(srcDir,exchange,resultClass);
					
					DBExchange.setExchange(null);
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
		 
		Map<String,List<MetaColumn>> duplicateNames=new HashMap<String,List<MetaColumn>>();
		for(MetaColumn c:table.getColumns()){
			String fieldName=c.getJavaName();
			List<MetaColumn> mcs=duplicateNames.get(fieldName);
			if(mcs==null){
				mcs=new ArrayList<MetaColumn>();		
				duplicateNames.put(fieldName, mcs);
			}
			mcs.add(c);
		}
		for(List<MetaColumn> mcs:duplicateNames.values()){
			if(mcs.size()>1){
				for(MetaColumn c:mcs){
					String cname=c.getTable().getJavaName()+"$"+c.getJavaName();				
					c.setJavaName(cname);
				}
			}
		}
		 
		
		Set<String> imps=new LinkedHashSet<String>();
		boolean importColumn=false;
		
		String projectPath=".";
		for(MetaColumn c:table.getColumns()){
			String tableName=c.getTable().getName();
			MetaTable columnTable=DBMetadata.getTable(projectPath,exchange.getDbKey(),tableName);
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
			 Configuration cfg = Freemarker.getFreemarkConfiguration();
	         Template modelTpl = cfg.getTemplate("select.ftl"); 
	         
	         Map<String,Object>  data=new HashMap<String, Object>();
	         data.put("table", table);
	         data.put("imports", imps);
	         data.put("see", "");
	         data.put("fingerprint", "");
	         
	         ByteArrayOutputStream bos=new ByteArrayOutputStream();
	         modelTpl.process(data, new OutputStreamWriter(bos,"utf-8"));
	         bos.flush();	         
	          
	         File dir=new File(srcDir,packageName.replace(".","/"));
			 FileHelper.mkdirs(dir);
				
			 File java=new File(dir,javaName+".java");
			 logger.info("Write result class: "+resultClass+" to "+java.getAbsolutePath());
			 FileHelper.write(java, bos.toByteArray());
	         
        }catch(Exception e){
       	 	throw new RuntimeException(e);
        }
		
	}	 
}
