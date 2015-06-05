package com.tsc9526.monalisa.plugin.eclipse.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

import com.tsc9526.monalisa.core.tools.Helper;

public class SourceUnit {
	private CompilationUnit unit;
	private TypeDeclaration unitType;	 
	private String unitName;
	private int endpos=-1;	
	private IJavaProject project;
	private Map<String,String> imports=new HashMap<String,String>();
	
	private int importPosition=-1;
	
	public SourceUnit(CompilationUnit unit){
		this.unit=unit;
		
		unitType=(TypeDeclaration)unit.types().get(0);		
		unitName=unitType.getName().toString();
		endpos=unitType.getStartPosition()+unitType.getLength();
		 
		project=unit.getJavaElement().getJavaProject();
		
		for(Object i:unit.imports()){
			ImportDeclaration id=(ImportDeclaration)i;			
			String name=id.getName().getFullyQualifiedName();
			int p=name.lastIndexOf(".");
			if(p>0){
				imports.put(name.substring(p+1),name);
			}else{
				imports.put(name, name);
			}
			
			importPosition=id.getStartPosition()+id.getLength();
		}	
				
	}
  	
	
	public TextEdit createImports(Collection<String> imps){		
		StringBuffer sb=new StringBuffer();
		for(String i:imps){
			String key=i;
			int p=i.lastIndexOf(".");
			if(p>0){
				key=i.substring(p+1);
			}
			
			if(imports.containsKey(key)==false){				 
				sb.append("\r\nimport ").append(i).append(";");
			}
		}
		return new ReplaceEdit(importPosition,0,sb.toString());
	}
	
	public String getProjectPath(){
		return project.getProject().getLocation().makeAbsolute().toString();						 
	}
	
	public String[] getRuntimeClasspath(){
		try {			  
			String[] cp=JavaRuntime.computeDefaultRuntimeClassPath(project);
			return cp;
		} catch (CoreException e) {
			Helper.throwRuntimeException(e);
			return null;
		}
	}
	
	public String[] getPluginClasspath(){
		List<String> classpath = new ArrayList<String>();
		classpath.addAll(Arrays.asList(System.getProperty("sun.boot.class.path").split(File.pathSeparator)));
		
	 
		return classpath.toArray(new String[0]); 					
	}
	
	public Annotation getAnnotation(BodyDeclaration body,Class<?> annotationClass){
		List<?> tfs=body.modifiers();
		for(Object o:tfs){					 
			if(o instanceof Annotation){
				Annotation a=(Annotation)o;	
				
				String name=getFullName(a.getTypeName().getFullyQualifiedName());				 
				if(name.equals(annotationClass.getName())){
					return a;
				}
			}
		}
 		//type.getSuperclassType();
		return null;
	}
	
	public org.jboss.tools.ws.jaxrs.core.jdt.Annotation findClassAnnotation(String annotationClassName){
		org.jboss.tools.ws.jaxrs.core.jdt.Annotation annotation=null;
		
		IType type=unit.getTypeRoot().findPrimaryType();				
		annotation=getAnnotation(type,annotationClassName,unit);		
		if(annotation==null){					 
			String superClass=""+unitType.getSuperclassType();
			if(superClass!=null){
				type=findType(superClass);
				if(type!=null ){					 
					annotation=getAnnotation(type,annotationClassName,null);
				}
			}
			
			if(annotation==null){			
				List<?> interfaces=unitType.superInterfaceTypes();
				for(Object i:interfaces){
					if(i instanceof Type){
						type=findType(""+i);	
						if(type!=null){
							annotation=getAnnotation(type,annotationClassName,null);
							if(annotation!=null){
								break;
							}
						}
					}
				}
			}
		}
		return annotation;
	}	 
	
	private org.jboss.tools.ws.jaxrs.core.jdt.Annotation getAnnotation(IType type,String annotationClassName,CompilationUnit cu){
		try{
			if(cu==null){
				ICompilationUnit icu=org.jboss.tools.ws.jaxrs.core.jdt.JdtUtils.getCompilationUnit(type);
				cu=org.jboss.tools.ws.jaxrs.core.jdt.JdtUtils.parse(icu,null);
			}
			return org.jboss.tools.ws.jaxrs.core.jdt.JdtUtils.resolveAnnotation(type.getAnnotation(annotationClassName),cu);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String getFingerprint(String name){
		try{		
			String fullClassName=getFullName(name);
			
			IType type=findType(fullClassName);
			if(type!=null){
				if(type.getCompilationUnit()!=null){
					IField field=type.getField("FINGERPRINT");
					if(field!=null){
						String fp=(String)field.getConstant();
						if(fp.startsWith("\"")){
							fp=fp.substring(1,fp.length()-1);
						}						
						return fp;
					}
				}
			}			
		}catch(JavaModelException jme){
			Helper.throwRuntimeException(jme);
		}	
		return null;
	}
	
	public IType findType(String name){
		try{	
			String fullClassName=getFullName(name);
			
			IType type=getProject().findType(fullClassName);
			return type;		
		}catch(JavaModelException jme){
			Helper.throwRuntimeException(jme);
			return null;
		}	
	}
	 
	public String getFullName(String name){
		String im=imports.get(name);
		if(im==null){
			if(name.indexOf(".")>0){
				im=name;
			}else{	
				char c=name.charAt(0);
				if(c>='a' && c<='z'){
					im=name;
				}else{
					try{
						Class.forName("java.lang."+name);
						im=name;
					}catch(ClassNotFoundException e){
						im=this.getPackageName()+"."+name;
					}
				}
			}
		}
		return im;
	}
	
	public Map<String,String> getImports(){
		return imports;
	}
	
	public String getJavaCode(){
		return unit.toString();
	}
	
	public String getPackageName(){
		return unit.getPackage().getName().toString();
	}

	public CompilationUnit getUnit() {
		return unit;
	}

	public String getSubPackageName(){
		return getPackageName()+"."+unitName.toLowerCase();
	}
	
	public IPackageFragmentRoot getPackageFragmentRoot(){
		IJavaElement e=unit.getJavaElement();
		while(e!=null){
			if(e instanceof IPackageFragmentRoot){
				return (IPackageFragmentRoot)e;			
			}else{
				e=e.getParent();
			}
		}
		throw new RuntimeException("Pakcage fragment root not found: "+unit.getJavaElement().getElementName());
	}
	
	public TypeDeclaration getUnitType() {
		return unitType;
	}

	public String getUnitName() {
		return unitName;
	}

	public int getEndpos() {
		return endpos;
	}

	public IJavaProject getProject() {
		return project;
	}
}
