package com.tsc9526.monalisa.core.tools;

import java.util.HashMap;

public class CaseInsensitiveMap<T> extends HashMap<String, T> {

	private static final long serialVersionUID = -5859767087732654080L;

	public T put(String key, T value) {
		return super.put(convertKey(key), value);
	}

	public T get(Object key) {
		return super.get(convertKey(key));
	}

	public boolean containsKey(Object key) {
		return super.containsKey(convertKey(key));
	}

	protected String convertKey(Object key) {
		if (key != null) {
			return key.toString().toLowerCase();
		} else {
			return null;
		}
	}
}
