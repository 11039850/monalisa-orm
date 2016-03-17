package test.com.tsc9526.monalisa.core.mysql;

 
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.mysql.mock.MockProcessingEnvironment;
import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestLogyyyymm;
import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable1;

import com.tsc9526.monalisa.core.generator.DBGeneratorLocal;
import com.tsc9526.monalisa.core.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.query.Tx;
import com.tsc9526.monalisa.core.tools.FileHelper;
 
@Test
public class TestGenterator {
	static Logger logger=Logger.getLogger(TestGenterator.class.getSimpleName());
	   
	@BeforeClass
	public void init()throws Exception{
		logger.info("Init database ...");
	 	
		Connection conn=MysqlDB.DB.getDataSource().getConnection();
		boolean autoCommit=conn.getAutoCommit();
		conn.setAutoCommit(true);
		
		List<String> tables=new ArrayList<String>();
		 
		DatabaseMetaData dbm=conn.getMetaData();
		ResultSet rs=dbm.getTables("", "","%", new String[]{"TABLE"});
		while(rs.next()){
			String table=rs.getString("TABLE_NAME");
			tables.add(table);
		}
		rs.close(); 
		
		if(tables.size()>0){
			logger.info("Begin drop tables: "+tables.size());
			Statement statement=conn.createStatement();
			for(String table:tables){
				statement.execute("DROP TABLE `"+table+"`");
				logger.info(" DROP TABLE `"+table+"`");
			}
			
			statement.close();
		}
		
		 
		String sql=FileHelper.readToString(TestGenterator.class.getResourceAsStream("/mysql-create.sql"),"utf-8");
		Statement statement=conn.createStatement();
		for(String x:sql.split(";")){
			x=x.trim();
			logger.info(x+"\r\n\r\n");
			statement.execute(x);
		}
		statement.close();
		
		conn.setAutoCommit(autoCommit);
		conn.close();
	}
	 
	
	public void testGenteratorLocal()throws Exception{
		String pkg=MysqlDB.class.getPackage().getName()+"."+MysqlDB.class.getSimpleName().toLowerCase();
		pkg=pkg.replace(".","/");
		
		String outputJavaDir="src/test/java";
		String outputResourceDir="src/test/resources";
	  
		FileHelper.delete(new File(outputJavaDir+"/"+pkg), true);
		FileHelper.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
	
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
	  
		FileHelper.delete(new File(outputJavaDir+"/"+pkg), true);
		FileHelper.delete(new File(outputResourceDir+"/resources/"+pkg), true); 
	
		Assert.assertTrue(new File(outputResourceDir+"/resources/"+pkg+"/"+CreateTable.FILE_NAME).exists()==false);
		
		MockProcessingEnvironment mpe=new MockProcessingEnvironment(MysqlDB.class);
		 
		DBGeneratorProcessing g=new DBGeneratorProcessing(mpe.createProcessingEnvironment(),mpe.createTypeElement());
		g.generateFiles();
		 
		TestLogyyyymm log=new TestLogyyyymm();
		log.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-03-01 01:01:01"));
		log.save();
		
		int maxId=MysqlDB.DB.selectOne("select max(id) from test_table_1").getInt(0,0); 
		
		Tx.putUserContext("zzg.zhou");
		TestTable1 t1=new TestTable1();
		t1.defaults();	
		t1.save();
		
		Assert.assertEquals(t1.getId().intValue(), maxId+1);
		Assert.assertEquals(t1.getCreateBy(),"zzg.zhou");
		
		t1=new TestTable1(maxId+1);
		Assert.assertTrue(!t1.entity());
		
		t1=new TestTable1(maxId+1).load();
		Assert.assertTrue(t1.entity());
		
		t1=new TestTable1(maxId+2).load();
		Assert.assertTrue(!t1.entity());
		
	}
}
