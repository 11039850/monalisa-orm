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
package com.tsc9526.monalisa.core.query.executor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.tsc9526.monalisa.core.query.cache.CacheTableRow;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class RelationExecutor {
	protected Set<CacheTableRow> relationTables=new HashSet<CacheTableRow>();
	 
	protected ResultSet setupRelationTables(ResultSet rs)throws SQLException{
		ResultSetMetaData rsmd= rs.getMetaData();
		for(int i=1;i<=rsmd.getColumnCount();i++){
			String catalog=rsmd.getCatalogName(i);
			String schema=rsmd.getSchemaName(i);
			String table =rsmd.getTableName(i);
			
			relationTables.add(new CacheTableRow(catalog,schema,table));
		}
		
		return rs;
	}
	
	public Set<CacheTableRow> getRelationTables(){
		return relationTables;
	}
}
