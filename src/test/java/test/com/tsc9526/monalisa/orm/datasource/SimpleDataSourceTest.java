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
package test.com.tsc9526.monalisa.orm.datasource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.io.MelpClose;

import test.com.tsc9526.monalisa.TestConstants;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class SimpleDataSourceTest {
	
	public void testA() {
		DBConfig db=DBConfig.fromClass(testDataSourceA.class);
		DataTable<DataMap> rs=db.select("SELECT * FROM test_record");
		Assert.assertTrue(rs.size()>=0);
	}
	
	public void testConnections()throws Exception {
		DBConfig db=DBConfig.fromClass(testDataSourceA.class);
		List<Connection> connections = new ArrayList<Connection>();
		for(int i=0;i<10;i++) {
			connections.add( db.getDataSource().getConnection());
		}
		
		try {
			db.getDataSource().getConnection();
			Assert.fail("Connection pool overflow");
		}catch(Exception e) {
			//test OK
		}
		
		for(int i=0;i<5;i++) { 
			connections.remove(0).close();
		}
		
		for(int i=0;i<5;i++) {
			connections.add( db.getDataSource().getConnection());
		}
		 
		MelpClose.close(connections.toArray());
	}
	 
	
	@DB(url=TestConstants.mysqlUrl,username=TestConstants.username,password=TestConstants.password
			,properties={"pool.min=5","pool.max=10","pool.waitTime=1"})
	private class testDataSourceA{}
	  
}
