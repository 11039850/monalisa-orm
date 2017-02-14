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
package com.tsc9526.monalisa.tools.clazz;



/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PkgNames {
	public final static String ORM_PACKAGE       = "com.tsc9526.monalisa.orm";
	
	public final static String ORM_ANNOTATION_DB = ORM_PACKAGE+".annotation.DB";
	public final static String ORM_DATASOURCE    = ORM_PACKAGE+".datasource";
	public final static String ORM_DS_C3p0       = ORM_PACKAGE+".datasource.C3p0DataSource";
	public final static String ORM_DS_Durid      = ORM_PACKAGE+".datasource.DruidDataSource";
	
	
	public final static String ORM_TOOLS         = "com.tsc9526.monalisa.tools";
	
	public final static String ORM_LOGGER_PKG    = ORM_TOOLS+".logger";
	public final static String ORM_JSONHELPER    = ORM_TOOLS+".string.MelpJson";
	public final static String ORM_LOADERCLASS   = ORM_TOOLS+".clazz.MelpLib"; 
	public final static String ORM_AGENTCLASS    = ORM_TOOLS+".agent.AgentClass";
	public final static String ORM_AgentHotSpotVM= ORM_TOOLS+".agent.AgentHotSpotVM";
	 
	 
	public final static String libVirtualMachineClass = "com.sun.tools.attach.VirtualMachine";
	
}
