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
package test.com.tsc9526.monalisa.orm.dialect.postgres.cases;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.postgres.postgresdb.TestRecord;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test(enabled=TestConstants.ENABLE_TEST_WITH_POSTGRES)
public class TestRecordTest {
 
	public void testSave() {
		TestRecord r = new TestRecord();
		
		r.setTsA(new Date());
		
		Assert.assertEquals(r.defaults().save(),1);
		Assert.assertTrue(r.getRecordId()>0);
		
		TestRecord x = TestRecord.SELECT().selectByRecordId(r.getRecordId());
		Assert.assertEquals(r.getCreateTime().getTime(), x.getCreateTime().getTime());
		Assert.assertEquals(r.getTsA().getTime()       , x.getTsA().getTime());
	}
}
