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
package com.tsc9526.monalisa.orm.tools.helper;

import java.util.HashMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
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
