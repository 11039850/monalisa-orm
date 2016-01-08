package com.tsc9526.monalisa.core.convert;


public interface Converter {

	public <T> T convert(Object source, Class<T> target);

}
