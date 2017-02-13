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
package test.com.tsc9526.monalisa.orm;

import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;
import test.com.tsc9526.monalisa.orm.mysql.TestGenterator;

import com.tsc9526.monalisa.orm.compiler.SQLGenerator;
import com.tsc9526.monalisa.orm.generator.DBGeneratorLocal;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class InitTestDatabase {
	static Logger logger=Logger.getLogger(InitTestDatabase.class);
	
	public static void main(String[] args)throws Exception{
		initDatebase();
		generateModels();  
	}
	
	private static void initDatebase()throws Exception{
		logger.info("Init test database ...");
	 	for(String table:MysqlDB.DB.getTables()){
			if(table.toLowerCase().startsWith("test_")){
				logger.info(" DROP TABLE `"+table+"`");
				MysqlDB.DB.execute("DROP TABLE `"+table+"`");
			}
		}
	 	 
		String sql=MelpFile.readToString(TestGenterator.class.getResourceAsStream("/mysql-create.sql"),"utf-8");
		for(String x:sql.split(";")){
			x=x.trim();
			if(x.length()>0){
				logger.info(x+"\r\n\r\n");
				MysqlDB.DB.execute(x);
			}
		}
	}
	
	public static void generateModels(){
		logger.info("Generator test models ...");
		
		String outputJavaDir="src/test/java";
		String outputResourceDir="src/test/resources";
		DBGeneratorLocal g1=new DBGeneratorLocal(MysqlDB.class, outputJavaDir,outputResourceDir);
		g1.generateFiles();
		
		SQLGenerator g2=new SQLGenerator(outputJavaDir, outputResourceDir);
		g2.generateFiles();
	}
}
