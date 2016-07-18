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
import java.sql.Statement;

import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class KeysExecutor extends RelationExecutor implements Execute<Integer>{
	private boolean autoKey=false;
	private Model<?> model;
	
	public KeysExecutor(Model<?> model){
		this.model=model;
		
		FGS fgs=model.autoField();
		if(fgs!=null && fgs.getObject(model)==null){
			autoKey=true;
		}
	}
	
	public PreparedStatement preparedStatement(Connection conn, String sql)throws SQLException {
		if(autoKey){
			return conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		}else{
			return conn.prepareStatement(sql);
		}
	}
	 
	public Integer execute(PreparedStatement pst) throws SQLException {
		int r=pst.executeUpdate();
	 
		if(autoKey){
			ResultSet rs = pst.getGeneratedKeys();   
            if (rs.next()) {  
                Long id = rs.getLong(1);   
                model.autoField().setObject(model, id.intValue()); 
            }  
            rs.close();
		}			

		return r;
	}
	 
}