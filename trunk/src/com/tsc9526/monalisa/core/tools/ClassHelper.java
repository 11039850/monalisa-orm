package com.tsc9526.monalisa.core.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 

public class ClassHelper {
	private static ConcurrentHashMap<String, MetaClass> hBeanClasses = new ConcurrentHashMap<String, MetaClass>();
	  
	public static MetaClass getMetaClass(Class<?> clazz) {
		String name = clazz.getName();
		MetaClass mc = hBeanClasses.get(name);
		if (mc == null) {
			mc = loadMetaClass(clazz);
		}
		return mc;
	}
	
	public static MetaClass getMetaClass(Object bean) {
		return getMetaClass(bean.getClass());
	}
	 
	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T a=null;
				 
		Class<?> c=findClassWithAnnotation(clazz,annotationClass);
		if(c!=null){
			a=c.getAnnotation(annotationClass);
		}
		 			
		return a;
	}
	
	public static <T extends Annotation> Class<?> findClassWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T a=null;
				 
		while(a==null && clazz!=null){
			a=clazz.getAnnotation(annotationClass); 
			if(a==null){
				Class<?>[] interfaces=clazz.getInterfaces();
				if(interfaces!=null){
					for(Class<?> c:interfaces){
						a=c.getAnnotation(annotationClass);
						if(a!=null){
							return c;
						}
					}
				}					 					
				clazz=clazz.getSuperclass();
			}
		}							
		return null;
	}

	public static void copy(Object from,Object to){
		MetaClass fm=getMetaClass(from);
		MetaClass ft=getMetaClass(to);
		
		for(FGS fgs:fm.getFields()){
			Object value=fgs.getObject(from);
			
			FGS x=ft.getField(fgs.getFieldName());
			if(x!=null){
				x.setObject(to, value);
			}
		}
	}
	 
	
	public static void setObject(Object bean, FGS fgs, Object obj) {
		try {			 			 
			Method set = fgs.getSetMethod();
			set.invoke(bean, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	 
	
	public static Object getObject(Object bean, FGS fgs) {
		try {			 
			Method get = fgs.getGetMethod();
			Object r=get.invoke(bean);
			return r;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	 
	
	private synchronized static MetaClass loadMetaClass(Class<?> clazz) {
		String name = clazz.getName();
		if (hBeanClasses.containsKey(name)) {
			return hBeanClasses.get(name);
		} else {
			MetaClass mc = new MetaClass(clazz);
			hBeanClasses.put(name, mc);
			return mc;
		}
	}

	public static class MetaClass {
		private Class<?> clazz;
		private Map<String, FGS> hFields = new LinkedHashMap<String, FGS>();
		 	
		public MetaClass(Class<?> clazz) {
			this.clazz = clazz;
			loadClassInfo();
		}

		private void loadClassInfo() {
			List<Field> fields=new ArrayList<Field>();
			fetchFields(fields,clazz);						
			for (Field f : fields) {
				int modifiers = f.getModifiers();				 
				if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
					FGS fgs = createFGS(f);
					
					String fn=f.getName();
					if (!hFields.containsKey(fn)){
						hFields.put(fn, fgs);													
					}											 
				}
			}
		}
		
		private void fetchFields(List<Field> fields,Class<?> clazz){			 
			Field[] fs=clazz.getDeclaredFields();
			for(Field f:fs){
				fields.add(f);
			}
			
			Class<?> su=clazz.getSuperclass();
			if(su!=null){
				fetchFields(fields,su);
			}						
		}
		

		private FGS createFGS(Field f) {
			FGS fgs = new FGS();
			fgs.setField(f);

			String fn = f.getName();
			String m = fn.substring(0, 1).toUpperCase();
			if (fn.length() > 1) {
				m += fn.substring(1);
			}

			String get = "get" + m;
			String set = "set" + m;
			if (f.getType() == Boolean.class || f.getType() == boolean.class) {
				get = "is" + m;
			}

			fgs.setGetMethod(getMethod(get));
			fgs.setSetMethod(getMethod(set, f.getType()));

			 
			return fgs;
		}

		private Method getMethod(String name, Class<?>... parameterTypes) {
			try {
				return clazz.getMethod(name, parameterTypes);
			} catch (NoSuchMethodException e) {				 
				return null;
			}
		}

		public FGS getField(String name) {
			return hFields.get(name);
		} 
		
		public Collection<FGS> getFields(){
			return hFields.values();
		}
		
		public List<FGS> getFieldsWithAnnotation(Class<? extends Annotation> annotationClass){
			List<FGS> fgs=new ArrayList<FGS>();
			for(FGS f:hFields.values()){
				if(f.field.getAnnotation(annotationClass)!=null){
					fgs.add(f);
				}
			}
			
			return fgs;
		}				 
	}
  
	public static class FGS {
		private Field field;
		private Method getMethod;
		private Method setMethod;
		
		private boolean nullNone=false;;
		
	 
		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		public Method getGetMethod() {
			return getMethod;
		}

		public void setGetMethod(Method getMethod) {
			this.getMethod = getMethod;
		}

		public Method getSetMethod() {
			return setMethod;
		}

		public void setSetMethod(Method setMethod) {
			this.setMethod = setMethod;
		}
		
		public Object getObject(Object bean){
			return ClassHelper.getObject(bean,this);
		}
		 
		public void setObject(Object bean,Object value){
			ClassHelper.setObject(bean,this,value);
		}

		public boolean isNullNone() {
			return nullNone;
		}

		public void setNullNone(boolean nullNone) {
			this.nullNone = nullNone;
		}

		public String getFieldName() {
			return field.getName();
		}
		 

	}
}
