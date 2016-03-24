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
package com.tsc9526.monalisa.core.query.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.tools.CaseInsensitiveMap;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ModelHolder implements Serializable {
	private static final long serialVersionUID = 703976566431364671L;
	
	protected boolean     fieldFilterExcludeMode = true;	
	protected Set<String> fieldFilterSets		 = new LinkedHashSet<String>();
	
	protected boolean     updateKey = false;
	protected boolean     readonly  = false;
	protected boolean     dirty     = true;
	protected boolean     entity    = false;
	
	protected CaseInsensitiveMap<Object> hModelValues=null;
	
	//javaName
	protected Set<String> 		 changedFields=new LinkedHashSet<String>();
 
	protected Model<?> model;
	
	public ModelHolder(Model<?> model){
		this.model=model;
	}	
	
	protected CaseInsensitiveMap<Object> getModelValues() {
		if(hModelValues==null){
			hModelValues=new CaseInsensitiveMap<Object>();
		}
		return hModelValues;
	}
	
	protected void set(String name,Object value){
		getModelValues().put(name, value);		
		fieldChanged(name);
	}
	
	protected Object get(String name){
		return getModelValues().get(name);
	}
	
	
	public Collection<FGS> changedFields(){
		Set<FGS> fields=new LinkedHashSet<FGS>();
		
		for(String name:changedFields){
			FGS fgs=model.field(name);
			if(fgs!=null){
				fields.add(fgs);
			}
		}
		
		//Fill user defined fields without annotation: @Column
		for(FGS fgs:model.fields()){
			Field f=fgs.getField();
			if(f!=null && f.getAnnotation(Column.class)==null){
				fields.add(fgs);		
			}
		}
		
		return fields;
	}
	
	/**
	 * 
	 * @return 逗号分隔的字段名， *： 表示返回所有字段
	 */
	public String filterFields(){
		if(fieldFilterSets.size()>0){	
			Set<String> fs=new LinkedHashSet<String>();			 
			//Add primary key
			for(FGS fgs:model.fields()){
				Column c=fgs.getAnnotation(Column.class);
				if(c.key()){
					fs.add(model.dialect().getColumnName(c.name()));
					 
				}
			}			
			
			if(fieldFilterExcludeMode){				
				for(FGS fgs:model.fields()){
					Column c=fgs.getAnnotation(Column.class);
					String f=model.dialect().getColumnName(c.name());
					if(fieldFilterSets.contains(f.toLowerCase()) == false && fs.contains(f)==false){
						fs.add(f);						
					}
				}								
			}else{
				for(String f:fieldFilterSets){
					f=model.dialect().getColumnName(f);
					if(fs.contains(f)==false){
						fs.add(f);
					}
				}
			}
			
			if(fs.size()>0){
				StringBuffer sb=new StringBuffer(); 
				for(String f:fs){
					if(sb.length()>0){
						sb.append(", ");
					}
					sb.append(f);
				}
				return sb.toString();
			}else {
				return "*";
			}
		}else{
			return "*";
		}
	}
	
	
	/**
	 * 排除表的某些字段。 用于在查询表时， 过滤掉某些不必要的字段
	 * 
	 * @param fields 要排除的字段名
	 * 
	 * @return  模型本身
	 */
	public void exclude(String ... fields){		 
		fieldFilterExcludeMode=true;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:Helper.fieldsToArrays(fields)){				 
				fieldFilterSets.add(model.dialect().getColumnName(f.toLowerCase()));
			}
		}		 
	}
	
	 
		
	/**
	 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
	 */
	public void excludeBlobs(){
		excludeBlobs(Short.MAX_VALUE);
	}
	
	/**
	 *  排除超过指定长度的字段
	 * 
	 * @param maxLength  字段长度
	 * 
	 */
	public void excludeBlobs(int maxLength){
		List<String> es=new ArrayList<String>();
		
		for(FGS fgs:model.fields()){
			Column column=fgs.getAnnotation(Column.class);
			if(column.length()>=maxLength){
				es.add(model.dialect().getColumnName(column.name().toLowerCase()));
			}
		}
		exclude(es.toArray(new String[0]));
	}
	
	/**
	 * 只提取某些字段
	 * 
	 * @param fields  需要的字段名称	 
	 */
	public void include(String ... fields){		 
		fieldFilterExcludeMode=false;
		
		if(fields==null || fields.length==0){
			fieldFilterSets.clear();
		}else{
			for(String f:Helper.fieldsToArrays(fields)){
				fieldFilterSets.add(model.dialect().getColumnName(f.toLowerCase()));
			}
		}
		 
	}		
	
	public void fieldChanged(String fieldJavaName){
		if(!changedFields.contains(fieldJavaName)){
			changedFields.add(fieldJavaName);
		}
		
		dirty=true;
	}
	
	public void clearChanges(){
		changedFields.clear();
		
		dirty=false;
	}
	
}
