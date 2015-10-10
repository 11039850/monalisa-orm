package com.tsc9526.monalisa.core.query.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
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
		if(name.startsWith("`")){
			return name;
		}else{
			return "`"+name+"`";
		}
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
			 
			rs=stmt.executeQuery("SHOW CREATE TABLE "+getTableName(table.getName()));
			if(rs.next()){
				String createSQL=rs.getString(2);
				
				int p=createSQL.indexOf("(");
				
				createSQL="CREATE TABLE IF NOT EXISTS "+getTableName(CreateTable.TABLE_VAR)+createSQL.substring(p);
				
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

	public boolean createTableIfNotExists(DBConfig db,MetaTable table,String theTableName){
		String sql  =table.getCreateTable().getCreateSQL(theTableName);			 
		sql=sql.replaceFirst("\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
			
		Connection conn=null;
		Statement stmt=null;
		try{
			conn=DataSourceManager.getInstance().getDataSource(db).getConnection();
			stmt=conn.createStatement();
			
			logger.info(sql);
			
			stmt.execute(sql);
			
			return true;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(stmt);
			CloseQuietly.close(conn);
		}				
	}
	
	
}
