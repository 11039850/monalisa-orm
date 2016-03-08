package com.tsc9526.monalisa.core.parser.jsp;

public abstract class JspElement{
	protected int pos;
	protected int length;
	
	protected Jsp jsp;
	
	protected String code;
	
	public JspElement(Jsp jsp,int pos,int length) {
		this.jsp=jsp;
		this.pos=pos;
		this.length=length;
	}
	
	public String toString(){
		return jsp.getBody().substring(pos,pos+length);
	}

	public String getCode(){
		return this.code;
	}	
	
	public JspElement parseCode(String code){
		this.code=code;
		return this;
	}
}