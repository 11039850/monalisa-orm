package com.tsc9526.monalisa.core.query.model;

import java.util.List;

import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

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
	
}