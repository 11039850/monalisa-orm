/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpFile {
	
	public static byte[] toBytes(InputStream input) throws IOException {
		byte[] buffer = new byte[Math.max(1024, input.available())];
		int offset = 0;
		for (int bytesRead; -1 != (bytesRead = input.read(buffer, offset, buffer.length - offset));) {
			offset += bytesRead;
			if (offset == buffer.length) {
				buffer = Arrays.copyOf(buffer, buffer.length + Math.max(input.available(), buffer.length >> 1));
			}
		}
		return (offset == buffer.length) ? buffer : Arrays.copyOf(buffer, offset);
	}
	
	public static byte[] readFile(String filePath)throws IOException{
		return readBytes(new FileInputStream(filePath));
	}
	
	public static byte[] readFile(File file)throws IOException{
		return readBytes(new FileInputStream(file));
	}
	
	public static byte[] readBytes(InputStream in)throws IOException{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		byte[] buf=new byte[16*1024];
		int len=in.read(buf);
		while(len>0){
			bos.write(buf,0, len);
			
			len=in.read(buf);
		}
		in.close();
		
		return bos.toByteArray();
	}
	
	public static byte[] readBytes(InputStream is, int length)throws IOException {
		int readLen = 0;
		int readLengthThisTime = 0;
		byte[] message = new byte[length];
		  
		while (readLen != length) {
			readLengthThisTime = is.read(message, readLen, length - readLen);
			if (readLengthThisTime == -1) {// Should not happen.
				break;
			}
			readLen += readLengthThisTime;
		}
		return message;	 
	}
	
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
	
	public static String[] combineExistFiles(String[]... ls) {
		if (ls == null) {
			return null;
		}

		List<String> rs = new ArrayList<String>();
		for (String[] s : ls) {
			if (s != null) {
				for (String x : s) {
					if (x != null) {
						if (new File(x).exists() && rs.contains(x) == false) {
							rs.add(x);
						}
					}
				}
			}
		}
		return rs.toArray(new String[0]);
	}
	
	public static File mkdirs(String dir){
		return mkdirs(new File(dir));
	}
	
	public static File mkdirs(File dir){
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}
	
	/**
	 * 
	 * @param f  the file/dir to be deleted
	 * @param delete true if delete the root dir, otherwise false.
	 */
  	public static void delete(File f,boolean delete){
		if(f.isFile()){
			f.delete();
		}else if(f.isDirectory()){
			File[] fs=f.listFiles();
			
			if(fs!=null && fs.length>0){
				for(File i:f.listFiles()){
					delete(i,true);
				}
			}
			
			if(delete){
				f.delete();
			}
		}
	}
   	
	public static void writeUTF8(File target,String data){
		write(target,data,"utf-8");
	}
	
  	public static void write(File target,String data,String charset) {
  		try{
  			write(target,data.getBytes(charset));
  		}catch(IOException e){
			throw new RuntimeException(e);
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
	
	
	public static void write(InputStream from,OutputStream to)throws IOException {
		try{
			byte[] buf=new byte[64*1024];
			
			int len=from.read(buf);
			while(len>0){
				to.write(buf,0,len);
				len=from.read(buf);
			}
		}finally{
			MelpClose.close(from,to);
		}
	}
	
	public static void copy(File src,File target)throws IOException {
		byte[] data=readFile(src);
		write(target, data);
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
			MelpClose.close(fin);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readToObject(InputStream fin){
	 	try{
			ObjectInputStream inputStream=new ObjectInputStream(fin);
			T r=(T)inputStream.readObject();
			inputStream.close();
			return r;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			MelpClose.close(fin);
		}
	}
	
	public static String readToString(File f,String charset){
		try{
			return readToString(new FileInputStream(f),charset);
		}catch(IOException e){    		
    		throw new RuntimeException(e);
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
    		throw new RuntimeException(e);
    	}finally{
    		MelpClose.close(in);
    	}
    }
}
