package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;

@SuppressWarnings({"rawtypes","unchecked"})
public class Delete<T extends Model> {
	protected T  model;
	
	protected DBConfig db;
	
	public  Delete(T model){
		this.model=model;		 
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Delete set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
 
	
	public Delete use(DBConfig db){
		this.db=db;
		return this;
	}
	
	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}

	/**
	 * Delete by primary key
	 * 
	 * @return Number of delete records 
	 */
	public int delete(){
		Query query=model.getDialect().delete(model);
		query.use(db());
		return query.execute();
	}
	
	public int deleteAll(){
		Query query=model.getDialect().deleteAll(model);
		query.use(db());
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
		query.use(db());
		return query.execute();
	}
		 
	public int deleteByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().delete(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
}
