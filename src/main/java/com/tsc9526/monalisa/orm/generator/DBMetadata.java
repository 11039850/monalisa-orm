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
package com.tsc9526.monalisa.orm.generator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.TableType;
import com.tsc9526.monalisa.orm.utils.TableHelper;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.io.MelpFile;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBMetadata {
	private static Map<String,Map<String, MetaTable>> hDBMetaTables=new ConcurrentHashMap<String,Map<String,MetaTable>>();
	
	public static MetaTable getTable(String dbKey,String tableName){
		if(tableName==null || dbKey==null || tableName.trim().length()<1){
			return null;
		}else{
			Map<String, MetaTable> tables=hDBMetaTables.get(dbKey);
			if(tables==null){
				String metafile=MelpFile.combinePath(DbProp.TMP_WORK_DIR_METATABLE,"/"+dbKey+".meta");		
				File taget=new File(metafile);
				if(taget.exists()){
					DBGenerator.plogger.info("Load meta-table("+dbKey+") from file: "+taget.getAbsolutePath());
					tables=MelpFile.readToObject(taget);
					
				}else{
					String resource="resources/"+dbKey+".meta";
						
					InputStream in=DBMetadata.class.getClassLoader().getResourceAsStream(resource);
					if(in!=null){
						DBGenerator.plogger.info("Load meta-table("+dbKey+") from resource: "+resource+", ClassLoader: "+DBMetadata.class.getClassLoader());
						
						tables=MelpFile.readToObject(in);
					}else{
						DBGenerator.plogger.error("Load meta-table("+dbKey+") failed, resource: "+resource+", ClassLoader: "+DBMetadata.class.getClassLoader());
					}
				}
				
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
 
	private static Map<String,Map<String,MetaTable>> hRuntimeTables=new HashMap<String, Map<String,MetaTable>>();
	public synchronized static MetaTable getMetaTable(String dbKey,String theTableName) {
		try{
			Map<String,MetaTable> hmt=hRuntimeTables.get(dbKey);
			if(hmt == null){
				hmt=loadMetaTables(dbKey);
				hRuntimeTables.put(dbKey, hmt);	 			
			}
			return hmt.get(theTableName);			 		
		}catch(Exception e){
			throw new RuntimeException("MetaTable not found: "+theTableName+", dbKey: "+dbKey,e);
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
				String       tablePrefix=line.substring(key_begin.length(),p).trim();
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
				table.setCreateTable(new CreateTable(tableName,createSQL.toString(),TableType.NORMAL));
				hRuntimeTables.put(tablePrefix, table);				 
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
		  
		catalog=dbcfg.getCfg().getCatalog();
		schema=dbcfg.getCfg().getSchema();
		tableName=dbcfg.getCfg().getTables();
		
		if(schema==null || schema.length()==0){
			schema=dsm.getDialect(dbcfg).getSchema(dbcfg.getCfg().getUrl());
		}		
	}
	 
	
	public List<MetaTable> getTables(){
		if(schema==null || schema.length()==0){
			throw new RuntimeException("Database not set in DB annotation schema or url: "+dbcfg.getCfg().getUrl());
		}
		
		DataSource ds=dbcfg.getDataSource();
		Connection conn=null;
		try{
			conn=ds.getConnection();
 			DatabaseMetaData dbm=conn.getMetaData();
			List<MetaTable> tables=getTables(dbm);
		    for(MetaTable table:tables){
		    	TableHelper.getTableColumns(dbcfg,dbm, table);		    	 
		    	TableHelper.getTableIndexes(dbcfg,dbm, table);
		    
		    	table.setJavaPackage(javaPackage);
		    }		    
		   		    
		    for(MetaTable table:tables){
		    	if(table.getPartition()!=null){
		    		CreateTable createTable=dbcfg.getDialect().getCreateTable(dbcfg, table.getName());
		    		table.setCreateTable(createTable);
		    	}
		    }
		     
		    
		    String dbKey=dbcfg.getCfg().getKey();		  
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
		    	if(!new File(DbProp.TMP_WORK_DIR_METATABLE).exists()){
		    		new File(DbProp.TMP_WORK_DIR_METATABLE).mkdirs();
		    	}
		    	String metafile=MelpFile.combinePath(DbProp.TMP_WORK_DIR_METATABLE,"/"+dbcfg.getCfg().getKey()+".meta");	
		    	
				saveTables(hTables,new FileOutputStream(metafile));
		    }
		    
			return tables;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			MelpClose.close(conn);			 
		}
	}	 
	 
	public void saveTables(Map<String, MetaTable> hTables,OutputStream out)throws IOException {
		ByteArrayOutputStream buf=new ByteArrayOutputStream();
		ObjectOutputStream outputStream=new ObjectOutputStream(buf);
		outputStream.writeObject(hTables);
		outputStream.flush();
		
		 
		MelpFile.copy(new ByteArrayInputStream(buf.toByteArray()),out);  
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
		List<MetaPartition> partitions=dbcfg.getCfg().getMetaPartitions();
		for(MetaPartition p:partitions){
			p.clearTable();
		}
		
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
		String mapping=dbcfg.getCfg().getMapping().trim();
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
	 
	 
	public DBConfig getDb() {
		return dbcfg;
	}		 
}
