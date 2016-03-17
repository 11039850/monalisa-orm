package com.tsc9526.monalisa.core.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import bsh.This;

import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JavaBeansHelper;
import com.tsc9526.monalisa.core.tools.SQLHelper;
import com.tsc9526.monalisa.core.tools.TypeHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBExchange implements Serializable {
	private static final long serialVersionUID = 5069827028195702115L;
	
	public final static String EXCHANGE_NAME=DBExchange.class.getName()+".EXCHANGE_NAME";	
		
	public static DBExchange getExchange() {
		String v=System.getProperty(DBExchange.EXCHANGE_NAME);
	 	
		if(v!=null){
			if(v.startsWith("@")){
			 	int index=Integer.parseInt(v.substring(1));
				 			
				DBExchange exchange=new DBExchange();
				exchange.setIndex(index);
				 
				return exchange;				
			}else{
				try{
					return deserial(v);
				}catch(Exception e){
					Helper.throwRuntimeException(e);
				}				
			}			
		}
		
		return null;		
	}
		
	
	public static void setExchange(DBExchange exchange){
		try{
			System.setProperty(DBExchange.EXCHANGE_NAME,serial(exchange));		 
		}catch(Exception e){
			Helper.throwRuntimeException(e);
		}
	}
	
	public static void setExchange(int index){
		System.setProperty(DBExchange.EXCHANGE_NAME,"@"+index);
		 
	}
	
	private static String serial(DBExchange exchange)throws IOException{
		ByteArrayOutputStream bufArrayOutputStream=new ByteArrayOutputStream();
		ObjectOutputStream outputStream=new ObjectOutputStream(bufArrayOutputStream);
		outputStream.writeObject(exchange);
		outputStream.flush();
		
		return Helper.bytesToHexString(bufArrayOutputStream.toByteArray());
			
	}
	
	private static DBExchange deserial(String s)throws Exception{
		byte[] b=Helper.hexStringToBytes(s);
		ByteArrayInputStream bInputStream=new ByteArrayInputStream(b);
		ObjectInputStream inputStream=new ObjectInputStream(bInputStream);
		DBExchange exchange=(DBExchange)inputStream.readObject();
		
		return exchange;
	}
	
	
	public static boolean doExchange(Query query){
		DBExchange exchange=DBExchange.getExchange();
		if(exchange!=null){
			String psql=query.getExecutableSQL();
			exchange.setSql(psql);
			
			Connection conn=null;
			try{
				exchange.setDbKey(query.getDb().getCfg().getKey());				
				
				conn=DataSourceManager.getInstance().getDataSource(query.getDb()).getConnection();
				PreparedStatement pst=conn.prepareStatement(query.getSql());
				SQLHelper.setPreparedParameters(pst, query.getParameters());
			 				
				ResultSet rs=pst.executeQuery();
				ResultSetMetaData rsmd=rs.getMetaData();
				MetaTable table=new MetaTable();
				int cc=rsmd.getColumnCount();
				for(int i=1;i<=cc;i++){
					String type=TypeHelper.getJavaType(rsmd.getColumnType(i));
					String name =rsmd.getColumnName(i);
					String label=rsmd.getColumnLabel(i);
					
					MetaColumn column=new MetaColumn();
					String tableName=rsmd.getTableName(i);	
					column.setTable(new MetaTable(tableName));
					 
					column.setName(name);
					
					if(label!=null && label.trim().length()>0){
						column.setJavaName(JavaBeansHelper.getJavaName(label, false));
					}
					
					column.setJavaType(type);					
					table.addColumn(column); 					
				}
									 
				exchange.setTable(table);
				exchange.setErrorString(null);
				 
				rs.close();
				pst.close();											
			}catch(Exception e){				 
				StringWriter s=new StringWriter();
				e.printStackTrace(new PrintWriter(s));				
				exchange.setErrorString(s.toString());				 
			}finally{
				CloseQuietly.close(conn);
			}
			 
			DBExchange.setExchange(exchange);
			return true;
		}else{
			return false;
		}
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
