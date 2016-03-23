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

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SupportedAnnotationTypes("com.tsc9526.monalisa.core.annotation.DB")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DBAnnotationProcessor extends AbstractProcessor {	 
	 
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);			 		 
		 
		if(Helper.inEclipseIDE()){
			//Eclipse环境,设置日志输出
			Logger.setMessager(processingEnv.getMessager());
		} 		
	}
	 
	public boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv) {
		Logger logger=Logger.getLogger(DBAnnotationProcessor.class);
		
		if (!roundEnv.processingOver()) {	
			Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(DB.class);
			for (Element element : els) {				
				if (element.getKind() == ElementKind.INTERFACE) { 				 										
					try{						 
						doGenerateFiles((TypeElement)element);
					}catch(Throwable e){
						logger.error(""+e,e);
						
						if(Helper.inEclipseIDE()){
							processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+Helper.toString(e), element);
						}
					}
				}else{
					logger.warn("@DB should used for interface!");
					
					if(Helper.inEclipseIDE()){
						processingEnv.getMessager().printMessage(Kind.WARNING,"@DB should used for interface!", element);
					}					
				}
			}
		} 
		return true;
	}
	
	private void doGenerateFiles(TypeElement element)throws Exception{
		if(Helper.inEclipseIDE()){
			ProcessorInEclipse pie=new ProcessorInEclipse(processingEnv,element);
			if(!pie.generateFiles()){
				DBGeneratorProcessing dbai=new DBGeneratorProcessing(processingEnv,element);
				dbai.generateFiles();
			}
		}else{
			DBGeneratorProcessing dbai=new DBGeneratorProcessing(processingEnv,element);
			dbai.generateFiles();
		}
	}
  
}