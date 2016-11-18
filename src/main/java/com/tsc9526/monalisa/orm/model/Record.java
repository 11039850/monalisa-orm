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
package com.tsc9526.monalisa.orm.model;

import java.util.Date;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.annotation.Table;
import com.tsc9526.monalisa.orm.criteria.Field;
import com.tsc9526.monalisa.orm.criteria.QEH;
import com.tsc9526.monalisa.orm.dao.Delete;
import com.tsc9526.monalisa.orm.dao.Insert;
import com.tsc9526.monalisa.orm.dao.Select;
import com.tsc9526.monalisa.orm.dao.Update;
import com.tsc9526.monalisa.orm.tools.helper.Helper;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * Simple model with getString, getDate, getInt ...
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class Record extends Model<Record>{ 
	private static final long serialVersionUID = -5264525130494733202L;
	 
	public Insert<Record> INSERT(){
		return new Insert<Record>(this);
	}
	
	public Delete<Record> DELETE(){
		return new Delete<Record>(this);
	}
	
	public Update<Record> UPDATE(Record model){
		return new Update<Record>(model);
	}
	 
	public Select<Record,Select<Record,?>> SELECT(){
		return new Select<Record,Select<Record,?>>(this);
	}
	 
	public Criteria WHERE(){
		return new Example().createCriteria();
	}
	  
	public class Example extends com.tsc9526.monalisa.orm.criteria.Example<Criteria, Model<Record>>{
 
		protected Criteria createInternal() {
			Criteria x= new Criteria(this);
			  			  			
			QEH.getQuery(x).use(db());
			
			return x;
		}
		
	}
	
	public class Criteria extends com.tsc9526.monalisa.orm.criteria.Criteria<Criteria>{
		
		private Example example;
		
		protected Criteria(Example example){
			this.example=example;
		}
		
		public Example getExample(){
			return this.example;
		}
		
		/**
		 * Create Select for example
		 * @return Select for example
		 */
		public Select<Record,Select<Record, ?>>.SelectForExample SELECT(){
			return Record.this.SELECT().selectForExample(example);
		}
		
		/**
		 * Update records with this example
		 * @param model update data to this model
		 * @return number of records
		 */
		public int update(Record model){			 			 
			return UPDATE(model).updateByExample(this.example);
		}
		
		/**
		 * Delete records with this example
		 * @return number of records
		 */
		public int delete(){
			return DELETE().deleteByExample(this.example);
		}
		
		public Field<Object,Criteria> field(String fieldName){
			FGS fgs=Record.this.field(fieldName);
			if(fgs!=null){			
				Column column=fgs.getAnnotation(Column.class);				
				return new Field<Object, Record.Criteria>(column.name(), this,column.jdbcType());
			}else{
				throw new RuntimeException("Field not found: "+fieldName+", in model: "+Record.this.mm().tableName);
			}
		}
		
		/**
		* Append "OR" Criteria  
		* 
		* @return Criteria
		*/	
		public Criteria OR(){
			return this.example.or();
		}
	}

	 
	
	public Record(){
		if(this.getClass()!=Record.class){
			if(this.getClass().getAnnotation(Table.class)==null){
				this.$tableName=this.getClass().getSimpleName();
			}
		}
	}
	
	public Record(String tableName,String... primaryKeys) {
		super(tableName,primaryKeys);
	}

	public String getString(String key,String defaultValue){
		String v=getString(key);
		if(v==null){
			v= defaultValue;
		}
		return v;
	}
	
	public String getString(String key){
		Object v=get(key);
		if(v==null){
			return null;
		}else {
			return v.toString();
		}
	}
	
	public boolean getBool(String key,boolean defaultValue){
		Boolean v=getBoolean(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public Boolean getBoolean(String key){
		Object v=(Object)get(key);
		if(v==null){
			return null;
		}else{
			if(v instanceof Boolean){
				return (Boolean)v;
			}else{
				return Boolean.valueOf(""+v);
			}
		}
	}
	
	public int getInt(String key, int defaultValue){
		Integer v=getInteger(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public Integer getInteger(String key){
		Object v=(Object)get(key);
		if(v==null){
			return null;
		}else{
			if(v instanceof Integer){
				return (Integer)v;
			}else{
				return Integer.parseInt(""+v);
			}
		}
	}
	
	public long getLong(String key, long defaultValue){
		Long v=getLong(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public Long getLong(String key){
		Object v=(Object)get(key);
		if(v==null){
			return null;
		}else{
			if(v instanceof Long){
				return (Long)v;
			}else{
				return Long.parseLong(""+v);
			}
		}
	}
	
	public float getFloat(String key, float defaultValue){
		Float v=getFloat(key);
		if(v==null){
			v=defaultValue;
		}
		return v;
	}
	
	public Float getFloat(String key){
		Object v=(Object)get(key);
		if(v==null){
			return null;
		}else{
			if(v instanceof Float){
				return (Float)v;
			}else{
				return Float.parseFloat(""+v);
			}
		}
	}
	
	public Double getDouble(String key){
		Object v=(Object)get(key);
		if(v==null){
			return null;
		}else{
			if(v instanceof Double){
				return (Double)v;
			}else{
				return Double.parseDouble(""+v);
			}
		}
	}
	
	/**
	 * Auto detect the date format:<br>
	 * yyyy-MM-dd<br>
	 * yyyy-MM-dd HH<br>
	 * yyyy-MM-dd HH:mm<br>
	 * yyyy-MM-dd HH:mm:ss<br>
	 *   
	 * @param key name of the column
	 * 
	 * @return date
	 */
	public Date getDate(String key){
		return getDate(key,null,null);
	}
	
	/**
	 * @param key name of the column
	 * @param defaultValue default value
	 * @return date
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key, Date defaultValue){
		return getDate(key,null,defaultValue);		  
	}
	
	/**
	 * 
	 * @param key name of the column
	 * @param format  new SimpleDateFormat(format): auto detect date format if null or ''
	 * @param defaultValue default value
	 * @return date
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key,String format,Date defaultValue){		 
		return Helper.toDate(get(key), format, defaultValue); 
	}

}
