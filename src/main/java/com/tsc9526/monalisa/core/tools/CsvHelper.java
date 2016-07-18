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
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.relique.io.DataReader;
import org.relique.io.TableReader;
import org.relique.jdbc.csv.CsvConnection;
import org.relique.jdbc.csv.CsvRawReader;
import org.relique.jdbc.csv.CsvResultSet;
import org.relique.jdbc.csv.CsvStatement;
import org.relique.jdbc.csv.SqlParser;

import com.tsc9526.monalisa.core.query.datatable.CsvOptions;
import com.tsc9526.monalisa.core.query.datatable.DataColumn;
import com.tsc9526.monalisa.core.query.datatable.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
 
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CsvHelper {
	 
	public static DataTable<DataMap> fromCsv(InputStream csvInputStream,CsvOptions options) {
		String csvString=FileHelper.readToString(csvInputStream, options.getCharset());
		
		return fromCsv(csvString, options);
	}
	 
	public static DataTable<DataMap> fromCsv(String csvString,CsvOptions options){
		try {
			DataTable<DataMap> table=new DataTable<DataMap>();
			
			CsvRawReader reader = CsvHelper.loadCsvRawReader(csvString, options);
			
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
	
	public static DataTable<DataMap> queryTable(DataTable<?>table,String sql){
		SqlParser parser = new SqlParser();
		try{
			parser.parse(sql);
					
			ResultSet rs = new DataTableResultSet( 
				CsvHelper.createDataReader(table),
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
	
	protected static DataMap loadToMap(ResultSet rs, DataMap map) throws SQLException {
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
	

	private static String addQuotes(String value, String separator, char quoteChar, String quoteStyle) {
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
	
	public static DataReader createDataReader(DataTable<?> table){
		return new DataTableReader(table);
	}
	
	
	public static class DataTableConnection extends CsvConnection {

		protected DataTableConnection() throws SQLException {
			
			super(new SimpleDataTableReader(),new Properties(),"");
		}
 	}
	
	public static class SimpleDataTableReader implements TableReader{
		 
		public Reader getReader(Statement statement, String tableName) throws SQLException {
			 
			return new StringReader("a");
		}
 		 
		public List<String> getTableNames(Connection connection) throws SQLException {
			List<String> tables=new ArrayList<String>();
			
			tables.add("_THIS_TABLE");
			
			return tables;
		}
	}
	

	public static class DataTableResultSet extends CsvResultSet{
		 
		public DataTableResultSet(
				DataReader reader,
				String tableName,
				int resultSetType,
				SqlParser parser) throws ClassNotFoundException, SQLException
		{
			super(createCsvStatement(), reader, tableName, parser.getColumns(),
					parser.isDistinct(),
					resultSetType,
					parser.getWhereClause(),
					parser.getGroupByColumns(),
					parser.getHavingClause(),
					parser.getOrderByColumns(),
					parser.getLimit(),
					parser.getOffset()
		 			, getColumnTypes(), getSkipLeadingLines(),new HashMap<String, Object>()); 
		}
		
		private static CsvStatement createCsvStatement() throws SQLException{
			return new TableStatement(new DataTableConnection(),ResultSet.TYPE_SCROLL_INSENSITIVE);
		}
		
		private static String getColumnTypes(){
			return null;
		}
		
		private static int getSkipLeadingLines(){
			return 0;
		}
	}

	public static class TableStatement extends CsvStatement{
		protected TableStatement(CsvConnection connection, int resultSetType) {
			super(connection, resultSetType);
		}
	}

	public static class DataTableReader extends DataReader {
		private String[] columnNames;
		private String[] columnTypes;
		private List<Object[]> columnValues;
		private int rowIndex;

		DataTableReader(DataTable<?> table){
			List<DataColumn> hs=table.getHeaders();
			
			List<String>  columnNames=new ArrayList<String>();
			List<String>  columnTypes=new ArrayList<String>();
			 
			List<Object[]> columnValues=new ArrayList<Object[]>();

			for(DataColumn c:hs){
				columnNames.add(c.getName());
				columnTypes.add(c.getTypeString());
			}
		
			for(Object row:table){
				Object[] vs=new Object[columnNames.size()];
				
				int i=0;
				if(row instanceof Map){
					Map<?,?> map=(Map<?,?>)row;
					for(String name:columnNames){
						vs[i++]=map.get(name);
					}
				}else{
					if(row.getClass().isPrimitive() || row.getClass().getName().startsWith("java.")){
						vs[i++]=row;
					}else if(row.getClass().isArray()){
						Object[] xs=(Object[])row;
						for(int k=0;k<vs.length;k++){
							vs[k]=xs[k];
						}
					}else{					
						MetaClass mc=ClassHelper.getMetaClass(row.getClass());
						for(String name:columnNames){
							FGS fgs=mc.getField(name);
							Object v=null;
							if(fgs!=null){
								v=fgs.getObject(row);
							}
							vs[i++]=v;
						}
					}
				}
				 
				columnValues.add(vs);
			}
		 
		  
			this.columnNames = columnNames.toArray(new String[0]);
			this.columnTypes = columnTypes.toArray(new String[0]);
			this.columnValues = columnValues;
			rowIndex = -1;
		}

		 
		public boolean next() throws SQLException {
			rowIndex++;
			boolean retval = (rowIndex < columnValues.size());
			return retval;
		}

		 
		public String[] getColumnNames() throws SQLException {
			return columnNames;
		}

		 
		public void close() throws SQLException {
		}

		 
		public Map<String, Object> getEnvironment() throws SQLException {
			HashMap<String, Object> retval = new HashMap<String, Object>();
			Object[] o = columnValues.get(rowIndex);
			for (int i = 0; i < columnNames.length; i++) {
				retval.put(columnNames[i].toUpperCase(), o[i]);
			}
			return retval;
		}

		 
		public String[] getColumnTypes() throws SQLException {
			return columnTypes;
		}

		 
		public int[] getColumnSizes() throws SQLException {
			int[] columnSizes = new int[columnTypes.length];
			Arrays.fill(columnSizes, DEFAULT_COLUMN_SIZE);
			return columnSizes;
		}

		public String getTableAlias() {
			return null;
		}
	}
}
