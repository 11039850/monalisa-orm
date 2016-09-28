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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.tsc9526.monalisa.orm.cache.Cache;
import com.tsc9526.monalisa.orm.cache.CacheKey;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.datatable.Page;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.executor.CacheExecutor;
import com.tsc9526.monalisa.orm.executor.Execute;
import com.tsc9526.monalisa.orm.executor.ResultExecutor;
import com.tsc9526.monalisa.orm.executor.ResultHandler;
import com.tsc9526.monalisa.orm.executor.ResultLoadExecutor;
import com.tsc9526.monalisa.orm.executor.ResultSetExecutor;
import com.tsc9526.monalisa.orm.executor.ResultSetsExecutor;
import com.tsc9526.monalisa.orm.executor.UpdateExecutor;
import com.tsc9526.monalisa.orm.tools.agent.AgentClass;
import com.tsc9526.monalisa.orm.tools.generator.DBExchange;
import com.tsc9526.monalisa.orm.tools.helper.CloseQuietly;
import com.tsc9526.monalisa.orm.tools.helper.SQLHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;
 

/**
 * Database query, usage: <br><br>
 * 
 * <code>
 * &#64;DB(url="jdbc:mysql://127.0.0.1:3306/test", username="root", password="root")<br>
 * public interface TestDB {<br>
 * 	 &nbsp;&nbsp;&nbsp;&nbsp;public final static DBConfig DB=DBConfig.fromClass(TestDB.class)<br>
 * }	
 * </code> 
 *<br><br> 
 * <code>
 * Query q=new Query(TestDB.DB); //or: Query q=TestDB.DB.createQuery(); <br>
 * q.add("select * from xxx where id=?",1); <br>
 * List&lt;Result&gt; r=q.getList(Result.class);   <br>
 * Page&lt;Result&gt; p=q.getPage(Result.class,10,0);<br>
 * Result       x=q.getResult(Result.class);<br>
 * </code>
 * 
 * @see com.tsc9526.monalisa.orm.annotation.DB
 * 
 * @author zzg.zhou(11039850@qq.com)
 */

@SuppressWarnings({"unchecked"})
public class Query {	
	static Logger logger=Logger.getLogger(Query.class.getName());
	  
	/**
	 * Create a database query dynamically
	 * 
	 * @param theQueryClass Database query class which can be loaded dynamically.
	 * @param <T> the class type		
	 * @return create new instance.
	 */
	public static <T> T create(Class<T> theQueryClass){
		 return AgentClass.createAgent(theQueryClass);
	}
 	
	/**
	 * Weather if show the running sql, default: false
	 */
	private Boolean debugSql=null;
	 
	protected DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected StringBuffer  sql=new StringBuffer();
	protected List<Object>  parameters=new ArrayList<Object>();
 	 
	protected DBConfig      db;
 	
	protected boolean enableCache=false;
	
	protected Boolean readonly;
 	
	protected List<List<Object>> batchParameters=new ArrayList<List<Object>>();
	
	protected Object tag;
	
	protected PrintWriter writer=null;
	
	
	protected Cache cache;
	
	public Query(){		 
	}
	
	public Query(DBConfig db){
		 this.db=db;
	}	 
	  
	public Query setDebugSql(boolean debugSql){
		this.debugSql=debugSql;
		return this;
	}
	
	public boolean isDebugSql(){
		return debugSql==null?false:debugSql;
	}
	
	public <T> T getTag(){
		return (T)tag;
	}
	
	public void setTag(Object tag){
		this.tag=tag;
	}
	 
	public Query notin(Object value,Object... otherValues){
		Dialect dialect=db==null?Dialect.SQLDialect:getDialect();
		
		return dialect.notin(this, new Object[]{value,otherValues});
	}
	 
	public Query in(Object value,Object... otherValues){
		Dialect dialect=db==null?Dialect.SQLDialect:getDialect();
		 
		return dialect.in(this, new Object[]{value,otherValues});
	}
	  
	public Query addQuery(Query q){	
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
  
	/**
	 * Add segment to SQL only if the args is not empty
	 * 
	 * @param segment the SQL segment
	 * @param args  the SQL parameters
	 * @return  this Query
	 */
	public Query addIfNotEmpty(String segment,Object ... args){
		if(args!=null && args.length==1){
			if(args[0]==null){
				return this;
			}else if(args[0] instanceof String){
				String s=(String)args[0];
				if(s.trim().length()<1){
					return this;
				}
			}
		}
		
		return add(segment, args);
	}
	
	/**
	 * Add segment to SQL only if 'condition' is true
	 * 
	 * @param condition true: add the segment and args to SQL otherwise ignore.
	 * @param segment the SQL segment
	 * @param args the SQL parameters
	 * @return this Query
	 */
	public Query addIf(boolean condition,String segment,Object ... args){
		if(condition){
			add(segment, args);
		}
		return this;
	}
	
	public boolean isEmpty(){
		return sql.length()==0;
	}
	
	/**
	 * 
	 * @return Original SQL, maybe "?" in it.
	 */
	public String getSql() {
		String r=sql.toString();
		return r;
	}
	
	/**
	 * 
	 * get the SQL which replace ? to the real value
	 * 
	 * @return the executable SQL
	 */
	public String getExecutableSQL() {
		 return SQLHelper.getExecutableSQL(getSql(), parameters);
	}

	/**
	 * 
	 * Clear the SQL statement and parameters.
	 * 
	 * @return this Query
	 */
	public Query clear(){		 
		if(this.sql.length()>0){
			this.sql.delete(0,this.sql.length());
		}		 
		this.parameters.clear();
		
		return this;
	}

	public int parameterCount(){
		return parameters.size();
	}
 
	public List<Object> getParameters() {
		return parameters;
	}
	 
	public Query clearParameters(){
		this.parameters.clear();
		return this;
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
	
	protected Connection getConnectionFromTx(Tx tx) throws SQLException{
		return tx.getConnection(db);		 
	}
	
	protected Connection getConnectionFromDB(boolean autoCommit) throws SQLException{
		Connection conn=db.getDataSource().getConnection();
		conn.setAutoCommit(autoCommit);
		return conn;
	}
	
	/**
	 * Execute the SQL
	 * 
	 * @return the effected number of rows
	 */
	public int execute(){
		return doExecute(new UpdateExecutor());
	}
	
	public <X> X execute(Execute<X> execute){
		 return doExecute(execute);
	}	
	
	public int[] executeBatch(){
		Tx tx=Tx.getTx();
		
		Connection conn=null;
		PreparedStatement pst=null;
		try{
			conn= tx==null?getConnectionFromDB(false):getConnectionFromTx(tx);
			
			pst=conn.prepareStatement(getSql());
			for(List<Object> p:batchParameters){
				SQLHelper.setPreparedParameters(pst, p);
				pst.addBatch();
			}
			
			logSql();
			
			int[] result=pst.executeBatch();
			
			if(tx==null){
				conn.commit();
			}			
			
			return result;
		}catch(Exception e){
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
	 
	
	protected <X> X doExecute(Execute<X> x){
		x=new CacheExecutor<X>(this, x);
		
		Tx tx=Tx.getTx();
		
		Connection conn=null;
		PreparedStatement pst=null;
		try{
			conn= tx==null?getConnectionFromDB(true):getConnectionFromTx(tx);
			 
			pst=x.preparedStatement(conn,getSql());
			 
			SQLHelper.setPreparedParameters(pst, parameters);
			
			logSql();
			
			return x.execute(pst);			
		}catch(SQLException e){
			String executeSQL=sql.toString();
			try{
				executeSQL=getExecutableSQL();
			}catch(Exception ex){}
			
			throw new RuntimeException("ERROR: "+e.getMessage()+"\r\nERROR SQL: \r\n========================================================================\r\n"
					                  +executeSQL+"\r\n========================================================================",e);
		}finally{
			CloseQuietly.close(pst);
			
			if(tx==null){
				CloseQuietly.close(conn);
			}
		}
	}
	 
 
	/**
	 * Get single result
	 * 
	 * @return DataMap
	 */
	public DataMap getResult(){
		return getResult(DataMap.class);				
	}
	 

	/**
	 * Get multi ResultSets from the SQL query
	 * 
	 * @return multi ResultSets
	 */
	public List<DataTable<DataMap>> getAllResults(){
		queryCheck();
		 
		int deepth = DbProp.PROP_DB_MULTI_RESULTSET_DEEPTH.getIntValue(db,100);
		ResultHandler<DataMap> resultHandler=new ResultHandler<DataMap>(this,DataMap.class);
		
		return doExecute(new ResultSetsExecutor<DataMap>(resultHandler,deepth));  
	}
	 
	/**
	 * Translate data to the resultClass
	 *  
	 * @param resultClass translate DataMap to the result class
	 * @param <T> result type
	 * @return the result object
	 */
	public <T> T getResult(final Class<T> resultClass){
		return getResult(new ResultHandler<T>(this,resultClass));
	}
	
	/**
	 * Translate data to the resultClass
	 *  
	 * @param resultHandler handle result set
	 * @param <T> result type
	 * @return the result object
	 */
	public <T> T getResult(final ResultHandler<T> resultHandler){
		if(!doExchange()){			
			queryCheck();
			
			return doExecute(new ResultExecutor<T>(resultHandler));	 
		}else{
			return null;
		}
	}
	
	/**
	 * @return List DataMap
	 */
	public DataTable<DataMap> getList() {
		return getList(DataMap.class);
	}
	
	/**
	 * @param resultClass the result class
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(final Class<T> resultClass) {
		return getList(new ResultHandler<T>(this, resultClass));
	}
	
	
	/**
	 * @param limit The max number of records for this query
	 * @param offset Base 0, the first record is 0
	 * @return List DataMap
	 */
	public DataTable<DataMap> getList(int limit,int offset) {		 
		return getList(DataMap.class,limit,offset);		 
	}
	
	/**
	 * @param resultClass the result class
	 * @param limit The max number of records for this query
	 * @param offset Base 0, the first record is 0
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(Class<T> resultClass,int limit,int offset) {		 
		return getList(new ResultHandler<T>(this,resultClass),limit,offset);
	}	
	
	/**
	 * @param resultHandler handle result set
	 * @param limit The max number of records for this query
	 * @param offset Base 0, the first record is 0
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(ResultHandler<T> resultHandler,int limit,int offset) {
		Query listQuery=getDialect().getLimitQuery(this, limit, offset);
		DataTable<T>  list=listQuery.getList(resultHandler);
			
		return list;
	}	 

	/**
	 * @param resultHandler handle result set
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(final ResultHandler<T> resultHandler) {
		if(!doExchange()){		 
			queryCheck();
			
			return doExecute(new ResultSetExecutor<T>(resultHandler));
		}else{
			return new DataTable<T>();
		}
	}
	
	/**
	 * @param limit The max number of records for this query
	 * @param offset   Base 0, the first record is 0
	 * @return Page Data
	 */
	public Page<DataMap> getPage(int limit,int offset) {
		return getPage(DataMap.class,limit, offset);
	}
	
	/**
	 * @param resultClass the result class
	 * @param limit The max number of records for this query
	 * @param offset   Base 0, the first record is 0
	 * @param <T> result type
	 * @return Page Data
	 */
	public <T> Page<T> getPage(Class<T> resultClass,int limit,int offset) {
		return getPage(new ResultHandler<T>(this,resultClass), limit, offset);
	}
	
	/**
	 * @param resultHandler handle result set
	 * @param limit The max number of records for this query
	 * @param offset   Base 0, the first record is 0
	 * @param <T> result type
	 * @return Page Data
	 */
	public <T> Page<T> getPage(ResultHandler<T> resultHandler,int limit,int offset) {
		if(!doExchange()){			
			queryCheck();
			
			Query countQuery=getDialect().getCountQuery(this);			
			long total=countQuery.getResult(Long.class);			
			 
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			DataTable<T>  list=listQuery.getList(resultHandler);
			 
			Page<T> page=new Page<T>(list,total,limit,offset);
		
			return page;
		}else{
			return new Page<T>();		 
		}
	}
	
	public <T> T load(final T result){
		if(!doExchange()){			 
			queryCheck();
			
			ResultHandler<T> resultHandler=new ResultHandler<T>(this,(Class<T>)result.getClass());
			
			return doExecute(new ResultLoadExecutor<T>(resultHandler, result)); 
		}else{
			return result;
		}
	}
	 
	protected boolean doExchange(){
		DBExchange exchange=DBExchange.getExchange(false);
		if(exchange!=null){
			ResultHandler.processExchange(this,exchange);
			return true;
		}else{
			return false;
		}
	}
 	
	protected void queryCheck(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
	}
	
	public PrintWriter getPrintWriter(){
		if(writer==null){
			writer= new PrintWriter(new Writer(){
				public void write(char[] cbuf, int off, int len) throws IOException {
					 add(new String(cbuf,off,len));
				}

				public void flush() throws IOException {}
	
				public void close() throws IOException {}
			});
		}
		return writer;
	}
	
	protected void logSql(){
		boolean debug=false;
		if(debugSql==null){
			debug=  "true".equalsIgnoreCase( DbProp.PROP_DB_SQL_DEBUG.getValue(db));
		}else{
			debug=debugSql.booleanValue();
		}
		
		if(debug){
			logger.info(getExecutableSQL());
		}
	}
	  
	public CacheKey getCacheKey(){
		 CacheKey cacheKey = new CacheKey();
		    
		 cacheKey.update(db.getKey());
		 cacheKey.update(""+tag);
		 cacheKey.update(getSql());
		 
		 for(Object p:this.getParameters()){
			 cacheKey.update(p);
		 } 
		    
		return cacheKey;    
	}
	
	public Cache getCache(){
		if(this.cache!=null){
			return this.cache;
		}else if(enableCache || DbProp.CFG_CACHE_GLOABLE_ENABLE){
			if(db==null){
				throw new RuntimeException("Query must use db!");
			}
			
			return getDb().getCfg().getCache();
		}else{
			return null;
		}
	}
	
	public void setCache(Cache cache){
		this.cache=cache;
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
      
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public Query setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
		return this;
	}
}