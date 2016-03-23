package com.tsc9526.monalisa.core.parser.jsp;

import java.io.PrintWriter;

public class JspPageOut extends PrintWriter{
 	
	public JspPageOut(PrintWriter pw){
		super(pw,true);
	}
}
