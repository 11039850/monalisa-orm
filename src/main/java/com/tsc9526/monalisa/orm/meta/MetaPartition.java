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
package com.tsc9526.monalisa.orm.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.partition.DatePartitionTable;
import com.tsc9526.monalisa.orm.partition.Partition;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.misc.MelpException;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MetaPartition implements java.io.Serializable {
	 
	private static final long serialVersionUID = 6730997065415685121L;
	
	public static List<MetaPartition> parsePartitions(String pts){
		try{
			List<MetaPartition> metaPartitions=new ArrayList<MetaPartition>();
			
			if(pts!=null && pts.trim().length()>0){
				pts=pts.trim();
				if(pts.startsWith("[") || pts.startsWith("{")){
					JsonElement je=new JsonParser().parse(pts);
					if(je.isJsonArray()){
						JsonArray array=je.getAsJsonArray();
						for(int i=0;i<array.size();i++){
							MetaPartition mp=parseFromJson(array.get(i).getAsJsonObject());
							metaPartitions.add(mp);
						}
					}else{
						MetaPartition mp=parseFromJson(je.getAsJsonObject());
						metaPartitions.add(mp);
					}
				}else{
					String[] ps=pts.split(";");
					for(String p:ps){
						p=p.trim();
						if(p.length()>0){
							MetaPartition mp=parseFromString(p);
							
							metaPartitions.add(mp);
						}
					}					
				}		
			}
			return metaPartitions;		
		}catch(Exception e){
			throw new RuntimeException("Invalid partition: "+pts,e);
		}		
	}
	
	public static MetaPartition parseFromJson(JsonObject json){
		MetaPartition mp=new MetaPartition();
		
		mp.tablePrefix = json.get("prefix").getAsString();
		mp.clazz       = json.get("class").getAsString();
		
		JsonArray args=json.get("args").getAsJsonArray();
		mp.args=new String[args.size()];
		if(args.size()>0){
			for(int i=0;i<args.size();i++){
				mp.args[i]=args.get(i).getAsString();
			}
		}
		
		return mp;
	}

	public static MetaPartition parseFromString(String partition){
		//log_access_{DatePartitionTable(yyyyMMdd,log_time)}
		
		MetaPartition mp=new MetaPartition();
		
		int p1=partition.indexOf("{");
		int p2=partition.indexOf("(",p1);
		int p3=partition.indexOf(")",p2);
		 
		mp.tablePrefix=partition.substring(0,p1);
		mp.clazz=partition.substring(p1+1,p2).trim();
		
		
		if(mp.clazz.equals("DatePartitionTable")){
			mp.clazz=DatePartitionTable.class.getName();
		}
		
		String parameters=partition.substring(p2+1,p3).trim();
		if(parameters.length()>0){
			String[] ps=parameters.split(",");
			mp.args=new String[ps.length];
			for(int i=0;i<ps.length;i++){
				String x=ps[i].trim();
				if(x.startsWith("\"") || x.startsWith("'")){
					x=x.substring(1);
				}
				if(x.endsWith("\"") || x.endsWith("'")){
					x=x.substring(0,x.length()-1);
				}
				
				mp.args[i]=x;
			}
		}
		
		return mp;
	}
	
	private String   tablePrefix;
	
	private String   clazz;
	
	private String[] args;
	 
	private List<MetaTable> tables=new ArrayList<MetaTable>();
	
	private Partition<Model<?>> partition;
	
	public MetaPartition(){			
	}
	
	@SuppressWarnings("unchecked")
	public Partition<Model<?>> getPartition(){
		try{
			if(partition==null){
				partition=(Partition<Model<?>>)MelpClass.forName(getClazz()).newInstance();
			}
			return partition;
		}catch(Exception e){
			return MelpException.throwRuntimeException(e);
		}		
	}
	
	
	public void addTable(MetaTable table){
		tables.add(table);
		
		table.setPartition(this);
		
		Collections.sort(tables,new Comparator<MetaTable>(){
 			public int compare(MetaTable o1, MetaTable o2) {
 				int r=o1.getName().compareTo(o2.getName());
 				return -r;
			}			
		});
	}
	
	public MetaTable getTable(){
		if(tables.size()>0){
			MetaTable table= tables.get(0);			 
			return table;
		}else{
			return null;
		}
	}
	
	
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String partitionPrefix) {
		this.tablePrefix = partitionPrefix;
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String partitionClass) {
		this.clazz = partitionClass;
	}
	
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] partitionArgs) {
		this.args = partitionArgs;
	}
}
