package com.tsc9526.monalisa.core.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import com.tsc9526.monalisa.core.meta.Name;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelEvent;
import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class QueryResult{
	protected Object        resultObject;			
	protected Class<?>      resultClass;	 
	 
	public QueryResult(){		
	} 	
	
	public <T> T createResult(Query query,ResultSet rs)throws SQLException{		 
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
		}else{
			return fromResultSet(query,rs);				
		}		 
	}	 
   
	protected <T> T fromResultSet(Query query,ResultSet rs) throws SQLException{
		T r=newResult(query);
		if(r!=null){
			load(query,rs,r);			
		}else{ 		
			//未指定结果类, 则采用HashMap
			DataMap x=new DataMap();
			
			loadToDataMap(query,rs,x); 
			
			r=(T)x;
		}	
		
		return r;
	}
	
	protected <T> T newResult(Query query) {
		try{
			if(resultObject!=null){
				return (T)resultObject;			 
			}else if(resultClass!=null){				
				return (T)resultClass.newInstance();
			}else{
				return null;
			} 
		}catch(Exception e){
			throw new RuntimeException(e);
		}		
	}
	
	protected <T> void loadToDataMap(Query query,ResultSet rs,DataMap r)throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		 
		for(int i=1;i<=rsmd.getColumnCount();i++){
			String name =rsmd.getColumnLabel(i);
			if(name==null || name.trim().length()<1){
				name =rsmd.getColumnName(i);
			}
			
			r.put(name, rs.getObject(i));
		}
	}
	
	protected <T> void load(Query query,ResultSet rs,T r)throws SQLException{
		if(r instanceof Model<?>){
			Model<?> model=(Model<?>)r;
			model.use(query.getDb());
			 
			model.before(ModelEvent.LOAD);
		 	
			loadModel(query,rs,model);
			
			((Model<?>)r).after(ModelEvent.LOAD,0);
		}else{		
			loadResult(query,rs, r);
		}
	}
	
	protected <T> void loadModel(Query query,ResultSet rs,Model<?> r)throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		
		for(int i=1;i<=rsmd.getColumnCount();i++){
			String name =rsmd.getColumnName(i);
			
			Name nColumn =new Name(false).setName(name);
			 
			FGS fgs=r.field(nColumn.getJavaName());			 
			if(fgs!=null){
				Object v=rs.getObject(i);
				fgs.setObject(r, v);
			}
		}
	}	
	
	protected <T> void loadResult(Query query,ResultSet rs,T r)throws SQLException{
		MetaClass metaClass=ClassHelper.getMetaClass(r.getClass());
		
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
	}
	
	public QueryResult setResultObject(Object resultObject) {
		this.resultObject=resultObject;
		this.resultClass=resultObject.getClass();
		return this;
	}
	
	public QueryResult setResultClass(Class<?> resultClass) {			
		this.resultClass=resultClass;
		return this;
	}	
	 
	public Object getResultObject() {
		return resultObject;
	}

	public Class<?> getResultClass() {
		return resultClass;
	}	  
}