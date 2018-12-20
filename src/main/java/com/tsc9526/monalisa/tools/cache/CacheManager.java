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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.tsc9526.monalisa.tools.Tools;
import com.tsc9526.monalisa.tools.cache.decorators.FifoCache;
import com.tsc9526.monalisa.tools.cache.decorators.LruCache;
import com.tsc9526.monalisa.tools.cache.decorators.SoftCache;
import com.tsc9526.monalisa.tools.cache.decorators.WeakCache;
import com.tsc9526.monalisa.tools.cache.impl.PerpetualCache;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheManager { 
	static Logger logger = Logger.getLogger(CacheManager.class);
	 
	private static CacheManager cm=new CacheManager();
 
	public static CacheManager getInstance(){
		return cm;
	}
	
	private ThreadPoolExecutor pool;
	
	private int refreshCachePoolSize = 5;
	
	private Map<String, Cache> hCaches = new ConcurrentHashMap<String, Cache>();
	
	protected Timer                 autoRefreshTimer = new Timer("Monalisa-CacheRefresh-Timer",true);
	 
	private Map<CacheKey,TimerTask> refreshCacheKeys = new ConcurrentHashMap<CacheKey,TimerTask>(); 
	private Map<CacheKey,String>    runningCacheKeys = new ConcurrentHashMap<CacheKey,String>(); 
	
	private Cache defaultCache = getCache(PerpetualCache.class.getName(), "LRU", "default"); 
	 
	private CacheManager(){  
	}
	
	public void setAutoRefreshThreads(int threads) {
		refreshCachePoolSize = threads;
	}
	
	protected synchronized ExecutorService getExecutorPool() {
		if(pool==null) {
			logger.info("Init refresh cache thread pool size: "+refreshCachePoolSize);
			
			pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(refreshCachePoolSize);
		}
		
		return pool;
	}
	
	public boolean addAutoRefreshCache(CacheKey key,Cache cache, Cacheable cacheable,long ttlInMillis, long autoRefreshInMillis) {
		if(!refreshCacheKeys.containsKey(key)) { 
		 	TimerTask timerTask = createRefreshTimerTask(key, cache, cacheable, ttlInMillis);
		 	TimerTask existTask = refreshCacheKeys.putIfAbsent(key, timerTask);
			
		 	if(existTask == null ) { 
		 		logger.debug("Add auto refresh cache key: "+key);
		 		
				autoRefreshTimer.schedule(timerTask, autoRefreshInMillis, autoRefreshInMillis);
				
				return true;
			}
		}
		
		return false;
	}
 
	
	protected TimerTask createRefreshTimerTask(final CacheKey key,final Cache cache, final Cacheable cacheable,final long ttlInMillis) {
		final Runnable r= new Runnable() {
			@Override
			public void run() {
				try {
					long ts = System.currentTimeMillis();
					
					Object value = cacheable.execute();
					cache.putObject(key, value, ttlInMillis);
					
					long delta = System.currentTimeMillis() -ts;
					
					if(logger.isDebugEnabled()) {
						logger.debug("Auto refresh cache("+ delta+" ms), "+Tools.getCachedInfo(key, value,ttlInMillis));
					}
				}finally {
					runningCacheKeys.remove(key);
				}
			}
		};	
		
		return new TimerTask() {
			@Override
			public void run() {
				String exists = runningCacheKeys.putIfAbsent(key, key.toString());
				if(exists==null) {
					getExecutorPool().submit(r);
				}
			}
		};
	}
	 
	public boolean removeAutoRefreshCache(CacheKey key) {
		TimerTask task = refreshCacheKeys.remove(key);
		if(task!=null) {
			logger.debug("Remove auto refresh cache key: "+key);
			
			task.cancel();
			
			autoRefreshTimer.purge();
			
			return true;
		}
		
		return false;
	}
	
	public List<CacheKey> getAutoRefreshCacheKeys() {
		List<CacheKey> keys = new ArrayList<CacheKey>();
		keys.addAll( refreshCacheKeys.keySet() );
		return keys;
	}
	  
	
	public CacheKey getCacheKeyByTag(Object tag){
		 List<CacheKey> rs = findCacheKeysByTag(tag);
		 if(rs.size()>0) {
			 return rs.get(0);
		 }
		 
		 return null;
	}
	
	public List<CacheKey> findCacheKeysByTag(Object tag){
		List<CacheKey> rs = new ArrayList<CacheKey>();
		
		List<Object> keys = new ArrayList<Object>();
		keys.addAll(getCacheKeys());
		
		for(Object key:keys) {
			if(key instanceof CacheKey) {
				CacheKey cacheKey     = (CacheKey)key;
				Object   cacheKeyTag  = cacheKey.getTag();
				
				if(tag==null) {
					if(cacheKeyTag==null) {
						rs.add(cacheKey);
					}
				}else if(tag.equals(cacheKeyTag)) {
					rs.add(cacheKey);
				}
			}
		}
		
		return rs;
	}
	
	public List<Object> evictCacheKeys(List<Object> keys) {
		return evictCacheKeys ( keys.toArray(new Object[keys.size()]) );
	}
	
	public List<Object> evictCacheKeys(Object... keys) {
		List<Object> rs = new ArrayList<Object>();
		for(Object key:keys) {
			rs.add ( defaultCache.removeObject(key) );
		}
		return rs;
	}
	
	public int getAutoRefreshActiveCount(){
		return pool.getActiveCount();
	}
	
	public void shutdown() {
		autoRefreshTimer.cancel();
		
		if(pool!=null) {
			pool.shutdownNow();
		}
	}
	
	public List<Object> getCacheKeys(){
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
				return createEvictCache((Cache)cs.newInstance(name),eviction);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		return defaultCache;
	}
	
	private Cache createEvictCache(Cache cache, String eviction){
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
