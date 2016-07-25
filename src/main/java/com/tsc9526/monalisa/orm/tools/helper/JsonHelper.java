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

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tsc9526.monalisa.orm.tools.converters.Conversion;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JsonHelper {
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
 	 

}
