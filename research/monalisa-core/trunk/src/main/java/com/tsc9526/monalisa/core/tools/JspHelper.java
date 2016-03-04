package com.tsc9526.monalisa.core.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JspHelper {
	public static class Jsp{
		private String body;
		private List<JspElement> es=new ArrayList<JspHelper.JspElement>();
		 
		public Jsp(String body) {
			this.body=body;
			
			int len=body.length();
			for(int i=0;i<len;i++){
				char c=body.charAt(i);
				if(c=='<' && i<len-1 && body.charAt(i+1)=='%'){
					int k=body.indexOf("%>",i);
					 
					if(i<len-2 && body.charAt(i+2)=='@'){
						//page element
						JspPageImport e=new JspPageImport();
						e.setJavaCode(body.substring(i+3,k));
						es.add(e);
					}else if(i<len-2 && body.charAt(i+2)=='!'){
						JspFunction e=new JspFunction();
						e.setJavaCode(body.substring(i+3,k));
						es.add(e);
					}else{
						JspCode e=new JspCode();
						e.setJavaCode(body.substring(i+2,k));
						es.add(e);
					}
					
					i=k+1;
				}else{
					int k=body.indexOf("<%",i);
				
					String text="";
					if(k<0){
						text=body;
						i=body.length()-1;
					}else{
						text=body.substring(i,k);
						
						i=k-1;
					} 
				 	
					JspText e=new JspText();
					e.setJavaCode(textToJava(text));
					es.add(e);
				}
			}
		}
		
		
		private String textToJava(String text){		
			try{
				StringBuffer sb=new StringBuffer();
				
				BufferedReader reader=new BufferedReader(new StringReader(text));
				String line=reader.readLine();
				while(line!=null){
					sb.append("System.out.println(\"").append(line.replace("\"", "\\\"")).append("\"");
					
					line=reader.readLine();
				}
				return sb.toString();
			}catch(IOException e){
				throw new RuntimeException(e);
			}
		}
		
		public void write(OutputStream out)throws Exception{
			JavaWriter w=new JavaWriter(out);
			w.println("package tmp;");
			w.println();
			
			for(JspElement e:es){
				if(e instanceof JspPageImport){
					for(String i:((JspPageImport)e).getImports()){					
						w.println("import "+i+";");
					}
				}				
			}
			
			for(JspElement e:es){
				if(e instanceof JspFunction){
					w.println(e.toString());
					
					w.println();
				}				
			}
			
			w.println("public void service(java.io.PrintWriter out){");
			for(JspElement e:es){
				w.println(e.getJavaCode());
				 				
			}
			w.println("}");
			
			w.close();
			 
		}
	}
	
	public static class JspPageImport extends JspPage{
		public List<String> getImports(){
			return null;
		}
	}
	
	public static class JspPage extends JspElement{
		 
	}

	public static class JspText extends JspElement{
		
	}

	public static class JspCode    extends JspElement{
		 
	}
	
	public static class JspFunction extends JspElement{
		 
	}
 
	
	public static class JspElement{
		private int pos;
		private int length;
		
		private Jsp jsp;
		
		private String javaCode;
		
		public String toString(){
			return jsp.body.substring(pos,length);
		}

		public String getJavaCode() {
			return javaCode;
		}

		public void setJavaCode(String javaCode) {
			this.javaCode = javaCode;
		}
		
		
	}
	
}
