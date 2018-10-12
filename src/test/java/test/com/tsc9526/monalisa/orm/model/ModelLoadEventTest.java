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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.model.ModelEvent;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecordV2;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelLoadEventTest {
	 
	public void testLoad1() {
		TestRecordEx ex=new TestRecordEx();
		ex.defaults().setName("modelLoadEventTest");
		
		Assert.assertEquals(ex.save(),1);
	 	
		TestRecordEx x = new TestRecordEx(ex.getRecordId());
		TestRecordV2 y = x.load();
		Assert.assertEquals(y,x);
		
		Assert.assertEquals(x.beforeCallCnt,1);
		Assert.assertEquals(x.afterCallCnt,1);
		
		Assert.assertTrue(x.changedFields().size()>0);
	}
	 
	public void testLoad2() {
		TestRecordEx ex=new TestRecordEx();
		ex.defaults().setName("modelLoadEventTest2");
		Assert.assertEquals(ex.save(),1);
		
		Query q = MysqlDB.DB.createQuery();
		q.add("select * from "+TestRecordV2.M.TABLE+" where record_id=?", ex.getRecordId());
		
		TestRecordEx x = q.getResult(TestRecordEx.class);
		
		Assert.assertNotNull(x);
		
		Assert.assertEquals(x.beforeCallCnt,1);
		Assert.assertEquals(x.afterCallCnt,1);
		Assert.assertTrue(x.changedFields().size()>0);
	}
	
	
	public static class TestRecordEx extends TestRecordV2{
		private static final long serialVersionUID = -2304158257014674386L;
		
		int beforeCallCnt =0;
		int afterCallCnt =0;
		
		public TestRecordEx() {
			super();
		}
		
		public TestRecordEx(Integer recordId){
			super(recordId);
		}
		
		protected void before(ModelEvent event) {
			super.before(event);
			
			beforeCallCnt++;
		}
		
		protected void after(ModelEvent event, int r) {
			super.after(event,r);
			afterCallCnt++;
		}
	}
}
