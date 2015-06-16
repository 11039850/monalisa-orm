package com.tsc9526.monalisa.core.tools;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.map.AbstractHashedMap;

import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.query.dao.ModelParser;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

@SuppressWarnings({"unchecked","rawtypes"})
public class ModelHelper {
	private static Map<Class<?>,ModelParser<Object>> parsers=new LinkedHashMap<Class<?>,ModelParser<Object>>();
	
	static{
		DateConverter dc = new DateConverter(); 
		dc.setUseLocaleFormat(true);
		String[] datePattern = {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS"};    
		dc.setPatterns(datePattern);    
		ConvertUtils.register(dc, java.util.Date.class);
		
		registerModelParser(Map.class,new MapModelParser());
		registerModelParser(String.class,new StringModelParser());
		
		try{
			Class<?> servletRequestClass=Class.forName("javax.servlet.ServletRequest");
			registerModelParser(servletRequestClass,new ServletRequestModelParser());
		}catch(ClassNotFoundException e){}
	}
	  
	public static void registerModelParser(Class<?> clazz, ModelParser parser){
		parsers.put(clazz, parser);
	}
	
	public static void unregisterModelParser(Class<?> clazz){
		parsers.remove(clazz);
	}
	
	/**
	 * @param model
	 * @param data  (ServletRequest|Map|JsonString) 
	 * @return
	 */
	public static boolean parseModel(Model<?> model,Object data) {
		if(data!=null){
			ModelParser<Object> parser=parsers.get(data.getClass());
			if(parser==null){
				for(Class<?> clazz:parsers.keySet()){
					if(clazz.isAssignableFrom(data.getClass())){
						parser=parsers.get(clazz);
						break;
					}
				}
			}
			
			if(parser!=null){				 
				return parser.parseModel(model, data);
			}else{
				return parseFromFields(model,data);
			}
		}
				
		return false;
	}
	
	
	private static boolean parseFromFields(Model<?> m,Object data){
		MetaClass source=ClassHelper.getMetaClass(data);
		
		for(FGS fgs:m.fields()){
			String name =fgs.getFieldName();
			
			FGS x=source.getField(name);
			if(x!=null){
				fgs.setObject(m, x.getObject(data));
			}						 			 
		}
		return true;
	}
	
	private static class StringMap extends AbstractHashedMap implements Serializable, Cloneable {
	    private static final long serialVersionUID = -1074655917369299456L;
 
	    private boolean nameIgnoreCase  = false;
	    private boolean nameToJavaStyle = false;
	    
	    public StringMap(boolean nameIgnoreCase,boolean nameToJavaStyle,Map data){
	        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD);
	        
	        this.nameIgnoreCase  = nameIgnoreCase;
	        this.nameToJavaStyle = nameToJavaStyle;
	        
	        this.putAll(data);
	    }
	    
	    protected Object convertKey(Object key) {	    	
	        if (key != null) {
	        	String x=key.toString();
	        	
	        	if(nameToJavaStyle){
	        		x=JavaBeansHelper.getCamelCaseString(x,false);
	        	}
	        	
	        	if(nameIgnoreCase){
	        		x=x.toLowerCase();
	        	}	             
	            
	            return x;
	        } else {
	            return AbstractHashedMap.NULL;
	        }
	    }  
	}
		 
	public static class ServletRequestModelParser implements ModelParser<javax.servlet.ServletRequest>{
		/**
		 * 是否忽略参数名的大小写, 默认: false
		 */
		public static boolean NAME_IGNORE_CASE  =false;
		
		/**
		 * 是否将参数名转换为JavaBean的风格, 如: account_no -> accountNo, 默认: 
		 */
		public static boolean NAME_TO_JAVA_STYLE=false;
		
		public boolean parseModel(Model<?> m, javax.servlet.ServletRequest data) {			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
				
				StringMap map=new StringMap(NAME_IGNORE_CASE,NAME_TO_JAVA_STYLE,data.getParameterMap());
				 
				if(map.containsKey(name)){
					String[] value=(String[])map.get(name);					
					fgs.setObject(m, value[0]);
				}				 
			}
			return true;
		}
		
	}
	
	public static class MapModelParser implements ModelParser<Map<String,Object>>{		
		/**
		 * 是否忽略参数名的大小写, 默认: false
		 */
		public static boolean NAME_IGNORE_CASE  =false;
		
		/**
		 * 是否将参数名转换为JavaBean的风格, 如: account_no -> accountNo, 默认: 
		 */
		public static boolean NAME_TO_JAVA_STYLE=false;
		
		public boolean parseModel(Model<?> m, Map<String,Object> data) {
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
				
				StringMap map=new StringMap(NAME_IGNORE_CASE,NAME_TO_JAVA_STYLE,data);
				
				if(map.containsKey(name)){
					Object value=map.get(name);
					fgs.setObject(m, value);
				}
			}
			return true;
		}		
	}
	
	public static class StringModelParser implements ModelParser<String>{		
		/**
		 * 是否忽略参数名的大小写, 默认: false
		 */
		public static boolean NAME_IGNORE_CASE  =false;
		
		/**
		 * 是否将参数名转换为JavaBean的风格, 如: account_no -> accountNo, 默认: 
		 */
		public static boolean NAME_TO_JAVA_STYLE=false;
		
		public boolean parseModel(Model<?> m, String data) {
			if(data.startsWith("{")){				
				//JSON String
				Map<String, Object> x=(Map<String, Object>)JsonHelper.parse(data);				
				StringMap map=new StringMap(NAME_IGNORE_CASE,NAME_TO_JAVA_STYLE,x);
				
				for(FGS fgs:m.fields()){
					String name =fgs.getFieldName();
					
					if(map.containsKey(name)){
						Object value=map.get(name);
						fgs.setObject(m, value);
					}
				}
				
				return true;			 
			}else{
				return false;
			}			
		}		
	}
}
