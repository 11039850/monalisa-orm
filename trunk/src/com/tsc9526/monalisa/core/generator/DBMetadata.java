package com.tsc9526.monalisa.core.generator;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DataSourceManager;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.tools.CloseQuietly;
import com.tsc9526.monalisa.core.tools.FileHelper;

public class DBMetadata {
	private static Map<String,Map<String, MetaTable>> hDBMetaTables=new ConcurrentHashMap<String,Map<String,MetaTable>>();
	
	public static MetaTable getTable(String projectPath,String dbKey,String tableName){
		if(tableName==null || dbKey==null || tableName.trim().length()<1){
			return null;
		}else{
			Map<String, MetaTable> tables=hDBMetaTables.get(dbKey);
			if(tables==null){
				String metafile=FileHelper.combinePath(projectPath,DBGenerator.PROJECT_TMP_PATH,"metatable/"+dbKey+".meta");		
				File taget=new File(metafile);
				tables=FileHelper.readToObject(taget);
				if(tables!=null){
					hDBMetaTables.put(dbKey, tables);
				}
			}			
			if(tables!=null){
				tableName=tableName.trim().toLowerCase();
				char c=tableName.charAt(0);
				if( !( (c>='a' && c<='z') || c=='_' ) ){
					tableName=tableName.substring(1,tableName.length()-1);
				} 
				return tables.get(tableName);
			}else{
				return null;
			}
		}
	}
	 
	private static Map<String,MetaTable> hRuntimeTables=null;
	private static Object lock=new Object();
	public static MetaTable getMetaTable(String dbKey,String theTableName) {
		try{
			if(hRuntimeTables == null){
				synchronized (lock) {
					if(hRuntimeTables == null){
						hRuntimeTables=loadMetaTables(dbKey);
					}
				}				
			}
			
			for(String key:hRuntimeTables.keySet()){
				if(theTableName.startsWith(key)){
					return hRuntimeTables.get(key);
				}
			}
			
			return null;			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static Map<String,MetaTable> loadMetaTables(String dbKey)throws IOException{			
		String res="/resources."+dbKey.toLowerCase();
		res=res.replace(".","/")+"/create_table.sql";
		
		Map<String,MetaTable> hRuntimeTables=new HashMap<String, MetaTable>();		 
		
		String key_begin="/***CREATE TABLE:";
		String key_end  ="***/";
		
		InputStream in=MetaTable.class.getResourceAsStream(res);
		BufferedReader reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
		
		String line=reader.readLine();
		while(line!=null){
			if(line.startsWith(key_begin) && line.endsWith(key_end)){
				int p=line.indexOf("::");
				
				String       tableName=line.substring(p+2,line.length()-key_end.length()).trim();
				String       tableNameWithoutPartition=line.substring(key_begin.length(),p).trim();
				StringBuffer createSQL=new StringBuffer();
				
				line=reader.readLine();
				while(line!=null){
					createSQL.append(line).append("\r\n");
					 
					line=reader.readLine();
					if(line==null || (line.startsWith(key_begin) && line.endsWith(key_end))){
						break;
					} 						
				}
				
				MetaTable table=new MetaTable(tableName);					
				table.setCreateTable(new CreateTable(tableName,createSQL.toString()));
				hRuntimeTables.put(tableNameWithoutPartition, table);				 
			}else{
				line=reader.readLine();
			}
		}
		reader.close();
		 
		
		return hRuntimeTables;
	}
	
	private DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected String projectPath;
	protected String javaPackage;
	protected DBConfig dbcfg;
	 	 
	protected String catalog;
	protected String schema;
	protected String tableName;
	
	public DBMetadata(String projectPath,String javaPackage,DBConfig dbcfg){
		this.projectPath=projectPath;
		this.javaPackage=javaPackage;
		this.dbcfg=dbcfg;
		  
		catalog=dbcfg.catalog();
		schema=dbcfg.schema();
		tableName=dbcfg.tables();
		
		if(schema==null || schema.length()==0){
			schema=dsm.getDialect(dbcfg).getSchema(dbcfg.url());
		}		
	}
	 
	
	public List<MetaTable> getTables(){
		if(schema==null || schema.length()==0){
			throw new RuntimeException("Database not set in DB annotation schema or url: "+dbcfg.url());
		}
		
		DataSource ds=dbcfg.getDataSource();
		Connection conn=null;
		try{
			conn=ds.getConnection();
 			DatabaseMetaData metadata=conn.getMetaData();
			List<MetaTable> tables=getTables(metadata);
		    for(MetaTable table:tables){
		    	getTableColumns(metadata, table);
		    	getTableKeyColumns(metadata, table);	
		    	
		    	table.setJavaPackage(javaPackage);
		    }	
		    
		    for(MetaTable table:tables){
		    	if(table.getPartition()!=null){
		    		dsm.getDialect(dbcfg).loadMetaTableDetails(dbcfg, table); 
		    	}
		    }
		    
		    String dbKey=dbcfg.key();		  
		    Map<String, MetaTable> hTables=hDBMetaTables.get(dbKey);
		    if(hTables==null){
		    	hTables=new ConcurrentHashMap<String, MetaTable>();
		    	hDBMetaTables.put(dbKey, hTables);
		    }else{
		    	hTables.clear();
		    }		    
		    		    
		    for(MetaTable table:tables){
		    	hTables.put(table.getName().toLowerCase(),table);
		    }		    
		    
		    if(projectPath!=null){
		    	cacheTables(hTables);
		    }
		    
			return tables;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			CloseQuietly.close(conn);			 
		}
	}
	
	private void cacheTables(Map<String, MetaTable> hTables)throws IOException {
		ByteArrayOutputStream bufArrayOutputStream=new ByteArrayOutputStream();
		ObjectOutputStream outputStream=new ObjectOutputStream(bufArrayOutputStream);
		outputStream.writeObject(hTables);
		outputStream.flush();
		
		String metafile=FileHelper.combinePath(projectPath,DBGenerator.PROJECT_TMP_PATH,"metatable/"+dbcfg.key()+".meta");		
		File taget=new File(metafile);
		FileHelper.write(taget, bufArrayOutputStream.toByteArray());  			 
	}

	private List<MetaPartition> getPartitions(){
		List<MetaPartition> partitions=new ArrayList<MetaPartition>();
		String pts=dbcfg.partitionTables();
		if(pts!=null && pts.trim().length()>0){
			String[] ps=pts.trim().split(";");
			for(String p:ps){
				p=p.trim();
				if(p.length()>0){
					partitions.add(new MetaPartition(p));
				}
			}
		}
		return partitions;
	}
	
	private MetaPartition findPartition(List<MetaPartition> partitions,MetaTable table){
		for(MetaPartition p:partitions){
			String prefix=p.getTablePrefix().toLowerCase();
			String name=table.getName().toLowerCase();
			if(name.startsWith(prefix)){
				return p;
			}			
		}
		
		return null;
	}
	
	protected List<MetaTable> getTables(DatabaseMetaData metadata)throws SQLException{
		List<MetaPartition> partitions=getPartitions();
		
		List<MetaTable> tables=new ArrayList<MetaTable>();
		ResultSet rs=metadata.getTables(catalog, schema, tableName, new String[]{"TABLE"});			 
		while(rs.next()){
			MetaTable table=new MetaTable();		 	
			table.setName(rs.getString("TABLE_NAME"));
			table.setRemarks(rs.getString("REMARKS"));
			
			MetaPartition partition=findPartition(partitions,table);
			if(partition!=null){
				partition.addTable(table);
			}else{
				tables.add(table);
			}
		}
		rs.close();
		
		processTableMapping(tables);
		
		for(MetaPartition p:partitions){
			MetaTable table=p.getTable();
			if(table!=null){
				tables.add(table);
			}
		}
		
		return tables;
	}
	
	private void processTableMapping(List<MetaTable> tables){
		String mapping=dbcfg.mapping().trim();
		if(mapping.length()>0){
			Map<String,String> hTableMapping=new HashMap<String,String>();
			for(String vs:mapping.split(";")){
				String[] nv=vs.split("=");
				hTableMapping.put(nv[0].trim().toLowerCase(),nv[1]);
			}
			
			for(MetaTable table:tables){
				String javaName=hTableMapping.get(table.getName().toLowerCase());
				if(javaName!=null){
					table.setJavaName(javaName);
				}
			}
		}
	}
	
	protected void getTableKeyColumns(DatabaseMetaData metadata,MetaTable table)throws SQLException{
		Map<Short, MetaColumn> keyColumns = new TreeMap<Short, MetaColumn>();
	    ResultSet rs = metadata.getPrimaryKeys(catalog, schema, table.getName());
	    while(rs.next()){
	    	String columnName = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
	    	short keyseq = rs.getShort("KEY_SEQ"); //$NON-NLS-1$
	    	
	    	MetaColumn column=table.getColumn(columnName);
	    	column.setKey(true);
	    	keyColumns.put(keyseq, column);
	    }
	    for(MetaColumn c:keyColumns.values()){
	    	table.addKeyColumn(c);
	    }
	    rs.close();
	}
	
	
	protected void getTableColumns(DatabaseMetaData metadata,MetaTable table)throws SQLException{
		ResultSet rs = metadata.getColumns(catalog, schema,table.getName(), null);
	    
	    while(rs.next()){
	    	boolean auto=false;
	    	String ai=(""+rs.getString("IS_AUTOINCREMENT")).toUpperCase();
	    	if("Y".equals(ai) || "YES".equals(ai) || "TRUE".equals(ai) || "1".equals(ai)){			    		 
	    		auto=true;
	    	}			    				    	
	    	  
	    	MetaColumn column=new MetaColumn();			    	 
	    	column.setName(rs.getString("COLUMN_NAME"));
	    	column.setValue(rs.getString("COLUMN_DEF"));
	    	column.setKey(false);
	    	column.setJdbcType(rs.getInt("DATA_TYPE"));
	    	column.setLength(rs.getInt("COLUMN_SIZE"));
	    	column.setNotnull(rs.getInt("NULLABLE") != DatabaseMetaData.columnNullable);
	    	column.setRemarks(rs.getString("REMARKS"));
	    	column.setAuto(auto);
	    	
	    	column.setTable(table);
	    	
	    	table.addColumn(column);
	    	
		}
	    rs.close();		 
	}
	 
	public DBConfig getDb() {
		return dbcfg;
	}		 
}
