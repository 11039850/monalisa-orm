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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Messager;

import com.tsc9526.monalisa.orm.resources.PkgNames;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Logger {
	public abstract void debug(String message);

	public abstract void debug(String message, Throwable t);

	public abstract void info(String message);

	public abstract void info(String message, Throwable t);

	public abstract void warn(String message);

	public abstract void warn(String message, Throwable t);

	public abstract void error(String message);

	public abstract void error(String message, Throwable t);

	public abstract boolean isDebugEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isWarnEnabled();

	public abstract boolean isErrorEnabled();

	public abstract boolean isFatalEnabled();
	
	
	public static final int  INDEX_AUTO      = -1;
	public static final int  INDEX_CONSOLE   =  0;
	
	private static final String[][] LIBRARY = {
		{ PkgNames.ORM_LOGGER_PKG+".Logger", "Console"},
		{ "java.util.logging.Logger",        "JDK14"},
		{ "org.apache.log4j.Logger",         "Log4J"}, 
		{ "org.apache.commons.logging.Log",  "Commons"},
		{ "org.slf4j.Logger",                "SLF4J"}		
	};
	
	private static int loggerIndex;
	private static LoggerFactory factory;
	private static String categoryPrefix = "";

	private static final Map<String,Logger> loggers = new HashMap<String,Logger>();

	public static void setMessager(Messager messager){
		ConsoleLoggerFactory.messager=messager;		 
	}
	
	public static void selectLoggerLibrary(int index) throws ClassNotFoundException {
		synchronized (Logger.class) {
			if (index < -1 || index >= LIBRARY.length) {
				throw new IllegalArgumentException();
			}
			loggerIndex = index;
			factory = createFactory();
		}
	}

	public static void setCategoryPrefix(String prefix) {
		synchronized (Logger.class) {
			if (prefix == null) {
				throw new IllegalArgumentException();
			}
			categoryPrefix = prefix;
		}
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	public static Logger getLogger(String category) {
		if (factory == null) {
			synchronized (Logger.class) {
				if (factory == null) {
					try {
						selectLoggerLibrary(INDEX_AUTO);
					} catch (ClassNotFoundException e) {						 
						throw new RuntimeException(e);
					}
				}
			}
		}

		category = categoryPrefix + category;

		synchronized (loggers) {
			Logger logger = (Logger) loggers.get(category);
			if (logger == null) {
				logger = factory.getLogger(category);
				logger=new LoggerWrapper(logger);
				loggers.put(category, logger);
			}
			return logger;
		}
	}

	private static LoggerFactory createFactory() throws ClassNotFoundException {
		if (loggerIndex == INDEX_AUTO) {
			if(factory!=null){
				return factory;
			}
			
			for (int i = LIBRARY.length - 1; i > 0; --i) {				 
				try {
					return createFactory(i);
				} catch (ClassNotFoundException e) {
				}
			}
			System.err.println("!!! WARNING: Monalisa logging suppressed!");
			return new ConsoleLoggerFactory();
		} else {
			return createFactory(loggerIndex);
		}
	}

	private static LoggerFactory createFactory(int index) throws ClassNotFoundException {
		String loggerClassName = LIBRARY[index][0];
		String factoryType     = LIBRARY[index][1];

		try {
			forName(loggerClassName);
			  
			return (LoggerFactory) forName(PkgNames.ORM_LOGGER_PKG+"." + factoryType + "LoggerFactory").newInstance();
		} catch (IllegalAccessException e) {			 
			throw new IllegalAccessError(e.getMessage());
		} catch (InstantiationException e) {			 
			throw new InstantiationError(e.getMessage());
		}
	} 
	
	private static Class<?> forName(String className)throws ClassNotFoundException{
		try{
			return Class.forName(className);
		}catch(ClassNotFoundException cnf){
			return Class.forName(className,true,Thread.currentThread().getContextClassLoader());
		}
	}
}
