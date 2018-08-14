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

import java.io.Serializable;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataColumn implements Serializable{
	private static final long serialVersionUID = 4472740716668557178L;

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
	
	public DataColumn(String name,int jdbcType) {
		this.name=name;
		this.jdbcType=jdbcType;
	}
	
	public DataColumn(String name, Class<?> type) {
		this.name=name;
		this.type=type;
	}

	public String getName() {
		return name;
	}

	public DataColumn setName(String name) {
		this.name = name;
		return this;
	}


	public String getLabel() {
		return label;
	}


	public DataColumn setLabel(String label) {
		this.label = label;
		return this;
	}


	public Class<?> getType() {
		return this.type;
	}


	public DataColumn setType(Class<?> type) {
		this.type = type;
		return this;
	}


	public int getIndex() {
		return index;
	}


	public DataColumn setIndex(int index) {
		this.index = index;
		return this;
	}

	public String getTypeString() {
		return typeString;
	}

	public DataColumn setTypeString(String typeString) {
		this.typeString = typeString;
		return this;
	}

	public String toString(){
		return name;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public DataColumn setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
		return this;
	}
}
