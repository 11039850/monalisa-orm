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

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class CacheTest {
	int testRecordSize = 10;
	
	@BeforeClass
	public void beforeClass() {
		TestTable1.WHERE().name.like("mm-test-cache-%").delete();
		
		for(int i=0;i<testRecordSize;i++) {
			TestTable1 x = new TestTable1().defaults();
			x.setName("mm-test-cache-"+i);
			x.setTitle("t"+i);
			x.save();
		}
	}
	 
	public void testPage() {
		Page<TestTable1> rs1 = getPage(2,0);
		Page<TestTable1> rs2 = getPage(2,0);
		
		Assert.assertEquals(rs1.getPage()  , 1);
		Assert.assertEquals(rs1.getTotal() , 5);
		Assert.assertEquals(rs1.getRecords()  , testRecordSize);
		Assert.assertEquals(rs1.getPage()  , rs2.getPage());
		Assert.assertEquals(rs1.getTotal() , rs2.getTotal());
		Assert.assertTrue(rs1.getRows()  == rs2.getRows() );
	}
	
	public void testSelect() {
		DataTable<TestTable1> rs=getList(10,0);
		Assert.assertEquals(rs.size(),testRecordSize);
		
		Assert.assertTrue(getList(10,0) == rs);
		
		Assert.assertTrue(getList(5,0) != rs);
	}
	
	private DataTable<TestTable1> getList(int limit,int offset){
		DataTable<TestTable1> rs=TestTable1.WHERE()
				.name.like("mm-test-cache-%")
				.SELECT()
				.setCacheTime(10,0)
				.select(limit,offset);
		return rs;
	}
	
	private Page<TestTable1> getPage(int limit,int offset){
		Page<TestTable1> rs=TestTable1.WHERE()
				.name.like("mm-test-cache-%")
				.SELECT()
				.setCacheTime(10,0)
				.selectPage(limit,offset);
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
