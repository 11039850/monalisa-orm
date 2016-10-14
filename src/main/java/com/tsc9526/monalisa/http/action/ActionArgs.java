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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.http.DBS;
import com.tsc9526.monalisa.http.Response;
import com.tsc9526.monalisa.orm.datatable.DataMap;

/**
 * <b>URI</b>
 * <ul>
 * <li><b>/{Database}</b><br>
 * <font style='color:red'>GET</font>: get all tables of the Database<br>
 * 
 * <li><b>/{Database}/!{QUERY_ID}</b> <br>
 * <font style='color:red'>GET/POST</font>: customize query<br>
 * 
 * <li><b>/{Database}/{Table}</b> <br>
 * <font style='color:red'>GET</font>:  get records of the Table <br>
 * <font style='color:red'>POST</font>: insert records to the Table<br>
 * 
 * <li><b>/{Database}/{Table}/{SinglePk}</b><br>
 * <font style='color:red'>GET</font>:      get a record by single primary key<br>
 * <font style='color:red'>PUT/POST</font>: update a record by single primary key<br>
 * <font style='color:red'>DELETE</font>:   delete a record by single primary key<br>
 * 
 * <li><b>/{Database}/{Table}/{C1=V1},{C2=V2},{...}</b><br>
 * <font style='color:red'>GET</font>:      get a record by multiple primary keys<br>
 * <font style='color:red'>PUT/POST</font>: update a record by multiple primary keys<br>
 * <font style='color:red'>DELETE</font>:   delete a record by multiple primary keys<br>
 * 
 * <li><b>/{Database}/{Table1},{Table2},{...}</b><br>
 * <font style='color:red'>POST</font>:     insert records to the Table1 and Table2 ...<br>
 * 
 * <li><b>/{Database}/{Table1},{Table2},{Table3}/{Table1.id=Table2.id},{Table2.id=Table3.id}</b><br>
 * <font style='color:red'>GET</font>: get records by multiple tables' join<br>
 * </ul>
 *
 * <b>Query String</b>
 * <ul>
 * <li><b>columns</b><br>
 * columns=c1,c2,c3...   means: include column(c1,c2,c3) <br>
 * -columns=c1,c2 means: exclude column(c1,c2)
 * <li><b>order</b><br>
 * order=+c1,-c2  means: ORDER BY c1 ASC, c2 DESC
 * <li><b>limit</b><br>
 * limit=10
 * <li><b>offset</b><br>
 * offset=0
 * <li><b>paging</b><br>
 * paging=true means: get one page records, response header: 'X-Total-Count' indicate the total number of records.
 * <li>Other query parameters are considered as filter conditions<br>
 * c1=a&amp;c2=b&amp;c3&gt;=10&amp;c4&lt;10&amp;c5~p*&amp;c6!=7 <br>
 * SQL: where c1='a' AND c2='b' AND c3 >= 10 AND c4 < 10 AND c5 like 'p%' AND c6 != 7
 * </ul>
 * @author zzg.zhou(11039850@qq.com)
 */
public class ActionArgs {
	private String database;
	private String[] databases;

	private String table;
	private String singlePK;
	private String[][] multiKeys;

	private String[] tables;
	private String[] joins;

	private String queryId;

	private boolean paging = false;

	private int limit = 100;
	private int offset = 0;

	private String pathRequest;
	private String pathDatabases;
	private String pathTables;

	private List<String> includeColumns = new ArrayList<String>();
	private List<String> excludeColumns = new ArrayList<String>();

	private List<String[]> orderBy = new ArrayList<String[]>();;

	private List<String[]> filters = new ArrayList<String[]>();

	private List<String> errors = new ArrayList<String>();

	private DataMap rd = new DataMap();

	private String actionName;

	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	private String ip;

	public ActionArgs(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;

		Map<String, String[]> rs = req.getParameterMap();
		for (String name : rs.keySet()) {
			rd.put(name, rs.get(name));
		}

		String uri = req.getRequestURI();

		String prefix = req.getContextPath();
		if(prefix==null){
			prefix="";
		}
		prefix += req.getServletPath();

		pathRequest = uri.substring(prefix.length());
		if (pathRequest.endsWith("/") && pathRequest.length() > 1) {
			pathRequest = pathRequest.substring(0, pathRequest.length() - 1);
		}
		if (pathRequest.startsWith("/")) {
			pathRequest = pathRequest.substring(1);
		}
		try {
			pathRequest = URLDecoder.decode(pathRequest, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		actionName = req.getMethod();

		// Override POST method using http header: X-HTTP-Method-Override
		if ("POST".equalsIgnoreCase(actionName)) {
			String m = req.getHeader("X-HTTP-Method-Override");
			if (m != null) {
				actionName = m;
			}
		}

		ip=getRequestRealIp();
		
		parsePathParameters();
	}

	private void parsePathParameters() {
		if (pathRequest.length() < 1) {
			errors.add("Missing database, using URI: " + req.getRequestURI() + "/your_database");
		}

		if (errors.size() == 0)
			parsePathInfo();

		if (errors.size() == 0)
			parseParameterLimit();
		if (errors.size() == 0)
			parseParameterOffset();
		if (errors.size() == 0)
			parseParameterOrder();
		if (errors.size() == 0)
			parseParameterColumns();
		if (errors.size() == 0)
			parseParameterFilters();

		paging = "true".equalsIgnoreCase(rd.getStringFromArray("paging", 0));
	}
 
	private void parseDatabases(String[] vs) {
		pathDatabases = vs[0];

		if (pathDatabases.indexOf(",") > 0) {
			databases = pathDatabases.split(",");
		} else {
			database = checkName(pathDatabases);
		}
	}
	
	private void parseTables(String[] vs){
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
	
	private void parsePathInfo() {
		String[] vs = pathRequest.split("/");

		if (vs.length > 0) {
			parseDatabases(vs);
		}

		if (vs.length >= 2) {
			if(vs[1].startsWith("!")){
				queryId=vs[1].substring(1);
			}else{
				parseTables(vs);
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

	private void parseParameterLimit() {
		limit = rd.getInt("limit", 1000);
		if (limit < 1) {
			limit = 1;
		}
	}

	private void parseParameterOffset() {
		offset = rd.getInt("offset", 0);
		if (offset < 0) {
			errors.add("Parameter: offset = " + offset + ", the first record’s offset is 0.");
		}
	}

	private Response parseParameterOrder() {
		// order=-manufactorer,+model
		String order = rd.getStringFromArray("order", 0);
		if (order != null && order.trim().length() > 1) {
			for (String c : order.trim().split(",")) {
				c = c.trim();
				if (c.startsWith("-")) {
					c = c.substring(1);
					orderBy.add(new String[] { checkName(c), "DESC" });
				} else {
					if (c.startsWith("+")) {
						c = c.substring(1);
					}
					orderBy.add(new String[] { checkName(c), "ASC" });
				}
			}
		}

		return null;
	}

	private Response parseParameterColumns() {
		// columns=model,id,color
		String cs = rd.getStringFromArray("columns", 0);

		if (cs == null) {
			cs = rd.getStringFromArray("+columns", 0);
		}

		if (cs != null) {
			for (String c : cs.split(",")) {
				includeColumns.add(checkName(c.trim()));
			}
		} else {
			cs = rd.getStringFromArray("-columns", 0);

			if (cs != null) {
				for (String c : cs.split(",")) {
					excludeColumns.add(checkName(c.trim()));
				}
			}
		}

		return null;
	}

	private String checkName(String name) {
		if (!name.matches("[a-z0-9A-Z_`\\.\\\"'\\[\\]]+")) {
			errors.add("Invalid name: " + name);
		}
		return name;
	}

	private String getRequestRealIp() {
		String ip = null;
		String[] ip_headers = new String[] { 
				"x-forwarded-for", 
				"X-Forwarded-For", 
				"HTTP_X_FORWARDED_FOR", 
				"Proxy-Client-IP", 
				"WL-Proxy-Client-IP",
				"HTTP_CLIENT_IP" };
		for (String ih : ip_headers) {
			String ips = req.getHeader(ih);

			if (ips != null && ips.length() > 0 && ("unknown".equalsIgnoreCase(ips) == false)) {
				String[] vs = ips.split(",");
				for (String v : vs) {
					v = v.trim();
					if ("unknown".equalsIgnoreCase(v) == false) {
						ip = v;
						break;
					}
				}

				if (ip != null) {
					break;
				}
			}
		}

		if (ip == null) {
			ip = req.getRemoteAddr();
		}

		int p = ip.indexOf(":");
		if (p > 0) {
			ip = ip.substring(0, p);
		}

		return ip;
	}

	private Response parseParameterFilters() {
		Set<String> excludes = new HashSet<String>();
		excludes.add("limit");
		excludes.add("offset");
		excludes.add("order");
		excludes.add("columns");
		excludes.add("+columns");
		excludes.add("-columns");
		excludes.add("paging");

		for (String name : rd.keySet()) {
			if (!excludes.contains(name.toLowerCase())) {

				String field = name;
				String op = "=";
				String value = rd.getStringFromArray(name, 0);

				int p1 = 0, p2 = 0, p3 = 0, p4 = 0;

				p1 = name.indexOf("~");
				if (p1 < 0)
					p2 = name.indexOf("<>");
				if (p2 < 0)
					p3 = name.indexOf(">");
				if (p3 < 0)
					p4 = name.indexOf("<");

				if (p1 > 0) {
					field = name.substring(0, p1);
					op = "LIKE";
					value = name.substring(p1 + 1).replace('*', '%');
				} else if (p2 > 0) {
					field = name.substring(0, p2);
					op = "<>";
					value = name.substring(p2 + 2);
				} else if (p3 > 0 && (value == null || value.length() == 0)) {
					field = name.substring(0, p3);
					op = ">";
					value = name.substring(p3 + 1);
				} else if (p4 > 0 && (value == null || value.length() == 0)) {
					field = name.substring(0, p4);
					op = "<";
					value = name.substring(p4 + 1);
				} else {
					if (name.endsWith("<")) {
						field = name.substring(0, name.length() - 1);
						op = "<=";
					} else if (name.endsWith(">")) {
						field = name.substring(0, name.length() - 1);
						op = ">=";
					} else if (name.endsWith("!")) {
						field = name.substring(0, name.length() - 1);
						op = "!=";
					}
				}

				filters.add(new String[] { field, op, value });
			}
		}

		return null;
	}

	public DBS getDBS() {
		return DBS.getDB(databases != null ? databases[0] : database);
	}
	
	public String getActionName() {
		return actionName;
	}

	public DataMap getRequestDataMap() {
		return rd;
	}

	public String getDatabase() {
		return database;
	}

	public String[] getDatabases() {
		return databases;
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

	public boolean isPaging() {
		return paging;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public String getPathRequest() {
		return pathRequest;
	}

	public List<String> getIncludeColumns() {
		return includeColumns;
	}

	public List<String> getExcludeColumns() {
		return excludeColumns;
	}

	public List<String[]> getOrderBy() {
		return orderBy;
	}

	public List<String[]> getFilters() {
		return filters;
	}

	public List<String> getErrors() {
		return errors;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public HttpServletResponse getResp() {
		return resp;
	}

	public String getPathDatabases() {
		return pathDatabases;
	}

	public String getPathTables() {
		return pathTables;
	}

	public String getQueryId() {
		return queryId;
	}

	public String getIp(){
		return ip;
	}
}
