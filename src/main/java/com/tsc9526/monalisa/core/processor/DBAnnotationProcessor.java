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
import com.tsc9526.monalisa.core.logger.ConsoleLoggerFactory;
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
		 
		try{
			Logger.selectLoggerLibrary(Logger.INDEX_CONSOLE);			 
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		 
		if(Helper.inEclipseProcessing()){
			//Eclipse环境,设置日志输出
			ConsoleLoggerFactory.setMessagerLogger(processingEnv.getMessager());
		} 		
	}
	
	
	 
	public boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv) {
		Logger logger=Logger.getLogger(DBAnnotationProcessor.class);
		
		if (!roundEnv.processingOver()) {	
			Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(DB.class);
			for (Element element : els) {				
				if (element.getKind() == ElementKind.INTERFACE) { 				 										
					try{						 
						DBGeneratorProcessing dbai=new DBGeneratorProcessing(processingEnv,(TypeElement)element);
						
						dbai.generateFiles();
						
					}catch(Throwable e){
						if(Helper.inEclipseProcessing()){
							processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+e.getMessage(), element);
						}else{
							logger.error(""+e,e);
						}
						
					}
				}else{
					if(Helper.inEclipseProcessing()){
						processingEnv.getMessager().printMessage(Kind.WARNING,"@DB should used for interface!", element);
					}else{
						logger.warn("@DB should used for interface!");
					}					
				}
			}
		} 
		return true;
	}
  
}