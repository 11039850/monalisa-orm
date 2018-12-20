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
package com.tsc9526.monalisa.tools;

import java.util.List;

import com.tsc9526.monalisa.tools.cache.CacheKey;
import com.tsc9526.monalisa.tools.datatable.Page;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Tools {
	private Tools() {}
	
	public static String getCachedInfo(CacheKey key,Object value,long ttlInMillis) {
		String vm ="";
		if(value instanceof List) {
			vm = "list("+((List<?>)value).size()+")";
		}else if(value instanceof Page) {
			Page<?> page = (Page<?>)value;
			vm = "page("+page.getPage()+"/"+page.getTotal()+": "+(page.getRows()==null?0:page.getRows().size());
		}else {
			vm = ""+value;
		}
		
		return "ttl: "+ttlInMillis+" ms, key: "+key+", value: "+vm;
	}
}
