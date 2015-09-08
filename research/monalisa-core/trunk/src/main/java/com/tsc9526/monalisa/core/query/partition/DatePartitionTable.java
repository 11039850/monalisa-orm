package com.tsc9526.monalisa.core.query.partition;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.JavaBeansHelper;

@SuppressWarnings("rawtypes")
public class DatePartitionTable implements Partition<Model>{	 
	private MetaPartition metaPartition;
	
	public void setMetaPartition(MetaPartition metaPartition){
		this.metaPartition=metaPartition;
		
	}
	
	public MetaPartition getMetaPartition(){
		return this.metaPartition;
	}
	
	public  String verify(MetaTable table) {	
		String[] args=metaPartition.getArgs();
		
		if(args==null || args.length!=2){
			return "Need only 2 parameters. First is date format,  the other is the date field name!";
		}
			
		try{
			SimpleDateFormat sdf=new SimpleDateFormat(args[0]);
			sdf.format(new Date());
		}catch(Exception e){
			return "Invalid date format: "+args[0]+", Error: "+e.getMessage();
		}
		
		MetaColumn column=table.getColumn(args[1]);
		if(column==null){
			return "Field not found: "+args[1]+" in table: "+table.getName();
		}
		
		String javaType=column.getJavaType();
		if(!("Date".equals(javaType) || "java.util.Date".equals(javaType))){
			return "Expect date field: "+args[0]+", but: "+javaType;
		}	
			
		return null;				 
	}
	
	public String getTableName(Model model) {
		String[] args=metaPartition.getArgs();
		
		String ymd=args[0];
		String dateField =JavaBeansHelper.getJavaName(args[1],false);
		if(dateField.length()==args[1].length()){
			dateField=args[1];
		}
		
		MetaClass mc=ClassHelper.getMetaClass(model.getClass());
		FGS fgs=mc.getField(dateField);
		if(fgs==null || fgs.getGetMethod()==null){
			throw new RuntimeException("DateTime field: "+dateField+" not found in modelClass: "+model.getClass().getName());
		}
		
		Object o=fgs.getObject(model);
		if(o==null){
			throw new RuntimeException("DateTime field: "+dateField+" value cannot be null, modelClass: "+model.getClass().getName());
		}else if(o instanceof Date ==false){
			throw new RuntimeException("DateTime field: "+dateField+" is not a java.util.Date, modelClass: "+model.getClass().getName());
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat(ymd);
		String tableName=metaPartition.getTablePrefix()+sdf.format((Date)o);
	 
		return tableName;
	} 
}
