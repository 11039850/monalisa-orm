package com.tsc9526.monalisa.core.tools;

import java.io.File;
import java.io.FileOutputStream;

import com.tsc9526.monalisa.core.parser.jsp.Jsp;

 
public class DBWriterHelper {
	public static void main(String[] args)throws Exception{
		DBWriterHelper jwc=new DBWriterHelper();
		jwc.createDBModelWriter();
		jwc.createDBSelectWriter();
		
	}
	public void createDBModelWriter()throws Exception{
		File jspFile=new File("./src/main/resources/com/tsc9526/monalisa/core/resources/template/model.jsp");
		Jsp jsp=new Jsp(jspFile);
		
		FileOutputStream fos=new FileOutputStream("src/main/java/com/tsc9526/monalisa/core/generator/DBWriterModel.java");
		jsp.writeToJava(new JavaWriter(fos), "com.tsc9526.monalisa.core.generator", "DBWriterModel","@author zzg.zhou(11039850@qq.com)");
		
	}
	
	public void createDBSelectWriter()throws Exception{
		File jspFile=new File("./src/main/resources/com/tsc9526/monalisa/core/resources/template/select.jsp");
		Jsp jsp=new Jsp(jspFile);
		
		FileOutputStream fos=new FileOutputStream("src/main/java/com/tsc9526/monalisa/core/generator/DBWriterSelect.java");
		jsp.writeToJava(new JavaWriter(fos), "com.tsc9526.monalisa.core.generator", "DBWriterSelect","@author zzg.zhou(11039850@qq.com)");
		
	}
	
}
