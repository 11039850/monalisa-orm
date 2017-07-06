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
package com.tsc9526.monalisa.tools.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.misc.MelpException;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpServlet {
	private MelpServlet(){}
	
	/**
	 * Webroot path : /example <br>
	 * Servlet path : /dbs     <br>
	 * Request path : /example/dbs/xxx/yyy... <br>
	 * <br>
	 * return: xxx/yyy...  <br>
	 * 
	 * @param request HttpServletRequest
	 * @return path without servlet content path.
	 */
	public static String getActionPath(HttpServletRequest request){
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
	
	/**
	 * 
	 * retrieve attributes in the following order (ignore the case of the key):
	 * <pre> 
	 * 1. request.getParameter()
	 * 2. request.getAttribute()
	 * 3. request.getSession().getAttribute()
	 * 4. request.getServletContext().getAttribute()
	 * </pre>
	 * @param request the servlet request
	 * @return data map
	 */
	public static DataMap toDataMapAll(HttpServletRequest request){
		DataMap m=new DataMap();
		
		m.putAll(toDataMapAttrs(request));
		
		m.putAll(toDataMapParas(request));
		
		return m;
	}
	
	/**
	 * retrieve parameters from request.getParameter  (ignore the case of the key). 
	 * @param request the servlet request
	 * @return data map (ignore the case of the key)
	 */
	public static DataMap toDataMapParas(HttpServletRequest request){
		DataMap m=new DataMap();
	 	
		Map<String, String[]> rs = request.getParameterMap();
		for(Map.Entry<String, String[]> entry: rs.entrySet()) {
			String[] vs=entry.getValue();
			if(vs.length==1){
				m.put(entry.getKey(), vs[0]);
			}else{
				m.put(entry.getKey(), vs);
			}
		}
		return m;
	}
	
	/**
	 * 
	 * retrieve attributes in the following order  (ignore the case of the key):
	 * <pre> 
	 * 1. request.getAttribute()
	 * 2. request.getSession().getAttribute()
	 * 3. request.getServletContext().getAttribute()
	 * </pre>
	 * @param request the servlet request
	 * @return data map (ignore the case of the key)
	 */
	public static DataMap toDataMapAttrs(HttpServletRequest request){
		DataMap m=new DataMap();
		
		ServletContext sc=request.getServletContext();
		Enumeration<String> es=sc.getAttributeNames();
		while(es.hasMoreElements()){
			String name  = es.nextElement();
			Object value = sc.getAttribute(name);
			m.put(name,value);
			
		} 
		
		HttpSession session=request.getSession();
		es=session.getAttributeNames();
		while(es.hasMoreElements()){
			String name  = es.nextElement();
			Object value = session.getAttribute(name);
			m.put(name,value);
		} 
		
		es=request.getAttributeNames();
		while(es.hasMoreElements()){
			String name  = es.nextElement();
			Object value = request.getAttribute(name);
			m.put(name,value);
		} 
		
		return m;
	}
	 
	public static <T> List<T> parseArrays(T targetTemplate, javax.servlet.ServletRequest data, String... mappings) {
		return MelpClass.parseArrays(targetTemplate, data, mappings);
	}
	
	public static String getBodyString(HttpServletRequest request,String ...charset)throws IOException{
		byte[] bytes=getBodyBytes(request);
		
		String cs= charset.length>0 ?charset[0] : "utf-8";
		
		return new String(bytes,cs);
	}
	
	public static byte[] getBodyBytes(HttpServletRequest request)throws IOException{
		int size = request.getContentLength();
		if(size<=0){
			return new byte[0];
		}
		
		InputStream is = request.getInputStream(); 
 
		byte[] reqBodyBytes = MelpFile.readBytes(is, size);
		
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
			"HTTP_CLIENT_IP" 
		};
		
		for(int i=0;ip==null && i<ip_headers.length; i++) {
			String ips = request.getHeader(ip_headers[i]);			
			 
			if(ips==null || "unknown".equalsIgnoreCase(ips)){
				continue;
			}
			 
			String[] vs = ips.split(",");
			for (String v : vs) {
				v = v.trim();
				if (!"unknown".equalsIgnoreCase(v)) {
					ip = v;
					break;
				}
			}
		}

		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		int p = ip.indexOf(':');
		if (p > 0) {
			ip = ip.substring(0, p);
		}

		return ip;
	}

	public static String getContextName(){
		ClassLoader loader=MelpServlet.class.getClassLoader();
		try{
			//org.apache.catalina.loader.WebappClassLoader
			Method gcn=loader.getClass().getMethod("getContextName");
		 	 
			return (String)gcn.invoke(loader);
		}catch(Exception e){
			return MelpException.throwRuntimeException(e);
		}
	}
}
