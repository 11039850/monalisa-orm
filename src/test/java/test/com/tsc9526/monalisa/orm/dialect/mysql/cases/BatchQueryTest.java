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
package test.com.tsc9526.monalisa.orm.dialect.mysql.cases;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;

import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;

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
		+ "\r\n"/**}*/).trim().split(";");
		
		Query q=MysqlDB.DB.createQuery();
		int[] rs=q.executeBatch(sqls);
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
			q.executeBatch(sqls);
			Assert.fail("Exception not found!");
		}catch(Exception e){
			Assert.assertEquals(TestTable1.SELECT().count(),0);
		}
	}
	
	
	public void testBatchSQLWithException2(){
		String[] sqls=(""+/**~!{*/""
			+ "DROP TABLE IF EXISTS `test_table_x` ;"
	+ "\r\n"
			+ "\r\nCREATE TABLE `test_table_x` ("
			+ "\r\n  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',"
			+ "\r\n  `name` varchar(128) NOT NULL default 'N0001' COMMENT 'the name',"
			+ "\r\n  `title` varchar(128) NULL  COMMENT 'the title',"
			+ "\r\n  `enum_int_a` int(11) NOT NULL default 0 COMMENT 'enum fields A  #enum{{V0,V1}}',"
			+ "\r\n  `enum_string_a` varchar(64) NOT NULL default 'TRUE' COMMENT '#enum{{ TRUE, FALSE}}',"
			+ "\r\n  `ts_a` datetime NOT NULL,"
			+ "\r\n  `create_time` datetime NOT NULL,"
			+ "\r\n  `create_by` varchar(64) NULL,"
			+ "\r\n  `update_time` datetime NULL,"
			+ "\r\n  `update_by` varchar(64) NULL,"
			+ "\r\n  PRIMARY KEY (`id`),"
			+ "\r\n  KEY `ix_name_title`(`name`,`title`),"
			+ "\r\n  UNIQUE KEY `ux_name_time`(`name`,`create_time`)"
			+ "\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;"
	+ "\r\n"
			+ "\r\nINSERT INTO `test_table_x`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('B2', 0, 'TRUE', '2017-02-24 17:32:26', '2017-02-24 17:32:26');"
			+ "\r\nINSERT INTO `test_table_x`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES('C2', 0, 'TRUE', 'xxxx-02-24 17:32:26', '2017-02-24 17:32:26');"
		+ "\r\n"/**}*/).split(";");
		
		try{
			Query q=MysqlDB.DB.createQuery();
			q.executeBatch(sqls);
			Assert.fail("Exception not found!");
		}catch(Exception e){
			Assert.assertEquals(TestTable1.SELECT().count(),0);
		}
	}
	
	
	
	public void testBatch(){
		Query q=MysqlDB.DB.createQuery();
		q.add("INSERT INTO `test_table_1`(`name`, `title`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES(?,?,?,?,?,?)");
		
		List<Object[]> args = new ArrayList<Object[]>();
		for(int i=0;i<5;i++){
			args.add(new Object[] {"X-"+i, "title-"+i, 0, "TRUE", "2017-02-24 17:32:26", "2017-02-24 17:32:26"});
		}
		int[] rs=q.executeBatch(args);
		Assert.assertEquals(rs.length,5); 
		for(int i=0;i<5;i++){
			Assert.assertEquals(rs[i],1);
		} 
		
		for(int i=0;i<5;i++) {
			Assert.assertEquals(TestTable1.WHERE().name.eq("X-"+i).SELECT().selectOne().getTitle(),"title-"+i);
		}
 	}
	
	public void testBatchWithException(){
		try{
			Query q=MysqlDB.DB.createQuery();
			q.add("INSERT INTO `test_table_1`(`name`, `enum_int_a`, `enum_string_a`, `ts_a`, `create_time`)VALUES(?,?,?,?,?)");
			
			List<Object[]> args = new ArrayList<Object[]>();
			for(int i=0;i<5;i++){
				args.add(new Object[] {"X-"+i, 0, "TRUE", "2017-02-24 17:32:26", "2017-02-24 17:32:26"});
			}
			
			args.add(new Object[] {"X-y", 0, "TRUE", "xxx-02-24 17:32:26", "2017-02-24 17:32:26"});
			
			q.executeBatch(args);
			 
			Assert.fail("Exception not found!");
		}catch(Exception e){
			Assert.assertEquals(TestTable1.SELECT().count(),0);
		}
	}
}
