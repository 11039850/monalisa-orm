package com.tsc9526.monalisa.core.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MetaTable extends Name implements Cloneable{
	private static final long serialVersionUID = 5069827028195702141L;
	
	private List<MetaColumn> columns=new ArrayList<MetaColumn>();
	
	private List<MetaColumn> keyColumns=new ArrayList<MetaColumn>();
	
	private List<MetaIndex> indexes=new ArrayList<MetaIndex>();
	
	private String javaPackage;
	
	private long serialID=0L;
	
	private MetaPartition partition;
	
	private CreateTable createTable;
	
	public MetaTable(){
		super(true);
	}
	
	public MetaTable(String name){
		super(true);
		
		this.setName(name);
	}
	
	
	public void addIndex(MetaIndex index){
		indexes.add(index);
	}
	
	public MetaIndex getIndex(String indexName){
		for(MetaIndex index:indexes){
			if(indexName.equalsIgnoreCase(index.getName())){
				return index;
			}
		}
		return null;
	}
	
	public List<MetaIndex> getIndexes(){
		return this.indexes;
	}
	
	public long getSerialID(){
		if(serialID==0){
			StringBuffer sb=new StringBuffer();
			sb.append(name).append("-").append(getJavaName()).append("-").append(getJavaPackage()).append("-").append(": {\r\n");
			for(MetaColumn c:columns){
				sb.append(c.name).append(c.getJavaType()).append("\r\n");
			}
			sb.append("}");
			
			serialID=( ((long)sb.length()) << 32 ) + Math.abs(sb.hashCode());
		}
		
		return serialID;
	}
	
	public List<MetaColumn> getColumns() {
		return columns;
	}
	
	public List<MetaColumn> getKeyColumns() {
		return keyColumns;
	}
	
	public MetaColumn addColumn(MetaColumn column) {		 		 
		columns.add(column);
		 
		return column;
	}
	
	public void addKeyColumn(MetaColumn column) {
		keyColumns.add(column);		 
	}
	
	public MetaColumn getColumn(String columnName){
		for(MetaColumn c:columns){
			if(c.getName().equalsIgnoreCase(columnName)){
				return c;
			}
		}
		return null;
	}
	 
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append(name).append(": {\r\n");
		for(MetaColumn c:columns){
			sb.append(c.toString()).append("\r\n");
		}
		sb.append("}");
		return sb.toString();
	}

	public String getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public MetaPartition getPartition() {
		return partition;
	}

	public void setPartition(MetaPartition partition) {
		this.partition = partition;
	}
 
	public void setCreateTable(CreateTable createTable) {
		this.createTable = createTable;
	}
	 
	public CreateTable getCreateTable() {
		return this.createTable;
	}
	
	public String getNamePrefix() {		 
    	if(partition!=null){
    		return partition.getTablePrefix();    		  
    	}else{		
    		return this.name;
    	}
	}	
	
	public MetaTable clone() throws CloneNotSupportedException{
		return (MetaTable)super.clone();
	}
	 	
	public static enum TableType{		
		NORMAL,
		
		PARTITION,
		
		HISTORY,		
	}
	
	
	public static class CreateTable implements Serializable{		 
		private static final long serialVersionUID = 4435179643490161535L;
		
		public final static String TABLE_VAR="#{table}";
		public final static String FILE_NAME="create_table.sql";
		
		private String    tableName;
		private String    createSQL;
		private TableType tableType;
		
		private CreateTable refTable;
		
		public CreateTable (String tableName,String createSQL){
			this(tableName, createSQL, TableType.NORMAL);
		}
		
		public CreateTable (String tableName,String createSQL,TableType tableType){
			this.tableName=tableName;
			this.createSQL=createSQL;
			this.tableType=tableType;
		}
		
		public CreateTable getRefTable(){
			return this.refTable;
		}
		
		public String getTableName() {
			return this.tableName;
		}	
		
		public TableType getTableType(){
			return this.tableType;
		}
	 	
		public String getOriginSQL() {			 
			return createSQL;			
		}
		
		public String getCreateSQL() {			 
			int p=createSQL.indexOf(TABLE_VAR);
			if(p>0){
				return createSQL.substring(0,p)+tableName+createSQL.substring(p+TABLE_VAR.length());
			}else{
				return createSQL;
			}
		}
		
		public CreateTable setCreateSQL(String createSQL){
			this.createSQL=createSQL;
			return this;
		}
		  
		public CreateTable createTable(TableType theTableType,String theTableName){
			CreateTable table=new CreateTable(theTableName,createSQL,theTableType);
			table.refTable=this;
			return table;
		}
	}
}
