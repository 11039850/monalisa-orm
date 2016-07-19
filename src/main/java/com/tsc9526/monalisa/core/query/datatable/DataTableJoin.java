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
package com.tsc9526.monalisa.core.query.datatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class DataTableJoin {
	private static String[] getJoinFields(DataTable<?> leftTable, DataTable<?> rightTable) {
		Set<String> cs=new LinkedHashSet<String>();
		
		for(DataColumn c:leftTable.getHeaders()){
			cs.add(c.getName().toLowerCase());
		}
		
		
		List<String> joinFieldNames=new ArrayList<String>();
		for(DataColumn c:rightTable.getHeaders()){
			String name=c.getName().toLowerCase();
			if(cs.contains(name)){
				joinFieldNames.add(name);
			}
		}
		
		return joinFieldNames.toArray(new String[joinFieldNames.size()]);
	}
	
	protected DataTable<DataMap> leftTable;
	protected DataTable<DataMap> rightTable;
	protected String[] leftJoinFields ;
	protected String[] rightJoinFields;
	
	protected DataMap leftKeyMapping =new DataMap();
	protected DataMap rightKeyMapping=new DataMap();
	
	protected Map<String, Integer> xs = new HashMap<String, Integer>();
 	 
	protected DataTable<DataMap> allTable=new DataTable<DataMap>();
	protected List<DataColumn>  allHeader=new ArrayList<DataColumn>();
	  
	public DataTableJoin(DataTable<?> leftTable,DataTable<?> rightTable, String... joinFieldNames){
		this.leftTable =leftTable.as(DataMap.class);
		this.rightTable=rightTable.as(DataMap.class);
		
		if(joinFieldNames.length==0){
			this.leftJoinFields =getJoinFields(leftTable,rightTable);
			this.rightJoinFields=leftJoinFields;
		}else if(joinFieldNames.length==1){
			this.leftJoinFields =joinFieldNames[0].split(",");
			this.rightJoinFields=leftJoinFields;
		}else{
			this.leftJoinFields =joinFieldNames[0].split(",");
			this.rightJoinFields=joinFieldNames[1].split(",");
		}
		
		setup();
	}
	 	
	 
	protected void setup(){
		setupHeaders(allHeader,this.leftTable,leftKeyMapping,xs);
		setupHeaders(allHeader,this.rightTable,rightKeyMapping,xs);
	}
	
	
	protected DataTable<DataMap> doLeftJoin(){
		DataTable<DataMap> allTable=new DataTable<DataMap>().setHeaders(allHeader);
		
		Map<String,List<DataMap>> rightValues=toMapList(rightTable,rightJoinFields);
		 
		for(DataMap x:leftTable){
			String key=getDataMapKey(x,leftJoinFields);
			List<DataMap> rms=rightValues.get(key);
			if(rms!=null){
				for(DataMap r:rms){
					DataMap m=new DataMap();
					
					addDataMap(m,x,leftKeyMapping);
					addDataMap(m,r,rightKeyMapping);
					
					allTable.add(m);
				}
			}else{
				DataMap m=new DataMap();
				addDataMap(m,x,leftKeyMapping);
				allTable.add(m);
			}
		}
		return allTable;
	}
	
	protected DataTable<DataMap> doInnerJoin(){
		DataTable<DataMap> allTable=new DataTable<DataMap>().setHeaders(allHeader);
		
		Map<String,List<DataMap>> rightValues=toMapList(rightTable,rightJoinFields);
		 
		for(DataMap x:leftTable){
			String key=getDataMapKey(x,leftJoinFields);
			List<DataMap> rms=rightValues.get(key);
			if(rms!=null){
				for(DataMap r:rms){
					DataMap m=new DataMap();
					
					addDataMap(m,x,leftKeyMapping);
					addDataMap(m,r,rightKeyMapping);
					
					allTable.add(m);
				}
			}
		}
		return allTable;
	}
	
	protected DataTable<DataMap> doFullJoin(){
		DataTable<DataMap> allTable=new DataTable<DataMap>().setHeaders(allHeader);
		 
		for(DataMap x:leftTable){
			for(DataMap y:rightTable){
				DataMap m=new DataMap();
				
				addDataMap(m,x,leftKeyMapping);
				addDataMap(m,y,rightKeyMapping);
				
				allTable.add(m);
			}
		}
		 
		return allTable;
	}
	
	protected DataTable<DataMap> doRightJoin(){
		DataTable<DataMap> allTable=new DataTable<DataMap>().setHeaders(allHeader);
		
		Map<String,List<DataMap>> leftValues=toMapList(leftTable,leftJoinFields);
		 
		for(DataMap x:rightTable){
			String key=getDataMapKey(x,rightJoinFields);
			List<DataMap> rms=leftValues.get(key);
			if(rms!=null){
				for(DataMap r:rms){
					DataMap m=new DataMap();
					
					addDataMap(m,r,leftKeyMapping);
					addDataMap(m,x,rightKeyMapping);
					
					allTable.add(m);
				}
			}else{
				DataMap m=new DataMap();
				addDataMap(m,x,rightKeyMapping);
				allTable.add(m);
			}
		}
		return allTable;
	}
	
	protected void setupHeaders(List<DataColumn> allHeader,DataTable<DataMap> table,DataMap keyMapping,Map<String, Integer> xs){
		for(DataColumn c:table.getHeaders()){
			String name=c.getName();
			String lowername=name.toLowerCase();
			Integer n = xs.get(lowername);
			if (n != null) {
				name = name + n;

				xs.put(lowername, n + 1);
			} else {
				xs.put(lowername, 1);
			}
			
			keyMapping.put(c.getName(),name);
			
			allHeader.add(new DataColumn(name));
		}
	}
	
	protected void addDataMap(DataMap target,DataMap data,DataMap keyMapping){
		for(String key:data.keySet()){
			Object value=data.get(key);
			
			String targetKey=(String)keyMapping.get(key);
			
			target.put(targetKey, value);
		}
	}
	
	protected Map<String,List<DataMap>> toMapList(DataTable<DataMap> table, String[] keyFields){
		Map<String,List<DataMap>> rs=new LinkedHashMap<String,List<DataMap>>();
		
		 
		for(DataMap x:table){
			String key=getDataMapKey(x,keyFields); 
			
			List<DataMap> list=rs.get(key);
			if(list==null){
				list=new ArrayList<DataMap>();
				rs.put(key, list);
			}
			
			list.add(x);
			
		}
		
		return rs;
	}
	
	protected String getDataMapKey(DataMap x,String[] keyFields){
		if(keyFields.length==0){
			return "#"+x.toString().toLowerCase();
		}else{
			String key="@";
			for(String n:keyFields){
				if(key.length()>1){
					key+="&";
				}
				key+=key+"="+x.getString(n);
			}
			
			return key.toLowerCase();
		}
	}
	
}
