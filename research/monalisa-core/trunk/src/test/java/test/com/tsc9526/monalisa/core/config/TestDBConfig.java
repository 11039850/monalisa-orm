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
package test.com.tsc9526.monalisa.core.config;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.ConfigClass;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.query.model.Record;

@Test
public class TestDBConfig {

	@DB(configClass=DB01.class)
	public static class DB01 extends ConfigClass{
	 	public Properties getConfigProperties() {
			Properties p=new Properties();
			p.put(DbProp.PROP_DB_URL.getFullKey(),        "jdbc:mysql://127.0.0.1:3306/test_monalisa");
			p.put(DbProp.PROP_DB_USERNAME.getFullKey(),   "monalisa");
			p.put(DbProp.PROP_DB_PASSWORD.getFullKey(),   "monalisa");
			p.put(DbProp.PROP_DB_PARTITIONS.getFullKey(), "test_logyyyymm_{DatePartitionTable(yyyyMM,log_time)}");
			return p;
		}
	}
	
	public void testConfigClass()throws Exception{
		DBConfig db=DBConfig.fromClass(DB01.class);
		
		Record record=new Record("test_table_1");
		record.use(db);
		
		long c= record.SELECT().count();
		System.out.println("Count: "+c);	
	}
}
