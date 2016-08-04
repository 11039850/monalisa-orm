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

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class SQLDialect extends Dialect{

	@Override
	public String getUrlPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDriver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchema(String jdbcUrl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query getLimitQuery(Query origin, int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateTable getCreateTable(DBConfig db, String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdleValidationQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
