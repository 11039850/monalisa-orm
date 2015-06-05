package com.tsc9526.monalisa.core.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tsc9526.monalisa.core.query.partition.DatePartitionTable;

public class MetaPartition implements java.io.Serializable {
	 
	private static final long serialVersionUID = 6730997065415685121L;
	
	private String   tablePrefix;
	
	private String   clazz;
	
	private String[] args;
	
	
	private List<MetaTable> tables=new ArrayList<MetaTable>();
	
	
	public MetaPartition(String partition){
		//log_access_{DatePartitionTable(yyyyMMdd,log_time)}
		try{
			partition=partition.trim();
			
			int p1=partition.indexOf("{");
			int p2=partition.indexOf("(",p1);
			int p3=partition.indexOf(")",p2);
			 
			tablePrefix=partition.substring(0,p1);
			clazz=partition.substring(p1+1,p2).trim();
			
			
			if(clazz.equals("DatePartitionTable")){
				clazz=DatePartitionTable.class.getName();
			}
			
			String parameters=partition.substring(p2+1,p3).trim();
			if(parameters.length()>0){
				String[] ps=parameters.split(",");
				args=new String[ps.length];
				for(int i=0;i<ps.length;i++){
					String x=ps[i].trim();
					if(x.startsWith("\"") || x.startsWith("'")){
						x=x.substring(1);
					}
					if(x.endsWith("\"") || x.endsWith("'")){
						x=x.substring(0,x.length()-1);
					}
					
					args[i]=x;
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException("Invalid partition: "+partition,e);
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
