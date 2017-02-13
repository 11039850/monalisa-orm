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
package com.tsc9526.monalisa.tools.csv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.relique.io.DataReader;
import org.relique.jdbc.csv.CsvResultSet;
import org.relique.jdbc.csv.CsvStatement;
import org.relique.jdbc.csv.SqlParser;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class DataTableResultSet extends CsvResultSet{
	 
	public DataTableResultSet(
			DataReader reader,
			String tableName,
			int resultSetType,
			SqlParser parser) throws ClassNotFoundException, SQLException
	{
		super(createCsvStatement(), reader, tableName, parser.getColumns(),
				parser.isDistinct(),
				resultSetType,
				parser.getWhereClause(),
				parser.getGroupByColumns(),
				parser.getHavingClause(),
				parser.getOrderByColumns(),
				parser.getLimit(),
				parser.getOffset()
	 			, getColumnTypes(), getSkipLeadingLines(),new HashMap<String, Object>()); 
	}
	
	private static CsvStatement createCsvStatement() throws SQLException{
		return new TableStatement(new DataTableConnection(),ResultSet.TYPE_SCROLL_INSENSITIVE);
	}
	
	private static String getColumnTypes(){
		return null;
	}
	
	private static int getSkipLeadingLines(){
		return 0;
	}
}