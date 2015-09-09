package com.tsc9526.monalisa.core.query.dialect;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.EnumHelper;
import com.tsc9526.monalisa.core.tools.JsonHelper;
import com.tsc9526.monalisa.core.tools.TypeHelper;

import freemarker.log.Logger;
 
@SuppressWarnings({"rawtypes"})
public abstract class Dialect{
	static Logger logger=Logger.getLogger(Dialect.class.getName());
	
	public abstract String getUrlPrefix();
	
	public abstract String getDriver();
	
	public abstract String getSchema(String jdbcUrl);
	
	public abstract String getColumnName(String name);
	
	public abstract String getTableName(String name);
 	
	public abstract Query getLimitQuery(Query origin,int limit ,int offset);
	
	 
	public abstract void loadMetaTableDetails(DBConfig db,MetaTable table);
	
	public abstract boolean createTableIfNotExists(DBConfig db,MetaTable table,String theTableName); 
	
	protected Query doInsert(boolean selective,Model model,boolean updateOnDuplicateKey){
		Query query=new Query().setResultClass(model.getClass());
		
		if(updateOnDuplicateKey){
			query.add("REPLACE ");
		}else{
			query.add("INSERT "); 
		}		 
		
		query.add("INTO "+getTableName(model.table().name())+"(");
		
		for(Object o:model.fields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getField().getAnnotation(Column.class);
			Object v=getValue(fgs,model);;
			if(selective){
				if(v!=null){
					if(query.parameterCount()>0){
						query.add(", ");
					}
					query.add(getColumnName(c.name()),v);
				}
			}else{
				if(c.auto()==false || v!=null){
					if(query.parameterCount()>0){
						query.add(", ");
					}
					query.add(getColumnName(c.name()),v);
				}
			}
		}
		query.add(")VALUES(");
		
		for(int i=0;i<query.parameterCount();i++){			 
			query.add(i>0?", ?":"?");
		}
		query.add(")");
	 
		 
		return query;		 
	}
	
	public Query insert(Model model,boolean updateOnDuplicateKey){		 		 
		return doInsert(false,model,updateOnDuplicateKey);
	}
	
	public Query insertSelective(Model model,boolean updateOnDuplicateKey){
		return doInsert(true,model,updateOnDuplicateKey);
	} 
	
	
	public Query deleteAll(Model model){
		Query query=new Query().setResultClass(Long.class);
		
		query.add("TRUNCATE TABLE "+getTableName(model.table().name()));
		return query;
	}
	
	public Query delete(Model model){
		Query q=getWhereByPrimaryKey(model);
		
		return delete(model,q.getSql(),q.getParameters());
	}
	 
	 
	public Query delete(Model model,String whereStatement,Object ... args){
		if(whereStatement==null || whereStatement.trim().length()==0){
			throw new RuntimeException("Model: "+model.getClass()+" delete fail, no where cause.");
		}
		
		Query query=new Query().setResultClass(Long.class);
		
		query.add("DELETE FROM "+getTableName(model.table().name())+" ");
		if(whereStatement.toUpperCase().trim().startsWith("WHERE")){
			query.add(whereStatement, args);
		}else{
			query.add("WHERE ").add(whereStatement,  args);
		}
		
		return query;
	}
	
	public Query update(Model model){		 
		Query q=getWhereByPrimaryKey(model);
		
		return update(model,q.getSql(),q.getParameters());
	}	
	
	public Query update(Model model,String whereStatement,Object ... args){		
		return doUpdate(false, model, whereStatement, args);		 				 
	}
	
	public Query updateSelective(Model model,String whereStatement,Object ... args){	
		return doUpdate(true, model, whereStatement, args);
	}
	
	protected Query doUpdate(boolean selective,Model model,String whereStatement,Object ... args){		
		if(whereStatement==null || whereStatement.trim().length()==0){
			throw new RuntimeException("Model: "+model.getClass()+" update fail, no where cause.");
		}		
	 
		Query query=new Query().setResultClass(Long.class);
		
		query.add("UPDATE "+getTableName(model.table().name())+" SET ");
		for(Object o:model.fields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getField().getAnnotation(Column.class);
			Object v=getValue(fgs,model);;
			if(selective){
				if((c.key()==false || model.enableUpdateKey()) && v!=null){
					if(query.parameterCount()>0){
						query.add(", ");
					}
					query.add(getColumnName(c.name())+"=?",v);				 		
				}
			}else{
				if(c.key()==false || (model.enableUpdateKey() && v!=null)){
					if(query.parameterCount()>0){
						query.add(", ");
					}
					query.add(getColumnName(c.name())+"=?",v);				 		
				}
			}
		}		
		query.add(" ");
		
		if(whereStatement.toUpperCase().trim().startsWith("WHERE")){
			query.add(whereStatement, args);
		}else{
			query.add("WHERE ").add(whereStatement,  args);
		}	  
		
		return query;		 				 
	}
	
	public Query updateSelective(Model model){		 
		Query q=getWhereByPrimaryKey(model);		 
		
		return updateSelective(model,q.getSql(),q.getParameters());
	}
	
	public Query load(Model model){
		Query query=new Query().setResultObject(model);
		 
		query.add("SELECT "+model.filterFields()+" FROM ").add(getTableName(model.table().name())).add(" WHERE ");
		for(Object o:model.fields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getField().getAnnotation(Column.class);
			if(c.key()){
				Object v=getValue(fgs,model);;
				if(v==null){
					throw new RuntimeException("Model: "+model.getClass()+" load fail, Primary key is null: "+c.name());
				}
				
				if(query.parameterCount()>0){
					query.add(" AND ");
				}
				query.add(getColumnName(c.name())+"=?", v);
			}
		}		
		
		if(query.parameterCount()<1){
			throw new RuntimeException("Model: "+model.getClass()+" load fail, no primary key.");
		}
		
		return query;		  
	}
		
	
	public Query selectOne(Model model,String whereStatement,Object ... args){
		Query query=select(model, whereStatement, args);
		return getLimitQuery(query,1,0); 
	}
 	
	public Query select(Model model,String whereStatement,Object ... args){
		Query query=new Query().setResultClass(model.getClass());
		
		query.add("SELECT "+model.filterFields()+" FROM ").add(getTableName(model.table().name()));
		if(whereStatement!=null && whereStatement.length()>0){
			query.add(" ");
			
			if(whereStatement.toUpperCase().startsWith("WHERE")){
				query.add(whereStatement, args);
			}else if(whereStatement.toUpperCase().startsWith("ORDER")){
				query.add(whereStatement, args);
			}else{
				query.add("WHERE ").add(whereStatement,  args);
			}
		}
		
		return query;
	}
	
	public Query count(Model model,String whereStatement,Object ... args){
		Query query=new Query().setResultClass(Long.class);
		query.add("SELECT COUNT(*) FROM ").add(getTableName(model.table().name()));
		if(whereStatement!=null && whereStatement.length()>0){
			query.add(" ");
			
			if(whereStatement.toUpperCase().startsWith("WHERE")){
				query.add(whereStatement, args);
			}else{
				query.add("WHERE ").add(whereStatement,  args);
			}
		}
		return query;
	}
	 
	
	
	public Query notin(Query query,Object[] values){
		return inOrNotIn("NOT IN",query,values);		 
	}
 
	public Query in(Query query,Object[] values){
		return inOrNotIn("IN",query,values);
	}
	
	protected Query inOrNotIn(String keyInOrNotin,Query query,Object[] values){
		String sql=query.getSql();
		if(sql.length()>0){
			char c=sql.charAt(sql.length()-1);
			if(c!=' ' &&  c!='\n' && c!='\t'){
				query.add(" ");
			}
		}
		
		query.add(keyInOrNotin).add("(");
		
		for(int i=0;i<values.length;i++){			 
			query.add(i>0?", ?":"?",values[i]);
		}
		query.add(")");
		
		return query;
	}
	
	
	protected Query getWhereByPrimaryKey(Model model){
		Query query=new Query();
		  
		for(Object o:model.fields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getField().getAnnotation(Column.class);
			if(c.key()){
				Object v=getValue(fgs,model);
				if(v==null){
					throw new RuntimeException("Model: "+model.getClass()+", Primary key is null: "+c.name());
				}				
				 
				if(!query.isEmpty()){
					query.add(" AND ");
				}
				
				query.add(getColumnName(c.name())+" = ?",v);				
			}
		}
		
		return query;
	}
	
	protected Object getValue(FGS fgs,Model model) {
		Object v=fgs.getObject(model);
		if(v!=null){
			Column c=fgs.getField().getAnnotation(Column.class);			
			String type=TypeHelper.getJavaType(c.jdbcType());
			
			if(v.getClass().isEnum()){
				if(type.equals("String")){
					 return EnumHelper.getStringValue((Enum<?>)v);
				}else{
					return EnumHelper.getIntValue((Enum<?>)v);
				}
			}else if(v.getClass() == Boolean.class || v.getClass()==boolean.class){				 
				if( (Boolean)v ){
					if(type.equals("String")){
						return "TRUE";
					}else{
						return 1;
					}
				}else{
					if(type.equals("String")){
						return "FALSE";
					}else{
						return 0;
					}
				} 
			}else if(v.getClass().isArray() && v.getClass()!=byte[].class){
				if(v.getClass()==byte[].class){
					return v;
				}else{
					JsonArray array=new JsonArray();
					Object[] os=(Object[])v;
					for(Object o:os){
						array.add(new JsonPrimitive(o==null?"":o.toString()));
					}
					return array.toString();
				}				
			}else if(v.getClass()==JsonObject.class){
				return v.toString();
			}else if(v.getClass().isPrimitive() || v.getClass().getName().startsWith("java.")){				
				return v;
			}else if(type.equals("String")){
				return JsonHelper.getGson().toJson(v);
			}
		}
		
		return v;		
	}
	
	public Query getCountQuery(Query origin){
		Query query=new Query();
		query.use(origin.getDb());
		String sql=origin.getSql().toLowerCase();
	 	query.add(getCountSql(sql), origin.getParameters());
		
		return query;
	} 
	
	protected String getCountSql(String sql){
    	String cql=sql;
    	
    	String loweredString = sql.toLowerCase();
     
    	int orderByIndex = loweredString.indexOf("order by");
    	if(orderByIndex>0){
    		cql=sql.substring(0,orderByIndex);
    	}
    	
    	int groupByIndex=loweredString.indexOf("group by");
    	if(groupByIndex>0){
    		return "select count(*) as cnt from( "+cql+") as tmp";
    	}else{    		
	    	int p=loweredString.indexOf("from");
	    	while(p>0){	    		
	    		char left =loweredString.charAt(p-1);
	    		char right=loweredString.charAt(p+4);
	    		if(isSplitChar(left) && isSplitChar(right)){
	    			break;
	    		}else{
	    			p=loweredString.indexOf("from",p+4);
	    		}	    			
	    	}
	    	
	    	if(p>0){
	    		cql="select count(*) as cnt "+cql.substring(p);
	    		return cql;
	    	}else{
	    		return sql;
	    	}
    	}
    }
    
    protected boolean isSplitChar(char c){
    	return c==' '|| c=='\t' || c=='\r' || c=='\n';
    }

 
}
