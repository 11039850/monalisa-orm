package com.tsc9526.monalisa.core.query.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.Execute;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 
 * 完成数据库模型的插入操作
 * 
 * @author zzg.zhou(11039850@qq.com)
 *
 * @param <T> 数据库模型类型
 */
@SuppressWarnings({"rawtypes"})
public class Insert<T extends Model>{
	protected T  model;
	
	protected DBConfig db;
	
	public  Insert(T model){
		this.model=model;		 
	}
	
	public T getModel(){
		return this.model;
	}
			
	public Insert set(String name,Object value){		
		this.model.set(name,value);
		return this;
	}	
	
	public Insert use(DBConfig db){
		this.db=db;
		return this;
	}
	
	public DBConfig db(){
		return this.db==null?model.db():this.db;
	}
	/**
	 * insert到数据库
	 * 
	 * @return 成功变更的记录数
	 */
	public int insert(){			
		return insert(false);
	}
	
	
	/**
	 * insert到数据库
	 * 
	 * @param updateOnDuplicateKey   true: 如果插入时出现主键冲突则进行更新操作
	 * 
	 * @return 成功变更的记录数
	 */
	public int insert(boolean updateOnDuplicateKey){	 
		Query query=model.getDialect().insert(model, updateOnDuplicateKey);
		query.use(db());
		return query.execute(new AutoKeyCallback());  
	}	
	
	
	/**
	 * insert到数据库， 忽略其中为null的字段
	 * 
	 * @return 成功变更的记录数
	 */
	public int insertSelective(){			
		return insertSelective(false);
	}
		
	 
	/**
	 * insert到数据库， 忽略其中为null的字段
	 * 
	 * @param updateOnDuplicateKey   true: 如果插入时出现主键冲突则进行更新操作
	 * 
	 * @return 成功变更的记录数
	 */
	public int insertSelective(boolean updateOnDuplicateKey){	 
		Query query=model.getDialect().insertSelective(model, updateOnDuplicateKey);
		query.use(db());
		return query.execute(new AutoKeyCallback());
	}	
	
	private class AutoKeyCallback implements Execute<Integer>{
		private boolean autoKey=false;
		
		AutoKeyCallback(){
			FGS fgs=model.autoField();
			if(fgs!=null && fgs.getObject(model)==null){
				autoKey=true;
			}
		}
		public PreparedStatement preparedStatement(Connection conn, String sql)throws SQLException {
			if(autoKey){
				return conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			}else{
				return conn.prepareStatement(sql);
			}
		}
		 
		public Integer execute(PreparedStatement pst) throws SQLException {
			int r=pst.executeUpdate();
		 
			if(autoKey){
				ResultSet rs = pst.getGeneratedKeys();   
	            if (rs.next()) {  
	                Long id = rs.getLong(1);   
	                model.autoField().setObject(model, id.intValue()); 
	            }  
	            rs.close();
			}			
			
			return r;
		}
		 
	}
}
