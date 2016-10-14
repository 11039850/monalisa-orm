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
package com.tsc9526.monalisa.http.action;

import com.tsc9526.monalisa.http.DBS;
import com.tsc9526.monalisa.http.Response;
import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Action {
	protected Logger logger=Logger.getLogger(this.getClass());
	 
	protected ActionArgs args;
	protected DBConfig   db;
	
	public Action(ActionArgs args){
		this.args=args;
		
		if(args!=null){
			DBS dbs=args.getDBS();
			if(dbs!=null){
				db=dbs.getDB();
			}
		}
	}
	 
	protected Query createQuery(){
		return db.createQuery();
	}
	
	protected Record createRecord(){
		return db.createRecord(args.getTable());
	}
	
	/**
	 * 
	 * @return null if verify is OK, otherwise return the response with some error message
	 */
	public Response verify(){
		return null;
	}
	
	/**
	 * default column prefix  is: "~the_table_name."
	 * 
	 * @param table: name of the table
	 * 
	 * @return column name prefix
	 */
	protected String getTableMapping(String table){
		return "~"+table+".";
	}
	
	public abstract Response getResponse();
	
}
