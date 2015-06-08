package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;

@SuppressWarnings({"rawtypes"})
public class Update{
	private Model model;
	
	public Update(Model model){
		this(model,false);
	}
	
	public Update(Model model,boolean updateKey){
		this.model=model;
		this.model.enableUpdateKey(updateKey);
	}

	public int update(){
		Query query=model.getDialect().update(model);
		query.use(model.db());
		return query.execute();
	}
	
	public int update(String whereStatement,Object ... args){
		Query query=model.getDialect().update(model,whereStatement,args);
		query.use(model.db());
		return query.execute();	 				 
	}
	
	public int updateByExample(Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().update(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		return query.execute();
	}
	
	public int updateSelective(String whereStatement,Object ... args){
		Query query=model.getDialect().updateSelective(model,whereStatement,args);
		query.use(model.db());
		return query.execute();	 				 
	}
	
	public int updateSelectiveByExample(Example example){
		Query w=example.getQuery();
		
		Query query=model.getDialect().updateSelective(model,w.getSql(), w.getParameters());
		query.use(model.db());
		
		return query.execute();
	}
	
	 
}
