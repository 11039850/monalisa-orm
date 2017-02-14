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
package test.com.tsc9526.monalisa.tools.agent;
 

 
import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.sqlfiles.Q1;
import test.com.tsc9526.monalisa.orm.sqlfiles.Q2;
import test.com.tsc9526.monalisa.orm.sqlfiles.Q3;
import test.com.tsc9526.monalisa.orm.sqlfiles.Q4;
import test.com.tsc9526.monalisa.orm.sqlfiles.Q5;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.tools.agent.AgentClass;
import com.tsc9526.monalisa.tools.io.JavaFile;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;
 

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class QueryTest {
	static Logger logger=Logger.getLogger(QueryTest.class);
	
	static{
		try{
			File dir=new File("src/test/java/test/com/tsc9526/monalisa/orm/sqlfiles");
			for(File src:dir.listFiles()){
				if(src.isFile() && src.getName().endsWith(".java")){
					File target=new File(DbProp.CFG_SQL_PATH+"/"+src.getName());
					MelpFile.copy(src, target);
					target.setLastModified(src.lastModified());
				}
			}
			
			initJavaSources("Q3",3);
			initJavaSources("Q4",4);
			initJavaSources("Q5",5);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	static void initJavaSources(String name,int value)throws Exception{
		JavaFile java=new JavaFile("src/test/java/test/com/tsc9526/monalisa/orm/sqlfiles/"+name+".java");
		
		java.increaseVersion();
		String r=java.replace("return 1;", "return "+value+";");
		
		MelpFile.write(new File(DbProp.CFG_SQL_PATH+"/"+name+".java"), r.getBytes("utf-8"));
	}
	
	private Q4 q4;
	private Q5 q5=Query.create(Q5.class);
	
	@BeforeTest
	public void setUp()throws Exception{
		Assert.assertEquals(q5.findOne(),5);
		  
		q4=Query.create(Q4.class);
		Assert.assertEquals(q4.findOne(),4);
		
		Assert.assertEquals(Query.create(Q1.class).findOne(),1);
		Assert.assertEquals(Query.create(Q2.class).findOne(),1);
		Assert.assertEquals(Query.create(Q3.class).findOne(),3);	 
	}
	
	@Test
	public void testPrivate(){
		Q2 q21=Query.create(Q2.class);
		Q2 q22=Query.create(Q2.class);
		Assert.assertTrue(q21!=q22);
	}
	
	@Test
	public void testQuery01()throws Exception {
		JavaFile java=new JavaFile(DbProp.CFG_SQL_PATH+"/Q1.java");
		  
		java.replaceAndSave("return 1;", "return 2;");
		AgentClass.reloadClasses(); 
		
		Q1 q1=Query.create(Q1.class);
		Assert.assertEquals(q1.findOne(),2);
		  
		java.replaceAndSave("return 1;", "return 3;");
		AgentClass.reloadClasses(); 
		Assert.assertEquals(q1.findOne(),3); 	
		 
	}
	
	@Test
	public void testQueryWithVersion()throws Exception {
		JavaFile java=new JavaFile(DbProp.CFG_SQL_PATH+"/Q2.java");
		 
		java.replaceAndSave("return 1;", "return 2;");
		AgentClass.reloadClasses(); 
		
		Q2 q2=Query.create(Q2.class);
		Assert.assertEquals(q2.findOne(),1);
		  
		
		java.increaseVersion();
		java.replaceAndSave("return 1;", "return 3;"); 
		AgentClass.reloadClasses(); 
		Assert.assertEquals(q2.findOne(),3); 
		
		q2=Query.create(Q2.class);
		java.increaseVersion();
		java.replaceAndSave("return 1;", "return 4;"); 
		AgentClass.reloadClasses(); 
		Assert.assertEquals(q2.findOne(),4); 
	}
	
	@Test
	public void testQuery4(){
		Assert.assertEquals(q4.findOne(),4);
	}
	
	@Test
	public void testQuery5(){
		Assert.assertEquals(q5.findOne(),5);
	}
}
