package com.tsc9526.monalisa.tools.cache.impl;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.tsc9526.monalisa.tools.cache.Cache;

@SuppressWarnings("unchecked")
public class PerpetualCache implements Cache {

	private String id;

	private Map<Object, CacheObject> cache = new ConcurrentHashMap<Object, CacheObject>();

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public PerpetualCache(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getSize() {
		return cache.size();
	}

	public <T> T putObject(Object key, T value,long ttlInSeconds) {
		cache.put(key, new CacheObject(value,ttlInSeconds));
		return value;
	}

	public <T> T getObject(Object key) {
		CacheObject o =  cache.get(key);
		if(o!=null && !o.isExpired()) {
			return (T)o.data;	
		}
		
		return null;	
	}

	public List<Object> keys(){
		List<Object> s = new ArrayList<Object>();
		s.addAll( cache.keySet() );
		return s;
	}
	
	public <T> T removeObject(Object key) {
		return (T)cache.remove(key);
	}

	public void clear() {
		cache.clear();
	}

	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	public boolean equals(Object o) {
		if (getId() == null){
			throw new RuntimeException("Cache instances require an ID.");
		}
		
		if (this == o){
			return true;
		}
		
		if (!(o instanceof Cache)){
			return false;
		}

		Cache otherCache = (Cache) o;
		return getId().equals(otherCache.getId());
	}

	public int hashCode() {
		if (getId() == null){
			throw new RuntimeException("Cache instances require an ID.");
		}
		
		return getId().hashCode();
	}
	
	static class CacheObject{
		Object data;
		long   expiredTime;
		long   createTime;
		
		CacheObject(Object data,long ttlInSeconds){
			this.data         = data;
			this.createTime   = System.currentTimeMillis();
			this.expiredTime  = createTime + ttlInSeconds*1000;	
		}
		
		public boolean isExpired() {
			return expiredTime < System.currentTimeMillis();
		}
	}

}
