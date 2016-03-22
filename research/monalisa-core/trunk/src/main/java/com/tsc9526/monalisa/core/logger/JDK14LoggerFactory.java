package com.tsc9526.monalisa.core.logger;

import java.util.logging.Level;

/**
 *
 * @author zzg.zhou(11039850@qq.com)
 */
public class JDK14LoggerFactory implements LoggerFactory {
	public Logger getLogger(String category) {
		return new JDK14Logger(java.util.logging.Logger.getLogger(category));
	}

	private static class JDK14Logger extends Logger {
		private final java.util.logging.Logger logger;

		JDK14Logger(java.util.logging.Logger logger) {
			this.logger = logger;
		}

		public void debug(String message) {
			logger.log(Level.FINE, message);
		}

		public void debug(String message, Throwable t) {
			logger.log(Level.FINE, message, t);
		}

		public void error(String message) {
			logger.log(Level.SEVERE, message);
		}

		public void error(String message, Throwable t) {
			logger.log(Level.SEVERE, message, t);
		}

		public void info(String message) {
			logger.log(Level.INFO, message);
		}

		public void info(String message, Throwable t) {
			logger.log(Level.INFO, message, t);
		}

		public void warn(String message) {
			logger.log(Level.WARNING, message);
		}

		public void warn(String message, Throwable t) {
			logger.log(Level.WARNING, message, t);
		}

		public boolean isDebugEnabled() {
			return logger.isLoggable(Level.FINE);
		}

		public boolean isInfoEnabled() {
			return logger.isLoggable(Level.INFO);
		}

		public boolean isWarnEnabled() {
			return logger.isLoggable(Level.WARNING);
		}

		public boolean isErrorEnabled() {
			return logger.isLoggable(Level.SEVERE);
		}

		public boolean isFatalEnabled() {
			return logger.isLoggable(Level.SEVERE);
		}
	}
}
