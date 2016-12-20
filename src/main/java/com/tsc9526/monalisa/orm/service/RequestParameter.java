package com.tsc9526.monalisa.orm.service;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.ExceptionHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class RequestParameter {
	/**
	 * pkg.a_class$method
	 */
	public static String MS       = "$";
	
	/**
	 * Ignore request parameters with prefix: _
	 */
	public static String PREFIX   = "_";
	
	/**
	 * GET,POST,HEAD,PUT,DELETE ...
	 */
	public static String METHOD   = "method";
	
	/**
	 * xml, json, jsonp
	 */
	public static String FORMAT   = "format";
	
	public static String CALLBACK = "callback";  
	
	public static String AND      = "and";
	public static String OR       = "or";
	
	/**
	 * Include/Exclude columns
	 */
	public static String COLUMN   = "column";
	
	/**
	 * Page size
	 */
	public static String ROWS     = "rows";
	
	/**
	 * Page number: 1,2,3 ...
	 */
	public static String PAGE     = "page";
	
	/**
	 * Order by 
	 */
	public static String ORDER    = "order";
 	
  
	private static Set<String> fields=new HashSet<String>();
	 
	
	public static boolean contains(String name){
		if(fields.size()==0){
			synchronized (fields) {
				if(fields.size()==0){
					try{
						List<Field> fs=ClassHelper.fetchFields(RequestParameter.class);
						for(Field f:fs){
							if(f.getType()==String.class){
								String v=(String)f.get(null);
								fields.add(v.toLowerCase());
							}
						}
					}catch(Exception e){
						ExceptionHelper.throwRuntimeException(e);
					}
				}
			}
		}
		
		return fields.contains(name.toLowerCase());
	}
}
