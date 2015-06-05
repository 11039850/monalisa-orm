package com.tsc9526.monalisa.core.generator;

import java.io.Writer;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.query.partition.Partition;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.plugin.eclipse.tools.IdeProcessing;

public class DBGenerator {
	public static String PROJECT_TMP_PATH="/target/monalisa";
	
	private ProcessingEnvironment processingEnv;
	 
	private TypeElement typeElement;
	
	private DBConfig dbcfg;
	
	private DBMetadata dbmetadata; 
	
	private String javaPackage;
	
	public DBGenerator(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		super();
		this.processingEnv = processingEnv;		 
		this.typeElement = typeElement;
		
		String projectPath=null;
		
		if(Helper.inEclipseIDE()){
			String dbKey=this.typeElement.toString();
			
			IdeProcessing p=new IdeProcessing(processingEnv);	
			System.setProperty("DB@"+dbKey,p.getProjectPath());	
			
			projectPath=p.getProjectPath();			 
		}
		
		this.dbcfg=DataSourceManager.getInstance().getDBConfig(typeElement,true);
		
		
		String name=typeElement.getQualifiedName().toString();
		String pkg=name.toLowerCase();
		int p=name.lastIndexOf(".");
		if(p>0){
			pkg=name.substring(0,p)+name.substring(p).toLowerCase();
		}		
		this.javaPackage=pkg;
		
		this.dbmetadata=new DBMetadata(projectPath,javaPackage,dbcfg);		
	}	 
	
	
	
	public void generatorJavaFiles(){					
		List<MetaTable> tables=dbmetadata.getTables();
		for(MetaTable table:tables){
			generatorJavaFile(table);		 
		}	
		
		generatorResources(tables);
	}
	
	protected void generatorResources(List<MetaTable> tables){		
		try{			 					
			FileObject java = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "resources."+dbcfg.key().toLowerCase(), "create_table.sql", typeElement);
			
			Writer w = java.openWriter();
			for(MetaTable table:tables){
				if(table.getCreateTable()!=null){
					w.write("/***CREATE TABLE: "+table.getNameWithoutPartition()+" :: "+table.getName()+"***/\r\n");
					w.write(table.getCreateTable().getCreateSQL()); 
					w.write("\r\n\r\n\r\n");
				}
			}
			w.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void generatorJavaFile(MetaTable table){		
		try{			 
			MetaTable clone=table.clone();
			clone.setJavaName(null).setName(clone.getNameWithoutPartition());
			
			String className=clone.getJavaName();						
			JavaFileObject java = processingEnv.getFiler().createSourceFile(javaPackage+"."+className, typeElement);
			
			Writer os = java.openWriter();			
			
			String modelClass=getModelClassValue();
			if(modelClass==null || modelClass.trim().length()==0){
				modelClass=Model.class.getName();
			}
			  
			DBTableGeneratorByTpl g2=new DBTableGeneratorByTpl(clone, modelClass, typeElement.getQualifiedName().toString());
			g2.generate(os);
			
			verifyPartition(table);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private void verifyPartition(MetaTable table){
		MetaPartition partition=table.getPartition();
		if(partition!=null){
			String clazz=partition.getClazz();
			try{ 
				Class<?> x=Class.forName(clazz);
				Partition p=(Partition)x.newInstance();
				p.setup(partition.getTablePrefix(), partition.getArgs());
				String error=p.verify(table);
				if(error!=null){
					processingEnv.getMessager().printMessage(Kind.ERROR,"Partition error: "+error, typeElement);
				}			
			}catch(ClassNotFoundException e){
				
			}catch(Exception e) {
				 e.printStackTrace(System.out);
				 processingEnv.getMessager().printMessage(Kind.ERROR,e.getClass().getName()+":\r\n"+e.getMessage(), typeElement);
			}
		}
	}
	 
	
	
	protected String getModelClassValue(){
		String modelClass=null;
		try{
			modelClass=dbcfg.modelClass();//.getName();
		}catch(MirroredTypeException mte ){
			TypeMirror typeMirror=mte.getTypeMirror();
			Types TypeUtils = processingEnv.getTypeUtils();
			TypeElement te= (TypeElement)TypeUtils.asElement(typeMirror);
			modelClass=te.getQualifiedName().toString();
		}
		return modelClass;
	}
	 
  
}
