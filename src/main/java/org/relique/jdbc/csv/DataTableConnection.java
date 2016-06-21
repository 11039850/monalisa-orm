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

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.relique.io.TableReader;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DataTableConnection extends CsvConnection {

	protected DataTableConnection() throws SQLException {
		
		super(new DataTableReader(),new Properties(),"");
		 
	}

	public static class DataTableReader implements TableReader{
	 
		public Reader getReader(Statement statement, String tableName) throws SQLException {
			 
			return new StringReader("a");
		}
	
		 
		public List<String> getTableNames(Connection connection) throws SQLException {
			List<String> tables=new ArrayList<String>();
			
			tables.add("_THIS_TABLE");
			
			return tables;
		}
	}

}
