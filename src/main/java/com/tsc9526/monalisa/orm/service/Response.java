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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.tools.helper.JsonHelper;

/**
 * @author zzg.zhou(11039850@qq.com)
 */
public class Response implements Serializable{	 
	private static final long serialVersionUID = 5042617802808490420L;
 	
	public static Response fromJson(String jsonString){
		JsonObject json=new JsonParser().parse(jsonString).getAsJsonObject();
		
		Response r=new Response();
		r.setStatus(json.get("status").getAsInt());
		r.setMessage(JsonHelper.getString(json,"message"));
		r.setDetail(JsonHelper.getString(json,"detail"));
		
		JsonElement jd=json.get("data");
		if(jd!=null && !jd.isJsonNull()){
			if(jd.isJsonArray()){
				r.setData(JsonHelper.parseToDataTable(jd.getAsJsonArray()));
			}else if(jd.isJsonObject()){
				r.setData(JsonHelper.parseToDataMap(jd.getAsJsonObject()));
			}else{
				r.setData(jd);
			}
		}
		return r;
	}
	
	private int status=200;
	
	private String message="OK";
	 
	private String detail;
	
	private Object data;
	 
	public Response(){
		this(200,"OK");
	}
	
	public Response(int status,String message){
		this.status=status;
		this.message=message;
	}
	
	
	public Response(Object data){
		this(200,"OK");
		setData(data);
	}
	 
	/**
	 * Response status
	 * 
	 * 200 - OK <br>
	 * 4xx - Request error <br>
	 * 5xx - Server internal error<br>
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
	 * @parameter <T> the data type
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

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
