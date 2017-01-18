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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import com.tsc9526.monalisa.orm.datatable.DataMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ServletHelper {
	/**
	 * Web root path       : /example <br>
	 * Servlet content path: /dbs     <br>
	 * Request path        : /example/dbs/xxx/yyy... <br>
	 * 
	 * return: xxx/yyy...  <br>
	 * 
	 * @param request HttpServletRequest
	 * @return path without servlet content path.
	 */
	public static String getRequestPath(HttpServletRequest request){
		String uri = request.getRequestURI();

		String prefix = request.getContextPath();
		if(prefix==null){
			prefix="";
		}
		prefix += request.getServletPath();

		String pathRequest = uri.substring(prefix.length());
		if (pathRequest.endsWith("/") && pathRequest.length() > 1) {
			pathRequest = pathRequest.substring(0, pathRequest.length() - 1);
		}
		if (pathRequest.startsWith("/")) {
			pathRequest = pathRequest.substring(1);
		}
		try {
			pathRequest = URLDecoder.decode(pathRequest, "utf-8");
			
			return pathRequest;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static DataMap toDataMap(HttpServletRequest request){
		DataMap requestMap=new DataMap();
		Map<String, String[]> rs = request.getParameterMap();
		for (String name : rs.keySet()) {
			requestMap.put(name, rs.get(name));
		}
		return requestMap;
	}
	
	public static String getBodyString(HttpServletRequest request,String ...charset)throws IOException{
		byte[] bytes=getBodyBytes(request);
		
		String cs= charset.length>0 ?charset[0] : "utf-8";
		
		return new String(bytes,cs);
	}
	
	public static byte[] getBodyBytes(HttpServletRequest request)throws IOException{
		int size = request.getContentLength();
		if(size<=0){
			return null;
		}
		
		InputStream is = request.getInputStream(); 
 
		byte[] reqBodyBytes = FileHelper.readBytes(is, size);
		
		String encoding=request.getHeader("Content-Encoding");
		if(encoding!=null && encoding.indexOf("gzip")>=0){
			GZIPInputStream gzip=new GZIPInputStream(new ByteArrayInputStream(reqBodyBytes));
			
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte[] buf=new byte[64*1024];
			int len=gzip.read(buf);
			while(len>0){
				bos.write(buf,0,len);
				
				len=gzip.read(buf);
			}
			
			return bos.toByteArray();
		}else{
			return reqBodyBytes;
		}
	}

	public static String getRequestRealIp(HttpServletRequest request) {
		String ip = null;
		String[] ip_headers = new String[] { 
				"x-forwarded-for", 
				"X-Forwarded-For", 
				"HTTP_X_FORWARDED_FOR", 
				"Proxy-Client-IP", 
				"WL-Proxy-Client-IP",
				"HTTP_CLIENT_IP" };
		for (String ih : ip_headers) {
			String ips = request.getHeader(ih);

			if (ips != null && ips.length() > 0 && ("unknown".equalsIgnoreCase(ips) == false)) {
				String[] vs = ips.split(",");
				for (String v : vs) {
					v = v.trim();
					if ("unknown".equalsIgnoreCase(v) == false) {
						ip = v;
						break;
					}
				}

				if (ip != null) {
					break;
				}
			}
		}

		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		int p = ip.indexOf(":");
		if (p > 0) {
			ip = ip.substring(0, p);
		}

		return ip;
	}

	public static String getContextName(){
		ClassLoader loader=ServletHelper.class.getClassLoader();
		try{
			//org.apache.catalina.loader.WebappClassLoader
			Method gcn=loader.getClass().getMethod("getContextName");
		 	 
			return (String)gcn.invoke(loader);
		}catch(Exception e){
			return null;
		}
	}
}
