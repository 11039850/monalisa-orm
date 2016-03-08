package test.com.tsc9526.monalisa.core.jsp;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.jsp.JspElement;
import com.tsc9526.monalisa.core.parser.jsp.JspPage;
import com.tsc9526.monalisa.core.parser.jsp.JspText;

@Test
public class JspParserTest {

	public void testParseJspTest1()throws Exception{
		Jsp jsp=new Jsp("sql/mysqldb/test1.jsp");
		
		List<JspElement> es=jsp.getElements();
		for(int i=0;i<es.size();i++){
			JspElement e=es.get(i);
			 
			String code=e.getCode();
			if(e instanceof JspText && code.indexOf("<query")>=0){
				
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
