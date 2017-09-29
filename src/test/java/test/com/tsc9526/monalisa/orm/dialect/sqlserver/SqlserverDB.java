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
package test.com.tsc9526.monalisa.orm.dialect.sqlserver;
 
import test.com.tsc9526.monalisa.orm.mysql.TestGenterator;

import com.tsc9526.monalisa.main.DBModelGenerateMain;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.tools.io.MelpFile;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@DB(url="jdbc:sqlserver://127.0.0.1:1433;databaseName=test_monalisa",username="monalisa",password="monalisa")
public interface SqlserverDB {
	public static DBConfig DB=DBConfig.fromClass(SqlserverDB.class);
	
	public static class Generate{
		public static void main(String[] args) throws Exception{
			//initDatebase();
			
			DBModelGenerateMain.generateModelClass(SqlserverDB.class,"src/test/java");
		}
		
		public static void initDatebase()throws Exception{
			for(String table:DB.getTables()){
				if(table.toLowerCase().startsWith("test_")){
					 
					DB.execute("DROP TABLE `"+table+"`");
				}
			}
		 	 
			String sql=MelpFile.readToString(TestGenterator.class.getResourceAsStream("/create-sqlserver.sql"),"utf-8");
			DB.execute(sql); 
		}
	}
	
	
	
}
