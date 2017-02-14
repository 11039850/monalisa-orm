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
package com.tsc9526.monalisa.tools.clazz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.JsonObject;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.tools.annotation.Alias;
import com.tsc9526.monalisa.tools.converters.TypeConverter;
import com.tsc9526.monalisa.tools.parser.Parser;
import com.tsc9526.monalisa.tools.parser.impl.BeanParser;
import com.tsc9526.monalisa.tools.parser.impl.JsonObjectParser;
import com.tsc9526.monalisa.tools.parser.impl.MapParser;
import com.tsc9526.monalisa.tools.parser.impl.ServletRequestParser;
import com.tsc9526.monalisa.tools.parser.impl.StringParser;
import com.tsc9526.monalisa.tools.string.MelpString;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpClass {
	public static TypeConverter converter=new TypeConverter();	
	 
	private static ConcurrentHashMap<String, ClassAssist> hBeanClasses = new ConcurrentHashMap<String, ClassAssist>();
	 
	public static long getVersion(String className){
		try{
			return getVersion(Class.forName(className));
		}catch(ClassNotFoundException e){
			return -1;
		}
	} 
	
	public static long getVersion(Class<?> clazz){
		try{
			Field f=null;
			try{
				f=clazz.getDeclaredField(DbProp.CFG_FIELD_VERSION+"$");
			}catch(NoSuchFieldException e){
				f=clazz.getDeclaredField(DbProp.CFG_FIELD_VERSION);
			}
			
			if(f!=null){
				f.setAccessible(true);
				return f.getLong(null);
			} 
		}catch(NoSuchFieldException e){
			 
		}catch( IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
		return -1;
	}
	 
	
	public static ClassAssist getClassAssist(Class<?> clazz) {
		if(clazz==null){
			return null;
		}
				
		String name = clazz.getName();
		ClassAssist mc = hBeanClasses.get(name);
		if (mc == null) {
			mc = loadMetaClass(clazz);
		}
		return mc;
	}
	 
	public static ClassAssist getClassAssist(Object bean) {
		return getClassAssist(bean.getClass());
	}
	 
	
	public static Collection<FGS> getFields(Class<?> clazz){
		return getClassAssist(clazz).getFields();	
	}
	
	public static Collection<FGS> getFields(Object bean){
		if(bean instanceof Specifiable){
			return ((Specifiable)bean).fields();
		}else{
			return getClassAssist(bean).getFields();
		}
	}
	
	public static Class<?> forName(String className)throws ClassNotFoundException{
		try{
			return Class.forName(className);
		}catch(ClassNotFoundException cnf){
			return Class.forName(className,true,Thread.currentThread().getContextClassLoader());
		}
	}
  	
	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T a=null;
				 
		Class<?> c=findClassWithAnnotation(clazz,annotationClass);
		if(c!=null){
			a=c.getAnnotation(annotationClass);
		}
		 			
		return a;
	}
	
	public static Method[] findDeclaredMethods(Class<?> theClass, Class<?> baseClass) {
		Class<?> clazz = theClass;
		Method[] allMethods = null;

		while (!clazz.equals(baseClass)) {
			Method[] thisMethods = clazz.getDeclaredMethods();
			if (allMethods != null && allMethods.length > 0) {
				Method[] subClassMethods = allMethods;
				allMethods = new Method[thisMethods.length + subClassMethods.length];
				System.arraycopy(thisMethods, 0, allMethods, 0, thisMethods.length);
				System.arraycopy(subClassMethods, 0, allMethods, thisMethods.length, subClassMethods.length);
			} else {
				allMethods = thisMethods;
			}

			clazz = clazz.getSuperclass();
		}

		return ((allMethods != null) ? allMethods : new Method[0]);
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
			}else {
				return clazz;
			}
		}							
		return null;
	}

	/**
	 * Copy object 
	 * 
	 * @param from source object
	 * @param to dest object
	 * @param <T> dest type
	 * 
	 * @return The to Object
	 */
	public static <T> T copy(Object from,T to){
		ClassAssist fm=getClassAssist(from);
		ClassAssist ft=getClassAssist(to);
		
		for(FGS fgs:fm.getFields()){
			FGS x=ft.getField(fgs.getFieldName());
			if(x!=null){
				Object value=fgs.getObject(from);
				
				x.setObject(to, value);
			}
		}
		
		return to;
	}
 	 
	public static <T> T convert(Object source, Class<T> type) {
		return converter.convert(source, type);
	}
 
	private synchronized static ClassAssist loadMetaClass(Class<?> clazz) {
		String name = clazz.getName();
		if (hBeanClasses.containsKey(name)) {
			return hBeanClasses.get(name);
		} else {
			ClassAssist mc = new ClassAssist(clazz);
			hBeanClasses.put(name, mc);
			return mc;
		}
	}
	
	public static List<Field> fetchFields(Class<?> clazz){	
		List<Field> fs=new ArrayList<Field>();
		fetchFields(fs,clazz);
		return fs;
	}
	
	public static void fetchFields(List<Field> fields,Class<?> clazz){			 
		Field[] fs=clazz.getDeclaredFields();
		for(Field f:fs){
			fields.add(f);
		}
		
		Class<?> su=clazz.getSuperclass();
		if(su!=null){
			fetchFields(fields,su);
		}						
	}	
	
	
	private static Map<Class<?>,Parser<?>> parsers=new LinkedHashMap<Class<?>,Parser<?>>();
	   
	static{
		MelpLib.tryLoadGson();
		 
		registerParser(Map.class,new MapParser());
		registerParser(JsonObject.class,new JsonObjectParser());
		registerParser(String.class,new StringParser());
		
		try{
			Class<?> servletRequestClass=Class.forName("javax.servlet.ServletRequest");
			registerParser(servletRequestClass,new ServletRequestParser());
		}catch(ClassNotFoundException e){}
	}
	  
	public static void registerParser(Class<?> clazz, Parser<?> parser){
		parsers.put(clazz, parser);
	}
	
	public static void removeParser(Class<?> clazz){
		parsers.remove(clazz);
	}
	
	/**
	 * @param target the target object
	 * @param data  (ServletRequest|Map|JsonString|...) 
	 * @param mappings
	 *            [Options] Translate dataObject field to model field <br>
	 *            For example: <br>
	 *            "user_id=id", ... // Parse dataObject.user_id to Model.id<br>
	 *            Another example:<br>
	 *            "~XXX." //Only parse dataObject's fields with prefix: "XXX."
	 * @return true if parse from data to target ok, otherwise false
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public static boolean parse(Object target,Object data,String... mappings) {
		if(data!=null){
			Parser parser=parsers.get(data.getClass());
			if(parser==null){
				for(Class<?> clazz:parsers.keySet()){
					if(clazz.isAssignableFrom(data.getClass())){
						parser=parsers.get(clazz);
						break;
					}
				}
			}
			
			if(parser==null){
				parser=new BeanParser();
			}
			 
			String[] xms=MelpString.splits(mappings);
			
			return parser.parse(target, data,xms); 
		}
				
		return false;
	}
	
	/**
	 * Parse multi-objects from ServletRequest
	 * 
	 * @param targetTemplate one target object template
	 * @param data ServletRequest data
	 * @param mappings see #parse(Object,Object,String...)
	 * @return
	 * 
	 * @see #parse(Object,Object,String...)
	 */
	public static <T> List<T> parseArrays(T targetTemplate, javax.servlet.ServletRequest data, String... mappings) {
		return ServletRequestParser.parseArrays(targetTemplate, data, mappings);
	}
 
	public static class ClassAssist {
		private Class<?> clazz;
		private Map<String, FGS> hFields = new LinkedHashMap<String, FGS>();
		private ReadWriteLock lock=new ReentrantReadWriteLock(); 
		
		public ClassAssist(Class<?> clazz) {
			this.clazz = clazz;
			loadClassInfo();
		}

		private void loadClassInfo() {
			List<Field> fields=new ArrayList<Field>();
			
			fetchFields(fields,clazz);	
			
			for(Field f : fields) {
				int modifiers = f.getModifiers();				 
				if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
					FGS fgs = new FGS(clazz,f);
					
					if(!hFields.containsKey(fgs.getFieldName())){
						hFields.put(fgs.getFieldName(), fgs);													
					}
				}
			}
		}
	 	
		/**
		 * 
		 * @param fs Fields will be remove
		 * 
		 * @return Removed fields in the class
		 */
		public List<FGS> removeFields(List<FGS> fs){
			try{
				lock.writeLock().lock();
				
				List<FGS> xs=new ArrayList<FGS>();
				for(FGS fgs:fs){
					FGS x=hFields.remove(fgs.getFieldName());
					if(x!=null){
						xs.add(x);
					}
				}
				
				return xs;
			}finally{
				lock.writeLock().unlock();
			}
		}
		
		public List<FGS> clearFields(){
			try{
				lock.writeLock().lock();
				
				List<FGS> xs=new ArrayList<FGS>();
				
				xs.addAll(hFields.values());
				
				hFields.clear();
				 
				return xs;
			}finally{
				lock.writeLock().unlock();
			}
		}
		
	 
		public void addFields(List<FGS> fs){
			try{
				lock.writeLock().lock();
				
				for(FGS fgs:fs){
					hFields.put(fgs.getFieldName(), fgs);
				}
			}finally{
				lock.writeLock().unlock();
			}
			
		}
		
		public void replaceFields(List<FGS> fs){
			try{
				lock.writeLock().lock();
				
				hFields.clear();
				
				for(FGS fgs:fs){
					hFields.put(fgs.getFieldName(), fgs);
				}
			}finally{
				lock.writeLock().unlock();
			}
			
		}

		public FGS getField(String name){
			try{
				lock.readLock().lock();
			  	
				FGS fgs=hFields.get(name);
				
				if(fgs==null){
					for(FGS x:hFields.values()){
						if(x.getAlias().contains(name)){
							fgs=x;
							break;
						}
					}
				}
				
				return fgs;
			}finally{
				lock.readLock().unlock();
			}
		} 
		
		public Collection<FGS> getFields(){
			try{
				lock.readLock().lock();
				
				return hFields.values();
			}finally{
				lock.readLock().unlock();
			}			
		}
		
		/**
		 * Remove fields which field annotation with the annotationClass
		 * 
		 * @param annotationClass annotationClass
		 * @return Removed fields in the class
		 */
		public List<FGS> removeFieldsWithAnnotation(Class<? extends Annotation> annotationClass){
			List<FGS> fs=getFieldsWithAnnotation(annotationClass);
			return removeFields(fs);
		}
		
		/**
		 * Get fields which field annotation with the annotationClass
		 * 
		 * @param annotationClass annotationClass
		 * 
		 * @return fields annotation with the annotationClass
		 */
		public List<FGS> getFieldsWithAnnotation(Class<? extends Annotation> annotationClass){
			List<FGS> fgs=new ArrayList<FGS>();
			for(FGS f:getFields()){
				if(f.getAnnotation(annotationClass)!=null){
					fgs.add(f);
				}
			}
			
			return fgs;
		}				 
	}
  
	@SuppressWarnings("rawtypes")
	public static class FGS {
		protected String fieldName;
		
		protected Set<String> alias=new LinkedHashSet<String>();
		
		protected Class<?> type;
		
		protected Field field;
		protected Method getMethod;
		protected Method setMethod;
		
		protected boolean nullNone=false;
	 	
		public FGS(Class<?> type,String fieldName,String ... names){
			this.fieldName=fieldName;
			this.type=type;
			
			setAlias(names);
		}
		
		public FGS(Class<?> clazz,Field field){
			this.field=field;
			this.type=field.getType();
			
			fieldName = field.getName();
		 	 
			Alias alias=field.getAnnotation(Alias.class);
			if(alias!=null){
				setAlias(alias.value());
			}
			
			String m = fieldName.substring(0, 1).toUpperCase();
			if (fieldName.length() > 1) {
				m += fieldName.substring(1);
			}
		 	
			String get = "get" + m;
			String set = "set" + m;
			if (field.getType() == Boolean.class || field.getType() == boolean.class) {
				get = "is" + m;
			}

			getMethod=getMethod(clazz,get);
			setMethod=getMethod(clazz,set, field.getType());		
		}
		
		protected void setAlias(String[] names){
			for(String s:names){
				alias.add(s);
			}
		}
		
		public Field getField(){
			return this.field;
		}
		
		private Method getMethod(Class<?> clazz,String name, Class<?>... parameterTypes) {
			try {
				return clazz.getMethod(name, parameterTypes);
			} catch (NoSuchMethodException e) {				 
				return null;
			}
		}
		
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return field.getAnnotation(annotationClass);
		}
	 
		public Class<?> getType() {
		    return field.getType();
		}
		
		public Object getObject(Object bean){
			try {	
				if(field==null && bean instanceof Map){
					return getMapObject((Map)bean);
				}else{			
					if(getMethod!=null){
						return getMethod.invoke(bean);				
					}else{
						field.setAccessible(true);
						return field.get(bean);				
					}		
				}
			} catch (Exception e) {
				throw new RuntimeException("Error get method from field: "+getFieldName(),e);
			}
		}
		 
		public void setObject(Object target,Object value){
			try {	
				if(field==null && target instanceof Map){
					setMapObject((Map)target,value);				
				}else{
					Class<?> type=getType();
					Object theValue=convert(value, type);
					
					if(setMethod!=null){
						setMethod.invoke(target, theValue);
					}else{
						field.setAccessible(true);
						field.set(target, theValue);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Field: "+this.fieldName+", type: "+type.getName()+", value type: "+value.getClass().getName(),e);
			}
		}
		
		public void mapto(Map<?,?> keymap,Object target){
			String name=findKey(keymap);
			 	
		  	if(name!=null){
				Object value=keymap.get(name);
				setObject(target, value);
		  	} 
		}
		
	
		public String findKey(Map<?,?> keymap){
			String name =getFieldName();
		 	
		  	if(keymap.containsKey(name)){
		  		return name;
		  	}else{
		  		for(String s:alias){
					if(keymap.containsKey(s)){
						return s;
					}
				} 
		  	}
		  	
		  	return null;
		}
	 	
		protected Object getMapObject(Map m){
			return m.get(fieldName);
		}
		
		@SuppressWarnings("unchecked")
		protected void setMapObject(Map m,Object v){
			m.put(fieldName,v);
		}

		public boolean isNullNone() {
			return nullNone;
		}

		public void setNullNone(boolean nullNone) {
			this.nullNone = nullNone;
		}

		public String getFieldName() {
			return fieldName;
		}
		
		public Set<String> getAlias(){
			return alias;
		}
		
		public Method getSetMethod(){
			return setMethod;
		}
		
		public Method getGetMethod(){
			return getMethod;
		}
	}	
	
	
	public static Set<String> OBJECT_METHODS = new HashSet<String>() {
		private static final long serialVersionUID = -4949935939426517392L;
		{
			add("equals");
			add("getClass");
			add("hashCode");
			add("notify");
			add("notifyAll");
			add("toString");
			add("wait");
		}
	};
}
