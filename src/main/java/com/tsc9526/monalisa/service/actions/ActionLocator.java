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
package com.tsc9526.monalisa.service.actions;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.args.MethodHttp;
import com.tsc9526.monalisa.service.args.MethodSQL;
import com.tsc9526.monalisa.service.args.ModelArgs;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ActionLocator{
	static Logger logger=Logger.getLogger(ActionLocator.class);
	 
	protected List<MethodHttp> httpMethods=new ArrayList<MethodHttp>(){
		private static final long serialVersionUID = 1L;
		{
			for(MethodHttp m:MethodHttp.values()){
				add(m);
			}
		}
	};
	
	protected List<MethodSQL>  sqlMethods=new ArrayList<MethodSQL>(){
		private static final long serialVersionUID = 1L;
		{
			for(MethodSQL m:MethodSQL.values()){
				add(m);
			}
		}
	};

	protected List<ActionFilter> filters=new ArrayList<ActionFilter>();
	
	public ActionLocator(){
		 
	}
	
	public void setHttpMethods(MethodHttp ... methods){
		this.httpMethods.clear();
		
		for(MethodHttp m:methods){
			this.httpMethods.add(m);
		}
	}
	
	public void setSQLMethods(MethodSQL ... methods){
		this.sqlMethods.clear();
		
		for(MethodSQL m:methods){
			this.sqlMethods.add(m);
		}
	}
	 
	public Action getAction(ModelArgs args) {
		Response response=verify(args);
		if(response!=null){
			return new ResponseAction(response);
		}
		 
		MethodHttp httpMethod=args.getHttpMethod(); 
		if(!httpMethods.contains(httpMethod) || httpMethod==null){
			return new ResponseAction(Response.REQUEST_METHOD_NOT_ALLOWED,"Illegal http method: "+args.getActionName());
		}
		
		MethodSQL sqlMethod=httpMethod.toSQLMethod(args);
		if(!sqlMethods.contains(sqlMethod) || sqlMethod==null){
			return new ResponseAction(Response.REQUEST_METHOD_NOT_ALLOWED,"Illegal sql method: "+sqlMethod.name());
		}
		
		try{
			Action action= getActionByHttpMethod(httpMethod,args);
			
			Response r=doFilter(action);
			if(r==null){
				return action;
			}else{
				return new ResponseAction(r);
			}
		}catch(IllegalArgumentException  e){
			return new ResponseAction(Response.REQUEST_METHOD_NOT_ALLOWED, "Access forbidden, method: "+args.getActionName()+" not found in the class: "+this.getClass().getName());
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	protected Response doFilter(Action action){
		for(ActionFilter f:filters){
			if(f.accept(action)){
				Response r=f.doFilter(action);
				if(r!=null){
					return r;
				}
			}
		}
		return null; 
	}
	
	public ActionLocator addFilter(ActionFilter filter){
		filters.add(filter); 
		return this;
	}
	 
	protected Action getActionByHttpMethod(MethodHttp httpMethod,ModelArgs args) throws  IllegalArgumentException{
		switch(httpMethod){
			case GET:    return doGetAction(args);
			case DELETE: return doDeleteAction(args);
			case PUT:    return doPutAction(args);
			case POST:   return doPostAction(args);
			case HEAD:	 return doHeadAction(args);
			default:     throw new IllegalArgumentException(httpMethod.name());
		}
	}
    
	/**
	 * Verify request args
	 *  
	 * @param args the request args
	 * @return return null if ok, otherwise a response with error message. 
	 */
	protected Response verify(ModelArgs args){
		if(args.getErrors().size()>0){
			return new Response(Response.REQUEST_BAD_PARAMETER,"Request parameter error.").setData(args.getErrors());
		}else{
			return null;
		}
	}
  	
	protected Action doGetAction(ModelArgs args){
		return new GetAction(args);
	}
	
	protected Action doDeleteAction(ModelArgs args){
		return new DeleteAction(args);
	}
	
	protected Action doPutAction(ModelArgs args){
		return new PutAction(args);
	}
	
	protected Action doPostAction(ModelArgs args){
		return new PostAction(args);
	}
	 
	protected Action doHeadAction(ModelArgs args){
		return new HeadAction(args);
	}
	
	protected Action notAllowed(String method){
		return new ResponseAction(new Response(Response.REQUEST_METHOD_NOT_ALLOWED, "Method not allowed: "+method));
	}
	
	public List<MethodHttp> getHttpMethods(){
		return httpMethods;
	}
   
	public List<MethodSQL> getSQLMethods(){
		return sqlMethods;
	}
}
