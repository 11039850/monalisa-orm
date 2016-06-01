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
package test.com.tsc9526.monalisa.core;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import test.com.tsc9526.monalisa.core.mysql.MysqlDB;

import com.tsc9526.monalisa.core.generator.DBGeneratorLocal;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.parser.executor.SQLGenerator;
import com.tsc9526.monalisa.core.parser.executor.SQLResourceManager;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class GeneratorRun {
	static Logger logger=Logger.getLogger(GeneratorRun.class);
	
	public static void main(String[] args)throws Exception {
		File sqlFile=new File("sql/mysqldb/test1.jsp");
		long fileTime=0;
	 	
		String outputJavaDir="src/test/java";
		String outputResourceDir="src/test/resources";
		DBGeneratorLocal g1=new DBGeneratorLocal(MysqlDB.class, outputJavaDir,outputResourceDir);
		g1.generateFiles();
		
		SQLGenerator g2=new SQLGenerator(outputJavaDir, outputResourceDir);
		g2.generateFiles();
		
		while(true){
			if(fileTime!=sqlFile.lastModified()){
				if(fileTime>0){
					System.out.println("Reload file: "+sqlFile.getAbsolutePath());
				}
				fileTime=sqlFile.lastModified();
				
				Query query=SQLResourceManager.getInstance().createQuery("test.com.tsc9526.monalisa.core.sql.Q0001.testFindAll_A","name","","");
				System.out.println(query.getExecutableSQL());
				DataTable<DataMap> rs=query.getList();
				System.out.println("Total results: "+rs.size());
				for(DataMap x:rs){
					System.out.println(x.toString());
				}
			}else{
				Thread.sleep(1000);
				
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("ts_a",new Date());
				map.put("title","test tile");
				map.put("extra_1","good");
				 
			}
		}
	}
	
	 
}
