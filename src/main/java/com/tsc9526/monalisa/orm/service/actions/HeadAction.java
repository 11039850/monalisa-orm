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
package com.tsc9526.monalisa.orm.service.actions;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.TypeHelper;

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
		}else{		
			return new Response(Response.REQUEST_BAD_PARAMETER,"Head error, missing table, using: /"+args.getPathDatabases()+"/your_table_name");
		}	 
	}
	
	protected Response getTableModel(){
		Record record=createRecord();
		
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
			if(TypeHelper.isNumber(jdbcType)){
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
		
		Response response=new Response(data);
		return response;
	}
  
}
