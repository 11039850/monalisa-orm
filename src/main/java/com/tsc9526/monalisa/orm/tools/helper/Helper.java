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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Helper {
	 
	/**
	 * 分隔字符串， 可能的分隔符号有3个：   逗号(,) 分号(;) 竖号(|)
	 * @param m 待分隔的字符串
	 * @return 分隔后的数组
	 */
	public static String[] splits(String m){
		if(m==null){
			return null;
		}
		
		String[] xs=m.trim().split(",|;|\\|");
		
		return xs;
	}
	
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static String getTime() {
		return getTime(new Date());
	}

	public static String getTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(date);
	}

	public static Throwable getCause(Throwable throwable) {
		Throwable cause = throwable;
		while (cause != null && cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}

	public static String toString(Throwable t){
		StringWriter w=new StringWriter();
				
		t.printStackTrace(new PrintWriter(w));
		
		return w.toString();
	}
	
	public static boolean inEclipseIDE() {
		try {
			Class.forName("org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeProcessingEnvImpl");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static byte[] toByteArray(InputStream input) throws IOException {
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
 
	public static String[] fieldsToArrays(String... fields) {
		List<String> xs = new ArrayList<String>();
		for (String s : fields) {
			for (String x : s.split(",|;|\\|")) {
				if (x != null && x.trim().length() > 0) {
					xs.add(x.trim());
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
	
	public static String toDateString(long v, String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		
		return sdf.format(new Date(v));
	}

	public static Date toDate(Object v, String format, Date defaultValue) {
		if (v == null) {
			return defaultValue;
		} else {
			if (v instanceof Date) {
				return (Date) v;
			}else if (v.getClass()==Long.class || v.getClass()==long.class) {
				return new Date( (Long)v);
			} else {
				String x = "" + v;

				try {
					if (format != null && format.length() > 0) {
						SimpleDateFormat sdf = new SimpleDateFormat(format);
						return sdf.parse(x);
					} else {
						int m = x.indexOf(":");
						if (m > 0) {
							if (x.indexOf(":", m + 1) > 0) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								return sdf.parse(x);
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								return sdf.parse(x);
							}
						} else {
							if (x.indexOf(" ") > 0) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
								return sdf.parse(x);
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								return sdf.parse(x);
							}
						}
					}
				} catch (ParseException e) {
					throw new RuntimeException("Invalid date: " + x, e);
				}
			}
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

	public static byte charToByte(char c) {
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
		return bytesToHexString(src,null);
	}
	
	public static String bytesToHexString(byte[] src,String bytePrefix) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			
			if(bytePrefix!=null && bytePrefix.length()>0){
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

	public static void throwRuntimeException(Exception e) {
		if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else {
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, String> parseRemarks(String remark) {
		CaseInsensitiveMap<String> map = new CaseInsensitiveMap<String>();

		int len = remark.length();
		for (int i = 0; i < len; i++) {
			char c = remark.charAt(i);
			if (c == '#') {
				StringBuffer n = new StringBuffer();
				StringBuffer v = new StringBuffer();

				while (++i < len) {
					c = remark.charAt(i);

					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						n.append(c);
					} else if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '{') {
						if (c == '{') {
							while (++i < len) {
								c = remark.charAt(i);
								if (c == '}') {
									break;
								} else {
									v.append(c);
									if (c == '{') {
										while (++i < len) {
											c = remark.charAt(i);
											v.append(c);
											if (c == '}') {
												break;
											}
										}
									}
								}
							}
							break;
						} else {
							n.append(" ");
						}
					} else {
						n.delete(0, n.length());
						i--;
						break;
					}
				}

				String name = n.toString().trim();
				if (name.length() > 0) {
					map.put(name.toLowerCase(), v.toString().trim());
				}
			}
		}

		return map;

	}

	
	public static Class<?> forName(String className) throws ClassNotFoundException {
		try {
			return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {			 
		} catch (SecurityException e) {			 
		}
		 
		return Class.forName(className);
	}
	
	public static URL[] toURLs(String[] classPath){
		List<URL> urls = new ArrayList<URL>();
		try{ 
			for (String x : classPath) {
				File file = new File(x);
				if(file.exists()){
					urls.add(file.toURI().toURL());
				}
			}
			
			return urls.toArray(new URL[0]);
		}catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getPid() {
		String vmName = ManagementFactory.getRuntimeMXBean().getName();
		int p = vmName.indexOf('@');
		return vmName.substring(0, p);
	}

	public static boolean inDebug() {
		List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();

		for (String arg : args) {
			if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
				return true;
			}
		}
		return false;
	}
}
