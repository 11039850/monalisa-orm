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
package com.tsc9526.monalisa.tools.string;

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
public class MelpTypes {
	private static final Class<?>[] PRIMITIVE_TYPES = { int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class,
		Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };

	public static boolean isPrimitiveOrString(Object target) {
		if (target instanceof String) {
			return true;
		}
	
		Class<?> classOfPrimitive = target.getClass();
		if(classOfPrimitive.isPrimitive()){
			return true;
		}
		
		for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
			if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
				return true;
			}
		}
		return false;
	}

	private static Map<Integer, String> jdbcTypeToName;
	private static Map<String, Integer> nameToType;
	private static Map<Integer, String> jdbcTypeToJava;
	private static Set<String> javaKeywords=new HashSet<String>();
 	 	
	static {
		jdbcTypeToName = new HashMap<Integer, String>();
		jdbcTypeToName.put(Types.ARRAY, "ARRAY"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.BIGINT, "BIGINT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.BINARY, "BINARY"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.BIT, "BIT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.BLOB, "BLOB"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.BOOLEAN, "BOOLEAN"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.CHAR, "CHAR"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.CLOB, "CLOB"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.DATALINK, "DATALINK"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.DATE, "DATE"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.DECIMAL, "DECIMAL"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.DISTINCT, "DISTINCT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.DOUBLE, "DOUBLE"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.FLOAT, "FLOAT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.INTEGER, "INTEGER"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.JAVA_OBJECT, "JAVA_OBJECT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.LONGVARBINARY, "LONGVARBINARY"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.LONGVARCHAR, "LONGVARCHAR"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.NCHAR, "NCHAR"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.NCLOB, "NCLOB"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.NVARCHAR, "NVARCHAR"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.LONGNVARCHAR, "LONGNVARCHAR"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.NULL, "NULL"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.NUMERIC, "NUMERIC"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.OTHER, "OTHER"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.REAL, "REAL"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.REF, "REF"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.SMALLINT, "SMALLINT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.STRUCT, "STRUCT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.TIME, "TIME"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.TIMESTAMP, "TIMESTAMP"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.TINYINT, "TINYINT"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.VARBINARY, "VARBINARY"); //$NON-NLS-1$
		jdbcTypeToName.put(Types.VARCHAR, "VARCHAR"); //$NON-NLS-1$

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

		jdbcTypeToJava = new HashMap<Integer, String>();
		jdbcTypeToJava.put(Types.ARRAY, Object.class.getName());
		jdbcTypeToJava.put(Types.BIGINT, Long.class.getName());
		jdbcTypeToJava.put(Types.BINARY, "byte[]");
		jdbcTypeToJava.put(Types.BIT, Boolean.class.getName());
		jdbcTypeToJava.put(Types.BLOB, "byte[]");
		jdbcTypeToJava.put(Types.BOOLEAN, Boolean.class.getName());
		jdbcTypeToJava.put(Types.CHAR, String.class.getName());
		jdbcTypeToJava.put(Types.CLOB, String.class.getName());
		jdbcTypeToJava.put(Types.DATALINK, Object.class.getName());
		jdbcTypeToJava.put(Types.DATE, Date.class.getName());
		jdbcTypeToJava.put(Types.DISTINCT, Object.class.getName());
		jdbcTypeToJava.put(Types.DOUBLE, Double.class.getName());
		jdbcTypeToJava.put(Types.FLOAT, Double.class.getName());
		jdbcTypeToJava.put(Types.INTEGER, Integer.class.getName());
		jdbcTypeToJava.put(Types.JAVA_OBJECT, Object.class.getName());
		jdbcTypeToJava.put(Types.LONGNVARCHAR, String.class.getName());
		jdbcTypeToJava.put(Types.LONGVARBINARY, "byte[]");
		jdbcTypeToJava.put(Types.LONGVARCHAR, String.class.getName());
		jdbcTypeToJava.put(Types.NCHAR, String.class.getName());
		jdbcTypeToJava.put(Types.NCLOB, String.class.getName());
		jdbcTypeToJava.put(Types.NVARCHAR, String.class.getName());
		jdbcTypeToJava.put(Types.NULL, Object.class.getName());
		jdbcTypeToJava.put(Types.OTHER, Object.class.getName());
		jdbcTypeToJava.put(Types.REAL, Float.class.getName());
		jdbcTypeToJava.put(Types.REF, Object.class.getName());
		jdbcTypeToJava.put(Types.SMALLINT, Integer.class.getName());
		jdbcTypeToJava.put(Types.STRUCT, Object.class.getName());
		jdbcTypeToJava.put(Types.TIME, Date.class.getName());
		jdbcTypeToJava.put(Types.TIMESTAMP, Date.class.getName());
		jdbcTypeToJava.put(Types.TINYINT, Integer.class.getName());
		jdbcTypeToJava.put(Types.VARBINARY, "byte[]");
		jdbcTypeToJava.put(Types.VARCHAR, String.class.getName());
		jdbcTypeToJava.put(Types.DECIMAL, BigDecimal.class.getName());
		jdbcTypeToJava.put(Types.NUMERIC, BigDecimal.class.getName());

		
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

	private MelpTypes() {
		super();
	}
 
	
	public static boolean isJavaKeyword(String word){
		return javaKeywords.contains(word);
	}
	
	public static String getTypeName(int jdbcType) {
		String answer = jdbcTypeToName.get(jdbcType);
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
		String java = jdbcTypeToJava.get(jdbcType);
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
		String java = jdbcTypeToJava.get(jdbcType);
		if(java==null || java.equals(String.class.getName())){
			return true;
		}else{
			return false;
		}		
	}
	
	public static boolean isBoolean(int jdbcType){
		String java = jdbcTypeToJava.get(jdbcType);
		if(java!=null){
			if(java.equals(Boolean.class.getName())){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isNumber(int jdbcType){
		String java = jdbcTypeToJava.get(jdbcType);
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


	public static boolean isDateType(int jdbcType) {
		return jdbcType==Types.DATE || jdbcType==Types.TIMESTAMP;
	}

}
