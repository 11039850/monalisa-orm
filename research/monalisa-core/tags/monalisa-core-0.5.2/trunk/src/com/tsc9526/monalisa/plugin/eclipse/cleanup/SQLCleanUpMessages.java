package com.tsc9526.monalisa.plugin.eclipse.cleanup;

import org.eclipse.osgi.util.NLS;

public class SQLCleanUpMessages extends NLS{	
	private static final String BUNDLE_NAME= "com.tsc9526.monalisa.plugin.eclipse.cleanup.SQLCleanUpMessages";  
  
	public static String SQL;
	
	public static String SQL_SELECT;
	
	public static String SQL_SELECT_DESC;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, SQLCleanUpMessages.class);
	}

 
	private SQLCleanUpMessages() {
 	}

}
