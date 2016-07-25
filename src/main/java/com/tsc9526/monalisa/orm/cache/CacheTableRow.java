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
package com.tsc9526.monalisa.orm.cache;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheTableRow {
	private String catalog;
	private String schema;
	private String table;
	
	private long createTime;
	private long updateTime;
	
	public CacheTableRow(){
	}
	
	public CacheTableRow(String catalog, String schema, String table) {
		super();
		this.catalog = catalog;
		this.schema = schema;
		this.table = table;
	}
	
	 
	public String getCatalog() {
		return catalog;
	}
	
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public boolean equals(Object other){
		if(other==this){
			return true;
		}
		
		if(other instanceof CacheTableRow){
			CacheTableRow o=(CacheTableRow)other;
			
			return o.toString().equalsIgnoreCase(toString());
		}
		 
		return false;
		
	}
	
	public int hashCode(){
		return toString().hashCode();
	}
	
	public String toString(){
		return "catalog: "+catalog+", schema: "+schema+", table: "+table;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
