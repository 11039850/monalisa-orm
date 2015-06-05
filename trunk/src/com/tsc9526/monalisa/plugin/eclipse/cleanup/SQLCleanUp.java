package com.tsc9526.monalisa.plugin.eclipse.cleanup;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.cleanup.CleanUpContext;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class SQLCleanUp implements ICleanUp  {
	final static String KEY_SQL_SELECT="com.tsc9526.monalisa.plugin.eclipse.cleanup.SQLCleanUp.select";
	
	private CleanUpOptions fOptions;
	private RefactoringStatus fStatus;

	public SQLCleanUp() {
		
	}
	
	public ICleanUpFix createFix(CleanUpContext context) throws CoreException {		
		ICompilationUnit copy =context.getCompilationUnit();//.getWorkingCopy(null);		
		CompilationUnit compilationUnit= context.getAST();
		if (compilationUnit != null){	
			compilationUnit.getProblems();
			return new SQLCleanUpFix(compilationUnit,copy);
		}else{
			return null;
		}		
		 	
	} 

	public CleanUpRequirements getRequirements() {
		boolean changedRegionsRequired= false;
		Map<String,String> compilerOptions= null;
		boolean isEnableSQL= fOptions.isEnabled(KEY_SQL_SELECT);
		return new CleanUpRequirements(isEnableSQL, isEnableSQL, changedRegionsRequired, compilerOptions);     
	}
	
	public void setOptions(CleanUpOptions options) {
		Assert.isLegal(options != null);
		Assert.isTrue(fOptions == null);
		fOptions= options;  
	}
	
	public RefactoringStatus checkPreConditions(IJavaProject project, ICompilationUnit[] compilationUnits, IProgressMonitor monitor) throws CoreException {
		if (fOptions.isEnabled(KEY_SQL_SELECT)) { 
			fStatus= new RefactoringStatus();
		}
		return new RefactoringStatus();
	}
	
	
	public RefactoringStatus checkPostConditions(IProgressMonitor monitor) throws CoreException {
		try {
			if (fStatus == null || fStatus.isOK()) {
				return new RefactoringStatus();
			} else {
				return fStatus;
			}
		} finally {
			fStatus= null;
		}
	}
 

	public String[] getStepDescriptions() {
		if (fOptions.isEnabled(KEY_SQL_SELECT)){
			return new String[] {SQLCleanUpMessages.SQL_SELECT_DESC};
		}
		return null;
	} 
}
