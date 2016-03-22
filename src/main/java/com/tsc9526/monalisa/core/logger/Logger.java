package com.tsc9526.monalisa.core.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public class Logger{
	private static Messager messager = null;

	public static void setMessager(Messager messager) {
		Logger.messager = messager;
	}
	
	public static Logger getLogger(Class<?> clazz){
		return new Logger(clazz.getSimpleName());
	}
	
	public static Logger getLogger(String name){
		return new Logger(name);
	}

	private Logger(String name) {
		this.name=name;
	}
	
	
	protected String name;
	 
 
	public void debug(Object message) {
		println("DEBUG", message);
	}

	public void debug(Object message, Throwable t) {
		println("DEBUG", message, t);
	}

	public void error(Object message) {
		println("ERROR", message);
	}

	public void error(Object message, Throwable t) {
		println("ERROR", message, t);
	}

	public void info(Object message) {
		println("INFO", message);
	}

	public void info(Object message, Throwable t) {
		println("INFO", message, t);
	}

	public void warn(Object message) {
		println("WARN", message);
	}

	public void warn(Object message, Throwable t) {
		println("WARN", message, t);
	}

	public void trace(Object message) {
		println("TRACE", message);
	}

	public void trace(Object message, Throwable t) {
		println("TRACE", message, t);
	}

	public void fatal(Object message) {
		println("FATAL", message);
	}

	public void fatal(Object message, Throwable t) {
		println("FATAL", message, t);
	}

	public boolean isDebugEnabled() {
		return false;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return false;
	}

	private void println(String level, Object message) {
		println(level, message, null);
	}

	private void println(String level, Object message, Throwable t) {
		StringBuffer sb = new StringBuffer();
		sb.append("["+name+"] ");
		sb.append("[").append(level).append("] ").append(message);

		if (t != null) {
			StringWriter writer = new StringWriter(4 * 1024);
			t.printStackTrace(new PrintWriter(writer));
			sb.append("\r\n").append(writer.getBuffer().toString());
		}

		if (messager != null) {
			if (isError(level)) {
				messager.printMessage(Kind.ERROR, sb.toString());
			} else {
				messager.printMessage(Kind.NOTE, sb.toString());
			}
		} else {
			if (isError(level)) {
				System.err.println(sb.toString());
			} else {
				System.out.println(sb.toString());
			}
		}
	}

	private boolean isError(String level) {
		return level.equals("ERROR") || level.equals("FATAL");
	}
}
