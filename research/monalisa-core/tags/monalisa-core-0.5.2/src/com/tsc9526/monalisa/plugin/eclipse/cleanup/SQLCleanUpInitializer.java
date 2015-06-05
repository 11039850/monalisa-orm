package com.tsc9526.monalisa.plugin.eclipse.cleanup;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer;

public class SQLCleanUpInitializer implements ICleanUpOptionsInitializer  {
 	 
	public SQLCleanUpInitializer(){
		
	}
	
	public void setDefaultOptions(CleanUpOptions options) {
		options.setOption(SQLCleanUp.KEY_SQL_SELECT, CleanUpOptions.TRUE);
	}
}
