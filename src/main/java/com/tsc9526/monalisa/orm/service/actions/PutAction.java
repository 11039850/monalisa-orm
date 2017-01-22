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

import java.lang.reflect.Method;
import java.util.List;

import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.RequestParameter;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PutAction extends Action{
  
	public PutAction(ModelArgs args) {
		super(args);
	}

	public Response getResponse() {
		if(args.getTables()!=null){
			return insertTablesRows();
		}else if(args.getTable()!=null){
			String table=args.getTable();
			int p=table.indexOf(RequestParameter.MS);
			if(p>0){
				return updateTableQuery(table.substring(0,p),table.substring(p+2));
			}else{
				if(args.getSinglePK()!=null){
					return updateTableRowBySinglePk();
				}else if(args.getMultiKeys()!=null){
					return updateTableRowByMultiKeys();
				}else{
					return insertTableRows();
				}
			}
		}else{		
			return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, missing table, using: /"+args.getPathDatabases()+"/your_table_name");
		}
	}
	 
	protected Response updateTableQuery(String className,String methodName){
		try{
			Class<?> clazz=Class.forName(className);
			
			Method x=clazz.getMethod(methodName, ModelArgs.class); 
			  
			Object r=x.invoke(clazz.newInstance(), args);

			return new Response(r);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected Response updateTableRowBySinglePk(){
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
	
	protected Response updateTableRowByMultiKeys(){
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
	
	protected Response insertTableRows(){
		return new Response(Response.REQUEST_FORBIDDEN,"Access forbidden: PUT, using POST instead.");
	} 
	
	protected Response insertTablesRows(){
		return new Response(Response.REQUEST_FORBIDDEN,"Access forbidden: PUT, using POST instead.");
	}
	
	protected void parseRecord(Record record){
		record.parse(args.getReq());
	}
	
	protected int doUpdateRecord(Record record){
		return record.update();
	}

}
