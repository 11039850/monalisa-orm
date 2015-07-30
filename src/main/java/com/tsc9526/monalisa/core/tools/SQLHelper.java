package com.tsc9526.monalisa.core.tools;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
 
public class SQLHelper {	
	
	public static String getExecutableSQL(String original,List<Object> parameters) {		 
		if(parameters==null || parameters.size()==0){
			return original;
		}else{	
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
					}else if(p instanceof Integer || p.getClass()==int.class){
						sb.append(p);
					}else if(p instanceof Long || p.getClass()==long.class){
						sb.append(p);
					}else if(p instanceof Float || p.getClass()==float.class){
						sb.append(p);
					}else if(p instanceof Double || p.getClass()==double.class){
						sb.append(p);
					}else if(p instanceof Byte || p.getClass()==byte.class){
						sb.append(p);
					}else if(p instanceof Boolean || p.getClass()==boolean.class){
						sb.append(p);
					}else if(p instanceof Date){						
						sb.append("'").append(Helper.getTime((Date)p)).append("'");
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
}
