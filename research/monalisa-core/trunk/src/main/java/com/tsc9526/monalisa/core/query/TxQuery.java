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
package com.tsc9526.monalisa.core.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tsc9526.monalisa.core.datasource.DBConfig;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TxQuery {	 
	private Map<String, CI> hcs=new HashMap<String, CI>();
	
	TxQuery(){}
	
	private int level=-1;
	
	private String txid=UUID.randomUUID().toString().replace("-","").toLowerCase();
	
	public String getTxid(){
		return txid;
	}
	
	public Connection getConnection(DBConfig db) throws SQLException{		 
		String key=db.getKey();
		 
		CI ci=hcs.get(key);
		if (ci==null) {
			Connection conn=db.getDataSource().getConnection();
			ci=new CI(conn);
			conn.setAutoCommit(false);
			
			if(level>-1){
				conn.setTransactionIsolation(level);
			}
			
			hcs.put(key, ci);
		}
		return ci.conn;
	}
	
	public void setTransactionIsolation(int level)throws SQLException{
		this.level=level;
		for(CI ci:hcs.values()){
			ci.conn.setTransactionIsolation(level);
		}
	}
	
	public void commit()  throws SQLException{
		for(CI ci:hcs.values()){
			ci.conn.commit();			
		} 
	}

	public void rollback(){
		try{
			for(CI ci:hcs.values()){
				ci.conn.rollback();			
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		} 	
	}
	
	public void close(){
		try{
			for(CI ci:hcs.values()){
				ci.conn.setAutoCommit(ci.autoCommit);
				ci.conn.close();
			}
		}catch(SQLException e){
			throw new RuntimeException(e);		
		}finally{
			hcs.clear();
		}
	}
	
	class CI{
		Connection conn;
		boolean  autoCommit;
		
		CI(Connection conn) throws SQLException{
			this.conn=conn;
			this.autoCommit=conn.getAutoCommit();
		}
	}

}
