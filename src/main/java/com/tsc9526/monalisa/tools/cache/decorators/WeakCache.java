package com.tsc9526.monalisa.tools.cache.decorators;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;

import com.tsc9526.monalisa.tools.cache.Cache;

/**
 * Weak Reference cache decorator Thanks to Dr. Heinz Kabutz for his guidance
 * here.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class WeakCache implements Cache {
	private final LinkedList hardLinksToAvoidGarbageCollection;
	private final ReferenceQueue queueOfGarbageCollectedEntries;
	private final Cache delegate;
	private int numberOfHardLinks;

	public WeakCache(Cache delegate) {
		this.delegate = delegate;
		this.numberOfHardLinks = 256;
		this.hardLinksToAvoidGarbageCollection = new LinkedList();
		this.queueOfGarbageCollectedEntries = new ReferenceQueue();
	}

	public String getId() {
		return delegate.getId();
	}

	public int getSize() {
		removeGarbageCollectedItems();
		return delegate.getSize();
	}

	public void setSize(int size) {
		this.numberOfHardLinks = size;
	}

	public <T> T putObject(Object key, T value,long ttlInSeconds) {
		removeGarbageCollectedItems();
		delegate.putObject(key, new WeakEntry(key, value, queueOfGarbageCollectedEntries),ttlInSeconds);
		return value;
	}

	public <T> T getObject(Object key) {
		T result = null;
		WeakReference weakReference = (WeakReference) delegate.getObject(key);
		if (weakReference != null) {
			result = (T)weakReference.get();
			if (result == null) {
				delegate.removeObject(key);
			} else {
				hardLinksToAvoidGarbageCollection.addFirst(result);
				if (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
					hardLinksToAvoidGarbageCollection.removeLast();
				}
			}
		}
		return result;
	}

	public <T> T removeObject(Object key) {
		removeGarbageCollectedItems();
		return delegate.removeObject(key);
	}

	public void clear() {
		hardLinksToAvoidGarbageCollection.clear();
		removeGarbageCollectedItems();
		delegate.clear();
	}

	public ReadWriteLock getReadWriteLock() {
		return delegate.getReadWriteLock();
	}

	private void removeGarbageCollectedItems() {
		WeakEntry sv;
		while ((sv = (WeakEntry) queueOfGarbageCollectedEntries.poll()) != null) {
			delegate.removeObject(sv.key);
		}
	}

	private static class WeakEntry extends WeakReference {
		private final Object key;

		private WeakEntry(Object key, Object value, ReferenceQueue garbageCollectionQueue) {
			super(value, garbageCollectionQueue);
			this.key = key;
		}
	}

}
