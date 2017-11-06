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
package test.com.tsc9526.monalisa.orm.dialect.mysql.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DBConfig;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MockProcessingEnvironment {
	private DB db;
	private String dbClassName;
	private Class<?> clazzWithDBAnnotation;
	
	private String outputJavaDir    ; 
	private String outputResourceDir;

	public MockProcessingEnvironment(Class<?> clazzWithDBAnnotation,String outputJavaDir,String outputResourceDir){
		this.clazzWithDBAnnotation=clazzWithDBAnnotation;
		db=DBConfig.fromClass(clazzWithDBAnnotation).getDb();
		dbClassName=clazzWithDBAnnotation.getName();
		 
		this.outputJavaDir=outputJavaDir;
		this.outputResourceDir=outputResourceDir;
	}
	
	public  ProcessingEnvironment  createProcessingEnvironment()throws IOException{
		ProcessingEnvironment env = mock(ProcessingEnvironment.class);
		
		Filer filer=mock(Filer.class);
		 
		doAnswer(new Answer<JavaFileObject>() {  
		    public JavaFileObject answer(InvocationOnMock invocation) {  
		        Object[] args = invocation.getArguments(); 
		        CharSequence name=(CharSequence)args[0];
		        
		        String path=outputJavaDir+"/"+name.toString().replace(".","/")+".java";
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
		}).when(filer).createSourceFile(any(CharSequence.class),any(Element[].class));
		 
		
		doAnswer(new Answer<FileObject>() {  
		    public FileObject answer(InvocationOnMock invocation) {  
		    	Object[] args = invocation.getArguments(); 
		    	CharSequence pkg=(CharSequence)args[1];
		    	CharSequence relativeName=(CharSequence)args[2];
		    	String path=outputResourceDir+"/"+pkg.toString().replace(".","/")+"/"+relativeName.toString();
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
		}).when(filer).createResource(any(Location.class),any(CharSequence.class),any(CharSequence.class),any(Element[].class));
		
		
		when(env.getFiler()).thenReturn(filer);
	 	 
	     
		return env;
	}
	
	public TypeElement createTypeElement(){
		TypeElement mock = mock(TypeElement.class);
	 
		when(mock.getAnnotation(DB.class)).thenReturn(db);	
		when(mock.toString()).thenReturn(DBConfig.fromClass(clazzWithDBAnnotation).getKey());
		
		doAnswer(new Answer<Name>() {
			 public Name answer(InvocationOnMock invocation) {  
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
		}).when(mock).getQualifiedName();
		
		return mock;
	}
}
