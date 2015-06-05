package com.tsc9526.monalisa.plugin.eclipse.generator;

import org.eclipse.text.edits.MultiTextEdit;

public interface SourceGenerator {
	 
	public void generate(SourceUnit unit,MultiTextEdit edit);
}
