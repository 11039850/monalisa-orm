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
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class CacheTest {
	
	public void testSelect() {
		TestTable1.WHERE().name.like("mm-test-cache-%").delete();
		
		for(int i=0;i<5;i++) {
			TestTable1 x = new TestTable1().defaults();
			x.setName("mm-test-cache-"+i);
			x.save();
		}
		
		DataTable<TestTable1> rs=findCacheMMTestCache(10,0);
		Assert.assertEquals(rs.size(),5);
		
		Assert.assertTrue(findCacheMMTestCache(10,0) == rs);
		
		Assert.assertTrue(findCacheMMTestCache(5,0) != rs);
	}
	
	private DataTable<TestTable1> findCacheMMTestCache(int limit,int offset){
		DataTable<TestTable1> rs=TestTable1.WHERE().name.like("mm-test-cache-%").SELECT().setCacheTime(10,0).select(limit,offset);
		return rs;
	}
	
	public void testLRU(){
		Query q = MysqlDB.DB.createQuery().setCacheTime(10);
		q.add("SELECT * FROM test_table_1");
		
		DataTable<DataMap> rs=q.getList();
		Assert.assertTrue(rs==q.getList());
		
		DataTable<DataMap> rs0=null;
		for(int i=0;i<1030;i++){
			q.clear();
			q.add("SELECT * FROM test_table_1 a"+i);
			if(i==0){
				rs0=q.getList();
			}else{
				q.getList();
			}
			
			Query qx=MysqlDB.DB.createQuery().setCacheTime(10);
			qx.add("SELECT * FROM test_table_1");
			Assert.assertTrue(rs==qx.getList(),"loop "+i);
		}
		
		Assert.assertTrue(rs!=MysqlDB.DB.select("SELECT * FROM test_table_1"));
		Assert.assertTrue(rs!=MysqlDB.DB.select(1,1,"SELECT * FROM test_table_1"));
		
		Assert.assertTrue(rs==q.clear().add("SELECT * FROM test_table_1").getList());
		
		q.clear();
		q.add("SELECT * FROM test_table_1 a0");
		Assert.assertTrue(rs0!=q.getList());
	}
	
	public void testCacheDefaultMax1024() {
		Query q=MysqlDB.DB.createQuery().setCacheTime(100);
		q.add("SELECT * FROM test_table_1");
		q.getAllResults();
	}
}
