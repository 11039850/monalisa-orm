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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.model.Record;

import test.com.tsc9526.monalisa.orm.dialect.postgres.PostgresDB;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class InsertTests {

	public void testInsert1() {
		Record r = PostgresDB.DB.createRecord("test","id");
		r.set("id","3");
		r.set("name","xx0");
		r.saveOrUpdate();
		
		String content = r.SELECT().select(2, 0).format();
		Assert.assertTrue(content.length()>1);
		
		Assert.assertTrue(content.indexOf("id")>=0);
		Assert.assertTrue(content.indexOf("name")>=0);
		Assert.assertTrue(content.indexOf("\n")>=0);
	}
}
