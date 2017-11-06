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

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.oracle.oracledb.TestTable1;

import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.string.MelpString;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test(enabled=TestConstants.ENABLE_TEST_WITH_ORACLE)
public class TestTable1Test {
	static Logger logger=Logger.getLogger(TestTable1Test.class);
	
	private final static int MultiThreadInvocationCount   = 100;
	private final static int MultiThreadInvocationThreads = 10;
	
	private final static int SingleThreadInvocationCount  = 50;
	
	private AtomicLong totalInserts=new AtomicLong();
	 
	@BeforeClass
	public void beforeTest(){
		int c=TestTable1.DELETE().deleteAll();
		logger.info("Delete("+c+") table: "+TestTable1.class.getSimpleName());
		
		long r=TestTable1.SELECT().count();
		Assert.assertEquals(0, r);
	}
 
	@Test(invocationCount = SingleThreadInvocationCount)
	public void testInsert(){
		long tx = totalInserts.addAndGet(1);
		TestTable1 table1=new TestTable1();
		table1.defaults();
		table1.setName("jjyy-"+MelpString.leftPadding(String.valueOf(tx),'0',10));
		table1.setTitle("single-thread-count-"+SingleThreadInvocationCount);
		table1.setUpdateBy(String.valueOf(tx));
		int r=table1.save();
		
		Assert.assertEquals(1,r);
		Assert.assertTrue(table1.getId().longValue()>0);

		TestTable1 t1=TestTable1.SELECT().selectById(table1.getId());
		Assert.assertEquals(table1.getTitle(),t1.getTitle());
		Assert.assertNotNull(t1.getCreateTime());
	}
	
	@AfterClass
	public void afterClass(){
		long c=TestTable1.SELECT().count();
		
		long expect = MultiThreadInvocationCount + SingleThreadInvocationCount;
		Assert.assertEquals(expect, c);
		 
	}
	
	@Test(dependsOnMethods={"testInsert","testMultiInserts","testSelect"})
	public void testUpdate(){
		DataTable<TestTable1> tx=TestTable1.WHERE().name.like("jjyy%").SELECT().select(10,0);
		
		TestTable1 t1=tx.get(0);
		Assert.assertNull(t1.getUpdateTime());
	 	t1.setTitle("update-ok");
		int r=t1.saveOrUpdate();
		
		Assert.assertEquals(1,r);
		Assert.assertNotNull(t1.getUpdateTime());
		
		TestTable1 t2=TestTable1.SELECT().selectById(t1.getId());
		Assert.assertNotNull(t2.getUpdateTime());
		Assert.assertEquals(t1.getCreateTime(),t2.getCreateTime());
		Assert.assertEquals(t1.getTitle(),t2.getTitle());
	 
	}
	
	@Test(dependsOnMethods="testSaveOrUpdate")
	public void testDeleteByUniqueKey(){
		TestTable1 z=new TestTable1();
		z.setName("tsc9526");
		z.setTitle("REMOVE");
		z.setTsA(new Date());
		
		int r=z.delete();
		Assert.assertEquals(1,r);
		
	}
	
	@Test(dependsOnMethods="testSaveOrUpdate")
	public void testDeleteByPk(){
		TestTable1 z=new TestTable1();
		z.setName("tsc9526-1");
		z.setTitle("REMOVE-1");
		z.setTsA(new Date());
		
		int r=z.delete();
		Assert.assertEquals(0,r);
		
		r=z.saveOrUpdate();
		Assert.assertEquals(1,r);
		
		r=new TestTable1(z.getId()).delete();
		Assert.assertEquals(1,r);
	}
	
	public void testSaveOrUpdate(){
		TestTable1 x=new TestTable1();
		x.defaults().setName("tsc9526");
		x.setTitle("REMOVE");
		x.setTsA(new Date());
		
		x.saveOrUpdate();
		Assert.assertNotNull(x.getId());
		
		Date tsa=new Date();
		TestTable1 y=TestTable1.SELECT().selectById(x.getId());
		Assert.assertEquals(x.getName(),y.getName());
		y.setTsA(tsa);
		int r=y.saveOrUpdate();
		Assert.assertEquals(1,r);
		
		TestTable1 z=new TestTable1();
		z.setName("tsc9526");
		z.setTitle("REMOVE");
		z.setUpdateBy("xyz");
		r=z.saveOrUpdate();
		Assert.assertEquals(1,r);
		
		TestTable1 m=TestTable1.SELECT().selectById(x.getId());
		Assert.assertEquals("xyz",m.getUpdateBy());
		Assert.assertEquals(x.getId(),m.getId());
	}
	
	@Test(dependsOnMethods={"testInsert","testMultiInserts"})
	public void testSelect(){
		long c=TestTable1.WHERE().name.like("jjyy%").SELECT().count();
		Assert.assertEquals(SingleThreadInvocationCount, c);
		 
		DataTable<TestTable1> ts=TestTable1.WHERE().name.like("jjyy%").title.asc().SELECT().select();
		Assert.assertEquals(SingleThreadInvocationCount, ts.size());
		
		TestTable1 t1=TestTable1.WHERE().name.like("jjyy%").name.asc().SELECT().selectOne();
		int x=Integer.parseInt( t1.getUpdateBy() );
		
		int pageSize  = 10;
		int pageCount = SingleThreadInvocationCount / pageSize;
		for(int k=0;k<pageCount;k++){
			DataTable<TestTable1> tx=TestTable1.WHERE().name.like("jjyy%").name.asc().SELECT().select(pageSize,k*pageSize);
			for(int i=0;i<pageSize;i++){
				Assert.assertEquals(x++, Integer.parseInt( tx.get(i).getUpdateBy() )); 
			}
		}
		
		x=Integer.parseInt( t1.getUpdateBy() );
		for(int k=0;k<pageCount;k++){
			Page<TestTable1> tx=TestTable1.WHERE().name.like("jjyy%").name.asc().SELECT().selectPage(pageSize,k*pageSize);
			Assert.assertEquals(SingleThreadInvocationCount,tx.getRecords());
			for(int i=0;i<pageSize;i++){
				Assert.assertEquals(x++, Integer.parseInt( tx.getRows().get(i).getUpdateBy() )); 
			}
		}
		
	}
	
	@Test(threadPoolSize = MultiThreadInvocationThreads, invocationCount = MultiThreadInvocationCount)
	public void testMultiInserts(){
		TestTable1 table1=new TestTable1();
		table1.defaults();
		table1.setName("MultiThreadInvocationCount: "+MultiThreadInvocationCount);
		table1.setTitle("Title-"+totalInserts.addAndGet(1));
		table1.save();
		
		Assert.assertTrue(table1.getId().longValue()>0);
	}
}
