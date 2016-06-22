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
package com.tsc9526.monalisa.core.tools;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.ModelParser;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

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
	 * @param model model
	 * @param data  (ServletRequest|Map|JsonString|...) 
	 * @param mappings mappings
	 * @return true if parse ok otherwise false
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
			
			 
			String[] xms=splitMappings(mappings);
			
			if(parser!=null){				 
				return parser.parse(model, data,xms);
			}else{
				return parseFromFields(model,data,xms);
			}
		}
				
		return false;
	}
	
	
	private static String[] splitMappings(String... mappings){
		List<String> ms=new ArrayList<String>();
				
		if(mappings!=null && mappings.length>0){
			for(String m:mappings){			
				String[] xs=m.trim().split(",|;|\\|");
				if(xs.length>0){
					for(String x:xs){
						ms.add(x.trim());
					}
				}				 
			}
		} 		
		return ms.toArray(new String[0]);	
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
	
	public static class StringMap extends HashMap implements Serializable, Cloneable {
	    private static final long serialVersionUID = -1074655917369299456L;
 
	    private boolean caseSensitive = false;
	    private String  prefix=null; 
	    private Map<String, String> hNameMapping=new CaseInsensitiveMap();
	    
	    public StringMap(Map data, String... mappings){
	    	super();
	        	  
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
	    
	    public Object put(Object key,Object value){
	    	return super.put(convertKey(key),value);
	    }
	    
	    public Object get(Object key){
	    	return super.get(convertKey(key));
	    }
	    
	    public boolean containsKey(Object key){
	    	return super.containsKey(convertKey(key));
	    }
	    
	    protected Object convertKey(Object key) {
	        if (key != null) {
	        	if(caseSensitive){
		    		return key.toString();
		    	}else{
		    		return key.toString().toLowerCase();
	        	}	
	        } else {
	            return null;
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
	            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
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
			
			if(mappings.length>0 && mappings[0].startsWith("@")){
				String name=mappings[0].substring(1);
				json=json.get(name).getAsJsonObject();
			}
			
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
				 
				sb.append(value.replace("&","&amp;").replaceAll("<", "&lt;").replaceAll(">","&gt;"));

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
