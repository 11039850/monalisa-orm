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
package com.tsc9526.monalisa.core.converters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.tsc9526.monalisa.core.converters.impl.ArrayTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.BigDecimalTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.BooleanTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.ByteTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.CharacterTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.DateTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.DoubleTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.FloatTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.IntegerTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.LongTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.ObjectTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.SameTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.ShortTypeConversion;
import com.tsc9526.monalisa.core.converters.impl.StringTypeConversion;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.EnumHelper;
import com.tsc9526.monalisa.core.tools.JsonHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class TypeConverter {
	static Logger logger=Logger.getLogger(TypeConverter.class);
	 
	public <T> T convert(Object v, Class<T> type) {
		if(type==null){
			return (T)v;
		}
		
		if(v instanceof JsonNull){
			return null;
		}
				 
		if(v!=null){
			if(type.isInstance(v)){
				return (T)v;
			}else{
				return doConvert(v, type);
			}
		}else{
			return null;
		}		 
	}
	 
	protected <T> T convertToEnum(Object v, Class<T> type) {
		return (T)EnumHelper.getEnum(type, v);
	}
	
	protected JsonObject convertToJsonObject(Object v,  Class<?> type) {
		if(v.getClass()==JsonObject.class){
			return (JsonObject)v;
		}else{
			return new JsonParser().parse(v.toString()).getAsJsonObject();
		}	
	}	 
	
	protected String convertMapToString(Map<?,?> m){		
		Gson gson=JsonHelper.getGson();
		return gson.toJson(m);		
	}
	
	protected String convertArrayToString(Object[] v){		
		return Arrays.toString(v);			
	}
	
	protected String convertToString(Object v){		
		return v.toString();			
	}
	
	protected Object convertJsonToArray(Object v,  Class<?> type){
		Object value=null;
		
		JsonElement je=new JsonParser().parse(v.toString());
		if(je==null || je.isJsonNull()){
			value=null;
		}else{
			JsonArray array=je.getAsJsonArray();						
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
		}
		return value;
	}
	
	protected Object convertStringToBean(Object v, Class<?> type){
		//Json String to Java Object
		if(v.getClass()==String.class){
			return JsonHelper.getGson().fromJson(v.toString(), type);
		}else{
			return JsonHelper.getGson().fromJson((JsonElement)v, type);
		}
	}
	   
	protected <T> T doConvert(Object v, Class<T> type){
		Object value=null;
		
		if(type.isEnum()){
			value=convertToEnum(v,type);
		}else if(type==JsonObject.class){
			value=convertToJsonObject(v,type);
		}else if(v.getClass().isArray()==false && type.isArray()){
			value=convertJsonToArray(v,type);
		}else if(type.isArray()==false 
				&& type.isPrimitive()==false 
				&& type.getName().startsWith("java.")==false
				&& (v.getClass() == String.class || v instanceof JsonElement)){
			value=convertStringToBean(v,type);
		}else{		
			if(v instanceof JsonPrimitive){
				JsonPrimitive p=((JsonPrimitive)v);
				v=p.getAsString();
				  
			}
			value=tryConvert(type, v);
		}		
	
		return (T)value;
	}	
	

	public static void registerTypeConversion(Conversion<?> conversion) {
		Object[] keys = conversion.getTypeKeys();
		if (keys == null) {
			return;
		}

		for (int i = 0; i < keys.length; i++) {
			typeConversions.put(keys[i], conversion);
		}
	}
 
	public static void unregisterTypeConversion(Conversion<?> conversion) {
		if (conversion != null) {
			Object[] keys = conversion.getTypeKeys();
			 		
			if (keys != null) {
				for (int i = 0; i < keys.length; i++) {
					typeConversions.remove(keys[i]);
				}
			}
		}
	}
   
	protected Object tryConvert(Class<?> type, Object value) {
		if (value == null){
			return null;
		}

		if (type == null) {
			return value;
		}

		Conversion<?> conversion = getTypeConversion(type, value);

		if (conversion != null) {
			Object result = conversion.convert(value);
			return result;
		} else {
			return null;
		}
	}

	 
	protected  Conversion<?> getTypeConversion(Object typeKey, Object value) {
		// Check if the provided value is already of the target type
		if (typeKey instanceof Class && ((Class<?>) typeKey) != Object.class && ((Class<?>) typeKey).isInstance(value)) {
			return IDENTITY_CONVERSION;
		}

		return typeConversions.get(typeKey);
	}

	  
	private static final Map<Object, Conversion<?>> typeConversions = Collections.synchronizedMap(new HashMap<Object, Conversion<?>>());

	private static final Conversion<?> IDENTITY_CONVERSION = new SameTypeConversion();

	static {
		registerTypeConversion(new BigDecimalTypeConversion());
		registerTypeConversion(new BooleanTypeConversion());
		registerTypeConversion(new ByteTypeConversion());
		registerTypeConversion(new CharacterTypeConversion());
		registerTypeConversion(new DoubleTypeConversion());
		registerTypeConversion(new FloatTypeConversion());
		registerTypeConversion(new IntegerTypeConversion());
		registerTypeConversion(new LongTypeConversion());
		registerTypeConversion(new ObjectTypeConversion());
		registerTypeConversion(new ShortTypeConversion());
		registerTypeConversion(new DateTypeConversion());
		registerTypeConversion(new StringTypeConversion());
		
		registerTypeConversion(new ArrayTypeConversion());
	}
}
