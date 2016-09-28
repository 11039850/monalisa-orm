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

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ModelHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class PostAction extends PutAction{
  
	public PostAction(ActionArgs args) {
		super(args);
	} 
	
	public Response postTableRows(){
		Record tpl=createRecord();
		
		List<Model<?>> models=ModelHelper.ServletRequestModelParser.parseModels(tpl, args.getReq());
		if(models.size()==1){
		
		
		}else if(models.size()>1){
			
		}else{
			return postTableNoData();
		}
		
		
		return null;
	}
	
	public Response postTableRow(Model<?> m){
		int n=m.save();
		 
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
	
	public Response postTableRows(List<Model<?>> models){
		int[] rs= db.batchInsert(models);
		
		int n=0;
		for(int x:rs){
			n+=x;
		}
		
		FGS fgs=models.get(0).autoField();
		Column c=fgs==null?null:fgs.getAnnotation(Column.class);
		
		Response r=new Response();
		r.setMessage("Insert table: "+args.getTable()+" ok: "+n);
		
		DataTable<DataMap> data=new DataTable<DataMap>();
		for(int i=0;i<rs.length;i++){
			DataMap map=new DataMap();
			map.put("rows",rs[i]);
			 
			DataMap entity=new DataMap();
			if(fgs!=null){
				entity.put(c.name(), fgs.getObject(models.get(i)));
				
				map.put("entity", entity);
			}
			
			r.setData(data);
		}
		
		return r;
	}
	
	public Response postTableNoData(){
		return new Response(400, "No post data found for table: "+args.getTable());
	}
}
