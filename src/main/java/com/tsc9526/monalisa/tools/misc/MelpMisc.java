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
package com.tsc9526.monalisa.tools.misc;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.List;

import com.tsc9526.monalisa.tools.converters.impl.ArrayTypeConversion;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpMisc {
	private MelpMisc() {}
	
	/**
	 * Empty checking:  
	 * <ol>
	 * 	<li> null object </li>
	 * 	<li> empty collection</li>
	 * 	<li> empty array: array[0]</li>
	 * 	<li> empty string: "" ,  "  " , "null"</li>
	 * </ol> 
	 * @param obj : the object to check
	 * @return true if the object is empty
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof Collection) {
			return  ((Collection<?>) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			Object[] vs = null;
			if(obj.getClass().getComponentType().isPrimitive()){
				ArrayTypeConversion conversion = new ArrayTypeConversion();
				
				vs = (Object[])conversion.convert(obj, Object[].class);
			}else {
				vs = (Object[]) obj;
			}
					
			return vs.length == 0;
		}else if (obj instanceof String) {
			String x = obj.toString();

			return "null".equalsIgnoreCase(x) || x.trim().length()==0;
		}

		return false;
	}
	
	public static String getPid() {
		String vmName = ManagementFactory.getRuntimeMXBean().getName();
		int p = vmName.indexOf('@');
		return vmName.substring(0, p);
	}

	public static boolean inDebug() {
		List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();

		for (String arg : args) {
			if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
				return true;
			}
		}
		return false;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
