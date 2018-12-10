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
package test.com.tsc9526.monalisa.orm.dialect.mysql.cases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.data.ColumnData;
import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestLogyyyymm;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable2;

import com.tsc9526.monalisa.orm.Tx;
import com.tsc9526.monalisa.tools.string.MelpDate;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelTest {
	public void testInsertDate() throws Exception{
		TestTable1 table1=new TestTable1();
		table1.setName("xyz"+System.currentTimeMillis());
		table1.defaults();
		table1.save();
		
		TestTable1 x=TestTable1.SELECT().selectByPrimaryKey( table1.getId() );
		Date createTime=x.getCreateTime();
		
		String time=MelpDate.toString(createTime,"HH:mm:ss");
		Assert.assertNotEquals(time, "00:00:00");
	}
	
	public void testUpdateBy() {
		int r = TestTable1.WHERE()
			.name.eq("mm-test-update-by-01")
			.OR()
			.name.eq("mm-test-update-by-02")
			.delete();
		Assert.assertTrue(r<=2);
		
		TestTable1 t1 = new TestTable1().defaults().setName("mm-test-update-by-01").setTitle("x1");
		int r1 = t1.save();
		Assert.assertEquals(r1,1);
		
		TestTable1 t2 = new TestTable1().defaults().setName("mm-test-update-by-02").setTitle("x2");
		int r2 = t2.save();
		Assert.assertEquals(r2,1);
		
		Assert.assertEquals(t1.setTitle("y1").update(),1);
		Assert.assertEquals(t2.setTitle("y2").update(),1);
		 
		Assert.assertEquals("y1",TestTable1.SELECT().selectByPrimaryKey(t1.getId()).getTitle());
		Assert.assertEquals("y2",TestTable1.SELECT().selectByPrimaryKey(t2.getId()).getTitle());
		
		Assert.assertEquals(t1.setTitle("z1").updateBy("title=?","x1"),0);
		Assert.assertEquals(t1.setTitle("z1").updateBy("title=?","y1"),1);
		Assert.assertEquals(t2.setTitle("z1").updateBy("title=?","y1"),0);
		Assert.assertEquals(t2.setTitle("z1").updateBy("title=?","y2"),1);
		
		Assert.assertEquals("z1",TestTable1.SELECT().selectByPrimaryKey(t1.getId()).getTitle());
		Assert.assertEquals("z1",TestTable1.SELECT().selectByPrimaryKey(t2.getId()).getTitle());
		
		Assert.assertEquals(t1.setTitle("zx1").updateBy("title=?","y1"),0);
		Assert.assertEquals(t1.setTitle("zx1").updateBy("title=?","z1"),1);
		Assert.assertEquals("zx1",TestTable1.SELECT().selectByPrimaryKey(t1.getId()).getTitle());
		
	}
	
	public void testPartition()throws Exception{
		TestLogyyyymm log=new TestLogyyyymm();
		log.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-03-01 01:01:01"));
		log.save();
		
		TestTable1.DELETE().deleteAll();
		new TestTable1().defaults().setName("keep-not-empty").save();
		int maxId=MysqlDB.DB.selectOne("select max(id) as x from test_table_1").getInt("x",0); 
		
		Tx.putContext(Tx.CONTEXT_CURRENT_USERID,"zzg.zhou");
		TestTable1 t1=new TestTable1();
		t1.defaults();	
		t1.save();
		
		Assert.assertEquals(t1.getId().intValue(), maxId+1);
		Assert.assertEquals(t1.getCreateBy(),"zzg.zhou");
		
		t1=new TestTable1(maxId+1);
		Assert.assertTrue(!t1.entity());
		t1=new TestTable1(maxId+1).load();
		Assert.assertNotNull(t1.load());
		Assert.assertTrue(t1.entity());
		
		TestTable1 t2=new TestTable1(maxId+2);
		Assert.assertTrue(!t2.entity());
		Assert.assertNull(t2.load());
		Assert.assertTrue(!t2.entity());
		
		Map<Integer,TestTable1> ms=TestTable1.WHERE().id.ge(0).SELECT().selectToMap();
		Assert.assertTrue(ms.size()>0);
		
	}
	
	
	public void testInsertArrays()throws Exception{
		int[] i1=new int[]{3,2,1};
		String[] s1=new String[]{"3","2"};
		
		TestTable2 t2=new TestTable2();
		t2.defaults();
		t2.setObj(new ColumnData());
		t2.setArrayInt(i1);
		t2.setArrayString(s1);
		t2.save();
		
		TestTable2 t2x=TestTable2.SELECT().selectByPrimaryKey(t2.getId());
		Assert.assertEquals(t2x.getObj(),t2.getObj());
		
		for(int i=0;i<i1.length;i++){
			Assert.assertEquals(t2x.getArrayInt()[i], i1[i]);
		}
		for(int i=0;i<s1.length;i++){
			Assert.assertEquals(t2x.getArrayString()[i], s1[i]);
		}
	}
}
