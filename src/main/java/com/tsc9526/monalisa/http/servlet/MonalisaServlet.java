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
package com.tsc9526.monalisa.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.http.action.ActionDispatcher;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MonalisaServlet extends HttpServlet{
	private static final long serialVersionUID = -3809556004137368401L;
	 
	protected ActionDispatcher dispatcher=new ActionDispatcher();
	
	
	//GET: get data
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	//POST: create or update data
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	//DELETE: delete data
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	//PUT: update data 
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	protected void doDispatch(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String uri=req.getRequestURI();
		if("/favicon.ico".equalsIgnoreCase( uri )){
			doFavicon(req,resp);
		}else{
			dispatcher.doDispatch(req, resp);
		}
	}
	
	protected void doFavicon(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		byte[] buf=new byte[16*1024];
		InputStream in=HttpServlet.class.getResourceAsStream("/com/tsc9526/monalisa/http/web/image/monalisa.png");
		int len=in.read(buf);
		
		resp.addHeader("Content-Type","image/png");
		resp.addHeader("Content-Length",""+len);
		
		OutputStream out=resp.getOutputStream();
		out.write(buf,0,len);
		out.flush();
		out.close();
	}
}
