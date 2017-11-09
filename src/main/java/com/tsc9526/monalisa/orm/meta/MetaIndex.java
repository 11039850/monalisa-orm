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
package com.tsc9526.monalisa.orm.meta;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.annotation.Index;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MetaIndex implements Serializable{	 
	private static final long serialVersionUID = -2784842725414559141L;

	/**
	 * 索引名称
	 */
	private String   name;
	
	/**
	 * 索引类型
	 */
	private int      type;
	
	/**
	 * 是否唯一性索引
	 */
	private boolean  unique;
	
	 
	/**
	 * 索引的字段
	 */
	private List<MetaColumn> columns=new ArrayList<MetaColumn>();

	
	public MetaIndex addColumn(MetaColumn c,int index){
		columns.add(index,c);
		return this;
	}
 
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

	public List<MetaColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<MetaColumn> columns) {
		this.columns = columns;
	}

	public Index toIndexAnnotation() {
		return new Index() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return Index.class;
			}
			
			@Override
			public boolean unique() {
				return isUnique();
			}
			
			@Override
			public int type() {
				return getType();
			}
			
			@Override
			public String name() {
				return getName();
			}
			
			@Override
			public String[] fields() {
				List<String> fs=new ArrayList<String>();
				for(MetaColumn c:getColumns()){
					fs.add(c.getName());
				}
				
				return fs.toArray(new String[0]);
			}
		};
	 
	}

}
