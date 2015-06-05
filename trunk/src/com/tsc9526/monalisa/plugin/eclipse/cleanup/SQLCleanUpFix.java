package com.tsc9526.monalisa.plugin.eclipse.cleanup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.text.edits.MultiTextEdit;

import com.tsc9526.monalisa.plugin.eclipse.console.MMC;
import com.tsc9526.monalisa.plugin.eclipse.generator.JavaBeanGenerator;
import com.tsc9526.monalisa.plugin.eclipse.generator.SelectGenerator;
import com.tsc9526.monalisa.plugin.eclipse.generator.SourceGenerator;
import com.tsc9526.monalisa.plugin.eclipse.generator.SourceUnit;

public class SQLCleanUpFix implements ICleanUpFix{
	private CompilationUnit  compilationUnit;
	private ICompilationUnit copy;
	 
	public SQLCleanUpFix(CompilationUnit compilationUnit,ICompilationUnit copy){
		this.compilationUnit=compilationUnit;	
		this.copy=copy;
	}
	 
	public CompilationUnitChange createChange(IProgressMonitor p)throws CoreException {
		IFile file=(IFile)copy.getResource();
		
		MultiTextEdit edit=new MultiTextEdit();
		
		if(isTypeDeclaration(compilationUnit)){
			MMC.getConsole().clearConsole();
			
			SourceUnit    unit=new SourceUnit(compilationUnit);
			for(SourceGenerator sg:getSourceGenerators()){
				sg.generate(unit, edit);
			}
		}
		
		CompilationUnitChange unitChange=new CompilationUnitChange(file.getName(),copy);
		unitChange.setEdit(edit);
		 	
		return unitChange;		 
	}
	
	private boolean isTypeDeclaration(CompilationUnit unit){
		if(unit.types().size()>0 && unit.types().get(0) instanceof TypeDeclaration){
			return true;
		}else{
			return false;
		}
	}
	
	private List<SourceGenerator> getSourceGenerators(){
		List<SourceGenerator> sgs=new ArrayList<SourceGenerator>();
		sgs.add(new SelectGenerator());
		sgs.add(new JavaBeanGenerator());
		return sgs;
	}
}
