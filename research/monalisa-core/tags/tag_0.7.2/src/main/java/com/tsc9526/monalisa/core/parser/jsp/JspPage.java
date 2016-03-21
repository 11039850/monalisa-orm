package com.tsc9526.monalisa.core.parser.jsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JspPage extends JspElement{
	private Map<String, String> attributes=new HashMap<String, String>();
	 
	private Pattern pattern = Pattern.compile("[a-zA-Z]+\\s*=\\s*\\\"[^\\\"]*\\\"");
	
	public JspPage(Jsp jsp, int pos, int length) {
		super(jsp, pos, length);
	}

	public JspElement parseCode(String code){
		attributes.clear();
		
		this.code=code;
		 
		Matcher matcher=pattern.matcher(code);
		while (matcher.find()) { 
			String g=matcher.group();
			int x=g.indexOf("=");
			
			String name=g.substring(0,x).trim().toLowerCase();
			String value=g.substring(x+1).trim();
			value=value.substring(1,value.length()-1);
			attributes.put(name,value);
		}
		
		return this;
	}
	
	public String getAttribute(String name){
		return attributes.get(name.toLowerCase());
	}
	
	public String getPageEncoding(){
		return getAttribute("pageEncoding");
	}
	
	public String getLanguage(){
		return getAttribute("language");
	}
	
	public List<String> getImports(){
		List<String> imports=new ArrayList<String>();
		String m=getAttribute("import");
		if(m!=null){
			for(String s: m.split(",")){
				imports.add(s.trim());
			}
		}
		return imports;
	}
}