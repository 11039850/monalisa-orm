package com.tsc9526.monalisa.core.datasource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.tsc9526.monalisa.core.tools.ClassHelper;

/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
public class C3p0DataSource implements PooledDataSource {
	private com.mchange.v2.c3p0.ComboPooledDataSource cpds=new com.mchange.v2.c3p0.ComboPooledDataSource();
	
	private Map<String,Method> hPropertyMethods=new HashMap<String,Method>();
	
	public C3p0DataSource(){
		for(Method m:cpds.getClass().getMethods()){
			if(m.getParameterCount()==1){
				hPropertyMethods.put(m.getName(),m);
			}
		}
	}
	
	public void setProperties(Properties properties){
		try{
			for(Object key:properties.keySet()){
				String name=key.toString();
				String set="set"+name.substring(0,1).toUpperCase()+name.substring(1);
				Method m=hPropertyMethods.get(set);
				if(m!=null){
					Object v=properties.get(key);
					v=ClassHelper.convert(v, m.getParameterTypes()[0]);
					if(v!=null){
						m.invoke(cpds, v);
					}
				} 
			}	 
		}catch(Exception e){
			throw new RuntimeException(e);
		}
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
