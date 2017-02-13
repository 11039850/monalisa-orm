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
package test.com.tsc9526.monalisa.orm.query;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassAssist;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelEnumTest {
  
  public void testParseRemark()throws Exception {
	  TestSimpleModel model=new TestSimpleModel();
	  ClassAssist mc= MelpClass.getMetaClass(model.getClass());
	  mc.getField("status").setObject(model, StatusA.ERROR);
	  
	  Assert.assertEquals(model.getStatus(),StatusA.ERROR);
	  
	  mc.getField("status").setObject(model, "OK");
	  Assert.assertEquals(model.getStatus(),StatusA.OK);
	  
	  mc.getField("status").setObject(model, 2);
	  Assert.assertEquals(model.getStatus(),StatusA.NOT_FOUND);
	  
	  
	  mc.getField("statusB").setObject(model, 1);
	  Assert.assertEquals(model.getStatusB(),StatusB.B1);
	  
	  mc.getField("statusB").setObject(model, 2);
	  Assert.assertEquals(model.getStatusB(),StatusB.B2);
	  
	  mc.getField("statusB").setObject(model, "B3");
	  Assert.assertEquals(model.getStatusB(),StatusB.B3);
	  
  }
}
