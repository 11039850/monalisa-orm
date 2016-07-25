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
package com.tsc9526.monalisa.orm.tools.logger;

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
