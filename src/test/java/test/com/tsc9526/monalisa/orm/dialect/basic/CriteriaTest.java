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
package test.com.tsc9526.monalisa.orm.dialect.basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.criteria.QEH;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class CriteriaTest {
	static {
		DataSourceManager.getInstance();
	}
	
	
	public void testParseMapping(){
		Date d1=new Date();
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("d1", d1);
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h,"d1=dateField1");
		
		Assert.assertEquals(model.getDateField1(), d1);
	}
	
	public void testParsePrefixMapping(){
		Date d1=new Date();
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("simpleMode@d1", d1);
		
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h,"d1=dateField1","~simpleMode@");
		
		Assert.assertEquals(model.getDateField1(), d1);
		
		
		TestSimpleModel model2=new TestSimpleModel();
		model2.parse(h,"d1=dateField1","~simpleMode");
		
		Assert.assertEquals(model.getDateField1(), d1);
		
	}
	
	
	
	public void testNullDate()throws Exception{
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("dateField1", null);
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h);
	}
	
	public void testParse()throws Exception{
		Date d1=new Date();
		
		String d2="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("intField1", 1);
		h.put("intField2", "2");
		
		h.put("dateField1", d1);
		h.put("dateField2", d2);
		
		h.put("stringField1", "xxx");
		h.put("stringField2", 123);
		
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h);
		
		Assert.assertEquals(model.getIntField1().intValue(),1);
		Assert.assertEquals(model.getIntField2().intValue(),2);
		
		Assert.assertEquals(model.getDateField1(), d1);
		Assert.assertEquals(model.getDateField2(), sdf.parseObject(d2));
		
		Assert.assertEquals(model.getStringField1(), "xxx");
		Assert.assertEquals(model.getStringField2(), "123");
	} 
	
	public void testParseNameCaseSensitive()throws Exception{		 
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("intField1", 1);
		h.put("INTField2", 2);
		
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h);
		
		Assert.assertEquals(model.getIntField1().intValue(),1);
	 
		Assert.assertNull(model.getIntField2());
	}
	
	public void testParseNameToJavaStyle()throws Exception{
		Date d1=new Date();
		
		String d2="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Object> h=new HashMap<String, Object>();
		h.put("int_field1", 1);
		h.put("int_field2", "2");
		
		h.put("date_field1", d1);
		h.put("date_field2", d2);
		
		h.put("string_field1", "xxx");
		h.put("string_field2", 123);
		
		TestSimpleModel model=new TestSimpleModel();
		model.parse(h);
		
		Assert.assertEquals(model.getIntField1().intValue(),1);
		Assert.assertEquals(model.getIntField2().intValue(),2);
		
		Assert.assertEquals(model.getDateField1(), d1);
		Assert.assertEquals(model.getDateField2(), sdf.parseObject(d2));
		
		Assert.assertEquals(model.getStringField1(), "xxx");
		Assert.assertEquals(model.getStringField2(), "123");				 
	}
	 
	public void testOrderByOne(){
		TestSimpleModel.Criteria criteria=TestSimpleModel.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		
		String sql=QEH.getQuery(criteria).getSql();
		String eql=QEH.getQuery(criteria).getExecutableSQL();
		String sql_expect="`int_field1` BETWEEN ? AND ?";
		String eql_expect="`int_field1` BETWEEN 1 AND 10";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
		
		sql=QEH.getQuery(criteria.getExample()).getSql();
		eql=QEH.getQuery(criteria.getExample()).getExecutableSQL();
		sql_expect="`int_field1` BETWEEN ? AND ? ORDER BY `int_field1` ASC";
		eql_expect="`int_field1` BETWEEN 1 AND 10 ORDER BY `int_field1` ASC";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
	}
	 
	public void testOrderByTwo()throws Exception{		
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		TestSimpleModel.Criteria criteria=TestSimpleModel.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		criteria.dateField1.eq(sdf.parse(time)).dateField1.desc();
		
		String sql=QEH.getQuery(criteria).getSql();
		String eql=QEH.getQuery(criteria).getExecutableSQL();
		String sql_expect="`int_field1` BETWEEN ? AND ? AND `date_field1` = ?";
		String eql_expect="`int_field1` BETWEEN 1 AND 10 AND `date_field1` = '"+time+"'";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
		
		sql=QEH.getQuery(criteria.getExample()).getSql();
		eql=QEH.getQuery(criteria.getExample()).getExecutableSQL();
		sql_expect="`int_field1` BETWEEN ? AND ? AND `date_field1` = ? ORDER BY `int_field1` ASC, `date_field1` DESC";
		eql_expect="`int_field1` BETWEEN 1 AND 10 AND `date_field1` = '"+time+"' ORDER BY `int_field1` ASC, `date_field1` DESC";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
				
	}
	
	public void testInsertOrUpdate()throws Exception{
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		
		TestSimpleModel model=new TestSimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setDateField1(sdf.parse(time));		
		 
		Query query=model.dialect().insertOrUpdate(model);
		String sql=query.getSql();
		String sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`)VALUES(?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
		
		model.setAuto(1);
		query=model.dialect().insertOrUpdate(model);
		sql=query.getSql();
		sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`, `auto`)VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE `int_field1` = ?, `int_field2` = ?, `date_field1` = ?";
		Assert.assertEquals(sql,sql_expect);
		
		//update none
		model=new TestSimpleModel();
		model.setIntField1(1);
		model.setStringField1("1");
		query=model.dialect().insertOrUpdate(model);
		sql=query.getSql();
		sql_expect="INSERT INTO `simple_model`(`int_field1`, `string_field1`)VALUES(?, ?) ON DUPLICATE KEY UPDATE `int_field1` = `int_field1`";
		Assert.assertEquals(sql,sql_expect);
	}
	
	public void testInsert()throws Exception{
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		
		TestSimpleModel model=new TestSimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setDateField1(sdf.parse(time));		
	 	
		Query query=model.dialect().insert(model);
		String sql=query.getSql();
		String sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`)VALUES(?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
		
		 
		model.setStatus(StatusA.OK);
		query=model.dialect().insert(model);
		sql=query.getExecutableSQL();
		sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`, `status`)VALUES(1, 2, '2015-06-08 11:10:31', 0)";
		Assert.assertEquals(sql,sql_expect);		 
	 
	}
	
	public void testSelect()throws Exception{
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		TestSimpleModel model=new TestSimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setDateField1(sdf.parse(time));		
		
		TestSimpleModel.Example example=new TestSimpleModel.Example();
		TestSimpleModel.Criteria criteria=example.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		criteria.dateField1.eq(sdf.parse(time)).dateField1.desc();
		
		TestSimpleModel.Criteria or=example.or();
		or.intField2.gt(1);
		
		Query eq=QEH.getQuery(example);
		Query query=model.dialect().select(model, eq.getSql(), eq.getParameters());
		String sql_expect="SELECT * FROM `simple_model` WHERE (`int_field1` BETWEEN ? AND ? AND `date_field1` = ?) OR (`int_field1` > ?) ORDER BY `int_field1` ASC, `date_field1` DESC";
		Assert.assertEquals(query.getSql(), sql_expect);
	}
	 
	
	public void testIn(){
		TestSimpleModel.Example example=new TestSimpleModel.Example();
		TestSimpleModel.Criteria criteria=example.createCriteria();
		criteria.stringField1.ins("1, 2,null ,3");
		 
		String sql_expect="`string_field1` IN('1', '2', 'null', '3')";
		Assert.assertEquals(QEH.getQuery(example).getExecutableSQL(), sql_expect);
		
		criteria.stringField2.in("abc","def","ggh");
		sql_expect="`string_field1` IN('1', '2', 'null', '3') AND `string_field2` IN('abc', 'def', 'ggh')";
		Assert.assertEquals(QEH.getQuery(example).getExecutableSQL(), sql_expect);
	}
	
	public void testNull(){
		TestSimpleModel.Example example=new TestSimpleModel.Example();
		TestSimpleModel.Criteria criteria=example.createCriteria();
		
		criteria.stringField1.eq(null);
		String sql_expect="`string_field1` = null";
		Assert.assertEquals(QEH.getQuery(example).getExecutableSQL(), sql_expect);
	}
}

