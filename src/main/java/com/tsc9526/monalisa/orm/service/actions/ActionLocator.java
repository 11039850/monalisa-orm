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
package com.tsc9526.monalisa.orm.service.actions;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ActionLocator{
	static Logger logger=Logger.getLogger(ActionLocator.class);
	
	public static enum METHOD{
		/**SELECT*/
		GET,
		
		/**DELETE*/
		DELETE,
		
		/**UPDATE*/
		POST,
		
		/**UPDATE*/
		PUT,
		
		/**DESCRIBE TABLE, SHOW DATABASE ...*/
		HEAD;
	}
	 
	private List<METHOD> methods=new ArrayList<METHOD>(){
		private static final long serialVersionUID = 1L;
		{
			for(METHOD m:METHOD.values()){
				add(m);
			}
		}
	};

	public ActionLocator(){
		 
	}
	
	public void setMethods(METHOD ... methods){
		this.methods.clear();
		
		for(METHOD m:methods){
			this.methods.add(m);
		}
	}
	
	public Action getAction(ModelArgs args) {
		Response response=verify(args);
		if(response!=null){
			return new ResponseAction(response);
		}
		
		try{
			return getActionByMethod(args);
		}catch(IllegalArgumentException  e){
			return new ResponseAction(new Response(Response.REQUEST_METHOD_NOT_ALLOWED, "Access forbidden, method: "+args.getActionName()+" not found in the class: "+this.getClass().getName()));
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	protected Action getActionByMethod(ModelArgs args) throws  IllegalArgumentException{
		String name=args.getActionName().toUpperCase();
		
		METHOD method=METHOD.valueOf(name);
		
		if(!methods.contains(method) || method==null){
			throw new IllegalArgumentException(name);
		}
		
		switch(method){
			case GET:    return doGetAction(args);
			case DELETE: return doDeleteAction(args);
			case PUT:    return doPutAction(args);
			case POST:   return doPostAction(args);
			case HEAD:	 return doHeadAction(args);
			default:     throw new IllegalArgumentException(name);
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
	
	public List<METHOD> getMethods(){
		return methods;
	}
  
}
