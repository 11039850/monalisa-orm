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
package com.tsc9526.monalisa.service.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.service.DBS;
import com.tsc9526.monalisa.service.Dispatcher;
import com.tsc9526.monalisa.service.actions.ActionFilter;
import com.tsc9526.monalisa.service.actions.ActionExecutor;
import com.tsc9526.monalisa.service.args.MethodHttp;
import com.tsc9526.monalisa.service.args.MethodSQL;
import com.tsc9526.monalisa.service.auth.DigestAuth;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpException;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DbQueryHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -3809556004137368401L;
	
	static Logger logger=Logger.getLogger(DbQueryHttpServlet.class);
	
	public final static String DB_CFG_PREFIX="DB";
	  
	protected Dispatcher dispatcher=new Dispatcher();
 	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String contentType=config.getInitParameter("Content-Type");
		if(contentType!=null && contentType.length()>0){
			dispatcher.setContentType(contentType);
		}
	 
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
			
			String describe=sc.getServletContext().getContextPath()+"/"+sc.getServletName()+"/"+name;
			
			if(DBS.getDB(name)==null){
				ActionExecutor executor=getActionLocator(sc,prefix);
			 	DBS.add(name,db,executor,describe); 
			}else{
				throw new RuntimeException("DB service init error: "+prefix+".name existed: "+name+", please check web.xml");
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
					db=DBConfig.fromClass(MelpClass.forName(clazz));
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
	
	protected MethodHttp[] getHttpMethods(ServletConfig sc,String prefix) {
		String ms=sc.getInitParameter(prefix+".method.http");
		 
		if(ms==null){
			return MethodHttp.values();
		}else{
			List<MethodHttp> xs=new ArrayList<MethodHttp>();
			for(String m:MelpString.splits(ms)){
				MethodHttp x=MethodHttp.valueOf( m.trim().toUpperCase() );
				xs.add(x);
			}
			
			return xs.toArray(new  MethodHttp[0]);
		}
	}
	
	protected MethodSQL[] getSQLMethods(ServletConfig sc,String prefix) {
		String ms=sc.getInitParameter(prefix+".method.sql");
		 
		if(ms==null){
			return MethodSQL.values();
		}else{
			List<MethodSQL> xs=new ArrayList<MethodSQL>();
			for(String m:MelpString.splits(ms)){
				MethodSQL x=MethodSQL.valueOf( m.trim().toUpperCase() );
				xs.add(x);
			}
			
			return xs.toArray(new  MethodSQL[0]);
		}
	}
	
	
	protected ActionExecutor getActionLocator(ServletConfig sc,String prefix){
		String executorClass=sc.getInitParameter(prefix+".executor");
		 
		ActionExecutor executor=new ActionExecutor();
		if(executorClass!=null && executorClass.trim().length()>0){
			try {
				executor=(ActionExecutor)MelpClass.forName(executorClass.trim()).newInstance();
		 	} catch (Exception e) {
				MelpException.throwRuntimeException(e);	
			}
		}
		
		setupAuth(executor,sc,prefix);
		
		setupMethods(executor,sc,prefix);
		
		setupFilters(executor,sc,prefix);
		
		return executor;
	}
	
	protected void setupAuth(ActionExecutor executor,ServletConfig sc,String prefix){
		String auth=sc.getInitParameter(prefix+".auth.class");
		if(auth!=null && auth.trim().length()>0){
			try{
				ActionFilter af=(ActionFilter)MelpClass.forName(auth.trim()).newInstance();
					
				executor.addFilter(af);
			} catch (Exception e) {
				MelpException.throwRuntimeException(e);			
			}
		}else{
			String authUsers=sc.getInitParameter(prefix+".auth.users");
			if(authUsers==null || authUsers.trim().length()<1){
				authUsers="monalisa:monalisa";
				printAuthWarn(prefix); 
			}
			 
			if(!authUsers.equalsIgnoreCase("none")){
			 	List<String[]> userpwds=new ArrayList<String[]>();
				for(String uv:MelpString.splits(authUsers)){
					uv=uv.trim();
					
					int p=uv.indexOf(":");
					String username=uv.substring(0,p).trim();
					String password=uv.substring(p+1).trim();
					
					userpwds.add(new String[]{username,password});
				}
				
				executor.addFilter(new DigestAuth(userpwds));
			}
		}
	}
	
	protected void printAuthWarn(String prefix){
		logger.warn("Missing auth config\r\n"+/**~!{*/""
				+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
				+ "\r\n!!! Missing servlet init parameter: " +((prefix))+ ".auth.users in web.xml, "
				+ "\r\n!!! default authorization is monalisa:monalisa (user:password,user2:password2 ...) "
				+ "\r\n!!! set to \"none\" means disable auth"
				+ "\r\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
			+ "\r\n"/**}*/.trim());
	}
	
	protected void setupMethods(ActionExecutor executor,ServletConfig sc,String prefix){
		MethodHttp[] ms=getHttpMethods(sc,prefix);
		MethodSQL[]  mq=getSQLMethods(sc,prefix);
	
		executor.setHttpMethods(ms);
		executor.setSQLMethods(mq);
	}
	
	protected void setupFilters(ActionExecutor executor,ServletConfig sc,String prefix){
		String filters=sc.getInitParameter(prefix+".filters");
		if(filters!=null && filters.trim().length()>0){
			try{
				for(String f:MelpString.splits(filters)){
					ActionFilter af=(ActionFilter)MelpClass.forName(f.trim()).newInstance();
					
					executor.addFilter(af);
				}
			} catch (Exception e) {
				MelpException.throwRuntimeException(e);			
			}
		}	}

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
