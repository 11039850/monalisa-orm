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
package test.com.tsc9526.monalisa.tools.cache;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.misc.MelpMisc;

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class AutoRefreshCacheTest {
	int testRecordSize = 3;
	
	@BeforeClass
	public void beforeClass() {
		TestTable1.WHERE().name.like("auto-test-cache-%").delete();
		
		for(int i=0;i<testRecordSize;i++) {
			TestTable1 x = new TestTable1().defaults();
			x.setName("auto-test-cache-"+i);
			x.setTitle("t"+i);
			x.save();
		}
	}
	
	public void testAutoRefresh() {
		TestTable1 t1= getTestTable1AutoRefresh("auto-test-cache-"+0);
		Assert.assertEquals(t1.getTitle(),"t0");
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) == t1);
		
		//update title
		TestTable1.SELECT().selectByPrimaryKey(t1.getId()).setTitle("tx").update();
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"t0");
		
		MelpMisc.sleep(200);
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) == t1);
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"t0");
		
		MelpMisc.sleep(1000);
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) != t1);
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"tx");
	}
	

	private TestTable1 getTestTable1AutoRefresh(String name) {
		TestTable1 t1= TestTable1.WHERE()
				.name.eq(name)
				.SELECT().setCacheTime(5, 1).selectOne();
		return t1;
	}
	
}
