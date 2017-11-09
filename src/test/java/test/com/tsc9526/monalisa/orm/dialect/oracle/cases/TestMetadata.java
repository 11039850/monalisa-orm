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
package test.com.tsc9526.monalisa.orm.dialect.oracle.cases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;

import junit.framework.Assert;

import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.oracle.OracleDB;

import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test(enabled = TestConstants.ENABLE_TEST_WITH_ORACLE)
public class TestMetadata {
	static Logger logger=Logger.getLogger(TestMetadata.class);
	
	public void testGetTableName() throws Exception {
		Class.forName(OracleDB.DB.getCfg().getDriver());

		Properties props = new Properties();
		props.put("user",     TestConstants.username);
		props.put("password", TestConstants.password);
		props.put("ResultSetMetaDataOptions", "1");
		Connection conn = DriverManager.getConnection(TestConstants.oracleUrl, props);

		PreparedStatement pst = conn.prepareStatement("SELECT ID, NAME FROM test_table_1");
		ResultSet rs = pst.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			//Assert.assertEquals("test_table_1", rsmd.getTableName(1).toLowerCase());
		}
		
		logger.info("ResultSet:\r\n"+MelpString.toString(rs));

		MelpClose.close(rs, pst, conn);
	}
}
