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
package test.com.tsc9526.monalisa.service.servlet;

import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.actions.Action;
import com.tsc9526.monalisa.service.actions.ActionFilter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class LoggerActionFilter implements ActionFilter{
	
	public boolean accept(Action action) {
		return true;
	}

	public Response doFilter(Action action) {
		DbWebContainerInitializerTest.logger.info("doAction ...");
		
		return null;
	}
	
}