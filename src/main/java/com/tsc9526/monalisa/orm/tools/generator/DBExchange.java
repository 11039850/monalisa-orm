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
package com.tsc9526.monalisa.orm.tools.generator;

import java.io.Serializable;

import com.tsc9526.monalisa.orm.meta.MetaTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBExchange implements Serializable {
	private static final long serialVersionUID = 5069827028195702115L;
 		
	private static ThreadLocal<DBExchange> localExchange=new ThreadLocal<DBExchange>();
	public static void setExchange(DBExchange exchange){
		localExchange.set(exchange);
	}
	 
	public static DBExchange getExchange(boolean remove){
		DBExchange exchange=localExchange.get();
		if(remove && exchange!=null){
			localExchange.remove();
		}
		return exchange;
	}
	
	public static void setExchange(int index){
		DBExchange exchange=new DBExchange();
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
