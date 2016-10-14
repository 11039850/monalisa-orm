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
import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.dao.Delete;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DeleteAction extends Action{
   
	public DeleteAction(ActionArgs args){
		super(args);
	}
	
	public Response getResponse() {
		if(args.getTable()==null){
			return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, missing table, using: /"+args.getPathDatabases()+"/your_table_name");
		}else{
			if(args.getSinglePK()!=null){
				return deleteTableRowBySinglePK();
			}else if(args.getMultiKeys()!=null){
				return deleteTableRowByMultiKeys();
			}else if(args.getFilters().size()>0){
				return deleteTableRowsByFilter();
			}else{
				return deleteTableRowsAll();
			}
		}
	}

	public Response deleteTableRowsByFilter() {
		Query query=createQuery();
		query.add("DELETE FROM "+args.getTable()+" WHERE ");
		 
		for(String[] fs:args.getFilters()){
			if(query.getParameters().size()>0){
				query.add(" AND ");
			}
			query.add(fs[0]+" "+fs[1]+" ?",fs[2]);
		}
		int n=doDeleteByQuery(query);
		
		return new Response(Response.OK,"Delete from table "+args.getTable()+" success: "+n).setData(n);  
	}
	
	public Response deleteTableRowsAll(){
		if(isAllowDeleteAll()){
			int n=doDeleteAll();
			
			return new Response(200,"Delete all from table "+args.getTable()+" success: "+n).setDetail(""+n);
		}else{
			return new Response(Response.REQUEST_FORBIDDEN,"Access forbidden, delete all from table disabled: "+args.getTable());
		}
	}

	public Response deleteTableRowByMultiKeys() {
		Record record=createRecord();
		 
		Record.Criteria c=record.WHERE();
		for(String[] nv:args.getMultiKeys()){
			if(record.field(nv[0])!=null){
				c.field(nv[0]).eq(nv[1]);
			}else{
				return new Response(Response.REQUEST_BAD_PARAMETER,"Column not found: "+nv[0]+" in the table: "+args.getTable());
			}
		}
		
		int n=doDeleteByCriteria(c);
		return new Response(Response.OK,"Delete by multi-keys from table "+args.getTable()+" success: "+n).setData(n); 
	}

	public Response deleteTableRowBySinglePK() {
		Record record=createRecord();
		List<FGS> pks=record.pkFields();
		if(pks.size()==1){
			Delete<Record> rd=record.set(pks.get(0).getFieldName(),args.getSinglePK()).DELETE();
			
			int n=doDeleteByRecord(rd);
			return new Response(Response.OK,"Delete by single primary key: "+args.getSinglePK()+" from table "+args.getTable()+" success: "+n).setData(n); 
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
	
	protected boolean isAllowDeleteAll(){
		return false;
	}
	
	protected int doDeleteByRecord(Delete<Record> record){
		return record.delete();
	}	
	
	protected int doDeleteByCriteria(Record.Criteria criteria){
		return criteria.delete();
	}
	
	protected int doDeleteByQuery(Query query){
		return query.execute();
	}
	
	protected int doDeleteAll(){
		Record record=createRecord();
		return record.DELETE().deleteAll();
	}
}
