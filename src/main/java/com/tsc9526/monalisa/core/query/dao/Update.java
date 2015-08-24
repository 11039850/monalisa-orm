package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;

@SuppressWarnings({"rawtypes","unchecked"})
public class Update<T extends Model>{
	protected T model;
	
	protected DBConfig db;
	
	public Update(T model){
		this(model,false);
		 
	}
	
	public Update(T model,boolean updateKey){
		this.model=model;
		this.model.enableUpdateKey(updateKey);
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Update set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
 
	public Update use(DBConfig db){
		this.db=db;
		return this;
	}		

	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}
	
	public int update(){
		Query query=model.getDialect().update(model);
		query.use(db());
		return query.execute();
	}
	
	public int updateSelective(){
		Query query=model.getDialect().updateSelective(model);
		query.use(db());
		return query.execute();
	}
	
	public int update(String whereStatement,Object ... args){
		Query query=model.getDialect().update(model,whereStatement,args);
		query.use(db());
		return query.execute();	 				 
	}
	
	public int updateByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().update(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
	
	public int updateSelective(String whereStatement,Object ... args){
		Query query=model.getDialect().updateSelective(model,whereStatement,args);
		query.use(db());
		return query.execute();	 				 
	}
	
	public int updateSelectiveByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().updateSelective(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
	
	 
}
