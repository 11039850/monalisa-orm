package com.tsc9526.monalisa.core.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.query.dao.ModelParser;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

@SuppressWarnings({"unchecked","rawtypes"})
public class ModelHelper {
	private static Map<Class<?>,ModelParser<Object>> parsers=new ConcurrentHashMap<Class<?>,ModelParser<Object>>();
	
	static{
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
	
	public static boolean parseModel(Model<?> model,Object data) {
		if(data!=null){
			Class<?> parserClass=null;
			for(Class<?> clazz:parsers.keySet()){
				if(data.getClass().isAssignableFrom(clazz)){
					parserClass=clazz;
					break;
				}
			}
			
			if(parserClass!=null){
				ModelParser<Object> parser=parsers.get(parserClass);
				parser.parseModel(model, data);
			}
		}
		return false;
	}
	
	public static class ServletRequestModelParser implements ModelParser<javax.servlet.ServletRequest>{
		
		public boolean parseModel(Model<?> m, javax.servlet.ServletRequest data) {			
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();				
				String value=data.getParameter(name);
				if(value!=null){
					fgs.setObject(m, value);
				}
			}
			return true;
		}
		
	}
	
	public static class MapModelParser implements ModelParser<Map<String,Object>>{		
		public boolean parseModel(Model<?> m, Map<String,Object> data) {
			for(FGS fgs:m.fields()){
				String name =fgs.getFieldName();
				
				if(data.containsKey(name)){
					Object value=data.get(name);
					fgs.setObject(m, value);
				}
			}
			return true;
		}		
	}
	
	public static class StringModelParser implements ModelParser<String>{		
		public boolean parseModel(Model<?> m, String data) {
			if(data.startsWith("{")){				
				//JSON String
				Map<String, Object> map=(Map<String, Object>)JsonHelper.parse(data);
				
				for(FGS fgs:m.fields()){
					String name =fgs.getFieldName();
					
					if(map.containsKey(name)){
						Object value=map.get(name);
						fgs.setObject(m, value);
					}
				}
				
				return true;
			}else if(data.startsWith("<")){
				//XML String
				
				return true;
			}else{
				return false;
			}			
		}		
	}
}
