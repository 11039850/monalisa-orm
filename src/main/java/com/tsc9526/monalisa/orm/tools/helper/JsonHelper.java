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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import com.tsc9526.monalisa.orm.datatable.DataColumn;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.tools.converters.Conversion;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JsonHelper {
	static{
		DynmicLibHelper.tryLoadGson();
	}
	
	private static GsonBuilder gb=new GsonBuilder().registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {   
	    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
	        if(src == src.longValue()){
	            return new JsonPrimitive(src.longValue());      
	        }
	        return new JsonPrimitive(src);
	    }
	 }).setDateFormat(Conversion.DEFAULT_DATETIME_FORMAT);
	 
	
	public static Gson getGson(){		
		return gb.create();   
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
