package com.tsc9526.monalisa.core.query;

import java.io.Serializable;

import com.tsc9526.monalisa.core.meta.MetaTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class QExchange implements Serializable {
	private static final long serialVersionUID = 5069827028195702115L;
 		
	private static ThreadLocal<QExchange> localExchange=new ThreadLocal<QExchange>();
	public static void setExchange(QExchange exchange){
		localExchange.set(exchange);
	}
	 
	public static QExchange getExchange(boolean remove){
		QExchange exchange=localExchange.get();
		if(remove && exchange!=null){
			localExchange.remove();
		}
		return exchange;
	}
	
	public static void setExchange(int index){
		QExchange exchange=new QExchange();
		exchange.setIndex(index);
		setExchange(exchange);
	}
	
	
	private int   index;
	
	private MetaTable table;
	
	private String sql;
	
	private String errorString="NOT RUN!";
 	
	private String dbKey;
	
	public MetaTable getTable() {
		return table;
	}
	public void setTable(MetaTable table) {
		this.table = table;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}	 

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


	public String getDbKey() {
		return dbKey;
	}


	public void setDbKey(String dbKey) {
		this.dbKey = dbKey;
	}
}
