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
package com.tsc9526.monalisa.orm.service.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.service.Response;

/**
 * 
 * <b>URI</b>
 * <ul>
 * <li><b>/@Database</b><br>
 * Get all tables of the @Database<br>
 * 
 * <li><b>/@Database/@Table</b> <br>
 * Get records of the @Table <br>
 * 
 * <li><b>/@Database/@Table/@SinglePk</b><br>
 * Get a record by single primary key
 * 
 * <li><b>/@Database/@Table/C1=V1/C2=V2/...</b><br>
 * Get a record by multiple primary keys 
 * 
 * <li><b>/@Database/@Table1,@Table2,@Table3/@Table1.id=@Table2.id/@Table2.id=@Table3.id</b><br>
 * Get records by multiple tables' join
 * 
 * </ul>
 *
 * @author zzg.zhou(11039850@qq.com)
 */
public class ActionArgs {
	private String database;

	private String table;
	private String singlePK;
	private String[][] multiKeys;

	private String[] tables;
	private String[] joins;

	private boolean paging = false;

	private int limit = 100;
	private int offset = 0;

	private String requestPath;

	private List<String> includeColumns = new ArrayList<String>();
	private List<String> excludeColumns = new ArrayList<String>();

	private List<String[]> orderBy = new ArrayList<String[]>();;

	private List<String[]> filters = new ArrayList<String[]>();

	private List<String> errors = new ArrayList<String>();

	private DataMap rd = new DataMap();

	private String actionName;

	private HttpServletRequest req;
	private HttpServletResponse resp;

	public ActionArgs(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;

		Map<String, String[]> rs = req.getParameterMap();
		for (String name : rs.keySet()) {
			rd.put(name, rs.get(name)[0]);
		}

		String uri = req.getRequestURI();

		String prefix = req.getContextPath();
		prefix += req.getServletPath();

		requestPath = uri.substring(prefix.length());
		if (requestPath.endsWith("/") && requestPath.length() > 1) {
			requestPath = requestPath.substring(0, requestPath.length() - 1);
		}
		if (requestPath.startsWith("/")) {
			requestPath = requestPath.substring(1);
		}
		try {
			requestPath = URLDecoder.decode(requestPath, "utf-8");
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

		parsePathParameters();
	}

	public String getActionName() {
		return this.actionName;
	}

	protected void parsePathParameters() {
		if (requestPath.length() < 1) {
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

		paging = rd.getBool("paging", false);
	}

	protected void parsePathInfo() {
		String[] vs = requestPath.split("/");

		if (vs.length > 0) {
			database = checkName(vs[0]);
		}

		if (vs.length >= 2) {
			if (vs[1].indexOf(",") > 0) {
				tables = vs[1].split(",");
				
				for(String t:tables){
					checkName(t);
				}
				
				if (vs.length > 2) {
					joins = new String[vs.length - 2];
					System.arraycopy(vs, 2, joins, 0, joins.length);
					for(String js:joins){
						String[] ns=js.split("=");
						
						if(ns.length!=2){
							errors.add("Invliad join field: "+js+", for example: .../Table1.column1=Table2.column2/...");
						}else{
							checkName(ns[0]);
							checkName(ns[1]);
						}
					}
				}
			} else {
				table = checkName(vs[1]);

				if (vs.length == 3) {
					String x = vs[2];
					int p = x.indexOf("=");
					if (p > 0) {
						multiKeys = new String[][] { { x.substring(0, p), x.substring(p + 1) } };
					} else {
						singlePK = vs[2];
					}
				} else if (vs.length > 3) {
					multiKeys = new String[vs.length - 2][];

					for (int i = 0; i < vs.length - 2; i++) {
						String x = vs[i + 2];
						int p = x.indexOf("=");

						multiKeys[i] = new String[] { x.substring(0, p), x.substring(p + 1) };
					}
				}
			}
		}
	}

	protected void parseParameterLimit() {
		limit = rd.getInt("limit", 1000);
		if (limit < 1) {
			limit = 1;
		}
	}

	protected void parseParameterOffset() {
		offset = rd.getInt("offset", 0);
		if (offset < 0) {
			errors.add("Parameter: offset = " + offset + ", the first record’s offset is 0.");
		}
	}

	protected Response parseParameterOrder() {
		// order=-manufactorer,+model
		String order = rd.getString("order");
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

	protected Response parseParameterColumns() {
		// columns=model,id,color
		String cs = rd.getString("columns");

		if (cs == null) {
			rd.getString("+columns");
		}

		if (cs != null) {
			for (String c : cs.split(",")) {
				includeColumns.add(checkName(c.trim()));
			}
		} else {
			cs = rd.getString("-columns");

			if (cs != null) {
				for (String c : cs.split(",")) {
					excludeColumns.add(checkName(c.trim()));
				}
			}
		}

		return null;
	}

	protected String checkName(String name) {
		if (!name.matches("[a-z0-9A-Z_`\\.\\\"'\\[\\]]+")) {
			errors.add("Invalid name: " + name);
		}
		return name;
	}

	protected Response parseParameterFilters() {
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
				String value = rd.getString(name);

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

	public DataMap getRequestDataMap() {
		return rd;
	}

	public String getDatabase() {
		return database;
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

	public String getRequestPath() {
		return requestPath;
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

}
