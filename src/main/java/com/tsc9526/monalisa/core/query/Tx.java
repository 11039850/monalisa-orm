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
package com.tsc9526.monalisa.core.query;

import java.sql.SQLException;

/**
 * 
 * Transaction execute 
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Tx {
	public final static String CONTEXT_CURRENT_USERID="CONTEXT_CURRENT_USERID";
	
	private static ThreadLocal<TxQuery> txQuery =new ThreadLocal<TxQuery>();  
	private static ThreadLocal<DataMap> context=new ThreadLocal<DataMap>();
	
	public static void putUserContext(Object user){
		putContext(CONTEXT_CURRENT_USERID,user);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getUserContext(){
		return (T)getContext(CONTEXT_CURRENT_USERID);
	}
	
	public static Object getContext(String key){
		DataMap m=context.get();
		if(m!=null){
			return m.get(key);
		}else{
			return null;
		}
	}
	
	public static void removeContext(String key){
		DataMap m=context.get();
		if(m!=null){
			m.remove(key);
		}
	}
	
	public static void putContext(String key,Object value){
		DataMap m=context.get();
		if(m==null){
			m=new DataMap();
			context.set(m);
		}
		
		m.put(key, value);
	}
	 
	public static void clearContext(){
		if(context.get()!=null){
			context.remove();
		}
	}
	
	public static TxQuery getTxQuery(){
		return txQuery.get();
	}
	
	/**
	 * 
	 * @return null if the transaction started by other method. 
	 */
	public static TxQuery begin(){
		TxQuery x=txQuery.get();
		if(x==null){
			x=new TxQuery();
			txQuery.set(x);
			
			return x;
		}else{
			return null;
		}
	}
	
	public static void commit() throws SQLException{
		TxQuery x=txQuery.get();
		if(x!=null){			 
			x.commit();
		}else{
			throw new RuntimeException("Commit error, transaction not start, call begin first!");
		}
	}
	
	public static void rollback(){
		TxQuery x=txQuery.get();
		if(x!=null){			 
			x.rollback();
		}else{
			throw new RuntimeException("Rollback error, transaction not start, call begin first!");
		}
	}
	
	public static void close(){
		TxQuery x=txQuery.get();
		if(x!=null){
			txQuery.remove();
			x.close();
		}
	}
	
	/**
	 * Execute the run() method in transaction
	 * 
	 * @param x Executable
	 * @return Number of rows affected 
	 */
	public static int execute(Executable x){
		return execute(x,-1);
	}
	
	/**
	 * Execute the run() method in transaction
	 * 	   
	 * @param x Executable
	 * @param level setTransactionIsolation
	 * @return Number of rows affected 
	 */
	public static int execute(Executable x, int level){
		TxQuery tq=begin();
		try{
			if(tq!=null && level>-1){
				tq.setTransactionIsolation(level);
			}
			
			int r=x.execute();
			
			if(tq!=null){
				commit();
			}
			return r;
		}catch(Exception e){
			if(tq!=null){
				rollback();
			}
			
			if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			}else{
				throw new RuntimeException(e);
			}
		}finally{
			if(tq!=null){
				close();
			}
		}
	}
	public static interface Executable{
		public int execute(); 
	}
}
