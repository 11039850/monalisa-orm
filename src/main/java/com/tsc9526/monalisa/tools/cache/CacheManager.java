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
package com.tsc9526.monalisa.tools.cache;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tsc9526.monalisa.tools.cache.decorators.FifoCache;
import com.tsc9526.monalisa.tools.cache.decorators.LruCache;
import com.tsc9526.monalisa.tools.cache.decorators.SoftCache;
import com.tsc9526.monalisa.tools.cache.decorators.WeakCache;
import com.tsc9526.monalisa.tools.cache.impl.PerpetualCache;
import com.tsc9526.monalisa.tools.datatable.Page;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheManager {
	static Logger logger = Logger.getLogger(CacheManager.class);
	
	public static String getCachedInfo(CacheKey key,Object value,int ttlInSeconds) {
		String vm ="";
		if(value instanceof List) {
			vm = "list("+((List<?>)value).size()+")";
		}else if(value instanceof Page) {
			Page<?> page = (Page<?>)value;
			vm = "page("+page.getPage()+"/"+page.getTotal()+": "+(page.getRows()==null?0:page.getRows().size());
		}else {
			vm = ""+value;
		}
		
		return "ttl: "+ttlInSeconds+"s, key: "+key+", value: "+vm;
	}
	
	private static CacheManager cm=new CacheManager();
 
	public static CacheManager getInstance(){
		return cm;
	}
	
	private ExecutorService pool;
	
	private int refreshCachePoolSize = 3;
	
	private Map<String, Cache> hCaches = new ConcurrentHashMap<String, Cache>();
	
	protected Timer   timer = new Timer("Monalisa-CacheRefresh-Timer",true);
	 
	private Map<CacheKey,String> refreshCacheKeys = new ConcurrentHashMap<CacheKey,String>(); 
		
	private Cache defaultCache = getCache(PerpetualCache.class.getName(), "LRU", "default"); 
	
	private CacheManager(){  
	}
	
	public void setAutoRefreshThreads(int threads) {
		refreshCachePoolSize = threads;
	}
	
	protected synchronized ExecutorService getExecutorPool() {
		if(pool==null) {
			pool = Executors.newFixedThreadPool(refreshCachePoolSize);
		}
		
		return pool;
	}
	
	public boolean addAutoRefreshCache(final CacheKey key,final Cache cache, final Cacheable cacheable,final int ttlInSeconds, int autoRefreshInSeconds) {
		String cacheTime = ttlInSeconds+":"+autoRefreshInSeconds;
		String old       = refreshCacheKeys.putIfAbsent(key, cacheTime);
		
		if(old == null) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					Object value = cacheable.execute();
					cache.putObject(key, value, ttlInSeconds);
					
					if(logger.isDebugEnabled()) {
						logger.debug("Auto refresh cache, "+getCachedInfo(key, value,ttlInSeconds));
					}
				}
			};
			
			long period = autoRefreshInSeconds *1000;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					getExecutorPool().submit(r);
				}
			}, period, period);
			
			return true;
		}
		
		return false;
	}
	
	
	
	public void shutdown() {
		timer.cancel();
		
		if(pool!=null) {
			pool.shutdownNow();
		}
	}
	
	public List<Object> keys(){
		return defaultCache.keys();
	}
	
	public void setDefaultCache(Cache cache) {
		defaultCache = cache;
	}
	
	public Cache getDefaultCache() {
		return defaultCache;
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
		if(cacheClass!=null && cacheClass.length()>0){
			try{
				Class<?> clazzCache=Class.forName(cacheClass);
				
				Constructor<?> cs=clazzCache.getConstructor(String.class);
				return createEvicate((Cache)cs.newInstance(name),eviction);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		return defaultCache;
	}
	
	private Cache createEvicate(Cache cache, String eviction){
		if(eviction.equalsIgnoreCase("FIFO")){
			return new FifoCache(cache);
		}else if(eviction.equalsIgnoreCase("LRU")){
			return new LruCache(cache);
		}else if(eviction.equalsIgnoreCase("SOFT")){
			return new SoftCache(cache);
		}else if(eviction.equalsIgnoreCase("WEAK")){
			return new WeakCache(cache);
		}
		
		return cache;
	}
}
