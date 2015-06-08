package com.tsc9526.monalisa.core.query.partition;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.query.dao.Model;


public class CreateTableCache{
	private static Map<String, Table> hTables=new ConcurrentHashMap<String, Table>();
	private static Object lock=new Object();
	  
	public static Table getTable(Partition<Model<?>> partition,Model<?> model,Table modelTable) {
		String tableName=partition.getTableName(model);
		
		DBConfig db=model.db();
		String tableKey=db.key()+":"+tableName;
		Table table=hTables.get(tableKey);
		if(table==null){
			synchronized (lock) {
				if(hTables.containsKey(tableKey)==false){
					DataSourceManager dsm=DataSourceManager.getInstance();		
					if(dsm.getDialect(db).createTableIfNotExists(db,tableName)){
						table=new PT(tableName,modelTable);
						hTables.put(tableKey,table);
					}else{
						throw new RuntimeException("Fail create table: "+tableName+", db: "+db.key());
					}
				}else{
					table=hTables.get(tableKey);
				}
			}
		}
		
		return table;		 
	}	
	
	private static class PT implements Table{
		private String tableName;
		private Table modelTable;
		PT(String tableName,Table modelTable){
			this.tableName=tableName;
			this.modelTable=modelTable;
		}
		
		public Class<? extends Annotation> annotationType() {
			return Table.class;
		}
		 
		public String name() {					 
			return tableName;
		}

		public String remarks() {
			return modelTable.remarks();
		}	
	}
}
