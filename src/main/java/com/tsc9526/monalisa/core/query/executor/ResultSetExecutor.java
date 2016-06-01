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
package com.tsc9526.monalisa.core.query.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tsc9526.monalisa.core.query.ResultHandler;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.tools.CloseQuietly;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ResultSetExecutor<T> implements Execute<DataTable<T>>{
	private ResultHandler<T> resultHandler;
	
	public ResultSetExecutor(ResultHandler<T> resultHandler){
		this.resultHandler=resultHandler;
	}
	
	public DataTable<T> execute(PreparedStatement pst) throws SQLException {		
		DataTable<T> result=new DataTable<T>();
		ResultSet rs=null;
		try{
			rs=pst.executeQuery();				 		
			while(rs.next()){
				T r=resultHandler.createResult(rs); 
				result.add(r);					
			}
			return result;
		}finally{
			CloseQuietly.close(rs);
		}
	} 
	
	public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {				 
		return conn.prepareStatement(sql);
	}	
}
