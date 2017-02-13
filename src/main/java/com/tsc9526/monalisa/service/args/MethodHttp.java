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
package com.tsc9526.monalisa.service.args;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public enum MethodHttp {
	/**
	 * SQL: SELECT 
	 */
	GET,
	
	/**
	 * SQL: DELETE
	 */
	DELETE,
	
	/** 
	 * SQL: INSERT OR UPDATE
	 */
	POST,
	
	/**
	 * SQL: UPDATE
	 */
	PUT,
	
	/**
	 * SQL: DESCRIBE TABLE, SHOW DATABASE ...
	 */
	HEAD;
	
	public MethodSQL toSQLMethod(ModelArgs args){
		switch(this){
			case GET:    return MethodSQL.SELECT;
			case DELETE: return MethodSQL.DELETE;
			case PUT:    return MethodSQL.UPDATE;
			case POST:   
						 if(args.getTables()!=null){
							 return MethodSQL.INSERT;
						 }else if(args.getTable()!=null){
							 if(args.getSinglePK()!=null || args.getMultiKeys()!=null){
								 return MethodSQL.INSERT;
							 }
						 }
						 return MethodSQL.UPDATE;
						 
			case HEAD:	 return MethodSQL.DESCRIBE;
			default:     throw new IllegalArgumentException(this.name());
		}
	}
}
