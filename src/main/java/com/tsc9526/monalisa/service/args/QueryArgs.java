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
package com.tsc9526.monalisa.service.args;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.service.DBS;
import com.tsc9526.monalisa.service.RequestParameter;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.servlet.MelpServlet;

/**
 * <b>URI</b>
 * <ul>
 * <li><b>/{Database}/{your.query.class.name$method}</b> <br>
 * <font style='color:red'>GET/POST</font>: customize query<br>
 * 
 * <li><b>/{Database}/{Table}</b> <br>
 * <font style='color:red'>HEAD</font>: get the meta info of Table<br>
 * 
 * <li><b>/{Database}</b><br>
 * <font style='color:red'>GET</font>: get all tables of the Database<br>
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
 * <li><b>method</b><br>
 * GET/POST/PUT/DELETE/HEAD: replace HTTP request's method. 
 * <li><b>column</b><br>
 * column=c1,c2,c3...   means: include column(c1,c2,c3) <br>
 * -column=c1,c2 means: exclude column(c1,c2)
 * <li><b>_order</b><br>
 * order=+c1,-c2  means: ORDER BY c1 ASC, c2 DESC
 * <li><b>limit</b><br>
 * limit=30
 * <li><b>offset</b><br>
 * offset=0
 * <li><b>paging</b><br>
 * paging=true, response header: 'X-Total-Count' &amp; 'X-Total-Page' indicate the total number of records &amp; pages.
 * <li>Other query parameters are considered as filter conditions<br>
 * c1=a&amp;c2=(1,2,3)&amp;c3&gt;=10&amp;c4&lt;10&amp;c5~p*&amp;c6!=7&amp;c7=[1,10] <br>
 * SQL: where c1='a' AND c2 in (1,2,3) AND c3 &gt;= 10 AND c4 &lt; 10 AND c5 like 'p%' AND c6 != 7 AND c7 BETWEEN 1 AND 10
 * </ul>
 * @author zzg.zhou(11039850@qq.com)
 */
public class QueryArgs {	
	protected String     authType;
	protected String     authUsername;
	
	protected String     dbname;
	protected String[]   dbnames;
	
	protected boolean    paging = false;
	
	/**
	 * Page size, default value is 30
	 */
	protected int        limit   = 30;
	
	/**
	 * The first record, offset is 0
	 */
	protected int        offset  = 0;
	 
	protected List<String> includeColumns = new ArrayList<String>();
	protected List<String> excludeColumns = new ArrayList<String>();
	protected List<String[]> orders       = new ArrayList<String[]>();
	protected List<String[]> filters      = new ArrayList<String[]>();

	protected DataMap requestDataMap      = new DataMap();
	protected List<String> errors         = new ArrayList<String>();
	
	protected HttpServletRequest  req;
	protected HttpServletResponse resp;
	 
	protected String pathAction;
	protected String pathDatabases;
	 
	protected String actionName;
	
	protected String   ip;
	protected String[] vs;
	 
	public QueryArgs(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		
		parse();
	}
	 
	protected void parse() {
		requestDataMap=MelpServlet.toDataMap(req);
		
		pathAction=MelpServlet.getActionPath(req);

		actionName = req.getMethod();

		// Override POST method using http header: X-HTTP-Method-Override
		if ("POST".equalsIgnoreCase(actionName)) {
			String m = req.getHeader("X-HTTP-Method-Override");
			if (m != null) {
				actionName = m;
			}
		}
		
		actionName=requestDataMap.getString(RequestParameter.METHOD, actionName);
		 
		ip=MelpServlet.getRequestRealIp(req);
		
		if (pathAction.length() > 0) {
			parsePathInfo();

			if (errors.size() == 0){
				parseParameters();
			}
		}else{
			errors.add("Missing database, using URI: " + req.getRequestURI() + "/your_database");
		}
	}
 	 
	
	private void parseParameters(){
		parseParameterColumns();
		parseParameterLimit();
		parseParameterOffset();
		parseParameterOrder();
		parseParameterFilters();
		
		paging = requestDataMap.getBool(RequestParameter.PAGING, false);
	}
 
	private void parseDatabases(String[] vs) {
		pathDatabases = vs[0];

		if (pathDatabases.indexOf(",") > 0) {
			dbnames = pathDatabases.split(",");
		} else {
			dbname = checkName(pathDatabases);
		}
	}
	 
	
	protected void parsePathInfo() {
		vs = pathAction.split("/");

		if (vs.length > 0) {
			parseDatabases(vs);	
		}
	}
	 

	protected void parseParameterLimit() {
		limit = requestDataMap.getInt(RequestParameter.LIMIT, limit);
		if (limit < 1) {
			limit = 1;
		}
	}

	protected void parseParameterOffset() {
		offset = requestDataMap.getInt(RequestParameter.OFFSET, 0);
	}

	protected void parseParameterOrder() {
		// sort=f1,f2&order=asc,desc
	 	String order = requestDataMap.getString(RequestParameter.ORDER);
	 	if(order != null && order.trim().length() > 1) {
			for (String c : order.trim().split(",")) {
				c = c.trim();
				if (c.startsWith("-")) {
					c = c.substring(1);
					orders.add(new String[] { checkName(c), "DESC" });
				} else {
					if (c.startsWith("+")) {
						c = c.substring(1);
					}
					orders.add(new String[] { checkName(c), "ASC" });
				}
			}
		}
	}

	protected void parseParameterColumns() {
		// columns=model,id,color
		String column=RequestParameter.COLUMN;
		
		String cs = requestDataMap.getString(column);

		if (cs == null) {
			cs = requestDataMap.getString("+"+column);
		}

		if (cs != null) {
			for (String c : cs.split(",")) {
				includeColumns.add(checkName(c.trim()));
			}
		} else {
			cs = requestDataMap.getString("-"+column);

			if (cs != null) {
				for (String c : cs.split(",")) {
					excludeColumns.add(checkName(c.trim()));
				}
			}
		}
	}
	
	protected void parseParameterFilters() {
		for (String name : requestDataMap.keySet()) {
			String field = name;
			String op    = "=";
			String value = requestDataMap.getString(name);
			
			boolean addFilter=
				( !name.startsWith(RequestParameter.PREFIX) )
				&&
				( !name.startsWith("+") )
				&&
				( !name.startsWith("-") )
				&&
				( !RequestParameter.contains(name) );
 			
			if(addFilter){
				addFilter(name,field,op,value);
			}
		}
	}
	
	protected void addFilter(String name,String field,String op,String value){
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

		checkName(field);
		
		filters.add(new String[] { field, op, value });
	}

	public String getOrderBy(String defaultOrderBy){
		String orderBy=getOrderBy();
		return orderBy!=null? orderBy : defaultOrderBy;
	}
	
	public String getOrderBy(){
		if (orders != null && orders.size() > 0) {
			StringBuffer sb=new StringBuffer();
			 
			for(int i=0;i<orders.size();i++){
				String[] by=orders.get(i);
				if(i>0){
					sb.append(", ");
				}
				sb.append(by[0]+" "+by[1]);
			}
			
			return sb.toString();
		} else {
			return null;
		}
	}
	
	public DBS getDBS() {
		return DBS.getDB(dbnames != null ? dbnames[0] : dbname);
	}
	
	public Query createQuery(){
		return getDBS().getDB().createQuery();
	}
	
	protected String checkName(String name) {
		if (name.matches(""+/**~{*/""
			+ "[\\\\;'\"]+  "
		+ "\r\n"/**}*/.trim()) ){
			errors.add("Invalid name: " + name);
		}
		return name;
	}
	
	public void addError(String error){
		errors.add(error);
	}
 
	public String getActionName() {
		return actionName;
	}

	public DataMap getRequestDataMap() {
		return requestDataMap;
	}

	public String getDbname() {
		return dbname;
	}

	public String[] getDbnames() {
		return dbnames;
	}

	public boolean isPaging() {
		return paging;
	}
 
	public String getPathAction() {
		return pathAction;
	}

	public List<String> getIncludeColumns() {
		return includeColumns;
	}

	public List<String> getExcludeColumns() {
		return excludeColumns;
	}

	public List<String[]> getOrders() {
		return orders;
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
 
 
	public String getIp(){
		return ip;
	}
  	 
	public void setDbname(String database) {
		this.dbname = database;
	}

	public void setDbnames(String[] databases) {
		this.dbnames = databases;
	}
 

	public void setPaging(boolean paging) {
		this.paging = paging;
	}
 

	public void setPathAction(String pathAction) {
		this.pathAction = pathAction;
	}

	public void setPathDatabases(String pathDatabases) {
		this.pathDatabases = pathDatabases;
	}
 

	public void setIncludeColumns(List<String> includeColumns) {
		this.includeColumns = includeColumns;
	}

	public void setExcludeColumns(List<String> excludeColumns) {
		this.excludeColumns = excludeColumns;
	}

	public void setOrders(List<String[]> orderBy) {
		this.orders = orderBy;
	}

	public void setFilters(List<String[]> filters) {
		this.filters = filters;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
 
	public String toString(){
		String qs=req.getQueryString();
		return this.pathAction+(qs==null?"":"?"+qs);
	}
 
	/**
	 * 
	 * @return the rows in one page(page size)
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit : page size
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
 
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset=offset;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthUsername() {
		return authUsername;
	}

	public void setAuthUsername(String authUsername) {
		this.authUsername = authUsername;
	}
}
