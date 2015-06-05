package com.tsc9526.monalisa.core.query.criteria;

import java.util.List;

import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.query.Query;

@SuppressWarnings({"unchecked"})
public class Field<X,Y extends Criteria>{
	private DataSourceManager dsm=DataSourceManager.getInstance();
	
	private Y criteria;
	private String name;
	private String formatName;
	
	private Query q;
	 
	public Field(String name,Y criteria){
		this.name=name;
		this.criteria=criteria;	
		this.q=criteria.getQuery();
		
	}	
		 
	public Y equalsTo(X value){
		return add(" = ?", value); 
	}
	
	public Y notEqualsTo(X value){
		return add(" <> ?", value); 
	}
	
	public Y greatThan(X value){
		return add(" > ?", value); 
	}
	
	public Y greatThanOrEqualsTo(X value){
		return add(" >= ?", value); 
	}
	
	public Y lessThan(X value){
		return add(" < ?", value); 
	}
	
	public Y lessThanOrEqualsTo(X value){
		return add(" <= ?", value); 
	}

	/**
	 * SQL Like statement, for example:  like("%name%");
	 * 
	 * @param value 
	 */
	public Y like(X value){
		return add(" like ?", value); 
	}	 
	
	public Y isNull(){
		return add(" IS NULL"); 
	}
	
	public Y isNotNull(){
		return add(" IS NOT NULL"); 
	}
	
	public Y between(X from,X to){
		return add(" BETWEEN ? AND ?", from,to); 
	}
	
	public Y in(X[] values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 
		q.add(getFormatName()).in(values);
		return criteria;
	}
	
	public Y notin(X[] values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
	 		 
		q.add(getFormatName()).notin(values);
		return criteria;
	}
	
	public Y in(List<X> values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 
		q.add(getFormatName()).in(values);
		return criteria;
	}
	
	public Y notin(List<X> values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
		
		q.add(getFormatName()).notin(values);
		return criteria;
	}
	
	public Y asc(){
		criteria.addOrderByAsc(getFormatName());
		
		return criteria;
	}
		
	public Y desc(){
		criteria.addOrderByDesc(getFormatName());
		
		return criteria;
	}

	private Y add(String op,X... values){
		if(q.isEmpty()==false){
			q.add(" AND ");
		}
				 
		q.add(getFormatName()).add(op, values);
		return criteria;
	}	
	
	private String getFormatName(){
		if(formatName==null){
			this.formatName=dsm.getDialect(q.getDb()).getColumnName(name);			
		}
		return formatName;
	}
	
	  
	public String name(){
		return name;
	}
	
	public static class FieldInteger<Y extends Criteria> extends Field<Integer,Y>{
		public FieldInteger(String name, Y criteria) {
			super(name, criteria);		
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
			Integer[] x=toIntegers(values);
			return super.in(x);
		}
		 
		/**
		 * @param values  整型字符串数字
		 */
		public Y notin(String[] values){
			Integer[] x=toIntegers(values);
			return super.notin(x);
		}
		
		private Integer[] toIntegers(String[] values){
			Integer[] x=new Integer[values.length];
			for(int i=0;i<values.length;i++){
				x[i]=Integer.parseInt(values[i].trim());
			}
			return x;
		}
	}
	
	
	public static class FieldLong<Y extends Criteria> extends Field<Long,Y>{
		public FieldLong(String name, Y criteria) {
			super(name, criteria);		
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
			Long[] x=toLongs(values);
			return super.in(x);
		}
		
		
		/**
		 * @param values  长整型字符串数字
		 */
		public Y notin(String[] values){
			Long[] x=toLongs(values);
			return super.notin(x);
		}
		
		private Long[] toLongs(String[] values){
			Long[] x=new Long[values.length];
			for(int i=0;i<values.length;i++){
				x[i]=Long.parseLong(values[i].trim());
			}
			return x;
		}
	}
	
	public static class FieldShort<Y extends Criteria> extends Field<Short,Y>{
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
			Short[] x=toShorts(values);
			return super.in(x);
		}
		
		
		/**
		 * @param values  短整型字符串数字
		 */
		public Y notin(String[] values){
			Short[] x=toShorts(values);
			return super.notin(x);
		}
		
		private Short[] toShorts(String[] values){
			Short[] x=new Short[values.length];
			for(int i=0;i<values.length;i++){
				x[i]=Short.parseShort(values[i].trim());
			}
			return x;
		}
	}
	
	public static class FieldString<Y extends Criteria> extends Field<String,Y>{
		public FieldString(String name, Y criteria) {
			super(name, criteria);		
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的字符串列表
		 */
		public Y in(String valueSplitByComma){			 
			return in(valueSplitByComma.split(","));
		}
		
		/**
		 * @param valueSplitByComma  逗号分隔的字符串列表
		 */
		public Y notin(String valueSplitByComma){
			return notin(valueSplitByComma.split(","));
		}		 
	}
}
