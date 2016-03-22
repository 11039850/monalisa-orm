package com.tsc9526.monalisa.core.logger;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Messager;

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
		{ "com.tsc9526.monalisa.core.logger.Logger", "Console"},
		{ "java.util.logging.Logger",                "JDK14"},
		{ "org.apache.log4j.Logger",                 "Log4J"}, 
		{ "org.apache.commons.logging.Log",          "Commons"},
		{ "org.slf4j.Logger",                        "SLF4J"}		
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
				loggers.put(category, logger);
			}
			return logger;
		}
	}

	private static LoggerFactory createFactory() throws ClassNotFoundException {
		if (loggerIndex == INDEX_AUTO) {
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
			Class.forName(loggerClassName);
			 
			return (LoggerFactory) Class.forName("com.tsc9526.monalisa.core.logger." + factoryType + "LoggerFactory").newInstance();
		} catch (IllegalAccessException e) {			 
			throw new IllegalAccessError(e.getMessage());
		} catch (InstantiationException e) {			 
			throw new InstantiationError(e.getMessage());
		}
	}
}
