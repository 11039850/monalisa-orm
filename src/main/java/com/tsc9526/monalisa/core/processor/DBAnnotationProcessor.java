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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.generator.DBGeneratorProcessing;
import com.tsc9526.monalisa.core.logger.MessagerLogger;
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
		 
		if(Helper.inEclipseProcessing()){
			//Eclipse环境,设置日志输出
			MessagerLogger.setMessagerLogger(processingEnv.getMessager());
		} 		
	}
	
	
	 
	public boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv) {
		Log logger=LogFactory.getLog(DBAnnotationProcessor.class);
		
		if (!roundEnv.processingOver()) {	
			Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(DB.class);
			for (Element element : els) {				
				if (element.getKind() == ElementKind.INTERFACE) { 				 										
					try{						 
						DBGeneratorProcessing dbai=new DBGeneratorProcessing(processingEnv,(TypeElement)element);
						
						dbai.generateFiles();
						
					}catch(Throwable e){
						logger.error(""+e,e);
						
						if(Helper.inEclipseProcessing()){
							processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+Helper.toString(e), element);
						}
					}
				}else{
					logger.warn("@DB should used for interface!");
					
					if(Helper.inEclipseProcessing()){
						processingEnv.getMessager().printMessage(Kind.WARNING,"@DB should used for interface!", element);
					}					
				}
			}
		} 
		return true;
	}
  
}