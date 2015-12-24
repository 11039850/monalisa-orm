package com.tsc9526.monalisa.core.query.criteria;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.EnumHelper;
import com.tsc9526.monalisa.core.tools.TypeHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"unchecked"})
public class Field<X,Y extends Criteria<?>>{
	private DataSourceManager dsm=DataSourceManager.getInstance();
	
	private Y criteria;
	private String name;
	private String columnName;
	
	private Query q;
	 
	private String type;
	
	public Field(String name,Y criteria){
		this(name, criteria, Types.INTEGER);
	}
	
	public Field(String name,Y criteria,int jdbcType){
		this.name=name;
		this.criteria=criteria;	
		this.q=criteria.q;
		
		this.type=TypeHelper.getJavaType(jdbcType);		
	}	
		 
	/**
	 * SQL: <code>=</code>
	 */	 
	public Y eq(X value){
		return add(" = ?", value); 
	}
	
	/**
	 * SQL: <code>&lt;&gt;</code>
	 */	
	public Y ne(X value){
		return add(" <> ?", value); 
	}
	
	/**
	 *  SQL: <code>&gt;</code>
	 */
	public Y gt(X value){
		return add(" > ?", value); 
	}
	
	/**
	 * SQL: <code>&gt;=</code>
	 */
	public Y ge(X value){
		return add(" >= ?", value); 
	}
	
	/**
	 * SQL: <code>&lt;</code>
	 */
	public Y lt(X value){
		return add(" < ?", value); 
	}
	
	/**
	 * SQL: <code>&lt;=</code>
	 */
	public Y le(X value){
		return add(" <= ?", value); 
	}

	/**
	 * Example: <br>
	 * <li><code>like("%value%"); -> like '%value%'</code></li>
	 * <li><code>like("value%");  -> like 'value%' </code></li>
	 * <li><code>like("%value");  -> like '%value' </code></li>
	 *  
	 * @param value 
	 */
	public Y like(X value){
		return add(" like ?", value); 
	}		 
	
	/**
	 * SQL: <code>IS NULL</code>
	 */
	public Y isNull(){
		return add(" IS NULL"); 
	}
	
	/**
	 * SQL: <code>IS NOT NULL</code>
	 */
	public Y isNotNull(){
		return add(" IS NOT NULL"); 
	}
	
	/**
	 * 
	 * SQL: <code>BETWEEN ? AND ?</code>
	 *  
	 * @param from  &gt;= from
	 * @param to    &lt;= to
	 * @return
	 */
	public Y between(X from,X to){
		return add(" BETWEEN ? AND ?", from,to); 
	}
	
	/**
	 * SQL: <code>NOT BETWEEN ? AND ?</code>
	 * 
	 * @param from  &lt; from OR
	 * @param to    &gt; to
	 * @return
	 */
	public Y notBetween(X from,X to){
		return add(" NOT BETWEEN ? AND ?", from,to); 
	}
	
	/**
	 * SQL: <code>IN(...)</code>
	 */
	public Y in(X value,X... values){
		if(isIngore(value)){
			return criteria;
		}

		if(q.isEmpty()==false){
			q.add(" AND ");
		}
 		
		q.add(getColumnName()).in(getValues(value,values));
		return criteria;
	}
	
	/**
	 * SQL: <code>NOT IN (...)</code>
	 */
	public Y notin(X value,X... values){
		if(isIngore(value)){
			return criteria;
		}
		
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
	 		 
		q.add(getColumnName()).notin(getValues(value,values));
		return criteria;
	}
	
	/**
	 * SQL: <code>IN(...)=</code>
	 */
	public Y in(List<X> values){		 
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 
		q.add(getColumnName()).in(getValues(values));
		return criteria;
	}
	
	/**
	 * SQL: <code>NOT IN (...)</code>
	 */
	public Y notin(List<X> values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
		  
		q.add(getColumnName()).notin(getValues(values));
		
		return criteria;
	}
	
	/**
	 * SQL: <code>[ORDER BY] ASC</code>
	 */
	public Y asc(){
		criteria.addOrderByAsc(getColumnName());
		
		return criteria;
	}
		
	/**
	 * SQL: <code>[ORDER BY] DESC</code>
	 */
	public Y desc(){
		criteria.addOrderByDesc(getColumnName());
		
		return criteria;
	}

	private Y add(String op,X value,X... values){
		if(isIngore(value)){
			return criteria;
		}
		
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 		
		q.add(getColumnName()).add(op, getValues(value,values));
		
		return criteria;
	}	
	
	private Y add(String op){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 		
		q.add(getColumnName()).add(op);
		
		return criteria;
	}	
	
	
	private List<?> getValues(List<X> values){
		if(values!=null && values.size()>0 && values.get(0).getClass().isEnum()){
			List<Object> vs=new ArrayList<Object>();
			for(int i=0;i<values.size();i++){
				if("String".equals(type)){
					vs.add(EnumHelper.getStringValue((Enum<?>)values.get(i)));
				}else{
					vs.add(EnumHelper.getIntValue((Enum<?>)values.get(i)));
				}					
			}
			return vs;
		}else{
			return values;
		}
	}
	
	private List<Object> getValues(X value,X... values){
		List<Object> vs=new ArrayList<Object>();
		if(value==null){
			vs.add(null);
		}else{
			if(value.getClass().isEnum()){
				if("String".equals(type)){
					vs.add( EnumHelper.getStringValue((Enum<?>)value) );
				}else{
					vs.add( EnumHelper.getIntValue((Enum<?>)value) );
				}	
			}else{
				vs.add(value);
			}
		}
		
		if(values!=null && values.length>0 ){
			if(values[0].getClass().isEnum()){
				for(int i=0;i<values.length;i++){ 
					if("String".equals(type)){
						vs.add( EnumHelper.getStringValue((Enum<?>)values[i]) );
					}else{
						vs.add( EnumHelper.getIntValue((Enum<?>)values[i]) );
					}					
				}
			}else{
				for(int i=0;i<values.length;i++){ 
					vs.add(values[i]);
				}
			}			 
		}
		return vs;
	}
  
	
	protected boolean isIngore(X value){
		int ingore=criteria.getIgnore();
		
		if(ingore==Criteria.IGNORE_NULL && value==null){
			return true;
		}
		
		if(ingore==Criteria.IGNORE_EMPTY){
			if(value==null){
				return true;
			}
			
			if(value instanceof String){
				if( ((String)value).trim().length()<1 ){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private String getColumnName(){
		if(columnName==null){
			this.columnName=dsm.getDialect(q.getDb()).getColumnName(name);			
		}
		return columnName;
	}
	
	  
	public String name(){
		return name;
	}
	
	public static class FieldInteger<Y extends Criteria<?>> extends Field<Integer,Y>{
		public FieldInteger(String name, Y criteria) {
			super(name, criteria);		
		}
		
		/**
		 * SQL: <code>=</code>
		 */	 
		public Y eq(String value){
			if(value==null || value.trim().length()==0){
				return super.eq((Integer)null);
			}else{
				return super.eq(Integer.parseInt(value.trim())); 
			}			 
		}
		
		/**
		 * SQL: <code>&lt;&gt;</code>
		 */	
		public Y ne(String value){
			if(value==null || value.trim().length()==0){
				return super.ne((Integer)null);
			}else{
				return super.ne(Integer.parseInt(value.trim())); 
			}
		}
		
		/**
		 *  SQL: <code>&gt;</code>
		 */
		public Y gt(String value){
			if(value==null || value.trim().length()==0){
				return super.gt((Integer)null);
			}else{
				return super.gt(Integer.parseInt(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&gt;=</code>
		 */
		public Y ge(String value){
			if(value==null || value.trim().length()==0){
				return super.ge((Integer)null);
			}else{
				return super.ge(Integer.parseInt(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&lt;</code>
		 */
		public Y lt(String value){
			if(value==null || value.trim().length()==0){
				return super.lt((Integer)null);
			}else{
				return super.lt(Integer.parseInt(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&lt;=</code>
		 */
		public Y le(String value){
			if(value==null || value.trim().length()==0){
				return super.le((Integer)null);
			}else{
				return super.le(Integer.parseInt(value.trim())); 
			}
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的整型列表
		 */
		public Y in(String valueSplitByComma){
			return in(valueSplitByComma.split(","));
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的整型列表
		 */
		public Y notin(String valueSplitByComma){
			return notin(valueSplitByComma.split(","));
		}
		
		/**
		 * @param values  整型字符串数字
		 */
		public Y in(String[] values){		 
			return super.in(toIntegers(values));
		}
		 
		/**
		 * @param values  整型字符串数字
		 */
		public Y notin(String[] values){		 
			return super.notin(toIntegers(values));
		}
		
		private List<Integer> toIntegers(String[] values){
			List<Integer> xs=new ArrayList<Integer>();
			for(int i=0;i<values.length;i++){
				xs.add(Integer.parseInt(values[i].trim()));
			}
			return xs;
		}
	}
	
	
	public static class FieldLong<Y extends Criteria<?>> extends Field<Long,Y>{
		public FieldLong(String name, Y criteria) {
			super(name, criteria);		
		}
		
		/**
		 * SQL: <code>=</code>
		 */	 
		public Y eq(String value){			 
			if(value==null || value.trim().length()==0){
				return super.eq((Long)null);
			}else{
				return super.eq(Long.parseLong(value.trim())); 
			}			 
		}
		
		/**
		 * SQL: <code>&lt;&gt;</code>
		 */	
		public Y ne(String value){
			if(value==null || value.trim().length()==0){
				return super.ne((Long)null);
			}else{
				return super.ne(Long.parseLong(value.trim())); 
			}
		}
		
		/**
		 *  SQL: <code>&gt;</code>
		 */
		public Y gt(String value){
			if(value==null || value.trim().length()==0){
				return super.gt((Long)null);
			}else{
				return super.gt(Long.parseLong(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&gt;=</code>
		 */
		public Y ge(String value){
			if(value==null || value.trim().length()==0){
				return super.ge((Long)null);
			}else{
				return super.ge(Long.parseLong(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&lt;</code>
		 */
		public Y lt(String value){
			if(value==null || value.trim().length()==0){
				return super.lt((Long)null);
			}else{
				return super.lt(Long.parseLong(value.trim())); 
			}
		}
		
		/**
		 * SQL: <code>&lt;=</code>
		 */
		public Y le(String value){
			if(value==null || value.trim().length()==0){
				return super.le((Long)null);
			}else{
				return super.le(Long.parseLong(value.trim())); 
			}
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的长整型列表
		 */
		public Y in(String valueSplitByComma){
			return in(valueSplitByComma.split(","));
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的长整型列表
		 */
		public Y notin(String valueSplitByComma){
			return notin(valueSplitByComma.split(","));
		}
		
		/**
		 * @param values  长整型字符串数字
		 */
		public Y in(String[] values){			 
			return super.in(toLongs(values));
		}
		
		
		/**
		 * @param values  长整型字符串数字
		 */
		public Y notin(String[] values){			 
			return super.notin(toLongs(values));
		}
		
		private List<Long> toLongs(String[] values){
			List<Long> xs=new ArrayList<Long>();
			for(int i=0;i<values.length;i++){
				xs.add(Long.parseLong(values[i].trim()));
			}
			return xs;
		}
	}
	
	public static class FieldShort<Y extends Criteria<?>> extends Field<Short,Y>{
		public FieldShort(String name, Y criteria) {
			super(name, criteria);		
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的短整型列表
		 */
		public Y in(String valueSplitByComma){
			return in(valueSplitByComma.split(","));
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的短整型列表
		 */
		public Y notin(String valueSplitByComma){
			return notin(valueSplitByComma.split(","));
		}
		
		/**
		 * @param values  短整型字符串数字
		 */
		public Y in(String[] values){		 
			return super.in(toShorts(values));
		}
		
		
		/**
		 * @param values  短整型字符串数字
		 */
		public Y notin(String[] values){			 
			return super.notin(toShorts(values));
		}
		
		private List<Short> toShorts(String[] values){
			List<Short> xs=new ArrayList<Short>();
			for(int i=0;i<values.length;i++){
				xs.add(Short.parseShort(values[i].trim()));
			}
			return xs;
		}
	}
	
	public static class FieldString<Y extends Criteria<?>> extends Field<String,Y>{
		public FieldString(String name, Y criteria) {
			super(name, criteria);		
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的字符串列表
		 */
		public Y ins(String valueSplitByComma){				 
			return in(toStrings(valueSplitByComma));
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的字符串列表
		 */
		public Y notins(String valueSplitByComma){
			return notin(toStrings(valueSplitByComma));
		}		 
		
		private List<String> toStrings(String valueSplitByComma){
			List<String> xs=new ArrayList<String>();
			for(String v:valueSplitByComma.split(",")){			 
				xs.add(v==null?null:v.trim());
			}
			return xs;
		}
	}
}
