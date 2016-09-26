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
package com.tsc9526.monalisa.orm.service;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBS {
	static Logger logger=Logger.getLogger(DBS.class);
	
	private static DataMap dbs=new DataMap();
	
	public static void add(String dbName,DBConfig db){
		logger.info("Add database service: /"+dbName+", dbkey: "+db.getKey());
		dbs.put(dbName,db);
	}

	public static DBConfig getDB(String dbName){
		return (DBConfig)dbs.get(dbName);	 
	}
}
