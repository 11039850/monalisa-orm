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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.json.MelpJson;
import com.tsc9526.monalisa.tools.xml.XMLDocument;
import com.tsc9526.monalisa.tools.xml.XMLObject;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpString {
	private MelpString(){}
	
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	public static String toString(Object bean){
		if(bean==null){
			return "<NULL>";
		}
		
		if(bean instanceof Throwable){
			StringWriter w = new StringWriter();
			((Throwable)bean).printStackTrace(new PrintWriter(w));
			return w.toString();
		}else if(bean instanceof ResultSet){
			return DataTable.fromResultSet((ResultSet)bean).format();
		}else if(MelpTypes.isPrimitiveOrString(bean) || bean.getClass().isEnum()){
			return bean.toString();
		}else{
			return MelpJson.toJson(bean);
		}
	}
	 
	public static String leftPadding(String source,int size){
		return leftPadding(source,' ',size);
	}
	
	public static String leftPadding(String source,char padChar,int size){
		StringBuilder sb=new StringBuilder();
		if(isEmpty(source)){
			for(int i=0;i < size; i++){
				sb.append(padChar);
			}
		}else{
			int p = size - source.length();
			for(int i=0;i < p; i++){
				sb.append(padChar);
			}
			sb.append(source);
		}
		return sb.toString();
	}
	
	public static String rightPadding(String source,int size){
		return rightPadding(source,' ',size);
	}
	
	public static String rightPadding(String source,char padChar,int size){
		StringBuilder sb=new StringBuilder();
		if(isEmpty(source)){
			for(int i=0;i < size; i++){
				sb.append(padChar);
			}
		}else{
			sb.append(source);
			
			int p = size - source.length();
			for(int i=0;i < p; i++){
				sb.append(padChar);
			}
		}
		return sb.toString();
	}
	
	public static String toJson(Object bean) {
		Gson gson=MelpJson.getGson();
		
		return MelpJson.toJson(gson,bean); 
	}
	 
	public static String repeat(String x,int times){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<times;i++){
			sb.append(x);
		}
		return sb.toString();
	}
 
	public static String toXml(Object bean){
		return new XMLObject(bean).toString();
	}
	
	public static String toXml(Object bean,boolean withXmlHeader, boolean ignoreNullFields) {
		return new XMLObject(bean)
			.setWithXmlHeader(withXmlHeader)
			.setIgnoreNullFields(ignoreNullFields)
			.toString();	
	}	
	 
	public static DataMap json2Map(String json){
		return MelpJson.parseToDataMap(json);
	}
	
	public static String normalizeXml(String xmlString){
		String xml=xmlString.trim();
		
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
		return xml;
	}
	
	public static DataMap xml2Map(String xmlString){
		String xml=normalizeXml(xmlString); 
		
		try{
			XMLDocument p= new XMLDocument(); 
			Document doc=p.parseDocument(new InputSource(new StringReader(xml)));
			return p.toMap(doc.getChildNodes().item(0));  
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

		StringBuilder r = new StringBuilder();
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

	 
	public static String join(String[] vs,String joinString) {
		return join(vs,0,vs.length,joinString);
	}
	
	public static String join(String[] vs, int from, int len, String joinString) {
		StringBuilder sb = new StringBuilder();
		for (int i = from; i < (from + len) && i < vs.length; i++) {
			if (sb.length() > 0) {
				sb.append(joinString);
			}
			sb.append(vs[i]);
		}
		return sb.toString();
	}

	public static String join(List<String> vs,String joinString) {
		return join(vs,0,vs.size(),joinString);
	}
	
	public static String join(List<String> vs, int from, int len, String joinString) {
		StringBuilder sb = new StringBuilder();
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
			return new String[0];
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
		if (hexString == null || hexString.isEmpty()) {
			return new byte[0];
		}
		
		String hex = hexString.toUpperCase();
		int length = hex.length() / 2;
		char[] hexChars = hex.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 | (hexCharToByte(hexChars[pos + 1]) & 0x0FF ));

		}
		return d;
	}
 	
	public static byte hexCharToByte(char c) {
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
	
	public static byte[] toBytesUtf8(String data){
		return toBytes(data, "utf-8");
	}
	
	public static byte[] toBytes(String data,String charset){
		if(data==null || data.length()==0){
			return new byte[0];
		}else{
			try{
				return data.getBytes(charset);
			}catch(UnsupportedEncodingException e){
				throw new RuntimeException(e);
			}
		}
	}
	
	public static String fromBytesUtf8(byte[] data) {
		return fromBytes(data, "utf-8");
	}
	
	public static String fromBytes(byte[] data,String charset) {
		if(data==null){
			return null;
		}else if(data.length==0) {
			return "";
		}else{
			try{
				return new String(data,charset);
			}catch(UnsupportedEncodingException e){
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Translates a string into URL parameter value
	 * @param s source string
	 * @return  the translated String using charset: UTF-8.
	 */
	public static String urlEncodeUtf8(String s) {
		return urlEncode(s,"utf-8");
	}
	
	/**
	 * Translates a string into URL parameter value
	 * 
	 * @param s        source string
	 * @param charset  the encode charset
	 * @return         the translated String using the charset.
	 */
	public static String urlEncode(String s,String charset) {
		try {
			return URLEncoder.encode(s,charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Invalid url encode("+charset+"): "+s,e);
		}
	}
	
	/**
	 * Decodes a application/x-www-form-urlencoded string using a specific encoding scheme
	 * @param s  source string
	 * @return  the newly decoded String using charset: UTF-8.
	 */
	public static String urlDecodeUtf8(String s) {
		return urlDecode(s,"utf-8");
	}
	
	/**
     * Decodes a application/x-www-form-urlencoded string using a specific encoding scheme
	 * @param s  source string
	 * @param charset  the decode charset
	 * @return   the newly decoded String using the charset
	 */
	public static String urlDecode(String s,String charset) {
		try {
			return URLDecoder.decode(s, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Invalid url decode("+charset+"): "+s,e);
		}
	}

	public static int getCharWidth(String s) {
		if(s==null || s.length()==0){
			return 0;
		}
		
		int w=0;
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			if(c>255){
				w+=2;
			}else{
				w++;
			}
		}
		
		return w;
	}
}
