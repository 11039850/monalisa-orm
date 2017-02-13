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

import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.generator.DBGenerator;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class LoggerWrapper extends Logger{
	private Logger logger;
	
	public LoggerWrapper(Logger logger){
		this.logger=logger;
	}
	
	private Logger getLogger() {
		if( DbProp.ProcessingEnvironment && (DBGenerator.plogger instanceof LoggerWrapper ==false)){
			return DBGenerator.plogger;
		}else{
			return this.logger;
		}
	}
	
	public void debug(String message) {
		getLogger().debug(message);
	}

	public void debug(String message, Throwable t) {
		getLogger().debug(message, t);
	}

	public void info(String message) {
		getLogger().info(message);
	}

	public void info(String message, Throwable t) {
		getLogger().info(message, t);
	}

	public void warn(String message) {
		getLogger().warn(message);
	}

	public int hashCode() {
		return getLogger().hashCode();
	}

	public void warn(String message, Throwable t) {
		getLogger().warn(message, t);
	}

	public void error(String message) {
		getLogger().error(message);
	}

	public void error(String message, Throwable t) {
		getLogger().error(message, t);
	}

	public boolean isDebugEnabled() {
		return getLogger().isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return getLogger().isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return getLogger().isWarnEnabled();
	}

	public boolean isErrorEnabled() {
		return getLogger().isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return getLogger().isFatalEnabled();
	}
	 
}
