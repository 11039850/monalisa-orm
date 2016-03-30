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
	 * @return number of rows affected
	 */
	public int delete(){
		Query query=model.dialect().delete(model);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * Delete all data use DML-SQL(Can rollback): delete from xxxTable<br>
	 * 
	 * 
	 * @return number of rows affected
	 */
	public int deleteAll(){
		Query query=model.dialect().deleteAll(model);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * Delete all data use DDL-SQL(Cannot rollback): truncate table xxx;<br>
	 *  
	 * @return number of rows affected
	 */
	public int truncate(){
		Query query=model.dialect().deleteAll(model);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * Delete records filter by whereStatement  
	 * @param whereStatement where cause
	 * @param args  args
	 * 
	 * @return number of rows affected
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public int delete(String whereStatement,Object ... args){
		Query query=model.dialect().delete(model,whereStatement,args);
		query.use(db());
		return query.execute();
	}
	
	/**
	 * 
	 * @param example Example
	 * @return number of rows affected
	 * 
	 * @see com.tsc9526.monalisa.core.resources.HelpDoc#helpQuery(int,int,Example,String, Object...)
	 */
	public int deleteByExample(Example example){
		Query w=QEH.getQuery(example);
		
		Query query=model.dialect().delete(model,w.getSql(), w.getParameters());
		query.use(db());
		
		return query.execute();
	}
	  
	public DeleteForExample deleteForExample(Example example){
		return new DeleteForExample(example);
	}
	
	public class DeleteForExample{
		private Example example;
		
		public DeleteForExample(Example example){
			this.example=example;
		}
		
		public DeleteForExample set(String name,Object value){		
			set(name,value);
			return this;
		}	
		
		/**
		 * @return  number of rows affected
		 * 
		 * @see Delete#deleteByExample(Example)
		 */
		public int delete(){
			return Delete.this.deleteByExample(example);
		}	  
	}
}
