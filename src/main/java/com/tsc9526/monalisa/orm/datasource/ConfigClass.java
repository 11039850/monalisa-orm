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

import java.util.Properties;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class ConfigClass {
	/**
	 * @return 配置项定义和configFile相同
	 */
	public abstract Properties getConfigProperties();
	
	public boolean isCfgChanged(long lastestLoadTime){
		return false;
	}
	
	public String getKey(String key){
		return getKey(key,DBConfig.CFG_DEFAULT_NAME);
	}
	
	public String getPoolKey(String key){
		return getPoolKey(key,DBConfig.CFG_DEFAULT_NAME);
	}
	
	public String getKey(String key,String cfgName){
		return DBConfig.PREFIX_DB+"."+cfgName+"."+key;
	}
	
	public String getPoolKey(String key,String cfgName){
		return DBConfig.PREFIX_DB+"."+cfgName+".pool."+key;
	}
}