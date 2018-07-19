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

import com.tsc9526.monalisa.tools.string.MelpString;

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecord;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelTest {
	public void testCut() {
		TestRecord record =  new TestRecord();
		record.setName(MelpString.repeat("1", 128));
		Assert.assertEquals(record.getName().length(),128);
	 
		
		record.setName(MelpString.repeat("1", 129));
		record.cut();
		Assert.assertEquals(record.getName().length(),128);
		Assert.assertTrue(record.getName().endsWith(" ..."));
	}
}
