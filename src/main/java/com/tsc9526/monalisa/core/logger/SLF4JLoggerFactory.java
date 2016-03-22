package com.tsc9526.monalisa.core.logger;

import org.slf4j.spi.LocationAwareLogger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SLF4JLoggerFactory implements LoggerFactory {

	public Logger getLogger(String category) {
		org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(category);
		if (slf4jLogger instanceof LocationAwareLogger) {
			return new LocationAwareSLF4JLogger((LocationAwareLogger) slf4jLogger);
		} else {
			return new LocationUnawareSLF4JLogger(slf4jLogger);
		}
	}
	 
	private static final class LocationAwareSLF4JLogger extends Logger {

		private static final String FQCN = LocationAwareSLF4JLogger.class.getName();

		private final LocationAwareLogger logger;

		LocationAwareSLF4JLogger(LocationAwareLogger logger) {
			this.logger = logger;
		}

		public void debug(String message) {
			debug(message, null);
		}

		public void debug(String message, Throwable t) {
			logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, message, null, t);
		}

		public void info(String message) {
			info(message, null);
		}

		public void info(String message, Throwable t) {
			logger.log(null, FQCN, LocationAwareLogger.INFO_INT, message, null, t);
		}

		public void warn(String message) {
			warn(message, null);
		}

		public void warn(String message, Throwable t) {
			logger.log(null, FQCN, LocationAwareLogger.WARN_INT, message, null, t);
		}

		public void error(String message) {
			error(message, null);
		}

		public void error(String message, Throwable t) {
			logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, message, null, t);
		}

		public boolean isDebugEnabled() {
			return logger.isDebugEnabled();
		}

		public boolean isInfoEnabled() {
			return logger.isInfoEnabled();
		}

		public boolean isWarnEnabled() {
			return logger.isWarnEnabled();
		}

		public boolean isErrorEnabled() {
			return logger.isErrorEnabled();
		}

		public boolean isFatalEnabled() {
			return logger.isErrorEnabled();
		}

	}
	 
	private static class LocationUnawareSLF4JLogger extends Logger {

		private final org.slf4j.Logger logger;

		LocationUnawareSLF4JLogger(org.slf4j.Logger logger) {
			this.logger = logger;
		}

		public void debug(String message) {
			logger.debug(message);
		}

		public void debug(String message, Throwable t) {
			logger.debug(message, t);
		}

		public void info(String message) {
			logger.info(message);
		}

		public void info(String message, Throwable t) {
			logger.info(message, t);
		}

		public void warn(String message) {
			logger.warn(message);
		}

		public void warn(String message, Throwable t) {
			logger.warn(message, t);
		}

		public void error(String message) {
			logger.error(message);
		}

		public void error(String message, Throwable t) {
			logger.error(message, t);
		}

		public boolean isDebugEnabled() {
			return logger.isDebugEnabled();
		}

		public boolean isInfoEnabled() {
			return logger.isInfoEnabled();
		}

		public boolean isWarnEnabled() {
			return logger.isWarnEnabled();
		}

		public boolean isErrorEnabled() {
			return logger.isErrorEnabled();
		}

		public boolean isFatalEnabled() {
			return logger.isErrorEnabled();
		}

	}

}
