package com.tsc9526.monalisa.core.parser.jsp;


public class JspCode extends JspElement{
	private boolean eval=false;
	public JspCode(Jsp jsp,int pos,int length) {
		super(jsp, pos, length);
	}
	
	public boolean isEval(){
		return eval;
	}
	
	public JspElement parseCode(String code){
		if(code.startsWith("=")){
			eval=true;
			this.code=code.substring(1);
		}else{
			this.code=code;
		}
		return this;
	}
}