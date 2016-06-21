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
package com.tsc9526.monalisa.core.query.datatable;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.relique.jdbc.csv.CsvRawReader;
import org.relique.jdbc.csv.DataTableResultSet;
import org.relique.jdbc.csv.SqlParser;

import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.CsvHelper;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.SQLHelper;

 /**
  *  
  * @author zzg.zhou(11039850@qq.com)
  */
public class DataTable<E> extends ArrayList<E> { 
	private static final long serialVersionUID = 6839964505006290332L;
	
	public static DataTable<DataMap> fromCsv(InputStream csvInputStream,CsvOptions options) {
		String csvString=FileHelper.readToString(csvInputStream, options.getCharset());
		
		return fromCsv(csvString, options);
	}
	 
	public static DataTable<DataMap> fromCsv(String csvString,CsvOptions options){
		try {
			DataTable<DataMap> table=new DataTable<DataMap>();
			
			CsvRawReader reader = CsvHelper.loadCsvRawReader(csvString, options);
			
			String[] columns=reader.getColumnNames();
			table.setHeaders(columns);
			
			while(reader.next()){
				String[] vs=reader.getFieldValues();
				
				DataMap m=new DataMap();
				for(int i=0;i<columns.length;i++){
					m.put(columns[i], i<vs.length?vs[i]:null);
					
				}
				table.add(m);
			}
			
			reader.close();
			
			return table;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected List<DataColumn> headers=new ArrayList<DataColumn>();
	
	protected String name="_THIS_TABLE";
	
	public DataTable() {		
	}
	
	public DataTable(Collection<? extends E> cs) {
		super(cs);
	}
	 
	public void saveCsv(OutputStream csvOutputStream,CsvOptions options){ 
		CsvHelper.writeToCsv(this, csvOutputStream, options);
	}
 
	/**
	 * 
	 * @param columns  SELECT fields,        null or ""  means: *(all fields)
	 * @param where    WHERE statement,      null or ""  means: all records 
	 * @param orderBy  ORDER BY statement,   null or ""  means: no order by
	 * @param groupBy  GROUP By statement,   null or ""  means: no group by
	 * 
	 * @return the first record. null if no result.
	 */
	public DataMap selectOne(String columns,String where,String orderBy,String groupBy){
		DataTable<DataMap> rs=select(columns, where, orderBy, groupBy);
		if(rs.size()>0){
			return rs.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param columns  SELECT fields,        null or ""  means: *(all fields)
	 * @param where    WHERE statement,      null or ""  means: all records 
	 * @param orderBy  ORDER BY statement,   null or ""  means: no order by
	 * @param groupBy  GROUP By statement,   null or ""  means: no group by
	 * 
	 * @return data
	 */
	public DataTable<DataMap> select(String columns,String where,String orderBy,String groupBy){
		String sql=getSQL(columns, where, orderBy, groupBy);
		
		SqlParser parser = new SqlParser();
		try
		{
			parser.parse(sql);
					
			ResultSet rs = new DataTableResultSet( 
				new DataTableReader(this),
				name,
				parser.getColumns(),
				parser.isDistinct(),
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				parser.getWhereClause(),
				parser.getGroupByColumns(),
				parser.getHavingClause(),
				parser.getOrderByColumns(),
				parser.getLimit(),
				parser.getOffset());
			
			DataTable<DataMap> rtable=new DataTable<DataMap>();
			while(rs.next()){
				DataMap row=new DataMap();
				loadToMap(rs,row);
				rtable.add(row);
			}
			rs.close();
			
			return rtable;
		}catch (Exception e){
			throw new RuntimeException("SQL exception: " + sql+"\r\nHeaders: \r\n"+getHeaders(),e);
		}
	}
	
	protected String getSQL(String columns,String where,String orderBy,String groupBy){
		if(columns==null || columns.trim().length()==0){
			columns="*";
		}
		
		String sql="SELECT "+columns+" FROM "+name;
		
		if(where!=null && where.trim().length()>0){
			if(SQLHelper.isStartByKeyWord(where,"WHERE")){
				sql+=" "+where;
			}else{
				sql+=" WHERE "+where;
			}
		}
		
		if(orderBy!=null && orderBy.trim().length()>0){
			if(SQLHelper.isStartByKeyWord(orderBy,"ORDER")){
				sql+=" "+orderBy;
			}else{
				sql+=" ORDER BY "+orderBy;
			}
		}
		
		if(groupBy!=null && groupBy.trim().length()>0){
			if(SQLHelper.isStartByKeyWord(groupBy,"GROUP")){
				sql+=" "+groupBy;
			}else{
				sql+=" GROUP BY "+groupBy;
			}
		}
		return sql;
	}
	 
 
	/**
	 * Full Join
	 * 
	 * @param rightTable      
	 * @param leftFieldNames  左表连接字段，多个字段逗号分隔
	 * @param rightFieldNames 右表连接字段，多个字段逗号分隔
	 * @return result
	 */
	public DataTable<DataMap> join(DataTable<?> rightTable,String leftFieldNames,String rightFieldNames){
		return new DataTableJoin(this,rightTable,leftFieldNames,rightFieldNames).doFullJoin();
	}
	
	
	/**
	 * Left Join
	 * 
	 * @param rightTable      
	 * @param leftFieldNames  左表连接字段，多个字段逗号分隔
	 * @param rightFieldNames 右表连接字段，多个字段逗号分隔
	 * @return result
	 */
	public DataTable<DataMap> joinLeft(DataTable<?> rightTable,String leftFieldNames,String rightFieldNames){
		return new DataTableJoin(this,rightTable,leftFieldNames,rightFieldNames).doLeftJoin();
	}
	
	/**
	 * Inner Join
	 * 
	 * @param rightTable      
	 * @param leftFieldNames  左表连接字段，多个字段逗号分隔
	 * @param rightFieldNames 右表连接字段，多个字段逗号分隔
	 * @return result
	 */
	public DataTable<DataMap> joinInner(DataTable<?> rightTable,String leftFieldNames,String rightFieldNames){
		return new DataTableJoin(this,rightTable,leftFieldNames,rightFieldNames).doInnerJoin();
	}
	
	/**
	 * Right Join
	 * 
	 * @param rightTable      
	 * @param leftFieldNames  左表连接字段，多个字段逗号分隔
	 * @param rightFieldNames 右表连接字段，多个字段逗号分隔
	 * @return result
	 */
	public DataTable<DataMap> joinRight(DataTable<?> rightTable,String leftFieldNames,String rightFieldNames){
		return new DataTableJoin(this,rightTable,leftFieldNames,rightFieldNames).doRightJoin();
	}
	
	
	public Page<E> getPage(int limit,int offset){
		DataTable<E> list=new DataTable<E>();
		
		for(int i=offset;list.size()<limit && i<size();i++){
			list.add(get(i));
		}
		
		return new Page<E>(list,size(),limit,offset);
	}
	
	@SuppressWarnings("unchecked")
	public <T> DataTable<T> as(Class<T> toClass){
		if(size()>0 && get(0)!=null && toClass.isAssignableFrom(get(0).getClass())){
			return (DataTable<T>)this;
		}
		
		DataTable<T> r=new DataTable<T>();
		r.headers=headers;
		
		for(int i=0;i<size();i++){
			E from=this.get(i);
			
			if(from instanceof DataMap){
				r.add(((DataMap)from).as(toClass));
			}else{
				T to=(T)ClassHelper.convert(from, toClass);
				r.add(to);
			}
		}
		
		return r;
	}
	 
	protected DataMap loadToMap(ResultSet rs, DataMap map) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();

		Map<String, Integer> xs = new HashMap<String, Integer>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}
			name = name.toLowerCase();

			Integer n = xs.get(name);
			if (n != null) {
				map.put(name + n, rs.getObject(i));

				xs.put(name, n + 1);
			} else {
				xs.put(name, 1);

				map.put(name, rs.getObject(i));
			}
		}

		return map;
	}
	
	 
	
	public synchronized List<DataColumn> getHeaders() {
		if(headers.size()==0 && this.size()>0){
			Object v=this.get(0);
			if(v!=null){
				if(v instanceof Map){
					for(Object key:((Map<?,?>)v).keySet()){
					 	headers.add(new DataColumn(""+key));
					}
				}else{
					if(v.getClass().isPrimitive() || v.getClass().getName().startsWith("java.")){
						headers.add(new DataColumn("c0"));
					}else{					
						MetaClass mc=ClassHelper.getMetaClass(v.getClass());
						for(FGS fgs:mc.getFields()){
							headers.add(new DataColumn(fgs.getFieldName()));
						}
					}
				}
			}
		}
		return headers;
	}

	public DataTable<E> setHeaders(List<DataColumn> headers) {
		this.headers = headers;
		return this;
	}
	
	public DataTable<E> setHeaders(String... names){
		headers.clear();
		
		if(names!=null) {
			for(String name:names){
				headers.add(new DataColumn(name));
			}
		}
		
		return this;
	}
	 
}
