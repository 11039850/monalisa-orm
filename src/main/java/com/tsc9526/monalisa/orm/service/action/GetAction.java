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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.datatable.Page;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class GetAction extends Action{
  
	public GetAction(ActionArgs args) {
		super(args);
	}

	public Response getResponse() {
		if(args.getTable()==null){
			return getTables();
		}else{
			if(args.getSinglePK()!=null){
				return getTableRowBySinglePK();
			}else if(args.getMultiKeys()!=null){
				return getTableRowByMultiKeys();
			}else{
				return getTableRows();
			}
		}
	}

	public Response getTables(){
		DataTable<DataMap> table=new DataTable<DataMap>();
		for(String t:db.getTables()){
			DataMap m=new DataMap();
			
			m.put("table_name",t);
			
			table.add(m);
		}
		return new Response( table );
	}
	 
	public Response getTableRows(){
		Query query=createQuery();
		
		query.add("SELECT ");
		
		List<String> ics=args.getIncludeColumns();
		List<String> exs=args.getExcludeColumns();
		if(ics.size()==0 && exs.size()>0){
			Set<String> es=new HashSet<String>();
			for(String n:exs){
				es.add(Dialect.getRealname(n).toLowerCase());
			}
			
			Record model=createRecord();
			
			for(FGS fgs:model.fields()){
				Column c=fgs.getAnnotation(Column.class);
				
				String cname=c.name();
				if(!es.contains(cname.toLowerCase())){
					ics.add(cname);
				}		
			} 
		}
		
		if(ics.size()>0){
			for(int i=0;i<ics.size();i++){
				String c=ics.get(i);
				if(i>0){
					query.add(", "+c);
				}else{
					query.add( c);
				}
			}
		}else{
			query.add("*");
		}
		query.add(" FROM "+args.getTable());
		
		List<String[]> filters=args.getFilters();
		if(filters.size()>0){
			query.add(" WHERE ");
			for(int i=0;i<filters.size();i++){
				String[] fs=filters.get(i);
				if(i>0){
					query.add(" AND ");
				}
				query.add(fs[0]+" "+fs[1]+" ?",fs[2]);
			}
		}
		
		List<String[]> orderBy=args.getOrderBy();
		if(orderBy.size()>0){
			query.add(" ORDER BY ");
			for(int i=0;i<orderBy.size();i++){
				String[] fs=orderBy.get(i);
				if(i>0){
					query.add(", ");
				}
				query.add(fs[0]+" "+fs[1]);
			}
		}
		
		int limit =args.getLimit();
		int offset=args.getOffset();
		
		int maxLimit=DbProp.PROP_TABLE_DBS_MAX_ROWS.getIntValue(db, 10000);
		if(limit>maxLimit){
			return new Response(400, "Error parameter: limit = "+limit+", it is too bigger than the max value: "+maxLimit+". (OR increase the max value: \"DB.cfg.dbs.max.rows\")");
		}
		
		if(args.isPaging()){
			Page<DataMap> r=query.getPage(limit,offset);
			args.getResp().setHeader("X-Total-Count", ""+r.getTotalRow());
			args.getResp().setHeader("X-Total-Page" , ""+r.getTotalPage());
			
			return new Response(r.getList());
		}else{
			List<DataMap> r=query.getList(limit, offset);
			
			return new Response(r);
		}
		
	}
	 
	public Response getTableRowBySinglePK(){
		Record model=createRecord();
		List<FGS> pks=model.pkFields();
		if(pks.size()==1){
			for(String c:args.getExcludeColumns()){
				model.exclude(c);
			}
	
			for(String c:args.getIncludeColumns()){
				model.include(c);
			}
			
			model.set(pks.get(0).getFieldName(),args.getSinglePK()).load();
			if(model.entity()){
				return new Response(model);
			}else{
				return new Response(404,args.getActionName()
						+" error, primary key not found: /"+args.getDatabase()+"/"+args.getTable()+"/"+args.getSinglePK());
			}
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
	
	public Response getTableRowByMultiKeys(){
		Record model=createRecord();
		
		for(String c:args.getExcludeColumns()){
			model.exclude(c);
		}

		for(String c:args.getIncludeColumns()){
			model.include(c);
		}
		
		Record.Criteria c=model.WHERE();
		for(String[] nv:args.getMultiKeys()){
			if(model.field(nv[0])!=null){
				c.field(nv[0]).eq(nv[1]);
			}else{
				return new Response(400,args.getActionName()+" error, column not found: "+nv[0]+" in the table: "+args.getTable());
			}
		}
		
		Record x=c.SELECT().selectOne();
		if(x!=null){
			return new Response(x);
		}else{
			return new Response(404,args.getActionName()+" error, multi keys not found: "+args.getRequestPath());
		}
	}

}
