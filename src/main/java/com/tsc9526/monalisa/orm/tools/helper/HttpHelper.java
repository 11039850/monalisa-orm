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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class HttpHelper {

	public static void download(String url, File target) throws Exception {
		URL theUrl = new URL(url);

		URLConnection conn = theUrl.openConnection();
		setupHeaders(conn);

		File tmp = File.createTempFile(target.getName(), ".tmp");

		FileHelper.write(getStream(conn), new FileOutputStream(tmp));
		 
		FileHelper.copy(tmp, target);
		tmp.delete();
	}
 

	private static InputStream getStream(URLConnection conn) throws Exception {
		if (conn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

			TrustManager[] tm = { new HttpsX509TrustManager() };

			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();
			httpsConn.setSSLSocketFactory(ssf);
		}

		return conn.getInputStream();
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

	private static class HttpsX509TrustManager implements X509TrustManager {
		X509TrustManager sunJSSEX509TrustManager;

		HttpsX509TrustManager() throws Exception {

			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("trustedCerts"), "passphrase".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
			tmf.init(ks);
			TrustManager tms[] = tmf.getTrustManagers();

			for (int i = 0; i < tms.length; i++) {
				if (tms[i] instanceof X509TrustManager) {
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];
					return;
				}
			}
		 
			throw new Exception("Couldn't initialize");
		}

		 
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
			} catch (CertificateException excep) {
				 
			}
		}

		 
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
			} catch (CertificateException excep) {
			}
		}
 
		public X509Certificate[] getAcceptedIssuers() {
			return sunJSSEX509TrustManager.getAcceptedIssuers();
		}
	}
}
