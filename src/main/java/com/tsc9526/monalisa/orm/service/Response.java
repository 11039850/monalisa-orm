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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.tools.helper.JsonHelper;

/**
 * @author zzg.zhou(11039850@qq.com)
 */
public class Response implements Serializable{	 
	private static final long serialVersionUID = 5042617802808490420L;
	 
	/**
	 * HTTP status: 200-OK
	 */
	public final static int OK                    = 200;
	
	/**
	 * HTTP status: 304-Not Modified
	 */
	public final static int RESOURCE_NOT_MODIFIED = 304;	
	
	/**
	 * HTTP status: 400-Bad Request 
	 */
	public final static int REQUEST_BAD_PARAMETER = 400;
	
	/**
	 * HTTP status: 401-Unauthorized
	 */
	public final static int REQUEST_UNAUTHORIZED  = 401;
	
	/**
	 * HTTP status: 403-Access Forbidden
	 */
	public final static int REQUEST_FORBIDDEN          = 403;
	
	/**
	 * HTTP status: 404-Resource Not found
	 */
	public final static int REQUEST_NOT_FOUND          = 404;
 	

	/**
	 * HTTP status: 405-Method not allowed
	 */
	public final static int REQUEST_METHOD_NOT_ALLOWED = 405;
	
	/**
	 * HTTP status: 500-Internal Server Error 
	 */
	public final static int ERROR_SERVER_ERROR         = 500;
	
	public static Response fromJson(String jsonString){
		JsonObject json=new JsonParser().parse(jsonString).getAsJsonObject();
		
		Response r=new Response();
		r.setStatus(json.get("status").getAsInt());
		r.setMessage(JsonHelper.getString(json,"message"));
		r.setDetail(JsonHelper.getString(json,"detail"));
		
		JsonElement jd=json.get("data");
		if(jd!=null && !jd.isJsonNull()){
			if(jd.isJsonArray()){
				JsonArray array=jd.getAsJsonArray();
				if(array.size()>0 && array.get(0).isJsonObject()){
					r.setData(JsonHelper.parseToDataTable(array));
				}else if(array.size()==0){
					r.setData(new DataTable<DataMap>());
				}else{
					r.setData(jd);
				}
			}else if(jd.isJsonObject()){
				JsonObject js=jd.getAsJsonObject();
				if(js.get("total")!=null && js.get("page")!=null){
					r.setData(JsonHelper.parseToPage(jd.getAsJsonObject()));
				}else{
					r.setData(JsonHelper.parseToDataMap(jd.getAsJsonObject()));
				}
			}else{
				r.setData(jd);
			}
		}
		return r;
	}
	
	private int status=OK;
	
	private String message="OK";
	 
	private String detail;
	
	private Object data;
	 
	public Response(){
		this(OK,"OK");
	}
	
	public Response(int status,String message){
		this.status=status;
		this.message=message;
	}
	
	
	public Response(Object data){
		this(OK,"OK");
		setData(data);
	}
	 
	/**
	 * Response status
	 * 
	 * 200 - OK <br>
	 * 4xx - Request error <br>
	 * 5xx - Server internal error<br>
	 * 
	 * @return http status code.
	 */
	public int getStatus() {
		return status;
	}
  
	public Response setStatus(int status) {
		this.status = status;
		return this;
	}


	/**
	 * @return the Response message
	 */
	public String getMessage() {
		return message;
	}

	public Response setMessage(String message) {
		this.message = message;
		return this;
	}
   
	/**
	 * @param <T> the data type
	 * @return Response data body
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T)data;
	}
  	
	public Response setData(Object data) {	
		if(data instanceof Model){	
			this.data=((Model<?>)data).toMap(false);
		}else if(data instanceof List){
			List<?> ls=((List<?>)data);
			if(ls.size()>0 && ls.get(0) instanceof Model){
				List<Object> xs=new ArrayList<Object>();
				for(Object m:ls){
					xs.add(((Model<?>)m).toMap(false));
				}
				
				this.data=xs;
			}else{
				this.data=data;
			}
		}else{
			this.data = data;
		}
		
		return this;
	}	 
 	  

	public String getDetail() {
		return detail;
	}

	public Response setDetail(String detail) {
		this.detail = detail;
		return this;
	}
	
	public void writeResponse(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		 
		Gson gson=JsonHelper.createGsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		
		String body=gson.toJson(this);
		  
		PrintWriter w=resp.getWriter();
		w.write(body);
		w.close(); 	 
	}
  	
}
