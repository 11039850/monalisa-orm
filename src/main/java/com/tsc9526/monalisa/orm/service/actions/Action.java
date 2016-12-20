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
package com.tsc9526.monalisa.orm.service.actions;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.DBS;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Action {
	protected Logger logger=Logger.getLogger(this.getClass());
	 
	protected ModelArgs args;
	protected DBConfig   db;
	
	public Action(ModelArgs args){
		this.args=args;
		
		if(args!=null){
			DBS dbs=args.getDBS();
			if(dbs!=null){
				db=dbs.getDB();
			}
		}
	}
	
	public ModelArgs getActionArgs(){
		return this.args;
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

	protected void appendFilters(Query query, boolean and){
		for(String[] fs:args.getFilters()){
			if(and){
				query.add(" AND ");
			}
			
			char c=fs[2].length()>0 ? fs[2].charAt(0) : 0;
			
			if(fs[1].equals("=") && c=='(' || c=='['){
				String   v=fs[2].substring(1,fs[2].length()-1);
				String[] s=v.split(",");
				
				if(c=='('){
					query.add(fs[0]).in(s);
				}else{
					query.add(fs[0]+" BETWEEN ? AND ?", s[0],s[1]);
				}
			}else{
				query.add(fs[0]+" "+fs[1]+" ?",fs[2]);
			}
			
			and=true;
		}
	}
	
	public abstract Response getResponse();
	
}
