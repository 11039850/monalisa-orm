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
package com.tsc9526.monalisa.core.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tsc9526.monalisa.core.tools.ClassHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DruidDataSource implements PooledDataSource {
	private com.alibaba.druid.pool.DruidDataSource cpds = new com.alibaba.druid.pool.DruidDataSource();

	private Map<String, Method> hPropertyMethods = new HashMap<String, Method>();

	public DruidDataSource() {
		for (Method m : cpds.getClass().getMethods()) {
			Class<?>[] ps = m.getParameterTypes();
			if (ps != null && ps.length == 1) {
				hPropertyMethods.put(m.getName(), m);
			}
		}
	}

	public void setProperties(Properties properties) {
		try {
			for (Object key : properties.keySet()) {
				String name = key.toString();
				String set = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
				Method m = hPropertyMethods.get(set);
				if (m != null) {
					Object v = properties.get(key);
					v = ClassHelper.convert(v, m.getParameterTypes()[0]);
					if (v != null) {
						m.invoke(cpds, v);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setIdleValidationQuery(int idleInSeconds,String validationQuery){
		cpds.setTestWhileIdle(true);
		cpds.setTimeBetweenEvictionRunsMillis(idleInSeconds*1000);
		cpds.setValidationQuery(validationQuery);
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return cpds.getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return cpds.getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		cpds.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		cpds.setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return cpds.getLoginTimeout();
	}

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return cpds.getParentLogger();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return cpds.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return cpds.isWrapperFor(iface);
	}

	public void close() throws IOException {
		cpds.close();
	}

	public void setUrl(String url) {
		cpds.setUrl(url);
	}

	public void setDriver(String driver) {
		cpds.setDriverClassName(driver);
	}

	public void setUsername(String username) {
		cpds.setUsername(username);
	}

	public void setPassword(String password) {
		cpds.setPassword(password);
	}

}
