package com.tsc9526.monalisa.core.meta;

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
