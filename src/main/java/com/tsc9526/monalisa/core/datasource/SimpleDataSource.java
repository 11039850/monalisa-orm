package com.tsc9526.monalisa.core.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SimpleDataSource implements PooledDataSource {
	private String url;
	private String driver;
	private String username;
	private String password;
	 
	public SimpleDataSource(DBConfig cfg) {
		setDriver(cfg.driver());
		setUrl(cfg.url());
		setUsername(cfg.username());
		setPassword(cfg.password());
	}

	 
	public Connection getConnection() throws SQLException {
		return getConnection(username, password);			
	}

	
	public Connection getConnection(String username, String password)throws SQLException {
		try {
			Class.forName(driver);
			
			return DriverManager.getConnection(url, username, password);	
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
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

	
	public void close() throws IOException {

	}

	
	public void setUrl(String url) {
		 this.url=url;

	}

	
	public void setDriver(String driver) {
		this.driver=driver;

	}

	
	public void setUsername(String username) {
		 this.username=username;

	}

	
	public void setPassword(String password) {
		this.password=password;

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