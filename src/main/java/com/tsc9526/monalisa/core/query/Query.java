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

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.generator.DBExchange;
import com.tsc9526.monalisa.core.meta.Name;
import com.tsc9526.monalisa.core.query.dialect.Dialect;
import com.tsc9526.monalisa.core.query.model.ModelEvent;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;
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
@SuppressWarnings("unchecked")
public class Query {	
	static Logger logger=Logger.getLogger(Query.class.getName());
	
	/**
	 * 是否显示执行的SQL语句, 默认为: false
	 */
	public static boolean SQL_DEBUG=false;
	
	protected DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected StringBuffer sql=new StringBuffer();
	protected List<Object> parameters=new ArrayList<Object>();
	protected Class<?> resultClass;
	protected Object resultObject;
	protected MetaClass metaClass;
	protected DBConfig db;
 	
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
		 setResultClass(resultClass);
	}
	
	public Query(Class<?> resultClass){
		 setResultClass(resultClass);
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
			
			if( SQL_DEBUG || "true".equalsIgnoreCase( db.getProperty("sql.debug","false") ) ){
				logger.info(getExecutableSQL());
			}
			
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
							result=createRecord(rs,(T)resultObject); 											
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
			long total=countQuery.setResultClass(Long.class).getResult();			
			 
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			List<T>  list=listQuery.getList();
			 
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
	public <T> List<T> getList(int limit,int offset) {
		if(!DBExchange.doExchange(this)){			
			queryCheck();
			   
			Query listQuery=getDialect().getLimitQuery(this, limit, offset);
			List<T>  list=listQuery.getList();
			 
			return list;
		}else{
			return new ArrayList<T>();		 
		}
	}
	
	/**
	 * 如果没有调用setResultClass指定结果类, 则List存储的对象为{@link:com.tsc9526.monalisa.core.query.DataMap}
	 * @return List对象
	 */
	public <T> List<T> getList() {
		if(!DBExchange.doExchange(this)){			 
			queryCheck();
			
			return doExecute(new Execute<List<T>>(){
				public List<T> execute(PreparedStatement pst) throws SQLException {		
					List<T> result=new ArrayList<T>();
					ResultSet rs=null;
					try{
						rs=pst.executeQuery();				 		
						while(rs.next()){
							T r=createRecord(rs,null); 
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
			return new ArrayList<T>();
		}
	}	 
	
	
	protected void queryCheck(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
	}
	
	
	protected <T> T createRecord(ResultSet rs,T resultObject)throws SQLException{
		T r=resultObject;
		
		if(resultClass==Long.class || resultClass==long.class){
			r=(T)new Long(rs.getLong(1));			 
		}else if(resultClass==Integer.class || resultClass==int.class){
			r=(T)new Integer(rs.getInt(1));			 
		}else if(resultClass==Float.class || resultClass==float.class){
			r=(T)new Float(rs.getFloat(1));			 
		}else if(resultClass==Short.class || resultClass==short.class){
			r=(T)new Short(rs.getShort(1));			 
		}else if(resultClass==Byte.class || resultClass==byte.class){
			r=(T)new Byte(rs.getByte(1));			 
		}else if(resultClass==Double.class || resultClass==double.class){
			r=(T)new Double(rs.getDouble(1));			 
		}else if(resultClass==String.class){
			r=(T)rs.getString(1);
		}else if(resultClass==BigDecimal.class){
			r=(T)rs.getBigDecimal(1);
		}else if(resultClass==Date.class){
			r=(T)rs.getDate(1);
		}else{								 					
			r=toResult(rs,r);	
		}
		
		return r;
	}	 
   
	protected <T> T toResult(ResultSet rs,T r) throws SQLException{
		if(resultClass!=null || r!=null){
			try{
				if(r==null){
					r=(T)resultClass.newInstance();						 
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
						 
			load(rs,r);
			
		}else{ 		
			//未指定结果类, 则采用HashMap
			DataMap x=new DataMap();
			
			loadToDataMap(rs,x); 
			
			r=(T)x;
		}	
		
		return r;
	}
	
	protected <T> void loadToDataMap(ResultSet rs,DataMap r)throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		 
		for(int i=1;i<=rsmd.getColumnCount();i++){
			String name =rsmd.getColumnLabel(i);
			if(name==null || name.trim().length()<1){
				name =rsmd.getColumnName(i);
			}
			
			r.put(name, rs.getObject(i));
		}
	}
	
	protected <T> void load(ResultSet rs,T r)throws SQLException{
		if(r instanceof Model<?>){
			Model<?> m=(Model<?>)r;
			if(m.listener()!=null){
				m.listener().before(ModelEvent.LOAD, m);
			}
		}
		
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
				fgs.setObject(r, v);
			}						
		}
		
		if(r instanceof Model<?>){
			Model<?> m=(Model<?>)r;
			if(m.listener()!=null){
				m.listener().after(ModelEvent.LOAD, m,0);
			}
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
		if(db!=null){
			if(db.isCfgFileChanged()){
				this.db=dsm.getDBConfig(db.key(),db.getDb());
			}else{
				this.db = db;
			}
		}
		return this;
	}
	
	public Dialect getDialect(){
		if(db==null){
			throw new RuntimeException("Query must use db!");
		}
		
		return dsm.getDialect(db);
	}
  
	public Query setResultObject(Object resultObject){
		this.resultObject=resultObject;
		this.resultClass = resultObject.getClass();
		this.metaClass=ClassHelper.getMetaClass(resultClass);
		return this;
	}

	public Class<?> getResultClass(){
		return this.resultClass;
	}
	
	public Query setResultClass(Class<?> resultClass) {		
		this.resultClass = resultClass;
		this.metaClass=ClassHelper.getMetaClass(resultClass);
		return this;
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