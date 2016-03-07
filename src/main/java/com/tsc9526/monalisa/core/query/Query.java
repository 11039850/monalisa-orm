package com.tsc9526.monalisa.core.query;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.meta.Name;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelEvent;
import com.tsc9526.monalisa.core.query.sqlfile.SqlFile;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.SQLHelper;
 

/**
 * 数据库查询对象, 基本用法: <br>
 * <code>
 * Query q=new Query(); <br>
 * q.use(db); <br>
 * q.add("select * from xxx where id=?",1); <br>
 * List&lt;Result&gt; r=q.getList(Result.class);   <br>
 * Page&lt;Result&gt; p=q.getPage(Result.class,10,0);<br>
 * Result       x=q.getResult(Result.class);<br>
 * </code>
 * 
 * @author zzg.zhou(11039850@qq.com)
 */

@SuppressWarnings({"unchecked"})
public class Query {	
	static Log logger=LogFactory.getLog(Query.class.getName());
	
	/**
	 * 是否显示执行的SQL语句, 默认为: false
	 */
	public static boolean SQL_DEBUG=false;
	
	protected DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected StringBuffer  sql=new StringBuffer();
	protected List<Object>  parameters=new ArrayList<Object>();
 	 
	protected DBConfig      db;
 	
	protected int cacheTime=0;
	
	protected Boolean readonly;
 	
	protected List<List<Object>> batchParameters=new ArrayList<List<Object>>();
	
	public Query(){		 
	}
	
	public Query(DBConfig db){
		 this.db=db;
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
  
	/**
	 * 如果参数非空，则添加该SQL片段，否则忽略
	 * 
	 * @param segment  SQL片段
	 * @param args  参数
	 * @return 查询本身
	 */
	public Query addIgnoreEmpty(String segment,Object ... args){
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
	
	public boolean isEmpty(){
		return sql.length()==0;
	}
	
	/**
	 * 
	 * @return 原始的SQL语句, 中间可能会有参数
	 */
	public String getSql() {
		String r=sql.toString();
		if(r.matches("[a-zA-Z_][a-zA-Z0-9_]+")){
			//load sql by id
			queryCheck();
			
			return SqlFile.getSql(db, r);
		}else{
			return r;
		}
	}
	
	/**
	 * 
	 * @return 处理过参数后的SQL语句
	 */
	public String getExecutableSQL() {
		 return SQLHelper.getExecutableSQL(getSql(), parameters);
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
			
			pst=conn.prepareStatement(getSql());
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
			
			pst=x.preparedStatement(conn,getSql());
			 
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
	
	public DataMap getResult(){
		return getResult(DataMap.class);				
	}
	 
	
	public DataTable<DataMap> getList() {
		return getList(DataMap.class);
	}
	
	/**
	 * @param limit 
	 *   The max number of records for this query
	 *    
	 * @param offset  
	 *   Base 0, the first record is 0
	 * @return List对象
	 */
	public DataTable<DataMap> getList(int limit,int offset) {		 
		return getList(DataMap.class,limit,offset);		 
	}
	
	public <T> DataTable<T> getList(Class<T> resultClass,int limit,int offset) {		 
		return getList(new ResultCreator<T>(this,resultClass),limit,offset);
	}	
	
	public <T> DataTable<T> getList(ResultCreator<T> resultCreator,int limit,int offset) {
		Query listQuery=getDialect().getLimitQuery(this, limit, offset);
		DataTable<T>  list=listQuery.getList(resultCreator);
			
		return list;
	}	 
		
	public Page<DataMap> getPage(int limit,int offset) {
		return getPage(DataMap.class,limit, offset);
	}
	
	/**
	 * 将查询结果转换为指定的类
	 *  
	 * @param resultClass
	 * @return
	 */
	public <T> T getResult(final Class<T> resultClass){
		return getResult(new ResultCreator<T>(this,resultClass));
	}
	
	/**
	 * 将查询结果转换为指定的类
	 *  
	 * @param resultClass
	 * @return
	 */
	public <T> T getResult(final ResultCreator<T> resultCreator){
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			
			return doExecute(new Execute<T>(){ 
				public T execute(PreparedStatement pst) throws SQLException {				 
					T result=null;
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				
						if(rs.next()){	
							result=resultCreator.createResult(rs); 											
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
	
	public <T> Page<T> getPage(Class<T> resultClass,int limit,int offset) {
		return getPage(new ResultCreator<T>(this,resultClass), limit, offset);
	}
	
	/**
	 * @param limit 
	 *   The max number of records for this query
	 *    
	 * @param offset  
	 *   Base 0, the first record is 0
	 * @return Page对象
	 */
	public <T> Page<T> getPage(ResultCreator<T> resultCreator,int limit,int offset) {
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			
			Query countQuery=getDialect().getCountQuery(this);			
			long total=countQuery.getResult(Long.class);			
			 
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			DataTable<T>  list=listQuery.getList(resultCreator);
			 
			Page<T> page=new Page<T>(list,total,limit,offset);
		
			return page;
		}else{
			return new Page<T>();		 
		}
	}
	
	 
	public <T> DataTable<T> getList(final Class<T> resultClass) {
		return getList(new ResultCreator<T>(this, resultClass));
	}
	
	public <T> DataTable<T> getList(final ResultCreator<T> resultCreator) {
		if(!DBExchange.doExchange(this)){			 
			queryCheck();
			
			return doExecute(new Execute<DataTable<T>>(){
				public DataTable<T> execute(PreparedStatement pst) throws SQLException {		
					DataTable<T> result=new DataTable<T>();
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				 		
						while(rs.next()){
							T r=resultCreator.createResult(rs); 
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
	
	public <T> T load(final T result){
		if(!DBExchange.doExchange(this)){			 
			queryCheck();
			
			return doExecute(new Execute<T>(){
				public T execute(PreparedStatement pst) throws SQLException {		
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				 		
						if(rs.next()){
							new ResultCreator<T>(Query.this,(Class<T>)result.getClass()).load(rs, result);							 				
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
			return result;
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
   
	 
	public boolean isReadonly() {
		if(readonly!=null){
			return readonly;
		}else{
			String x=getSql().toLowerCase().trim();
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
 
	public static class ResultCreator<T>{
		private Query query;
		private Class<T> resultClass;
		
		public ResultCreator(Query query,Class<T> resultClass){
			this.query=query;
			this.resultClass=resultClass;
		}
		
		public T createResult(ResultSet rs)throws SQLException{
			if(resultClass==Long.class || resultClass==long.class){
				return (T)new Long(rs.getLong(1));			 
			}else if(resultClass==Integer.class || resultClass==int.class){
				return (T)new Integer(rs.getInt(1));			 
			}else if(resultClass==Float.class || resultClass==float.class){
				return (T)new Float(rs.getFloat(1));			 
			}else if(resultClass==Short.class || resultClass==short.class){
				return (T)new Short(rs.getShort(1));			 
			}else if(resultClass==Byte.class || resultClass==byte.class){
				return (T)new Byte(rs.getByte(1));			 
			}else if(resultClass==Double.class || resultClass==double.class){
				return (T)new Double(rs.getDouble(1));			 
			}else if(resultClass==String.class){
				return (T)rs.getString(1);
			}else if(resultClass==BigDecimal.class){
				return (T)rs.getBigDecimal(1);
			}else if(resultClass==Date.class){
				return (T)rs.getDate(1);
			}else if(resultClass==byte[].class){
				return (T)rs.getBytes(1);
			}else {
				try{
					if(Map.class.isAssignableFrom(resultClass)){				
						return (T)loadToMap(rs, new DataMap());				 
					}else{				 
						return (T)load(rs,resultClass.newInstance());
					}
				}catch(IllegalAccessException e){
					throw new RuntimeException(e);
				}catch(InstantiationException e){
					throw new RuntimeException(e);
				}
			}		 
		}
		

		protected T load(ResultSet rs,T result)throws SQLException{
			if(result instanceof Model<?>){
				loadModel(rs,(Model<?>)result);
			}else{		
				loadResult(rs, result);
			}
			return result;
		}
	  
		protected DataMap loadToMap(ResultSet rs, DataMap map)throws SQLException{
			ResultSetMetaData rsmd=rs.getMetaData();
			 
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String name =rsmd.getColumnLabel(i);
				if(name==null || name.trim().length()<1){
					name =rsmd.getColumnName(i);
				}
				
				map.put(name, rs.getObject(i));
			}
			
			return map;
		}		
		
		protected void loadModel(ResultSet rs,Model<?> model)throws SQLException{
			Class<?> clazz=ClassHelper.findClassWithAnnotation(model.getClass(),DB.class);
			if(clazz==null && model.use()==null){ 
				model.use(query.getDb());
			}
			
			model.before(ModelEvent.LOAD);
			
			ResultSetMetaData rsmd=rs.getMetaData();
			
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String name =rsmd.getColumnName(i);
				
				Name nColumn =new Name(false).setName(name);
				 
				FGS fgs=model.field(nColumn.getJavaName());			 
				if(fgs!=null){
					Object v=rs.getObject(i);
					fgs.setObject(model, v);
				}
			}
			
			model.after(ModelEvent.LOAD, 0);
		}	
		
		protected T loadResult(ResultSet rs,T result)throws SQLException{
			MetaClass metaClass=ClassHelper.getMetaClass(result.getClass());
			
			ResultSetMetaData rsmd=rs.getMetaData();
			
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String name =rsmd.getColumnName(i);
				
				Name nColumn =new Name(false).setName(name);
				 
				FGS fgs=metaClass.getField(nColumn.getJavaName());
				if(fgs==null){
					String table=rsmd.getTableName(i);
					if(table!=null && table.length()>0){
						Name nTable  =new Name(true).setName(table);
											
						String jname=nTable.getJavaName()+"$"+nColumn.getJavaName();
						fgs=metaClass.getField(jname);
					}
				}
				if(fgs!=null){
					Object v=rs.getObject(i);
					fgs.setObject(result, v);
				}						
			}
			return result;
		}
	}	 
	
}