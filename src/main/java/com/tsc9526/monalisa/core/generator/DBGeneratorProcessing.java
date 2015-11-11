package com.tsc9526.monalisa.core.generator;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.tools.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorProcessing extends DBGenerator{	 
	private ProcessingEnvironment processingEnv;	 
	private TypeElement typeElement;
	 
	public DBGeneratorProcessing(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		super();
		this.processingEnv = processingEnv;		 
		this.typeElement = typeElement;
		
		DB db=typeElement.getAnnotation(DB.class);
		if(db==null){
			throw new RuntimeException("TypeElement without @DB: "+typeElement.toString());
		}		
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=typeElement.toString();
		}
		String projectPath=DBProject.getProject(processingEnv, typeElement).getProjectPath();
		System.setProperty("DB@"+dbKey,projectPath);				 
		
		this.dbcfg=DataSourceManager.getInstance().getDBConfig(dbKey,db,true);
				
		String name=typeElement.getQualifiedName().toString();
		String pkg=name.toLowerCase();
		int p=name.lastIndexOf(".");
		if(p>0){
			pkg=name.substring(0,p)+name.substring(p).toLowerCase();
		}		
		this.javaPackage=pkg;		
		this.resourcePackage="resources."+pkg;
		this.dbi=typeElement.getQualifiedName().toString();
		this.dbmetadata=new DBMetadata(projectPath,javaPackage,dbcfg);		
	}	 
	  
	protected void generateResources(List<MetaTable> tables){		
		try{			 					
			FileObject res = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, resourcePackage, "create_table.sql", typeElement);
			OutputStream out=res.openOutputStream();
			
			Writer w = new OutputStreamWriter(out,"UTF-8");
			for(MetaTable table:tables){
				if(table.getCreateTable()!=null){
					w.write("/***CREATE TABLE: "+table.getNamePrefix()+" :: "+table.getName()+"***/\r\n");
					w.write(table.getCreateTable().getCreateSQL()); 
					w.write("\r\n\r\n\r\n");
				}
			}
			w.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void generateJavaFile(MetaTable table){		
		try{			 
			MetaTable clone=table.clone();
			clone.setJavaName(null).setName(clone.getNamePrefix());
			
			String className=clone.getJavaName();
			String modelClass=getModelClassValue(clone);
			
			JavaFileObject java = processingEnv.getFiler().createSourceFile(javaPackage+"."+className, typeElement);
			Writer os = java.openWriter();			
			JavaWriter writer=new JavaWriter(os);
			DBTableGenerator g2=new DBTableGenerator(clone, modelClass, dbi);
			g2.generate(writer);
			
			verifyPartition(table);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	protected String getModelClassValue(MetaTable table){
		String modelClass=null;
		try{
			modelClass=super.getModelClassValue(table);
			
		}catch(MirroredTypeException mte ){
			TypeMirror typeMirror=mte.getTypeMirror();
			Types TypeUtils = processingEnv.getTypeUtils();
			TypeElement te= (TypeElement)TypeUtils.asElement(typeMirror);
			modelClass=te.getQualifiedName().toString();
		}
		
		if(modelClass==null || modelClass.trim().length()==0){
			modelClass=Model.class.getName(); 
		}
		
		return modelClass;
	}
	
	protected void error(String message){
		processingEnv.getMessager().printMessage(Kind.ERROR,"Partition error: "+message, typeElement);
	}
  
}
