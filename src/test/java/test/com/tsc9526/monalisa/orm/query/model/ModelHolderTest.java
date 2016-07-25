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
package test.com.tsc9526.monalisa.orm.query.model;
 
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.query.TestSimpleModel;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelHolderTest {
	public void testNoInclude()throws Exception{
		TestSimpleModel model=new TestSimpleModel();
	 	model.setIntField1(1);
		model.setIntField2(2);
		
		Query query=model.dialect().insert(model,false);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field1`, `int_field2`)VALUES(1, 2)");
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field1`=1, `int_field2`=2 WHERE `auto` = 1");
	}
	
	public void testInclude()throws Exception{
		TestSimpleModel model=new TestSimpleModel();
		model.include("int_field1");
		model.setIntField1(1);
		model.setIntField2(2);
		
		Query query=model.dialect().insert(model,false);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field1`)VALUES(1)");
		
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field1`=1 WHERE `auto` = 1");
	}
	
	public void testExclude()throws Exception{
		TestSimpleModel model=new TestSimpleModel();
		model.exclude("int_field1");
		model.setIntField1(1);
		model.setIntField2(2);
		model.setStringField1("xstring");
		
		Query query=model.dialect().insert(model,false);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field2`, `string_field1`)VALUES(2, 'xstring')");
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field2`=2, `string_field1`='xstring' WHERE `auto` = 1");
	}
	
	public void testChangeFields()throws Exception{
		TestSimpleModel model=new TestSimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setStringField1("xstring");
		
		Collection<FGS> cgs= model.changedFields();
		Assert.assertEquals(cgs.size(),3);
		
		model.exclude("int_field1");
		cgs= model.changedFields();
		Assert.assertEquals(cgs.size(),2);
		
		Query query=model.dialect().insert(model,false);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field2`, `string_field1`)VALUES(2, 'xstring')");
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field2`=2, `string_field1`='xstring' WHERE `auto` = 1");
	}
}
