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

import java.util.Properties;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.tools.oracle.ddl.Drops;

import test.com.tsc9526.monalisa.TestConstants;
import test.com.tsc9526.monalisa.orm.dialect.basic.BaseRecordTest;
import test.com.tsc9526.monalisa.orm.dialect.oracle.OracleDB; 

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class OracleRecordTest extends BaseRecordTest implements OracleDB{ 
	private static final long serialVersionUID = -1974865252589672370L;
  
	@Override
	public DBConfig getDB() {
		return OracleDB.DB;
	}

	@Override
	public String getInitSqls() {
		return ""+/**~{*/""
				+ "CREATE TABLE \"TEST_RECORD\" ("
				+ "\r\n	\"RECORD_ID\"   NUMBER(11,0)                  NOT NULL ENABLE, "
				+ "\r\n	\"NAME\"        NVARCHAR2(128)   DEFAULT 'N'  NOT NULL ENABLE, "
				+ "\r\n	\"TITLE\"       NVARCHAR2(128), "
				+ "\r\n	\"TS_A\"        DATE, "
				+ "\r\n	\"VERSION\"     NUMBER(11,0)     DEFAULT 0    NOT NULL ENABLE, "
				+ "\r\n	\"CREATE_TIME\" DATE                          NOT NULL ENABLE, "
				+ "\r\n	\"CREATE_BY\"   NVARCHAR2(64), "
				+ "\r\n	\"UPDATE_TIME\" DATE, "
				+ "\r\n	\"UPDATE_BY\"   NVARCHAR2(64), "
				+ "\r\n	 PRIMARY KEY (\"RECORD_ID\")"
				+ "\r\n);"
				+ "\r\n"
			 + "\r\n	CREATE sequence SEQ_TEST_RECORD minvalue 1  start with 2 increment by 1 cache 20;"
			 + "\r\n"
				+ "\r\nINSERT INTO test_record(\"RECORD_ID\",\"NAME\",\"TITLE\",TS_A,CREATE_TIME)VALUES(1,'hello','record',sysdate,sysdate);"
		+ "\r\n"/**}*/;
	}

	@Override
	public String getCleanSqls() {
		return Drops.dropTableIfExists("TEST_RECORD")+SQL_SPLIT+"\r\n"
			+  Drops.dropSequenceIfExists("SEQ_TEST_RECORD");
	}
	 
	
	protected Properties getConfigProperties() {
		Properties p=new Properties();
		p.put(DbProp.PROP_DB_URL.getFullKey(),                TestConstants.oracleUrl);
		p.put(DbProp.PROP_DB_DATASOURCE_CLASS.getFullKey(),   TestConstants.datasourceClass);
		p.put(DbProp.PROP_DB_USERNAME.getFullKey(),           TestConstants.username);
		p.put(DbProp.PROP_DB_PASSWORD.getFullKey(),           TestConstants.password);
		
		p.put("sql.debug", TestConstants.DEBUG_SQL);
		return p;
	}
}
