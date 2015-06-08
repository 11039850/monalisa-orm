package com.tsc9526.monalisa.core.query.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.query.Query;

public abstract class Criteria {	
	private Query q=new Query();
	
	private List<String> orderBy=new ArrayList<String>();
	
	public Query getQuery() {
		 
		return q;
	}
	
	void addOrderByAsc(String field){
		addOrderBy(field+" ASC");
	}
	
	void addOrderByDesc(String field){
		addOrderBy(field+" DESC");
	}
	
	private void addOrderBy(String by){
		if(!orderBy.contains(by)){
			orderBy.add(by);		 
		}	
	}
	
	List<String> getOrderBy(){
		return orderBy;
	}
}
