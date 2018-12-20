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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.executor.BatchSqlExecutor;
import com.tsc9526.monalisa.orm.executor.BatchStatementExecutor;
import com.tsc9526.monalisa.orm.executor.CacheableExecute;
import com.tsc9526.monalisa.orm.executor.CacheableResultExecutor;
import com.tsc9526.monalisa.orm.executor.CacheableResultLoadExecutor;
import com.tsc9526.monalisa.orm.executor.CacheableResultSetExecutor;
import com.tsc9526.monalisa.orm.executor.Execute;
import com.tsc9526.monalisa.orm.executor.HandlerResultSet;
import com.tsc9526.monalisa.orm.executor.ResultSetsExecutor;
import com.tsc9526.monalisa.orm.executor.UpdateExecutor;
import com.tsc9526.monalisa.orm.generator.DBExchange;
import com.tsc9526.monalisa.tools.Tools;
import com.tsc9526.monalisa.tools.agent.AgentClass;
import com.tsc9526.monalisa.tools.cache.Cache;
import com.tsc9526.monalisa.tools.cache.CacheKey;
import com.tsc9526.monalisa.tools.cache.CacheManager;
import com.tsc9526.monalisa.tools.cache.Cacheable;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.string.MelpSQL;
import com.tsc9526.monalisa.tools.string.MelpString;
 

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
 	
	private static ThreadLocal<Boolean> putCacheMode = new ThreadLocal<Boolean>();
	
	/**
	 * Indicates whether or not always call the real query and put the results in the cache
	 * 
	 * @param putCache true: alway call ; false: first cache, then do real call if cache miss.
	 */
	public static void setPutCacheMode(boolean putCache) {
		if(putCache) {
			putCacheMode.set(true);
		}else {
			putCacheMode.remove();
		}
	}
	 
	
	/**
	 * Weather if show the running sql, default: false
	 */
	private Boolean debugSql = null;
	  	
	protected StringBuilder sql       = new StringBuilder();
	protected List<Object>  queryArgs = new ArrayList<Object>();
 	 
	protected DBConfig      db;
 	
	//0: no cache, -1: no expired
	protected long ttlInMillis=0;
	
	protected long autoRefreshInMillis = 0;
	
	protected Boolean readonly;
 	 
	protected Object tag;
	
	protected PrintWriter writer=null;
	 
	protected Cache cache;
	
	protected Dialect dialect;
 
	public Query(){		 
	}
	
	public Query(DBConfig db){
		 use(db);
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
	
	public long count(){
		Query countQuery=getDialect().getCountQuery(this);			
		return countQuery.getResult(Long.class); 
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
		if(segment!=null && segment.length()>0){			 
			sql.append(segment);
		}
		
		if(args!=null){
			for(Object arg:args){
				if(arg instanceof Collection){
					for(Object x:((Collection<?>)arg)){
						queryArgs.add(x);
					}
				}else{
					queryArgs.add(arg);
				}
			}
		}
		return this;
	}	
  
	/**
	 * Add segment to SQL only if the arg is not empty
	 * 
	 * @param segment the SQL segment
	 * @param arg  the SQL parameter
	 * @return  this Query
	 */
	public Query addIfArgNotEmpty(String segment,Object arg){
		if(arg==null){
			return this;
		}
		
		if(arg instanceof String){
			String s=(String)arg;
			if(s.trim().length()<1){
				return this;
			}
		}
		
		return add(segment, arg);
	}
	
	/**
	 * Add segment to SQL only if 'condition' is true
	 * 
	 * @param condition true: add the segment and args to SQL, otherwise ignore.
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
		return sql.toString(); 
	}
	
	/**
	 * 
	 * get the SQL which replace ? to the real value
	 * 
	 * @return the executable SQL
	 */
	public String getExecutableSQL() {
		 return MelpSQL.getExecutableSQL(getDialect(),getSql(),queryArgs);
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
		this.queryArgs.clear(); 
		
		return this;
	}

	public int parameterCount(){
		return queryArgs.size();
	}
 
	public List<Object> getParameters() {
		return queryArgs;
	}
	 
	public Query clearParameters(){
		this.queryArgs.clear();
	 
		return this;
	}
	
	public Query setParameters(List<Object> parameters) {
		this.queryArgs = parameters;
		
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
		return doExecute(new UpdateExecutor(),getSql(),queryArgs,false);
	}
	  
	/**
	 * Execute sql statements in transaction
	 * @param sqls sql statements
	 * @return each result of sql statements
	 */
	public int[] executeBatch(final String[] sqls){
		return Tx.execute(new Tx.Atom<int[]>() {
			public int[] execute() throws Throwable {
				return doExecute(new BatchSqlExecutor(),null,Arrays.asList(sqls),true);
			}
		});
	}
	
	/**
	 *  
	 * Execute in transaction
	 * @param batchParameters  the batch sql parameters to execute
	 * @return each result of sql statements
	 */
	public int[] executeBatch(final List<Object[]> batchParameters ){
		return Tx.execute(new Tx.Atom<int[]>() {
			public int[] execute() throws Throwable {
				return doExecute(new BatchStatementExecutor(),getSql(),batchParameters,true);
			}
		});
	}
	 
	public <X> X execute(Execute<X> execute){
		if(execute instanceof CacheableExecute) {
			return doCacheExecute( (CacheableExecute<X>) execute);
		}else {
			return doExecute(execute,getSql(),queryArgs,false);
		}
	}	
 	
	protected <X> X doCacheExecute(final CacheableExecute<X> execute){
		Object extraTag = execute.getCacheExtraTag();
	 	long ttlInMillis = getCacheTime();
	 	
	 	if(ttlInMillis == -1 ) {
	 		ttlInMillis = 10*365*24*3600*1000L;
	 	}
	 	
		if(ttlInMillis > 0 ){
			Cache cache   = getCache();
			CacheKey key  = createCacheKey(extraTag);
			 
			X value = getCachedObject(cache,key);
			
			if(value==null){
				value = doExecute(execute, getSql(),queryArgs,false);
				
				cache.putObject(key, value,ttlInMillis); 
				
				if(isDebug()){
					logger.info("Cached, "+Tools.getCachedInfo(key, value, ttlInMillis));
				}
			}else {
				if(isDebug()){
					logger.info("Loaded from cache, "+Tools.getCachedInfo(key, value, ttlInMillis));
				}
			} 
			
			if(autoRefreshInMillis>0) {
				boolean ok = CacheManager.getInstance().addAutoRefreshCache(key,cache,new Cacheable() {
					@Override
					public Object execute() {
						return doExecute(execute,getSql(),queryArgs,false); 
					}
				}, ttlInMillis, autoRefreshInMillis);
				
				if(ok) {
					if(isDebug()){
						logger.info("Add auto refresh("+autoRefreshInMillis+"ms): "+key);
					}
				}
			}
			
			return value;	
			 
		}
		
		return doExecute(execute,getSql(),queryArgs,false);
	}
	
	protected <X> X getCachedObject(Cache cache,CacheKey key) {
		Boolean isPutCacheMode = putCacheMode.get();
		if(isPutCacheMode != null) {
			return null;
		}
		
		X value= cache.getObject(key);
		return value;
	}
	
	protected CacheKey createCacheKey(Object extraTag) {
		CacheKey key  = new CacheKey(getCacheKey());
		key.update("extra:"+extraTag);
		 
		return key;
	}
	
	protected <X> X doExecute(Execute<X> x,String sql,List<?> parameters, boolean isBatchQuery){
		Tx tx=Tx.getTx();
		
		Connection conn=null; 
		try{
			conn= tx==null?getConnectionFromDB(true):getConnectionFromTx(tx);
			  
			if(!MelpString.isEmpty(sql) || isBatchQuery) {
				logExecutableSql(sql,parameters,isBatchQuery);
			}
			
			return x.execute(conn, sql, parameters);
			 
		}catch(SQLException e){
			String executeSQL=sql.toString();
			try{
				executeSQL=MelpSQL.getExecutableSQL(getDialect(),sql,parameters);
			}catch(Exception ex){
				MelpClose.close(ex);
			}
			
			throw new RuntimeException("SQL Exception: "+e.getMessage()+"\r\n========================================================================\r\n"
					                  +executeSQL+"\r\n========================================================================",e);
		}finally{
			if(tx==null){
				MelpClose.close(conn);
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
		HandlerResultSet<DataMap> resultHandler=new HandlerResultSet<DataMap>(this,DataMap.class);
		
		return execute(new ResultSetsExecutor<DataMap>(resultHandler,deepth));  
	}
	 
	/**
	 * Translate data to the resultClass
	 *  
	 * @param resultClass translate DataMap to the result class
	 * @param <T> result type
	 * @return the result object
	 */
	public <T> T getResult(final Class<T> resultClass){
		return getResult(new HandlerResultSet<T>(this,resultClass));
	}
	
	/**
	 * Translate data to the resultClass
	 *  
	 * @param resultHandler handle result set
	 * @param <T> result type
	 * @return the result object
	 */
	public <T> T getResult(final HandlerResultSet<T> resultHandler){
		if(!doExchange()){			
			queryCheck();
			
			return execute(new CacheableResultExecutor<T>(resultHandler));	 
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
		return getList(new HandlerResultSet<T>(this, resultClass));
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
		return getList(new HandlerResultSet<T>(this,resultClass),limit,offset);
	}	
	
	/**
	 * @param resultHandler handle result set
	 * @param limit The max number of records for this query
	 * @param offset Base 0, the first record is 0
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(HandlerResultSet<T> resultHandler,int limit,int offset) {
		Query listQuery=getDialect().getLimitQuery(this, limit, offset);
		return listQuery.getList(resultHandler); 
	}	 

	/**
	 * @param resultHandler handle result set
	 * @param <T> result type
	 * @return List Data
	 */
	public <T> DataTable<T> getList(final HandlerResultSet<T> resultHandler) {
		if(!doExchange()){		 
			queryCheck();
			
			return execute(new CacheableResultSetExecutor<T>(resultHandler));
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
		return getPage(new HandlerResultSet<T>(this,resultClass), limit, offset);
	}
	
	/**
	 * @param resultHandler handle result set
	 * @param limit The max number of records for this query
	 * @param offset   Base 0, the first record is 0
	 * @param <T> result type
	 * @return Page Data
	 */
	public <T> Page<T> getPage(HandlerResultSet<T> resultHandler,int limit,int offset) {
		if(!doExchange()){			
			queryCheck();
			
			Query countQuery=getDialect().getCountQuery(this);			
			long total=countQuery.getResult(Long.class);			
			 
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			DataTable<T>  list=listQuery.getList(resultHandler);
			 
			return new Page<T>(list,total,limit,offset); 
		}else{
			return new Page<T>();		 
		}
	}
	
	public <T> T load(final T result){
		if(!doExchange()){			 
			queryCheck();
			
			HandlerResultSet<T> resultHandler=new HandlerResultSet<T>(this,(Class<T>)result.getClass());
			
			return execute(new CacheableResultLoadExecutor<T>(resultHandler, result)); 
		}else{
			return result;
		}
	}
	 
	protected boolean doExchange(){
		DBExchange exchange=DBExchange.getExchange(false);
		if(exchange!=null){
			HandlerResultSet.processExchange(this,exchange);
			return true;
		}else{
			return false;
		}
	}
 	
	protected void queryCheck(){
		if(db==null){
			checkDbAndThrowException();
		}
	}
	
	public PrintWriter getPrintWriter(){
		if(writer==null){
			writer= new PrintWriter(new Writer(){
				public void write(char[] cbuf, int off, int len) throws IOException {
					 add(new String(cbuf,off,len));
				}
				public void flush() throws IOException {MelpClose.close();}
	
				public void close() throws IOException {MelpClose.close();}
			});
		}
		return writer;
	}
	   
	protected void logExecutableSql(String sql,List<?> parameters,boolean isBatchQuery){
		if(isDebug()){
			StringBuilder sb = new StringBuilder();
			if(isBatchQuery) {
				if(sql == null) { //batch sql
					for(Object s:parameters) {
						if(sb.length()>0) {
							sb.append(";\r\n");
						}
						sb.append(s);
					}
					
					logger.info("Executable batch SQL:\r\n"+ sb.toString());
				}else { //batch statement
					for(Object p:parameters) {
						Object[] args = (Object[]) p;
						
						String s = MelpSQL.getExecutableSQL(getDialect(),sql,Arrays.asList(args));
						
						if(sb.length()>0) {
							sb.append(";\r\n");
						}
						sb.append(s);
					}
					logger.info("Executable batch statement SQL:\r\n"+ sb.toString());
				}
			}else {
				String s = MelpSQL.getExecutableSQL(getDialect(),sql,parameters);
				sb.append(s);
				logger.info("Executable SQL:\r\n"+ sb.toString());
			}
		} 		 
	}
	
	protected boolean isDebug() {
		boolean debug=false;
		if(debugSql==null){
			debug  = "true".equalsIgnoreCase( DbProp.PROP_DB_SQL_DEBUG.getValue(db));
		}else{
			debug  = debugSql.booleanValue();
		}
		
		return debug;
	}
 
	
	public CacheKey getCacheKey() {
		CacheKey cacheKey = new CacheKey();
		cacheKey.setTag(tag);
		
		cacheKey.update("dbkey:" + db.getKey());
		cacheKey.update("tag:" + (tag == null ? "" : tag.toString()));
		cacheKey.update("sql:" + getSql());

		for (Object p : this.getParameters()) {
			cacheKey.update(p);
		}

		return cacheKey;
	}
	
	public Cache getCache(){
		if(this.cache!=null){
			return this.cache;
		}else if(ttlInMillis > 0){
			if(db!=null) {
				return getDb().getCfg().getCache();
			}else {
				return CacheManager.getInstance().getDefaultCache();
			}
		}else {
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
		
		if(db!=null){
			this.dialect=db.getDialect();
		}
		
		return this;
	}
	
	public Dialect getDialect(){
		return this.dialect;
	}
	
	public void setDialect(Dialect dialect){
		this.dialect=dialect;
	}
	
	protected void checkDbAndThrowException(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
	}
      
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public long getCacheTime() {
		return ttlInMillis;
	}

	/**
	 * Cache all query results returned by this query. eg: 
	 * 								<ul>
	 *                              	<li> getResult(...)</li>
	 *                              	<li> getList(...)</li>
	 *                              	<li> getPage(...)</li>
	 *                              	<li> load()</li> 
	 *                              </ul>
	 * 
	 * @param ttlInMillis         	cache time in millis. 
	 * 								<ul>
		 * 								<li> &gt;0: expired time.</li>
		 *            					<li>  0: no cache</li>
		 *            					<li> -1: never expired</li>
		 *            				</ul>
	 * @return this
	 * 
	 * @see #setCacheTime(long, long)
	 */
	public Query setCacheTime(long ttlInMillis) {
		return setCacheTime(ttlInMillis,0);
	}
	
	/**
	 * Cache all query results returned by this query. eg: 
	 * 								<ul>
	 *                              	<li> getResult(...)</li>
	 *                              	<li> getList(...)</li>
	 *                              	<li> getPage(...)</li>
	 *                              	<li> load()</li>
	 *                              </ul>
	 *                              
	 *                              
	 * @param ttlInMillis         	cache time in millis. 
	 * 								<ul>
	 * 									<li> &gt;0: expired time.</li>
	 *            						<li>  0: no cache</li>
	 *            						<li> -1: never expired</li>
	 *            					</ul>	
	 * @param autoRefreshInMillis   auto refresh cache in background in millis.
	 * 								<ul>
	 * 									<li> 0 : no refresh</li>
	 * 									<li> &gt;0: auto refresh</li>
	 * 								</ul>
	 * 
	 * @return this
	 */
	public Query setCacheTime(long ttlInMillis,long autoRefreshInMillis) {
		this.ttlInMillis         = ttlInMillis;
		this.autoRefreshInMillis = autoRefreshInMillis;
		 
		return this;
	}
	
	public long getAutoRefreshInMillis() {
		return autoRefreshInMillis;
	}

	public Query setAutoRefreshInSeconds(long autoRefreshInMillis) {
		this.autoRefreshInMillis = autoRefreshInMillis;
		return this;
	}
	
	public String toString(){
		return "SQL: "+getSql();
	}

	
}