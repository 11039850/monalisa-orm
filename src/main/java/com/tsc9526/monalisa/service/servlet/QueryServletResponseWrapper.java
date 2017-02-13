package com.tsc9526.monalisa.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class QueryServletResponseWrapper extends HttpServletResponseWrapper{
	private StringWriter content=new StringWriter();
	
	private PrintWriter pw=new PrintWriter(content);
	
	public QueryServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}	
	
	public PrintWriter getWriter() throws IOException{
		return pw;
	}
	
	public String getContent(){
		String s= content.toString();
		return s.replaceAll("^[\\s]+", "");
		
	}
}
