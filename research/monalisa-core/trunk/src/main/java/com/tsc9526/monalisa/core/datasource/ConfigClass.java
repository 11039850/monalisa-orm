package com.tsc9526.monalisa.core.datasource;

import java.util.Properties;

public abstract class ConfigClass {
	/**
	 * 配置项定义和configFile相同
	 */
	public abstract Properties getConfigProperties();
	
	public boolean isCfgChanged(){
		return false;
	}
}