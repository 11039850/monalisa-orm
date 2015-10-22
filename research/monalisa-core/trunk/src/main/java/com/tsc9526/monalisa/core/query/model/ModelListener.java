package com.tsc9526.monalisa.core.query.model;

public interface ModelListener {
	public void before(ModelEvent event, Model<?> model);
	public void after (ModelEvent event, Model<?> model,int result);
}