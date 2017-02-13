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
package com.tsc9526.monalisa.tools.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.tools.clazz.Shallowable;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.misc.MelpException;
import com.tsc9526.monalisa.tools.parser.Parser;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ServletRequestParser implements Parser<javax.servlet.ServletRequest>{
	
	public boolean parse(Object target, javax.servlet.ServletRequest data, String... mappings) {
		KeyMapping map=new KeyMapping(data.getParameterMap(),mappings);
		
		for(FGS fgs:MelpClass.getFields(target)){
			fgs.mapto(map, target);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> parseArrays(T target, javax.servlet.ServletRequest data, String... mappings) {
		KeyMapping map = new KeyMapping(data.getParameterMap(), mappings);
		List<T> rs=new ArrayList<T>();
		
		int size=0;
		for (FGS fgs : MelpClass.getFields(target)) {
			String key=fgs.findKey(map);
			if(key!=null){
				String[] value = (String[])map.get(key);
				if(value!=null && value.length>size){
					size=value.length;
				}
			}
		}
		
		if(size<1){
			return rs;
		}
		
		try{
			if(target instanceof Shallowable<?>){
				for(int i=0;i<size;i++){
					Object x=((Shallowable<?>)target).shallow();
					rs.add((T)x);
				}
			}else{
				for(int i=0;i<size;i++){
					rs.add((T)target.getClass().newInstance());
				}
			}
		}catch(Exception e){
			return MelpException.throwRuntimeException(e);
		}
		
		for (FGS fgs : MelpClass.getFields(target)) {
			String key=fgs.findKey(map);
			if(key!=null){
				String[] value = (String[])map.get(key);
			 
				if(value!=null){
					for(int i=0;i<value.length;i++){
						fgs.setObject(rs.get(i), value[i]);
					}
				}
			}
		}
		
		return rs;
	}	
}