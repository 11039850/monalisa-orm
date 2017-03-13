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

import org.slf4j.spi.LocationAwareLogger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class SLF4JLoggerFactory implements LoggerFactory {
	static{
		Log4jCfg.initLog4jConfiguration();
	}
	
	public Logger getLogger(String category) {
		org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(category);
		
		try{
			Class.forName("org.slf4j.spi.LocationAwareLogger");
			
			if (slf4jLogger instanceof LocationAwareLogger) {
				return new LocationAwareSLF4JLogger((LocationAwareLogger) slf4jLogger);
			} else {
				return new LocationUnawareSLF4JLogger(slf4jLogger);
			}
		}catch(ClassNotFoundException e){
			return new LocationUnawareSLF4JLogger(slf4jLogger);
		}catch(NoClassDefFoundError e){
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
