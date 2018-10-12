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
package test.com.tsc9526.monalisa.orm.dialect.oracle.cases;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.model.ModelMeta;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.tools.oracle.ddl.Drops;

import test.com.tsc9526.monalisa.orm.dialect.oracle.OracleDB;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ReloadTableStructTest {
	private String testTableName = "MONALISA.TEST_RELOAD";
	private String testSeqName   = "MONALISA.SEQ_TEST_RELOAD";
	
	@BeforeTest
	public void beforeTest() {
		String dropTableSql = Drops.dropTableIfExists(testTableName);
		String createTableSql=""+/**~!{*/""
			+ "CREATE TABLE " +(testTableName)+ " ("
			+ "\r\n	\"id\"    INTEGER       NOT NULL ,"
			+ "\r\n	\"value\" VARCHAR2(255)     NULL ,"
			+ "\r\n	PRIMARY KEY (\"id\")"
			+ "\r\n)"
		+ "\r\n"/**}*/;
		
		OracleDB.DB.execute(dropTableSql);
		OracleDB.DB.execute(createTableSql);
		
		String dropSeqSql    = Drops.dropSequenceIfExists(testSeqName);
		OracleDB.DB.execute(dropSeqSql);
	}
	
	
	public void testAddSeq() {
		Record r=OracleDB.DB.createRecord("TEST_RELOAD");
		r.set("value","testValue");
		try {
			r.save(); //will throw Exception: "id" NOT NULL
			Assert.fail("field: id cannot be null!");
		}catch(Exception e) {
			Assert.assertTrue(e.getMessage().indexOf("ORA-01400")>=0); 
		}
		
		String createSeqSql  = "create sequence "+testSeqName+" start with 1";
		OracleDB.DB.execute(createSeqSql);
		
		
		ModelMeta mm=ModelMeta.getModelMeta(r);
		Assert.assertTrue(mm.checkChanged());
		
		int n = r.save();
		Assert.assertEquals(n,1);
		Assert.assertTrue(r.getInt("id",0)>0);
	}
 
	 
}
