package com.tsc9526.monalisa.core.tools;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class EnumHelper {
	private static Map<String, Object> hIntegerEnum=new ConcurrentHashMap<String, Object>();
	private static Map<String, Object> hStringEnum=new ConcurrentHashMap<String, Object>();
	
	public static Object getEnum(Class<?> type,Object value){
		if(value!=null){
			if(type.isEnum()){
				if(value.getClass().isEnum() && value.getClass()==type){
					return value;
				}else{
					if(value instanceof Number){
						int x=((Number)value).intValue();
						return getEnum(type,x);					
					}else{
						String x=value.toString();
						return getEnum(type,x);						
					}
				}
			} 			 
		} 
		return null;
	}
	
	private static Object getEnum(Class<?> type,int x){
		String key=type.getName()+"-"+x;
		
		if(!hIntegerEnum.containsKey(key)){
			Object[] ecs=type.getEnumConstants();
			for(Object e:ecs){
				int v=getIntValue((Enum<?>)e);
				hIntegerEnum.put(type.getName()+"-"+v, e);
			}						
		}
		
		return hIntegerEnum.get(key);
		
	}
	
	private static Object getEnum(Class<?> type,String x){
		String key=type.getName()+"-"+x;
		
		if(!hStringEnum.containsKey(key)){
			Object[] ecs=type.getEnumConstants();
			for(Object e:ecs){
				String v=getStringValue((Enum<?>)e);
				hStringEnum.put(type.getName()+"-"+v, e);
			}						
		}
		
		return hStringEnum.get(key);
	}

	public static int getIntValue(Enum<?> e){ 
		try {			 
			Method m = e.getClass().getMethod("getValue");
			Object v =m.invoke(e);
			return Integer.parseInt(v.toString().trim());
		}catch(NoSuchMethodException ex){			
			return e.ordinal();			
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public static String getStringValue(Enum<?> e){ 
		return e.name(); 
	}
	
}
