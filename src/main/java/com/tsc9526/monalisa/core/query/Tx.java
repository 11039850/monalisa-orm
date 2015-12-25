package com.tsc9526.monalisa.core.query;

import java.sql.SQLException;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Tx {
	public final static String CONTEXT_CURRENT_USERID="CONTEXT_CURRENT_USERID";
	
	private static ThreadLocal<TxQuery> local =new ThreadLocal<TxQuery>();  
	private static ThreadLocal<DataMap> context=new ThreadLocal<DataMap>();
	
	public static Object getContext(String key){
		DataMap m=context.get();
		if(m!=null){
			return m.get(key);
		}else{
			return null;
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
	 * 
	 * @param x
	 * 
	 * Use execute() instead
	 */
	@Deprecated()
	public static void run(Runnable x){
		run(x,-1);
	}
	
	/**
	 * Execute the run() method in transaction	
	 * 
	 * Use execute() instead
	 */
	@Deprecated()
	public static void run(Runnable x, int level){
		TxQuery tq=begin();
		try{
			if(tq!=null && level>-1){
				tq.setTransactionIsolation(level);
			}
			
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
	
	
	public static int execute(Executeable x){
		return execute(x,-1);
	}
	
	/**
	 * Execute the run() method in transaction	  
	 */
	public static int execute(Executeable x, int level){
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
	public static interface Executeable{
		public int execute(); 
	}
}
