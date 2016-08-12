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
package com.tsc9526.monalisa.orm.tools.helper;

import com.tsc9526.monalisa.orm.datasource.C3p0DataSource;
import com.tsc9526.monalisa.orm.datasource.DruidDataSource;
import com.tsc9526.monalisa.orm.tools.agent.AgentEnhancer;
import com.tsc9526.monalisa.orm.tools.csv.Csv;
import com.tsc9526.monalisa.orm.tools.resources.PkgNames;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DynmicLibHelper {
	private static boolean init_libCglibClass  =false;
	
	private static boolean init_libCsvjdbcClass=false;
	private static boolean init_libC3p0Class   =false;
	private static boolean init_libDruidClass  =false;
	
	
	public static AgentEnhancer createAgentEnhancer(){
		if(!init_libCglibClass){
			JarLocationHelper.loadClass(PkgNames.libAsmClass);
			JarLocationHelper.loadClass(PkgNames.libCglibClass);
			
			init_libCglibClass=true;
		}
		
		return new AgentEnhancer();
	}
	
	public static Csv createCsv(){
		if(!init_libCsvjdbcClass){
			JarLocationHelper.loadClass(PkgNames.libCsvjdbcClass);
			init_libCsvjdbcClass=true;
		}
		
		return new Csv();
	}
	
	public static C3p0DataSource createC3p0DataSource(){
		if(!init_libC3p0Class){
			JarLocationHelper.loadClass(PkgNames.libC3p0Class);
			init_libC3p0Class=true;
		}
		
		return new C3p0DataSource();
	}
	
	public static DruidDataSource createDruidDataSource(){
		if(!init_libDruidClass){
			JarLocationHelper.loadClass(PkgNames.libDruidClass);
			init_libDruidClass=true;
		}
		
		return new DruidDataSource();
	}
}
