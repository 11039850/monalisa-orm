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
package com.tsc9526.monalisa.orm.service.action;

import java.util.List;

import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.Response;
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
		if(args.getTable()==null){
			return new Response(400,args.getActionName()+" error, missing table, using: /"+args.getDatabase()+"/your_table_name");
		}else{
			if(args.getSinglePK()!=null){
				return putTableRowBySinglePk();
			}else if(args.getMultiKeys()!=null){
				return putTableRowByMultiKeys();
			}else{
				return putTableRows();
			}
		}
	}
	
	public Response putTableRows(){
		return new Response(400,args.getActionName()+" error, missing table primary key, using: /"+args.getDatabase()+"/your_table_name/pk1=v1/pk2=v2...");
	} 
	
	public Response putTableRowBySinglePk(){
		Record model=createRecord();
		List<FGS> pks=model.pkFields();
		if(pks.size()==1){
			parseModel(model);
			
			model.set(pks.get(0).getFieldName(),args.getSinglePK());
			int n=model.update();
			return new Response(200,args.getActionName()+" by single primary key:"+args.getSinglePK()+" to table "+args.getTable()+" success: "+n).setData(n); 
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append(args.getActionName()+" error, table: "+args.getTable()+" primary key has more than one columns");
			sb.append(", change the request's path to: /"+args.getDatabase()+"/"+args.getTable());
			for(FGS fgs:pks){
				sb.append("/").append(fgs.getFieldName()).append("=xxx");
			}
			return new Response(400,sb.toString());
		}
	}
	
	public Response putTableRowByMultiKeys(){
		Record model=createRecord();
		
		parseModel(model);
		  
		for(String[] nv:args.getMultiKeys()){
			if(model.field(nv[0])!=null){
				model.set(nv[0], nv[1]);
			}else{
				return new Response(400,args.getActionName()+" error, column not found: "+nv[0]+" in the table: "+args.getTable());
			}
		}
		
		int n=model.update();
		return new Response(200,"Delete by multi-keys from table "+args.getTable()+" success: "+n).setData(n); 
	}
	
	protected void parseModel(Record model){
		model.parse(args.getReq());
	}

}
