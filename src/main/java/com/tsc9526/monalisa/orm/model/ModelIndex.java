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
package com.tsc9526.monalisa.orm.model;

import java.util.List;

import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ModelIndex{
	/**
	 * 索引名
	 */
	private String name;
	
	/**
	 * 索引类型
	 */
	private int      type;
	
	/**
	 * 是否唯一性索引
	 */
	private boolean  unique;
	
	/**
	 * 索引字段
	 */
	private List<FGS> fields;
	
	private boolean primary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public List<FGS> getFields() {
		return fields;
	}

	public void setFields(List<FGS> fields) {
		this.fields = fields;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
}