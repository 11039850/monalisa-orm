package test.com.tsc9526.monalisa.core.query;

import java.text.SimpleDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.query.Query;

@Test
public class CriteriaTest {
	static {
		DataSourceManager.getInstance();
	}
	 
	public void testOrderByOne(){
		SimpleModel.Criteria criteria=SimpleModel.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		
		String sql=criteria.getQuery().getSql();
		String eql=criteria.getQuery().getExecutableSQL();
		String sql_expect="`int_field1` BETWEEN ? AND ?";
		String eql_expect="`int_field1` BETWEEN 1 AND 10";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
		
		sql=criteria.getExample().getQuery().getSql();
		eql=criteria.getExample().getQuery().getExecutableSQL();
		sql_expect="`int_field1` BETWEEN ? AND ? ORDER BY `int_field1` ASC";
		eql_expect="`int_field1` BETWEEN 1 AND 10 ORDER BY `int_field1` ASC";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
	}
	 
	public void testOrderByTwo()throws Exception{		
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		SimpleModel.Criteria criteria=SimpleModel.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		criteria.dateField1.equalsTo(sdf.parse(time)).dateField1.desc();
		
		String sql=criteria.getQuery().getSql();
		String eql=criteria.getQuery().getExecutableSQL();
		String sql_expect="`int_field1` BETWEEN ? AND ? AND `date_field1` = ?";
		String eql_expect="`int_field1` BETWEEN 1 AND 10 AND `date_field1` = '"+time+"'";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
		
		sql=criteria.getExample().getQuery().getSql();
		eql=criteria.getExample().getQuery().getExecutableSQL();
		sql_expect="`int_field1` BETWEEN ? AND ? AND `date_field1` = ? ORDER BY `int_field1` ASC, `date_field1` DESC";
		eql_expect="`int_field1` BETWEEN 1 AND 10 AND `date_field1` = '"+time+"' ORDER BY `int_field1` ASC, `date_field1` DESC";
		
		Assert.assertEquals(sql, sql_expect);
		Assert.assertEquals(eql, eql_expect);
				
	}
	
	public void testInset()throws Exception{
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SimpleModel model=new SimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setDateField1(sdf.parse(time));
		 
		Query query=model.getDialect().insert(model, true);
		String sql=query.getSql();
		String sql_expect="REPLACE INTO `simple_model`(`int_field1`, `int_field2`, `string_field1`, `string_field2`, `date_field1`, `date_field2`)VALUES(?, ?, ?, ?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
		
		query=model.getDialect().insert(model, false);
		sql=query.getSql();
		sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `string_field1`, `string_field2`, `date_field1`, `date_field2`)VALUES(?, ?, ?, ?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
		
		
		query=model.getDialect().insertSelective(model, true);
		sql=query.getSql();
		sql_expect="REPLACE INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`)VALUES(?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
		
		query=model.getDialect().insertSelective(model, false);
		sql=query.getSql();
		sql_expect="INSERT INTO `simple_model`(`int_field1`, `int_field2`, `date_field1`)VALUES(?, ?, ?)";
		Assert.assertEquals(sql,sql_expect);
	 
	}
	
	public void testSelect()throws Exception{
		String time="2015-06-08 11:10:31";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SimpleModel model=new SimpleModel();
		model.setIntField1(1);
		model.setIntField2(2);
		model.setDateField1(sdf.parse(time));
		
		
		SimpleModel.Example example=new SimpleModel.Example();
		SimpleModel.Criteria criteria=example.createCriteria();
		criteria.intField1.between(1, 10).intField1.asc();
		criteria.dateField1.equalsTo(sdf.parse(time)).dateField1.desc();
		
		SimpleModel.Criteria or=example.or();
		or.intField2.greatThan(1);
		
		Query eq=example.getQuery();
		Query query=model.getDialect().select(model, eq.getSql(), eq.getParameters());
		String sql_expect="SELECT * FROM `simple_model` WHERE `int_field1` BETWEEN ? AND ? AND `date_field1` = ? OR (`int_field1` > ?) ORDER BY `int_field1` ASC, `date_field1` DESC";
		Assert.assertEquals(query.getSql(), sql_expect);
	}
}

