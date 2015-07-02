package com.tsc9526.monalisa.core.query.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

public class MysqlDialect extends Dialect{
	
	public String getUrlPrefix(){
		return "jdbc:mysql://";
	}
	
	public String getDriver(){
		return "com.mysql.jdbc.Driver";
	}	 
	
	public String getSchema(String jdbcUrl){
		String schema="";
		
		String prefix=getUrlPrefix();
		
		if(jdbcUrl.startsWith(prefix)){
			int p=jdbcUrl.indexOf("/",prefix.length());
			if(p>0){
				schema=jdbcUrl.substring(p+1);
				p=schema.indexOf("?");
				if(p>0){
					schema=schema.substring(0,p);
				}
			}
		}		
		
		return schema;
	} 
	
	public String getColumnName(String name){
		if(name.startsWith("`")){
			return name;
		}else{
			return "`"+name+"`";
		}
	}
	
	public String getTableName(String name){
		return "`"+name+"`";
	}	 	 	 
		 
	public Query getLimitQuery(Query origin,int limit ,int offset){
		Query query=new Query().setResultClass(origin.getResultClass());
		
		query.use(origin.getDb());
		query.add(origin.getSql());
		query.setParameters(origin.getParameters()); 
		query.add(" LIMIT "+limit+" OFFSET "+offset);
		
		return query;
	}
	
	public void loadMetaTableDetails(DBConfig db,MetaTable table) {
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			conn=db.getDataSource().getConnection();
			stmt=conn.createStatement();
			
			rs=stmt.executeQuery("SHOW CREATE TABLE "+table.getName());
			if(rs.next()){
				String createSQL=rs.getString(2);
				CreateTable createTable=new CreateTable(table.getName(), createSQL);
				table.setCreateTable(createTable);
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(rs);
			CloseQuietly.close(stmt);
			CloseQuietly.close(conn);
		}
	}

	public String getCreateTableSQL(CreateTable create,String theTableName){
		if(create!=null){			 
			String createSQL  =create.getCreateSQL();
			String tableName  =create.getTableName();
			if(tableName.equalsIgnoreCase(theTableName)==false){
				String lower=createSQL.toLowerCase();
				int p1=lower.indexOf("table");
				int p2=lower.indexOf(tableName.toLowerCase(),p1);
				
				String sql=createSQL.substring(0,p1+5); 
				sql+=" IF NOT EXISTS ";
				sql+=theTableName;
				sql+=createSQL.substring(p2+tableName.length()+1);
				
				sql=sql.replaceFirst("\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
				
				return sql;
			}else{
				return "";
			}
		}else{
			return null;
		}
	}
	
}
