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
package test.com.tsc9526.monalisa.orm.model;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.ConfigClass;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.model.Record;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecordV2;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelClearChangeTest {
	
	@BeforeClass
	public void beforeClass() {
		Properties p=new Properties();
		p.put("DB.cfg.url",        TestConstants.mysqlUrl);
		p.put("DB.cfg.username",   TestConstants.username);
		p.put("DB.cfg.password",   TestConstants.password);
		
		p.put("DB.cfg.event.load.clear_changes", true);
		
		p.put("DB.cfg.pool.min",   1);
		p.put("DB.cfg.pool.max",   6);
		 
		SimpleConfigC.cfgProperties = p;
	}
	
	public void testClearChanges() {
		TestRecordV2 ex=new TestRecordV2();
		ex.defaults().setName("modelClearChangesTest");
		ex.save();
		
		Record x = testDataSourceC.DB.createRecord(TestRecordV2.M.TABLE);
		x.set(TestRecordV2.M.recordId,ex.getRecordId());
	 	x.load();
		Assert.assertEquals(x.changedFields().size(),0);
		
		
		TestRecordV2 y = new TestRecordV2(ex.getRecordId());
	 	y.load();
		Assert.assertTrue(y.changedFields().size()>0);
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


