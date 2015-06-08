package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;

@SuppressWarnings({"rawtypes"})
public class Delete {
	private Model  model;
	
	public  Delete(Model model){
		this.model=model;
	}
	

	/**
	 * Delete by primary key
	 * 
	 * @return Number of delete records 
	 */
	public int delete(){
		Query query=model.getDialect().delete(model);
		query.use(model.db());
		return query.execute();
	}
	
	/**
	 * Delete records filter by whereStatement  
	 * @param whereStatement
	 * @param args
	 * @return
	 */
	public int delete(String whereStatement,Object ... args){
		Query query=model.getDialect().delete(model,whereStatement,args);
		query.use(model.db());
		return query.execute();
	}
	
	public int deleteByExample(Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().delete(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		return query.execute();
	}
}
