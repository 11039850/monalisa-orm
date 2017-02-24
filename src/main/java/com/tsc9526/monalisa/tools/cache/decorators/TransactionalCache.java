package com.tsc9526.monalisa.tools.cache.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import com.tsc9526.monalisa.tools.cache.Cache;

public class TransactionalCache implements Cache {

	private Cache delegate;
	private boolean clearOnCommit;
	private Map<Object, AddEntry> entriesToAddOnCommit;
	private Map<Object, RemoveEntry> entriesToRemoveOnCommit;

	public TransactionalCache(Cache delegate) {
		this.delegate = delegate;
		this.clearOnCommit = false;
		this.entriesToAddOnCommit = new HashMap<Object, AddEntry>();
		this.entriesToRemoveOnCommit = new HashMap<Object, RemoveEntry>();
	}

	public String getId() {
		return delegate.getId();
	}

	public int getSize() {
		return delegate.getSize();
	}

	public Object getObject(Object key) {
		if(entriesToAddOnCommit.containsKey(key)){
			return entriesToAddOnCommit.get(key).value;
		}
		
		return delegate.getObject(key);
	}

	public ReadWriteLock getReadWriteLock() {
		return delegate.getReadWriteLock();
	}

	public void putObject(Object key, Object object,long ttlInSeconds) {
		entriesToRemoveOnCommit.remove(key);
		entriesToAddOnCommit.put(key, new AddEntry(delegate, key, object,ttlInSeconds));
	}

	public Object removeObject(Object key) {
		entriesToAddOnCommit.remove(key);
		entriesToRemoveOnCommit.put(key, new RemoveEntry(delegate, key));
		return delegate.getObject(key);
	}

	public void clear() {
		reset();
		clearOnCommit = true;
	}

	public void commit() {
		delegate.getReadWriteLock().writeLock().lock();
		try {
			if (clearOnCommit) {
				delegate.clear();
			} else {
				for (RemoveEntry entry : entriesToRemoveOnCommit.values()) {
					entry.commit();
				}
			}
			for (AddEntry entry : entriesToAddOnCommit.values()) {
				entry.commit();
			}
			reset();
		} finally {
			delegate.getReadWriteLock().writeLock().unlock();
		}
	}

	public void rollback() {
		reset();
	}

	private void reset() {
		clearOnCommit = false;
		entriesToRemoveOnCommit.clear();
		entriesToAddOnCommit.clear();
	}

	private static class AddEntry {
		private Cache cache;
		private Object key;
		private Object value;
		private long ttlInSeconds;
		
		public AddEntry(Cache cache, Object key, Object value,long ttlInSeconds) {
			this.cache = cache;
			this.key = key;
			this.value = value;
			this.ttlInSeconds=ttlInSeconds;
		}

		public void commit() {
			cache.putObject(key, value,ttlInSeconds);
		}
	}

	private static class RemoveEntry {
		private Cache cache;
		private Object key;

		public RemoveEntry(Cache cache, Object key) {
			this.cache = cache;
			this.key = key;
		}

		public void commit() {
			cache.removeObject(key);
		}
	}

}
