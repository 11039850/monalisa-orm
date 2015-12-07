package com.tsc9526.monalisa.core.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.SQLHelper;

import freemarker.log.Logger;


/**
 * 数据库查询对象, 基本用法: <br>
 * <code>
 * Query q=new Query(Result.class); <br>
 * q.use(db); <br>
 * q.add("select * from xxx where id=?",1); <br>
 * List&lt;Result&gt; r=q.getList();   <br>
 * Page&lt;Result&gt; p=q.getPage(10,0);<br>
 * Result       x=q.getResult();<br>
 * </code>
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Query {	
	static Logger logger=Logger.getLogger(Query.class.getName());
	
	/**
	 * 是否显示执行的SQL语句, 默认为: false
	 */
	public static boolean SQL_DEBUG=false;
	
	protected DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected StringBuffer  sql=new StringBuffer();
	protected List<Object>  parameters=new ArrayList<Object>();
	
	protected QueryResult rc=new QueryResult();
 
	protected DBConfig      db;
 	
	protected int cacheTime=0;
	
	protected Boolean readonly;
 	
	protected List<List<Object>> batchParameters=new ArrayList<List<Object>>();
	
	public Query(){		 
	}
	
	public Query(DBConfig db){
		 this.db=db;
	}
	
	public Query(DBConfig db,Class<?> resultClass){
		 this.db=db;
		 setResultCreator(resultClass);
	}
	
	public Query(Class<?> resultClass){
		setResultCreator(resultClass);
	}
	 
	public Query notin(Object... values){
		 return getDialect().notin(this, values);
	}
	 
	public Query in(Object... values){
		 return getDialect().in(this, values);
	}
	
	public Query notin(List<?> values){
		 return getDialect().notin(this, values.toArray(new Object[]{}));
	}
	
	public Query in(List<?> values){
		 return getDialect().in(this, values.toArray(new Object[]{}));
	}
	
	public Query add(Query q){	
		return add(q.getSql(),q.getParameters());
	}
	
	public Query add(String segment,Object ... args){		
		if(segment!=null){			 
			sql.append(segment);
		}
		
		if(args!=null){
			for(Object arg:args){
				if(arg instanceof Collection){
					for(Object x:((Collection<?>)arg)){
						parameters.add(x);
					}
				}else{
					parameters.add(arg);
				}
			}
		}
		return this;
	}	
	
	public boolean isEmpty(){
		return sql.length()==0;
	}
	
	/**
	 * 
	 * @return 原始的SQL语句, 中间可能会有参数
	 */
	public String getSql() {
		return sql.toString();
	}
	
	/**
	 * 
	 * @return 处理过参数后的SQL语句
	 */
	public String getExecutableSQL() {
		 return SQLHelper.getExecutableSQL(sql.toString(), parameters);
	}

	public Query clear(){		 
		if(this.sql.length()>0){
			this.sql.delete(0,this.sql.length());
		}		 
		this.parameters.clear();
		
		return this;
	}

	public List<Object> getParameters() {
		return parameters;
	}
	
	public int parameterCount(){
		return parameters.size();
	}

	public Query setParameters(List<Object> parameters) {
		this.parameters = parameters;
		
		return this;
	}

	
	public Query addBatch(Object... parameters) {
		if(this.parameters!=null && this.parameters.size()>0){
			this.batchParameters.add(this.parameters);
		}
		
		this.batchParameters.add(Arrays.asList(parameters));
				
		return this;
	}
	
	public int[] executeBatch(){
		TxQuery tx=Tx.getTxQuery();
		
		Connection conn=null;
		PreparedStatement pst=null;
		try{
			conn= tx==null?getConnectionFromDB(false):getConnectionFromTx(tx);
			
			pst=conn.prepareStatement(sql.toString());
			for(List<Object> p:batchParameters){
				SQLHelper.setPreparedParameters(pst, p);
				pst.addBatch();
			}
			
			int[] result=pst.executeBatch();
			
			if(tx==null){
				conn.commit();
			}			
			
			return result;
		}catch(SQLException e){
			if(tx==null && conn!=null){
				try{ conn.rollback(); }catch(SQLException ex){}
			}
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(pst);
			
			if(tx==null){
				CloseQuietly.close(conn);
			}
		}
	}
	
	protected Connection getConnectionFromTx(TxQuery tx) throws SQLException{
		return tx.getConnection(db);		 
	}
	
	protected Connection getConnectionFromDB(boolean autoCommit) throws SQLException{
		Connection conn=db.getDataSource().getConnection();
		conn.setAutoCommit(autoCommit);
		return conn;
	}
	
	protected <X> X doExecute(Execute<X> x){
		TxQuery tx=Tx.getTxQuery();
		
		Connection conn=null;
		PreparedStatement pst=null;
		try{
			conn= tx==null?getConnectionFromDB(true):getConnectionFromTx(tx);
			
			pst=x.preparedStatement(conn,sql.toString());
			 
			SQLHelper.setPreparedParameters(pst, parameters);
			
			logSql();
			
			return x.execute(pst);			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(pst);
			
			if(tx==null){
				CloseQuietly.close(conn);
			}
		}
	}
	
	protected void logSql(){
		if( SQL_DEBUG || "true".equalsIgnoreCase( DbProp.PROP_DB_SQL_DEBUG.getValue(db) ) ){
			logger.info(getExecutableSQL());
		}
	}
	
	public int execute(){
		return doExecute(new Execute<Integer>(){
			public Integer execute(PreparedStatement pst) throws SQLException {				 
				return pst.executeUpdate();
			}
 	 
			public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {				 
				return conn.prepareStatement(sql);
			}	 
		});
	}
	
	public int execute(Execute<Integer> execute){
		 return doExecute(execute);
	}
	 
	/**
	 * @return 如果没有调用setResultClass指定结果类, 则对象为{@link:com.tsc9526.monalisa.core.query.DataMap}
	 */
	public <T> T getResult(){
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			
			return doExecute(new Execute<T>(){ 
				public T execute(PreparedStatement pst) throws SQLException {				 
					T result=null;
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				
						if(rs.next()){	
							result=rc.createResult(Query.this,rs); 											
						}	
						return result;
					}finally{
						CloseQuietly.close(rs);
					}
				}

			 
				public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {				 
					return conn.prepareStatement(sql);
				}	 
			});			 
		}else{
			return null;		
		}
	}
	
	/**
	 * 如果没有调用setResultClass指定结果类, 则PageList存储的对象为{@link:com.tsc9526.monalisa.core.query.DataMap}
	 * @param limit 
	 *   The max number of records for this query
	 *    
	 * @param offset  
	 *   Base 0, the first record is 0
	 * @return Page对象
	 */
	public <T> Page<T> getPage(int limit,int offset) {
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			
			Query countQuery=getDialect().getCountQuery(this);			
			long total=countQuery.setResultCreator(Long.class).getResult();			
			 
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			DataTable<T>  list=listQuery.getList();
			 
			Page<T> page=new Page<T>(list,total,limit,offset);
		
			return page;
		}else{
			return new Page<T>();		 
		}
	}
	
	/**
	 * 如果没有调用setResultClass指定结果类, 则List存储的对象为{@link:com.tsc9526.monalisa.core.query.DataMap}
	 * @param limit 
	 *   The max number of records for this query
	 *    
	 * @param offset  
	 *   Base 0, the first record is 0
	 * @return List对象
	 */
	public <T> DataTable<T> getList(int limit,int offset) {
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			   
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			DataTable<T>  list=listQuery.getList();
			
			return list;
		}else{
			return new DataTable<T>();		 
		}
	}
	
	/**
	 * 如果没有调用setResultClass指定结果类, 则List存储的对象为{@link:com.tsc9526.monalisa.core.query.DataMap}
	 * @return List对象
	 */
	public <T> DataTable<T> getList() {
		if(!DBExchange.doExchange(this)){			 
			queryCheck();
			
			return doExecute(new Execute<DataTable<T>>(){
				public DataTable<T> execute(PreparedStatement pst) throws SQLException {		
					DataTable<T> result=new DataTable<T>();
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				 		
						while(rs.next()){
							T r=rc.createResult(Query.this,rs); 
							result.add(r);					
						}
						return result;
					}finally{
						CloseQuietly.close(rs);
					}
				} 
				

				public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {				 
					return conn.prepareStatement(sql);
				}	
			}); 
		}else{
			return new DataTable<T>();
		}
	}	 
	
	
	protected void queryCheck(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
	}
	
	 
	public int getCacheTime() {
		return cacheTime;
	}

	public Query setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
		return this;
	}

	public DBConfig getDb() {
		return db;
	}

	public Query use(DBConfig db) {
		this.db = db;
		return this;
	}
	
	public Dialect getDialect(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
		
		return dsm.getDialect(db);
	}
   
	/**
	 * 
	 * @param rc : maybe (Object ResultConstructor) or  (Object resultObject) or (Class<?> resultClass) or (ResultCreator resultCreator)
	 * @return
	 */
	public Query setResultCreator(Object rc){
		if(rc instanceof QueryResult){
			this.rc=(QueryResult)rc;
		}else if(rc instanceof Class){
			this.rc.setResultClass((Class<?>)rc);	
		}else{
			this.rc.setResultObject(rc);
		}		
		return this;
	}
	
	public QueryResult getResultCreator(){
		return this.rc;
	}

	public boolean isReadonly() {
		if(readonly!=null){
			return readonly;
		}else{
			String x=sql.toString().toLowerCase().trim();
			if(x.startsWith("select")){
				return true;
			}else{
				return false;
			}
		}		
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}
 
	
}