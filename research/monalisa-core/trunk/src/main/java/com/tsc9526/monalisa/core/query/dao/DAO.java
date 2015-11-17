package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.query.model.Model;

public class DAO{
	private Model<?> model;
	
	public DAO(Model<?> model) {
		this.model=model;		 
	}
	
	public int save(){
		return 0;
	}

}
