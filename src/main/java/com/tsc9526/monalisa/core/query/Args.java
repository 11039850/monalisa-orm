package com.tsc9526.monalisa.core.query;

import java.util.Stack;

@SuppressWarnings("unchecked")
public class Args {
	protected Stack<Object> dynamic=new Stack<Object>();
	
	public Args(Object... args){
		for(int i=0;i<args.length;i++){
			dynamic.push(args[args.length-1-i]);
		}
	}
	
	public Args push(Object arg){
		dynamic.add(arg);
		return this;
	}
	
	/**
	 * 获取所有的动态参数
	 * 
	 * @return
	 */
	public Object[] popAll(){
		return dynamic.toArray();
	}
	
	/**
	 * 弹出一个动态参数
	 * 
	 * @return
	 */
	public <T> T pop(){
		return (T)dynamic.pop();
	}
	
	/**
	 * 弹出一个动态参数， 如果为动态参数不存在或null 则返回默认值
	 * 
	 * @param defaultValue 默认值
	 * @return
	 */
	public <T> T pop(T defaultValue){
		if(dynamic.isEmpty()){
			return defaultValue;
		}else{
			T r=(T)dynamic.pop();
			if(r==null){
				r=defaultValue;
			}
			return r;
		}
	}
}
