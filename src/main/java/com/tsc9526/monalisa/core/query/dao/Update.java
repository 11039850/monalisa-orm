package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;
import com.tsc9526.monalisa.core.query.model.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Update<T extends Model>{
	protected T model;
	
	protected DBConfig db;
	
	public Update(T model){		 
		this.model=model;		 
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
		Query query=model.dialect().update(model);
		query.use(db());
		return query.execute();
	}
	 
	
	/**
	 * 
	 * @param whereStatement
	 * @param args
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public int update(String whereStatement,Object ... args){
		Query query=model.dialect().update(model,whereStatement,args);
		query.use(db());
		return query.execute();	 				 
	}
	
	/**
	 * 
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public int updateByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().update(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
	 
	 
	public class UpdateForExample{
		private Example example;
		
		public UpdateForExample(Example example){
			this.example=example;
		}
		
		public UpdateForExample set(String name,Object value){		
			set(name,value);
			return this;
		}	
		
		/**
		 * 
		 * @return
		 * 
		 * @see Update#updateByExample(Example)
		 */
		public int update(){
			return Update.this.updateByExample(example);
		}				
	}
	 
}
