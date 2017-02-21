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
package com.tsc9526.monalisa.service;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.service.actions.ActionLocator;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBS {
	static Logger logger = Logger.getLogger(DBS.class);
 	
	public static DataMap dbs = new DataMap();
  	 
	public static void add(String dbName, DBConfig db) {
		add(dbName, db, new ActionLocator(),"");
	}

	public synchronized static void add(String dbName, DBConfig db, ActionLocator locator,String describe) {
		dbName = Dialect.getRealname(dbName);

		if (!dbs.containsKey(dbName)) {
			DBS s = new DBS(dbName, db, locator);

			logger.info("Add DB service: "+ dbName+"["+describe+"], HTTP: " + locator.getHttpMethods() + ", SQL: "+locator.getSQLMethods()+", dbkey: " + db.getKey());
			dbs.put(dbName, s);
		} else {
			logger.error("DB service: "+ dbName+"["+describe+"] already exists, dbkey: " + db.getKey());
		}
	}

	public static DBS getDB(String dbName) {
		dbName = Dialect.getRealname(dbName);

		return (DBS) dbs.get(dbName);
	}
	
	public static List<String> getNames(){
		List<String> names=new ArrayList<String>();
		for(Object db:dbs.values()){
			names.add( ((DBS)db).getDbName());
		}
		
		return names;
	}
	
	public static void remove(String dbName) {
		dbName = Dialect.getRealname(dbName);

		DBS s = (DBS) dbs.remove(dbName);
		if (s != null) {
			logger.info("Removed DB service, HTTP: " + s.locator.getHttpMethods()+ ", SQL: "+s.locator.getSQLMethods()+" : /" + dbName);
		}
	}

	public static void clear() {
		dbs.clear();
	}
 

	private String dbName;

	private DBConfig db;

	private ActionLocator locator;
 
	private DBS(String dbName, DBConfig db, ActionLocator locator) {
		this.dbName = dbName;

		this.db = db;

		this.locator = locator;
	}

	public DBConfig getDB() {
		return this.db;
	}
	 
	public ActionLocator getLocator() {
		return locator;
	}

	public String getDbName() {
		return dbName;
	}

	public void setLocator(ActionLocator locator) {
		this.locator = locator;
	}
}
