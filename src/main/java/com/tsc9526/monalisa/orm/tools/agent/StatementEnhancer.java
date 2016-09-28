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
package com.tsc9526.monalisa.orm.tools.agent;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.List;

import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class StatementEnhancer {

	@SuppressWarnings("rawtypes")
	public static Class<?> createClass(Class<?> clazz) {
		if (Statement.class.isAssignableFrom(clazz) && clazz.isInterface() == false) {
			Enhancer enhancer = new Enhancer() {
				protected void filterConstructors(Class sc, List constructors) {
					// don't filter, so that even classes without visible
					// constructors will work
				}
			};

			enhancer.setSuperclass(clazz);
			enhancer.setInterfaces(new Class<?>[] { Statement.class });

			enhancer.setCallbackTypes(new Class[] { Interceptor.class });
			enhancer.setCallbackFilter(new InterceptorFilter());

			return enhancer.createClass();
		} else {
			return clazz;
		}
	}

	public static class Interceptor implements MethodInterceptor {
		public Object intercept(Object enhanced, Method method, Object[] args, MethodProxy fastProxy) throws Throwable {
			System.err.println("intercept Object");
			return method.invoke(enhanced, args);
		}
	}

	static class InterceptorFilter implements CallbackFilter {

		public InterceptorFilter() {

		}

		public int accept(Method method) {
			return 0;
		}
	}

}
