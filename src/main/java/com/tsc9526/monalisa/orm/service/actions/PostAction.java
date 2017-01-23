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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.args.ModelArgs;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ModelHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PostAction extends PutAction{
  
	public PostAction(ModelArgs args) {
		super(args);
	} 
	 
	public Response insertTablesRows(){
		List<Record> all=new ArrayList<Record>();
		
		for(String table:args.getTables()){
			String tb=Dialect.getRealname(table);
			
			Record tpl=db.createRecord(tb);
			List<Record> records=ModelHelper.ServletRequestModelParser.parseModels(tpl, args.getReq(),getTableMapping(tb));
			
			if(records.size()==0){
				return insertTableNoData(table);
			}
			
			all.addAll(records);
		}
		 
		int[] rs= doBatchRecordInsert(all);
		
		int n=0;
		for(int x:rs){
			n+=x;
		}
		
		Response r=new Response();
		r.setMessage("Insert tables: "+Arrays.toString(args.getTables())+" ok: "+n);
	 	
		DataTable<DataMap> data=new DataTable<DataMap>();
		for(int i=0;i<rs.length;i++){
			DataMap map=new DataMap();
			map.put("table",Dialect.getRealname(all.get(i).table().name()));
			map.put("rows",rs[i]);
			 
			FGS fgs=all.get(0).autoField();
			Column c=fgs==null?null:fgs.getAnnotation(Column.class);
			
			DataMap entity=new DataMap();
			if(fgs!=null){
				entity.put(c.name(), fgs.getObject(all.get(i)));
				
				map.put("entity", entity);
			}
			
			data.add(map);
			
			r.setData(data);
		}
		
		return r;
	}
	
	public Response insertTableRows(){
		Record tpl=createRecord();
		
		List<Record> records=ModelHelper.ServletRequestModelParser.parseModels(tpl, args.getReq());
		if(records.size()==1){
			return insertTableRow(records.get(0));
		}else if(records.size()>1){
			return insertTableRows(records);
		}else{
			return insertTableNoData(args.getTable());
		}
	}
	
	public Response insertTableRow(Record m){
		int n=doInsertRecord(m);
		 
		Response r=new Response();
		r.setMessage("Insert table: "+args.getTable()+" ok: "+n);
	 	
		DataMap map=new DataMap();
		map.put("rows",n);
		
		DataMap entity=new DataMap();
		FGS fgs=m.autoField();
		if(fgs!=null){
			Column c=fgs.getAnnotation(Column.class);
			entity.put(c.name(), fgs.getObject(m));
			map.put("entity", entity);
		}
		
		r.setData(map);
		
		return r;
	}
	
	public Response insertTableRows(List<Record> records){
		int[] rs= doBatchRecordInsert(records);
		
		int n=0;
		for(int x:rs){
			n+=x;
		}
		
		FGS fgs=records.get(0).autoField();
		Column c=fgs==null?null:fgs.getAnnotation(Column.class);
		
		Response r=new Response();
		r.setMessage("Insert table: "+args.getTable()+" ok: "+n);
		
		DataTable<DataMap> data=new DataTable<DataMap>();
		for(int i=0;i<rs.length;i++){
			DataMap map=new DataMap();
			map.put("rows",rs[i]);
			map.put("table",Dialect.getRealname(args.getTable()));
			DataMap entity=new DataMap();
			if(fgs!=null){
				entity.put(c.name(), fgs.getObject(records.get(i)));
				
				map.put("entity", entity);
			}
			
			data.add(map);
			
			r.setData(data);
		}
		
		return r;
	}
	
	
	protected int doInsertRecord(Record record){
		return record.save();
	}
	
	protected int[] doBatchRecordInsert(List<Record> records){
		int[] rs= db.batchInsert(records);
		return rs;
	}
	
	
	protected Response insertTableNoData(String table){
		return new Response(Response.REQUEST_BAD_PARAMETER, "No post data found for table: "+table);
	}
	
}
