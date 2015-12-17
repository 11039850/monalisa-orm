package com.tsc9526.monalisa.core.query.dao;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.criteria.Example;
import com.tsc9526.monalisa.core.query.criteria.QEH;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.model.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Select<T extends Model,S extends Select> {
	protected T model;
	
	protected DBConfig db;
	
	public Select(T model){
		this.model=model;		 
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Select<T, S> set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
	
	/**
	 * 只提取某些字段
	 * 
	 * @param fields  需要的字段名称
	 * @return Select本身
	 */
	public S include(String... fields){
		model.include(fields);
		return (S)this;
	}
	
	/**
	 * 排除表的某些字段。 用于在查询表时， 过滤掉某些不必要的字段
	 * 
	 * @param fields 要排除的字段名
	 * 
	 * @return  Select本身
	 */
	public S exclue(String... fields){
		model.exclude(fields);
		return (S)this;
	}
	
	/**
	 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
	 * 
	 * @return Select本身
	 */
	public S excludeBlobs(){
		model.excludeBlobs();
		return (S)this;
	}
	
	/**
	 *  排除超过指定长度的字段
	 * 
	 * @param maxLength  字段长度
	 * 
	 * @return Select本身
	 */
	public S excludeBlobs(int maxLength ){
		model.excludeBlobs(maxLength);
		return (S)this;
	}
	 
	public Select use(DBConfig db){
		this.db=db;
		return this;
	}		

	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}
	 
	/**
	 * 
	 * @param whereStatement
	 * @param args
	 * 
	 * @return The model object
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(String, Object...)
	 */
	public T selectOne(String whereStatement,Object ... args){
		Query query=model.dialect().selectOne(model,whereStatement, args);
		query.use(db());
		T r=(T)query.getResult(model.getClass());
		return r;
	}
	
	public long count(){
		Query query=model.dialect().count(model,null);		 
		query.use(db());
		return query.getResult(Long.class);
	}	

	/**
	 * 
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public long count(Example example){
		Query w=QEH.getQuery(example);
		
		return count(w.getSql(), w.getParameters());	 
	}
	
	/**
	 * 
	 * @param whereStatement
	 * @param args
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(String, Object...)
	 */
	public long count(String whereStatement,Object ... args){
		Query query=model.dialect().count(model,whereStatement, args);		 
		query.use(db());
		return query.getResult(Long.class);
	}
	
	/**
	 * 
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public T selectOneByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().selectOne(model,w.getSql(), w.getParameters());
		query.use(db());
		
		T r= (T)query.getResult(model.getClass());
		return r;
	}
 	
	/**
	 * 
	 * @param whereStatement
	 * @param args
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(String, Object...)
	 */
	public DataTable<T> select(String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		query.use(db());
		
		return (DataTable<T>)query.getList(model.getClass());
	}
	
	/**
	 * 
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> selectByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		DataTable<T> r= (DataTable<T>)query.getList(model.getClass());
		return r;
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param whereStatement
	 * @param args
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> select(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		query.use(db());
		
		DataTable<T> r=(DataTable<T>)query.getList(model.getClass(),limit, offset);
		return r;
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> selectByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		DataTable<T> r=(DataTable<T>)query.getList(model.getClass(),limit, offset);
		return r;
	}
	
	/**
	 * @param limit
	 * @param offset
	 * @param whereStatement
	 * @param args
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPage(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		query.use(db());
		
		Page<T> r=(Page<T>)query.getPage(model.getClass(),limit, offset);
		return r;
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param example
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPageByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		query.use(db());
		
		Page<T> r=(Page<T>)query.getPage(model.getClass(),limit, offset);
		return r;
	}
  
	
	public DataTable<T> select(){
		return select(null);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> select(int limit,int offset){
		return select(limit, offset, null);
	}	
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPage(int limit,int offset){
		return selectPage(limit, offset, null);
	}	
	
	public SelectForExample selectForExample(Example example){
		return new SelectForExample(example);
	} 
	
	public class SelectForExample{
		private Example example;
		
		public SelectForExample(Example example){
			this.example=example;
		}
		
		public SelectForExample set(String name,Object value){		
			set(name,value);
			return this;
		}	
		
		/**
		 * 只提取某些字段
		 * 
		 * @param fields  需要的字段名称
		 * @return SelectForExample
		 */
		public SelectForExample include(String... fields){
			model.include(fields);
			return this;
		}
		
		/**
		 * 排除表的某些字段。 用于在查询表时， 过滤掉某些不必要的字段
		 * 
		 * @param fields 要排除的字段名
		 * 
		 * @return  SelectForExample
		 */
		public SelectForExample exclue(String... fields){
			model.exclude(fields);
			return this;
		}
		
		/**
		 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
		 * 
		 * @return SelectForExample
		 */
		public SelectForExample excludeBlobs(){
			model.excludeBlobs();
			return this;
		}
		
		/**
		 *  排除超过指定长度的字段
		 * 
		 * @param maxLength  字段长度
		 * 
		 * @return SelectForExample
		 */
		public SelectForExample excludeBlobs(int maxLength ){
			Select.this.excludeBlobs(maxLength);
			return this;
		}
		
		public long count(){
			return Select.this.count(example);	 
		}
			 
		public T selectOne(){
			return Select.this.selectOneByExample(example);
		}	 	 
		
		public DataTable<T> select(){
			return Select.this.selectByExample(example);
		}	 
		
		/**
		 * 
		 * @param limit
		 * @param offset
		 * @return
		 * 
		 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
		 */
		public DataTable<T> select(int limit,int offset){
			return Select.this.selectByExample(limit, offset, example);
		}		 
		
		/**
		 * 
		 * @param limit
		 * @param offset
		 * @return
		 * 
		 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
		 */
		public Page<T> selectPage(int limit,int offset){
			return Select.this.selectPageByExample(limit, offset, example);
		}
	}
}
