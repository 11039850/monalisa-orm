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
package com.tsc9526.monalisa.tools.datatable;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.tools.string.MelpJson;


/**
 * Page data. The first page is 1, the second is 2 ...
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -33321L;
	
	/**
	 * Total pages
	 */
	private long total  = 0;
	
	/**
	 * Current page no.  first is 1
	 */
	private long page   = 1;		
	
	/**
	 * Total records 
	 */
	private long records= 0;
	
	/**
	 * Page size
	 */
	private long size   = 10;	
	
	/**
	 * Page data
	 */
	private DataTable<T> rows=new DataTable<T>();
 
	public Page(){
	}
	
	/**
	 * Constructor of the page
	 * 
	 * @param rows      records in this page
	 * @param records   total records
	 * @param size      size of page
	 * @param offset    position of the first record. first is 0.
	 */
	public Page(List<T> rows,long records, long size , long offset) {
		if(rows!=null){
			if(rows instanceof DataTable){
				this.rows = (DataTable<T>)rows;
			}else{
				this.rows = new DataTable<T>(rows);
			}
		}else {
			this.rows=null;
		}
		
		this.records= records;
		this.size   = size;
		
		this.page   = 1 + offset/size;
		
		this.total = (int) (this.records / this.size);
		if (this.records % this.size != 0) {
			this.total++;
		}
	}
	
	/**
	 * 
	 * @return the row count in this page
	 */
	public int rows(){
		return rows==null?0:rows.size();
	}
 	
	/**
	 *  
	 * @return the page data or empty list if no data.
	 */
	public DataTable<T> getRows() {
		return rows;
	}	
	 
	/**
	 * 
	 * @return first page: 1, the second is 2 ...
	 */
	public long getPage() {
		return page;
	}
	
 
	/**
	 * 
	 * @return size of page
	 */
	public long getSize() {
		return size;
	}
	 
	/**
	 * 
	 * @return total pages
	 */
	public long getTotal() {
		return total;
	}
		 
	/**
	 * 
	 * @return total records
	 */
	public long getRecords() {
		return records;
	}
	
	
	/**
	 * Copy to new page, and transform the list data to target class
	 * 
	 * @param toClass the target class
	 * @param <X> target class type
	 * @return the new page object
	 */
	public <X> Page<X> as(Class<X> toClass){
		Page<X> page=new Page<X>();
		
		page.page      = this.page;
		page.size  = this.size;
		page.total = this.total;
		page.records  = this.records;
		
		if(rows!=null){
			page.rows=this.rows.as(toClass);
		}
		
		return page;
	}
	 
	
	public String toJson(){
		try{
			StringWriter buffer=new StringWriter();
			JsonWriter w=new JsonWriter(buffer);
			w.setSerializeNulls(true);
			w.beginObject();
			
			w.name("page").value(page);
			w.name("total").value(total);
			w.name("size").value(size);
			w.name("records").value(records);
			w.name("rows");
			MelpJson.writeJson(w,rows,false);
			 
			w.endObject();
			w.close();
			
			return buffer.toString();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
}


