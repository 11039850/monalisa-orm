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

import java.sql.Types;
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
		}else if(args.getTable()!=null){
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
		DataMap data=getTableModel(args.getTable()); 
		
		Response response=new Response(data);
		return response;
	}
	
	protected Response getTablesModel(){
		List<DataMap> ds=new ArrayList<DataMap>();
		
		for(String table:args.getTables()){
			DataMap data=getTableModel(table); 
			
			ds.add(data);
		}
		
		Response response=new Response(ds);
		return response;
	}
	
	protected DataMap getTableModel(String tableName){
		Record record=db.createRecord(tableName);
				
		List<String>  colNames =new ArrayList<String>();
		List<DataMap> colModel=new ArrayList<DataMap>();
		for(FGS fgs:record.fields()){
			Column c=fgs.getAnnotation(Column.class);
			colNames.add(c.name());
			
			//{ name : 'id', index : 'id', width : 60, sorttype : "int", editable : true }
			DataMap model=new DataMap();
			model.put("name", c.name());
			model.put("id",   c.name());
			model.put("width",60);
			model.put("editable", "true");
			model.put("hidden",   false);
			
			int jdbcType=c.jdbcType();
			if(MelpTypes.isNumber(jdbcType)){
				model.put("sorttype","int");
			}else if(jdbcType==Types.DATE || jdbcType==Types.TIMESTAMP){
				model.put("sorttype","date");
				model.put("unformat","pickDate");
			}else if(c.length()<Short.MAX_VALUE){
				model.put("sorttype","text");
			}
		 	
			colModel.add(model);
		}
		
		
		DataMap data=new DataMap();
		data.put("colNames", colNames);
		data.put("colModel", colModel);
		
		return data;
	}
  
}
