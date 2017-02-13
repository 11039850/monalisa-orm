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
package com.tsc9526.monalisa.tools.parser.impl;

import java.util.Map;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.parser.Parser;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MapParser implements Parser<Map<String,Object>>{		
	public boolean parse(Object target, Map<String,Object> data, String... mappings) {
		KeyMapping map=new KeyMapping(data,mappings);
		
		for(FGS fgs:MelpClass.getFields(target)){
			fgs.mapto(map, target);
		}
		return true;
	}		
}