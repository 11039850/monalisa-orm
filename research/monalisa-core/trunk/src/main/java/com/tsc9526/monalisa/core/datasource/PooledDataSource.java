package com.tsc9526.monalisa.core.datasource;

import java.io.Closeable;
import java.util.Properties;

import javax.sql.DataSource;

public interface PooledDataSource extends DataSource,Closeable{

	public void setUrl     (String url);
	public void setDriver  (String driver);
	public void setUsername(String username);
	public void setPassword(String password);
	
	public void setProperties(Properties properties);
	
}
