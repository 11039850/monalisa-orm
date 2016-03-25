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
package com.tsc9526.monalisa.core.converters.impl;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tsc9526.monalisa.core.converters.Conversion;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.JsonHelper;
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
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
			ByteArrayInputStream bis= new ByteArrayInputStream((byte[])value);
			ObjectInputStream ois=null;
			try {
				ois=new ObjectInputStream(bis);
				r= ois.readObject();
			}catch (Exception e) {
				throw new IllegalArgumentException("Could not deserialize object",e);
			}finally {
				 CloseQuietly.close(bis,ois); 
			}
		}else{
			Gson gson=JsonHelper.getGson();
			if(value instanceof JsonElement){
				r= gson.fromJson((JsonElement)value, type);
			}else if(value instanceof String){
				r= gson.fromJson(value.toString(), type);
			}else{
				JsonElement json=gson.toJsonTree(value);
				r= gson.fromJson(json, type);
			}
		}
		
		if(type.isInstance(r)){
			return r;
		}else{
			return null;
		}
	}
}
