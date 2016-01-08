package com.tsc9526.monalisa.core.convert;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.tsc9526.monalisa.core.tools.ClassHelper.DateValue;
import com.tsc9526.monalisa.core.tools.EnumHelper;
import com.tsc9526.monalisa.core.tools.JsonHelper;

@SuppressWarnings("unchecked")
public class DefaultConverter implements Converter{
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
			return doConvert(v, type);
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
	
	protected <T> T convertOthers(Object v, Class<T> type){
		return (T)ConvertUtils.convert(v, type);
	}
	
	protected <T> T doConvert(Object v, Class<T> type){
		Object value=null;
		
		if(type.isEnum()){
			value=convertToEnum(v,type);
		}else if(type==JsonObject.class){
			value=convertToJsonObject(v,type);
		}else{
			if(v.getClass().isArray() && type == String.class){
				value=convertArrayToString((Object[])v);						
			}else if(Map.class.isAssignableFrom(v.getClass()) && type == String.class){
				value=convertMapToString((Map<?,?>)v);						
			}else if(v.getClass().isArray()==false && type.isArray()){
				value=convertJsonToArray(v,type);
			}else if(type.isArray()==false 
					&& type.isPrimitive()==false 
					&& type.getName().startsWith("java.")==false
					&& (v.getClass() == String.class || v instanceof JsonElement)){
				value=convertStringToBean(v,type);
			}else{		
				if(v instanceof JsonPrimitive){
					v=JsonHelper.getGson().fromJson((JsonElement)v, String.class);
				}
				value=convertOthers(v, type);
			}
		}
		
		return (T)value;
	}	
}
