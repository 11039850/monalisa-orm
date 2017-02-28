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
package com.tsc9526.monalisa.tools.parser.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsc9526.monalisa.tools.parser.Parser;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JsonObjectParser implements Parser<JsonObject>{			 
	public boolean parse(Object target, JsonObject json, String... mappings) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(mappings.length>0 && mappings[0].startsWith("/")){
			String name=mappings[0].substring(1);
			for(String n:name.split("/")){
				json=json.get(n).getAsJsonObject();
			}
		}
		
		Iterator<Map.Entry<String, JsonElement>> es=json.entrySet().iterator();
		while(es.hasNext()){
			Entry<String, JsonElement> e=es.next();
			
			data.put(e.getKey(),e.getValue());
		}			
		return new MapParser().parse(target, data, mappings);
	}		
}