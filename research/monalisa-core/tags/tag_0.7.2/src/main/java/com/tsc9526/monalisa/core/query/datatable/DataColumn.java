package com.tsc9526.monalisa.core.query.datatable;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataColumn {
	private String name;
	
	private String label;
	
	private Class<?> type;
	
	private int index;	
	
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

}
