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
package test.com.tsc9526.monalisa.orm.dialect.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpString;
 

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class GetTablesTest {
 	  
	public void testMysqlGetTables()throws Exception{
		Connection conn = MysqlDB.DB.getDataSource().getConnection();
		try{
			DatabaseMetaData dbm = conn.getMetaData();
			
			ResultSet rs = dbm.getTables(null, null, "%", new String[]{"TABLE"});
			
			System.out.println( MelpString.toString(rs) );
			
			MelpClose.close(rs);
		}finally{
			MelpClose.close(conn);
		}
	}
}
