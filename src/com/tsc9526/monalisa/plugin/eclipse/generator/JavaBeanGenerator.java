package com.tsc9526.monalisa.plugin.eclipse.generator;

import org.eclipse.text.edits.MultiTextEdit;

public class JavaBeanGenerator implements SourceGenerator{
	private SourceUnit unit;
	
	@Override
	public void generate(SourceUnit unit, MultiTextEdit edit) {
		this.unit=unit;
		
	}

}
