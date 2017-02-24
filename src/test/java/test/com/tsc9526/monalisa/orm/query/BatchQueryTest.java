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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestTable1;

import com.tsc9526.monalisa.orm.Query;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class BatchQueryTest {

	@BeforeMethod
	public void setup(){
		TestTable1.DELETE().deleteAll();
		
		Assert.assertEquals(TestTable1.SELECT().count(),0);
	}
	
	public void testBatchSQL(){
		String[] sqls=(""+/**~!{*/""
			+ "INSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('A1', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
			+ "\r\nINSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('B1', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
			+ "\r\nINSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('C1', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
		+ "\r\n"/**}*/).split(";");
		
		Query q=MysqlDB.DB.createQuery();
		int[] rs=q.execute(sqls);
		Assert.assertEquals(rs.length,3); 
		for(int i=0;i<3;i++){
			Assert.assertEquals(rs[i],1);
		}
	}
	
	public void testBatchSQLWithException(){
		String[] sqls=(""+/**~!{*/""
			+ "INSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('A2', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
			+ "\r\nINSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('B2', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
			+ "\r\nINSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('C2', 0, 'TRUE', 'xxxx-02-24 17:32:26', '2017-02-24 17:32:26');"
		+ "\r\n"/**}*/).split(";");
		
		try{
			Query q=MysqlDB.DB.createQuery();
			q.execute(sqls);
			Assert.fail("Exception not found!");
		}catch(Exception e){
			Assert.assertEquals(TestTable1.SELECT().count(),0);
		}
	}
	
	public void testBatch(){
		Query q=MysqlDB.DB.createQuery();
		q.add("INSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES(?,?,?,?,?)");
		for(int i=0;i<5;i++){
			q.addBatch("X-"+i, 0, "TRUE", "2017-02-24 17:32:26", "2017-02-24 17:32:26");
		}
		int[] rs=q.executeBatch();
		Assert.assertEquals(rs.length,5); 
		for(int i=0;i<5;i++){
			Assert.assertEquals(rs[i],1);
		} 
 	}
	
	public void testBatchWithException(){
		try{
			Query q=MysqlDB.DB.createQuery();
			q.add("INSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES(?,?,?,?,?)");
			for(int i=0;i<5;i++){
				q.addBatch("X-"+i, 0, "TRUE", "2017-02-24 17:32:26", "2017-02-24 17:32:26");
			}
			q.addBatch("X-y", 0, "TRUE", "xxx-02-24 17:32:26", "2017-02-24 17:32:26");
			q.executeBatch();
			 
			Assert.fail("Exception not found!");
		}catch(Exception e){
			Assert.assertEquals(TestTable1.SELECT().count(),0);
		}
	}
}
