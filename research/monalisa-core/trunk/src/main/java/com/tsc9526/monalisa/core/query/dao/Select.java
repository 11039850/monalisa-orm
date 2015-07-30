package com.tsc9526.monalisa.core.query.dao;

import java.util.List;

import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;


@SuppressWarnings("rawtypes")
public class Select<T extends Model> {
	protected T model;
	
	public Select(T model){
		this.model=model;
		model.readonly(true);		
	}
	 
	 
	public T selectOne(String whereStatement,Object ... args){
		Query query=model.getDialect().selectOne(model,whereStatement, args);
		query.use(model.db());
		T r=query.getResult();
		return r;
	}
	
	public long count(Example example){
		Query w=example.getQuery();
		
		return count(w.getSql(), w.getParameters());	 
	}
	
	public long count(String whereStatement,Object ... args){
		Query query=model.getDialect().count(model,whereStatement, args);		 
		query.use(model.db());
		return query.getResult();
	}
	
	public T selectOneByExample(Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		T r= query.getResult();
		return r;
	}
 	
	public List<T> select(String whereStatement,Object ... args){
		Query query=model.getDialect().select(model,whereStatement, args);
		query.use(model.db());
		
		return query.getList();
	}
	
	public List<T> selectByExample(Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		List<T> r= query.getList();
		return r;
	}
	
	public Page<T> select(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.getDialect().select(model,whereStatement, args);
		query.use(model.db());
		
		Page<T> r=query.getPage(limit, offset);
		return r;
	}
	
	public Page<T> selectByExample(int limit,int offset,Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		Page<T> r=query.getPage(limit,offset);
		return r;
	}
  
	
	public List<T> selectAll(){
		return select(null);
	}
	
	public Page<T> selectAll(int limit,int offset){
		return select(limit, offset, null);
	}	
}
