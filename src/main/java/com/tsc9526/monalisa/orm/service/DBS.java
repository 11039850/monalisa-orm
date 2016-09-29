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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.service.action.ActionArgs;
import com.tsc9526.monalisa.orm.service.action.ActionLocate;
import com.tsc9526.monalisa.orm.service.action.DefaultActionLocate;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBS {
	static Logger logger = Logger.getLogger(DBS.class);

	private static DataMap dbs = new DataMap();

	public static void add(String dbName, DBConfig db) {
		add(dbName, db, new DefaultActionLocate());
	}

	public static void add(String dbName, DBConfig db, ActionLocate locate) {
		dbName=Dialect.getRealname(dbName);
		
		if (!dbs.containsKey(dbName)) {
			DBS s=new DBS(dbName, db, locate);
			
			logger.info("Add DB service" + s.methods + ": /" + dbName + ", dbkey: " + db.getKey());
			dbs.put(dbName, s);
		} else {
			logger.info("Database service existed: /" + dbName + ", dbkey: " + db.getKey());
		}
	}

	public static void remove(String dbName) {
		dbName=Dialect.getRealname(dbName);
		
		DBS s=(DBS)dbs.remove(dbName);
		if(s!=null){
			logger.info("Removed DB service" + s.methods + ": /" + dbName);
		}
	}

	public static void clear() {
		dbs.clear();
	}

	public static DBS getDB(String dbName) {
		dbName=Dialect.getRealname(dbName);
		
		return (DBS) dbs.get(dbName);
	}

	private String dbName;

	private DBConfig db;

	private ActionLocate locate;

	private List<String> methods = new ArrayList<String>();

	private DBS(String dbName, DBConfig db, ActionLocate locate) {
		this.dbName = dbName;

		this.db = db;

		this.locate = locate;
		
		getMethods();
	}

	private void getMethods() {
		for (Method m : locate.getClass().getMethods()) {
			String name = m.getName();
			if (name.startsWith("on") && name.endsWith("Action")) {
				name = name.substring(2, name.length() - 6);

				if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == ActionArgs.class) {
					methods.add(name.toUpperCase());
				}
			}
		}
	}

	public DBConfig getDB() {
		return this.db;
	}

	public ActionLocate getLocate() {
		return locate;
	}

	public String getDbName() {
		return dbName;
	}

}
