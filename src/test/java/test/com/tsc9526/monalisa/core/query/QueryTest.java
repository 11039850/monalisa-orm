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
package test.com.tsc9526.monalisa.core.query;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.mysql.MysqlDB;

import com.tsc9526.monalisa.core.query.Query;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class QueryTest {

	public void testIn01(){
		Query q=MysqlDB.DB.createQuery();
		
		String[] values="x1,x2".split(",");
		q.add("SELECT * FROM T1 WHERE f1").in((Object)values);
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 IN(?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 IN('x1', 'x2')");
	}
	
	public void testIn02(){
		Query q=MysqlDB.DB.createQuery();
		
		String[] values="x1,x2".split(",");
		q.add("SELECT * FROM T1 WHERE f1").in((Object[])values);
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 IN(?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 IN('x1', 'x2')");
	}
	
	public void testIn03(){
		Query q=MysqlDB.DB.createQuery();
		 
		q.add("SELECT * FROM T1 WHERE f1").in("x1","x2");
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 IN(?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 IN('x1', 'x2')");
	}
	
	public void testIn04(){
		Query q=MysqlDB.DB.createQuery();
		 
		List<String> xs=new ArrayList<String>();
		xs.add("x1");
		xs.add("x2");
		
		q.add("SELECT * FROM T1 WHERE f1").in(xs);
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 IN(?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 IN('x1', 'x2')");
	}
	
	public void testIn05(){
		Query q=MysqlDB.DB.createQuery();
		 
		List<String> xs=new ArrayList<String>();
		xs.add("x1");
		xs.add("x2");
		
		q.add("SELECT * FROM T1 WHERE f1").in(xs,"x3");
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 IN(?, ?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 IN('x1', 'x2', 'x3')");
	}
	 
	public void testNotIn05(){
		Query q=MysqlDB.DB.createQuery();
		 
		List<String> xs=new ArrayList<String>();
		xs.add("x1");
		xs.add("x2");
		
		q.add("SELECT * FROM T1 WHERE f1").notin(xs,"x3");
		
		Assert.assertEquals(q.getSql(),"SELECT * FROM T1 WHERE f1 NOT IN(?, ?, ?)");
		Assert.assertEquals(q.getExecutableSQL(),"SELECT * FROM T1 WHERE f1 NOT IN('x1', 'x2', 'x3')");
	}
	
}
