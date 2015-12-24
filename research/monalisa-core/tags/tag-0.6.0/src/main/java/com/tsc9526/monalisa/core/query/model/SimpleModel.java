package com.tsc9526.monalisa.core.query.model;

import java.util.Date;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.query.criteria.Field;
import com.tsc9526.monalisa.core.query.criteria.QEH;
import com.tsc9526.monalisa.core.query.dao.Delete;
import com.tsc9526.monalisa.core.query.dao.Insert;
import com.tsc9526.monalisa.core.query.dao.Select;
import com.tsc9526.monalisa.core.query.dao.Update;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * Simple model with getString, getDate, getInt ...
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class SimpleModel extends Model<SimpleModel>{ 
	private static final long serialVersionUID = -5264525130494733202L;
	 
	public Insert<SimpleModel> INSERT(){
		return new Insert<SimpleModel>(this);
	}
	
	public Delete<SimpleModel> DELETE(){
		return new Delete<SimpleModel>(this);
	}
	
	public Update<SimpleModel> UPDATE(SimpleModel model){
		return new Update<SimpleModel>(model);
	}
	 
	public Select<SimpleModel,Select<SimpleModel,?>> SELECT(){
		return new Select<SimpleModel,Select<SimpleModel,?>>(this);
	}
	 
	public Criteria WHERE(){
		return new Example().createCriteria();
	}
	  
	public class Example extends com.tsc9526.monalisa.core.query.criteria.Example<Criteria, Model<SimpleModel>>{
 
		protected Criteria createInternal() {
			Criteria x= new Criteria(this);
			  			  			
			QEH.getQuery(x).use(db());
			
			return x;
		}
		
	}
	
	public class Criteria extends com.tsc9526.monalisa.core.query.criteria.Criteria<Criteria>{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		/**
		 * Create Select for example
		 */
		public Select<SimpleModel,Select<SimpleModel, ?>>.SelectForExample forSelect(){
			return SELECT().selectForExample(example);
		}
		
		/**
		 * Create Update for example
		 */
		public Update<SimpleModel>.UpdateForExample forUpdate(SimpleModel model){			 			 
			return UPDATE(model).updateForExample(this.example);
		}
		
		/**
		 * Create Delete for example
		 */
		public Delete<SimpleModel>.DeleteForExample forDelete(){
			return DELETE().deleteForExample(this.example);
		}
		
		public Field<Object,Criteria> field(String fieldName){
			FGS fgs=SimpleModel.this.field(fieldName);
			if(fgs!=null){			
				Column column=fgs.getAnnotation(Column.class);				
				return new Field<Object, SimpleModel.Criteria>(column.name(), this,column.jdbcType());
			}else{
				throw new RuntimeException("Field not found: "+fieldName+", in model: "+SimpleModel.this.mm().tableName);
			}
		}
	}

	 
	
	public SimpleModel(){
		if(this.getClass()!=SimpleModel.class){
			if(this.getClass().getAnnotation(Table.class)==null){
				this.TABLE_NAME=this.getClass().getSimpleName();
			}
		}
	}
	
	public SimpleModel(String tableName,String... primaryKeys) {
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
	 * Auto detect the date format:
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy-MM-dd HH</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 *   
	 * @param key
	 * 
	 * @return
	 */
	public Date getDate(String key){
		return getDate(key,null,null);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key, Date defaultValue){
		return getDate(key,null,defaultValue);		  
	}
	
	/**
	 * 
	 * @param key
	 * @param format  new SimpleDateFormat(format): auto detect date format if null or ''
	 * @param defaultValue
	 * @return
	 * 
	 * @see #getDate(String)
	 */
	public Date getDate(String key,String format,Date defaultValue){		 
		return Helper.toDate(get(key), format, defaultValue); 
	}

}
