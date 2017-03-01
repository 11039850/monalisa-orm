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

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.TestConstants;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.ConfigClass;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DruidDataSourceTest {

	public void testA() {
		DBConfig db=DBConfig.fromClass(testDruidA.class);
		DataTable<DataMap> rs=db.select("SELECT * FROM test_record");
		Assert.assertTrue(rs.size()>=0);
	}
	
	public void testB() {
		DBConfig db=DBConfig.fromClass(testDruidB.class);
		DataTable<DataMap> rs=db.select("SELECT * FROM test_record");
		Assert.assertTrue(rs.size()>=0);
	}
	
	public void testC() {
		DBConfig db=DBConfig.fromClass(testDruidC.class);
		DataTable<DataMap> rs=db.select("SELECT * FROM test_record");
		Assert.assertTrue(rs.size()>=0);
	}
	
	
	@DB(url=TestConstants.url,username=TestConstants.username,password=TestConstants.password,datasourceClass="DruidDataSource")
	private class testDruidA{}
	
	@DB(url=TestConstants.url,username=TestConstants.username,password=TestConstants.password,datasourceClass="com.tsc9526.monalisa.orm.datasource.DruidDataSource")
	private class testDruidB{}
	
	@DB(configClass=InnerConfigClass.class)
	private class testDruidC{}
	
	public static class InnerConfigClass extends ConfigClass{
		public Properties getConfigProperties() {
			Properties p=new Properties();
			 
			p.put(getKey("url"),        TestConstants.url);
			p.put(getKey("username"),   TestConstants.username);
			p.put(getKey("password"),   TestConstants.password);
			
			p.put(getKey("datasourceClass"), "DruidDataSource");
			p.put(getPoolKey("testWhileIdle"),true);
			p.put(getPoolKey("validationQuery"),"SELECT 1");
			
			return p;
		}
	}
}
