package com.tsc9526.monalisa.core.datasource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class C3p0DataSource implements PooledDataSource {
	private com.mchange.v2.c3p0.ComboPooledDataSource cpds=new com.mchange.v2.c3p0.ComboPooledDataSource();
	
	public C3p0DataSource(){
		
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		 
		return cpds.getConnection(username, password);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return cpds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		cpds.setLogWriter(out);		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		cpds.setLoginTimeout(seconds);
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {		 
		return cpds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {		 
		throw new SQLFeatureNotSupportedException("getParentLogger");
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {		 
		throw new SQLFeatureNotSupportedException("unwrap");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {		 
		throw new SQLFeatureNotSupportedException("isWrapperFor");
	}

	@Override
	public void close() throws IOException {
		cpds.close();
		
	}

	@Override
	public void setUrl(String url) {
		cpds.setJdbcUrl(url);
		
	}

	@Override
	public void setDriver(String driver) {		
		try {
			cpds.setDriverClass(driver);
		} catch (PropertyVetoException e) {			 
			throw new RuntimeException(e);
		}		 
		
	}

	@Override
	public void setUsername(String username) {
		cpds.setUser(username);
		
	}

	@Override
	public void setPassword(String password) {
		cpds.setPassword(password);
		
	}

}
