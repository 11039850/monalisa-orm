package com.tsc9526.monalisa.core.query.model;

public interface ModelParser<T> { 
	public boolean parse(Model<?> m,T data,String... mappings);
}