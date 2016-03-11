package test.com.tsc9526.monalisa.core.jsp;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.executor.SQLResourceManager;
import com.tsc9526.monalisa.core.parser.jsp.JspPage;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.datatable.DataTable;

@Test
public class JspParserTest {

	public void testParseJspTest1()throws Exception{
		File sqlFile=new File("sql/mysqldb/test1.jsp");
		long fileTime=0;
		
		SQLResourceManager.getInstance().loadSqlFiles(sqlFile, ".jsp", true);
		
		while(true){
			if(fileTime!=sqlFile.lastModified()){
				if(fileTime>0){
					System.out.println("Reload file: "+sqlFile.getAbsolutePath());
				}
				fileTime=sqlFile.lastModified();
				
				Query query=Query.create("example.testFindAll_A","name");
				System.out.println(query.getExecutableSQL());
				DataTable<DataMap> rs=query.getList();
				System.out.println("Total results: "+rs.size());
				for(DataMap x:rs){
					System.out.println(x.toString());
				}
			}else{
				Thread.sleep(1000);
			}
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
