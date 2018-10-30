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

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.model.Model;

/**
 * jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;user=MyUserName;password=*****;
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public class SQLServerDialect extends Dialect {

	public String getUrlPrefix() {
		return "jdbc:sqlserver://";
	}

	public String getDriver() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}
	
	public String getIdleValidationQuery(){
    	return "SELECT 1";
    }

	public String geCatalog(String jdbcUrl) {
		String catelog = null;

		//jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;user=MyUserName;password=*****;
		String prefix = getUrlPrefix();
		if (jdbcUrl.startsWith(prefix)) {
			for(String vs: jdbcUrl.split(";")){
				int p=vs.indexOf('=');
				if(p>0){
					String name =vs.substring(0,p).trim();
					String value=vs.substring(p+1).trim();
					
					if("databaseName".equalsIgnoreCase(name)){
						catelog=value;
						break;
					}
				}
			}
		}

		return catelog;
	}
	
	public String getSchema(String jdbcUrl) {
		return null;
	}

	public String getColumnName(String name) {
		if (name.startsWith("[")) {
			return name;
		} else {
			return "[" + name + "]";
		}
	}

	public String getTableName(String name) {
		if (name.startsWith("[")) {
			return name;
		} else {
			return "[" + name + "]";
		}
	}
 
	public Query getLimitQuery(Query origin, int limit, int offset) {
		throw new RuntimeException("Not implement!");
	}

	@Override
	public String getCountSql(String sql) {
		throw new RuntimeException("Not implement!");
	}
	
	@Override
	public String getLimitSql(String orignSql, int limit, int offset) {
		throw new RuntimeException("Not implement!");
	}
	
	@Override
	public boolean tableExist(DBConfig db,String name,boolean incudeView){
		throw new RuntimeException("Not implement!");
	}
	
	public CreateTable getCreateTable(DBConfig db, String tableName) {
		throw new RuntimeException("Not implement!");
	}

	@Override
	public synchronized void createTable(DBConfig db, CreateTable table) {
		throw new RuntimeException("Not implement!");
	}

	
	@Override
	public Query insertOrUpdate(Model model) {
		throw new RuntimeException("Not implement!");
	}

	
}
