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
package com.tsc9526.monalisa.tools.datatable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CaseInsensitiveMap<T> extends LinkedHashMap<String, T> {
	private static final long serialVersionUID = -5859767087732654080L;

	private transient Map<String,String> keys=new HashMap<String,String>();
	
	public CaseInsensitiveMap(){
	}
	
	public CaseInsensitiveMap(int initialCapacity) {
        super(initialCapacity);
    }
	
	public T put(String key, T value) {
		if(key==null){
			return super.put(key, value);
		}else{
			String lower=key.toLowerCase();
			
			String origin=getKeys().get(lower);
			if(origin==null){
				origin=key;
				keys.put(lower, origin);
			}
			
			return super.put(origin, value);
		}
	}
	
	
	public void putAll(Map<? extends String,? extends T> m) {
		for(Map.Entry<? extends String,? extends T> entry:m.entrySet()){
			String key   = entry.getKey();
			T      value = entry.getValue();
			
			put(key, value);
		}      
	}
	 
	@Override
	public T get(Object key) {
		return super.get(convertKey(key));
	}

	public boolean containsKey(Object key) {
		return super.containsKey(convertKey(key));
	}

	public T remove(Object key){
		return super.remove(convertKey(key));
	}
	 
	protected String convertKey(Object key) {
		if (key != null) {
			String lower=key.toString().toLowerCase();
			String origin=getKeys().get(lower);
			if(origin==null){
				origin=key.toString();
			}
			 
			return origin;
		} else {
			return null;
		}
	}
	
	protected Map<String,String> getKeys(){
		// keys may be null if deserialize object, so reconstruct it
		if(keys==null){
			keys=new HashMap<String,String>();
			for(String origin:keySet()){
				if(origin!=null){
					keys.put(origin.toLowerCase(), origin);
				}
			}
		}
		return keys;
	}
	
	@Override
	public boolean equals(Object other){
		return super.equals(other);
	}
	
	@Override
	public int hashCode(){
		return super.hashCode();
	}
}
