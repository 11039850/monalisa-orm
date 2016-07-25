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
package com.tsc9526.monalisa.orm.tools.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonNull;
import com.tsc9526.monalisa.orm.tools.converters.impl.ArrayTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.BigDecimalTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.BooleanTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.ByteTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.CharacterTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.DateTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.DoubleTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.EnumTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.FloatTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.IntegerTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.JsonObjectTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.LongTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.ObjectTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.SameTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.ShortTypeConversion;
import com.tsc9526.monalisa.orm.tools.converters.impl.StringTypeConversion;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class TypeConverter {
	static Logger logger=Logger.getLogger(TypeConverter.class);
	 
	public <T> T convert(Object v, Class<T> type) {
		if(type==null){
			return (T)v;
		}
		
		if(v==null || v instanceof JsonNull){
			return null;
		}
				 
		if(type.isInstance(v)){
			return (T)v;
		}else{
			return doConvert(v, type);
		}		 
	}
	  
	protected <T> T doConvert(Object value, Class<T> type){
		Conversion<?> conversion=null;
		if(type.isEnum()){
			conversion = getTypeConversion(Conversion.TYPE_ENUM, value);
		}else{	
			conversion = getTypeConversion(type, value); 
		}	
		
		if(conversion!=null){
			return (T)conversion.convert(value, type);
		}else{
			return null;
		}
	}	
	 
	public static void registerTypeConversion(Conversion<?> conversion) {
		Object[] keys = conversion.getTypeKeys();
		if (keys == null) {
			return;
		}

		for (int i = 0; i < keys.length; i++) {
			typeConversions.put(keys[i], conversion);
		}
	}
 
	public static void unregisterTypeConversion(Conversion<?> conversion) {
		if (conversion != null) {
			Object[] keys = conversion.getTypeKeys();
			 		
			if (keys != null) {
				for (int i = 0; i < keys.length; i++) {
					typeConversions.remove(keys[i]);
				}
			}
		}
	}
     	 
	protected  Conversion<?> getTypeConversion(Object typeKey, Object value) {
		// Check if the provided value is already of the target type
		if (typeKey instanceof Class && ((Class<?>) typeKey) != Object.class && ((Class<?>) typeKey).isInstance(value)) {
			return SAME_CONVERSION;
		}

		Conversion<?> conversion=typeConversions.get(typeKey);
		if(conversion==null && typeKey instanceof Class){
			if(((Class<?>)typeKey).isArray()){
				conversion=typeConversions.get(Conversion.TYPE_ARRAYS);
			}else{
				conversion=typeConversions.get(Conversion.TYPE_OBJECT);
			}
		}
		
		return conversion;
	}

	  
	private static final Map<Object, Conversion<?>> typeConversions = Collections.synchronizedMap(new HashMap<Object, Conversion<?>>());

	private static final Conversion<?> SAME_CONVERSION = new SameTypeConversion();

	static {
		registerTypeConversion(new BigDecimalTypeConversion());
		registerTypeConversion(new BooleanTypeConversion());
		registerTypeConversion(new ByteTypeConversion());
		registerTypeConversion(new CharacterTypeConversion());
		registerTypeConversion(new DoubleTypeConversion());
		registerTypeConversion(new FloatTypeConversion());
		registerTypeConversion(new IntegerTypeConversion());
		registerTypeConversion(new LongTypeConversion());
		registerTypeConversion(new ObjectTypeConversion());
		registerTypeConversion(new ShortTypeConversion());
		registerTypeConversion(new DateTypeConversion());
		registerTypeConversion(new StringTypeConversion());
		
		registerTypeConversion(new ArrayTypeConversion());
		registerTypeConversion(new EnumTypeConversion());
		registerTypeConversion(new JsonObjectTypeConversion());
	}
}
