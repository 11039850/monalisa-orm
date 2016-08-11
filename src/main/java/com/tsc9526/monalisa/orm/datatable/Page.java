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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.orm.tools.helper.JsonHelper;


/**
 * Page data
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -33321L;
	
	private DataTable<T> list=new DataTable<T>();	
	 
	private long pageNo   = 1;				 
	private long pageSize = 10;				 
	private long totalPage= 0;				 
	private long totalRow = 0;				 
 
	public Page(){
	}
	
	/**
	 * Constructor of the page
	 * 
	 * @param list    records in this page
	 * @param total   total records
	 * @param limit   size of page
	 * @param offset  position of the first record. first is 0.
	 */
	public Page(List<T> list,long total, long limit , long offset) {
		if(list!=null){
			if(list instanceof DataTable){
				this.list = (DataTable<T>)list;
			}else{
				this.list = new DataTable<T>(list);
			}
		}else {
			this.list=null;
		}
		
		this.totalRow   = total;
		this.pageSize   = limit;
		
		this.pageNo = 1 + offset/limit;
		
		this.totalPage = (int) (this.totalRow / this.pageSize);
		if (this.totalRow % this.pageSize != 0) {
			this.totalPage++;
		}
	}
	
	/**
	 * 
	 * @return rows in this page
	 */
	public int getRows(){
		return list==null?0:list.size();
	}
	
	
	/**
	 * Copy to new page, and transform the list data to target class
	 * 
	 * @param toClass the target class
	 * @return the new page object
	 */
	public <X> Page<X> as(Class<X> toClass){
		Page<X> page=new Page<X>();
		
		page.pageNo    = pageNo;
		page.pageSize  = pageSize;
		page.totalPage = totalPage;
		page.totalRow  = totalRow;
		
		if(list!=null){
			page.list=list.as(toClass);
		}
		
		return page;
	}
	 
	/**
	 *  
	 * @return non null data list
	 */
	public DataTable<T> getList() {
		return list;
	}	
	 
	/**
	 * 
	 * @return first page: 1, the second is 2 ...
	 */
	public long getPageNo() {
		return pageNo;
	}
	
 
	/**
	 * 
	 * @return size of page
	 */
	public long getPageSize() {
		return pageSize;
	}
	 
	/**
	 * 
	 * @return total pages
	 */
	public long getTotalPage() {
		return totalPage;
	}
		 
	/**
	 * 
	 * @return total records
	 */
	public long getTotalRow() {
		return totalRow;
	}
	
	
	public String toJson(){
		try{
			StringWriter buffer=new StringWriter();
			JsonWriter w=new JsonWriter(buffer);
			w.setSerializeNulls(true);
			w.beginObject();
			
			w.name("pageNo").value(pageNo);
			w.name("pageSize").value(pageSize);
			w.name("totalPage").value(totalPage);
			w.name("totalRow").value(totalRow);
			
			w.name("list");
			JsonHelper.writeJson(w,list,false);
			 
			w.endObject();
			w.close();
			
			return buffer.toString();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
}


