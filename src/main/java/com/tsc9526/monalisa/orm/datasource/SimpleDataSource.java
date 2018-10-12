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
package com.tsc9526.monalisa.orm.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpLib;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class SimpleDataSource implements PooledDataSource {	 
	static Logger logger=Logger.getLogger(SimpleDataSource.class);
	
	private ConcurrentMap<Connection, Date> pool = new ConcurrentHashMap<Connection, Date>();
	
	private String url;
	private String driver;
	private String username;
	private String password;
 	
	private int maxSize;
	private int minSize;
	private int waitTime;
	
	private Semaphore semaphore;

	private Properties connProps=new Properties();
	
	public SimpleDataSource(DBConfig db) {
		this(db, null);
	}
	
	public SimpleDataSource(DBConfig db,Properties poolProperties) {
		String driver=db.getDialect().getDriver();
		String v=db.getCfg().getDriver();
		if(v!=null && v.length()>1){
			driver=v;
		}
		
		setDriver(driver);
		setUrl(db.getCfg().getUrl());
		setUsername(db.getCfg().getUsername());
		setPassword(db.getCfg().getPassword());
		 
		connProps.put("user",getUsername());     
		connProps.put("password",getPassword());     
		 
		if(poolProperties!=null){
			connProps.putAll(poolProperties);
		}

		maxSize  = db.getCfg().getProperty("pool.max", 50);
		minSize  = db.getCfg().getProperty("pool.min", 3);
		waitTime = db.getCfg().getProperty("pool.waitTime", 5);
		
		MelpLib.loadClass(driver);
		
		initConnections(db);
	}
	
	private void initConnections(DBConfig db){
		logger.info("Initializing simple data source{ pool.max = "+maxSize+", pool.min = "+minSize+", jdbcUrl = "+db.getCfg().getUrl()+", username = "+ db.getCfg().getUsername() +"}");
		
		semaphore = new Semaphore(maxSize, false);
		
		if(minSize>0 && minSize < maxSize){
			try{
				List<Connection> connections=new ArrayList<Connection>();
				for(int i=0;i<minSize;i++){
					connections.add(getConnection());
				}
				for(Connection conn:connections){
					conn.close();
				}
			}catch(SQLException e){
				throw new RuntimeException(e);
			}
		}
	}

	public void close() throws IOException {
		try {
			for (Connection conn : pool.keySet()) {
				conn.close();
			}
			pool.clear();
		 	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	private void closeConnection(Connection realConnection) throws SQLException {
		synchronized (pool) {
			if (pool.size() <= maxSize) {
				pool.put(realConnection, new Date());
				return;
			}
		}
			
		try {
			realConnection.close();
		} finally {
			semaphore.release();
		}
	}

	public Connection getConnection() throws SQLException {
		return getConnection(username, password);
	}

	public Connection getConnection(String username, String password) throws SQLException {		 
		synchronized (pool) {
			if (!pool.isEmpty()) {
				Connection realConn = pool.keySet().iterator().next();
				pool.remove(realConn);
				  
				realConn.setAutoCommit(true);
				 
				return getProxyConnection(realConn);
			}
		}
		 	
		try {
			if(semaphore.tryAcquire(waitTime,TimeUnit.SECONDS)) {
				return getProxyConnection(getRealConnection(username, password));
			}else {
				throw new RuntimeException("Connection pool is full: "+maxSize);
			}
		}catch(SQLException e) {
			semaphore.release();
			throw e;
		} catch (InterruptedException e) {
			throw new RuntimeException(e); 
		}
	}
	
	private Connection getProxyConnection(final Connection realConnection) {		
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] params) throws Exception {
				Object ret = null;
				if ("close".equals(method.getName())) {
					closeConnection(realConnection);
				}else if ("unwrap".equals(method.getName())) {
					ret=realConnection;
				} else {
					ret = method.invoke(realConnection, params);
				}
				return ret;
			}
		};
		return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
	}

	

	protected Connection getRealConnection(String username, String password) throws SQLException {
		try {
			MelpClass.forName(driver);
			
			this.connProps.put ("user",username);
			this.connProps.put ("password",password);
			
			return DriverManager.getConnection(url, connProps);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void setProperties(Properties properties){
		this.connProps.putAll(properties);
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public void setUrl(String url) {
		this.url = url;

	}

	public void setDriver(String driver) {
		this.driver = driver;

	}

	public void setUsername(String username) {
		this.username = username;

	}

	public void setPassword(String password) {
		this.password = password;

	}

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setIdleValidationQuery(int idleInSeconds,String validationQuery){
		//do noting
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public Properties getConnProps() {
		return connProps;
	}
}
