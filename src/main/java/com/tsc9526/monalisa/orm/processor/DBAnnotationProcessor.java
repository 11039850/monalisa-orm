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
package com.tsc9526.monalisa.orm.processor;

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

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.generator.DBGenerator;
import com.tsc9526.monalisa.orm.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.orm.generator.DBGeneratorProcessingInEclipse;
import com.tsc9526.monalisa.tools.PkgNames;
import com.tsc9526.monalisa.tools.logger.Logger;
import com.tsc9526.monalisa.tools.misc.MelpEclipse;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SupportedAnnotationTypes(PkgNames.ORM_ANNOTATION_DB)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DBAnnotationProcessor extends AbstractProcessor {	 
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);			 		 
		 
		if(MelpEclipse.inEclipseIDE()){
			//Eclipse环境,设置日志输出
			Logger.setMessager(processingEnv.getMessager());
		}
	}
	 
	public boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv) {
		DbProp.ProcessingEnvironment=true;
		 
		if (!roundEnv.processingOver()) {	
			Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(DB.class);
			for (Element es : els) {	
				if(es instanceof TypeElement ==false){
					continue;
				}
				
				TypeElement element=(TypeElement)es;
				
				if (es.getKind() == ElementKind.INTERFACE) { 
					try{	
						doGenerateFiles(element);
					}catch(Throwable e){
						DBGenerator.plogger.error(""+e,e);
						
						if(MelpEclipse.inEclipseIDE()){
							processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+MelpString.toString(e), element);
						}
					}
				}else{
					DBGenerator.plogger.warn("@DB should used for interface: "+element.getQualifiedName().toString());
					
					if(MelpEclipse.inEclipseIDE()){
						processingEnv.getMessager().printMessage(Kind.WARNING,"@DB should used for interface: "+element.getQualifiedName().toString(), element);
					}					
				}
			}
		} 
	
		return true;
	}
	
	private void doGenerateFiles(TypeElement element)throws Exception{
		long tm=System.currentTimeMillis();
		DBGenerator.plogger.info("****** Starting generate model classes ******");
		
		if(MelpEclipse.inEclipseIDE()){
			DBGeneratorProcessingInEclipse g=new DBGeneratorProcessingInEclipse(processingEnv,element);
			g.generateFiles();
		}else{
			DBGeneratorProcessing g=new DBGeneratorProcessing(processingEnv,element);
			g.generateFiles();
		}
		
		long delta=System.currentTimeMillis()-tm;
		
		DBGenerator.plogger.info("Finished, use time: "+delta+" ms.");
	}
  
}