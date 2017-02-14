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
package com.tsc9526.monalisa.tools.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.relique.io.DataReader;
import org.relique.jdbc.csv.CsvRawReader;
import org.relique.jdbc.csv.SqlParser;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassAssist;
import com.tsc9526.monalisa.tools.datatable.CsvOptions;
import com.tsc9526.monalisa.tools.datatable.DataColumn;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.io.MelpFile;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Csv{	 
	public DataTable<DataMap> fromCsv(InputStream csvInputStream,CsvOptions options) {
		String csvString=MelpFile.readToString(csvInputStream, options.getCharset());
		
		return fromCsv(csvString, options);
	}
	 
	public DataTable<DataMap> fromCsv(String csvString,CsvOptions options){
		try {
			DataTable<DataMap> table=new DataTable<DataMap>();
			
			CsvRawReader reader = loadCsvRawReader(csvString, options);
			
			String[] columns=reader.getColumnNames();
			table.setHeaders(columns);
			
			while(reader.next()){
				String[] vs=reader.getFieldValues();
				
				DataMap m=new DataMap();
				for(int i=0;i<columns.length;i++){
					m.put(columns[i], i<vs.length?vs[i]:null);
					
				}
				table.add(m);
			}
			
			reader.close();
			
			return table;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void writeToCsv(DataTable<?> table, OutputStream outputStream, CsvOptions options){
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
						ClassAssist mc=MelpClass.getClassAssist(row.getClass());
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
					String value = MelpClass.converter.convert(vs[k],String.class);
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
	
	public DataTable<DataMap> queryTable(DataTable<?>table,String sql){
		SqlParser parser = new SqlParser();
		try{
			parser.parse(sql);
					
			ResultSet rs = new DataTableResultSet( 
				createDataReader(table),
				"_THIS_TABLE",ResultSet.TYPE_SCROLL_INSENSITIVE,
				parser);
			
			DataTable<DataMap> rtable=new DataTable<DataMap>();
			while(rs.next()){
				DataMap row=new DataMap();
				loadToMap(rs,row);
				rtable.add(row);
			}
			rs.close();
			
			return rtable;
		}catch (Exception e){
			throw new RuntimeException("SQL exception: " + sql+"\r\nHeaders: \r\n"+table.getHeaders(),e);
		}
	}
	
	protected DataMap loadToMap(ResultSet rs, DataMap map) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();

		Map<String, Integer> xs = new HashMap<String, Integer>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}
			name = name.toLowerCase();

			Integer n = xs.get(name);
			if (n != null) {
				map.put(name + n, rs.getObject(i));

				xs.put(name, n + 1);
			} else {
				xs.put(name, 1);

				map.put(name, rs.getObject(i));
			}
		}

		return map;
	}
	

	private String addQuotes(String value, String separator, char quoteChar, String quoteStyle) {
		if ("C".equals(quoteStyle)) {
			value = value.replace("\\", "\\\\");
			value = value.replace("" + quoteChar, "\\" + quoteChar);
		} else {
			value = value.replace("" + quoteChar, "" + quoteChar + quoteChar);
		}

		 
		if (value.indexOf(separator) >= 0 || value.indexOf(quoteChar) >= 0 || value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0) {
			value = quoteChar + value + quoteChar;
		}
		return value;
	}

	public CsvRawReader loadCsvRawReader(String csvString, CsvOptions options) {
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
	
	public DataReader createDataReader(DataTable<?> table){
		return new DataTableReader(table);
	}
}
