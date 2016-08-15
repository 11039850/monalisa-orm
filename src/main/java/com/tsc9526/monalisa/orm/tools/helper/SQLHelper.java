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
package com.tsc9526.monalisa.orm.tools.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SQLHelper {	
	
	public static String getExecutableSQL(String original,List<Object> parameters) {		 
		if(parameters==null || parameters.size()==0){
			return original;
		}else{	
			 try{
				 return toSQL(original,parameters);
			 }catch(IOException e){
				 throw new RuntimeException(e);
			 }
		}
	}
	
	private static String toSQL(String original,List<Object> parameters)throws IOException{
		StringBuffer sb = new StringBuffer();
		int x=0;
		for(int i=0;i<original.length();i++){
			char c=original.charAt(i);
			if(c=='?'){
				if(x>=parameters.size()){
					return original;
				}
				
				Object p=parameters.get(x);
				if(p==null){
					sb.append("null");
				}else if(p instanceof Number || p instanceof Boolean){
					sb.append(p);
				}else if(p instanceof Date){
					sb.append("'").append(Helper.getTime((Date)p)).append("'");
				}else if(p.getClass() == byte[].class || p.getClass() == Byte[].class){
					appendBytes(sb,(byte[])p);
				}else if(p instanceof InputStream){
					appendStream(sb,(InputStream)p); 
				}else if(p instanceof Reader){
					appendReader(sb,(Reader)p); 
				}else{
					sb.append(escapeSqlValue(p.toString()));
				}	
				
				x++;
			}else{
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	public static void appendStream(StringBuffer sb,InputStream r)throws IOException{
		ByteArrayOutputStream tmp=new ByteArrayOutputStream();
		
		byte[] buf=new byte[16*1024];
		int len=r.read(buf);
		while(len>0){
			tmp.write(buf,0,len);
			
			len=r.read(buf);
		}
		r.close();
		
		appendBytes(sb,tmp.toByteArray());
	}
	
	private static void appendReader(StringBuffer sb,Reader r)throws IOException{
		StringBuffer tmp=new StringBuffer();
		
		char[] buf=new char[16*1024];
		int len=r.read(buf);
		while(len>0){
			tmp.append(new String(buf,0,len));
			len=r.read(buf);
		}
		r.close();
		
		sb.append(escapeSqlValue(tmp.toString()));
	}
	
	public static void appendBytes(StringBuffer sb,byte[] bytes){
		String s=Helper.bytesToHexString(bytes,"\\x");
		
		sb.append("'");
		sb.append(s);
		sb.append("'");
	}
	
	
	public static String escapeSqlValue(String v){
		if(v==null){
			return null;
		}
		
		StringBuffer r=new StringBuffer();
		r.append("'");
		for(int i=0;i<v.length();i++){
			char c=v.charAt(i);
			switch(c){
				case '\'': r.append("''");   break;
				case '"' : r.append("\\\""); break;
				case '\\': r.append("\\\\"); break;
				default  : r.append(c);
			}
		}
		r.append("'");
		return r.toString();		 
	}
	
	public static void setPreparedParameters(PreparedStatement pst,List<Object> parameters)throws SQLException{
		if(parameters!=null && parameters.size()>0){
			int parameterIndex=1;
			for(Object p:parameters){
				if(p==null){
					pst.setObject(parameterIndex, null);
				}else if(p instanceof Integer || p.getClass()==int.class){
					pst.setInt(parameterIndex, (Integer)p);
				}else if(p instanceof Long || p.getClass()==long.class){
					pst.setLong(parameterIndex, (Long)p);
				}else if(p instanceof Float || p.getClass()==float.class){
					pst.setFloat(parameterIndex, (Float)p);
				}else if(p instanceof Double || p.getClass()==double.class){
					pst.setDouble(parameterIndex, (Double)p);
				}else if(p instanceof Byte || p.getClass()==byte.class){
					pst.setByte(parameterIndex, (Byte)p);
				}else if(p instanceof Boolean || p.getClass()==boolean.class){
					pst.setBoolean(parameterIndex, (Boolean)p);
				}else if(p instanceof Date){
					pst.setTimestamp(parameterIndex, new java.sql.Timestamp(((Date)p).getTime()));
				}else if(p.getClass().isArray()){					
					pst.setBytes(parameterIndex, (byte[])p);
				}else{
					pst.setObject(parameterIndex, p);
				}				
				parameterIndex++;
			}
		}				
	}
	
	public static List<String> splitKeyWords(String sql){
		List<String> kws=new ArrayList<String>();
		
		StringBuffer word=new StringBuffer();
		for(int i=0;i<sql.length();i++){
			char c=sql.charAt(i);
			if(c==' ' || c=='\t' || c=='\r' || c=='\n' ){
				if(word.length()>0){
					kws.add(word.toString().toUpperCase());
					
					word.delete(0, word.length());
				}
			}else if(c=='=' || c=='>' || c=='<' || c=='!' || c==','){
				if(word.length()>0){
					kws.add(word.toString().toUpperCase());
					
					word.delete(0, word.length());
				}
				kws.add(String.valueOf(c));				
			}else{
				if(c=='\''){
					int from=i;
					
					i++;
					for(;i<sql.length();i++){
						c=sql.charAt(i);
						if(c=='\\'){
							i++;
						}else if(c=='\''){
							break;
						}
					}
					
					kws.add(sql.substring(from,i+1)); 
				}else if(c=='"'){
					int from=i;
					
					i++;
					for(;i<sql.length();i++){
						c=sql.charAt(i);
						if(c=='\\'){
							i++;
						}else if(c=='"'){
							break;
						}
					}
					
					kws.add(sql.substring(from,i+1)); 
				}else{
					word.append(c);
				}
			}
		}
		
		if(word.length()>0){
			kws.add(word.toString().toUpperCase());
			
			word.delete(0, word.length());
		}
		
		return kws;
	}
	
	public static boolean isStartByKeyWord(String s,String keyword){
		if(s==null || s.trim().length()==0){
			return false;
		}
		
		String x=s.trim().toUpperCase();
		int len=keyword.length();
		if(x.startsWith(keyword.toUpperCase()) && x.length()>len ){
			char c=x.charAt(len);
			
		 
			if( c==' ' || c=='\t' || c=='\r' || c=='\n'){
				return true;
			}
		}
		
		return false;
	}
}
