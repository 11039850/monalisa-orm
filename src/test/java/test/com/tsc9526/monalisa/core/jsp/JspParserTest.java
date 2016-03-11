package test.com.tsc9526.monalisa.core.jsp;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.jsp.JspPage;
import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.tools.JavaWriter;

@Test
public class JspParserTest {

	public void testParseJspTest1()throws Exception{
		Jsp jsp=new Jsp(new File("sql/mysqldb/test1.jsp"));
		
		 
		JavaWriter writer=JavaWriter.getBufferedWriter(1);
		QueryPackage pkg=new QueryPackage(jsp);
		pkg.write(writer);
		
		
		System.out.println(writer.getContent());
		

		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		String dir="target/run";
		
		int r=javac.run(System.in, System.out,System.err,
				"-encoding", "utf-8",
				"-classpath","target/classes;target/test-classes", "-d","target/testing",dir+"/TestTable1.java");
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
