package com.tsc9526.monalisa.core.parser.jsp;

import java.io.File;
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
	 
	public Jsp(File jspFile)throws IOException {
		this.filePath=jspFile.getAbsolutePath();
		
		String body=FileHelper.readToString(new FileInputStream(filePath), DEFAULT_PAGE_ENCODING);
		
		parseBody(body);
		
		String encoding=getPageEncoding();
		if(!DEFAULT_PAGE_ENCODING.equalsIgnoreCase(encoding)){
			elements.clear();
			body=FileHelper.readToString(new FileInputStream(filePath), encoding);			
			parseBody(body);
		}
	}
	
	public Jsp(String body){
		parseBody(body);
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
	
	protected void parseBody(String body){
		this.body=body;
		
		int len=body.length();
		for(int i=0;i<len;i++){
			char c=body.charAt(i);
			if(c=='<' && i<len-1 && body.charAt(i+1)=='%'){
				int k=body.indexOf("%>",i);
				 
				if(i<len-2 && body.charAt(i+2)=='@'){
					String page=body.substring(i+3,k).trim();
					if(page.startsWith("page")){
						page=page.substring(4).trim();
						add(new JspPage(this,i,k+2-i).parseCode(page));
					}
				}else if(i<len-2 && body.charAt(i+2)=='!'){
					add(new JspFunction(this,i,k+2-i).parseCode(body.substring(i+3,k)));
				}else if(i<len-2 && body.charAt(i+2)=='='){
					add(new JspEval(this,i,k+2-i).parseCode(body.substring(i+3,k)));
				}else{
					add(new JspCode(this,i,k+2-i).parseCode(body.substring(i+2,k))); 
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
			 
				add(new JspText(this,pos,k-pos).parseCode(text));
			}
		}
	}
	
	
	private JspElement add(JspElement e){
		e.setIndex(elements.size());
		elements.add(e);
		return e;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
 
	public String getBody() {
		return body;
	}

	public JspElement getElement(int index){
		return elements.get(index);
	}
	
	public List<JspElement> getElements() {
		return elements;
	}
}