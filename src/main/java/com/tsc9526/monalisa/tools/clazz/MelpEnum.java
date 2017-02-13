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
package com.tsc9526.monalisa.tools.clazz;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpEnum {
	private static Map<String, Object> hIntegerEnum=new ConcurrentHashMap<String, Object>();
	private static Map<String, Object> hStringEnum=new ConcurrentHashMap<String, Object>();
	
	public static Object getEnum(Class<?> type,Object value){
		if(value!=null){
			if(type.isEnum()){
				if(value.getClass().isEnum() && value.getClass()==type){
					return value;
				}else{
					if(value instanceof Number){
						int x=((Number)value).intValue();
						return getEnum(type,x);					
					}else{
						String x=value.toString();
						try{ 
							int i=Integer.parseInt(x);
							return getEnum(type,i);	
						}catch(NumberFormatException e){
							return getEnum(type,x);			
						}
					}
				}
			} 			 
		} 
		return null;
	}
	
	private static Object getEnum(Class<?> type,int x){
		String key=type.getName()+"-"+x;
		
		if(!hIntegerEnum.containsKey(key)){
			Object[] ecs=type.getEnumConstants();
			for(Object e:ecs){
				int v=getIntValue((Enum<?>)e);
				hIntegerEnum.put(type.getName()+"-"+v, e);
			}						
		}
		
		return hIntegerEnum.get(key);
		
	}
	
	private static Object getEnum(Class<?> type,String x){
		String key=type.getName()+"-"+x;
		
		if(!hStringEnum.containsKey(key)){
			Object[] ecs=type.getEnumConstants();
			for(Object e:ecs){
				String v=getStringValue((Enum<?>)e);
				hStringEnum.put(type.getName()+"-"+v, e);
			}						
		}
		
		return hStringEnum.get(key);
	}

	public static int getIntValue(Enum<?> e){ 
		try {			 
			Method m = e.getClass().getMethod("getValue");
			Object v =m.invoke(e);
			return Integer.parseInt(v.toString().trim());
		}catch(NoSuchMethodException ex){			
			return e.ordinal();			
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public static String getStringValue(Enum<?> e){ 
		return e.name(); 
	}
	
}
