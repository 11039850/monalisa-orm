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
package com.tsc9526.monalisa.core.tools;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.relique.jdbc.csv.CsvRawReader;

import com.tsc9526.monalisa.core.query.datatable.CsvOptions;
import com.tsc9526.monalisa.core.query.datatable.DataColumn;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CsvHelper {
	public static void writeToCsv(DataTable<?> table, OutputStream outputStream, CsvOptions options){
		PrintStream out=null;
		
		try{ 
			out=new PrintStream(outputStream,true,options.getCharset());
			
			String separator = options.getSeparator();
			Character quoteChar = options.getQuotechar();
			String quoteStyle = options.getQuoteStyle();
			boolean writeHeaderLine = !options.isSuppressHeaders();
	
			List<DataColumn> headers = table.getHeaders();
	
			int columnCount = headers.size();
			if (writeHeaderLine) {
				for (int i = 0; i <  columnCount; i++) {
					if (i > 0) {
						out.print(separator);
					}
					out.print(headers.get(i).getName());
				}
				out.println();
			}
	
			for(Object row:table){
				Object[] vs=new Object[headers.size()];
				
				int i=0;
				if(row instanceof Map){
					Map<?,?> map=(Map<?,?>)row;
					for(DataColumn column:headers){
						vs[i++]=map.get(column.getName());
					}
				}else{
					if(row.getClass().isPrimitive() || row.getClass().getName().startsWith("java.")){
						vs[i++]=row;
					}else{					
						MetaClass mc=ClassHelper.getMetaClass(row.getClass());
						for(DataColumn column:headers){
							FGS fgs=mc.getField(column.getName());
							Object v=null;
							if(fgs!=null){
								v=fgs.getObject(row);
							}
							vs[i++]=v;
						}
					}
				}
				
				for (int k= 0; k < vs.length; k++) {
					if (k > 0) {
						out.print(separator);
					}
					String value = ClassHelper.converter.convert(vs[k],String.class);
					if (value != null) {
						if (quoteChar != null) {
							value = addQuotes(value, separator, quoteChar.charValue(), quoteStyle);
						}
						out.print(value);
					}
				}
				
				out.println(); 
			}
	
			out.flush();
		}catch(IOException e){
			throw new RuntimeException(e);
		}finally{
			if(out!=null)out.close();
		}
	}

	private static String addQuotes(String value, String separator, char quoteChar, String quoteStyle) {
		/*
		 * Escape all quote chars embedded in the string.
		 */
		if ("C".equals(quoteStyle)) {
			value = value.replace("\\", "\\\\");
			value = value.replace("" + quoteChar, "\\" + quoteChar);
		} else {
			value = value.replace("" + quoteChar, "" + quoteChar + quoteChar);
		}

		/*
		 * Surround value with quotes if it contains any special characters.
		 */
		if (value.indexOf(separator) >= 0 || value.indexOf(quoteChar) >= 0 || value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0) {
			value = quoteChar + value + quoteChar;
		}
		return value;
	}

	public static CsvRawReader loadCsvRawReader(String csvString, CsvOptions options) {
		try {
			LineNumberReader input = new LineNumberReader(new StringReader(csvString));

			CsvRawReader reader = new CsvRawReader(
					input,
					null,
					null,
					options.getSeparator(),
					options.isSuppressHeaders(),
					options.isHeaderFixedWidth(),
					options.getQuotechar(),
					options.getCommentChar(), 
					options.getHeaderLine(),
					options.isTrimHeaders(),
					options.isTrimValues(),
					options.getSkipLeadingLines(),
					options.isIgnoreUnparseableLines(),
					options.getMissingValue(),
					options.isDefectiveHeaders(),
					options.getSkipLeadingDataLines(),
					options.getQuoteStyle(),
					options.getFixedWidthColumns());
			return reader;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
