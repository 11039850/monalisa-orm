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
import com.tsc9526.monalisa.core.query.executor.KeysExecutor;
import com.tsc9526.monalisa.core.query.model.Model;

/**
 * 
 * 完成数据库模型的插入操作
 * 
 * @author zzg.zhou(11039850@qq.com)
 *
 * @param <T> 数据库模型类型
 */
@SuppressWarnings({"rawtypes"})
public class Insert<T extends Model>{
	protected T  model;
	
	protected DBConfig db;
	
	public  Insert(T model){
		this.model=model;		 
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Insert set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
	
	public Insert use(DBConfig db){
		this.db=db;
		return this;
	}
	
	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}
	/**
	 * insert到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int insert(){			
		return insert(false);
	}
	
	
	/**
	 * insert到数据库
	 * 
	 * @param updateOnDuplicateKey   true: 如果插入时出现主键冲突则进行更新操作
	 * 
	 * @return 成功变更的记录数
	 */
	public int insert(boolean updateOnDuplicateKey){	 
		Query query=model.dialect().insert(model, updateOnDuplicateKey);
		query.use(db());
		return query.execute(new KeysExecutor(model));  
	}
}
