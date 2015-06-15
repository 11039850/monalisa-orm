package com.tsc9526.monalisa.core.query.dao;


public interface ModelParser<T> { 
	public boolean parseModel(Model<?> m,T data);
}
