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
import java.util.List;

import javax.sql.DataSource;

import oracle.jdbc.driver.OracleConnection;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
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
		//jdbc:oracle:thin:@//172.16.95.218/qndbzhu
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
	
	@Override
	public Query insert(Model model,boolean updateOnDuplicateKey){
		if(updateOnDuplicateKey){
			List<FGS> pkFields = model.pkFields(); 
			for(FGS fgs:pkFields){
				Object v=fgs.getObject(model);
				if(v==null){
					return super.insert(model, updateOnDuplicateKey);
				}
			}
			
			return insertUsingMerge(model);
		}else{
			return super.insert(model, updateOnDuplicateKey);
		}
	}
	
	protected Query insertUsingMerge(Model model){
		Query query=createQuery();
		
		int i=0;
		
		query.add("MERGE INTO ").add(getTableName(model.table())).add(" m USING dual ON (");
		
		List<FGS> pkFields = model.pkFields(); 
		for(FGS fgs:pkFields){
			Object v=fgs.getObject(model);
			if(i>0){
				query.add(" AND ");
			}
			Column c=fgs.getAnnotation(Column.class);
			query.add(getColumnName(c.name()) + " = ?",v);
			
			i++;
		}
		
		query.add(") \r\n");
		query.add("  WHEN NOT MATCHED THEN INSERT ");
		
		addNameValues(query,model);
		
		query.add("  WHEN MATCHED     THEN UPDATE SET ");
		
		i=0;
		for(Object o:model.changedFields()){
			FGS fgs=(FGS)o;
			
			Column c=fgs.getAnnotation(Column.class);
			Object v=getValue(fgs,model);
			if(!c.auto() && !c.key()){
				if(i>0){
					query.add(", ");
				}
				query.add(getColumnName(c.name()) + " = ?",v);
				
				i++;
			}			
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
	public Connection getMetaConnection(DataSource ds)throws SQLException {
		Connection conn= ds.getConnection();
		
		OracleConnection oraCon=conn.unwrap(OracleConnection.class);
		oraCon.setRemarksReporting(true);
		
		return conn;
	}
	
	@Override
	public String getMetaSchemaPattern(DBConfig db) {
		return db.getCfg().getUsername().toUpperCase();
	}
}
