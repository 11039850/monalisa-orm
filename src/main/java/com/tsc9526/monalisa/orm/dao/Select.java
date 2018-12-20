/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.criteria.Example;
import com.tsc9526.monalisa.orm.criteria.QEH;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.executor.HandlerResultSet;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Select<T extends Model,S extends Select> {
	protected T        model;
	protected DBConfig db;
	protected long      ttlInMillis=0;
	protected long      autoRefreshInMillis = 0;
	protected Object    cacheTag;
	
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
	 * @return this
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
	 * @return  this
	 */
	public S exclue(String... fields){
		model.exclude(fields);
		return (S)this;
	}
	
	/**
	 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
	 * 
	 * @return this
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
	 * @return this
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
	 * @param whereStatement where cause
	 * @param args args
	 * 
	 * @return the first record
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String,Object...)
	 */
	public T selectOne(String whereStatement,Object ... args){
		Query query=model.dialect().selectOne(model,whereStatement, args);
		setup(query);
		
		T r=(T)query.getResult(getResultCreator(query));
		return r;
	}
	
	public long count(){
		Query query=model.dialect().count(model,null);		 
		setup(query);
		
		return query.getResult(Long.class);
	}	

	/**
	 * 
	 * @param example Example
	 * @return count of records
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public long count(Example example){
		Query w=QEH.getQuery(example);
		
		return count(w.getSql(), w.getParameters());	 
	}
	
	/**
	 * 
	 * @param whereStatement where cause
	 * @param args args
	 * @return count of records
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public long count(String whereStatement,Object ... args){
		Query query=model.dialect().count(model,whereStatement, args);		 
		setup(query);
		
		return query.getResult(Long.class);
	}
	
	/**
	 * 
	 * @param example Example
	 * @return the first record
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public T selectOneByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().selectOne(model,w.getSql(), w.getParameters());
		setup(query);
		
		T r= (T)query.getResult(getResultCreator(query));
		return r;
	}
	
	/**
	 * 
	 * @param example Example
	 * @return the first record
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public T selectByKeyExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		setup(query);
		
		T r= (T)query.getResult(getResultCreator(query));
		return r;
	}
 	
	/**
	 * 
	 * @param whereStatement where cause
	 * @param args args
	 * @return DataTable
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> select(String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		setup(query);
		
		return (DataTable<T>)query.getList(getResultCreator(query));
	}
	
	/**
	 * 
	 * @param example Example
	 * @return DataTable
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> selectByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		setup(query);
		
		DataTable<T> r= (DataTable<T>)query.getList(getResultCreator(query));
		return r;
	}
	
	/**
	 * 
	 * @param limit limit
	 * @param offset offset 
	 * @param whereStatement where cause
	 * @param args args 
	 * @return DataTable
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> select(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		setup(query);
		
		DataTable<T> r=(DataTable<T>)query.getList(getResultCreator(query),limit, offset);
		return r;
	}
	
	/**
	 * 
	 * @param limit limit 
	 * @param offset offset
	 * @param example Example
	 * @return DataTable
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> selectByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		setup(query);
		
		DataTable<T> r=(DataTable<T>)query.getList(getResultCreator(query),limit, offset);
		return r;
	}
	
	/**
	 * @param limit limit
	 * @param offset offset
	 * @param whereStatement where cause
	 * @param args args
	 * @return Page
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPage(int limit,int offset,String whereStatement,Object ... args){
		Query query=model.dialect().select(model,whereStatement, args);
		setup(query);
		
		Page<T> r=(Page<T>)query.getPage(getResultCreator(query),limit, offset);
		return r;
	}
	
	/**
	 * 
	 * @param limit limit 
	 * @param offset offset
	 * @param example Example 
	 * @return Page
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPageByExample(int limit,int offset,Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().select(model,w.getSql(), w.getParameters());
		setup(query);
		
		Page<T> r=(Page<T>)query.getPage(getResultCreator(query),limit, offset);
		return r;
	}
  
	
	public DataTable<T> select(){
		return select(null);
	}
	
	/**
	 * 
	 * @param limit limit 
	 * @param offset offset
	 * @return DataTable
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public DataTable<T> select(int limit,int offset){
		return select(limit, offset, null);
	}	
	
	/**
	 * 
	 * @param limit limit
	 * @param offset offset
	 * @return Page
	 * 
	 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public Page<T> selectPage(int limit,int offset){
		return selectPage(limit, offset, null);
	}	
	
	public $SelectForExample selectForExample(Example example){
		return new $SelectForExample(example);
	} 
	
	
	public long getCacheTime() {
		return ttlInMillis;
	}

	public S setCacheTime(long ttlInMillis) {
		this.ttlInMillis = ttlInMillis;
		return (S)this;
	}

	public S setCacheTime(int ttlInMillis, int autoRefreshInMillis) {
		this.ttlInMillis         = ttlInMillis;
		this.autoRefreshInMillis = autoRefreshInMillis;
		return (S)this;
	}
	
	public long getAutoRefreshInMillis() {
		return autoRefreshInMillis;
	}

	public S setAutoRefreshInMillis(long autoRefreshInMillis) {
		this.autoRefreshInMillis = autoRefreshInMillis;
		return (S)this;
	}
 
	public Object getCacheTag() {
		return cacheTag;
	}

	public void setCacheTag(Object cacheTag) {
		this.cacheTag = cacheTag;
	}

	
	
	protected HandlerResultSet getResultCreator(Query query) {
		return new HandlerResultSet(query,model.getClass()){
			public  T createResult(ResultSet rs)throws SQLException{
				Model result=model.shallow();
				loadModel(rs, result);				
				return (T)result;
			}
		};
		
	}
	
	protected void setup(Query query) {
		DBConfig db=db();
		
		query.use(db);
		
		query.setTag(cacheTag!=null ? cacheTag : ("@"+db.getKey()+"#"+model.table().name()));
		  
		query.setCache(db.getCfg().getCache(model));	
		query.setCacheTime(ttlInMillis,autoRefreshInMillis);
	}
	
	public class $SelectForExample{
		protected Example example;
		
		public $SelectForExample(Example example){
			this.example=example;
		}
		
		public $SelectForExample set(String name,Object value){		
			set(name,value);
			return this;
		}	
		
		/**
		 * 只提取某些字段
		 * 
		 * @param fields  需要的字段名称
		 * @return SelectForExample
		 */
		public $SelectForExample include(String... fields){
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
		public $SelectForExample exclue(String... fields){
			model.exclude(fields);
			return this;
		}
		
		/**
		 * 排除大字段（字段长度 大于等于 #Short.MAX_VALUE)
		 * 
		 * @return SelectForExample
		 */
		public $SelectForExample excludeBlobs(){
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
		public $SelectForExample excludeBlobs(int maxLength ){
			Select.this.excludeBlobs(maxLength);
			return this;
		}
		
		/**
		 * @param ttlInMillis         	cache time in millis. 
		 * 								<ul>
		 * 									<li> &gt;0: expired time.</li>
		 *            						<li>  0: no cache</li>
		 *            						<li> -1: never expired</li>
		 *            					</ul>
		 *            
		 * @param autoRefreshInMillis   auto refresh cache in background in millis.
		 * 								<ul>
		 * 									<li> 0 : no refresh</li>
		 * 									<li> &gt;0: auto refresh</li>
		 * 								</ul>
		 * @return this
		 */ 
		public $SelectForExample setCacheTime(int ttlInMillis, int autoRefreshInMillis ){
			Select.this.setCacheTime(ttlInMillis, autoRefreshInMillis);
			return this;
		}
		
		public $SelectForExample setCacheTag(Object cacheTag){
			Select.this.setCacheTag( cacheTag);
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
		 * @param limit limit 
		 * @param offset offset 
		 * @return DataTable
		 * 
		 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
		 */
		public DataTable<T> select(int limit,int offset){
			return Select.this.selectByExample(limit, offset, example);
		}		 
		
		/**
		 * 
		 * @param limit limit
		 * @param offset offset
		 * @return Page
		 * 
		 * @see com.tsc9526.monalisa.orm.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
		 */
		public Page<T> selectPage(int limit,int offset){
			return Select.this.selectPageByExample(limit, offset, example);
		}
	}

	
}
