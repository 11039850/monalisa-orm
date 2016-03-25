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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tsc9526.monalisa.core.converters.Conversion;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ArrayTypeConversion implements Conversion<Object>{

	public Object[] getTypeKeys() {
		return new Object[] {
			byte[].class,Byte[].class,
			char[].class,Character[].class,
			short[].class,Short[].class,
			int[].class,Integer[].class,
			long[].class,Long[].class,
			float[].class,Float[].class,
			double[].class,Double[].class,
			boolean[].class,Boolean[].class,

			Object[].class,
			TYPE_ARRAYS
		};
	}
	
	public Object convert(Object value, Class<?> type) {
		if (value == null){
			return null;
		}
		  
		if(value instanceof JsonArray){
			JsonArray array=(JsonArray)value;
			return convertJsonToArray(array,type);
		}else{
			Object[] vs=toObjectArray(value,type);
			
			if(type.isArray() && type.getComponentType().isPrimitive()){
				return toPrimitiveArray(vs, type);
			}else{
				return vs;
			}
		}
	}
	
	protected Object toPrimitiveArray(Object[] value,Class<?> type){
		Class<?> t=type.getComponentType();
		
		if(t==byte.class){
			byte[] r=new byte[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Byte.parseByte(value[i].toString());
			}
			return r;
		}else if(t==char.class){
			char[] r=new char[value.length];
			for(int i=0;i<value.length;i++){
				Object v=value[i];
				r[i]= v.toString().charAt(0);
			}
			return r;
		}else if(t==short.class){
			short[] r=new short[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Short.parseShort(value[i].toString());
			}
			return r;
		}else if(t==int.class){
			int[] r=new int[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Integer.parseInt(value[i].toString());
			}
			return r;
		}else if(t==long.class){
			long[] r=new long[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Long.parseLong(value[i].toString());
			}
			return r;
		}else if(t==double.class){
			double[] r=new double[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Double.parseDouble(value[i].toString());
			}
			return r;
		}else if(t==boolean.class){
			boolean[] r=new boolean[value.length];
			for(int i=0;i<value.length;i++){
				r[i]=Boolean.valueOf(value[i].toString());
			}
			return r;
		}
		
		return null;
	}
	
	 
	
	protected Object[] toObjectArray(Object value,Class<?> type){
		if (!(value instanceof Object[])) {
			List<Object> vs = new ArrayList<Object>();

			if (value instanceof byte[]) {
				for (byte i : (byte[]) value) {
					vs.add(new Byte(i));
				}
			}else if (value instanceof char[]) {
				for (char i : (char[]) value) {
					vs.add(new Character(i));
				}
			}else if (value instanceof short[]) {
				for (short i : (short[]) value) {
					vs.add(new Short(i));
				}
			}else if (value instanceof int[]) {
				for (int i : (int[]) value) {
					vs.add(new Integer(i));
				}
			}else if (value instanceof long[]) {
				for (long i : (long[]) value) {
					vs.add(new Long(i));
				}
			}else if (value instanceof float[]) {
				for (float i : (float[]) value) {
					vs.add(new Float(i));
				}
			}else if (value instanceof double[]) {
				for (double i : (double[]) value) {
					vs.add(new Double(i));
				}
			}else if (value instanceof boolean[]) {
				for (boolean i:(boolean[]) value) {
					vs.add(new Boolean(i));
				}
			}else if(value instanceof Collection){
				vs.addAll((Collection<?>)value);	
			}else{
				vs.add(value);
			}
			
			return vs.toArray(new Object[] {});
		} else {
			return (Object[]) value;
		}
	}
	
	protected Object convertJsonToArray(JsonArray array,  Class<?> type){
		Object value=null;
		if(type==int[].class){
			int[] iv=new int[array.size()];
			for(int i=0;i<array.size();i++){
				JsonElement e=array.get(i);
				iv[i]=e.getAsInt();
			}
			value=iv;
		}else if(type==float[].class){
			float[] iv=new float[array.size()];
			for(int i=0;i<array.size();i++){
				JsonElement e=array.get(i);
				iv[i]=e.getAsFloat();
			}
			value=iv;
		}else if(type==long[].class){
			long[] iv=new long[array.size()];
			for(int i=0;i<array.size();i++){
				JsonElement e=array.get(i);
				iv[i]=e.getAsLong();
			}
			value=iv;
		}else if(type==double[].class){
			double[] iv=new double[array.size()];
			for(int i=0;i<array.size();i++){
				JsonElement e=array.get(i);
				iv[i]=e.getAsDouble();
			}
			value=iv;
		}else{//String[]
			String[] iv=new String[array.size()];
			for(int i=0;i<array.size();i++){
				JsonElement e=array.get(i);
				if(e.isJsonPrimitive()){
					iv[i]=e.getAsString();
				}else{
					iv[i]=e.toString();
				}
			}
			value=iv;
		}		
	 
		return value;
	}
}
