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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.service.DBS;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.tools.helper.Helper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Action {
	protected Logger logger=Logger.getLogger(this.getClass());
	
	protected HttpServletRequest  req;
	protected HttpServletResponse resp;
	 
	protected String database;
	
	protected String table;
	protected String singlePK;
	protected String[][] multiKeys; 
	
	protected String[] tables;
	
	protected boolean paging=false;
	
	protected int limit  = 100;
	protected int offset = 0;
	
	protected DBConfig db;
	
	protected String requestPath;
	 
	protected List<String>   includeColumns=new ArrayList<String>();
	protected List<String>   excludeColumns=new ArrayList<String>();
	
	protected List<String[]> orderBy=new ArrayList<String[]>();;
 	
	protected List<String[]> filters=new ArrayList<String[]>();
	
	protected List<String>   errors=new ArrayList<String>();
	 
	
	protected DataMap rd=new DataMap();
	
	public Action(HttpServletRequest req, HttpServletResponse resp){
		this.req =req;
		this.resp=resp;
		
		Map<String, String[]> rs=req.getParameterMap();
		for(String name:rs.keySet()){
			rd.put(name,rs.get(name)[0]); 
		}
	}
	
	/**
	 * <ul><b>URI<b>
	 * 	<li>/@Database
	 * 	<li>/@Database/@Table
	 *  <li>/@Database/@Table/@SinglePk
	 *  <li>/@Database/@Table/C1=V1/C2=V2/...
	 * </ul>
	 * 
	 * @return null if parse is OK, otherwise error
	 */
	protected Response parsePathParameters(){
		Response r=parsePathInfo();
		if(r==null){
			r=parseParameters();
		}
		
		if(r==null && errors.size()>0){
			r=new Response(400,errors.get(0));
			if(errors.size()>1){
				r.setData(errors);
			}
		}
		
		return r;
	}
	
	protected Response parsePathInfo(){
		String path=req.getPathInfo(); 
		if(path==null || path.equals("")){
			path=req.getRequestURI();
		}
		if(path.startsWith("/")){
			path=path.substring(1);
		}
		 
		this.requestPath=path;
		
		String[] vs=path.split("/");
		
		database=vs[0];
		
		if(vs.length>=2){
			if(vs[1].indexOf(",")>0){
				tables=vs[1].split(",");
			}else{
				table=vs[1];
		
				if(vs.length==3){
					String x=vs[2];
					int p=x.indexOf("=");
					if(p>0){
						multiKeys=new String[][]{ {x.substring(0,p),x.substring(p+1)} };
					}else{
						singlePK=vs[2];
					}
				}else{
					multiKeys=new String[vs.length-2][];
					 
					for(int i=0;i<vs.length-2;i++){
						String x=vs[i+2];
						int p=x.indexOf("=");
						
						multiKeys[i]=new String[]{x.substring(0,p),x.substring(p+1)} ;
					}
				}
			}
		}
		
		db=DBS.getDB(database);
		if(db==null){
			return new Response(404, "Database not found: "+database);
		}
	 
		return null;
	}
	
	protected Response parseParameters(){
		Response r=parseParameterLimit();
		
		if(r==null)r=parseParameterOffset();
		if(r==null)r=parseParameterOrder();
		if(r==null)r=parseParameterColumns();
		if(r==null)r=parseParameterFilters();
	 	
		paging=rd.getBool("paging",false);
		
		return r;
	}
	
	protected Response parseParameterLimit(){
		limit=rd.getInt("limit",1000);
		if(limit<1){
			limit=1;
		}
		
		int max=DbProp.PROP_TABLE_DBS_MAX_ROWS.getIntValue(db, table,10000);
		if(limit>max){
			return new Response(400, "Error parameter: limit = "+limit+", it is too bigger than the max value: "+max+". (OR increase the max value: \"DB.cfg.dbs.max.rows\")");
		}
		
		return null;
	}
	
	protected Response parseParameterOffset(){
		offset=rd.getInt("offset",0);
		if(offset<0){
			return new Response(400, "Error parameter: offset = "+offset+", the first record’s offset is 0.");
		}
		
		return null;
	}
	
	protected Response parseParameterOrder(){
		//order=-manufactorer,+model
		String order=rd.getString("order");
		if(order!=null && order.trim().length()>1){
			for(String c:order.trim().split(",")){
				c=c.trim();
				if(c.startsWith("-")){
					c=c.substring(1);
					orderBy.add(new String[]{checkField(c),"DESC"});
				}else{
					if(c.startsWith("+")){
						c=c.substring(1);
					}
					orderBy.add(new String[]{checkField(c),"ASC"});
				}
			}
		}
		
		return null;
	}
	
	protected Response parseParameterColumns(){
		//columns=model,id,color
		String cs=rd.getString("columns");
		
		if(cs==null){
			rd.getString("+columns");
		}
		
		if(cs!=null){
			for(String c:cs.split(",")){
				includeColumns.add(checkField(c.trim()));
			}
		}else{
			cs=rd.getString("-columns");
			
			if(cs!=null){
				for(String c:cs.split(",")){
					excludeColumns.add(checkField(c.trim()));
				}
			}
		}
		
		return null;
	}
	
	protected String checkField(String name){
		if(!name.matches("[a-z0-9A-Z_`\\[\\]]+")){
			errors.add("Invalid field name: "+name);
		}
		return name;
	}
	
	protected Response parseParameterFilters(){
		Set<String> excludes=new HashSet<String>();
		excludes.add("limit");
		excludes.add("offset");
		excludes.add("order");
		excludes.add("columns");
		excludes.add("+columns");
		excludes.add("-columns");
		excludes.add("paging");
		
		for(String name:rd.keySet()){
			if(!excludes.contains(name.toLowerCase())){
				
				String field = name;
				String op    = "=";
				String value = rd.getString(name);
				
				int p1=0,p2=0,p3=0,p4=0;
				
				p1=name.indexOf("~");
				if(p1<0)p2=name.indexOf("<>");
				if(p2<0)p3=name.indexOf(">");
				if(p3<0)p4=name.indexOf("<");
				
				if(p1>0){
					field=name.substring(0,p1);
					op="LIKE";
					value=name.substring(p1+1).replace('*','%');
				}else if(p2>0){
					field=name.substring(0,p2);
					op="<>";
					value=name.substring(p2+2);
				}else if(p3>0){
					field=name.substring(0,p3);
					op=">";
					value=name.substring(p3+1);
				}else if(p4>0){
					field=name.substring(0,p4);
					op="<";
					value=name.substring(p4+1);
				}else{
					if(name.endsWith("<")){ 
				 		field=name.substring(0,name.length()-1);
						op="<=";
					}else if(name.endsWith(">")){ 
						field=name.substring(0,name.length()-1);
						op=">=";
					}else if(name.endsWith("!")){ 
						field=name.substring(0,name.length()-1);
						op="!=";
					}
				}
				
				filters.add(new String[]{field,op,value});
			}
		}
		
		return null;
	}
 	
	public void service()throws ServletException, IOException {
		logger.info("Request URI: "+req.getRequestURI());
		
		Response response=new Response();
		try{
			response=parsePathParameters();
			if(response==null){
				response=getResponse();
			}
		}catch(Exception e){
			logger.error("Error process path: "+requestPath+", Error message: \r\n"+e,e);
			
			response=new Response(500,e.getMessage());
			 
			response.setDetail(Helper.toString(e));
		}
		
		writeResponse(response);
	}
	

	protected void writeResponse(Response r)throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		 
		String body=r.toJson();
		 
		PrintWriter w=resp.getWriter();
		w.write(body);
		w.close(); 	 
	}
	 
	public abstract Response getResponse();
	
}
