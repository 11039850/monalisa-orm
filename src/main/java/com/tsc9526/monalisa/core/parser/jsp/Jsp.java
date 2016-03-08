package com.tsc9526.monalisa.core.parser.jsp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.tools.FileHelper;

public class Jsp{
	public static String DEFAULT_PAGE_ENCODING="utf-8";
	
	private String filePath;
	private String body;
	private List<JspElement> elements=new ArrayList<JspElement>();
	 
	public Jsp(String filePath)throws IOException {
		this.filePath=filePath;
		
		body=FileHelper.readToString(new FileInputStream(filePath), DEFAULT_PAGE_ENCODING);
		
		parseBody();
		
		String encoding=getPageEncoding();
		if(DEFAULT_PAGE_ENCODING.equalsIgnoreCase(encoding)){
			elements.clear();
			body=FileHelper.readToString(new FileInputStream(filePath), encoding);			
			parseBody();
		}
	}
	
	public String getPageEncoding(){
		String pageEncoding=null;
		for(JspElement e:elements){
			if(e instanceof JspPage){
				pageEncoding=((JspPage)e).getPageEncoding();
				if(pageEncoding!=null){
					return pageEncoding;
				}
			}
		}
		return DEFAULT_PAGE_ENCODING;
	}
	
	private void parseBody(){
		int len=body.length();
		for(int i=0;i<len;i++){
			char c=body.charAt(i);
			if(c=='<' && i<len-1 && body.charAt(i+1)=='%'){
				int k=body.indexOf("%>",i);
				 
				if(i<len-2 && body.charAt(i+2)=='@'){
					//page element	 
					String page=body.substring(i+3,k).trim();
					if(page.startsWith("page")){
						page=page.substring(4).trim();
						elements.add(new JspPage(this,i,k+2-i).parseCode(page));
					}
				}else if(i<len-2 && body.charAt(i+2)=='!'){
					elements.add(new JspFunction(this,i,k+2-i).parseCode(body.substring(i+3,k)));
				}else{
					elements.add(new JspCode(this,i,k+2-i).parseCode(body.substring(i+2,k))); 
				}
				
				i=k+1;
			}else{
				int pos=i;
				int k=body.indexOf("<%",i);
			
				String text="";
				if(k<0){
					text=body;
					i=body.length()-1;
				}else{
					text=body.substring(i,k);
					
					i=k-1;
				} 	
			 
				elements.add(new JspText(this,pos,k-pos).parseCode(text));
			}
		}
	}
	
	
	public String getFilePath(){
		return this.filePath;
	}
 
	public String getBody() {
		return body;
	}

	public List<JspElement> getElements() {
		return elements;
	}
}