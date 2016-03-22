package com.tsc9526.monalisa.core.convert;


public interface Converter {
	public static String JSON_CLASS="";
	
	public <T> T convert(Object source, Class<T> target);

}
