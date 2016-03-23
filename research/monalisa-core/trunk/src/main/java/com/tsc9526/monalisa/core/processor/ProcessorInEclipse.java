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
package com.tsc9526.monalisa.core.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeProcessingEnvImpl;
import org.eclipse.jdt.launching.JavaRuntime;

import com.tsc9526.monalisa.core.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.core.tools.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class ProcessorInEclipse {
	private IdeProcessingEnvImpl processingEnv;
	private TypeElement element;
	
	public ProcessorInEclipse(ProcessingEnvironment processingEnv, TypeElement element) {
		if(processingEnv instanceof IdeProcessingEnvImpl){
			this.processingEnv=(IdeProcessingEnvImpl)processingEnv;
			
			this.element=element;
		}
	}

	public boolean generateFiles()throws Exception {
		if(processingEnv!=null){
			IJavaProject project=processingEnv.getJavaProject();
			
			String[] classPath=JavaRuntime.computeDefaultRuntimeClassPath(project);
			 
		 	URLClassLoader loader=new URLClassLoader(Helper.toURLs(classPath),processingEnv.getClass().getClassLoader());
			try{
				String className=DBGeneratorProcessing.class.getName();
				Class<?> clazz=Class.forName(className,true,loader);
				Constructor<?> cs=clazz.getConstructor(ProcessingEnvironment.class,TypeElement.class);
				
				Method m=clazz.getMethod("generateFiles");
				Object dbgp=cs.newInstance(processingEnv,element);
				m.invoke(dbgp);
				
				return true;
			}finally{
				loader.close();
			}
		}else{
			return false;
		}
	}

}
