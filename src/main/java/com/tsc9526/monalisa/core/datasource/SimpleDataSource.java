package com.tsc9526.monalisa.core.datasource;

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
import java.util.logging.Logger;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class SimpleDataSource implements PooledDataSource {
	private String url;
	private String driver;
	private String username;
	private String password;

	private static final ConcurrentMap<Connection, Date> pool = new ConcurrentHashMap<Connection, Date>();
	private int maxSize;
	private int minSize;
	private Semaphore semaphore;

	public SimpleDataSource(DBConfig db) {
		setDriver(db.getCfg().getDriver());
		setUrl(db.getCfg().getUrl());
		setUsername(db.getCfg().getUsername());
		setPassword(db.getCfg().getPassword());
		
		initConnections(db);
	}
	
	private void initConnections(DBConfig db){
		maxSize = db.getCfg().getProperty("pool.max", 20);
		minSize = db.getCfg().getProperty("pool.min", 1);
		
		if(maxSize<1){
			maxSize=1;
		}		
		
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
		try {
			synchronized (pool) {
				if (pool.size() < maxSize) {
					pool.put(realConnection, new Date());
					return;
				}
			}
			realConnection.close();
		} finally {
			semaphore.release();
		}
	}

	public Connection getConnection() throws SQLException {
		return getConnection(username, password);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		try {
			semaphore.acquire();
		}catch (InterruptedException e) {			
		}

		synchronized (pool) {
			if (!pool.isEmpty()) {
				Connection realConn = pool.keySet().iterator().next();
				pool.remove(realConn);
				
				try {
					realConn.setAutoCommit(true);
				} catch (SQLException e) {
					pool.clear();
					
					realConn = getRealConnection(username,password);
				}
				return getProxyConnection(realConn);
			}
		}

		return getProxyConnection(getRealConnection(username, password));

	}
	
	private Connection getProxyConnection(final Connection realConnection) {		
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] params) throws Exception {
				Object ret = null;
				if ("close".equals(method.getName())) {
					closeConnection(realConnection);
				} else {
					ret = method.invoke(realConnection, params);
				}
				return ret;
			}
		};
		return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
	}

	

	private Connection getRealConnection(String username, String password) throws SQLException {
		try {
			Class.forName(driver);

			return DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void setProperties(Properties properties){
		
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

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
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

}
