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
package test.com.tsc9526.monalisa.core.agent;
 

 
import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.sqlfiles.Q1;
import test.com.tsc9526.monalisa.core.sqlfiles.Q2;
import test.com.tsc9526.monalisa.core.sqlfiles.Q3;
import test.com.tsc9526.monalisa.core.sqlfiles.Q4;

import com.tsc9526.monalisa.core.agent.AgentClass;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.FileHelper;
 

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class QueryTest {
	static Logger logger=Logger.getLogger(QueryTest.class);

	@BeforeTest
	public void setUp()throws Exception{
		File dir=new File("src/test/java/test/com/tsc9526/monalisa/core/sqlfiles");
		for(File src:dir.listFiles()){
			if(src.isFile() && src.getName().endsWith(".java")){
				File target=new File(DbProp.CFG_SQL_PATH+"/"+src.getName());
				FileHelper.copy(src, target);
				target.setLastModified(src.lastModified());
			}
		}
		
		initJavaSources("Q3");
		initJavaSources("Q4");
		 
		Assert.assertEquals(Query.create(Q1.class).findOne(),1);
		Assert.assertEquals(Query.create(Q2.class).findOne(),1);
		Assert.assertEquals(Query.create(Q3.class).findOne(),226);
		Assert.assertEquals(Query.create(Q4.class).findOne(),226);
	}
	
	private void initJavaSources(String name)throws Exception{
		File java=new File("src/test/java/test/com/tsc9526/monalisa/core/sqlfiles/"+name+".java");
		String source=FileHelper.readToString(java,"utf-8");
		 
		logger.info("Change "+java.getAbsolutePath()+", with version: 226");
		String r=source.replace("return 1;", "return 226;").replace("$VERSION=1;","$VERSION=226;");
		FileHelper.write(new File(DbProp.CFG_SQL_PATH+"/"+name+".java"), r.getBytes("utf-8"));
	}
	
	@Test
	public void testQuery01()throws Exception {
		File java=new File(DbProp.CFG_SQL_PATH+"/Q1.java");
		String source=FileHelper.readToString(java,"utf-8");
	 
		logger.info("Change "+java.getAbsolutePath()+", return 2");
		String r=source.replace("return 1;", "return 2;");
		FileHelper.write(java, r.getBytes("utf-8"));
		AgentClass.reloadClasses(); 
		
		Q1 q1=Query.create(Q1.class);
		Assert.assertEquals(q1.findOne(),2);
		  
		logger.info("Change "+java.getAbsolutePath()+", return 3");
		r=source.replace("return 1;", "return 3;");
		FileHelper.write(java, r.getBytes("utf-8"));
		AgentClass.reloadClasses(); 
		
		Assert.assertEquals(q1.findOne(),3); 	
		 
	}
	
	@Test
	public void testQueryWithVersion()throws Exception {
		File java=new File(DbProp.CFG_SQL_PATH+"/Q2.java");
		String source=FileHelper.readToString(java,"utf-8");
		
		logger.info("Change "+java.getAbsolutePath()+", return 2 whitout version change");
		String r=source.replace("return 1;", "return 2;");
		FileHelper.write(java, r.getBytes("utf-8"));
		AgentClass.reloadClasses(); 
		
		Q2 q2=Query.create(Q2.class);
		Assert.assertEquals(q2.findOne(),1);
		  
		
		logger.info("Change "+java.getAbsolutePath()+", return 3 with version 3");
		r=source.replace("return 1;", "return 3;").replace("$VERSION=1;","$VERSION=3;");
		 
		FileHelper.write(java, r.getBytes("utf-8"));
		AgentClass.reloadClasses(); 
		
		Assert.assertEquals(q2.findOne(),3); 
		
		q2=Query.create(Q2.class);
		logger.info("Change "+java.getAbsolutePath()+", return 4 with version 4");
		r=source.replace("return 1;", "return 4;").replace("$VERSION=1;","$VERSION=4;");
		 
		FileHelper.write(java, r.getBytes("utf-8"));
		AgentClass.reloadClasses(); 
		
		Assert.assertEquals(q2.findOne(),4); 
	}
	
}
