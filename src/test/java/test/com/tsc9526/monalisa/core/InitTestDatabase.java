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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import test.com.tsc9526.monalisa.core.mysql.MysqlDB;
import test.com.tsc9526.monalisa.core.mysql.TestGenterator;

import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.FileHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class InitTestDatabase {
	static Logger logger=Logger.getLogger(InitTestDatabase.class);
	
	public static void main(String[] args)throws Exception{
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
}
