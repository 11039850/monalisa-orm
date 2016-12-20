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
package com.tsc9526.monalisa.orm.service.args;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see  QueryArgs
 * @author zzg.zhou(11039850@qq.com)
 */
public class ModelArgs extends QueryArgs{	
 	private String     table;
	private String     singlePK;
	private String[][] multiKeys; 

	private String[]   tables;
	private String[]   joins;
  
	private String pathTables;
 
	public ModelArgs(HttpServletRequest req, HttpServletResponse resp) {
		super(req, resp);
		
		if(vs!=null && vs.length>1){
			parseTables(vs);
		} 
	}
	 
	protected void parseTables(String[] vs){
		pathTables = vs[1];
	 	
		if (pathTables.indexOf(",") > 0) {// table joins
			tables = pathTables.split(",");

			for (String t : tables) {
				checkName(t);
			}

			if (vs.length == 3) {
				joins = vs[2].split(",");
				for (String js : joins) {
					String[] ns = js.split("=");

					if (ns.length != 2) {
						errors.add("Invalid join field: " + js + ", for example: .../Table1.column1=Table2.column1,Table1.column2=Table2.column2,...");
					} else {
						checkName(ns[0]);
						checkName(ns[1]);
					}
				}
			} else if (vs.length > 3) {
				unnecessaryPath(vs, 3);
			}
		} else {// single table
			table = checkName(pathTables);

			if (vs.length == 3) {
				String[] xs = vs[2].split(",");

				if (xs.length == 1) {// single primary key
					int p = xs[0].indexOf("=");
					if (p > 0) {
						multiKeys = new String[][] { { xs[0].substring(0, p), xs[0].substring(p + 1) } };
					} else {
						singlePK = vs[2];
					}
				} else {// multiple keys
					multiKeys = new String[xs.length][];

					for (int i = 0; i < xs.length; i++) {
						String x = xs[i];
						int p = x.indexOf("=");

						multiKeys[i] = new String[] { x.substring(0, p), x.substring(p + 1) };
					}
				}
			} else if (vs.length > 3) {
				unnecessaryPath(vs, 3);
			}
		}
	}
	 
	private void unnecessaryPath(String[] vs, int from) {
		StringBuilder sb = new StringBuilder();
		for (int i = from; i < vs.length; i++) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(vs[i]);
		}
		errors.add("Unnecessary path: " + sb.toString());
	}
   
	public String getTable() {
		return table;
	}

	public String getSinglePK() {
		return singlePK;
	}

	public String[][] getMultiKeys() {
		return multiKeys;
	}

	public String[] getTables() {
		return tables;
	}

	public String[] getJoins() {
		return joins;
	}
  
	public String getPathTables(){
		return pathTables;
	}
   
	public void setTable(String table) {
		this.table = table;
	}

	public void setSinglePK(String singlePK) {
		this.singlePK = singlePK;
	}

	public void setMultiKeys(String[][] multiKeys) {
		this.multiKeys = multiKeys;
	}

	public void setTables(String[] tables) {
		this.tables = tables;
	}

	public void setJoins(String[] joins) {
		this.joins = joins;
	}
 
	public void setPathTables(String pathTables) {
		this.pathTables = pathTables;
	}	
}
