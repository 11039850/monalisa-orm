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
package org.relique.jdbc.csv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.relique.io.DataReader;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataTableResultSet extends CsvResultSet{
	 
	public DataTableResultSet(
			DataReader reader,
			String tableName,
			List<Object []> queryEnvironment,
			boolean isDistinct,
			int resultSetType,
			LogicalExpression whereClause,
			List<Expression> groupByColumns,
			LogicalExpression havingClause,
			List<Object []> orderByColumns,
			int sqlLimit,
			int sqlOffset) throws ClassNotFoundException, SQLException
	{
		 
	 	super(createCsvStatement(), reader, tableName, queryEnvironment
	 			, isDistinct, resultSetType, whereClause
	 			, groupByColumns, havingClause
	 			, orderByColumns, sqlLimit, sqlOffset
	 			, getColumnTypes(), getSkipLeadingLines(),new HashMap<String, Object>()); 
	}
	
	private static CsvStatement createCsvStatement() throws SQLException{
		return new CsvStatement(new DataTableConnection(),ResultSet.TYPE_SCROLL_INSENSITIVE);
	}
	
	private static String getColumnTypes(){
		return null;
	}
	
	private static int getSkipLeadingLines(){
		return 0;
	}
 
}

