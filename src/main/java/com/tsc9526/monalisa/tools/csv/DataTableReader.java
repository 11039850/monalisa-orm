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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.relique.io.DataReader;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassHelper;
import com.tsc9526.monalisa.tools.datatable.DataColumn;
import com.tsc9526.monalisa.tools.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class DataTableReader extends DataReader {
	private String[] columnNames;
	private String[] columnTypes;
	private List<Object[]> columnValues;
	private int rowIndex;

	public DataTableReader(DataTable<?> table){
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
					ClassHelper mc=MelpClass.getClassAssist(row.getClass());
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