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
package com.tsc9526.monalisa.tools.string;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassAssist;
import com.tsc9526.monalisa.tools.datatable.DataMap;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpString {
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	
	public static String toString(Object bean){
		if(bean==null){
			return "null";
		}
		
		if(bean instanceof Throwable){
			StringWriter w = new StringWriter();
	
			((Throwable)bean).printStackTrace(new PrintWriter(w));
	
			return w.toString();
		}else{
			String cn=bean.getClass().getName();
			if(cn.startsWith("java.") || cn.startsWith("javax.")){
				return bean.toString();
			}else{
				try{
					ClassAssist mc=MelpClass.getMetaClass(bean);
					
					StringBuffer sb = new StringBuffer();
					for (FGS fgs : mc.getFields()) {
						Object v = fgs.getObject(bean);
						if (v != null) {
							if (sb.length() > 0) {
								sb.append(", ");
							}
							sb.append(fgs.getFieldName() + ": ");
							
							if(v.getClass() == byte[].class || v.getClass() == Byte[].class){
								MelpSQL.appendBytes(sb,(byte[])v);
							}else if(v instanceof InputStream){
								MelpSQL.appendStream(sb,(InputStream)v);
							}else{
								String s = "" + MelpClass.convert(v, String.class);
								sb.append(s);
							}
						}
					}
					sb.append("}");
					sb.insert(0, bean.getClass().getName()+":{");
					return sb.toString();
				}catch(IOException e){
					throw new RuntimeException(e);
				}
			}
		}
	}
	 
	
	public static String toJson(Object bean) {
		Gson gson=MelpJson.getGson();
		
		return MelpJson.toJson(gson,bean); 
	}
	 
 
	public static String toXml(Object bean){
		return toXml(bean,true,true);
	}
	
	public static String toXml(Object bean,boolean withXmlHeader, boolean ignoreNullFields) {
		StringBuilder sb = new StringBuilder();

		boolean pretty = true;
		String CRLN = "\r\n";

		if (withXmlHeader) {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			if (pretty) {
				sb.append(CRLN);
			}
		}

		String indent = "";

		String topTag = bean.getClass().getSimpleName();
		if (pretty) {
			sb.append(indent);
		}
		sb.append('<').append(topTag).append('>');
		if (pretty) {
			sb.append(CRLN);
		}
		
		ClassAssist mc=MelpClass.getMetaClass(bean);
		for (FGS fgs : mc.getFields()) {
			String name = fgs.getFieldName();

			Object v = fgs.getObject(bean);
			if (v != null) {
				String value = (String) MelpClass.convert(v, String.class);

				if (pretty) {
					sb.append("  ").append(indent);
				}
				sb.append('<').append(name).append('>');
				 
				sb.append(value.replace("&","&amp;").replaceAll("<", "&lt;").replaceAll(">","&gt;"));

				sb.append("</").append(name).append('>');
				if (pretty) {
					sb.append(CRLN);
				}
			} else if (!ignoreNullFields) {
				sb.append('<').append(name).append("/>");
			}
		}

		if (pretty) {
			sb.append(indent);
		}
		sb.append("</").append(topTag).append('>');

		return sb.toString();
	}	
	
	public static DataMap json2Map(String json){
		return MelpJson.parseToDataMap(json);
	}
	
	public static DataMap xml2Map(String xml){
		xml=xml.trim();
		
		//check xml tag
		int p1=xml.indexOf("<xml>");
		int p2=xml.indexOf("</xml>");
		if(p2>=p1 && p1>0){
			xml="<root>"+xml.substring(p1+"<xml>".length(),p2)+"</root>";
		}
		
		//check xml header
		if(!xml.startsWith("<?xml")){
			xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"+xml;
		}
		
		try{
			DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = domfactory.newDocumentBuilder();
		 
			Document doc= builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			
			return new XMLParser().toMap(doc.getChildNodes().item(0)); 
			 
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 分隔字符串， 可能的分隔符号有3个： 逗号(,) 分号(;) 竖号(|)
	 * 
	 * @param ms 待分隔的字符串
	 * @return 分隔后的数组
	 */
	public static String[] splits(String... ms) {
		List<String> xs = new ArrayList<String>();
		for (String s : ms) {
			if(s!=null){
				for (String x : s.split(",|;|\\|")) {
					if (x != null && x.trim().length() > 0) {
						xs.add(x.trim());
					}
				}
			}
		}

		return xs.toArray(new String[0]);
	}

	public static String escapeStringValue(String v) {
		if (v == null) {
			return null;
		}

		StringBuffer r = new StringBuffer();
		for (int i = 0; i < v.length(); i++) {
			char c = v.charAt(i);
			if (c == '\\' && (i + 1) < v.length()) {
				i++;
				c = v.charAt(i);
			}
			r.append(c);
		}
		return r.toString();
	}

	 

	public static String join(String[] vs, int from, int len, String joinString) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < (from + len) && i < vs.length; i++) {
			if (sb.length() > 0) {
				sb.append(joinString);
			}
			sb.append(vs[i]);
		}
		return sb.toString();
	}

	public static String join(List<String> vs, int from, int len, String joinString) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < (from + len) && i < vs.size(); i++) {
			if (sb.length() > 0) {
				sb.append(joinString);
			}
			sb.append(vs.get(i));
		}
		return sb.toString();
	}

	public static String[] shiftLeft(String[] vs, int len) {
		if (vs.length > len) {
			return Arrays.copyOfRange(vs, len, vs.length - 1);
		} else {
			return null;
		}
	}

	public static URL[] toURLs(String[] classPath) {
		List<URL> urls = new ArrayList<URL>();
		try {
			for (String x : classPath) {
				File file = new File(x);
				if (file.exists()) {
					urls.add(file.toURI().toURL());
				}
			}

			return urls.toArray(new URL[0]);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	 

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String intToBytesString(int i) {
		byte[] b = intToBytes(i);
		return bytesToHexString(b);
	}

	public static byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) ((i >> 24) & 0xFF);
		b[1] = (byte) ((i >> 16) & 0xFF);
		b[2] = (byte) ((i >> 8) & 0xFF);
		b[3] = (byte) ((i) & 0xFF);
		return b;
	}

	public static String bytesToHexString(byte[] src) {
		return bytesToHexString(src, null);
	}

	public static String bytesToHexString(byte[] src, String bytePrefix) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;

			if (bytePrefix != null && bytePrefix.length() > 0) {
				stringBuilder.append(bytePrefix);
			}

			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
}
