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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpSQL;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ResultSetsExecutor<T>  extends HandlerRelation implements Execute<List<DataTable<T>>> { 
	private int deepth;	 
	private HandlerResultSet<T> resultHandler;
	
	public ResultSetsExecutor(HandlerResultSet<T> resultHandler,int deepth){
		this.resultHandler = resultHandler;
		this.deepth        = deepth;
	}
	
	public List<DataTable<T>>  execute(Connection conn,String sql,List<?> parameters) throws SQLException {				 
		PreparedStatement pst = null;
		ResultSet         rs  = null;		
		
		List<DataTable<T>> result=new ArrayList<DataTable<T>>();
		
		try {
			pst = conn.prepareStatement(sql);
			MelpSQL.setPreparedParameters(pst, parameters);
			
			boolean x=pst.execute();
			
			if(deepth>0){
				rs=pst.getResultSet();
				
				for(int i=0;i<deepth;i++){
					if(rs!=null){
						addResult(result,rs);
						
						rs.close();
						rs=null;
					}
	
					if(pst.getMoreResults()){
						rs = pst.getResultSet();
					}
				}
			}else if(deepth==0){
				while(x){
					rs=pst.getResultSet();
					
					addResult(result,rs);
				 	
					rs.close();
					rs=null;
					
					x=pst.getMoreResults();
				}
			}
			
			return result;
		}finally {
			MelpClose.close(pst,rs);
		}
	}
	
	private void addResult(List<DataTable<T>> result,ResultSet rs)throws SQLException{
		setupRelationTables(rs);
				
		DataTable<T> r=new DataTable<T>();
		while(rs.next()){	
			T m=resultHandler.createResult(rs);
			r.add(m);
		}	
		result.add(r);
	}
 
}
