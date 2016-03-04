package com.tsc9526.monalisa.core.logger;

public class Logger extends MessagerLogger{
	public static Logger getLogger(Class<?> clazz){
		return new Logger(clazz.getSimpleName());
	}
	
	public static Logger getLogger(String name){
		return new Logger(name);
	}

	private Logger(String name) {
		super(name);
	}
	
}
