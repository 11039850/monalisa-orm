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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheKey {
	private static final int DEFAULT_MULTIPLYER = 37;
	private static final int DEFAULT_HASHCODE = 17;

	private int multiplier=DEFAULT_MULTIPLYER;
	private int hashcode=DEFAULT_HASHCODE;
	private int  count=0;
	private long checksum;
	
	private List<Object> updateList=new ArrayList<Object>();

	private Object tag;
	
	public CacheKey() {
	}

	public CacheKey(Object... objects) {
		update(objects);
	}

	public int getUpdateCount() {
		return updateList.size();
	}

	public void update(Object... objects) {
		for (Object o : objects) {
			updateKey(o);
		}
	}

	private void updateKey(Object object) {
		int baseHashCode = object == null ? 1 : object.hashCode();

		count++;
		checksum += baseHashCode;
		baseHashCode *= count;

		hashcode = multiplier * hashcode + baseHashCode;

		updateList.add(object);
	}

	
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof CacheKey))
			return false;

		final CacheKey cacheKey = (CacheKey) object;

		if (hashcode != cacheKey.hashcode)
			return false;
		if (checksum != cacheKey.checksum)
			return false;
		if (count != cacheKey.count)
			return false;

		for (int i = 0; i < updateList.size(); i++) {
			Object thisObject = updateList.get(i);
			Object thatObject = cacheKey.updateList.get(i);
			if (thisObject == null) {
				if (thatObject != null)
					return false;
			} else {
				if (!thisObject.equals(thatObject))
					return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T> T getTag() {
		return (T)tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	
	public int hashCode() {
		return hashcode;
	}

	public String toString() {
		StringBuffer returnValue = new StringBuffer().append(hashcode).append(':').append(checksum);
		for (int i = 0; i < updateList.size(); i++) {
			returnValue.append(':').append(updateList.get(i));
		}

		return returnValue.toString();
	}
 	
}
