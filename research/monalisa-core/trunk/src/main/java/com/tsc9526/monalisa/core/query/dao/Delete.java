package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Delete<T extends Model> {
	protected T  model;
	
	protected DBConfig db;
	
	public  Delete(T model){
		this.model=model;		 
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
	
	/**
	 * Delete all data use DML-SQL(Can rollback): delete from xxxTable<br>
	 * 
	 * 
	 * @return Number of delete records 
	 */
	public int deleteAll(){
		Query query=model.getDialect().deleteAll(model);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * Delete all data use DDL-SQL(Cannot rollback): truncate table xxx;<br>
	 *  
	 * @return Number of delete records 
	 */
	public int truncate(){
		Query query=model.getDialect().deleteAll(model);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * Delete records filter by whereStatement  
	 * @param whereStatement 
	 * @param args  
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 * @return The rows has been deleted
	 */
	public int delete(String whereStatement,Object ... args){
		Query query=model.getDialect().delete(model,whereStatement,args);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * 
	 * @param example
	 * @return The rows has been deleted
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public int deleteByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.getDialect().delete(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
	  
	
	public class DeleteForExample{
		private Example example;
		
		public DeleteForExample(Example example){
			this.example=example;
		}
		
		/**
		 * @return  
		 * 
		 * @see Delete#deleteByExample(Example)
		 */
		public int delete(){
			return Delete.this.deleteByExample(example);
		}	  
	}
}
