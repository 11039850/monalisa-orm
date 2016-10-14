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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.tsc9526.monalisa.http.Response;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DefaultActionLocate implements ActionLocate{
	static Logger logger=Logger.getLogger(DefaultActionLocate.class);
	
	public Action getAction(ActionArgs args) {
		Response response=verify(args);
		if(response!=null){
			return new ResponseAction(response);
		}
		
		String m=args.getActionName();
		try{
			return getActionByMethod(m,args);
		}catch(NoSuchMethodException e){
			return new ResponseAction(new Response(Response.REQUEST_FORBIDDEN, "Access forbidden, method: "+m+" not found in the class: "+this.getClass().getName()));
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	protected Action getActionByMethod(String m,ActionArgs args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String name=m.substring(0,1).toUpperCase()+m.substring(1).toLowerCase();
		name="on"+name+"Action";
		
		if(name.equals("onGetAction")){
			return onGetAction(args);
		}else if(name.equals("onDeleteAction")){
			return onDeleteAction(args);
		}else if(name.equals("onPutAction")){
			return onPutAction(args);
		}else if(name.equals("onPostAction")){
			return onPostAction(args);
		}else{
			Method call=this.getClass().getMethod(name, ActionArgs.class);
			call.setAccessible(true);
			return (Action)call.invoke(this, args);
		}
	}
 	
	protected Response verify(ActionArgs args){
		return null;
	}
	
	protected Action onGetAction(ActionArgs args){
		return new GetAction(args);
	}
	
	protected Action onDeleteAction(ActionArgs args){
		return new DeleteAction(args);
	}
	
	protected Action onPutAction(ActionArgs args){
		return new PutAction(args);
	}
	
	protected Action onPostAction(ActionArgs args){
		return new PostAction(args);
	}
}
