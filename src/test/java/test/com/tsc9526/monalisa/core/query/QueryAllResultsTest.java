package test.com.tsc9526.monalisa.core.query;

import java.util.List;

import org.testng.annotations.Test;

import junit.framework.Assert;
import test.com.tsc9526.monalisa.core.mysql.MysqlDB;
import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable1;

import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
 

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

}
