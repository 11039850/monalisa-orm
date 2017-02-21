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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.service.RequestParameter;
import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.service.args.ModelArgs;
import com.tsc9526.monalisa.service.args.QueryArgs;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class GetAction extends Action{
  
	public GetAction(ModelArgs args) {
		super(args);
	}

	public Response getResponse() {
		if(args.getTables()!=null){
			return getTablesRows();
		}else{
			if(args.getTable()!=null){
				String table=args.getTable();
				int p=table.indexOf(RequestParameter.MS);
				if(p>0){
					return getTableQuery(table.substring(0,p),table.substring(p+RequestParameter.MS.length()));
				}else{
					if(args.getSinglePK()!=null){
						return getTableRowBySinglePK();
					}else if(args.getMultiKeys()!=null){
						return getTableRowByMultiKeys();
					}else{
						return getTableRows();
					}
				}
			}else{
				return new Response(Response.REQUEST_BAD_PARAMETER,"Missing table, add request parameter: method=HEAD OR: /"+args.getPathDatabases()+"/your_table_name");
			}
		}
	}
	
	public Response getTableQuery(String className,String methodName){
		try{
			Class<?> clazz=Class.forName(className);
			
			Method x=clazz.getMethod(methodName, QueryArgs.class); 
			  
			Object r=x.invoke(clazz.newInstance(), args);

			return new Response(r);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get data using query multi-tables
	 * @return rows
	 */
	public Response getTablesRows(){
		List<String> ics=args.getIncludeColumns();
		
		Query query=createQuery();
		
		query.add("SELECT ");
		
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
		
		query.add(" FROM ");
		String[] tables=args.getTables();
		for(int i=0;i<tables.length;i++){
			if(i>0){
				query.add(", "+tables[i]);
			}else{
				query.add( tables[i] );
			}
		}
		
		query.add(" WHERE ");
		String[] joins =args.getJoins();
		if(joins!=null){
			for(int i=0;i<joins.length;i++){
				if(i>0){
					query.add(" AND "+joins[i]);
				}else{
					query.add( joins[i] );
				}
			}
		}else{
			//TODO: find table joins
			query.add(" 1=1 ");
		}
		
		appendFilters(query,true);
		
		query.addIf(args.getOrders().size()>0, " ORDER BY "+args.getOrderBy());
		 
		
		return createQueryResponse(query,null);
	}
	   
	/**
	 * Get the table rows
	 * 
	 * @return list rows of the table
	 */
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
			
			Record record=createRecord();
			
			for(FGS fgs:record.fields()){
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

			appendFilters(query,false);
		}
		
		query.addIf(args.getOrders().size()>0, " ORDER BY "+args.getOrderBy());
	 	 
		return createQueryResponse(query,args.getTable());
	}
	
	private Response createQueryResponse(Query query,String tableName){
		int maxRows=getMaxRows(tableName);
		
		int pageSize = args.getLimit();
		int offset   = args.getOffset();
		if(pageSize>maxRows){
			return new Response(Response.REQUEST_BAD_PARAMETER, "Error: rows = "+pageSize+", it is too bigger than the max value: "+maxRows+". (OR increase the max value: \"DB.cfg.dbs.max.rows\")");
		}
		  
		if(args.isPaging()){
			Page<DataMap> page=query.getPage(pageSize,offset);
		
			return doGetPage(page);
		}else{
			DataTable<DataMap> table=query.getList(pageSize, offset);
			
			return doGetTable(table);
		}
	}
	 
	/**
	 * Get row of the table by single primary key
	 * @return 200: one row,  404: NOT found
	 */
	public Response getTableRowBySinglePK(){
		Record record=createRecord();
		List<FGS> pks=record.pkFields();
		if(pks.size()==1){
			for(String c:args.getExcludeColumns()){
				record.exclude(c);
			}
	
			for(String c:args.getIncludeColumns()){
				record.include(c);
			}
			
			record.set(pks.get(0).getFieldName(),args.getSinglePK()).load();
			if(record.entity()){
				return doGetRecord(record);
			}else{
				return new Response(Response.REQUEST_NOT_FOUND,"Primary key not found: /"+args.getPathDatabases()+"/"+args.getPathTables()+"/"+args.getSinglePK());
			}
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
	
	public Response getTableRowByMultiKeys(){
		Record record=createRecord();
		
		for(String c:args.getExcludeColumns()){
			record.exclude(c);
		}

		for(String c:args.getIncludeColumns()){
			record.include(c);
		}
		
		Record.Criteria c=record.WHERE();
		for(String[] nv:args.getMultiKeys()){
			if(record.field(nv[0])!=null){
				c.field(nv[0]).eq(nv[1]);
			}else{
				return new Response(Response.REQUEST_BAD_PARAMETER,args.getActionName()+" error, column not found: "+nv[0]+" in the table: "+args.getTable());
			}
		}
		
		Record x=c.SELECT().selectOne();
		if(x!=null){
			return doGetRecord(x);
		}else{
			return new Response(Response.REQUEST_NOT_FOUND,args.getActionName()+" error, multi keys not found: "+args.getPathAction());
		}
	}
	
	protected int getMaxRows(String table){
		int maxLimit=DbProp.PROP_TABLE_DBS_MAX_ROWS.getIntValue(db,table, 10000);
		return maxLimit;
	}
	
	protected Response doGetRecord(Record r){
		return new Response(r);
	}
	
	protected Response doGetTable(DataTable<DataMap> table){
		return new Response(table).setMessage("OK: "+table.size());
	}
	
	protected Response doGetPage(Page<DataMap> page){
		return new Response(page).setMessage("OK: "+page.getRecords());
	}
}
