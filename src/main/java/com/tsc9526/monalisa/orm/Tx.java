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
package com.tsc9526.monalisa.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.tools.cache.TransactionalCacheManager;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.misc.MelpException;

/**
 * 
 * 以事务执行数据库操作<br><br>
 *  
 * <code>
 * Tx.execute(new Atom(){ <br>
 * &nbsp;&nbsp;public int execute()throws Throwable{ <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;... 数据库操作代码 <br>
 * &nbsp;&nbsp;}<br>
 * );<br>
 * </code> 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Tx {
	public final static String CONTEXT_CURRENT_USERID="CONTEXT_CURRENT_USERID";
	
	public static interface Atom<T>{
		public T execute()throws Throwable;
	}
	
	private static class CI{
		Connection conn;
		boolean  autoCommit;
		
		CI(Connection conn) throws SQLException{
			this.conn=conn;
			this.autoCommit=conn.getAutoCommit();
		}
	}
	
	private static ThreadLocal<Tx> tx =new ThreadLocal<Tx>();  
	private static ThreadLocal<DataMap> context=new ThreadLocal<DataMap>();
	  
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
	
	public static Tx getTx(){
		return tx.get();
	}
	
	/**
	 * 
	 * @return null if the transaction started by other method. 
	 */
	public static Tx begin(){
		Tx x=tx.get();
		if(x==null){
			x=new Tx();
			tx.set(x);
			
			return x;
		}else{
			return null;
		}
	}
	
	public static void commit() throws SQLException{
		Tx x=tx.get();
		if(x!=null){			 
			x.doCommit();
		}else{
			throw new RuntimeException("Commit error, transaction not start, call begin first!");
		}
	}
	
	public static void rollback(){
		Tx x=tx.get();
		if(x!=null){			 
			x.doRollback();
		}else{
			throw new RuntimeException("Rollback error, transaction not start, call begin first!");
		}
	}
	
	public static void close(){
		Tx x=tx.get();
		if(x!=null){
			tx.remove();
			x.doClose();
		}
	}
	
	/**
	 * Execute the run() method in transaction
	 * 
	 * @param x Atom
	 * @param <T> T int / int[]
	 * @return Number of rows affected 
	 */
	public static <T> T execute(Atom<T> x){
		return execute(x,-1);
	}
	
	/**
	 * Execute the run() method in transaction
	 * 	   
	 * @param x Atom
	 * @param level setTransactionIsolation
	 * @param <T> T int / int[]
	 * @return Number of rows affected 
	 */
	public static <T> T execute(Atom<T> x, int level){
		Tx tx=begin();
		try{
			if(tx!=null && level>-1){
				tx.setTransactionIsolation(level);
			}
			
			T r=x.execute();
			
			if(tx!=null){
				commit();
			}
			
			return r;
		}catch(Throwable e){
			if(tx!=null){
				rollback();
			}
			
			return MelpException.throwRuntimeException(e);
		}finally{
			if(tx!=null){
				close();
			}
		}
	}
	
	
	
	private Map<String, CI> hcs=new HashMap<String, CI>();
	 
	private int level=-1;
	
	private String txid=UUID.randomUUID().toString().replace("-","").toLowerCase();
	
	private TransactionalCacheManager tcm = new TransactionalCacheManager();

	public String getTxid(){
		return txid;
	}
	
	public Connection getConnection(DBConfig db) throws SQLException{		 
		String key=db.getKey();
		 
		CI ci=hcs.get(key);
		if (ci==null) {
			Connection conn=db.getDataSource().getConnection();
			ci=new CI(conn);
			conn.setAutoCommit(false);
			
			if(level>-1){
				conn.setTransactionIsolation(level);
			}
			
			hcs.put(key, ci);
		}
		return ci.conn;
	}
	
	public void setTransactionIsolation(int level)throws SQLException{
		this.level=level;
		for(CI ci:hcs.values()){
			ci.conn.setTransactionIsolation(level);
		}
	}
	
	public void doCommit()  throws SQLException{
		for(CI ci:hcs.values()){
			ci.conn.commit();			
		} 
		
		tcm.commit();
	}

	public void doRollback(){
		try{
			for(CI ci:hcs.values()){
				ci.conn.rollback();			
			}
			
			tcm.rollback();
		}catch(SQLException e){
			throw new RuntimeException(e);
		} 	
	}
	
	public void doClose(){
		try{
			for(CI ci:hcs.values()){
				ci.conn.setAutoCommit(ci.autoCommit);
				ci.conn.close();
			}
			 
		}catch(SQLException e){
			throw new RuntimeException(e);		
		}finally{
			hcs.clear();
		}
	}

	public TransactionalCacheManager getTxCacheManager() {
		return tcm;
	}
}
