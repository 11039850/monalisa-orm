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
package com.tsc9526.monalisa.orm.datatable;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;
import com.tsc9526.monalisa.orm.tools.helper.DynmicLibHelper;
import com.tsc9526.monalisa.orm.tools.helper.JsonHelper;
import com.tsc9526.monalisa.orm.tools.helper.SQLHelper;

 /**
  *  
  * @author zzg.zhou(11039850@qq.com)
  */
public class DataTable<E> extends ArrayList<E> { 
	private static final long serialVersionUID = 6839964505006290332L;
	
	public static DataTable<DataMap> fromCsv(InputStream csvInputStream) {
		return DynmicLibHelper.createCsv().fromCsv(csvInputStream, CsvOptions.createDefaultOptions());
	}
	 
	public static DataTable<DataMap> fromCsv(String csvString){
		return DynmicLibHelper.createCsv().fromCsv(csvString, CsvOptions.createDefaultOptions());
	}
	
	protected List<DataColumn> headers=new ArrayList<DataColumn>();
	
	protected String name="_THIS_TABLE";
	
	public DataTable() {		
	}
	
	public DataTable(Collection<? extends E> cs) {
		super(cs);
	}
	 
	public void saveCsv(OutputStream csvOutputStream){ 
		DynmicLibHelper.createCsv().writeToCsv(this, csvOutputStream,CsvOptions.createDefaultOptions());
	}
 
	/**
	 * 获取指定列数据
	 * 
	 * @param column 列名称
	 * @return 返回指定列名的整列数据
	 */
	public List<Object> getColumn(String column){
		List<Object> rs=new ArrayList<Object>();
		
		for(DataMap m:as(DataMap.class)){
			rs.add( m.get(column) );
		}
		
		return rs;
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
		
		return DynmicLibHelper.createCsv().queryTable(this, sql);
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
		 
		if(groupBy!=null && groupBy.trim().length()>0){
			if(SQLHelper.isStartByKeyWord(groupBy,"GROUP")){
				sql+=" "+groupBy;
			}else{
				sql+=" GROUP BY "+groupBy;
			}
		}
		
		if(orderBy!=null && orderBy.trim().length()>0){
			if(SQLHelper.isStartByKeyWord(orderBy,"ORDER")){
				sql+=" "+orderBy;
			}else{
				sql+=" ORDER BY "+orderBy;
			}
		}
		
		return sql;
	}
	 
 
	/**
	 * Inner Join
	 * 
	 * @param rightTable 连接表     
	 * @param joinFieldNames  连接的字段名称<br>
	 * 	1. 表连接字段，多个字段逗号分隔。<br>
	 * 	2. 如无此参数时，则按2个表中相同的字段名进行连接<br>
	 * 	3. 如左右表连接字段名不同时，需提供2个逗号分开的字段列表<br>
	 * @return result
	 */
	public DataTable<DataMap> join(DataTable<?> rightTable,String... joinFieldNames){
		return new DataTableJoin(this,rightTable,joinFieldNames).doInnerJoin();
	}
	
	
	/**
	 * Left Join
	 * 
	 * @param rightTable  连接表    
	 * @param joinFieldNames  连接的字段名称<br>
	 * 	1. 表连接字段，多个字段逗号分隔。<br>
	 * 	2. 如无此参数时，则按2个表中相同的字段名进行连接<br>
	 * 	3. 如左右表连接字段名不同时，需提供2个逗号分开的字段列表<br>
	 * @return result
	 */
	public DataTable<DataMap> joinLeft(DataTable<?> rightTable,String... joinFieldNames){
		return new DataTableJoin(this,rightTable,joinFieldNames).doLeftJoin();
	}
	
	/**
	 * Right Join
	 * 
	 * @param rightTable   连接表   
	 * @param joinFieldNames  连接的字段名称<br>
	 * 	1. 表连接字段，多个字段逗号分隔。<br>
	 * 	2. 如无此参数时，则按2个表中相同的字段名进行连接<br>
	 * 	3. 如左右表连接字段名不同时，需提供2个逗号分开的字段列表<br>
	 * @return result
	 */
	public DataTable<DataMap> joinRight(DataTable<?> rightTable,String... joinFieldNames){
		return new DataTableJoin(this,rightTable,joinFieldNames).doRightJoin();
	}
	
	/**
	 * Full Join
	 * 
	 * @param rightTable   连接表   
	 * @param joinFieldNames 连接的字段名称 <br>
	 * 	1. 表连接字段，多个字段逗号分隔。<br>
	 * 	2. 如无此参数时，则按2个表中相同的字段名进行连接<br>
	 * 	3. 如左右表连接字段名不同时，需提供2个逗号分开的字段列表<br>
	 * @return result
	 */
	public DataTable<DataMap> joinFull(DataTable<?> rightTable,String... joinFieldNames){
		return new DataTableJoin(this,rightTable,joinFieldNames).doFullJoin();
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
	
	/**
	 * 如未调用 setHeaders(...)指定列名，则列命名按照如下规则定义：<br>
	 * 
	 * 如果表数据为数组, 则列名默认为： c0,c1,c2 ... <br>
	 * 如果表数据为Map,则列名为key值<br>
	 * 如果表数据为对象，则列名为字段名<br>
	 * 如果表数据为原始类型的数据(int,bool 等），则列名为 ： c0
	 * 
	 * @return 列名
	 * 
	 * @see #setHeaders(List)
	 */
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
					}else if(v.getClass().isArray()){
						Object[] xs=(Object[])v;
						for(int k=0;k<xs.length;k++){
							headers.add(new DataColumn("c"+k));
						}
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

	/**
	 * 指定列名
	 * 
	 * @param headers 指定列名
	 * @return 表本身
	 * 
	 * @see #getHeaders()
	 */
	public DataTable<E> setHeaders(List<DataColumn> headers) {
		this.headers = headers;
		return this;
	}
	
	/**
	 * 指定列名
	 * 
	 * @param names 指定列名
	 * @return 表本身
	 * 
	 * @see #getHeaders()
	 */
	public DataTable<E> setHeaders(String... names){
		headers.clear();
		
		if(names!=null) {
			for(String name:names){
				headers.add(new DataColumn(name));
			}
		}
		
		return this;
	}
	
	public String toJson(){
		StringWriter buffer=new StringWriter();
		JsonWriter w=new JsonWriter(buffer);
		w.setSerializeNulls(true);
		
		JsonHelper.writeJson(w,this,true);
		 
		return buffer.toString();
	}
}
