package com.tsc9526.monalisa.core.parser.jsp;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JspContext {
	private Map<String, Object> context = new HashMap<String, Object>();

	public Object getAttribute(String name) {
		return context.get(name);
	}

	public String getParameter(String name) {
		return (String) getAttribute(name);
	}

	public JspContext setAttribute(String name, Object value) {
		context.put(name, value);
		return this;
	}

}
