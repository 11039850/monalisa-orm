package com.tsc9526.monalisa.plugin.eclipse.generator;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.text.edits.ReplaceEdit;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.generator.DBMetadata;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.resources.Freemarker;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JavaBeansHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;
import com.tsc9526.monalisa.plugin.eclipse.console.MMC;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SelectMethod {	 
	private SourceUnit unit;
	
	private MethodDeclaration md;
	
	private Annotation selectAnnotation;
	
	private String fingerprint;
	
	private String resultClassName;
	private String buildCode;
	
	private int index;
	
	private boolean forceRenameResultClass=false;
	
	 
	public SelectMethod(SourceUnit unit,MethodDeclaration md,Annotation selectAnnotation){
		this.unit=unit;
		this.md=md;		
		this.selectAnnotation=selectAnnotation;
		
		calculateResultClassName();
		
		calculateFingerprint(null);
	}
	
	public void calculateResultClassName() {
		if(selectAnnotation.isNormalAnnotation()){
			NormalAnnotation n=(NormalAnnotation)selectAnnotation;
			for(Object o:n.values()){
				MemberValuePair mvp=(MemberValuePair)o;
				String name=mvp.getName().toString();
				if(name.equals("name")){
					forceRenameResultClass=true;
					
					resultClassName=getMemberValue(mvp); 
				}else if(name.equals("build")){
					buildCode=getMemberValue(mvp); 
				}
			}
		}
		if(resultClassName==null){
			resultClassName=JavaBeansHelper.getCamelCaseString("Result_"+md.getName().toString(),true);
		}	
	}
	
	
	private String getMemberValue(MemberValuePair mvp){
		Expression expr=mvp.getValue();
		String v=expr.toString();
		if(v.startsWith("\"")){			
			v=v.substring(1,v.length()-1);
		}else{
			//TODO: 处理常量
		}
		v=Helper.escapeStringValue(v);
	 	return v;
	}
	
	public void calculateFingerprint(List<ReplaceEdit> changes) {
		try{			 
			IBuffer buffer=unit.getUnit().getJavaElement().getOpenable().getBuffer();
			 
			String body=buffer.getText(md.getStartPosition(),md.getLength());
			if(changes!=null && changes.size()>0){
				List<ReplaceEdit> copy=new ArrayList<ReplaceEdit>();
				copy.addAll(changes);
				
				Collections.sort(copy, new Comparator<ReplaceEdit>(){ 
					public int compare(ReplaceEdit o1, ReplaceEdit o2) {							 
						return o1.getOffset()-o2.getOffset();
					}						
				});
				
				int px=md.getStartPosition();
				StringBuffer sb=new StringBuffer();
				while(copy.size()>0){
					ReplaceEdit re=copy.get(0);
					int offset=re.getOffset();
					int length=re.getLength();
					
					sb.append(buffer.getText(px,offset-px)).append(re.getText());
					
					px=offset+length;
					
					copy.remove(0);
				}
				sb.append(buffer.getText(px,md.getStartPosition()+md.getLength()-px));
				
				body=sb.toString();
			}
			this.fingerprint=Helper.intToBytesString(body.length()) + Helper.intToBytesString(body.hashCode());
		}catch(Exception e){
			MMC.getConsole().error(e);
		}			 	
	}
	
	public boolean isForceRenameResultClass(){
		return forceRenameResultClass;
	}
	
	public String createResultJavaCode(DBExchange exchange){		 
		String ps="";
		List<?> params=md.parameters();
		for(Object p:params){
			SingleVariableDeclaration svd=(SingleVariableDeclaration)p;
			String ptype=svd.getType().toString();
			 
			if(ps.length()>0){
				ps+=", ";
			}
			ps+=unit.getFullName(ptype);
		}
		
		MetaTable table=exchange.getTable();
		table.setJavaPackage(unit.getSubPackageName());
		 
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
		
		String projectPath=unit.getProjectPath();
		for(MetaColumn c:table.getColumns()){
			String tableName=c.getTable().getName();
			MetaTable columnTable=DBMetadata.getTable(projectPath,exchange.getDbKey(),tableName);
			c.setTable(columnTable);
			if(columnTable!=null){
				MetaColumn cd=columnTable.getColumn(c.getName());
				if(cd!=null){
					c.setAuto(cd.isAuto());
					c.setJavaType(cd.getJavaType());
					c.setJdbcType(cd.getJdbcType());
					c.setKey(cd.isKey());
					c.setLength(cd.getLength());
					c.setNotnull(cd.isNotnull());
					c.setRemarks(cd.getRemarks());
					c.setValue(cd.getValue());	
					
					imps.add(columnTable.getJavaPackage()+"."+columnTable.getJavaName());
					
					importColumn=true;
				}else{
					c.setTable(null);
				}
			}
		}		
		table.setJavaName(resultClassName);
		
		if(importColumn){
			imps.add(Column.class.getName());
		}
		
		String see=unit.getPackageName()+"."+unit.getUnitName()+"#"+md.getName()+"("+ps+")";
		try{
			 Configuration cfg = Freemarker.getFreemarkConfiguration();
	         Template modelTpl = cfg.getTemplate("select.ftl"); 
	         
	         Map<String,Object>  data=new HashMap<String, Object>();
	         data.put("table", table);
	         data.put("see", see);	         	          
	         data.put("imports", imps);
	         data.put("fingerprint", fingerprint);
	         
	         ByteArrayOutputStream bos=new ByteArrayOutputStream();
	         modelTpl.process(data, new OutputStreamWriter(bos,"utf-8"));
	         bos.flush();
	         
	         return new String(bos.toByteArray(),"utf-8");
        }catch(Exception e){
       	 	throw new RuntimeException(e);
        }
		
	}	 
	
	public boolean isChanged(){
		boolean changed=false;
		
		String fullname=unit.getSubPackageName()+"."+resultClassName;
		String fp=unit.getFingerprint(fullname);
		if(fp!=null && fingerprint.equals(fp)==false){
			changed=true;
		} 		
		return changed;
	}
	 
	public void writeRunMethod(JavaWriter writer){
		String method=md.getName().toString();
		StringBuffer sp=new StringBuffer();	
		for(Object p:md.parameters()){
			SingleVariableDeclaration svd=(SingleVariableDeclaration)p;
			Type   t=svd.getType();
			String v=svd.getName().toString();			
		
			if(sp.length()>0){
				sp.append(",");
			}
			sp.append(v);	
			
			method+="_"+t.toString();
			
			method=JavaBeansHelper.getCamelCaseString(method, false);
		}
		
		writer.println("public void "+method+"()throws Exception{");
		writer.println("DBExchange.setExchange("+index+");");
		writer.println(unit.getUnitName()+" X = new "+unit.getUnitName()+"();");
		
		
		final List<String> filterVariables=new ArrayList<String>();
		if(buildCode!=null && buildCode.length()>0){
			String source="public class X{\r\n"+buildCode+ "\r\n}";
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(source.toCharArray());
			ASTNode node=parser.createAST(null);
			node.accept(new ASTVisitor() {
				@Override
				public boolean visit(VariableDeclarationFragment var) {
					String nameString=var.getName().toString(); 
					filterVariables.add(nameString);
					return false;
				}
			});
			writer.println(buildCode);
		}
		
		writeRunParameters(writer,filterVariables);		
				
		writer.println("X."+md.getName()+"("+sp+"); \r\n");
		writer.println("}");		 
	}
	
	
	private void writeRunParameters(JavaWriter writer,List<String> filterVariables){
		for(Object p:md.parameters()){
			SingleVariableDeclaration svd=(SingleVariableDeclaration)p;
			Type   t=svd.getType();
			String v=svd.getName().toString();
			
			if(filterVariables.contains(v) == false){
				Code code=PrimitiveType.VOID;			
				if(t.isPrimitiveType()){
					PrimitiveType pt=(PrimitiveType)t;
					code=pt.getPrimitiveTypeCode();				 
				}else if(t.isArrayType()){				
					writer.println(t+" "+v+" = new "+t+"{};");	
				}else if(t.isSimpleType()){
					String name= ((SimpleType)t).getName().toString();
					if(name.startsWith("java.lang.")){
						name=name.substring(10);
					}
					if(name.equals("Integer")){
						code=PrimitiveType.INT;
					}else if(name.equals("Character")){
						code=PrimitiveType.CHAR;
					}else if(name.equals("Boolean")){
						code=PrimitiveType.BOOLEAN;
					}else if(name.equals("Short")){
						code=PrimitiveType.SHORT;
					}else if(name.equals("Long")){
						code=PrimitiveType.LONG;
					}else if(name.equals("Float")){
						code=PrimitiveType.FLOAT;
					}else if(name.equals("Double")){
						code=PrimitiveType.DOUBLE;
					}else if(name.equals("Byte")){
						code=PrimitiveType.BYTE;
					}else{
						writer.println(t+" "+v+" = new "+t+"();");
					}
				}	
				 
				if(code==PrimitiveType.INT){						 
					writer.println(t+" "+v+" = 0;");
				}else if(code==PrimitiveType.CHAR){						 
					writer.println(t+" "+v+" = '0';");							
				}else if(code==PrimitiveType.BOOLEAN){						 
					writer.println(t+" "+v+" = flase;");							
				}else if(code==PrimitiveType.SHORT){						 
					writer.println(t+" "+v+" = 0;");							
				}else if(code==PrimitiveType.LONG){						 
					writer.println(t+" "+v+" = 0L;");							
				}else if(code==PrimitiveType.FLOAT){						 
					writer.println(t+" "+v+" = 0f;");							
				}else if(code==PrimitiveType.DOUBLE){						 
					writer.println(t+" "+v+" = 0D;");							
				}else if(code==PrimitiveType.BYTE){						 
					writer.println(t+" "+v+" = 0;");							
				}
			}
		}
	}
	
	public String getResultClassName(){
		return resultClassName;
	}
	
	public  String getFingerprint(){		
		return fingerprint;		
	}

	public MethodDeclaration getMd() {
		return md;
	}

	public void setMd(MethodDeclaration md) {
		this.md = md;
	}

	 
	public SourceUnit getUnit() {
		return unit;
	}

	public void setUnit(SourceUnit unit) {
		this.unit = unit;
	}

	public Annotation getSelectAnnotation() {
		return selectAnnotation;
	}

	public void setSelectAnnotation(Annotation selectAnnotation) {
		this.selectAnnotation = selectAnnotation;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	 
	
}
