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
package com.tsc9526.monalisa.tools.string;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpLib;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassAssist;
import com.tsc9526.monalisa.tools.converters.Conversion;
import com.tsc9526.monalisa.tools.datatable.DataColumn;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpJson {
	static{
		MelpLib.tryLoadGson();
	}
	
	private static GsonBuilder gb=createGsonBuilder();
	
	public static GsonBuilder createGsonBuilder(){
		return new GsonBuilder()
			.registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {   
			    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
			        if(src == src.longValue()){
			            return new JsonPrimitive(src.longValue());      
			        }
			        return new JsonPrimitive(src);
			    }
			})
			.setExclusionStrategies(new ExclusionStrategy() {
				public boolean shouldSkipField(FieldAttributes fa) {
					return fa.getName().startsWith("$");
				}
	
				public boolean shouldSkipClass(Class<?> clazz) {
					return false;
				}
			})
			.setDateFormat(Conversion.DEFAULT_DATETIME_FORMAT);
	}
	
	public static Gson getGson(){	
		return gb.create();   
	}
	 
	public static String toJson(Gson gson,Object bean) {
		JsonObject json=new JsonObject(); 
		
		ClassAssist mc=MelpClass.getClassAssist(bean);
		
		for (FGS fgs : mc.getFields()) {
			String name=fgs.getFieldName();
			 
			Object v = fgs.getObject(bean);
			if (v != null) {
				JsonElement e=null;
				if(v instanceof JsonElement){
					e=(JsonElement)v;
				}else{
					e=gson.toJsonTree(v);					 
				}				
				json.add(name, e);
			}			 
		} 		
		return gson.toJson(json);	
	}
	
	public static String getString(JsonObject json,String name){
		return getString(json, name,null);
	}
	
	public static String getString(JsonObject json,String name,String defaultValue){
		JsonElement e=json.get(name);
		if(e==null || e.isJsonNull()){
			return defaultValue;
		}else{
			return e.getAsString();
		}
	}
	
	public static int getInt(JsonObject json,String name,int defaultValue){
		JsonElement e=json.get(name);
		if(e==null || e.isJsonNull()){
			return defaultValue;
		}else{
			return e.getAsInt();
		}
	}
	
	public static double getDouble(JsonObject json,String name,double defaultValue){
		JsonElement e=json.get(name);
		if(e==null || e.isJsonNull()){
			return defaultValue;
		}else{
			return e.getAsDouble();
		}
	}
	
	public static DataTable<DataMap> parseToDataTable(JsonArray array){
		DataTable<DataMap> table=new DataTable<DataMap>();
		
		for(int i=0;i<array.size();i++){
			DataMap data=parseToDataMap(array.get(i).getAsJsonObject());
			
			table.add(data);		
		}
		return table;
	}	
	
	public static Page<DataMap> parseToPage(JsonObject json){
		DataTable<DataMap> rows=parseToDataTable(json.get("rows").getAsJsonArray());
		
		long records=json.get("records").getAsLong();
		long size   =json.get("size").getAsLong();
		long page   =json.get("page").getAsLong();
	 
		long offset=(page-1)*size;
		Page<DataMap> r= new Page<DataMap>(rows,records,size,offset);
		
		return r;
	}	
	
	public static DataMap parseToDataMap(String json){
		JsonElement je=new JsonParser().parse(json);
		if(je.isJsonObject()){
			return parseToDataMap(je.getAsJsonObject());
		}else{
			return null;
		}
	}
 	
	public static DataMap parseToDataMap(JsonObject jsonObject) throws JsonParseException {
		DataMap map = new DataMap();
       
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
        	JsonElement e=entry.getValue();
        	Object v=toObject(e);
        	map.put(entry.getKey(),v);
        }
        return map;
    }
	
	private static Object toObject(JsonElement e){
		if(e==null || e.isJsonNull()){
			return null;
		}else if(e.isJsonPrimitive()){
			JsonPrimitive x=(JsonPrimitive)e;
			
			if(x.isNumber()){
				return e.getAsNumber();
			}else{
				return e.getAsString();
			}
		}else if(e.isJsonObject()){
			return parseToDataMap(e.getAsJsonObject());
		}else if(e.isJsonArray()){
			List<Object> list=new ArrayList<Object>();
			
			JsonArray array=(JsonArray)e;
    		for(int i=0;i<array.size();i++){
    			JsonElement je=array.get(i);
    			
    			Object v=toObject(je);
    			
    			list.add(v);
    		}
    		return list;
		}else{
			return e;
		}   
	}
	
	public static void writeJson(JsonWriter w,DataTable<?> table,boolean close){
		try{
			List<DataColumn> headers=table.getHeaders();
		 	 
			w.beginObject();
			w.name("header");
			
			w.beginArray();
			for(DataColumn c:headers){
				w.value(c.getName());
			}
			w.endArray();
			
			w.name("data");
			w.beginArray();
			for(Object v:table){
				w.beginArray();
				writeValue(w,headers,v);
				w.endArray();
			}
			w.endArray();
			
			w.endObject();
			
			if(close){
				w.close();
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	private static void writeValue(JsonWriter w,List<DataColumn> headers,Object v)throws IOException{
		if(v!=null){
			if(v instanceof Map){
				for(DataColumn c:headers){
					Object o=((Map<?,?>)v).get(c.getName());
					doWriteValue(w,o);
				} 
			}else{
				if(v.getClass().isPrimitive() || v.getClass().getName().startsWith("java.")){
					doWriteValue(w,v);
				}else if(v.getClass().isArray()){
					Object[] xs=(Object[])v;
					for(int k=0;k<xs.length;k++){
						doWriteValue(w,xs[k]);
					}
				}else{	
					ClassAssist mc=MelpClass.getClassAssist(v.getClass());
					for(DataColumn c:headers){
						FGS fgs=mc.getField(c.getName());
						Object o=null;
						if(fgs!=null){
							o=fgs.getObject(v);
						}
						 
						doWriteValue(w,o);
					} 
				}
			}
		}
	}
	
	private static void doWriteValue(JsonWriter w,Object obj)throws IOException{
		if(obj instanceof Number){
			w.value((Number)obj);
		}else{
			w.value(obj==null?null:obj.toString());
		}
	}
 	 

}
