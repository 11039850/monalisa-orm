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
package com.tsc9526.monalisa.orm.dialect;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.TableType;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public class MysqlDialect extends Dialect {

	public String getUrl(String host,int port,String dbname){
		if(port>0){
			return getUrlPrefix()+host+":"+port+"/"+dbname;
		}else{
			return getUrlPrefix()+host+"/"+dbname;
		}
	}
	
	public String getUrlPrefix() {
		return "jdbc:mysql://";
	}

	public String getDriver() {
		return "com.mysql.jdbc.Driver";
	}
	
	public String getIdleValidationQuery(){
    	return "SELECT 1";
    }

	public String geCatalog(String jdbcUrl) {
		return null;
	}
	
	public String getSchema(String jdbcUrl) {
		String schema = "";

		String prefix = getUrlPrefix();

		if (jdbcUrl.startsWith(prefix)) {
			int p = jdbcUrl.indexOf('/', prefix.length());
			if (p > 0) {
				schema = jdbcUrl.substring(p + 1);
				p = schema.indexOf('?');
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
		return getColumnName(name);
	}
	
	
	public Query insertOrUpdate(Model model){
		List<FGS> uniqueFields = getUniqueFields(model); 
		if(uniqueFields.isEmpty()){
			return insert(model);
		}
		 
		Set<String> unames=new LinkedHashSet<String>();
		for(FGS fgs:uniqueFields){
			unames.add(fgs.getFieldName());
		}
		
		Query query=createQuery();
		
		query.add("INSERT INTO ").add(getTableName(model.table()));
		 
		addNameValues(query,model);
		
		query.add(" ON DUPLICATE KEY UPDATE ");
		
		int i=0;
		FGS createTime = model.fieldGetCreateTime();
		FGS createBy   = model.fieldGetCreateBy();
		for(Object o:model.changedFields()){
			FGS fgs=(FGS)o;
			
			Column c = fgs.getAnnotation(Column.class);
			Object v = getValue(fgs,model);
			
			boolean skip = c.auto() || c.key() || unames.contains(fgs.getFieldName());
			if(!skip){
				skip = fgs.isSameName(createTime) || fgs.isSameName(createBy);
			} 
			
			if(!skip){
				if(i>0){
					query.add(", ");
				}
				query.add(getColumnName(c.name()) + " = ?",v);
				i++;
			}			
		}
		
		if(i==0){
			FGS fgs=uniqueFields.get(0);
			Column c = fgs.getAnnotation(Column.class);
			query.add(getColumnName(c.name()) + " = " +getColumnName(c.name()));
		}
		
		return query;
	}
 
	public String getLimitSql(String orignSql, int limit,int offset){
		return orignSql+" LIMIT " + limit + " OFFSET " + offset;
	}
  
	@Override
	public DataTable<DataMap> getTableDesription(DBConfig db,String schemaPattern){
		String sql="SELECT TABLE_NAME,TABLE_COMMENT  FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=? ";
		return db.select(sql, schemaPattern);
	}

	@Override
	public boolean tableExist(DBConfig db,String name,boolean incudeView){
		//always include view 
		List<DataMap> rs=db.select("show tables like ?", name);
		return !rs.isEmpty();
	}
	
	public CreateTable getCreateTable(DBConfig db, String tableName) {
		String sql = "SHOW CREATE TABLE " + getTableName(tableName);
		DataMap rs = db.selectOne(sql);
		if (rs != null) {
			String createSQL = rs.get(1).toString();

			int p = createSQL.indexOf('(');

			createSQL = "CREATE TABLE IF NOT EXISTS " + getTableName(CreateTable.TABLE_VAR) + createSQL.substring(p);

			return new CreateTable(tableName, createSQL); 
		} else {
			throw new RuntimeException("Table not found: " + tableName);
		}
	}

	@Override
	public synchronized void createTable(DBConfig db, CreateTable table) {
		String key=db.getKey()+":"+table.getTableName();
		if(!hTables.containsKey(key)){
			if (table.getTableType() == TableType.HISTORY) {			 
				setupHistoryTable(db, table);
			}else if (table.getTableType() == TableType.PARTITION) {
				setupPartitionTable(db,table);
			}
			
			super.createTable(db, table);
		}
	}

	protected void setupPartitionTable(DBConfig db, CreateTable table) {
		//分区表: 去掉自动增长初始值设置
		String sql = table.getCreateSQL();
		sql = sql.replaceFirst("(?i)\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
		table.setCreateSQL(sql);		
	}
	
	protected void setupHistoryTable(DBConfig db, CreateTable table) {
		String tableName = table.getTableName();
		String sql = table.getCreateSQL();

		// 去掉字段自增属性
		sql = sql.replaceFirst("(?i)\\s+AUTO_INCREMENT\\s*=\\s*\\d+", "");
		sql = sql.replaceFirst("(?i)\\s+AUTO_INCREMENT\\s*", " ");

		String pkIndex=null;
		String prefix = DbProp.PROP_DB_HISTORY_PREFIX_COLUMN.getValue(db);
		StringBuilder sb = new StringBuilder();
		for (String x : sql.split("\\n")) {
			x = x.trim();
			if (x.startsWith("CREATE")) {
				//表创建语句
				sb.append(x).append("\r\n");

				//定义历史表的字段
				sb.append("`" + prefix + "id`   int(11)      NOT NULL AUTO_INCREMENT COMMENT '自增主键',\r\n");
				sb.append("`" + prefix + "time` datetime     NOT NULL COMMENT '变更时间',\r\n");
				sb.append("`" + prefix + "type` varchar(32)  NOT NULL COMMENT '变更类型: INSERT/UPDATE/DELETE/REPLACE',\r\n");
				sb.append("`" + prefix + "txid` varchar(64)  NOT NULL COMMENT '变更批次',\r\n");
				sb.append("`" + prefix + "user` varchar(128)          COMMENT '操作用户',\r\n");
			}else if (x.startsWith("PRIMARY")) {
				//为源表主键建立索引
				int p1=x.indexOf('(');
				int p2=x.indexOf(')');
				if(p2>p1 && p1>0){
					pkIndex="KEY `ix_" + tableName + "_pk` "+ x.substring(p1,p2+1) +" USING BTREE,\r\n";
				}
			} else if (x.startsWith("`")) {
				//字段定义
				sb.append(x).append("\r\n");
			} else if (x.startsWith(")")) {
				//主键,索引
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
