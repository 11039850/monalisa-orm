package com.tsc9526.monalisa.core.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateTimeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ClassHelper {
	static{
		DateValue dc = new DateValue(null);
		dc.setUseLocaleFormat(true);
		String[] datePattern = {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss.SSS"};    
		dc.setPatterns(datePattern);    
		ConvertUtils.register(dc, java.util.Date.class);
	}
	
	private static ConcurrentHashMap<String, MetaClass> hBeanClasses = new ConcurrentHashMap<String, MetaClass>();
	  
	public static MetaClass getMetaClass(Class<?> clazz) {
		if(clazz==null){
			return null;
		}
				
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

	/**
	 * Copy object 
	 * 
	 * @param from
	 * @param to
	 * 
	 * @return The to Object
	 */
	public static <T> T copy(Object from,T to){
		MetaClass fm=getMetaClass(from);
		MetaClass ft=getMetaClass(to);
		
		for(FGS fgs:fm.getFields()){
			FGS x=ft.getField(fgs.getFieldName());
			if(x!=null){
				Object value=fgs.getObject(from);
				
				x.setObject(to, value);
			}
		}
		
		return to;
	}
	 
	
	public static void setObject(Object bean, FGS fgs, Object v) {
		Method set = fgs.getSetMethod();
		Class<?> type=fgs.getField().getType();
		
		try {			 			 
			Object value=null;
			if(v!=null){
				if(type.isEnum()){
					value=EnumHelper.getEnum(fgs, v);
				}else if(type==JsonObject.class){
					if(v.getClass()==JsonObject.class){
						value=v;
					}else{
						value=new JsonParser().parse(v.toString());
					}	
				}else{
					if(v.getClass().isArray() && type == String.class){
						value=Arrays.toString((Object[])v);						
					}else if(Map.class.isAssignableFrom(v.getClass()) && type == String.class){
						value=mapToString((Map<?,?>)v);						
					}else if(v.getClass().isArray()==false && type.isArray()){
						JsonElement je=new JsonParser().parse(v.toString());
						if(je==null || je.isJsonNull()){
							value=null;
						}else{
							JsonArray array=je.getAsJsonArray();						
							if(type==int[].class){
								int[] iv=new int[array.size()];
								for(int i=0;i<array.size();i++){
									JsonElement e=array.get(i);
									iv[i]=e.getAsInt();
								}
								value=iv;
							}else if(type==long[].class){
								long[] iv=new long[array.size()];
								for(int i=0;i<array.size();i++){
									JsonElement e=array.get(i);
									iv[i]=e.getAsLong();
								}
								value=iv;
							}else if(type==double[].class){
								double[] iv=new double[array.size()];
								for(int i=0;i<array.size();i++){
									JsonElement e=array.get(i);
									iv[i]=e.getAsDouble();
								}
								value=iv;
							}else{//String[]
								String[] iv=new String[array.size()];
								for(int i=0;i<array.size();i++){
									JsonElement e=array.get(i);
									if(e.isJsonPrimitive()){
										iv[i]=e.getAsString();
									}else{
										//ingore
										return;
									}								
								}
								value=iv;
							}	
						}
					}else if(type.isArray()==false 
							&& type.isPrimitive()==false 
							&& type.getName().startsWith("java.")==false
							&& (v.getClass() == String.class || v.getClass()==JsonObject.class)){
						//Json String to Java Object
						if(v.getClass()==String.class){
							value=JsonHelper.getGson().fromJson(v.toString(), type);
						}else{
							value=JsonHelper.getGson().fromJson((JsonObject)v, type);
						}
					}else{					
						value=ConvertUtils.convert(v, fgs.getField().getType());
					}
				}
			}
			
			set.invoke(bean, value);
		} catch (Exception e) {
			throw new RuntimeException("Field type: "+type.getName()+", value type: "+v.getClass().getName(),e);
		}
	}
		
	private static String mapToString(Map<?,?> m){		
		Gson gson=JsonHelper.getGson();
		return gson.toJson(m);		
	}
	
	public static Object getObject(Object bean, FGS fgs) {
		try {			 
			Method get = fgs.getGetMethod();
			if(get!=null){
				Object r=get.invoke(bean);
				return r;
			}else{
				return null;
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error get method from field: "+fgs.getFieldName(),e);
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
	
	public static class DateValue extends DateTimeConverter {

		public DateValue() {
	        super();
	        
	        
	    }
  
	    public DateValue(Object defaultValue) {
	        super(defaultValue);
	    }
 
	    protected Class<?> getDefaultType() {
	        return Date.class;
	    }
	    
	    public <T> T convert(Class<T> type, Object value) {
	    	try{
	    		long v=Long.parseLong(value.toString());
	    		return super.convert(type, v);
	    	}catch(NumberFormatException e){
	    		return super.convert(type, value);
	    	}
	    }

	}
}
