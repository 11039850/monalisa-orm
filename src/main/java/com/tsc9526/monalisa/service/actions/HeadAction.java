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
package com.tsc9526.monalisa.service.actions;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.args.ModelArgs;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.string.MelpTypes;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class HeadAction extends Action{

	public HeadAction(ModelArgs args) {
		super(args);
	}

	public Response getResponse(){
		if(args.getTable()!=null){
			return getTableModel();
		}else if(args.getTables()!=null){
			return getTablesModel();
		}else{		
			return getAllTables();
		}	 
	}
	
	/**
	 * Get the database tables
	 * 
	 * @return list tables of the database 
	 */
	public Response getAllTables(){
		DataTable<DataMap> table=new DataTable<DataMap>();
		for(String t:db.getTables()){
			DataMap m=new DataMap();
			
			m.put("table_name",t);
			
			table.add(m);
		}
		return doGetTable(table );
	}
	
	protected Response doGetTable(DataTable<DataMap> table){
		return new Response(table).setMessage("OK: "+table.size());
	}
	
	protected Response getTableModel(){
		List<DataMap> model=getTableModel(args.getTable()); 
		
		Response response=new Response(model);
		return response;
	}
	
	protected Response getTablesModel(){
		DataMap models=new DataMap();
		
		for(String table:args.getTables()){
			List<DataMap> model=getTableModel(table); 
			
			models.put(table,model);
		}
		
		Response response=new Response(models);
		return response;
	}
	
	protected List<DataMap> getTableModel(String tableName){
		Record record=db.createRecord(tableName);
				
		List<DataMap> cs=new ArrayList<DataMap>();
		for(FGS fgs:record.fields()){
			Column c=fgs.getAnnotation(Column.class);
			 
			DataMap column=new DataMap();
			column.put("name", c.name());
			column.put("auto", c.auto());
			column.put("key", c.key());
			column.put("notnull", c.notnull());
			column.put("length", c.length());
			column.put("value", "NULL".equals(c.value())?null:c.value());
			 
			int jdbcType=c.jdbcType();
			column.put("type",jdbcType);
			column.put("typestring",MelpTypes.getJavaType(jdbcType));
		 	
			column.put("remarks", c.remarks());
		 	
			cs.add(column);
		}
		 
		
		return cs;
	}
  
}
