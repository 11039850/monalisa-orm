package com.tsc9526.monalisa.core.query.dao;

import java.util.List;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;


@SuppressWarnings({"rawtypes","unchecked"})
public class Select<T extends Model> {
	protected T model;
	
	protected DBConfig db;
	
	public Select(T model){
		this.model=model;		 
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Select set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
 
	public Select use(DBConfig db){
		this.db=db;
		return this;
	}		

	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}
	 
	public T selectOne(String whereStatement,Object ... args){
		Query query=model.getDialect().selectOne(model,whereStatement, args);
		query.use(db());
		T r=query.getResult();
		return r;
	}
	
	public long countAll(){
		Query query=model.getDialect().count(model,null);		 
		query.use(db());
		return query.getResult();
	}	

	public long count(Example example){
		Query w=QEH.getQuery(example);
		
		return count(w.getSql(), w.getParameters());	 
	}
		
	public long count(String whereStatement,Object ... args){
		Query query=model.getDialect().count(model,whereStatement, args);		 
		query.use(db());
		return query.getResult();
	}
	
	public T selectOneByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		T r= query.getResult();
		return r;
	}
 	
	public List<T> select(String whereStatement,Object ... args){
		Query query=model.getDialect().select(model,whereStatement, args);
		query.use(db());
		
		return query.getList();
	}
	
	public List<T> selectByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		List<T> r= query.getList();
		return r;
	}
	
	public List<T> select(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.getDialect().select(model,whereStatement, args);
		query.use(db());
		
		List<T> r=query.getList(limit, offset);
		return r;
	}
	
	public List<T> selectByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		List<T> r=query.getList(limit, offset);
		return r;
	}
	
	public Page<T> selectPage(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.getDialect().select(model,whereStatement, args);
		query.use(db());
		
		Page<T> r=query.getPage(limit, offset);
		return r;
	}
	
	public Page<T> selectPageByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		Page<T> r=query.getPage(limit,offset);
		return r;
	}
  
	
	public List<T> selectAll(){
		return select(null);
	}
	
	public List<T> selectAll(int limit,int offset){
		return select(limit, offset, null);
	}	
	
	public Page<T> selectAllPage(int limit,int offset){
		return selectPage(limit, offset, null);
	}	
}
