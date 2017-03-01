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
package com.tsc9526.monalisa.tools.template.jsp;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JspContext {
	private Map<String, Object> context = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		return (T)context.get(name);
	}

	public String getParameter(String name) {
		return (String) getAttribute(name);
	}

	public JspContext setAttribute(String name, Object value) {
		context.put(name, value);
		return this;
	}

}
