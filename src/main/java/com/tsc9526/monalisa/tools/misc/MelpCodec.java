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
package com.tsc9526.monalisa.tools.misc;

import java.security.MessageDigest;

import com.tsc9526.monalisa.tools.misc.MelpException;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpCodec {

	/**
	 * 
	 * @param source bytes to UTF-8
	 * 
	 * @return the MD5 value, lower case hex string 
	 */
	public static String MD5(String source){
		try {
			return MD5(source.getBytes("utf-8"));
		} catch (Exception e) {
			return MelpException.throwRuntimeException(e);
		}
	}

	/**
	 * @param bytes bytes to md5
	 * @return the MD5 value, lower case hex string 
	 */
	public static String MD5(byte[] bytes){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			
			byte[] md5Bytes = md5.digest(bytes);
			
			return MelpString.bytesToHexString(md5Bytes).toLowerCase();
		} catch (Exception e) {
			return MelpException.throwRuntimeException(e);
		}
	}
}
