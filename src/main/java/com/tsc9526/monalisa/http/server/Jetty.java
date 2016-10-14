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
package com.tsc9526.monalisa.http.server;

import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.tsc9526.monalisa.orm.tools.helper.DynamicLibHelper;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Jetty {
	static Logger logger=Logger.getLogger(Jetty.class);
	
	public final static String libJettySevlet  = "org.eclipse.jetty.servlet.ServletHandler";
	public final static String libJettyServer  = "org.eclipse.jetty.server.Server";
	public final static String libJettyHttp    = "org.eclipse.jetty.http.HttpMethod";
	public final static String libJettyIo      = "org.eclipse.jetty.io.Connection";
	public final static String libJettyUtil    = "org.eclipse.jetty.util.log.Log";

	public final static String libJavaxServlet = "javax.servlet.ServletRequest";

	static {
		Map<String, String[]> hLibClasses = DynamicLibHelper.hLibClasses;

		hLibClasses.put(libJettySevlet, new String[] { "org.eclipse.jetty:jetty-servlet:9.3.6.v20151106" });
		hLibClasses.put(libJettyServer, new String[] { "org.eclipse.jetty:jetty-server:9.3.6.v20151106"  });
		hLibClasses.put(libJettyHttp,   new String[] { "org.eclipse.jetty:jetty-http:9.3.6.v20151106"    });
		hLibClasses.put(libJettyIo,     new String[] { "org.eclipse.jetty:jetty-io:9.3.6.v20151106"      });
		hLibClasses.put(libJettyUtil,   new String[] { "org.eclipse.jetty:jetty-util:9.3.6.v20151106"    });

		hLibClasses.put(libJavaxServlet,new String[] { "javax.servlet:javax.servlet-api:3.1.0"           });
	}

	private static boolean init_libJetty = false;

	public static void downloadJetty(){
		if (!init_libJetty) {
			DynamicLibHelper.loadClass(libJettySevlet);
			DynamicLibHelper.loadClass(libJettyServer);
			DynamicLibHelper.loadClass(libJettyHttp);
			DynamicLibHelper.loadClass(libJettyIo);
			DynamicLibHelper.loadClass(libJettyUtil);

			DynamicLibHelper.loadClass(libJavaxServlet);

			init_libJetty = true;
		}
	}
	
	public static Jetty createJetty(int port) {
		downloadJetty();
		
		Jetty jetty=new Jetty(port);
		return jetty;
	}

	private Server server;
	private ServletHandler servletHandler=new ServletHandler();
	 
	private Jetty(int port) {
		server = new Server(port);
		server.setHandler(servletHandler);
	}
	
	public ServletHandler getServletHandler(){
		return servletHandler;
	}
	
	public void addServletWithMapping(Class<? extends Servlet> servlet,String pathSpec){
		servletHandler.addServletWithMapping(servlet, pathSpec);
	}
	
	/**
	 * Start web server
	 * 
	 * @param block if true: make the current thread join and wait until the server is done executing.
	 * @throws Exception
	 */
	public void start(boolean block)throws Exception{
		try{
			server.start();
	
			if(block){
				server.join();
			}
		}catch(Exception e){
			logger.error(""+e,e);
			
			server.stop();
		}
	}
	
	public void stop()throws Exception{
		server.stop();
	}
}
