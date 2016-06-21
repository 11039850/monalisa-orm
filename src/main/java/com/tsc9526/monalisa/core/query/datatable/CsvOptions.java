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
package com.tsc9526.monalisa.core.query.datatable;

import java.util.ArrayList;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CsvOptions {
	public static CsvOptions createDefaultOptions() {
		return new CsvOptions();
	}

	private String   separator = ",";
	private Character quotechar = '"';

	private String commentChar;
	private int    skipLeadingLines = 0;

	private boolean trimHeaders = true;
	private boolean ignoreUnparseableLines;
	private int 	skipLeadingDataLines;
	private boolean trimValues = true;
	private boolean suppressHeaders = false;
	 
	private boolean headerFixedWidth = true;

	private String quoteStyle="C";

	private boolean defectiveHeaders;

	private ArrayList<int[]> fixedWidthColumns = null;

	private String headerLine;
	
	private String missingValue;
	
	private String charset="utf-8";
	
	public String getSeparator() {
		return separator;
	}

	public CsvOptions setSeparator(String separator) {
		this.separator = separator;
		return this;

	}

	public Character getQuotechar() {
		return quotechar;
	}

	public CsvOptions setQuotechar(Character quotechar) {
		this.quotechar = quotechar;
		return this;
	}

	public int getSkipLeadingLines() {
		return skipLeadingLines;
	}

	public CsvOptions setSkipLeadingLines(int skipLeadingLines) {
		this.skipLeadingLines = skipLeadingLines;
		return this;
	}

	public boolean isTrimHeaders() {
		return trimHeaders;
	}

	public CsvOptions setTrimHeaders(boolean trimHeaders) {
		this.trimHeaders = trimHeaders;
		return this;
	}

	public boolean isTrimValues() {
		return trimValues;
	}

	public CsvOptions setTrimValues(boolean trimValues) {
		this.trimValues = trimValues;
		return this;
	}

	public boolean isSuppressHeaders() {
		return suppressHeaders;
	}

	public CsvOptions setSuppressHeaders(boolean suppressHeaders) {
		this.suppressHeaders = suppressHeaders;
		return this;
	}

	public boolean isHeaderFixedWidth() {
		return headerFixedWidth;
	}

	public CsvOptions setHeaderFixedWidth(boolean headerFixedWidth) {
		this.headerFixedWidth = headerFixedWidth;
		return this;
	}

	public String getCommentChar() {
		return commentChar;
	}

	public CsvOptions setCommentChar(String commentChar) {
		this.commentChar = commentChar;
		return this;
	}

	public boolean isIgnoreUnparseableLines() {
		return ignoreUnparseableLines;
	}

	public CsvOptions setIgnoreUnparseableLines(boolean ignoreUnparseableLines) {
		this.ignoreUnparseableLines = ignoreUnparseableLines;
		return this;
	}

	public int getSkipLeadingDataLines() {
		return skipLeadingDataLines;
	}

	public CsvOptions setSkipLeadingDataLines(int skipLeadingDataLines) {
		this.skipLeadingDataLines = skipLeadingDataLines;
		return this;
	}
  
	public CsvOptions setFixedWidthColumns(ArrayList<int[]> fixedWidthColumns) {
		this.fixedWidthColumns = fixedWidthColumns;
		return this;
	}

	public ArrayList<int[]> getFixedWidthColumns() {
		return this.fixedWidthColumns;
	}

	public String getQuoteStyle() {
		return quoteStyle;
	}

	public CsvOptions setQuoteStyle(String quoteStyle) {
		this.quoteStyle = quoteStyle;
		return this;
	}

	public boolean isDefectiveHeaders() {
		return defectiveHeaders;
	}

	public CsvOptions setDefectiveHeaders(boolean defectiveHeaders) {
		this.defectiveHeaders = defectiveHeaders;
		return this;
	}

	public String getHeaderLine() {
		return headerLine; 
	}

	public CsvOptions setHeaderLine(String headerLine) {
		this.headerLine = headerLine;
		return this;
	}

	public String getMissingValue() {
		return missingValue;
	}

	public CsvOptions setMissingValue(String missingValue) {
		this.missingValue = missingValue;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public CsvOptions setCharset(String charset) {
		this.charset = charset;
		return this;
	}
}
