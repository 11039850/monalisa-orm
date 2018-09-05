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
package test.com.tsc9526.monalisa.orm.dialect.oracle;

import test.com.tsc9526.monalisa.TestConstants;

import com.tsc9526.monalisa.main.DBModelGenerateMain;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DBConfig;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@DB(
	url=TestConstants.oracleUrl,
	username=TestConstants.username,
	password=TestConstants.password, 
	datasourceClass="C3p0DataSource",properties={
		"sql.debug = "+TestConstants.DEBUG_SQL	
	})
public interface OracleDB {
	public static DBConfig DB=DBConfig.fromClass(OracleDB.class);
	
	public static class Generate{
		public static void main(String[] args) throws Exception{
			DBModelGenerateMain.generateModelClass(OracleDB.class,"src/test/java");
		}
	}
}
