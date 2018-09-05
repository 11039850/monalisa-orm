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

import org.junit.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.string.MelpDate;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.oracle.oracledb.TestTable1;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test(enabled=TestConstants.ENABLE_TEST_WITH_ORACLE)
public class TimestampTest {

	public void testTimestamp() throws Exception{
		String name ="ts";
		String ts  = "2018-09-05 17:53:10";
		Date updateTime = MelpDate.toDate(ts);
		
		//清理数据
		TestTable1 old = TestTable1.SELECT().selectByNameTitle(name, ts);
		if(old!=null) {
			old.delete();
		}
		
		//增加测试数据
		TestTable1 table1=new TestTable1();
		table1.defaults();
		table1.setUpdateTime(updateTime);
		table1.setTitle(ts);
		table1.setName(name);
		Assert.assertEquals(1,table1.save());
		
		//读取记录进行检查
		Long rid = table1.getId();
		TestTable1 savedRecord = TestTable1.SELECT().selectByPrimaryKey(rid);
		Assert.assertEquals(ts,MelpDate.toTime(savedRecord.getUpdateTime()));
		Assert.assertEquals(ts,savedRecord.getTitle());
		Assert.assertEquals(name,savedRecord.getName());
		
		//Model校验
		TestTable1 x = TestTable1.DB.createQuery().add("SELECT * FROM TEST_TABLE_1 WHERE id=?", rid).getResult(TestTable1.class);
		Assert.assertEquals(ts,MelpDate.toTime(x.getUpdateTime())); 
		Assert.assertEquals(ts,x.getTitle());
		Assert.assertEquals(name,x.getName());
				
		//非Model校验
		TestTable0 y = TestTable1.DB.createQuery().add("SELECT * FROM TEST_TABLE_1 WHERE id=?", rid).getResult(TestTable0.class);
		Assert.assertEquals(ts,MelpDate.toTime(y.getUpdateTime())); 
		Assert.assertEquals(ts,y.getTitle());
		Assert.assertEquals(name,y.getName());
		
		
		table1.delete();
	}
	
	
	public static class TestTable0{
		private Long id;
		private Date updateTime;
		private String name;
		private String title;
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public Date getUpdateTime() {
			return updateTime;
		}
		
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
