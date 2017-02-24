package com.tsc9526.monalisa.tools.cache;

import java.util.HashMap;
import java.util.Map;

import com.tsc9526.monalisa.tools.cache.decorators.TransactionalCache;

public class TransactionalCacheManager {

	private Map<Cache, TransactionalCache> transactionalCaches = new HashMap<Cache, TransactionalCache>();

	public void clear(Cache cache) {
		getTransactionalCache(cache).clear();
	}

	public void putObject(Cache cache, CacheKey key, Object value,long ttlInSeconds) {
		getTransactionalCache(cache).putObject(key, value,ttlInSeconds);
	}
	
	public Object getObject(Cache cache, CacheKey key) {
		return getTransactionalCache(cache).getObject(key);
	}

	public void commit() {
		for (TransactionalCache txCache : transactionalCaches.values()) {
			txCache.commit();
		}
	}

	public void rollback() {
		for (TransactionalCache txCache : transactionalCaches.values()) {
			txCache.rollback();
		}
	}

	private TransactionalCache getTransactionalCache(Cache cache) {
		TransactionalCache txCache = transactionalCaches.get(cache);
		if (txCache == null) {
			txCache = new TransactionalCache(cache);
			transactionalCaches.put(cache, txCache);
		}
		return txCache;
	}

}
