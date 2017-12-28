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
package com.tsc9526.monalisa.tools.converters.impl;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tsc9526.monalisa.tools.converters.Conversion;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.json.MelpJson;
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ObjectTypeConversion implements Conversion<Object> {
 
	public Object[] getTypeKeys() {
		return new Object[] {
			Object.class,
			Object.class.getName(),
			TYPE_OBJECT
		};
	}
  
	public Object convert(Object value, Class<?> type) {
		if (value == null){
			return null;
		}
		
		if(type.isInstance(value)){
			return value;
		}
		
		Object r=null;
		if (value.getClass().isArray() && value.getClass().getComponentType()==Byte.TYPE) {
			r=convertArrayByteToObject((byte[])value);
		}else if (value.getClass().isArray() && Map.class.isAssignableFrom(type)) {
			r=convertArrayByteToMap((Object[])value, (Class<? extends Map>)type);
		}else{
			r=convertObject(value, type);
		}
		
		if(type.isInstance(r)){
			return r;
		}else{
			return null;
		}
	}
	
	protected Object convertArrayByteToObject(byte[] value){
		ByteArrayInputStream bis= new ByteArrayInputStream((byte[])value);
		ObjectInputStream ois=null;
		try {
			ois=new ObjectInputStream(bis);
			return  ois.readObject();
		}catch (Exception e) {
			throw new IllegalArgumentException("Could not deserialize object",e);
		}finally {
			 MelpClose.close(bis,ois); 
		}
	}
	
	protected Map convertArrayByteToMap(Object[] xs,Class<? extends Map> type){
		Map m=null;
		if(type.isInterface()){
			m=new HashMap<Object,Object>();
		}else{
			try{
				m=(Map)type.newInstance();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		for(int k=0;k<xs.length;k++){
			m.put("c"+k, xs[k]);
		}
		
		return m;
	}
	
	protected Object convertObject(Object value,Class<?> type){
		Object r = null;
		
		Gson gson=MelpJson.getGson();
		if(value instanceof JsonElement){
			r= gson.fromJson((JsonElement)value, type);
		}else if(value instanceof String){
			String s = (String) value;
			if(s.startsWith("{") || s.startsWith("[") ){
				r = gson.fromJson(value.toString(), type);
			}else if(type.isAssignableFrom(List.class)){
				r = Arrays.asList(s.split(",")); 
			}else{
				r = null;
			}
		}else{
			JsonElement json=gson.toJsonTree(value);
			r= gson.fromJson(json, type);
			
			if(r instanceof Map){
				trimDouble((Map)r);
			}
		}
		
		return r;
	}
	
	protected void trimDouble(Map<Object,Object> map) {
		for(Object key:map.keySet()){
			Object v=map.get(key);
			if(v instanceof Double){
				Double d=(Double)v;
				if((""+v).endsWith(".0")){
					if(d >= Integer.MIN_VALUE && d <=Integer.MAX_VALUE){
						map.put(key, d.intValue());
					}else{
						map.put(key, d.longValue());
					} 
				}
			}
		}
	}
}
