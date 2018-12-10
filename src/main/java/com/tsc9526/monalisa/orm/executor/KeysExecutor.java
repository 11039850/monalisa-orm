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
import java.sql.Statement;
import java.util.List;

import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpSQL;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class KeysExecutor extends HandlerRelation implements Execute<Integer>{
	private boolean autoKey=false;
	private Model<?> model;
	
	public KeysExecutor(Model<?> model){
		this.model=model;
		
		FGS fgs=model.autoField();
		if(fgs!=null && fgs.getObject(model)==null){
			autoKey=true;
		}
	}
	 
	 
	public Integer execute(Connection conn,String sql,List<?> parameters) throws SQLException {
		PreparedStatement pst = null;
		ResultSet         rs  = null;
		
		try {
			if(autoKey && model.dialect().supportAutoIncrease()){
				pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			}else{
				pst = conn.prepareStatement(sql);
			}
			
			MelpSQL.setPreparedParameters(pst, parameters);
			
			int r=pst.executeUpdate();
		 
			if(autoKey && model.dialect().supportAutoIncrease()){
				rs = pst.getGeneratedKeys();   
	            if (rs.next()) {  
	                Long id = rs.getLong(1);   
	                model.autoField().setObject(model, id.intValue()); 
	            }  
			}			
	
			return r;
		}finally {
			MelpClose.close(pst,rs);
		}
	}
	 
}