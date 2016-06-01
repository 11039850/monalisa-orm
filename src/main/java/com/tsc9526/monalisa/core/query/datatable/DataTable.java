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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

 /**
  *  
  * @author zzg.zhou(11039850@qq.com)
  */
@SuppressWarnings("unchecked")
public class DataTable<E> extends ArrayList<E> { 
	private static final long serialVersionUID = 6839964505006290332L;
	
	private List<DataColumn> headers=new ArrayList<DataColumn>();
		
	public DataTable() {		
	}
	
	public DataTable(Collection<? extends E> cs) {
		super(cs);
	}
	
	public Page<E> getPage(int limit,int offset){
		DataTable<E> list=new DataTable<E>();
		
		for(int i=offset;list.size()<limit && i<size();i++){
			list.add(get(i));
		}
		
		return new Page<E>(list,size(),limit,offset);
	}
	
	public <T> DataTable<T> as(Class<T> toClass){
		DataTable<T> r=new DataTable<T>();
		r.headers=headers;
		
		for(int i=0;i<size();i++){
			E from=this.get(i);
			
			if(from instanceof DataRow){
				r.add(((DataRow)from).as(toClass));
			}else{
				T to=(T)ClassHelper.convert(from, toClass);
				r.add(to);
			}
		}
		
		return r;
	}
	
	public DataTable<DataRow> select(String columns,String where,String orderBy,String groupBy){
		return null;
	}	 
	
	public DataTable<DataRow> join(DataTable<?> rightTable, String leftFieldName,String rightFieldName){
		return null;
	}

	public DataTable<DataRow> where(String where,String orderBy){
		return select("*",where,orderBy,null);
	}
	
	public DataTable<DataRow> join(DataTable<?> rightTable, String joinFieldName){
		return join(rightTable, joinFieldName,joinFieldName);
	}
	
	public synchronized List<DataColumn> getHeaders() {
		if(headers.size()==0 && this.size()>0){
			Object v=this.get(0);
			if(v!=null){
				if(v instanceof Map){
					int c=((Map<?,?>)v).size();
					for(int i=0;i<c;i++){
						headers.add(new DataColumn("c"+i));
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

	public void setHeaders(List<DataColumn> headers) {
		this.headers = headers;
	}

}
