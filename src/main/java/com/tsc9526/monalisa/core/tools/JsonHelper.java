package com.tsc9526.monalisa.core.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
	private static GsonBuilder gb=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Gson getGson(){		
		return gb.create();   
	}

}
