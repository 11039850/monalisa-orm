package com.tsc9526.monalisa.core.logger;

import org.apache.log4j.Level;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Log4JLoggerFactory implements LoggerFactory {
	public Logger getLogger(String category) {
		return new Log4JLogger(org.apache.log4j.Logger.getLogger(category));
	}

	private static class Log4JLogger extends Logger {
		private final org.apache.log4j.Logger logger;

		Log4JLogger(org.apache.log4j.Logger logger) {
			this.logger = logger;
		}

		public void debug(String message) {
			logger.debug(message);
		}

		public void debug(String message, Throwable t) {
			logger.debug(message, t);
		}

		public void error(String message) {
			logger.error(message);
		}

		public void error(String message, Throwable t) {
			logger.error(message, t);
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

		public boolean isDebugEnabled() {
			return logger.isDebugEnabled();
		}

		public boolean isInfoEnabled() {
			return logger.isInfoEnabled();
		}

		public boolean isWarnEnabled() {
			return logger.isEnabledFor(Level.WARN);
		}

		public boolean isErrorEnabled() {
			return logger.isEnabledFor(Level.ERROR);
		}

		public boolean isFatalEnabled() {
			return logger.isEnabledFor(Level.FATAL);
		}
	}
}
