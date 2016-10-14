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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.tsc9526.monalisa.http.DBS;
import com.tsc9526.monalisa.http.Response;
import com.tsc9526.monalisa.http.exception.ResponseException;
import com.tsc9526.monalisa.orm.tools.helper.JsonHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ActionDispatcher {
	static Logger logger=Logger.getLogger(ActionDispatcher.class);
	 
	public void doDispatch(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
		String om = req.getHeader("X-HTTP-Method-Override");
		String method=req.getMethod();
		String q=req.getQueryString();
		
		String req_msg=method+(om==null?"":"["+om+"]")+" "+req.getRequestURI()+(q==null?"":"?"+q);
		logger.info(req_msg);
		
		Response r=new Response(Response.ERROR_SERVER_ERROR,"No service!");
		try{
			ActionArgs args=new ActionArgs(req,resp);
			if(args.getErrors().size()>0){
				r=new Response(Response.REQUEST_BAD_PARAMETER,"Request parameter error.").setData(args.getErrors());
			}else{
				Action action=getAction(args);
				
				r=action.verify();
				
				if(r==null){
					r=action.getResponse();
				}
			}
		}catch(Throwable t){
			logger.error("Error process path: "+req.getRequestURI(),"true".equalsIgnoreCase(req.getHeader("DEV_TEST"))?null:t);
		
			if(t instanceof ResponseException){
				r= ((ResponseException) t).getResponse();
			}else{
		 		r=new Response(Response.ERROR_SERVER_ERROR, t.getMessage());
			}
		}
		
		if(r==null){
			r=new Response(Response.ERROR_SERVER_ERROR,"No response!");
		}
		
		if(r.getStatus()!=Response.OK){
			logger.error("["+r.getStatus()+"] " +req_msg+"\r\n"+r.getMessage());
		}
		
		writeResponse(r,req,resp);
	}
	
	public Action getAction(ActionArgs args){
		DBS dbs=args.getDBS();
		if(dbs==null){
			return new ResponseAction(new Response(Response.REQUEST_BAD_PARAMETER,"Database not found: "+args.getPathDatabases()));
		}else{
			return dbs.getLocate().getAction(args);
		}
	}
	 
	public void writeResponse(Response r,HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		 
		Gson gson=JsonHelper.createGsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		String body=gson.toJson(r);
		 
		PrintWriter w=resp.getWriter();
		w.write(body);
		w.close(); 	 
	}
 	
}
