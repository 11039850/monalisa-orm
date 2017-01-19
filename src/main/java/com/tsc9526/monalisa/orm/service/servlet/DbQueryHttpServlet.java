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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.service.DBS;
import com.tsc9526.monalisa.orm.service.Dispatcher;
import com.tsc9526.monalisa.orm.service.actions.ActionLocator;
import com.tsc9526.monalisa.orm.service.actions.ActionLocator.METHOD;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DbQueryHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -3809556004137368401L;
	
	public final static String DB_CFG_PREFIX="DB";
	
	protected Dispatcher dispatcher=new Dispatcher();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
	 
		initDBS(config,DB_CFG_PREFIX);
		
		int i=1;
		while(initDBS(config,DB_CFG_PREFIX+i)){
			i++;
		}
	}
	
	protected boolean initDBS(ServletConfig sc,String prefix){
		String name    =sc.getInitParameter(prefix+".name");
		if(name!=null){
			DBConfig db=getDbConfig(sc,prefix);
			 
			if(DBS.getDB(name)==null){
				METHOD[] ms=getMethods(sc,prefix);
				 
				ActionLocator locator=new ActionLocator();
				locator.setMethods(ms);
				
				DBS.add(name,db,locator); 
			}else{
				throw new RuntimeException("DBS init error: "+prefix+".name existed: "+name+", please check web.xml");
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	protected DBConfig getDbConfig(ServletConfig sc,String prefix){
		String name    =sc.getInitParameter(prefix+".name");
		String clazz   =sc.getInitParameter(prefix+".class");
		
		String url     =sc.getInitParameter(prefix+".url");
		String username=sc.getInitParameter(prefix+".username");
		String password=sc.getInitParameter(prefix+".password");
		
		DBConfig db=null;
		if(name!=null){
			if(clazz!=null){
				try{
					db=DBConfig.fromClass(ClassHelper.forClassName(clazz));
				}catch(ClassNotFoundException e){
					throw new RuntimeException(e);
				}
			}else if(url!=null && username!=null && password!=null){
				db=DBConfig.fromJdbcUrl(url, username, password);
			}else{
				StringBuffer msg=new StringBuffer("DBS init error, check web.xml: missing some parameters("+prefix+".url/username/password): {");
				msg.append(prefix+".name ="+name+", ");
				msg.append(prefix+".class ="+clazz+", ");
				msg.append(prefix+".url ="+url+", ");
				msg.append(prefix+".username ="+username+", ");
				msg.append(prefix+".password ="+password+"}");
				 
				throw new RuntimeException(msg.toString());
			}
		}
		return db;
	}
	
	protected METHOD[] getMethods(ServletConfig sc,String prefix) {
		String ms=sc.getInitParameter(prefix+".methods");
		if(ms!=null){
			ms=sc.getInitParameter(DB_CFG_PREFIX+".methods");
		}
		
		if(ms==null){
			return new METHOD[]{ METHOD.GET, METHOD.DELETE, METHOD.POST, METHOD.PUT, METHOD.HEAD};
		}else{
			List<METHOD> xs=new ArrayList<METHOD>();
			for(String m:Helper.splits(ms)){
				METHOD x=METHOD.valueOf( m.trim().toUpperCase() );
				xs.add(x);
			}
			
			return xs.toArray(new  METHOD[0]);
		}
	}
	
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
	
	//Head: meta data 
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	protected void doDispatch(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		dispatcher.doDispatch(req, resp);
	}
}
