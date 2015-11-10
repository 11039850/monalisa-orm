package com.tsc9526.monalisa.core.tools;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections.map.AbstractHashedMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelParser;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

import freemarker.template.utility.StringUtil;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ModelHelper {
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
	public static boolean parse(Model<?> model,Object data,String... mappings) {
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
				return parser.parse(model, data,mappings);
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
        					if(!( (c>='a' && c<='z') || (c>='A' && c<='Z') ) ){
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
		
		public boolean parse(Model<?> m, javax.servlet.ServletRequest data, String... mappings) {
			StringMap map=new StringMap(data.getParameterMap(),mappings);
			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
			 	
				if(map.containsKey(name)==false){
					Column column=fgs.getAnnotation(Column.class);
					name=column.name();
				} 
				
				if(map.containsKey(name)){
					String[] value=(String[])map.get(name);					
					fgs.setObject(m, value[0]);
				}				 
			}
			return true;
		}
		
	}
	
	public static class MapModelParser implements ModelParser<Map<String,Object>>{		
		public boolean parse(Model<?> m, Map<String,Object> data, String... mappings) {
			StringMap map=new StringMap(data,mappings);
			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
				
				if(map.containsKey(name)==false){
					Column column=fgs.getAnnotation(Column.class);
					name=column.name();
				}
				
			  	if(map.containsKey(name)){
					Object value=map.get(name);
					fgs.setObject(m, value);
				}
			}
			return true;
		}		
	}
	
	public static class StringModelParser implements ModelParser<String>{			 
		public boolean parse(Model<?> m, String data, String... mappings) {
			if(data.startsWith("{")){//json
				JsonObject  json=(JsonObject)new JsonParser().parse(data);				
				return new JsonObjectModelParser().parse(m, json, mappings);				  			 
			}if(data.startsWith("<")){//xml				 		
				return new XmlModelParser().parse(m, data, mappings);				  			 
			}else{
				return false;
			}			
		}					 
	}
	
	public static class XmlModelParser implements ModelParser<String>{			 
		public boolean parse(Model<?> m, String xml, String... mappings) {
			try {
	            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(xml.getBytes("utf-8")),"utf-8");
	            String attr = null;
	            String chars = null;
	            Map<String, Object> data = new HashMap<String, Object>();
	            while (reader.hasNext()) {
	                int event = reader.next();
	                switch (event) {
	                    case XMLStreamConstants.START_ELEMENT:
	                        attr = reader.getLocalName();
	                        break;
	                    case XMLStreamConstants.CHARACTERS:
	                        chars = reader.getText().trim();
	                        break;
	                    case XMLStreamConstants.END_ELEMENT:
	                        if (attr != null && chars!=null) {
	                            data.put(attr, chars);
	                        }
	                        attr = chars = null;
	                        break;
	                }
	            }
	            
	            return new MapModelParser().parse(m, data, mappings);
	        }catch(Exception e) {
	            throw new RuntimeException(e);
	        }
		}		
	}
	
	public static class JsonObjectModelParser implements ModelParser<JsonObject>{			 
		public boolean parse(Model<?> m, JsonObject json, String... mappings) {
			Map<String, Object> data = new HashMap<String, Object>();
			Iterator<Map.Entry<String, JsonElement>> es=json.entrySet().iterator();
			while(es.hasNext()){
				Entry<String, JsonElement> e=es.next();
				
				data.put(e.getKey(),e.getValue());
			}			
			return new MapModelParser().parse(m, data, mappings);
		}		
	}
	
	public static String toString(Model<?> model){
		StringBuffer sb = new StringBuffer();
		for (FGS fgs : model.fields()) {
			Object v = fgs.getObject(model);
			if (v != null) {
				String s = "" + ClassHelper.convert(v, String.class);
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(fgs.getFieldName() + ": ").append(s);
			}
		}
		sb.append("}");
		sb.insert(0, model.table().name() + ":{");
		return sb.toString();
	}
	
	public static String toJson(Model<?> model) {
		Gson gson=JsonHelper.getGson();
		
		JsonObject json=new JsonObject(); 
		for (FGS fgs : model.fields()) {
			String name=fgs.getFieldName();
			 
			Object v = fgs.getObject(model);
			if (v != null) {
				JsonElement e=null;
				if(v instanceof JsonElement){
					e=(JsonElement)v;
				}else{
					e=gson.toJsonTree(v);					 
				}				
				json.add(name, e);
			}			 
		} 		
		return gson.toJson(json);		 
	}
 

	public static String toXml(Model<?> model,boolean withXmlHeader, boolean ignoreNullFields) {
		StringBuilder sb = new StringBuilder();

		boolean pretty = true;
		String CRLN = "\r\n";

		if (withXmlHeader) {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			if (pretty) {
				sb.append(CRLN);
			}
		}

		String indent = "";

		String topTag = model.getClass().getSimpleName();
		if (pretty) {
			sb.append(indent);
		}
		sb.append('<').append(topTag).append('>');
		if (pretty) {
			sb.append(CRLN);
		}
		for (FGS fgs : model.fields()) {
			String name = fgs.getFieldName();

			Object v = fgs.getObject(model);
			if (v != null) {
				String value = (String) ClassHelper.convert(v, String.class);

				if (pretty) {
					sb.append("  ").append(indent);
				}
				sb.append('<').append(name).append('>');
				sb.append(StringUtil.XMLEnc(value));

				sb.append("</").append(name).append('>');
				if (pretty) {
					sb.append(CRLN);
				}
			} else if (!ignoreNullFields) {
				sb.append('<').append(name).append("/>");
			}
		}

		if (pretty) {
			sb.append(indent);
		}
		sb.append("</").append(topTag).append('>');

		return sb.toString();
	}
}
