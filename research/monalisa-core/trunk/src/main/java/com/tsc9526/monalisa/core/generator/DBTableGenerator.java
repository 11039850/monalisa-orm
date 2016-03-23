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
package com.tsc9526.monalisa.core.generator;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Index;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaIndex;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.parser.jsp.JspContext;
import com.tsc9526.monalisa.core.tools.ClassHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBTableGenerator {
	private MetaTable table;
	private String modelClass;
	private String dbi;

	public DBTableGenerator(MetaTable table, String modelClass, String dbi) {
		this.table = table;
		this.modelClass = modelClass;
		this.dbi = dbi;
	}

	public void generate(OutputStream os) throws Exception {
		generate(new OutputStreamWriter(os, "utf-8"));
	}

	public void generate(Writer w) {
		try {
			JspContext request = new JspContext();
			request.setAttribute("table", table);
			request.setAttribute("modelClass", modelClass);
			request.setAttribute("dbi", dbi);
			
			request.setAttribute("imports", getImports());
			
			DBWriterModel dbw=new DBWriterModel();
			dbw.service(request,new PrintWriter(w));
			 
			w.flush();
			w.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Set<String> getImports() {
		Set<String> imports = new LinkedHashSet<String>();
		
		boolean importListMap=false;
		boolean importIndex  =false;
		for(MetaIndex index:table.getIndexes()){
			importIndex=true;
			if(index.isUnique() && index.getColumns().size()==1){
				importListMap=true;
				break;
			}
		}
		if(table.getKeyColumns().size()==1){
			importListMap=true;
		}
	   
		imports.add(DB.class.getName());
		imports.add(Table.class.getName());
		imports.add(Column.class.getName());
		if(importIndex){
			imports.add(Index.class.getName());
		}	
		imports.add(ClassHelper.class.getName());
		
//		if (table.getKeyColumns().size() > 0) {
//			imports.add(Query.class.getName());
//		}
		
		for (MetaColumn c : table.getColumns()) {
			imports.addAll(c.getImports());
		}
	 			
		if(importListMap){
			imports.add(List.class.getName());
			imports.add(Map.class.getName());
			imports.add(LinkedHashMap.class.getName());
		}
		
		return imports;
	}
	
	public MetaTable getTable() {
		return table;
	}

	public void setTable(MetaTable table) {
		this.table = table;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getDbi() {
		return dbi;
	}

	public void setDbi(String dbi) {
		this.dbi = dbi;
	}
}
