package test.com.tsc9526.monalisa.core.query.datatable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.query.TestSimpleModel;

import com.tsc9526.monalisa.core.query.datatable.CsvOptions;
import com.tsc9526.monalisa.core.query.datatable.DataColumn;
import com.tsc9526.monalisa.core.query.datatable.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DataTableTest {

	public void testDataTableMap() {
		DataColumn[] headers = new DataColumn[] { new DataColumn("a"), new DataColumn("b"), new DataColumn("c") };

		DataTable<DataMap> table = new DataTable<DataMap>();
		table.setHeaders(Arrays.asList(headers));

		DataMap row = new DataMap();
		row.put("a", 1);
		row.put("b", "bstring");
		row.put("c", 9526);
		table.add(row);

		row = new DataMap();
		row.put("a", 2);
		row.put("b", "bstring2");
		row.put("c", 9527);
		table.add(row);

		
		DataTable<DataMap> rs=table.select("count(*) as cnt", "a>0", null, null);
		Assert.assertEquals(rs.get(0).getInteger(0).intValue(),2);
		
		rs=table.select("b as b0, c c1", "a=1", null, null);
		Assert.assertEquals(rs.get(0).getString("b0"),"bstring");
		Assert.assertEquals(rs.get(0).getInt("c1",0),9526);
		
		Assert.assertEquals(rs.get(0).getString("c1"),"9526");
		
		Assert.assertEquals(rs.get(0).getString(0),"bstring");
		Assert.assertEquals(rs.get(0).getString(1),"9526");
	}
	
	public void testDataTableBean(){
		DataTable<TestSimpleModel> table=new DataTable<TestSimpleModel>();
		table.add(new TestSimpleModel().setIntField1(1).setStringField1("s1"));
		table.add(new TestSimpleModel().setIntField1(2).setStringField1("s2"));
		table.add(new TestSimpleModel().setIntField1(3).setStringField1("s3"));
		
		DataTable<DataMap> rs=table.select("*","stringField1='s2'", null,null);
		Assert.assertEquals(rs.size(),1);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),2);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s2");
		
		rs=table.select("*","stringField1 like 's%'", "order by stringField1 desc",null);
		Assert.assertEquals(rs.size(),3);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),3);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s3");
		
		rs=table.select("sum(intField1) as s1","stringField1 in ('s2','s3')", null,null);
		Assert.assertEquals(rs.size(),1);
		Assert.assertEquals(rs.get(0).getInt("s1",0),5);
	}
	
	public void testDataTableWrite()throws IOException{
		DataTable<TestSimpleModel> table=new DataTable<TestSimpleModel>();
		table.add(new TestSimpleModel().setIntField1(1).setStringField1("\"s1"));
		table.add(new TestSimpleModel().setIntField1(2).setStringField1("s2"));
		table.add(new TestSimpleModel().setIntField1(3).setStringField1("s3"));
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		table.saveCsv(bos, CsvOptions.createDefaultOptions());
		
		String csv=new String(bos.toByteArray());
		Assert.assertTrue(csv.indexOf("stringField1")>0  && csv.indexOf("intField1")>0);
		Assert.assertTrue(csv.indexOf("\\\"s1")>0);
		Assert.assertTrue(csv.indexOf("s2")>0);
		Assert.assertTrue(csv.indexOf("s3")>0);
		 
		DataTable<DataMap> rs=DataTable.fromCsv(csv, CsvOptions.createDefaultOptions());
		Assert.assertEquals(rs.size(),3);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"\"s1");
		Assert.assertEquals(rs.get(1).getString("stringField1"),"s2");
		Assert.assertEquals(rs.get(2).getString("stringField1"),"s3");
		
	}
	  
	public void testAs(){
		DataTable<TestSimpleModel> table=new DataTable<TestSimpleModel>();
		TestSimpleModel v1=new TestSimpleModel().setIntField1(1).setStringField1("\"s1");
		table.add(v1);
		 
		Assert.assertTrue(table.as(TestSimpleModel.class)==table);
		
		
		DataTable<DataMap> table2 = new DataTable<DataMap>();
		
		DataMap row = new DataMap();
		row.put("intField1", 226);
		row.put("stringField1", "bstring");
		table2.add(row);
		
		Object r2=table2.as(Map.class);
		Assert.assertTrue(r2==table2);
		
		DataTable<TestSimpleModel> x2=table2.as(TestSimpleModel.class);
		Assert.assertTrue(x2.size()==1);
		Assert.assertTrue(x2.get(0).getIntField1()==226);
		Assert.assertTrue(x2.get(0).getStringField1()=="bstring");
		
		
		
		DataTable<Map<Object,Object>> table3 = new DataTable<Map<Object,Object>>();
				
		Map<Object,Object> row3 = new HashMap<Object,Object>();
		row3.put("intField1", 226);
		row3.put("stringField1", "bstring");
		table3.add(row3);
		
		Object r3=table3.as(Map.class);
		Assert.assertTrue(r3==table3);
		
		Object r4=table3.as(DataMap.class);
		Assert.assertTrue(r4!=table3);
		
		DataTable<DataMap> r5=table3.as(DataMap.class);
		Assert.assertTrue(r5.get(0).getInt("intField1",0)==226);
		
	}
}
