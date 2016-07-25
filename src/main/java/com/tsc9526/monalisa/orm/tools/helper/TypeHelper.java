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

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TypeHelper {

	private static Map<Integer, String> typeToName;
	private static Map<String, Integer> nameToType;
	private static Map<Integer, String> typeToJava;
	private static Set<String> javaKeywords=new HashSet<String>();
	
	static {
		typeToName = new HashMap<Integer, String>();
		typeToName.put(Types.ARRAY, "ARRAY"); //$NON-NLS-1$
		typeToName.put(Types.BIGINT, "BIGINT"); //$NON-NLS-1$
		typeToName.put(Types.BINARY, "BINARY"); //$NON-NLS-1$
		typeToName.put(Types.BIT, "BIT"); //$NON-NLS-1$
		typeToName.put(Types.BLOB, "BLOB"); //$NON-NLS-1$
		typeToName.put(Types.BOOLEAN, "BOOLEAN"); //$NON-NLS-1$
		typeToName.put(Types.CHAR, "CHAR"); //$NON-NLS-1$
		typeToName.put(Types.CLOB, "CLOB"); //$NON-NLS-1$
		typeToName.put(Types.DATALINK, "DATALINK"); //$NON-NLS-1$
		typeToName.put(Types.DATE, "DATE"); //$NON-NLS-1$
		typeToName.put(Types.DECIMAL, "DECIMAL"); //$NON-NLS-1$
		typeToName.put(Types.DISTINCT, "DISTINCT"); //$NON-NLS-1$
		typeToName.put(Types.DOUBLE, "DOUBLE"); //$NON-NLS-1$
		typeToName.put(Types.FLOAT, "FLOAT"); //$NON-NLS-1$
		typeToName.put(Types.INTEGER, "INTEGER"); //$NON-NLS-1$
		typeToName.put(Types.JAVA_OBJECT, "JAVA_OBJECT"); //$NON-NLS-1$
		typeToName.put(Types.LONGVARBINARY, "LONGVARBINARY"); //$NON-NLS-1$
		typeToName.put(Types.LONGVARCHAR, "LONGVARCHAR"); //$NON-NLS-1$
		typeToName.put(Types.NCHAR, "NCHAR"); //$NON-NLS-1$
		typeToName.put(Types.NCLOB, "NCLOB"); //$NON-NLS-1$
		typeToName.put(Types.NVARCHAR, "NVARCHAR"); //$NON-NLS-1$
		typeToName.put(Types.LONGNVARCHAR, "LONGNVARCHAR"); //$NON-NLS-1$
		typeToName.put(Types.NULL, "NULL"); //$NON-NLS-1$
		typeToName.put(Types.NUMERIC, "NUMERIC"); //$NON-NLS-1$
		typeToName.put(Types.OTHER, "OTHER"); //$NON-NLS-1$
		typeToName.put(Types.REAL, "REAL"); //$NON-NLS-1$
		typeToName.put(Types.REF, "REF"); //$NON-NLS-1$
		typeToName.put(Types.SMALLINT, "SMALLINT"); //$NON-NLS-1$
		typeToName.put(Types.STRUCT, "STRUCT"); //$NON-NLS-1$
		typeToName.put(Types.TIME, "TIME"); //$NON-NLS-1$
		typeToName.put(Types.TIMESTAMP, "TIMESTAMP"); //$NON-NLS-1$
		typeToName.put(Types.TINYINT, "TINYINT"); //$NON-NLS-1$
		typeToName.put(Types.VARBINARY, "VARBINARY"); //$NON-NLS-1$
		typeToName.put(Types.VARCHAR, "VARCHAR"); //$NON-NLS-1$

		nameToType = new HashMap<String, Integer>();
		nameToType.put("ARRAY", Types.ARRAY); //$NON-NLS-1$
		nameToType.put("BIGINT", Types.BIGINT); //$NON-NLS-1$
		nameToType.put("BINARY", Types.BINARY); //$NON-NLS-1$
		nameToType.put("BIT", Types.BIT); //$NON-NLS-1$
		nameToType.put("BLOB", Types.BLOB); //$NON-NLS-1$
		nameToType.put("BOOLEAN", Types.BOOLEAN); //$NON-NLS-1$
		nameToType.put("CHAR", Types.CHAR); //$NON-NLS-1$
		nameToType.put("CLOB", Types.CLOB); //$NON-NLS-1$
		nameToType.put("DATALINK", Types.DATALINK); //$NON-NLS-1$
		nameToType.put("DATE", Types.DATE); //$NON-NLS-1$
		nameToType.put("DECIMAL", Types.DECIMAL); //$NON-NLS-1$
		nameToType.put("DISTINCT", Types.DISTINCT); //$NON-NLS-1$
		nameToType.put("DOUBLE", Types.DOUBLE); //$NON-NLS-1$
		nameToType.put("FLOAT", Types.FLOAT); //$NON-NLS-1$
		nameToType.put("INTEGER", Types.INTEGER); //$NON-NLS-1$
		nameToType.put("JAVA_OBJECT", Types.JAVA_OBJECT); //$NON-NLS-1$
		nameToType.put("LONGVARBINARY", Types.LONGVARBINARY); //$NON-NLS-1$
		nameToType.put("LONGVARCHAR", Types.LONGVARCHAR); //$NON-NLS-1$
		nameToType.put("NCHAR", Types.NCHAR); //$NON-NLS-1$
		nameToType.put("NCLOB", Types.NCLOB); //$NON-NLS-1$
		nameToType.put("NVARCHAR", Types.NVARCHAR); //$NON-NLS-1$
		nameToType.put("LONGNVARCHAR", Types.LONGNVARCHAR); //$NON-NLS-1$
		nameToType.put("NULL", Types.NULL); //$NON-NLS-1$
		nameToType.put("NUMERIC", Types.NUMERIC); //$NON-NLS-1$
		nameToType.put("OTHER", Types.OTHER); //$NON-NLS-1$
		nameToType.put("REAL", Types.REAL); //$NON-NLS-1$
		nameToType.put("REF", Types.REF); //$NON-NLS-1$
		nameToType.put("SMALLINT", Types.SMALLINT); //$NON-NLS-1$
		nameToType.put("STRUCT", Types.STRUCT); //$NON-NLS-1$
		nameToType.put("TIME", Types.TIME); //$NON-NLS-1$
		nameToType.put("TIMESTAMP", Types.TIMESTAMP); //$NON-NLS-1$
		nameToType.put("TINYINT", Types.TINYINT); //$NON-NLS-1$
		nameToType.put("VARBINARY", Types.VARBINARY); //$NON-NLS-1$
		nameToType.put("VARCHAR", Types.VARCHAR); //$NON-NLS-1$

		typeToJava = new HashMap<Integer, String>();
		typeToJava.put(Types.ARRAY, Object.class.getName());
		typeToJava.put(Types.BIGINT, Long.class.getName());
		typeToJava.put(Types.BINARY, "byte[]");
		typeToJava.put(Types.BIT, Boolean.class.getName());
		typeToJava.put(Types.BLOB, "byte[]");
		typeToJava.put(Types.BOOLEAN, Boolean.class.getName());
		typeToJava.put(Types.CHAR, String.class.getName());
		typeToJava.put(Types.CLOB, String.class.getName());
		typeToJava.put(Types.DATALINK, Object.class.getName());
		typeToJava.put(Types.DATE, Date.class.getName());
		typeToJava.put(Types.DISTINCT, Object.class.getName());
		typeToJava.put(Types.DOUBLE, Double.class.getName());
		typeToJava.put(Types.FLOAT, Double.class.getName());
		typeToJava.put(Types.INTEGER, Integer.class.getName());
		typeToJava.put(Types.JAVA_OBJECT, Object.class.getName());
		typeToJava.put(Types.LONGNVARCHAR, String.class.getName());
		typeToJava.put(Types.LONGVARBINARY, "byte[]");
		typeToJava.put(Types.LONGVARCHAR, String.class.getName());
		typeToJava.put(Types.NCHAR, String.class.getName());
		typeToJava.put(Types.NCLOB, String.class.getName());
		typeToJava.put(Types.NVARCHAR, String.class.getName());
		typeToJava.put(Types.NULL, Object.class.getName());
		typeToJava.put(Types.OTHER, Object.class.getName());
		typeToJava.put(Types.REAL, Float.class.getName());
		typeToJava.put(Types.REF, Object.class.getName());
		typeToJava.put(Types.SMALLINT, Integer.class.getName());
		typeToJava.put(Types.STRUCT, Object.class.getName());
		typeToJava.put(Types.TIME, Date.class.getName());
		typeToJava.put(Types.TIMESTAMP, Date.class.getName());
		typeToJava.put(Types.TINYINT, Integer.class.getName());
		typeToJava.put(Types.VARBINARY, "byte[]");
		typeToJava.put(Types.VARCHAR, String.class.getName());
		typeToJava.put(Types.DECIMAL, BigDecimal.class.getName());
		typeToJava.put(Types.NUMERIC, BigDecimal.class.getName());

		
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

	private TypeHelper() {
		super();
	}
 
	
	public static boolean isJavaKeyword(String word){
		return javaKeywords.contains(word);
	}
	
	public static String getTypeName(int jdbcType) {
		String answer = typeToName.get(jdbcType);
		if (answer == null) {
			answer = "OTHER";
		}

		return answer;
	}

	public static int getJdbcType(String typeName) {
		Integer answer = nameToType.get(typeName);
		if (answer == null) {
			answer = Types.OTHER;
		}

		return answer;
	}

	public static String getJavaType(int jdbcType) {
		String java = typeToJava.get(jdbcType);
		if (java == null) {
			java = String.class.getName();			 
		}
		
		String lang="java.lang.";
		if(java.startsWith(lang) && java.indexOf(".",lang.length())<0 ){
			java=java.substring(lang.length());
		}
		return java;
	}
	
	public static boolean isString(int jdbcType){
		String java = typeToJava.get(jdbcType);
		if(java==null || java.equals(String.class.getName())){
			return true;
		}else{
			return false;
		}		
	}
	
	public static boolean isBoolean(int jdbcType){
		String java = typeToJava.get(jdbcType);
		if(java!=null){
			if(java.equals(Boolean.class.getName())){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isNumber(int jdbcType){
		String java = typeToJava.get(jdbcType);
		if(java!=null){
			if(java.equals(Integer.class.getName())){
				return true;
			}
			if(java.equals(Long.class.getName())){
				return true;
			}
			if(java.equals(Float.class.getName())){
				return true;
			}
			if(java.equals(Byte.class.getName())){
				return true;
			}
			if(java.equals(Double.class.getName())){
				return true;
			}
			
			if(java.equals(BigDecimal.class.getName())){
				return true;
			}
		}
		
		return false;
	}

}
