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
package test.com.tsc9526.monalisa.orm.criteria;
 

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecord;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.criteria.QEH;
import com.tsc9526.monalisa.orm.model.Record;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */

@Test
public class WhereTest {
	public void testAND(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria().name.eq("a1").title.eq("a2");
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "`name` = 'a1' AND `title` = 'a2'");
	}
	
	public void testOR1(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria() 
			.name.eq("a1")
			.OR()
			.name.eq("b1");
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1') OR (`name` = 'b1')");
	}
	
	public void testOR2(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria() 
			.name.eq("a1").title.eq("a2")
			.OR()
			.name.eq("b1").title.eq("b2");
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1' AND `title` = 'a2') OR (`name` = 'b1' AND `title` = 'b2')");
	}
	
	public void testOR3(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria() 
			.name.eq("a1")
			.OR()
			.name.eq("b1").title.eq("b2");
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1') OR (`name` = 'b1' AND `title` = 'b2')");
	}
	
	public void testASC(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria() 
			.name.eq("a1")
			.OR()
			.name.eq("b1").title.eq("b2")
			.name.asc().title.desc();
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1') OR (`name` = 'b1' AND `title` = 'b2') ORDER BY `name` ASC, `title` DESC");
	}
	
	public void testDESC(){
		TestRecord.$Example example=new TestRecord.$Example();
		example.createCriteria() 
			.name.eq("a1")
			.OR()
			.name.eq("b1").title.eq("b2")
			.name.desc();
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1') OR (`name` = 'b1' AND `title` = 'b2') ORDER BY `name` DESC");
	}
	
	
	public void testRecordASC(){
		Record r=new Record("test_record").use(MysqlDB.DB);
		 
		Record.Criteria c=r.WHERE()
			.field("name").eq("a1")
			.OR()
			.field("name").eq("b1").field("title").eq("b2")
			.field("name").asc().field("title").desc();
		
		Record.Example example=c.getExample();
		
		Query w=QEH.getQuery(example);
		String sql=w.getExecutableSQL();
		Assert.assertEquals(sql, "(`name` = 'a1') OR (`name` = 'b1' AND `title` = 'b2') ORDER BY `name` ASC, `title` DESC");
	}
}
