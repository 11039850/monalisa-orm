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
package com.tsc9526.monalisa.orm.partition;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tsc9526.monalisa.orm.meta.MetaColumn;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpJavaBeans;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public class DatePartitionTable implements Partition<Model>{	 
	private static final long serialVersionUID = 5743936516178062272L;

	public  String verify(MetaPartition mp,MetaTable table) {	
		String[] args=mp.getArgs();
		
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
	
	public String getTableName(MetaPartition mp,Model model) {
		String[] args=mp.getArgs();
		
		String ymd=args[0];
		String dateField =MelpJavaBeans.getJavaName(args[1],false);
		if(dateField.length()==args[1].length()){
			dateField=args[1];
		}
		
		ClassHelper mc=MelpClass.getClassHelper(model.getClass());
		FGS fgs=mc.getField(dateField);
		if(fgs==null){
			throw new RuntimeException("DateTime field: "+dateField+" not found in modelClass: "+model.getClass().getName());
		}
		
		Object o=fgs.getObject(model);
		if(o==null){
			throw new RuntimeException("DateTime field: "+dateField+" value cannot be null, modelClass: "+model.getClass().getName());
		}else if(o instanceof Date ==false){
			throw new RuntimeException("DateTime field: "+dateField+" is not a java.util.Date, modelClass: "+model.getClass().getName());
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat(ymd);
		String tableName=mp.getTablePrefix()+sdf.format((Date)o);
	 
		return tableName;
	} 
}
