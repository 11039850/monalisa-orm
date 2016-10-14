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
package com.tsc9526.monalisa.http.action;

import java.util.List;

import com.tsc9526.monalisa.http.Response;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PutAction extends Action{
  
	public PutAction(ActionArgs args) {
		super(args);
	}

	public Response getResponse() {
		if(args.getTables()!=null){
			return updateMultiTableRows();
		}else if(args.getTable()!=null){	
			if(args.getSinglePK()!=null){
				return updateTableRowBySinglePk();
			}else if(args.getMultiKeys()!=null){
				return updateTableRowByMultiKeys();
			}else{
				return updateTableRows();
			}
		}else{		
			return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, missing table, using: /"+args.getPathDatabases()+"/your_table_name");
		}
	}
	
	public Response updateTableRows(){
		return new Response(Response.REQUEST_FORBIDDEN,"Access forbidden: "+args.getActionName()+", using POST instead.");
	} 
	
	public Response updateMultiTableRows(){
		return new Response(Response.REQUEST_FORBIDDEN,"Access forbidden: "+args.getActionName()+", using POST instead.");
	}
	
	public Response updateTableRowBySinglePk(){
		Record record=createRecord();
		List<FGS> pks=record.pkFields();
		if(pks.size()==1){
			parseRecord(record);
			
			if(record.changedFields().size()==0){
				return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, no record found!"); 
			}
			
			record.set(pks.get(0).getFieldName(),args.getSinglePK());
			int n=doUpdateRecord(record);
			return new Response(Response.OK,"Insert table: "+args.getTable()+" ok: "+n).setData(n); 
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append(args.getActionName()+" error, table: "+args.getTable()+" primary key has more than one columns");
			sb.append(", change the request's path to: /"+args.getPathDatabases()+"/"+args.getPathTables());
			for(FGS fgs:pks){
				sb.append("/").append(fgs.getFieldName()).append("=xxx");
			}
			return new Response(Response.REQUEST_BAD_PARAMETER,sb.toString());
		}
	}
	
	public Response updateTableRowByMultiKeys(){
		Record record=createRecord();
		
		parseRecord(record);
		
		if(record.changedFields().size()==0){
			return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, no record found!"); 
		}else{
			for(String[] nv:args.getMultiKeys()){
				if(record.field(nv[0])!=null){
					record.set(nv[0], nv[1]);
				}else{
					return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, column not found: "+nv[0]+" in the table: "+args.getTable());
				}
			}
			
			int n=doUpdateRecord(record);
			return new Response(Response.OK,"Insert table: "+args.getTable()+" ok: "+n).setData(n); 
		} 
	}
	
	protected void parseRecord(Record record){
		record.parse(args.getReq());
	}
	
	protected int doUpdateRecord(Record record){
		return record.update();
	}

}
