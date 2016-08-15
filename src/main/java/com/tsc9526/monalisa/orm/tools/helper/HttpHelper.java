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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class HttpHelper {
	public static interface DownloadListener{
		public void onConnected(URLConnection conn);
		public void onProgress(long receivedBytes,long totalBytes);
	}
	public static void download(String url, File target,DownloadListener listener) throws IOException {
		URL theUrl = new URL(url);

		URLConnection conn = theUrl.openConnection();
		
		setupHeaders(conn);
		setupConnection(conn);
		
		long totalBytes=conn.getContentLength();
		if(listener!=null){
			listener.onConnected(conn);
		}
		
		File tmp = File.createTempFile(target.getName(), ".tmp");

		InputStream from=conn.getInputStream();
		OutputStream to =new FileOutputStream(tmp);
		try{
			long receivedBytes=0;
			
			byte[] buf=new byte[512*1024];
			
			int len=from.read(buf);
			while(len>0){
				receivedBytes+=len;
				
				if(listener!=null){
					listener.onProgress(receivedBytes,totalBytes);
				}
				
				to.write(buf,0,len);
				 
				len=from.read(buf);
			}
		}finally{
			CloseQuietly.close(from,to);
		}
		 

		FileHelper.copy(tmp, target);
		tmp.delete();
	}

	private static void setupConnection(URLConnection conn) throws IOException {
		if (conn instanceof HttpsURLConnection) {
			HttpsURLConnection https = (HttpsURLConnection) conn;
			
			https.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			});
			
			try{
				TrustManager[] trustAllCerts = new TrustManager[1];
		        TrustManager tm = new HttpsX509TrustManager();
		        trustAllCerts[0] = tm;
		        
		        SSLContext sc = SSLContext.getInstance("SSL");
		        sc.init(null, trustAllCerts, null);
		        
				https.setSSLSocketFactory(sc.getSocketFactory());
			}catch(NoSuchAlgorithmException e) {
				 throw new RuntimeException(e);
			}catch(KeyManagementException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void setupHeaders(URLConnection con) {
		con.setRequestProperty("Accept", "*/*");
		con.setRequestProperty("Accept-Language", "zh-CN, zh");
		con.setRequestProperty("Charset", "UTF-8,ISO-8859-1,US-ASCII,ISO-10646-UCS-2;q=0.6");
		con.setRequestProperty(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
	}

	public static Map<String, String> getHttpResponseHeader(URLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;

			String str = http.getHeaderFieldKey(i);
			if (str != null) {
				header.put(str.toLowerCase(Locale.CHINA), mine);
			}
		}
		return header;
	}
 
 
	private static class HttpsX509TrustManager implements TrustManager, X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}
	}
}
