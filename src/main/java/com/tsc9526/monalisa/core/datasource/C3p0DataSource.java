package com.tsc9526.monalisa.core.datasource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class C3p0DataSource implements PooledDataSource {
	private com.mchange.v2.c3p0.ComboPooledDataSource cpds=new com.mchange.v2.c3p0.ComboPooledDataSource();
	
	public C3p0DataSource(){
		
	}
	
	public void setProperties(Properties properties){
		cpds.setProperties(properties);		 
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	 
	public Connection getConnection(String username, String password)
			throws SQLException {
	 
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

	
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {		 
		throw new SQLFeatureNotSupportedException("getParentLogger");
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {		 
		throw new SQLFeatureNotSupportedException("unwrap");
	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {		 
		throw new SQLFeatureNotSupportedException("isWrapperFor");
	}

	
	public void close() throws IOException {
		cpds.close();
		
	}

	
	public void setUrl(String url) {
		cpds.setJdbcUrl(url);
		
	}

	
	public void setDriver(String driver) {		
		try {
			cpds.setDriverClass(driver);
		} catch (PropertyVetoException e) {			 
			throw new RuntimeException(e);
		}		 
		
	}

	
	public void setUsername(String username) {
		cpds.setUser(username);
		
	}

	
	public void setPassword(String password) {
		cpds.setPassword(password);
		
	}

}
