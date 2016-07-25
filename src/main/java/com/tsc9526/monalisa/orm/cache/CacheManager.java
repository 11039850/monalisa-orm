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
package com.tsc9526.monalisa.orm.cache;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.orm.cache.decorators.FifoCache;
import com.tsc9526.monalisa.orm.cache.decorators.LruCache;
import com.tsc9526.monalisa.orm.cache.decorators.SoftCache;
import com.tsc9526.monalisa.orm.cache.decorators.WeakCache;
import com.tsc9526.monalisa.orm.cache.impl.PerpetualCache;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheManager {
	private static CacheManager cm=new CacheManager();
 
	public static CacheManager getInstance(){
		return cm;
	}
	 
	private Map<String, Cache> hCaches=new ConcurrentHashMap<String, Cache>();
	  
	private CacheManager(){ 
		
	}
	 
	public Cache getCache(String cacheClass,String eviction,String name){
		String key=cacheClass+"#"+name;
		
		Cache cache=hCaches.get(key);
		
		if(cache==null){
			synchronized (hCaches) {
				if(!hCaches.containsKey(key)){
					cache=createCache(cacheClass,eviction,name);
					
					hCaches.put(key, cache);
				}else{
					cache=hCaches.get(key);
				}
			}
		}else{
			return cache;
		}
		  
		return cache;
	}
	
	
	
	private Cache createCache(String cacheClass,String eviction,String name){
		if(cacheClass!=null){
			try{
				Class<?> clazzCache=Class.forName(cacheClass);
				
				Constructor<?> cs=clazzCache.getConstructor(String.class);
				return createEvicate((Cache)cs.newInstance(name),eviction);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}else{
			return createEvicate(new PerpetualCache(name),eviction);
		}
	}
	
	private Cache createEvicate(Cache cache, String eviction){
		if(eviction.equalsIgnoreCase("FIFO")){
			cache=new FifoCache(cache);
		}else if(eviction.equalsIgnoreCase("LRU")){
			cache=new LruCache(cache);
		}else if(eviction.equalsIgnoreCase("SOFT")){
			cache=new SoftCache(cache);
		}else if(eviction.equalsIgnoreCase("WEAK")){
			cache=new WeakCache(cache);
		}else{
			try{
				Class<?> clazzEviction=Class.forName(eviction);
				
				Constructor<?> cs=clazzEviction.getConstructor(Cache.class);
				cache=(Cache)cs.newInstance(cache);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		return cache;
	}
}
