package com.tsc9526.monalisa.tools.cache.decorators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import com.tsc9526.monalisa.tools.cache.Cache;

/**
 * Least Recently Used cache decorator
 */
public class LruCache implements Cache {

	private final Cache delegate;
	private Map<Object, Object> keyMap;
	private Object eldestKey;

	public LruCache(Cache delegate) {
		this.delegate = delegate;
		setSize(1024);
	}

	public String getId() {
		return delegate.getId();
	}

	public int getSize() {
		return delegate.getSize();
	}

	public void setSize(final int size) {
		keyMap = new LinkedHashMap<Object, Object>(size, .75F, true) {
			private static final long serialVersionUID = 4267176411845948333L;

			protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
				boolean tooBig = size() > size;
				if (tooBig) {
					eldestKey = eldest.getKey();
				}
				return tooBig;
			}
		};
	}

	public <T> T putObject(Object key, T value,long ttlInSeconds) {
		delegate.putObject(key, value,ttlInSeconds);
		cycleKeyList(key);
		return value;
	}

	public <T> T getObject(Object key) {
		keyMap.get(key); // touch
		return delegate.getObject(key);

	}

	public <T> T removeObject(Object key) {
		return delegate.removeObject(key);
	}

	public void clear() {
		delegate.clear();
		keyMap.clear();
	}

	public ReadWriteLock getReadWriteLock() {
		return delegate.getReadWriteLock();
	}

	private void cycleKeyList(Object key) {
		keyMap.put(key, key);
		if (eldestKey != null) {
			delegate.removeObject(eldestKey);
			eldestKey = null;
		}
	}

}
