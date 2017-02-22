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

import java.lang.reflect.Method;

import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.args.ModelArgs;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CallAction extends Action{
	protected DataService service;
	
	public CallAction(ModelArgs args,DataService service) {
		super(args);
		this.service=service;
	}
	 
	public Response getResponse() {
		String fname=args.getSinglePK();
		if(fname==null){
			fname="index";
		}
		 
		try{
			Method x=service.getClass().getMethod(fname); 
			  
			Object r=x.invoke(service);

			return new Response(r);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
