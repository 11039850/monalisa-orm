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
package com.tsc9526.monalisa.orm.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.generator.DBGenerator;
import com.tsc9526.monalisa.orm.generator.DBMetadata;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import com.tsc9526.monalisa.orm.meta.MetaIndex;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TableHelper {
	private TableHelper(){}
	
	public static MetaTable getMetaTable(DBConfig dbcfg,String tableName)throws SQLException{
		MTable mTable=new MTable(dbcfg,tableName);
		return mTable.getMetaTable();		  
	}
	 
	public static void getTableIndexes(DBConfig dbcfg,DatabaseMetaData dbm,MetaTable table)throws SQLException{
		Dialect dialect         = dbcfg.getDialect();
		
		String catalogPattern   = dialect.getMetaCatalogPattern(dbcfg);
		String schemaPattern    = dialect.getMetaSchemaPattern(dbcfg);
		String tablePattern     = dialect.getMetaTablePattern(dbcfg,table);
		
		ResultSet rs = dbm.getIndexInfo(catalogPattern, schemaPattern, tablePattern, false, true);  
	    while(rs.next()){
	    	short type=rs.getShort("TYPE");
	    	if(type != DatabaseMetaData.tableIndexStatistic){		    	
		    	String indexName  = rs.getString("INDEX_NAME");
		    	Integer position  = rs.getInt("ORDINAL_POSITION");			    	  
		    	String columnName = rs.getString("COLUMN_NAME");			    	 		    
		    	boolean nonUnique = rs.getBoolean("NON_UNIQUE"); 
		    	
		    	if(indexName.equalsIgnoreCase("PRIMARY")==false){		    	
			    	MetaIndex index=table.getIndex(indexName);
			    	if(index==null){			    	
				    	index=new MetaIndex();
				    	index.setName(indexName);
				    	index.setType(type);
				    	index.setUnique(!nonUnique);
				    					    	 
				    	table.addIndex(index);
			    	}
			    	
			    	MetaColumn c=table.getColumn(columnName);
			    	index.addColumn(c,position-1); 	
		    	}
	    	}		    	
	    }		    		    
	    rs.close(); 	 					    		    		 
	}
	
	public static void getTableColumns(DBConfig dbcfg,DatabaseMetaData dbm,MetaTable table)throws SQLException{
		getTableAllColumns(dbcfg,dbm,table);
		getTableKeyColumns(dbcfg,dbm,table);
	}
	
	private static void getTableAllColumns(DBConfig dbcfg,DatabaseMetaData dbm,MetaTable table)throws SQLException{
		Dialect dialect=dbcfg.getDialect();
		
		String catalogPattern = dialect.getMetaCatalogPattern(dbcfg);
		String schemaPattern  = dialect.getMetaSchemaPattern(dbcfg);
		String tablePattern   = dialect.getMetaTablePattern(dbcfg, table);
		
		ResultSet rs = dbm.getColumns(catalogPattern, schemaPattern,tablePattern, null);
	    while(rs.next()){
	    	boolean auto=false;
	    	 
	    	if(dialect.supportAutoIncrease()){
		    	String ai=(""+rs.getString("IS_AUTOINCREMENT")).toUpperCase();
		    	if("Y".equals(ai) || "YES".equals(ai) || "TRUE".equals(ai) || "1".equals(ai)){			    		 
		    		auto=true;
		    	}		
	    	}
	    	
	    	String value=rs.getString("COLUMN_DEF");
	    	if(value!=null && value.startsWith("'")){
	    		value=value.substring(1,value.length()-2);
	    	}
	    	if(value!=null){
	    		value=value.trim();
	    	}
	    	
	    	MetaColumn column=new MetaColumn();		
	    	column.setTable(table);
	    	
	    	column.setName(rs.getString("COLUMN_NAME"));
	    	column.setValue(value);
	    	column.setKey(false);
	    	column.setJdbcType(rs.getInt("DATA_TYPE"));
	    	column.setLength(rs.getInt("COLUMN_SIZE"));
	    	column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
	    	column.setNotnull(rs.getInt("NULLABLE") != DatabaseMetaData.columnNullable);
	    	column.setRemarks(rs.getString("REMARKS"));
	    	column.setAuto(auto);
	    	 
	    	table.addColumn(column);
	    	
		}
	    rs.close();		    	   
	}

	private static void getTableKeyColumns(DBConfig dbcfg,DatabaseMetaData dbm,MetaTable table)throws SQLException{
		Dialect dialect         = dbcfg.getDialect();
		
		String catalogPattern   = dialect.getMetaCatalogPattern(dbcfg);
		String schemaPattern    = dialect.getMetaSchemaPattern(dbcfg);
		String tablePattern     = dialect.getMetaTablePattern(dbcfg,table);
		
		Map<Short, MetaColumn> keyColumns = new TreeMap<Short, MetaColumn>();
	    ResultSet rs = dbm.getPrimaryKeys(catalogPattern,schemaPattern, tablePattern);
	    while(rs.next()){
	    	String columnName = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
	    	short keyseq      = rs.getShort("KEY_SEQ"); //$NON-NLS-1$
	    	
	    	MetaColumn column=table.getColumn(columnName);
	    	column.setKey(true);
	    	keyColumns.put(keyseq, column);
	    }
	  
	    for(MetaColumn c:keyColumns.values()){
	    	table.addKeyColumn(c);
	    }
	    rs.close();
	}
	

	public static void setupSequence(DBConfig dbcfg,DatabaseMetaData dbm, List<MetaTable> tables) throws SQLException {
		Dialect dialect         = dbcfg.getDialect();
		
		String catalogPattern   = dialect.getMetaCatalogPattern(dbcfg);
		String schemaPattern    = dialect.getMetaSchemaPattern(dbcfg);
		
		DataMap seqs=new DataMap();
		ResultSet rs=dbm.getTables(catalogPattern, schemaPattern, "%", new String[] { "SEQUENCE" });
		while(rs.next()){
			//SEQ_TASK_ID_DELETE  
			String seq=rs.getString(DBMetadata.COLUMN_TABLE_NAME);
			seqs.put(seq,seq);
		}
		MelpClose.close(rs);
		
		for(MetaTable table:tables){
			String tableName=table.getName().toUpperCase();
			
			String seq=DbProp.PROP_TABLE_SEQ.getValue(dbcfg, tableName,"SEQ_"+tableName);
			
			String cname = null;
			int x=seq.indexOf('@');
			if(x>0){
				cname = seq.substring(x+1);
				seq   = seq.substring(0,x);
			}
			
			if(seqs.containsKey(seq)){
				MetaColumn c= cname == null ? table.getColumns().get(0) : table.getColumn(cname);
				c.setAuto(true);
				c.setSeq(seq);
				
				DBGenerator.plogger.info("Sequence: "+ MelpString.rightPadding(seq,26)+ " -> "+tableName+"."+c.getName());
			}
		}
	}
	 
	private static class MTable{
		private DBConfig dbcfg;
		private String tableName;
	 	
		public MTable(DBConfig dbcfg,String tableName){
			this.dbcfg=dbcfg; 
			this.tableName=tableName;
		}
		
		public MetaTable getMetaTable()throws SQLException{
			Connection conn=null;
			try{
				Dialect dialect=dbcfg.getDialect();
			 	
				conn=dbcfg.getDataSource().getConnection();
				DatabaseMetaData dbm=conn.getMetaData();
				
				MetaTable table=new MetaTable(tableName);
				
				getTableColumns(dbcfg,dbm,table);								 
				getTableIndexes(dbcfg,dbm,table);
				
				if(dialect.supportSequence()){
					List<MetaTable> tables=new ArrayList<MetaTable>();
					tables.add(table);
					setupSequence(dbcfg,dbm,tables);
				}
				
				if(table.getColumns().isEmpty()){
					return null;
				}else{				
					return table;
				}
			}finally{
				
				MelpClose.close(conn);
			}			  
		}
	}
}
