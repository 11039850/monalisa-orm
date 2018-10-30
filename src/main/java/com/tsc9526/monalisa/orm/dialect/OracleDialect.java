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
package com.tsc9526.monalisa.orm.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import oracle.jdbc.driver.OracleConnection;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.SimpleDataSource;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({ "rawtypes"})
public class OracleDialect extends Dialect{

	@Override
	public String getUrlPrefix() {
		return "jdbc:oracle:thin:@//";
	}

	@Override
	public String getDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public String geCatalog(String jdbcUrl) {
		return null;
	}

	@Override
	public String getSchema(String jdbcUrl) {
		//jdbc:oracle:thin:@//localhost/orcl
		String schema = "";

		String prefix = getUrlPrefix();

		if (jdbcUrl.startsWith(prefix)) {
			int p = jdbcUrl.indexOf('/', prefix.length());
			if (p > 0) {
				schema = jdbcUrl.substring(p + 1);
				p = schema.indexOf('?');
				if (p > 0) {
					schema = schema.substring(0, p);
				}
			}
		}

		return schema;
	}

	@Override
	public String getColumnName(String name) {
		if (name.startsWith("\"")) {
			return name;
		} else {
			return "\"" + name + "\"";
		}
	}

	@Override
	public String getTableName(String name) {
		return getColumnName(name);
	}
 
	public String getLimitSql(String orignSql, int limit,int offset){
		String sql = orignSql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}

		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		if (offset>0) {
			pagingSelect.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ FROM ( ");
		}
		else {
			pagingSelect.append("SELECT * FROM ( ");
		}
		pagingSelect.append(sql);
		if (offset>0) {
			pagingSelect.append(" ) row_ ) WHERE rownum <= "+limit+" and rownum_ > "+offset);
		}
		else {
			pagingSelect.append(" ) WHERE rownum <= "+limit);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " FOR UPDATE" );
		}

		return pagingSelect.toString();
	}
 
	
	public Query insertOrUpdate(Model model){
		List<FGS> uniqueFields = getUniqueFields(model); 
		if(uniqueFields.isEmpty()){
			return insert(model);
		}
		 
		Query query=createQuery(model);
		
		query.add("MERGE INTO ").add(getTableName(model.table())).add(" m USING dual ON (");
		
		int i=0;
		Set<String> unames=new LinkedHashSet<String>();
		for(FGS fgs:uniqueFields){
			Object v=fgs.getObject(model);
			if(i>0){
				query.add(" AND ");
			}
			Column c=fgs.getAnnotation(Column.class);
			query.add(getColumnName(c.name()) + " = ?",v);
			
			unames.add(fgs.getFieldName());
			i++;
		}
		
		query.add(")");
		query.add("\r\n WHEN NOT MATCHED THEN INSERT ");
		
		addNameValues(query,model);
		  
		i=0;
		StringBuilder updateSql = new StringBuilder("\r\n WHEN MATCHED     THEN UPDATE SET ");
		List<Object> updateArgs = new ArrayList<Object>();
		FGS createTime = model.fieldGetCreateTime();
		FGS createBy   = model.fieldGetCreateBy();
		for(Object o:model.changedFields()){
			FGS fgs=(FGS)o;
			
			Column c = fgs.getAnnotation(Column.class);
			Object v = getValue(fgs,model);
			
			boolean skip = c.auto() || c.key() || unames.contains(fgs.getFieldName());
			if(!skip){
				skip = fgs.isSameName(createTime) || fgs.isSameName(createBy);
			} 
			
			if(!skip){
				if(i>0){
					updateSql.append(", ");
				}
				updateSql.append(getColumnName(c.name()) + " = ?");
				updateArgs.add(v);
				i++;
			}			
		}
		
		if(i>0){
			query.add(updateSql.toString(), updateArgs);
		}
		
		return query;
	}
	 
	@Override
	public CreateTable getCreateTable(DBConfig db, String tableName) {
		throw new RuntimeException("Not implement!");
	}

	@Override
	public String getIdleValidationQuery() {
		return "SELECT 1 FROM dual";
	}
	
	@Override
	public boolean supportAutoIncrease(){
		return false;
	}
	
	@Override
	public boolean supportSequence() {
		return true;
	};
	
	@Override
	public String getFieldDateValue(String yyyy_MM_dd_HH_mm_ss){
		return "to_date("+yyyy_MM_dd_HH_mm_ss+",'yyyy-mm-dd hh24:mi:ss')";
	}
	
	@Override
	public String getSequenceNext(String seq){
		return "SELECT "+seq+".Nextval FROM dual";
	}
	
	@Override
	public DataSource getMetaDataSource(DBConfig dbcfg){
		Properties props=new Properties();
		props.put("ResultSetMetaDataOptions","1");  
		
		return new SimpleDataSource(dbcfg,props){
			@Override
			protected Connection getRealConnection(String username, String password) throws SQLException {
				OracleConnection conn=(OracleConnection)super.getRealConnection(username, password);
				conn.setRemarksReporting(true);
				return conn;
			}
		};
	}
	
	@Override
	public String getMetaSchemaPattern(DBConfig db) {
		return db.getCfg().getUsername().toUpperCase();
	}
}
