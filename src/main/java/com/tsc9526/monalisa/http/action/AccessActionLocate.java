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
package com.tsc9526.monalisa.http.action;

import com.tsc9526.monalisa.http.Response;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AccessActionLocate extends DefaultActionLocate{

	protected Response verify(ActionArgs args) {
		return null;
	} 
	
	
	protected Action onGetAction(ActionArgs args) {
		 
		return super.onGetAction(args);
	}

	 
	protected Action onDeleteAction(ActionArgs args) {
		 
		return super.onDeleteAction(args);
	}

	 
	protected Action onPutAction(ActionArgs args) {
		 
		return super.onPutAction(args);
	}

	 
	protected Action onPostAction(ActionArgs args) {
		 
		return super.onPostAction(args);
	}

}
