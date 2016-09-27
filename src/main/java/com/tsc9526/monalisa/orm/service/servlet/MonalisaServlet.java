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
package com.tsc9526.monalisa.orm.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.service.action.DeleteAction;
import com.tsc9526.monalisa.orm.service.action.GetAction;
import com.tsc9526.monalisa.orm.service.action.PostAction;
import com.tsc9526.monalisa.orm.service.action.PutAction;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MonalisaServlet extends HttpServlet{
	private static final long serialVersionUID = -3809556004137368401L;
	
	//使用订制的HTTP头 X-HTTP-Method-Override 来覆盖POST 方法.
	
	//GET用来获取资源，
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new GetAction(req,resp).service();
	}
	
	//POST用来新建资源（也可以用于更新资源）
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String m=req.getHeader("X-HTTP-Method-Override");
		
		if("DELETE".equals(m)){
			new DeleteAction(req,resp).service();
		}else if("PUT".equals(m)){
			new PutAction(req,resp).service();
		}else{
			new PostAction(req,resp).service();
		}
	}
	
	//DELETE用来删除资源。 
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DeleteAction(req,resp).service();
	}
	
	//PUT用来更新资源
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		new PutAction(req,resp).service();
	}
	 
}
