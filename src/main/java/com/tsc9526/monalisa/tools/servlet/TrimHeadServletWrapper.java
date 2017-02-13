package com.tsc9526.monalisa.tools.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TrimHeadServletWrapper extends HttpServletResponseWrapper{
	private PrintWriter pw=null;
	
	public TrimHeadServletWrapper(HttpServletResponse response) {
		super(response);
	}	
	
	public PrintWriter getWriter() throws IOException{
		if(pw==null){
			pw=new HeadTrimWriter(super.getWriter(),true);
		}
		return pw;
	}
	
	
	public class HeadTrimWriter extends PrintWriter {
		private long writeLength=0L;
		
		public HeadTrimWriter(PrintWriter out) {
			super(out);			
		}
		
		public HeadTrimWriter(PrintWriter out,boolean autoFlush) {
			super(out,autoFlush);			
		}
	 
		public void write(int c) {
			if(writeLength==0 && c=='\n'){
				
			}else{
				writeLength++;
				
				super.write(c);
			}
		}
		
		public void write(char buf[], int off, int len) {
			if(writeLength==0){
				int start=off;
				
				for(int i=off;i-off<len;i++){
					if(buf[i]=='\r' || buf[i]=='\n'){
						start++;
					}else{
						break;
					}
				}
				
				int l=len-(start-off);
				if(l>0){
					writeLength+=l;
					
					super.write(buf, start, l);
				}
			}else{
				writeLength+=len;
				
				super.write(buf, off, len);
			}			 
		}
		
		public void write(String s, int off, int len) {
			if(len>0){
				writeLength+=len;
				super.write(s, off, len);
			}
		}
		
		 public void println() {
			 if(writeLength>0){
				 super.println();
			 }
		 }
	}
}
