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
package com.tsc9526.monalisa.orm.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.Tx;
import com.tsc9526.monalisa.tools.cache.Cache;
import com.tsc9526.monalisa.tools.cache.CacheKey;
import com.tsc9526.monalisa.tools.cache.TransactionalCacheManager;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheExecutor<X> implements Execute<X> {
	private Query      query;
	private Execute<X> delegate;

	public CacheExecutor(Query query, Execute<X> delegate) {
		this.query=query;
		 
		this.delegate = delegate;
	}

	public PreparedStatement preparedStatement(Connection conn, String sql) throws SQLException {
		return delegate.preparedStatement(conn, sql);
	}

	@SuppressWarnings("unchecked")
	public X execute(PreparedStatement pst) throws SQLException {
		Tx tx=Tx.getTx();
		
		TransactionalCacheManager tcm=tx==null?null:tx.getTxCacheManager();
		
		Cache cache=query.getCache();
	 	long ttlInSeconds=query.getCacheTime();
		if (delegate instanceof Cacheable) {
			if(cache!=null){
				CacheKey key=query.getCacheKey();
				 
				cache.getReadWriteLock().readLock().lock();
				try{
					X x=(X)(tcm==null?cache.getObject(key):tcm.getObject(cache, key));
					
					if(x==null){
						x=delegate.execute(pst);
						
						if(tcm==null){
							cache.putObject(key, x,ttlInSeconds);
						}else{
							tcm.putObject(cache, key, x,ttlInSeconds);
						}
					}
					return x;
				}finally{
					cache.getReadWriteLock().readLock().unlock();
				}
			}
		}else{
			if(tcm!=null && cache!=null){
				tcm.clear(cache);
			}
		}

		return delegate.execute(pst);
	}

}
