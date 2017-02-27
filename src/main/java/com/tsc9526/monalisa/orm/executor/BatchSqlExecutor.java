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
package com.tsc9526.monalisa.orm.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class BatchSqlExecutor extends RelationExecutor implements Execute<int[]>{
	static Logger logger=Logger.getLogger(BatchSqlExecutor.class);
	
	protected List<String> sqls=new ArrayList<String>();
 	
	public BatchSqlExecutor(String[] sqls){
		for(String s:sqls){
			s=s.trim();
			if(s.length()>0){
				this.sqls.add(s);
			}
		}
	}
	
	public int[] execute(Connection conn,PreparedStatement pst) throws SQLException {
		Statement st=null;
		try{
			st=conn.createStatement();
			for(String sql:sqls){
				st.addBatch(sql);
			}
			int[] rs=st.executeBatch();
		
			return rs;
		}catch(SQLException e){
			throw new RuntimeException("Execute batch sql exception: "+e+"\r\n SQLs:"+sqls,e);
		}finally{
			MelpClose.close(st);
		}
	}

	public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {	
		return null;
	}	 
}
