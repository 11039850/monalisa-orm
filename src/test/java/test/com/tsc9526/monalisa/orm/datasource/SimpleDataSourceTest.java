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
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.ConfigClass;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.SimpleDataSource;
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
	
	@BeforeClass
	public void beforeClass() {
		Properties p=new Properties();
		p.put("DB.cfg.url",        TestConstants.mysqlUrl);
		p.put("DB.cfg.username",   TestConstants.username);
		p.put("DB.cfg.password",   TestConstants.password);
		  
		p.put("DB.cfg.pool.min",   1);
		p.put("DB.cfg.pool.max",   6);
		 
		SimpleConfigC.cfgProperties = p;
	}
	
	public void testA() {
		DBConfig db=DBConfig.fromClass(testDataSourceA.class);
		DataTable<DataMap> rs=db.select("SELECT * FROM test_record");
		Assert.assertTrue(rs.size()>=0);
	}
	
	public void testConnectionsA()throws Exception {
		DBConfig db=DBConfig.fromClass(testDataSourceA.class);
		
		int poolMaxSize = ((SimpleDataSource)db.getDataSource()).getMaxSize();
		Assert.assertEquals(poolMaxSize,10);
		
		testConnections(db);
	}
	 
	public void testConnectionsB()throws Exception {
		DBConfig db=testDataSourceB.DB;
		
		SimpleDataSource ds = ((SimpleDataSource)db.getDataSource());
		Assert.assertNotNull(ds.getUrl());
		Assert.assertEquals(ds.getMaxSize(),5);
		 
		testConnections(db);
	}
	
	
	public void testConnectionsC()throws Exception {
		DBConfig db=testDataSourceC.DB;
		
		SimpleDataSource ds = ((SimpleDataSource)db.getDataSource());
		Assert.assertNotNull(ds.getUrl());
		Assert.assertEquals(ds.getMaxSize(),6);
		 
		testConnections(db);
	}
	 
	
	private void testConnections(DBConfig db)throws Exception {
		int poolMaxSize = ((SimpleDataSource)db.getDataSource()).getMaxSize();
		
		List<Connection> connections = new ArrayList<Connection>();
		for(int i=0;i<poolMaxSize;i++) {
			connections.add( db.getDataSource().getConnection());
		}
		
		try {
			db.getDataSource().getConnection();
			Assert.fail("Connection pool overflow");
		}catch(Exception e) {
			//test OK
		}
		
		int freeSize = poolMaxSize/2;
		
		for(int i=0;i<freeSize;i++) { 
			connections.remove(0).close();
		}
		
		for(int i=0;i<freeSize;i++) {
			connections.add( db.getDataSource().getConnection());
		}
		 
		MelpClose.close(connections.toArray());
	}
	
	
	@DB(url=TestConstants.mysqlUrl,username=TestConstants.username,password=TestConstants.password
			,properties={"pool.min=5","pool.max=10","pool.waitTime=1"})
	private class testDataSourceA{}
	
	
	@DB(configClass=SimpleConfigB.class,properties={"pool.min=5","pool.max=10","pool.waitTime=1"})
	public static interface testDataSourceB{
		public static DBConfig DB = DBConfig.fromClass(testDataSourceB.class);
		
	}
	
	public static class SimpleConfigB extends ConfigClass{
		public Properties getConfigProperties() {
			Properties p=new Properties();
			 
			p.put(getKey("url"),        TestConstants.mysqlUrl);
			p.put(getKey("username"),   TestConstants.username);
			p.put(getKey("password"),   TestConstants.password);
			  
			p.put(getPoolKey("min"),   1);
			p.put(getPoolKey("max"),   5);
			return p;
		}
	}
	
	
	@DB(configClass=SimpleConfigC.class,properties={"pool.min=5","pool.max=10","pool.waitTime=1"})
	public static interface testDataSourceC{
		static final DBConfig DB = DBConfig.fromClass(testDataSourceC.class);
		 
	}
	
	public static class SimpleConfigC extends ConfigClass{
		static Properties cfgProperties;
		public Properties getConfigProperties() {
			return cfgProperties;
		}
	} 
	
	
}


