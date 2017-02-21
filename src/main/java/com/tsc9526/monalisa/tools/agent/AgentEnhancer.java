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
package com.tsc9526.monalisa.tools.agent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.tsc9526.monalisa.orm.annotation.Interceptors;
import com.tsc9526.monalisa.orm.annotation.Tx;
import com.tsc9526.monalisa.tools.Tasks;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentEnhancer implements MethodInterceptor {

	@SuppressWarnings({"unchecked","rawtypes"})
	public <T> T createProxyInstance(Class<T> theClass) {
		Enhancer enhancer = new Enhancer() {
			protected void filterConstructors(Class sc, List constructors) {
				// don't filter, so that even classes without
				// visible constructors will work
			}
		};
		
		enhancer.setSuperclass(theClass);
		 
		enhancer.setCallback(new AgentEnhancer());
		 
		return (T) enhancer.create();
	}

	

	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		Tx tx=method.getAnnotation(Tx.class);
		if (tx!= null) {
			final List<Object> r = new ArrayList<Object>();

			com.tsc9526.monalisa.orm.Tx.execute(new com.tsc9526.monalisa.orm.Tx.Atom() {
				public int execute() throws Throwable {
					Object v = doInterceptors(obj,method,args,proxy);
					r.add(v);

					return 0;
				}
			},tx.level());

			return r.get(0);
		} else {
			if(Tasks.instance.isDestoried() && method.getName().equals("finalize")){
				return null;
			}else{
				return doInterceptors(obj,method,args,proxy);
			}
		}
	}
	
	private Object doInterceptors(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)throws Throwable {
		List<Interceptor> calls=new ArrayList<Interceptor>();
		
		Interceptors interceptors=method.getAnnotation(Interceptors.class);
		if(interceptors!=null){
			for(Class<? extends Interceptor> cx:interceptors.value()){
				calls.add(cx.newInstance());
			}
		}
		
		for(Interceptor i:calls){
			i.before(obj, method, args);
		}
		
		Object v = proxy.invokeSuper(obj, args);
		
		for(Interceptor i:calls){
			Object rx=i.after(obj, method, args,v);
			if(rx!=null){
				return rx;
			}
		}
		
		return v;
	}
}
