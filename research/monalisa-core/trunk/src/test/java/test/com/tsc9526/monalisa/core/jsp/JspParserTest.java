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
