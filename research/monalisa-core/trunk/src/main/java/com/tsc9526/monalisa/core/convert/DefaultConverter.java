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
package com.tsc9526.monalisa.core.convert;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateTimeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.EnumHelper;
import com.tsc9526.monalisa.core.tools.JsonHelper;

@SuppressWarnings("unchecked")
public class DefaultConverter implements Converter{
	static Logger logger=Logger.getLogger(DefaultConverter.class);
	
	static{
		DateValue dc = new DateValue(null);
		dc.setUseLocaleFormat(true);
		String[] datePattern = {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss.SSS"};    
		dc.setPatterns(datePattern);
		ConvertUtils.register(dc, java.util.Date.class);
	}
	
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
			}if(type==float[].class){
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
	
	protected <T> T convertOtherTypes(Object v, Class<T> type){		
		return (T)ConvertUtils.convert(v, type);
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
			value=convertOtherTypes(v, type);
		}		
		
		
		return (T)value;
	}	
	
	
	public static class DateValue extends DateTimeConverter {

		public DateValue() {
	        super();        
	    }
  
	    public DateValue(Object defaultValue) {
	        super(defaultValue);
	    }
 
	    protected Class<?> getDefaultType() {
	        return Date.class;
	    }
	    
	    public <T> T convert(Class<T> type, Object value) {
	    	try{
	    		long v=Long.parseLong(value.toString());
	    		return super.convert(type, v);
	    	}catch(NumberFormatException e){
	    		return super.convert(type, value);
	    	}
	    }

	}
}
