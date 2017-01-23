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
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.service.actions.Action;
import com.tsc9526.monalisa.orm.service.actions.ResponseAction;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.helper.FileHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Dispatcher {
	static Logger logger=Logger.getLogger(Dispatcher.class);
	  
	protected String contentType = "text/json; charset=utf-8";
	
	public void doDispatch(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
		if("/favicon.ico".equalsIgnoreCase( req.getRequestURI() )){
			doResource(req,resp,"/com/tsc9526/monalisa/http/web/image/monalisa.png");
		}else{
			ModelArgs args=createActionArgs(req,resp);
		 	 
			doAction(req, resp,args);
		} 
	}
	
	protected ModelArgs createActionArgs(HttpServletRequest req,HttpServletResponse resp){
		ModelArgs args=new ModelArgs(req,resp);
		return args;
	}
	
	protected void doAction(HttpServletRequest req,HttpServletResponse resp,ModelArgs args)throws ServletException, IOException {
		long tm=System.currentTimeMillis();
		
		String om     = args.getActionName();
		String method = req.getMethod();
		String q      = req.getQueryString();
		
		String req_msg=method+(om.equals(method)?"":"["+om+"]")+" "+req.getRequestURI()+(q==null?"":"?"+q);
		logger.info(req_msg);
		
		Response r=new Response(Response.ERROR_SERVER_ERROR,"No service!");
		try{
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
			Object data=r.getData();
			
			logger.error(
					"["+r.getStatus()+"] " +req_msg
					+"\r\nmessage: "+r.getMessage()
					+(data==null?"": ("\r\ndata: "+data))
			);
		}
	 	
		resp.setContentType(contentType);
		resp.addHeader("X-Cost-Time",""+(System.currentTimeMillis()-tm));
		 
		r.writeResponse(req,resp);
	 	 
	}
	
	protected Action getAction(ModelArgs args){
		DBS dbs=args.getDBS();
		if(dbs==null){
			return new ResponseAction(new Response(Response.REQUEST_BAD_PARAMETER,"Database not found: "+args.getPathDatabases()));
		}else{
			return dbs.getLocator().getAction(args);
		}
	}
	 
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	protected void doResource(HttpServletRequest req, HttpServletResponse resp,String resPath)throws ServletException, IOException {
		int p=resPath.lastIndexOf(".");
		String ext=null;
		if(p>0){
			ext=resPath.substring(p+1);
		}
		
		if(ext==null){
			resPath+="/index.html";
			ext="html";
		}else if(resPath.endsWith("/")){
			resPath+="index.html";
			ext="html";
		}
		
		InputStream in=HttpServlet.class.getResourceAsStream(resPath);
		if(in!=null && ext!=null){
			String contentType=mimeTypes.getString(ext);
			
			if(contentType==null){
				contentType="application/octet-stream";
			}
			
			if(contentType.startsWith("text")){
				resp.addHeader("Content-Type",contentType+"; charset=utf-8");
			}else{
				resp.addHeader("Content-Type",contentType);
			}
			
			
			byte[] buf=FileHelper.readBytes(in);
			resp.addHeader("Content-Length",""+buf.length);
			
			OutputStream out=resp.getOutputStream();
			out.write(buf);
			out.flush();
			out.close();
		}else{
			resp.sendError(404);
		}
	}
	
	private static DataMap mimeTypes=new DataMap();
	static{
		String mimes=""+/**~{*/""
		 	+ "text/html                             html htm shtml;"
		  + "\r\n  text/css                              css;"
		  + "\r\n  text/xml                              xml;"
		  + "\r\n  text/plain                            txt;"
		  + "\r\n  application/x-javascript              js;"
		  + "\r\n  "
		  + "\r\n  image/gif                             gif;"
		  + "\r\n  image/png                             png;"
		  + "\r\n  image/jpeg                            jpeg jpg;"
		+ "\r\n"/**}*/.trim();
		
		for(String line:mimes.split(";")){
			line=line.trim();
			String[] vs=line.split("\\s+");
			if(vs.length>1){
				for(int i=1;i<vs.length;i++){
					mimeTypes.put(vs[i],vs[0]);
				}
			}
		}
	}
 	
}
