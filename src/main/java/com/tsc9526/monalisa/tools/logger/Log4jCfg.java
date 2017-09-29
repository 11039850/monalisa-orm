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
package com.tsc9526.monalisa.tools.logger;

import java.net.URL;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Log4jCfg {
	private Log4jCfg(){}
	
	public static void initLog4jConfiguration(){
		try{
			Class.forName("org.apache.log4j.Logger");
			
			boolean cfg=false;
			if(System.getProperty("log4j.configuration")!=null){
				cfg=true;
			}
			
			if(!cfg){
				ClassLoader loader=LoggerFactory.class.getClassLoader();
				if(loader.getResource("/log4j.xml")!=null ||  loader.getResource("/log4j.properties")!=null){
					cfg=true;
				}
			}
			
			if(!cfg){
				URL log4jCfg=LoggerFactory.class.getResource("/logger/log4j.properties");
				
				Thread.currentThread().setContextClassLoader(LoggerFactory.class.getClassLoader());
				
				org.apache.log4j.PropertyConfigurator.configure(log4jCfg);
				
			}
		}catch(Exception e){
			ConsoleLoggerFactory.LOGGER.trace(e);
		}
	}
}
