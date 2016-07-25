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
package com.tsc9526.monalisa.orm.meta;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Keywords {	
	private static Set<String> javaKeywords=new HashSet<String>();
	
	
	static{
		String[] kws=new String[]{
				"public", "private", "protected",
				"default", "switch", "case",
				"for", "do", "goto", "const", "strictfp", "while", "if", "else",
				"byte", "short", "int", "long", "float", "double", "void", "boolean", "char", 
				"null", "false", "true",
				"continue", "break", "return", "instanceof",
				"synchronized", "volatile", "transient", "final", "static",
				"interface", "class", "extends", "implements", "throws",
				"throw", "catch", "try", "finally", "abstract", "assert",
				"enum", "import", "package", "native", "new", "super", "this"
		};
		for(String k:kws){
			javaKeywords.add(k);
		}
	} 	
	
	public static boolean isJavaKeyword(String word){
		return javaKeywords.contains(word);
	}
}
