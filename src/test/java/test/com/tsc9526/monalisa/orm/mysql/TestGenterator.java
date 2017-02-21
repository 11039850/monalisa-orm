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

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.InitTestDatabase;
import test.com.tsc9526.monalisa.orm.mysql.mock.MockProcessingEnvironment;

import com.tsc9526.monalisa.orm.datasource.DbProp;
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
	 
	private String outputJavaDir    =DbProp.TMP_WORK_DIR_GEN+"/src/test/java";
	private String outputResourceDir=DbProp.TMP_WORK_DIR_GEN+"/src/test/resources";
	
	private String pkg=MysqlDB.class.getName().toLowerCase().replace(".","/");
	
	@BeforeClass
	public void before()throws Exception{
		InitTestDatabase.initDatebase();
	}
	
	public void testGenteratorLocal()throws Exception{
		MelpFile.delete(new File(outputJavaDir+"/"+pkg), true);
		MelpFile.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
	
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists()==false);
		
		DBGeneratorLocal g=new DBGeneratorLocal(MysqlDB.class, outputJavaDir,outputResourceDir);
		g.generateFiles();
		
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists());
		
		checkJavaCode();
	}
	
	public void testGenteratorProcessing()throws Exception{
		MelpFile.delete(new File(outputJavaDir+"/"+pkg), true);
		MelpFile.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists()==false);
		
		MockProcessingEnvironment mpe=new MockProcessingEnvironment(MysqlDB.class,outputJavaDir,outputResourceDir);
		 
		DBGeneratorProcessing g=new DBGeneratorProcessing(mpe.createProcessingEnvironment(),mpe.createTypeElement());
		g.generateFiles();
		  
		checkJavaCode();
	}
	
	private void checkJavaCode(){
		String sourceDir=outputJavaDir+"/"+pkg;
		String expectDir="src/test/java/"+pkg;
		
		File dirSource=new File(sourceDir);
		for(File fs:dirSource.listFiles()){
			File fe=new File(expectDir,fs.getName());
			
			String ss=MelpFile.readToString(fs, "utf-8");
			String se=MelpFile.readToString(fe, "utf-8");
			if(ss.equals(se)){
				logger.info("Check[OK] generate java file: "+fs.getName()+", expect: "+expectDir+"/"+fs.getName());
			}else{
				logger.error("Check[FAIL] generate java file: "+fs.getName()+", expect: "+expectDir+"/"+fs.getName());
			}
			Assert.assertTrue(ss.equals(se));
		}
		
	}
}
