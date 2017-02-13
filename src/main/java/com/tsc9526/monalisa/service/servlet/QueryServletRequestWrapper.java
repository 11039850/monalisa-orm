package com.tsc9526.monalisa.service.servlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class QueryServletRequestWrapper extends HttpServletRequestWrapper{
	 
	private Map<String,Object> attrs;
	
	public QueryServletRequestWrapper(Map<String,Object> attrs,HttpServletRequest request) {
		super(request);	
	}
	
	public Object getAttribute(String name) {
		if(attrs!=null && attrs.containsKey(name)){
			return attrs.get(name);
		}else{
			return super.getAttribute(name);
		}
    }
	
	public void setAttribute(String name,Object value) {
		if(attrs!=null){
			attrs.put(name, value);
		}else{
			super.setAttribute(name, value);
		}
	}
	
	public Object getQueryAttribute(String name) {
		return attrs.get(name);
    }
	
	public Map<?,?> getQueryAttributes() {
		return attrs;
    }
}
