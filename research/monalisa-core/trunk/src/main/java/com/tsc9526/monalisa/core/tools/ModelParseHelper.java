package com.tsc9526.monalisa.core.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.AbstractHashedMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.query.dao.ModelParser;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

@SuppressWarnings({"unchecked","rawtypes"})
public class ModelParseHelper {
	private static Map<Class<?>,ModelParser<Object>> parsers=new LinkedHashMap<Class<?>,ModelParser<Object>>();
	
	/**
	 * 区分参数名的大小写, 默认: false
	 */
	public final static String OPTIONS_NAME_CASE_SENSITIVE  = "[NAME_CASE_SENSITIVE]";
	 
	static{
		registerModelParser(Map.class,new MapModelParser());
		registerModelParser(JsonObject.class,new JsonObjectModelParser());
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
	 * @param data  (ServletRequest|Map|JsonString|...) 
	 * @param mappings
	 * @return
	 */
	public static boolean parseModel(Model<?> model,Object data,String... mappings) {
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
				return parser.parseModel(model, data,mappings);
			}else{
				return parseFromFields(model,data,mappings);
			}
		}
				
		return false;
	}
	
	
	private static boolean parseFromFields(Model<?> m,Object data,String... mappings){
		MetaClass source=ClassHelper.getMetaClass(data);
		
		Map<String, String> hNameMapping=new HashMap<String, String>();
		if(mappings!=null){
        	 for(String x:mappings){
        		if(x.indexOf("=")>0){
	        		String[] nv=x.split("=");	        		
	        		hNameMapping.put(nv[1].trim(),nv[0].trim());	        		
        		}
        	}
        }   
		 
		for(FGS fgs:m.fields()){
			String name =fgs.getFieldName();
			
			if(hNameMapping.containsKey(name)){
				name=hNameMapping.get(name);
			}
			FGS x=source.getField(name);
			if(x!=null){
				fgs.setObject(m, x.getObject(data));
			}						 			 
		}
		return true;
	}
	
	public static class StringMap extends AbstractHashedMap implements Serializable, Cloneable {
	    private static final long serialVersionUID = -1074655917369299456L;
 
	    private boolean caseSensitive = false;
	    private String  prefix=null; 
	    private Map<String, String> hNameMapping=new CaseInsensitiveMap();
	    
	    public StringMap(Map data, String... mappings){
	    	super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD);
	        	  
	    	initCheck(data,mappings);
	    	inputData(data,mappings);
	    }
	    
	    private void initCheck(Map data, String... mappings){
	    	if(mappings!=null){
	        	for(String m:mappings){
	        		if(m.indexOf("=")<0){
	        			if(m.indexOf(OPTIONS_NAME_CASE_SENSITIVE)>=0){
	        				caseSensitive=true;
	        			}else if(m.startsWith("~")){
	        				prefix=m.substring(1);							 
						}	        			 
	        		}
	        	}
	        
	        	for(String m:mappings){
	        		if(m.indexOf("=")>0){
		        		String[] nv=m.split("=");	        		
		        		
		        		hNameMapping.put(nv[0].trim(),nv[1].trim());		        		 
	        		}
	        	}
	        }
	    }
	        
	    private void inputData(Map data, String... mappings){	        
	        for(Object key:data.keySet()){
	        	String k=key.toString();
	        	Object v=data.get(key);
	        	
	        	if(prefix!=null){
        			if(k.startsWith(prefix)){
        				k=k.substring(prefix.length());
        				if(k.length()>0){
        					char c=k.charAt(0);
        					if( !(c>='a' && c<='z') || (c>='A' && c<='Z') ){
        						k=k.substring(1);
        					}
        				}
        				
        				if(k.length()<1){
        					continue;
        				}
        			}else{
        				continue;
        			}
        		}
	        	
	        	if(hNameMapping.containsKey(k)){
	        		k=hNameMapping.get(k);
	        	}else{
	        		if(k.indexOf("_")>=0 || k.indexOf("-")>=0){
	        			k=JavaBeansHelper.getCamelCaseString(k,false);
	        		}
	        	}
	       	
	        	this.put(k,v);
	        }
	    }
	    
	    
	    protected Object convertKey(Object key) {
	        if (key != null) {
	        	if(caseSensitive){
		    		return key.toString();
		    	}else{
		    		return key.toString().toLowerCase();
	        	}	
	        } else {
	            return AbstractHashedMap.NULL;
	        }
	    }   	     
	}
		 
	public static class ServletRequestModelParser implements ModelParser<javax.servlet.ServletRequest>{
		
		public boolean parseModel(Model<?> m, javax.servlet.ServletRequest data, String... mappings) {
			StringMap map=new StringMap(data.getParameterMap(),mappings);
			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
			 	 
				if(map.containsKey(name)){
					String[] value=(String[])map.get(name);					
					fgs.setObject(m, value[0]);
				}				 
			}
			return true;
		}
		
	}
	
	public static class MapModelParser implements ModelParser<Map<String,Object>>{		
		public boolean parseModel(Model<?> m, Map<String,Object> data, String... mappings) {
			StringMap map=new StringMap(data,mappings);
			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
				
			  	if(map.containsKey(name)){
					Object value=map.get(name);
					fgs.setObject(m, value);
				}
			}
			return true;
		}		
	}
	
	public static class StringModelParser implements ModelParser<String>{			 
		public boolean parseModel(Model<?> m, String data, String... mappings) {
			if(data.startsWith("{")){
				JsonObject  json=(JsonObject)new JsonParser().parse(data);				
				return new JsonObjectModelParser().parseModel(m, json, mappings);				  			 
			}else{
				return false;
			}			
		}		
	}
	
	public static class JsonObjectModelParser implements ModelParser<JsonObject>{			 
		public boolean parseModel(Model<?> m, JsonObject json, String... mappings) {
			for(FGS fgs:m.fields()){
				JsonElement e=json.get(fgs.getFieldName());
				if(e==null){
					Column column=fgs.getField().getAnnotation(Column.class);
					e=json.get(column.name());
				}
				
				if(e!=null){
					if(e.isJsonNull()){
						fgs.setObject(m,null);
					}else{
						if(e.isJsonPrimitive()){
							fgs.setObject(m,e.getAsString());
						}else{						
							fgs.setObject(m,e.toString());
						}
					}
				}
			}

			return true;
		}		
	}
}
