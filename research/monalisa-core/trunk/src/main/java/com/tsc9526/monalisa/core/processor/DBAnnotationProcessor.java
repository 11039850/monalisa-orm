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
import com.tsc9526.monalisa.core.generator.DBGenerator;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SupportedAnnotationTypes("com.tsc9526.monalisa.core.annotation.DB")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DBAnnotationProcessor extends AbstractProcessor {
	 
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);	
		    
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {	
			Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(DB.class);
			for (Element element : els) {				
				if (element.getKind() == ElementKind.INTERFACE) { 				 										
					try{						 
						DBGenerator dbai=new DBGenerator(processingEnv,(TypeElement)element);
						
						dbai.generatorJavaFiles();
						
					}catch(Throwable e){
						e.printStackTrace(System.out);
						
						processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+e.getMessage(), element);
					}
				}else{
					System.out.print("[ERROR] @DB only used for interface!");
					
					processingEnv.getMessager().printMessage(Kind.ERROR,"@DB only used for interface!", element);
				}
			} 							 
		} 
		return true;
	}
  
}