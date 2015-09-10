package com.tsc9526.monalisa.core.query;

import java.sql.SQLException;

public class Tx {	
	private static ThreadLocal<TxQuery> local=new ThreadLocal<TxQuery>();  
	
	public static TxQuery getTxQuery(){
		return local.get();
	}
	
	/**
	 * 
	 * @return null if the transaction started by other method. 
	 */
	public static TxQuery begin(){
		TxQuery x=local.get();
		if(x==null){
			x=new TxQuery();
			local.set(x);
			
			return x;
		}else{
			return null;
		}
	}
	
	public static void commit() throws SQLException{
		TxQuery x=local.get();
		if(x!=null){			 
			x.commit();
		}else{
			throw new RuntimeException("Commit error, transaction not start, call begin first!");
		}
	}
	
	public static void rollback(){
		TxQuery x=local.get();
		if(x!=null){			 
			x.rollback();
		}else{
			throw new RuntimeException("Rollback error, transaction not start, call begin first!");
		}
	}
	
	public static void close(){
		TxQuery x=local.get();
		if(x!=null){
			local.remove();
			x.close();
		}
	}
	
	/**
	 * Execute the run() method in transaction	  
	 */
	public static void run(Runnable x){
		TxQuery tq=begin();
		try{
			x.run();
			
			if(tq!=null){
				commit();
			}
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
}
