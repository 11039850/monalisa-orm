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
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.datatable.DataMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings({"rawtypes"})
public class PostgresDialect extends Dialect {

	public String getUrl(String host,int port,String dbname){
		if(port>0){
			return getUrlPrefix()+host+":"+port+"/"+dbname;
		}else{
			return getUrlPrefix()+host+"/"+dbname;
		}
	}
	
	public String getUrlPrefix() {
		return "jdbc:postgresql://";
	}

	public String getDriver() {
		return "org.postgresql.Driver";
	}
	
	public String getIdleValidationQuery(){
    	return "SELECT 1";
    }

	public String geCatalog(String jdbcUrl) {
		String catalog = "";

		String prefix = getUrlPrefix();

		if (jdbcUrl.startsWith(prefix)) {
			int p = jdbcUrl.indexOf('/', prefix.length());
			if (p > 0) {
				catalog = jdbcUrl.substring(p + 1);
				p = catalog.indexOf('?');
				if (p > 0) {
					catalog = catalog.substring(0, p);
				}
			}
		}

		return catalog;
	}
	
	public String getSchema(String jdbcUrl) {
		return "public";
	}

	public String getColumnName(String name) {
		if (name.startsWith("\"")) {
			return name;
		} else {
			return "\"" + name + "\"";
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
	 
		StringBuilder uniqueColumns = new StringBuilder(); 
		Set<String> unames=new LinkedHashSet<String>();
		for(FGS fgs:uniqueFields){
			unames.add(fgs.getFieldName());
			
			Column c = fgs.getAnnotation(Column.class);
			
			if(uniqueColumns.length()>0) {
				uniqueColumns.append(",");
			}
			uniqueColumns.append(getColumnName(c.name()));
		}
		
		Query query=createQuery(model);
		
		query.add("INSERT INTO ").add(getTableName(model.table()));
		 
		addNameValues(query,model);
		
		 
		query.add(" ON CONFLICT ( "+uniqueColumns.toString()+") DO UPDATE SET ");
		
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
	public boolean tableExist(DBConfig db,String name,boolean incudeView){
		DataMap rs=db.selectOne(""+/**~!{*/""
			+ "SELECT EXISTS ( "
			+ "\r\n	SELECT 1  "
			+ "\r\n		FROM information_schema.tables "
			+ "\r\n		WHERE table_schema = ? AND table_name = ? "
			+ "\r\n)"
		+ "\r\n"/**}*/, db.getSchema(),name);
		return rs!=null && "t".equalsIgnoreCase( rs.getString("exists") );
	}
	
	public CreateTable getCreateTable(DBConfig db, String tableName) {
		throw new RuntimeException("Not implement!");
	}

	@Override
	public synchronized void createTable(DBConfig db, CreateTable table) {
		throw new RuntimeException("Not implement!");
	} 
}
