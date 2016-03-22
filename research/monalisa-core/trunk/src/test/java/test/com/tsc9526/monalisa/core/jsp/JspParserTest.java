package test.com.tsc9526.monalisa.core.jsp;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.sql.Q0001;

import com.tsc9526.monalisa.core.generator.DBGeneratorMain;
import com.tsc9526.monalisa.core.parser.jsp.JspPage;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.datatable.DataTable;

@Test
public class JspParserTest {
	
	public void testGenerate(){
		DBGeneratorMain.generateSqlQueryClass("src/test/java");
	}
	
	public void testQueryByQueryId()throws Exception{
		Query query=Query.create("test.com.tsc9526.monalisa.core.sql.Q0001.testFindAll_A","name","","");
		System.out.println(query.getExecutableSQL());
		DataTable<DataMap> rs=query.getList();
		System.out.println("Total results: "+rs.size());
		for(DataMap x:rs){
			System.out.println(x.toString());
		}	 
	}
	
	public void testQueryByInterface(){
		Query query=Q0001.testFindAll_A("", "", "");
		System.out.println(query.getExecutableSQL());
		DataTable<DataMap> rs=query.getList();
		System.out.println("Total results: "+rs.size());
		for(DataMap x:rs){
			System.out.println(x.toString());
		}
	}
	
	 
	public void testJspPageAttribute(){
		JspPage page=new JspPage(null, 0, 0);
		
		page.parseCode(" language=\"java\" \r\n pageEncoding=\"utf-8\"");	
		Assert.assertEquals(page.getLanguage(),"java");
		Assert.assertEquals(page.getPageEncoding(),"utf-8");
		
		page.parseCode("import = \r\n \"test.com.tsc9526.monalisa.core.mysql.MysqlDB,\r\n com.tsc9526.monalisa.core.query.Query\"");	
		Assert.assertEquals(page.getImports().size(),2);
		Assert.assertTrue(page.getImports().contains("test.com.tsc9526.monalisa.core.mysql.MysqlDB"));
		Assert.assertTrue(page.getImports().contains("com.tsc9526.monalisa.core.query.Query"));
	}
}
