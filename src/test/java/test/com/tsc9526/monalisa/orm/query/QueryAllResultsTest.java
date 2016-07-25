package test.com.tsc9526.monalisa.orm.query;

import java.util.List;

import org.testng.annotations.Test;

import junit.framework.Assert;
import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestTable1;

import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
 

@Test
public class QueryAllResultsTest {
 
	public void testGetAllResults1() throws Exception {
		List<DataTable<DataMap>> rs=MysqlDB.DB.createQuery().add(""+/**~{*/""
			+ "SET @table=\"test_table_1\";"
			+ "\r\nSET @sql= CONCAT(\"select * from \", @table, \" where name='N0001'\");"
			+ "\r\n"
			+ "\r\nPREPARE statement from @sql;"
			+ "\r\nEXECUTE statement;"
			+ "\r\nDEALLOCATE PREPARE statement;"
		+ "\r\n"/**}*/).getAllResults();
		
		Assert.assertEquals(1, rs.size());
		
		for(TestTable1 r:rs.get(0).as(TestTable1.class)){
			Assert.assertEquals("N0001", r.getName());
		}
	}
	
	public void testGetAllResults2() throws Exception {
		List<DataTable<DataMap>> rs=MysqlDB.DB.createQuery().add(""+/**~{*/""
			+ "SET @table=\"test_table_1\";"
			+ "\r\nSET @sql= CONCAT(\"select * from \", @table, \" where name='N0001'\");"
			+ "\r\n"
			+ "\r\nPREPARE statement from @sql;"
			+ "\r\nEXECUTE statement;"
			+ "\r\nEXECUTE statement;"
			+ "\r\nDEALLOCATE PREPARE statement;"
		+ "\r\n"/**}*/).getAllResults();
		
		Assert.assertEquals(2, rs.size());
	}
	
	public void testGetAllResults3() throws Exception {
		List<DataTable<DataMap>> rs=MysqlDB.DB.createQuery().add(""+/**~{*/""
			+ "SET @table=\"test_table_1\";"
			+ "\r\nSET @sql= CONCAT(\"select * from \", @table, \" where name='N0001'\");"
			+ "\r\n"
			+ "\r\nPREPARE statement from @sql;"
			+ "\r\nEXECUTE statement;"
			+ "\r\nEXECUTE statement;"
			+ "\r\nDEALLOCATE PREPARE statement;"
			+ "\r\n"
			+ "\r\nSELECT * FROM test_table_1 limit 10;"
		+ "\r\n"/**}*/).getAllResults();
		
		Assert.assertEquals(3, rs.size());
	}

}
