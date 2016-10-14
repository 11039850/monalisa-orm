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
package com.tsc9526.monalisa.http.server;

import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;

import com.tsc9526.monalisa.http.DBS;
import com.tsc9526.monalisa.http.servlet.MonalisaServlet;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MonalisaServer {
	 

	public static void main(String[] args)throws Exception{
		int port=9526;
		String pathSpec="/*";
		
		if(args.length>0){
			port=Integer.parseInt(args[0]);
		}
		
		if(args.length>1){
			pathSpec=args[1];
		}
		
		System.out.println("Starting monalisa server at port: "+port+", path: "+pathSpec);
		
		DBS.add("testdb",MysqlDB.DB); 
		
		Jetty jetty=Jetty.createJetty(port);
		jetty.addServletWithMapping(MonalisaServlet.class, pathSpec);
		jetty.start(true);
	}
}
