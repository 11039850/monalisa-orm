/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.tools.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JavaWriter extends PrintWriter {
	private static final String DEFAULT_OUDENT = "\t";
	
	private int oudentLevel = 0;
	
	private int leftPadding=0;
	
	private boolean startLine = true;
	
	private boolean autoIndent = true;
 
	private ByteArrayOutputStream buffer;
	
	
	public static JavaWriter getBufferedWriter(){
		return getBufferedWriter(0);
	}
	public static JavaWriter getBufferedWriter(int leftPadding){
		ByteArrayOutputStream buffer=new ByteArrayOutputStream();
		JavaWriter w=new JavaWriter(buffer);
		w.setLeftPadding(leftPadding);
		w.buffer=buffer;
		return w;
	}
	
	public String getContent(){
		flush();
		if(buffer!=null){
			return new String(buffer.toByteArray());
		}else{
			return null;
		}
	} 
	
	private static Writer toWriter(OutputStream out,String charset){
		try{
			return new OutputStreamWriter(out, charset);
		}catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}
	
	public JavaWriter(Writer out) {
		super(out);
	}

	public JavaWriter(Writer out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public JavaWriter(OutputStream out){
		super(toWriter(out,"utf-8"));
	}

	public JavaWriter(OutputStream out, boolean autoFlush) {
		super(toWriter(out,"utf-8"), autoFlush);
	}

	public JavaWriter(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public JavaWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
	}

	public JavaWriter(File file) throws FileNotFoundException {
		super(file);
	}

	public JavaWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
	}
	
	
	
	public void setAutoIndent(boolean autoIndent) {
		this.autoIndent = autoIndent;
	}
	
	public void indent() {
		if (autoIndent) {
			throw new RuntimeException("Unable to indent manually in auto mode. Please set autoIndent to false.");
		}
		oudentLevel = (oudentLevel <= 0) ? 0 : oudentLevel-1;
	}
	
	public void oudent() {
		if (autoIndent) {
			throw new RuntimeException("Unable to oudent manually in auto mode. Please set autoIndent to false.");
		}
		oudentLevel++;
	}

	private void setAutoOudent(char c) {
		if (c == '{') {
			oudentLevel++;
		}
	}

	private void setAutoIndent(char c) {
		if (c == '}') {
			oudentLevel = (oudentLevel <= 0) ? 0 : oudentLevel-1;
		}
	}

	private boolean processing = false;
	
	private void addIdentation() {
		if (processing) {
			return;
		}
		processing = true;
		if (startLine) {
			String indentation = "";
			
			for (int i = 0; i < (leftPadding + oudentLevel); i++) {
				indentation += DEFAULT_OUDENT;
			}
			
			super.print(indentation);
			startLine = false;
		}
		processing = false;
	}

	private Character lastCharacted = null;
	
	@Override
	public void write(String text, int off, int len) {
		if (text != null && text.length() > off && autoIndent) {
			setAutoIndent(text.charAt(off));
		}
		addIdentation();
		super.write(text, off, len);
		if (text != null && text.length() > off + len - 1 && len > 0 && autoIndent) {
			lastCharacted = text.charAt(off + len - 1);
		}
	}

	@Override
	public void println() {
		if (lastCharacted != null && autoIndent) {
			setAutoOudent(lastCharacted);
			lastCharacted = null;
		}
		super.println();
		startLine = true;
	}

	public int getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(int leftPadding) {
		this.leftPadding = leftPadding;
	}
}