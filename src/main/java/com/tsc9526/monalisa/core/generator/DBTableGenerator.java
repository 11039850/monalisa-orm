package com.tsc9526.monalisa.core.generator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Criteria;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.Field;
import com.tsc9526.monalisa.core.query.dao.Select;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.GeneratorHelper;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBTableGenerator {
	private MetaTable table;
	private String superClass;
	private String superInterface;
	
	public DBTableGenerator(MetaTable table,String superClass,String superInterface){
		this.table=table;
		this.superClass=superClass;
		this.superInterface=superInterface;
	}
	
	private String getClassName(){
		return table.getJavaName();
	}
	
	public void generate(OutputStream os){	 					
		JavaWriter pw = new JavaWriter(os);
		pw.setAutoIndent(true);
		
		pw.println("package "+table.getJavaPackage()+";");
		
		pw.println("");
		 
		pw.println("import "+DB.class.getName()+";");		
		pw.println("import "+Table.class.getName()+";");
		pw.println("import "+Column.class.getName()+";");		  		 
		pw.println("import "+ClassHelper.class.getName()+";");	
		
		if(table.getKeyColumns().size()>0){
			pw.println("import "+Query.class.getName()+";");
		}
		
		pw.println("");
		
		if(!Helper.isEmpty(table.getName())){
			String annoString="@Table(name=\""+table.getName()+"\"";
			if(!Helper.isEmpty(table.getRemarks())){
				annoString+=", remark=\""+table.getRemarks()+"\"";
			}
			annoString+=")";
			pw.println(annoString);
		}
			
		 
		pw.println("public class "+getClassName()+" extends "+superClass+"<"+getClassName()+"> implements "+superInterface+"{");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String serialid=sdf.format(new Date());
		
		pw.println("private static final long serialVersionUID = "+serialid+"L;");
		pw.println("");		 
		pw.println("public static final Select SELECT =new Select(new "+getClassName()+"());");		 
		pw.println("");
		
		generatorConstructor(pw);
		 
		GeneratorHelper.generatorJavaProperties(table,pw);			
		 
		GeneratorHelper.generatorJavaGet(table,pw);
		
		GeneratorHelper.generatorJavaSet(table,pw);
		
		generatorExample(pw);
		
		generatorSelectByPrimary(pw);
		
		generatorMetadata(pw);
		
		pw.println("}");
		pw.flush();
		pw.close(); 
				
	}	
	
	private void generatorExample(JavaWriter pw){		 
		pw.println("");
		pw.println("public static Criteria createCriteria(){");
		pw.println("return new Example().createCriteria();");
		pw.println("}");
		pw.println("");
		
		pw.println("public static class Example extends "+Example.class.getName()+"<Criteria>{");			
		pw.println("protected Criteria createInternal(){");
		pw.println("Criteria x= new Criteria(this);");
		pw.println("DB db=ClassHelper.findAnnotation("+getClassName()+".class,DB.class);");
		pw.println("x.getQuery().use(db);");
		pw.println("return x;");
		pw.println("}");
		pw.println("}");
		pw.println("");
		
		pw.println("public static class Criteria extends "+Criteria.class.getName()+"{");
		
		pw.println("");
		pw.println("private Example example;");
		
		pw.println("");
		pw.println("private Criteria(Example example){");
		pw.println("this.example=example;");
		pw.println("}");
		
		pw.println("");
		pw.println("public Example getExample(){");
		pw.println("return this.example;");
		pw.println("}");
		
		pw.println("");		
		for(MetaColumn c:table.getColumns()){	
			String xs=Field.class.getName()+"<";
			xs+=c.getJavaType();
			xs+=",Criteria>";
			
			StringBuffer f=new StringBuffer();
			f.append("public ").append(xs).append(" ").append(c.getJavaName());
			f.append(" = new ").append(xs).append("(\"").append(c.getName()).append("\", this);");
			
			GeneratorHelper.generatorColumnComment(table, c, pw);
			pw.println(f.toString());
		}
		pw.println("}");
		pw.println("");
	}
	
	private void generatorSelectByPrimary(JavaWriter pw) {
		pw.println("public static class Select extends "+Select.class.getName()+"<"+getClassName()+">{");
		
		pw.println("public Select("+getClassName()+" x){");
		pw.println("super(x);");
		pw.println("}");
		
		List<MetaColumn> keys=table.getKeyColumns();
		if(keys.size()>0){
			StringBuffer ps=new StringBuffer();
			StringBuffer pv=new StringBuffer();
			
			pw.println("");
			pw.println("/**");
			for(MetaColumn c:keys){
				if(ps.length()>0){
					ps.append(", ");
					pv.append(", ");
				}
				ps.append(c.getJavaType()+" "+c.getJavaName());
				pv.append(c.getJavaName());
				
				pw.println(" * @param "+c.getJavaName() + ( c.getRemarks()==null? "":" "+c.getRemarks() ));
				 
			}			
			pw.println(" */");			
			
			pw.println("public "+getClassName()+" selectByPrimaryKey("+ps+"){");
			pw.println(getClassName()+" model = new "+getClassName()+"();");
			for(MetaColumn c:keys){
				pw.println("model."+c.getJavaName()+" = "+c.getJavaName()+";");
			}
			pw.println("@SuppressWarnings(\"rawtypes\")");
			pw.println("Query query=model.getDialect().load(model);");
			pw.println("query.use(model.db);");
			pw.println("return ("+getClassName()+")query.getResult();"); 			 	 
			pw.println("}");	
		}		
		pw.println("}");
		pw.println("");
	}
 
	private void generatorConstructor(JavaWriter pw) {
		List<MetaColumn> keys=table.getKeyColumns();
		 
		pw.println("public "+table.getJavaName()+"(){");		
		pw.println("}");
		pw.println("");
	  
		if(keys.size()>0){			 
			StringBuffer ps=new StringBuffer();
			pw.println("/**");
			pw.println(" * Constructor use primary keys.");
			for(MetaColumn c:keys){
				if(ps.length()>0){
					ps.append(", ");
				}
				ps.append(c.getJavaType()+" "+c.getJavaName());
				
				pw.println(" * @param "+c.getJavaName() + ( c.getRemarks()==null? "":" "+c.getRemarks() ));
			}
			pw.println(" */");	
			 
			pw.println("public "+table.getJavaName()+"("+ps+"){");
			
			for(MetaColumn c:keys){
				pw.println("this."+c.getJavaName()+" = "+c.getJavaName()+";");
			}
			pw.println("}");					
			 
			pw.println("");	
		}
	}
	 

	private void generatorMetadata(PrintWriter pw){
		pw.println("");
		pw.println("");
		pw.println("public static class Metadata{");
		pw.println("public final static String table =\""+table.getName()+"\" ;");		
		for(MetaColumn c:table.getColumns()){			 
			pw.println("");
			pw.println("public final static String  "+c.getJavaName()+"$name    = "+ formatStringValue(c.getName())+" ;");
			pw.println("public final static boolean "+c.getJavaName()+"$key     = "+ c.isKey()+" ;");
			pw.println("public final static int     "+c.getJavaName()+"$length  = "+ c.getLength()+" ;");
			pw.println("public final static String  "+c.getJavaName()+"$value   = "+ formatStringValue(c.getValue())+" ;");
			pw.println("public final static String  "+c.getJavaName()+"$remarks = "+ formatStringValue(c.getRemarks())+" ;");
			pw.println("public final static boolean "+c.getJavaName()+"$auto    = "+ c.isAuto()   +" ;");
			pw.println("public final static boolean "+c.getJavaName()+"$notnull = "+ c.isNotnull()+" ;");			
		}
		pw.println("}");
	}
	
	
	private String formatStringValue(String v){
		if(v==null){
			return "\"NULL\"";
		}else{		
			v=v.replace("\"", "\\\"");
			v=v.replace("\r", "\\r");
			v=v.replace("\n", "\\n");					 
			return "\""+v+"\"";
		}
		
	}
}
