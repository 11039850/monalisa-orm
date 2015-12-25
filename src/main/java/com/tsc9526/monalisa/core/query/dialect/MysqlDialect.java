package com.tsc9526.monalisa.core.query.dialect;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.core.meta.MetaTable.TableType;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MysqlDialect extends Dialect {

	public String getUrlPrefix() {
		return "jdbc:mysql://";
	}

	public String getDriver() {
		return "com.mysql.jdbc.Driver";
	}

	public String getSchema(String jdbcUrl) {
		String schema = "";

		String prefix = getUrlPrefix();

		if (jdbcUrl.startsWith(prefix)) {
			int p = jdbcUrl.indexOf("/", prefix.length());
			if (p > 0) {
				schema = jdbcUrl.substring(p + 1);
				p = schema.indexOf("?");
				if (p > 0) {
					schema = schema.substring(0, p);
				}
			}
		}

		return schema;
	}

	public String getColumnName(String name) {
		if (name.startsWith("`")) {
			return name;
		} else {
			return "`" + name + "`";
		}
	}

	public String getTableName(String name) {
		if (name.startsWith("`")) {
			return name;
		} else {
			return "`" + name + "`";
		}
	}

	public Query getLimitQuery(Query origin, int limit, int offset) {
		Query query = new Query();

		query.use(origin.getDb());
		query.add(origin.getSql());
		query.setParameters(origin.getParameters());
		query.add(" LIMIT " + limit + " OFFSET " + offset);

		return query;
	}

	public CreateTable getCreateTable(DBConfig db, String tableName) {
		String sql = "SHOW CREATE TABLE " + getTableName(tableName);
		DataMap rs = db.selectOne(sql);
		if (rs != null) {
			String createSQL = rs.getString(1);

			int p = createSQL.indexOf("(");

			createSQL = "CREATE TABLE IF NOT EXISTS " + getTableName(CreateTable.TABLE_VAR) + createSQL.substring(p);

			CreateTable createTable = new CreateTable(tableName, createSQL);
			return createTable;
		} else {
			throw new RuntimeException("Table not found: " + tableName);
		}
	}

	public synchronized void createTable(DBConfig db, CreateTable table) {
		String key=db.getKey()+":"+table.getTableName();
		if(hTables.containsKey(key)){
			return;
		}
		
		if (table.getTableType() == TableType.HISTORY) {			 
			setupHistoryTable(db, table);
		}else if (table.getTableType() == TableType.PARTITION) {
			setupPartitionTable(db,table);
		}
		
		super.createTable(db, table);
	}

	protected void setupPartitionTable(DBConfig db, CreateTable table) {
		//分区表: 去掉自动增长初始值设置
		String sql = table.getCreateSQL();
		sql = sql.replaceFirst("\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
		table.setCreateSQL(sql);		
	}
	
	protected void setupHistoryTable(DBConfig db, CreateTable table) {
		String tableName = table.getTableName();
		String sql = table.getCreateSQL();

		// 去掉字段自增属性
		sql = sql.replaceFirst("\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
		sql = sql.replaceFirst("\\s+AUTO_INCREMENT\\s*", " ");

		String pkIndex=null;
		String prefix = DbProp.PROP_DB_HISTORY_PREFIX_COLUMN.getValue(db);
		StringBuffer sb = new StringBuffer();
		for (String x : sql.split("\\n")) {
			x = x.trim();
			if (x.startsWith("CREATE")) {
				sb.append(x).append("\r\n");

				sb.append("`" + prefix + "id`   int(11)      NOT NULL AUTO_INCREMENT COMMENT '自增主键',\r\n");
				sb.append("`" + prefix + "time` datetime     NOT NULL COMMENT '变更时间',\r\n");
				sb.append("`" + prefix + "type` varchar(32)  NOT NULL COMMENT '变更类型: INSERT/UPDATE/DELETE/REPLACE',\r\n");
				sb.append("`" + prefix + "txid` varchar(64)  NOT NULL COMMENT '变更批次',\r\n");
				sb.append("`" + prefix + "user` varchar(128)          COMMENT '操作用户',\r\n");
			}else if (x.startsWith("PRIMARY")) {
				int p1=x.indexOf("(");
				int p2=x.indexOf(")");
				if(p2>p1 && p1>0){
					pkIndex="KEY `ix_" + tableName + "_pk` "+ x.substring(p1,p2+1) +" USING BTREE,\r\n";
				}
			} else if (x.startsWith("`")) {
				sb.append(x).append("\r\n");
			} else if (x.startsWith(")")) {
				sb.append("PRIMARY KEY (`" + prefix + "id`),\r\n");
				if(pkIndex!=null){
					sb.append(pkIndex);
				}
				sb.append("KEY `ix_" + tableName + "_time` (" + "`" + prefix + "time`) USING BTREE\r\n");
				sb.append(x).append("\r\n");
			}
		}
		table.setCreateSQL(sb.toString());
		
		//TODO: 处理字段变更 ...
	}
}
