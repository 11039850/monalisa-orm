package com.tsc9526.monalisa.plugin.eclipse.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tsc9526.monalisa.plugin.eclipse.activator.MonalisaPlugin;
 
public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(MonalisaPlugin.getDefault().getPreferenceStore());		 
	}
	
	 
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.CodeTemplateDirectory, "&Code Template Directory:", getFieldEditorParent()));
		/*
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_BOOLEAN,
				"&An example of a boolean preference",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"C&hoice 2", "choice2" }
		}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
		*/
		 
	}
	
	protected void performApply() {		
		super.performApply();	 
	}
 
	public void init(IWorkbench workbench) {
	}
	
}