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

import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpSQL;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class BatchStatementExecutor extends HandlerRelation implements Execute<int[]>{
 
	public BatchStatementExecutor(){ 
	}
	
	@SuppressWarnings("unchecked")
	public int[] execute(Connection conn,String sql,List<?> parameters) throws SQLException {
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			for(Object px:parameters){
				if(px.getClass().isArray()) {
					List<Object> xs=new ArrayList<Object>();
					Object[] os = (Object[])px;
					for(Object o:os) {
						xs.add(o);
					}
					MelpSQL.setPreparedParameters(pst, xs);
				}else if(px instanceof List) {
					MelpSQL.setPreparedParameters(pst, (List<Object>)px);
				}else {
					throw new SQLException("Invalid paramete type: "+px.getClass().getName()+", array or list required!");
				}
				pst.addBatch();
			}		 
			int[] result=pst.executeBatch();
			return result;
		}finally {
			MelpClose.close(pst);
		}
	}
}
