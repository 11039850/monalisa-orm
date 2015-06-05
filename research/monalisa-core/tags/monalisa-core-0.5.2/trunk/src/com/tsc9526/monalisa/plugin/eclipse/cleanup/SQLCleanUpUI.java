package com.tsc9526.monalisa.plugin.eclipse.cleanup;

import java.util.Map;

import org.eclipse.jdt.internal.ui.fix.AbstractCleanUp;
import org.eclipse.jdt.internal.ui.preferences.cleanup.AbstractCleanUpTabPage;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.tsc9526.monalisa.core.tools.FileHelper;
import com.tsc9526.monalisa.plugin.eclipse.resources.Resource;

@SuppressWarnings("restriction")
public class SQLCleanUpUI extends AbstractCleanUpTabPage {
	public static final String ID= "com.tsc9526.monalisa.plugin.eclipse.cleanup.SQLCleanUpUI"; 
	
	static String[] FALSE_TRUE=new String[]{CleanUpOptions.FALSE,CleanUpOptions.TRUE};
	 
	public SQLCleanUpUI() {
		super();
	}
  
	@Override
	protected AbstractCleanUp[] createPreviewCleanUps(Map<String, String> values) {
		return new AbstractCleanUp[] { };
	}
  

	@Override
	protected void doCreatePreferences(Composite composite, int numColumns) {
		Group annotationsGroup= createGroup(numColumns, composite, SQLCleanUpMessages.SQL);

		final CheckboxPreference annotationsPref= createCheckboxPref(annotationsGroup, numColumns, SQLCleanUpMessages.SQL_SELECT, SQLCleanUp.KEY_SQL_SELECT, FALSE_TRUE);
		
		registerPreference(annotationsPref);
		
		createLabel(numColumns, annotationsGroup, SQLCleanUpMessages.SQL_SELECT_DESC);
		

	}

	public String getPreview() {	
		return FileHelper.readToString(Resource.class.getResourceAsStream("/com/tsc9526/monalisa/plugin/eclipse/resources/preview_select.txt"),"utf-8");
	}
 

}
