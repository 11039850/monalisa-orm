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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.meta.MetaPartition;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.TableType;
import com.tsc9526.monalisa.orm.utils.TableHelper;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBMetadata {
	private static Map<String, Map<String, MetaTable>> hDBMetaTables = new ConcurrentHashMap<String, Map<String, MetaTable>>();

	public static MetaTable getTable(String dbKey, String theTableName) {
		String tableName=theTableName;
		if (tableName == null || dbKey == null || tableName.trim().length() < 1) {
			return null;
		} else {
			Map<String, MetaTable> tables = loadTables(dbKey);
			if (tables != null) {
				tableName = tableName.trim().toLowerCase();
				char c = tableName.charAt(0);
				if (!((c >= 'a' && c <= 'z') || c == '_')) {
					tableName = tableName.substring(1, tableName.length() - 1);
				}
				return tables.get(tableName);
			} else {
				return null;
			}
		}
	}
	
	private static Map<String, MetaTable> loadTables(String dbKey){
		Map<String, MetaTable> tables = hDBMetaTables.get(dbKey);
		if (tables == null) {
			tables=loadTablesFromFile(dbKey);

			if (tables != null) {
				hDBMetaTables.put(dbKey, tables);
			}
		}
		return tables;
	}
	
	private static Map<String, MetaTable> loadTablesFromFile(String dbKey){
		String logPrefix="Load meta-table(" + dbKey + ")";
		
		String metafile = MelpFile.combinePath(DbProp.TMP_WORK_DIR_METATABLE, "/" +getMetaFilename(dbKey));
		File taget = new File(metafile);
		if (taget.exists()) {
			DBGenerator.plogger.info(logPrefix+" from file: " + taget.getAbsolutePath());
			return  MelpFile.readToObject(taget);

		} else {
			String resource = "resources/" + getMetaFilename(dbKey);

			InputStream in = DBMetadata.class.getClassLoader().getResourceAsStream(resource);
			if (in != null) {
				DBGenerator.plogger.info(logPrefix+" from resource: " + resource + ", ClassLoader: " + DBMetadata.class.getClassLoader());

				return MelpFile.readToObject(in);
				
				
			} else {
				DBGenerator.plogger.error(logPrefix+" failed, resource: " + resource + ", ClassLoader: " + DBMetadata.class.getClassLoader());
			}
		}

		return null;
	}
	
	public static String getMetaFilename(String dbKey){
		return dbKey + ".meta";
	}

	private static Map<String, Map<String, MetaTable>> hRuntimeTables = new HashMap<String, Map<String, MetaTable>>();

	public synchronized static MetaTable getMetaTable(String dbKey, String theTableName) {
		try {
			Map<String, MetaTable> hmt = hRuntimeTables.get(dbKey);
			if (hmt == null) {
				hmt = loadMetaTables(dbKey);
				hRuntimeTables.put(dbKey, hmt);
			}
			return hmt.get(theTableName);
		} catch (Exception e) {
			throw new RuntimeException("MetaTable not found: " + theTableName + ", dbKey: " + dbKey, e);
		}
	}

	private static Map<String, MetaTable> loadMetaTables(String dbKey) throws IOException {
		String res = "/resources." + dbKey.toLowerCase();
		res = res.replace(".", "/") + "/create_table.sql";

		Map<String, MetaTable> hRuntimeTables = new HashMap<String, MetaTable>();

		String key_begin = "/***CREATE TABLE:";
		String key_end = "***/";

		InputStream in = MetaTable.class.getResourceAsStream(res);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith(key_begin) && line.endsWith(key_end)) {
				int p = line.indexOf("::");

				String tableName = line.substring(p + 2, line.length() - key_end.length()).trim();
				String tablePrefix = line.substring(key_begin.length(), p).trim();
				StringBuilder createSQL = new StringBuilder();

				line = reader.readLine();
				while (line != null) {
					createSQL.append(line).append("\r\n");

					line = reader.readLine();
					if (line == null || (line.startsWith(key_begin) && line.endsWith(key_end))) {
						break;
					}
				}

				MetaTable table = new MetaTable(tableName);
				table.setCreateTable(new CreateTable(tableName, createSQL.toString(), TableType.NORMAL));
				hRuntimeTables.put(tablePrefix, table);
			} else {
				line = reader.readLine();
			}
		}
		reader.close();

		return hRuntimeTables;
	}

	protected String projectPath;
	protected String javaPackage;
	protected DBConfig dbcfg;

	protected Dialect dialect;
	protected String catalogPattern;
	protected String schemaPattern;
	protected String tablePattern;

	public DBMetadata(String projectPath, String javaPackage, DBConfig dbcfg) {
		this.projectPath = projectPath;
		this.javaPackage = javaPackage;
		this.dbcfg = dbcfg;

		dialect = dbcfg.getDialect();

		catalogPattern = dialect.getMetaCatalogPattern(dbcfg);
		schemaPattern  = dialect.getMetaSchemaPattern(dbcfg);
		tablePattern   = dialect.getMetaTablePattern(dbcfg);

		if (catalogPattern == null || catalogPattern.length() == 0) {
			catalogPattern = dialect.geCatalog(dbcfg.getCfg().getUrl());
		}

		if (schemaPattern == null || schemaPattern.length() == 0) {
			schemaPattern = dialect.getSchema(dbcfg.getCfg().getUrl());
		}
		
		if (tablePattern == null || tablePattern.length() == 0) {
			tablePattern="%";
		}
	}

	public List<MetaTable> getTables() {
		DBGenerator.plogger.info("Loading tables from db-key: " + dbcfg.getKey() + ", url: " + dbcfg.getCfg().getUrl());
		
		DataSource ds = dialect.getMetaDataSource(dbcfg);
		Connection conn = null;
		try {
			conn = ds.getConnection();

			DatabaseMetaData dbm = conn.getMetaData();
			List<MetaTable> tables = getTables(dbm);
 
			saveMetadata(tables);

			return tables;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			MelpClose.close(conn);
		}
	}

	protected void setupColumnIndexes(DatabaseMetaData dbm, List<MetaTable> tables) throws SQLException {
		int index = 0; 
		for (MetaTable table : tables) {
			index++;
			boolean isView      = "VIEW".equalsIgnoreCase( table.getType() );
			String  viewIndexes = DbProp.PROP_TABLE_VIEW_INDEXES.getValue(dbcfg, table.getName(),"false");
			boolean loadIndexes = !isView || viewIndexes.equalsIgnoreCase("true");
			 
			DBGenerator.plogger.info("Load ["+index+"/"+tables.size()+"] "+(loadIndexes?"columns&indexes":"columns")+" of table: "+table.getName());
			
			TableHelper.getTableColumns(dbcfg, dbm, table);
			
			if(loadIndexes){
				TableHelper.getTableIndexes(dbcfg, dbm, table);
			}
			table.setJavaPackage(javaPackage);
		}
	}

	protected void setupCreateTable(List<MetaTable> tables) throws SQLException {
		for (MetaTable table : tables) {
			if (table.getPartition() != null) {
				CreateTable createTable = dbcfg.getDialect().getCreateTable(dbcfg, table.getName());
				table.setCreateTable(createTable);
			}
		}
	}
  
	protected void saveMetadata(List<MetaTable> tables) throws IOException {
		String dbKey = dbcfg.getCfg().getKey();
		Map<String, MetaTable> hTables = hDBMetaTables.get(dbKey);
		if (hTables == null) {
			hTables = new ConcurrentHashMap<String, MetaTable>();
			hDBMetaTables.put(dbKey, hTables);
		} else {
			hTables.clear();
		}

		for (MetaTable table : tables) {
			hTables.put(table.getName().toLowerCase(), table);
		}

		if (projectPath != null) {
			if (!new File(DbProp.TMP_WORK_DIR_METATABLE).exists()) {
				new File(DbProp.TMP_WORK_DIR_METATABLE).mkdirs();
			}
			String metafile = MelpFile.combinePath(DbProp.TMP_WORK_DIR_METATABLE, "/" +getMetaFilename(dbKey));
			
			DBGenerator.plogger.info("Save meta-file: "+metafile);
			
			saveTables(hTables, new FileOutputStream(metafile));
		}
	}

	public void saveTables(Map<String, MetaTable> hTables, OutputStream out) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		ObjectOutputStream outputStream = new ObjectOutputStream(buf);
		outputStream.writeObject(hTables);
		outputStream.flush();

		MelpFile.copy(new ByteArrayInputStream(buf.toByteArray()), out);
	}

	private MetaPartition findPartition(List<MetaPartition> partitions, MetaTable table) {
		for (MetaPartition p : partitions) {
			String prefix = p.getTablePrefix().toLowerCase();
			String name = table.getName().toLowerCase();
			if (name.startsWith(prefix)) {
				return p;
			}
		}

		return null;
	}

	protected List<MetaTable> getTables(DatabaseMetaData dbm) throws SQLException {
		DBGenerator.plogger.info("Loading tables: catalogPattern = "+catalogPattern+", schemaPattern = "+schemaPattern+", tablePattern = "+tablePattern);
		
		List<MetaPartition> partitions = dbcfg.getCfg().getMetaPartitions();
		for (MetaPartition p : partitions) {
			p.clearTable();
		}

		List<MetaTable> tables = new ArrayList<MetaTable>();
		
		String type = DbProp.PROP_DB_TABLE_TYPE.getValue(dbcfg);
		if(MelpString.isEmpty(type)){
			type = "TABLE";
		}

		String tableQuery=tablePattern;
		Set<String> filterTables=null;
		if(tablePattern.indexOf('%')<0) {
			filterTables = new HashSet<String>();
			tableQuery="%";
			
			for(String table:tablePattern.split(",")) {
				filterTables.add(table.trim().toLowerCase());
			}
		}
		
		ResultSet rs = dbm.getTables(catalogPattern, schemaPattern, tableQuery, type.split(","));
		while (rs.next()) {
			MetaTable table = new MetaTable();
			table.setName(rs.getString(COLUMN_TABLE_NAME));
			table.setRemarks(rs.getString(COLUMN_REMARKS));
			table.setType(rs.getString(COLUMN_TABLE_TYPE));
			
			boolean skip =false;
			if(filterTables!=null) {
				skip=true;
				
				String tableName = table.getName().toLowerCase();
				for(String regex: filterTables) {
					if( tableName.matches(regex) ) {
						skip = false;
						break;
					}
				}
			}
			
			DBGenerator.plogger.info( (skip?"Skip ":"Load ")+table.getType()+": "+MelpString.rightPadding(table.getName(),26)+" { "+table.getRemarks()+" }");
			if(!skip) {
				MetaPartition partition = findPartition(partitions, table);
				if (partition != null) {
					partition.addTable(table);
				} else {
					tables.add(table);
				}
			}
		}
		rs.close();

		DBGenerator.plogger.info("Loading columns & indexes ... ");
		
		processTableRemarks(tables);
		processTableMapping(tables);
		processPartitionTable(partitions,tables); 
		
		setupColumnIndexes(dbm, tables);
		setupCreateTable(tables);
		
		if(dialect.supportSequence()){
			List<String[]> seqMappings = TableHelper.setupSequence(dbcfg,dbm, tables);
			for(String[] sm:seqMappings) {
				DBGenerator.plogger.info("Sequence: "+ MelpString.rightPadding(sm[0],26)+ " -> "+sm[1]+"."+sm[2]);
			}
		}

		return tables;
	}

	protected void processPartitionTable(List<MetaPartition> partitions,List<MetaTable> tables){
		for (MetaPartition p : partitions) {
			MetaTable table = p.getTable();
			if (table != null) {
				tables.add(table);
			}
		}
	}
	
	protected void processTableRemarks(List<MetaTable> tables) {
		for (MetaTable table : tables) {
			if (!MelpString.isEmpty(table.getRemarks())) {
				return;
			}
		}

		try {
			DataTable<DataMap> tds = dbcfg.getDialect().getTableDesription(dbcfg, schemaPattern);
			if (tds != null) {
				DataMap xs = new DataMap();

				for (DataMap m : tds) {
					String name   = m.getString(COLUMN_TABLE_NAME, "");
					String remark = m.getString(COLUMN_TABLE_COMMENT, "");
					xs.put(name, remark);
				}

				for (MetaTable table : tables) {
					table.setRemarks(xs.getString(table.getName(), ""));
				}
			}
		} catch (Exception e) {
			DBGenerator.plogger.error("Failed read table schema info: " + e.getMessage(), e);
		}
	}

	private void processTableMapping(List<MetaTable> tables) {
		String mapping = dbcfg.getCfg().getMapping().trim();
		if (mapping.length() > 0) {
			Map<String, String> hTableMapping = new HashMap<String, String>();
			for (String vs : mapping.split(";")) {
				String[] nv = vs.split("=");
				hTableMapping.put(nv[0].trim().toLowerCase(), nv[1]);
			}

			for (MetaTable table : tables) {
				String javaName = hTableMapping.get(table.getName().toLowerCase());
				if (javaName != null) {
					table.setJavaName(javaName);
				}
			}
		}
	}

	public DBConfig getDb() {
		return dbcfg;
	}
	
	public final static String COLUMN_TABLE_NAME     = "TABLE_NAME";
	public final static String COLUMN_TABLE_COMMENT  = "TABLE_COMMENT";
	public final static String COLUMN_REMARKS  		 = "REMARKS";
	public final static String COLUMN_TABLE_TYPE     = "TABLE_TYPE";
}
