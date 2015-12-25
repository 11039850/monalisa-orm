package com.tsc9526.monalisa.core.query.partition;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tsc9526.monalisa.core.annotation.Index;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.generator.DBMetadata;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.meta.MetaTable.TableType;
import com.tsc9526.monalisa.core.query.model.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CreateTableCache{
	private static Map<String, Table> hTables=new ConcurrentHashMap<String, Table>();
	private static Object lock=new Object();
	  
	public static Table getTable(MetaPartition mp,Model<?> model,Table modelTable) {
		Partition<Model<?>> partition=mp.getPartition();
		
		String tableName=partition.getTableName(mp,model);
		
		DBConfig db=model.db();
		String tableKey=db.getKey()+":"+tableName;
		Table table=hTables.get(tableKey);
		if(table==null){
			synchronized (lock) {
				if(hTables.containsKey(tableKey)==false){					 
					String tablePrefix=mp.getTablePrefix();
					MetaTable metaTable=DBMetadata.getMetaTable(db.getKey(), tablePrefix);
					if(metaTable==null || metaTable.getCreateTable()==null){
						throw new RuntimeException("Fail create table: "+tableName+", db: "+db.getKey()+", MetaTable not found: "+tablePrefix);
					}
				   
					try{
						CreateTable createTable=metaTable.getCreateTable().createTable(TableType.PARTITION,tableName);
						
						db.getDialect().createTable(db, createTable);
										 
						table=createTable(tableName,modelTable);
						hTables.put(tableKey,table);
					}catch(Exception e){
						throw new RuntimeException("Fail create table: "+tableName+", db: "+db.getKey(),e);
					}
				}else{
					table=hTables.get(tableKey);
				}
			}
		}		
		return table;		 
	}	
	
	public static Table createTable(final String tableName,final Table modelTable){
		return new Table(){  
			public String name() {					 
				return tableName;
			}
			
			public String value() {					 
				return tableName;
			}
	 			
			public String remarks() {
				return modelTable.remarks();
			}
			
			public String[] primaryKeys(){
				return modelTable.primaryKeys();
			}
			
			public Index[] indexes(){
				return modelTable.indexes();
			}
			
			public Class<? extends Annotation> annotationType() {
				return Table.class;
			}
		};
	}
}
