package test.com.tsc9526.monalisa.core.jsp;

import java.io.FileInputStream;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JspHelper;

@Test
public class JspHelperTest {

	public void testQuery()throws Exception{
		JspHelper.Jsp jsp=new JspHelper.Jsp(FileHelper.readToString(new FileInputStream("sql/mysqldb/test1.jsp"),"utf-8"));
		
		jsp.write(System.out);
	}
}
