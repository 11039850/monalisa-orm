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
 
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable2;
import test.com.tsc9526.monalisa.orm.query.TestSimpleModel;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.string.MelpDate;

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
		
		Query query=model.dialect().insert(model);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field1`, `int_field2`)VALUES(1, 2)");
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field1`=1, `int_field2`=2 WHERE `auto` = 1");
	}
	
	public void testUpdateByVersion(){
		String ts=MelpDate.now();
		
		TestTable1 table1=new TestTable1();
		table1.setTitle("title_v1");
		table1.setName("name_v1");
		table1.setVersion(1);
		table1.set("ts_a",ts);
		table1.set("create_time",ts);
		
		Assert.assertEquals(table1.save(),1);
		
		TestTable1 table2=TestTable1.SELECT().selectByPrimaryKey(table1.getId());
		TestTable1 table3=TestTable1.SELECT().selectByPrimaryKey(table1.getId());
		 
		table2.setName("n2");
		table3.setName("n3");
		 
		String sql=MysqlDB.DB.getDialect().updateByVersion(table2).getExecutableSQL();
		String expect="UPDATE `test_table_1` SET `name`='n2', `title`='title_v1', `enum_int_a`=0, `enum_string_a`='TRUE', `ts_a`='"+ts+"', `create_time`='"+ts+"', `create_by`=null, `update_time`=null, `update_by`=null, `version` = `version` + 1 WHERE `id` = "+table1.getId()+" AND `version` = 1";
		Assert.assertEquals(sql,expect);
		
		
		sql=MysqlDB.DB.getDialect().update(table2).getExecutableSQL();
		expect="UPDATE `test_table_1` SET `name`='n2', `title`='title_v1', `enum_int_a`=0, `enum_string_a`='TRUE', `ts_a`='"+ts+"', `create_time`='"+ts+"', `create_by`=null, `update_time`=null, `update_by`=null, `version`=1 WHERE `id` = "+table1.getId();
		Assert.assertEquals(sql,expect);
		
		Assert.assertEquals(table2.updateByVersion(),1);
		Assert.assertEquals(table3.updateByVersion(),0);
		
		table3.load();
		Assert.assertEquals(table3.getVersion().intValue(),2);
		Assert.assertEquals(table3.getName(),"n2");
	}
	
	public void testUpdateByVersion2(){
		String ts=MelpDate.now();
		
		//version field's name is v1
		TestTable2 table1=new TestTable2();
		
		table1.setTitle("title_v2");
		table1.setName("name_v2");
		table1.setV1(1);
		table1.set("ts_a",ts);
		table1.set("create_time",ts);
		
		Assert.assertEquals(table1.save(),1);
		
		TestTable2 table2=TestTable2.SELECT().selectByPrimaryKey(table1.getId());
		TestTable2 table3=TestTable2.SELECT().selectByPrimaryKey(table1.getId());
		 
		table2.setName("n2");
		table3.setName("n3");
		 
		String sql=MysqlDB.DB.getDialect().updateByVersion(table2).getExecutableSQL();
		String expect="UPDATE `test_table_2` SET `name`='n2', `title`='title_v2', `enum_int_a`=0, `enum_string_a`='TRUE', `array_int`=null, `array_string`=null, `json`=null, `obj`=null, `ts_a`='"+ts+"', `create_time`='"+ts+"', `create_by`=null, `update_time`=null, `update_by`=null, `v1` = `v1` + 1 WHERE `id` = "+table1.getId()+" AND `v1` = 1";
		Assert.assertEquals(sql,expect);
		
		
		sql=MysqlDB.DB.getDialect().update(table2).getExecutableSQL();
		expect="UPDATE `test_table_2` SET `name`='n2', `title`='title_v2', `enum_int_a`=0, `enum_string_a`='TRUE', `array_int`=null, `array_string`=null, `json`=null, `obj`=null, `ts_a`='"+ts+"', `create_time`='"+ts+"', `create_by`=null, `update_time`=null, `update_by`=null, `v1`=1 WHERE `id` = "+table1.getId();
		Assert.assertEquals(sql,expect);
		
		Assert.assertEquals(table2.updateByVersion(),1);
		Assert.assertEquals(table3.updateByVersion(),0);
		
		table3.load();
		Assert.assertEquals(table3.getV1().intValue(),2);
		Assert.assertEquals(table3.getName(),"n2");
	}
	
	public void testInclude()throws Exception{
		TestSimpleModel model=new TestSimpleModel();
		model.include("int_field1");
		model.setIntField1(1);
		model.setIntField2(2);
		
		Query query=model.dialect().insert(model);
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
		
		Query query=model.dialect().insert(model);
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
		
		Query query=model.dialect().insert(model);
		String sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"INSERT INTO `simple_model`(`int_field2`, `string_field1`)VALUES(2, 'xstring')");
		
		model.setAuto(1);
		query=model.dialect().update(model);
		sql=query.getExecutableSQL(); 
		Assert.assertEquals(sql,"UPDATE `simple_model` SET `int_field2`=2, `string_field1`='xstring' WHERE `auto` = 1");
	}
	
	public void testCopy(){
		TestSimpleModel x1=new TestSimpleModel();
		x1.setIntField1(1);
		x1.setIntField2(2);
		x1.setStringField1("xstring");
		
		TestSimpleModel x2=x1.copy();
		Assert.assertEquals(x1.table().name(), x2.table().name());
		Assert.assertEquals(x1.getIntField1(), x2.getIntField1());
		Assert.assertEquals(x1.getStringField1(), x2.getStringField1());
	}
}
