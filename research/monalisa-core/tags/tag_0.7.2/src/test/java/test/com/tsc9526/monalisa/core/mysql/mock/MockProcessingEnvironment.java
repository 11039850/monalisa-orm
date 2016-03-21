package test.com.tsc9526.monalisa.core.mysql.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

import mockit.Mock;
import mockit.MockUp;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;

public class MockProcessingEnvironment {
	private DB db;
	private String dbClassName;
	
	public MockProcessingEnvironment(Class<?> clazzWithDBAnnotation){
		db=DBConfig.fromClass(clazzWithDBAnnotation).getDb();
		dbClassName=clazzWithDBAnnotation.getName();
	}
	
	public  ProcessingEnvironment  createProcessingEnvironment(){
		ProcessingEnvironment mock = new MockUp<ProcessingEnvironment>() {
			@Mock Filer getFiler(){
				return new Filer() {
					public FileObject getResource(Location location, CharSequence pkg,
							CharSequence relativeName) throws IOException {
						 
						return null;
					}
					
					 
					public JavaFileObject createSourceFile(CharSequence name,Element... originatingElements) throws IOException {
						String path="src/test/java/"+name.toString().replace(".","/")+".java";
						final File file=new File(path);
						
						File dir=file.getParentFile();
						if(!dir.exists()){
							dir.mkdirs();
						}
						
						Kind kind=Kind.SOURCE;
						return new SimpleJavaFileObject(file.toURI(),kind){
							 public Writer openWriter() throws IOException {
							       return new OutputStreamWriter(new FileOutputStream(file), "utf-8");
							 }
						};
					}
					
					 
					public FileObject createResource(Location location, CharSequence pkg,
							CharSequence relativeName, Element... originatingElements)
							throws IOException {
						String path="src/test/resources/"+pkg.toString().replace(".","/")+"/"+relativeName.toString();
						final File file=new File(path);
						
						File dir=file.getParentFile();
						if(!dir.exists()){
							dir.mkdirs();
						}
						
						Kind kind=Kind.SOURCE;
						return new SimpleJavaFileObject(file.toURI(),kind){
							 public OutputStream openOutputStream() throws IOException {
							       return new FileOutputStream(file);
							 }
						};
					}
					
					 
					public JavaFileObject createClassFile(CharSequence name,
							Element... originatingElements) throws IOException {
						 
						return null;
					}
				};
			}
	       
	     }.getMockInstance();

	     
		return mock;
	}
	
	public TypeElement createTypeElement(){
		TypeElement mock = new MockUp<TypeElement>() {
	        @SuppressWarnings("unchecked")
			@Mock <A extends Annotation> A getAnnotation(Class<A> annotationType){
	        	return (A)db;
	        }
	        
	        @Mock
	        Name getQualifiedName(){
	        	return new Name() {
	        		public String toString(){
	        			return dbClassName;
	        		}
	        		
					 
					public CharSequence subSequence(int start, int end) {
						return null;
					}
				 
					public int length() { 
						return 0;
					}
					
					 
					public char charAt(int index) { 
						return 0;
					}
					
					 
					public boolean contentEquals(CharSequence cs) {
						return false;
					}
				};
	        }
	       
	     }.getMockInstance();
	     
		return mock;
	}
}
