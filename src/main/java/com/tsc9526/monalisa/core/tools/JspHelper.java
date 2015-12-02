package com.tsc9526.monalisa.core.tools;

import java.io.OutputStream;
import java.util.List;

public class JspHelper {
	
	
	
	public static class Jsp{
		private String body;
		private List<JspElement> es;
		 
		public Jsp(String body) {
			this.body=body;
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
				if(e instanceof JspCode){
					w.println(e.toString());
				}else if(e instanceof JspText){
					String text=e.toString();
					text=text.replace("\"", "\\\"");
					w.println("out.write(\""+text+"\");");
				}					
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
		
		public String toString(){
			return jsp.body.substring(pos,length);
		}
	}
	
}
