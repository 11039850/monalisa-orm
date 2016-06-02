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

import java.util.Date;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.mysql.MysqlDB;

import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.Record;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class RecordTest extends Model<RecordTest> implements MysqlDB{ 
	private static final long serialVersionUID = -1974865252589672370L;

	@BeforeClass
	public void setUp(){
		tearDown();
		
		DB.execute(""+/**~{*/""
			+ "CREATE TABLE IF NOT EXISTS `test_record` ("
			+ "\r\n  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',"
			+ "\r\n  `name` varchar(128) NOT NULL default 'N0001' COMMENT '名称',"
			+ "\r\n  `title` varchar(128) NULL  COMMENT '标题',"
			+ "\r\n  `ts_a` datetime  NULL,"
			+ "\r\n  `create_time` datetime NOT NULL,"
			+ "\r\n  `create_by` varchar(64) NULL,"
			+ "\r\n  `update_time` datetime NULL,"
			+ "\r\n  `update_by` varchar(64) NULL,"
			+ "\r\n  PRIMARY KEY (`record_id`)"
			+ "\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;"
		+ "\r\n"/**}*/);
		
		DB.execute(""+/**~{*/""
			+ "INSERT INTO test_record(record_id,`name`,`title`,ts_a,create_time)VALUES(1,\"hello\",\"record\",now(),now());"
		+ "\r\n"/**}*/);
	}
	
	@AfterClass
	public void tearDown(){
		DB.execute("DROP TABLE IF EXISTS `test_record`");
	}
	
	String title;
	
	public RecordTest() {
		 super("test_record");
	}
	 
	
	@Test
	public void testRecordTest1()throws Exception{
		RecordTest tx=new RecordTest(); 
		Assert.assertEquals(DB, tx.db());
		Assert.assertEquals(tx.entity(),false); 
		 	
		tx.set("record_id", 1);
		tx.load();
		Assert.assertEquals(tx.entity(),true); 
	}
	
	@Test
	public void testRecordTest2()throws Exception{
		RecordTest tx=new RecordTest(); 
		tx.setTitle("TX");
		tx.set("name","Test-Tx");
		tx.set("ts_a", new Date());
		tx.save();
		
		int id=tx.get("record_id");
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertTrue(id>1);
	 
		RecordTest tx2=new RecordTest().set("record_id", id).load();
		Assert.assertEquals(tx.getTitle(),tx2.getTitle());
		Assert.assertTrue(tx2.get("create_time")!=null);
	}
	
	@Test
	public void testRecordTest3()throws Exception{
		RecordTest m=new RecordTest();
		m.set("name", "package_name");
		m.title="xxx";
		m.save();
		  
		int times=10000;
		long l1=System.currentTimeMillis();
		for(int i=0;i<times;i++){
			RecordTest x=new RecordTest();			 
			x.changedFields();
		}
		long l2=System.currentTimeMillis();
		System.out.println("Changed fields: "+times+", use time: "+(l2-l1)+" ms");		
		
		Assert.assertTrue( (l2-l1) <1000);
	}
	
	@Test
	public void testRecordSelect()throws Exception{
		Record sm=new Record("test_record").use(DB);
		DataTable<Record> rs=sm.WHERE()
		.field("title").like("record")
		.field("record_id").gt(0)
		.forSelect().select();
		
		Assert.assertTrue(rs.size()>0);
	}

	
	@Test
	public void testRecordLoad()throws Exception{
		Record tx=new Record("test_record").use(DB); 
		Assert.assertEquals(DB, tx.db());
		Assert.assertTrue(tx.fields().size()>1);
		Assert.assertTrue(tx.field("title")!=null);
		Assert.assertEquals(tx.entity(),false); 
		 	
		tx.set("record_id", 1);
		tx.load();
		Assert.assertEquals(tx.entity(),true); 
		
		DataTable<DataMap> rs=DB.select("select count(*) from test_record");
		Assert.assertTrue(rs.size()>0);
	}
	
	@Test
	public void testRecordSave()throws Exception{
		Record tx=new Record("test_record").use(DB); 
		tx.set("name",  "ns001");
		tx.set("title", "title001");
		tx.save();
		
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertTrue(tx.get("record_id")!=null);  
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return "{}";
	}
}
