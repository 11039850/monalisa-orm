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
package com.tsc9526.monalisa.orm.tools.helper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.orm.datatable.DataColumn;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.datatable.Page;
import com.tsc9526.monalisa.orm.tools.converters.Conversion;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JsonHelper {
	static{
		DynamicLibHelper.tryLoadGson();
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
		long total  =json.get("total").getAsLong();
	 
				 
		Page<DataMap> r= new Page<DataMap>();
		r.setRecords(records);
		r.setSize(size);
		r.setPage(page);
		r.setTotal(total);
		r.setRows(rows); 
		
		return r;
	}	
	
	public static DataMap parseToDataMap(JsonObject json){
		Gson gson = createGsonBuilder()
				.registerTypeAdapter(new TypeToken<DataMap>(){}.getType(),new JsonDeserializer<DataMap>() {
					public DataMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
						DataMap map = new DataMap();
		                JsonObject jsonObject = json.getAsJsonObject();
		                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		                for (Map.Entry<String, JsonElement> entry : entrySet) {
		                	JsonElement e=entry.getValue();
		                	if(e.isJsonPrimitive()){
		                		JsonPrimitive p=(JsonPrimitive)e;
		                		if(p.isString()){
		                			map.put(entry.getKey(), p.getAsString());
		                		}else{
		                			map.put(entry.getKey(), p);
		                		}
		                	}else{
		                		map.put(entry.getKey(), entry.getValue());
		                	}
		                }
		                return map;
		            }
		        }).create();
		
		DataMap data = gson.fromJson(json, new TypeToken<DataMap>(){}.getType());
		return data;
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
					MetaClass mc=ClassHelper.getMetaClass(v.getClass());
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
