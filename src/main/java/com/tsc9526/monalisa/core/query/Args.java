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
package com.tsc9526.monalisa.core.query;

import java.util.Stack;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class Args {
	protected Stack<Object> dynamic=new Stack<Object>();
	
	public Args(Object... args){
		for(int i=0;i<args.length;i++){
			dynamic.push(args[args.length-1-i]);
		}
	}
	
	public Args push(Object arg){
		dynamic.add(arg);
		return this;
	}
	
	/**
	 * 获取所有的动态参数
	 * 
	 * @return Object[]
	 */
	public Object[] popAll(){
		return dynamic.toArray();
	}
	
	/**
	 * 
	 * 弹出一个动态参数
	 * 
	 * @param <T> result type
	 * @return object
	 */
	public <T> T pop(){
		return (T)dynamic.pop();
	}
	
	/**
	 * 弹出一个动态参数， 如果为动态参数不存在或null 则返回默认值
	 * 
	 * @param defaultValue 默认值
	 * @param <T> result type
	 * @return object
	 */
	public <T> T pop(T defaultValue){
		if(dynamic.isEmpty()){
			return defaultValue;
		}else{
			T r=(T)dynamic.pop();
			if(r==null){
				r=defaultValue;
			}
			return r;
		}
	}
}
