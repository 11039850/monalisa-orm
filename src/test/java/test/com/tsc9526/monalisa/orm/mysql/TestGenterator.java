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
package test.com.tsc9526.monalisa.orm.mysql;

 
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.data.ColumnData;
import test.com.tsc9526.monalisa.orm.mysql.mock.MockProcessingEnvironment;
import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestLogyyyymm;
import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestTable1;
import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestTable2;

import com.tsc9526.monalisa.orm.Tx;
import com.tsc9526.monalisa.orm.generator.DBGeneratorLocal;
import com.tsc9526.monalisa.orm.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class TestGenterator {
	static Logger logger=Logger.getLogger(TestGenterator.class.getSimpleName());
	 
	
	public void testGenteratorLocal()throws Exception{
		String pkg=MysqlDB.class.getPackage().getName()+"."+MysqlDB.class.getSimpleName().toLowerCase();
		pkg=pkg.replace(".","/");
		
		String outputJavaDir="src/test/java";
		String outputResourceDir="src/test/resources";
	  
		MelpFile.delete(new File(outputJavaDir+"/"+pkg), true);
		MelpFile.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
	
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists()==false);
		
		DBGeneratorLocal g=new DBGeneratorLocal(MysqlDB.class, outputJavaDir,outputResourceDir);
		g.generateFiles();
		
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists());
		
		TestLogyyyymm log=new TestLogyyyymm();
		log.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-01 01:01:01"));
		log.save();
	}
	
	public void testGenteratorProcessing()throws Exception{
		String pkg=MysqlDB.class.getPackage().getName()+"."+MysqlDB.class.getSimpleName().toLowerCase();
		pkg=pkg.replace(".","/");
		
		String outputJavaDir="src/test/java";
		String outputResourceDir="src/test/resources";
	  
		MelpFile.delete(new File(outputJavaDir+"/"+pkg), true);
		MelpFile.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
	
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists()==false);
		
		MockProcessingEnvironment mpe=new MockProcessingEnvironment(MysqlDB.class);
		 
		DBGeneratorProcessing g=new DBGeneratorProcessing(mpe.createProcessingEnvironment(),mpe.createTypeElement());
		g.generateFiles();
		 
		TestLogyyyymm log=new TestLogyyyymm();
		log.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-03-01 01:01:01"));
		log.save();
		
		int maxId=MysqlDB.DB.selectOne("select max(id) as x from test_table_1").getInt("x",0); 
		
		Tx.putContext(Tx.CONTEXT_CURRENT_USERID,"zzg.zhou");
		TestTable1 t1=new TestTable1();
		t1.defaults();	
		t1.save();
		
		Assert.assertEquals(t1.getId().intValue(), maxId+1);
		Assert.assertEquals(t1.getCreateBy(),"zzg.zhou");
		
		t1=new TestTable1(maxId+1);
		Assert.assertTrue(!t1.entity());
		t1=new TestTable1(maxId+1).load();
		Assert.assertNotNull(t1.load());
		Assert.assertTrue(t1.entity());
		
		TestTable1 t2=new TestTable1(maxId+2);
		Assert.assertTrue(!t2.entity());
		Assert.assertNull(t2.load());
		Assert.assertTrue(!t2.entity());
		
		Map<Integer,TestTable1> ms=TestTable1.WHERE().id.ge(0).SELECT().selectToMap();
		Assert.assertTrue(ms.size()>0);
		
	}
	
	
	public void testInsertArrays()throws Exception{
		int[] i1=new int[]{3,2,1};
		String[] s1=new String[]{"3","2"};
		
		TestTable2 t2=new TestTable2();
		t2.defaults();
		t2.setObj(new ColumnData());
		t2.setArrayInt(i1);
		t2.setArrayString(s1);
		t2.save();
		
		TestTable2 t2x=TestTable2.SELECT().selectByPrimaryKey(t2.getId());
		Assert.assertEquals(t2x.getObj(),t2.getObj());
		
		for(int i=0;i<i1.length;i++){
			Assert.assertEquals(t2x.getArrayInt()[i], i1[i]);
		}
		for(int i=0;i<s1.length;i++){
			Assert.assertEquals(t2x.getArrayString()[i], s1[i]);
		}
	}
}
