package com.tsc9526.monalisa.core.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

}
