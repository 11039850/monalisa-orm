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
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.tools.string.MelpSQL;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class BatchStatementExecutor extends RelationExecutor implements Execute<int[]>{
	protected List<List<Object>> batchParameters=new ArrayList<List<Object>>();
	
	public BatchStatementExecutor(List<List<Object>> batchParameters){
		this.batchParameters=batchParameters;
	}
	
	public int[] execute(PreparedStatement pst) throws SQLException {
		for(List<Object> p:batchParameters){
			MelpSQL.setPreparedParameters(pst, p);
			pst.addBatch();
		}		 
		int[] result=pst.executeBatch();
		return result;
	}

	public PreparedStatement preparedStatement(Connection conn,String sql)throws SQLException {	
		return conn.prepareStatement(sql);
	}	 
}
