package com.tsc9526.monalisa.core.query;

import java.sql.SQLException;

public class Tx {	
	private static ThreadLocal<TxQuery> local=new ThreadLocal<TxQuery>();  
	
	public static TxQuery getTxQuery(){
		return local.get();
	}
	
	public static void begin(){
		TxQuery x=local.get();
		if(x==null){
			x=new TxQuery();
			local.set(x);
		}else{
			throw new RuntimeException("Transaction started already!");
		}
	}
	
	public static void commit() throws SQLException{
		TxQuery x=local.get();
		if(x!=null){
			local.remove();
			x.commit();
		}else{
			throw new RuntimeException("Commit error, transaction not start, call begin first!");
		}
	}
	
	public static void rollback() throws SQLException{
		TxQuery x=local.get();
		if(x!=null){
			local.remove();
			x.rollback();
		}else{
			throw new RuntimeException("Rollback error, transaction not start, call begin first!");
		}
	}
	
	public static void close()throws SQLException{
		TxQuery x=local.get();
		if(x!=null){
			local.remove();
			x.close();
		}
	}
}
