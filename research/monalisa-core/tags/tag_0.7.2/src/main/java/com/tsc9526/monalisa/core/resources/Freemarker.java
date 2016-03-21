package com.tsc9526.monalisa.core.resources;

import freemarker.template.Configuration;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Freemarker {

	public static Configuration getFreemarkConfiguration(){
		 Configuration cfg = new Configuration(); 
		 cfg.setDefaultEncoding("utf-8");
		 cfg.setClassForTemplateLoading(Freemarker.class,"/com/tsc9526/monalisa/core/resources/template");
		
		 return cfg;
	}
}
