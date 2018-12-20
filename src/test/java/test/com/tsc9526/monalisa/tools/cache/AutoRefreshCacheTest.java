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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.executor.CacheableExecute;
import com.tsc9526.monalisa.orm.executor.Execute;
import com.tsc9526.monalisa.tools.cache.CacheKey;
import com.tsc9526.monalisa.tools.cache.CacheManager;
import com.tsc9526.monalisa.tools.misc.MelpMisc;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class AutoRefreshCacheTest {
	CacheManager cm = CacheManager.getInstance();
	
	int testRecordSize = 3;
	
	@BeforeMethod
	public void beforeMethod() {
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
		
		//update title, not update cache
		TestTable1.SELECT().selectByPrimaryKey(t1.getId()).setTitle("tx").update();
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"t0");
		
		MelpMisc.sleep(200);
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) == t1);
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"t0");
		
		MelpMisc.sleep(1000);
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) != t1);
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"tx"); //expect: changed
	}
	

	private TestTable1 getTestTable1AutoRefresh(String name) {
		TestTable1 t1= TestTable1.WHERE()
				.name.eq(name)
				.SELECT().setCacheTime(5000, 1000).selectOne();
		return t1;
	}
	
	
	
	
	public void testAutoRefreshRemove1() {
		TestTable1 t1= getTestTable1AutoRefreshRemove1("auto-test-cache-"+0);
		Assert.assertEquals(t1.getTitle(),"t0");
		Assert.assertTrue(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0) == t1);
		
		//update title, not update cache
		TestTable1.SELECT().selectByPrimaryKey(t1.getId()).setTitle("tx").update();
		Assert.assertEquals(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0).getTitle(),"t0");
		
		MelpMisc.sleep(200);
		Assert.assertTrue(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0) == t1);
		Assert.assertEquals(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0).getTitle(),"t0");
		
		//remove auto refresh
		CacheKey cacheKey = cm.getCacheKeyByTag("test-remove-1");
		Assert.assertTrue ( cm.removeAutoRefreshCache(cacheKey) );
		Assert.assertFalse( cm.removeAutoRefreshCache(cacheKey) );
		
		MelpMisc.sleep(1000);
		Assert.assertTrue(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0) == t1);
		Assert.assertEquals(getTestTable1AutoRefreshRemove1("auto-test-cache-"+0).getTitle(),"t0"); //expect: not changed
		
		//cache expired
		MelpMisc.sleep(2000);
		Assert.assertTrue(getTestTable1AutoRefresh("auto-test-cache-"+0) != t1);
		Assert.assertEquals(getTestTable1AutoRefresh("auto-test-cache-"+0).getTitle(),"tx"); //expect: changed
	}
	

	private TestTable1 getTestTable1AutoRefreshRemove1(String name) {
		TestTable1 t1= TestTable1.WHERE()
				.name.eq(name).id.asc()
				.SELECT()
				.setCacheTime(2500, 1000).setCacheTag("test-remove-1")
				.selectOne();
		return t1;
	}
	
	
	public void testCacheExecuteLong() {
		Query q = MysqlDB.DB.createQuery();
		
		q.setCacheTime(500,50);
		 
		CacheObject<Integer> value = q.execute(getLongExecute());
		Assert.assertEquals(value, new CacheObject<Integer>(9526));
		cachedLongValue = new CacheObject<Integer>(9527);
		
		long t1 = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			q.execute(getLongExecute());
		}
		long delta = System.currentTimeMillis() - t1;
		Assert.assertTrue(delta < 100,"Too long: "+delta);
		
		MelpMisc.sleep(1600);
		Assert.assertEquals(q.execute(getLongExecute()).getValue(), new Integer(9527));
	}
	
	private CacheObject<Integer> cachedLongValue = new CacheObject<Integer>(9526);
	
	private Execute<CacheObject<Integer>> getLongExecute(){
		return new CacheableExecute<CacheObject<Integer>>() {
			@Override
			public CacheObject<Integer> execute(Connection conn, String sql, List<?> parameters) throws SQLException {
				MelpMisc.sleep(1000);
				return cachedLongValue;
			}

			@Override
			public String getCacheExtraTag() {
				return "test-auto-refresh";
			}
		};
	}
}
