package test.com.tsc9526.monalisa.core.jsp;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;
import com.tsc9526.monalisa.core.parser.jsp.JspPage;
import com.tsc9526.monalisa.core.parser.query.QueryPackage;
import com.tsc9526.monalisa.core.query.Args;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.core.tools.JavaWriter;

@Test
public class JspParserTest {

	public void testParseJspTest1()throws Exception{
		File sqlFile=new File("sql/mysqldb/test1.jsp");
		long fileTime=0;
		
		while(true){
			if(fileTime!=sqlFile.lastModified()){
				if(fileTime>0){
					System.out.println("Reload file: "+sqlFile.getAbsolutePath());
				}
				fileTime=sqlFile.lastModified();
				
				runSQL(sqlFile);
			}else{
				Thread.sleep(1000);
			}
		}
	}
	
	private void runSQL(File sqlFile)throws Exception{
		Jsp jsp=new Jsp(sqlFile);
		String dirSrc="target/monalisa/query/src/monalisa/query";
		String dirClasses="target/monalisa/query/classes";
		
		JavaWriter writer=JavaWriter.getBufferedWriter(1);
		QueryPackage pkg=new QueryPackage(jsp);
		pkg.write(writer);
		
		//System.out.println(writer.getContent());
		
		FileHelper.write(new File(dirSrc,"Q.java"),writer.getContent().getBytes("utf-8"));

		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		
		File classRootDir=new File(dirClasses);
		if(!classRootDir.exists()){
			classRootDir.mkdirs();
		}
		
		String classpath = System.getProperty("java.class.path");
		int r=javac.run(System.in, System.out,System.err,
				"-encoding", "utf-8",
				"-classpath",classpath,"-nowarn","-d",dirClasses,dirSrc+"/Q.java");
		if(r==0){
			System.out.println("Compile OK: "+dirSrc+"/Q.java");
			
			URLClassLoader loader=new URLClassLoader(new URL[]{classRootDir.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
			
			Class<?> qClazz=loader.loadClass("monalisa.query.Q");
			Object qRun=qClazz.newInstance();
			
			Method m=qClazz.getMethod("testFindAll_A", Query.class,Args.class);
			Query query=new Query();
			Args  args=new Args("name","title","zzg");
			m.invoke(qRun, query,args);
			
			System.out.println(query.getExecutableSQL());
			DataTable<DataMap> rs=query.getList();
			System.out.println("Total results: "+rs.size());
			for(DataMap x:rs){
				System.out.println(x.toString());
			}
			 
			loader.close();
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
