package com.tsc9526.monalisa.core.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class FileHelper {
	
	public static String combinePath(String... paths){
		StringBuffer sb=new StringBuffer();
		for(String x:paths){
			x=x.replace("\\","/");
			
			if(sb.length()==0){
				sb.append(x);
			}else{
				char last=sb.charAt(sb.length()-1);
				
				if(x.startsWith("/")){
					if(last=='/'){
						sb.append(x.substring(1));
					}else{
						sb.append(x);
					}
				}else{
					if(last=='/'){
						sb.append(x);
					}else{
						sb.append("/").append(x);
					}
				}
				
			}			 
		}
		return sb.toString(); 
	}
	
  	public static void delete(File f,boolean delete){
		if(f.isFile()){
			f.delete();
		}else{
			File[] fs=f.listFiles();
			if(fs!=null && fs.length>0){
				for(File i:f.listFiles()){
					delete(i,true);
				}
				if(delete){
					f.delete();
				}
			}
		}
	}
   	
	public static void write(File target, byte[] data) {
		try{
			String path=target.getAbsolutePath().replaceAll("\\\\", "/");
			int p=path.lastIndexOf("/");
			File dir=new File(path.substring(0,p));
			if(dir.exists()==false){
				dir.mkdirs();
			}
			
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(data);
			fos.close();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readToObject(File f){
		FileInputStream fin=null;
		try{
			fin=new FileInputStream(f);
			ObjectInputStream inputStream=new ObjectInputStream(fin);
			T r=(T)inputStream.readObject();
			inputStream.close();
			return r;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(fin);
		}
	}
	
	public static String readToString(InputStream in,String charset){
    	try{
	    	ByteArrayOutputStream bos=new ByteArrayOutputStream();
	    	byte[] buf=new byte[64*1024];
	    	int len=in.read(buf);
	    	while(len>0){
	    		bos.write(buf, 0, len);
	    		len=in.read(buf);
	    	}
	    		    	
	    	return new String(bos.toByteArray(),charset);
    	}catch(IOException e){
    		e.printStackTrace();
    		
    		CloseQuietly.close(in);
    		
    		return null;
    	}
    }
}
