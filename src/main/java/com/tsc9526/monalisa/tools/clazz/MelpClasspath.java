package com.tsc9526.monalisa.tools.clazz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tsc9526.monalisa.tools.io.MelpFile;

public class MelpClasspath {
	public static List<String> splitClassPaths(String classpath){
		List<String> rs=new ArrayList<String>();
		if(classpath==null || classpath.trim().length()==0){
			return rs;
		}
		
		classpath=classpath.trim();
		
		boolean win32=classpath.indexOf(";")>0;
		if(!win32){
			String[] vs=classpath.split(":");
			if(vs.length==2 && vs[0].length()==1){
				//win32
				rs.add(classpath);
			}else{
				for(String p:vs){
					rs.add(p);
				}
			}
		}else{
			for(String p:classpath.split(";")){
				rs.add(p);
			}
		}
		
		return rs;
	}
	
	public static File getClassOrJarFile(String clazz) {
		try {
			return getClassOrJarFile(Class.forName(clazz));
		}catch(ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
	 
	public static File getClassOrJarFile(Class<?> clazz) {
		URL url = getClassResourceURL(clazz);
		String path=null;
		 
		path=getFilePathfromResourceUrl(url);	 
		 
		if(path!=null){
			return new File(path);
		}else{
			throw new RuntimeException("Error locating classpath entry for: " + clazz.getName() + " url: " + url);
		}
	}
	
	public static String getFilePathfromResourceUrl(URL url)  {
		String urlString=url.toString();
		 
		int endIndex = urlString.toLowerCase().indexOf("!");
		if (endIndex > 0) {
			//class in jar
			int beginIndex = urlString.lastIndexOf("file:/");
			if (beginIndex >= 0) {
				String path=urlString.substring(beginIndex+5, endIndex);
				
				if(path.indexOf(":")>0){
					path=path.substring(1);
				}
				
				return path;
			}
			
			beginIndex = urlString.lastIndexOf("://");
			if (beginIndex > 0) {
				return urlString.substring(beginIndex+3, endIndex);
			}
		} else {
			//class in folder
			try{
				return new File(url.toURI()).getAbsolutePath().replace("\\","/"); 
			}catch(URISyntaxException e){
				throw new RuntimeException("Invalid url: "+urlString,e);
			}
		}
		
		return null;
	}
	
	public static File getClassPathFile(Class<?> clazz) {
		File path=getClassOrJarFile(clazz);
		if(path.getName().endsWith(".class")){
			path = path.getParentFile();
			
			String pn = clazz.getPackage().getName();
			for (int i = pn.indexOf('.'); i >= 0; i = pn.indexOf('.', i + 1)) {
				path = path.getParentFile();
			}
			path = path.getParentFile();
		}
		return path;
	}

	public static URL getClassResourceURL(Class<?> clazz) {
		int idx = clazz.getName().lastIndexOf('.');
		String fileName = (idx >= 0 ? clazz.getName().substring(idx + 1) : clazz.getName()) + ".class";
		return clazz.getResource(fileName);
	}

	
	public static void appendToSystemPath(Class<?> clazz) {
		try {
			appendToSystemPath(getClassPathFile(clazz).toURI().toURL());
		}catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	
	
	public static void appendToSystemPath(URL path) {
		if (path == null) {
			throw new IllegalArgumentException("Null path");
		}
		try {
			ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(systemClassLoader, path);
		} catch (Exception ex) {
			throw new RuntimeException("Add URL failed: " + path, ex);
		}
	}
 

	public static <T> Class<T> defineClass(ClassLoader loader, InputStream input)throws IOException {
		byte[] bytes = MelpFile.readBytes(input);
		return defineClass(loader, bytes);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> defineClass(ClassLoader loader, byte[] bytes) {
		try {
			Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
			defineClassMethod.setAccessible(true);
			return (Class<T>) defineClassMethod.invoke(loader, null, bytes, 0, bytes.length);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void defineClasses(ClassLoader loader, Map<String, byte[]> hClasses) {
		Map<String, _DefineClass> hdc=_DefineClass.from(loader,hClasses);
		
		_defineClasses(loader, hdc);
	}
	
	private static void _defineClasses(ClassLoader loader, Map<String, _DefineClass> hdc) {
		for(_DefineClass dc:hdc.values()){
			_defineClass(loader,hdc,dc);
		}
	}
	
	private static void _defineClass(ClassLoader loader, Map<String, _DefineClass> hdc,_DefineClass dc){
		try {
			if(!dc.defined){
				defineClass(loader,dc.bytes);
				dc.defined=true; 
			}
		}catch(RuntimeException e){
			String missing=getClassNotFoundError(e);
			_DefineClass x=hdc.get(missing);
			if(x!=null){
				_defineClass(loader,hdc,x);
				
				defineClass(loader,dc.bytes);
				dc.defined=true;
			}else{
				throw new RuntimeException("Define class failed: "+dc.className,e);
			}
		}
	}
	
	private static String getClassNotFoundError(Exception e){
		Throwable t=e.getCause();
		for(int i=0;t!=null && i<5;i++){
			if(t  instanceof NoClassDefFoundError){
				NoClassDefFoundError ndfe=(NoClassDefFoundError)t;
				String message=ndfe.getMessage();
				int p=message.lastIndexOf(":");
				String missing=message.substring(p+1).trim();
				missing=missing.replace("/",".");
				return missing;
			}
			
			t=t.getCause();
		} 
		
		return null;
	}

	static class _DefineClass{
		String className;
		boolean defined=false;
		byte[]  bytes;
		
		_DefineClass(String className,byte[] bytes){
			this.className=className;
			this.bytes=bytes;
		}
		
		static Map<String, _DefineClass> from(ClassLoader loader,Map<String, byte[]> hClasses){
			 Map<String, _DefineClass> hdc=new LinkedHashMap<String, MelpClasspath._DefineClass>();
			 for(String clazz:hClasses.keySet()){
				 _DefineClass dc= new _DefineClass(clazz, hClasses.get(clazz));
				
				 hdc.put(clazz,dc);
				 
				 try{
					 loader.loadClass(clazz);
					 dc.defined=true;
				 }catch(ClassNotFoundException e){}
			 }
			 return hdc;
		}
	}
}
