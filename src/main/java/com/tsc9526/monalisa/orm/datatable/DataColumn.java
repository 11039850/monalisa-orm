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

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataColumn {
	private String name;
	
	private String label;
	
	private Class<?> type=String.class;
	
	private int index;	
	
	private String typeString="String";
	
	private int jdbcType;
	
	public DataColumn() {
		
	}

	public DataColumn(String name) {
		this.name=name;
	}
	
	public DataColumn(String name, Class<?> type) {
		this.name=name;
		this.type=type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public Class<?> getType() {
		return this.type;
	}


	public void setType(Class<?> type) {
		this.type = type;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String toString(){
		return name;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}
}
