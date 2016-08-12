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
package com.tsc9526.monalisa.orm.tools.resources;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PkgNames {
	public final static String ORM_ANNOTATION_DB = "com.tsc9526.monalisa.orm.annotation.DB";
	
	
	public final static String ORM_DATASOURCE    = "com.tsc9526.monalisa.orm.datasource";
	
	public final static String ORM_LOGGER_PKG    = "com.tsc9526.monalisa.orm.tools.logger";
	public final static String ORM_JSONHELPER    = "com.tsc9526.monalisa.orm.tools.helper.JsonHelper";
	public final static String ORM_AGENTCLASS    = "com.tsc9526.monalisa.orm.tools.agent.AgentClass";
	public final static String ORM_AgentHotSpotVM= "com.tsc9526.monalisa.orm.tools.agent.AgentHotSpotVM";
	
	public final static String ORM_DS_C3p0       = "com.tsc9526.monalisa.orm.datasource.C3p0DataSource";
	public final static String ORM_DS_Durid      = "com.tsc9526.monalisa.orm.datasource.DruidDataSource";
	
	public final static String libVirtualMachineClass = "com.sun.tools.attach.VirtualMachine";
	
	public final static String libCglibClass     = "net.sf.cglib.proxy.Enhancer";
	public final static String libAsmClass       = "org.objectweb.asm.ClassWriter";
	public final static String libC3p0Class      = "com.mchange.v2.c3p0.ComboPooledDataSource";
	public final static String libDruidClass     = "com.alibaba.druid.pool.DruidDataSource";
	public final static String libMysqlClass     = "com.mysql.jdbc.Driver";
	public final static String libCsvjdbcClass   = "org.relique.jdbc.csv.CsvDriver";
	 
 	public static Map<String, String[]> hLibClasses=new LinkedHashMap<String, String[]>(){
 		private static final long serialVersionUID = 1L;
		{
 			put(libCglibClass,   new String[]{"cglib:cglib:3.2.0"});
 			put(libAsmClass,     new String[]{"org.ow2.asm:asm:5.0.3"});
 			put(libC3p0Class,    new String[]{"c3p0:c3p0:0.9.1.2"});
 			put(libDruidClass,   new String[]{"com.alibaba.druid:druid-wrapper:0.2.9"});
 			put(libMysqlClass,   new String[]{"mysql:mysql-connector-java:5.1.24"});
 			put(libCsvjdbcClass, new String[]{"net.sourceforge.csvjdbc:csvjdbc:1.0.28"});
 		}
 	};
}
