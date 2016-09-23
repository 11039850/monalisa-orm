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
package com.tsc9526.monalisa.orm.executor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.meta.Name;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.model.ModelEvent;
import com.tsc9526.monalisa.orm.tools.generator.DBExchange;
import com.tsc9526.monalisa.orm.tools.generator.DBMetadata;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.CloseQuietly;
import com.tsc9526.monalisa.orm.tools.helper.JavaBeansHelper;
import com.tsc9526.monalisa.orm.tools.helper.SQLHelper;
import com.tsc9526.monalisa.orm.tools.helper.TypeHelper;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.FGS;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public class ResultHandler<T> {
	protected static DataSourceManager dsm=DataSourceManager.getInstance();
	
	protected Query query;
	protected Class<T> resultClass;
	
	public ResultHandler(Query query, Class<T> resultClass) {
		this.query = query;
		this.resultClass = resultClass;
	}

	public T createResult(ResultSet rs) throws SQLException {
		if (resultClass == Long.class || resultClass == long.class) {
			return (T) new Long(rs.getLong(1));
		} else if (resultClass == Integer.class || resultClass == int.class) {
			return (T) new Integer(rs.getInt(1));
		} else if (resultClass == Float.class || resultClass == float.class) {
			return (T) new Float(rs.getFloat(1));
		} else if (resultClass == Short.class || resultClass == short.class) {
			return (T) new Short(rs.getShort(1));
		} else if (resultClass == Byte.class || resultClass == byte.class) {
			return (T) new Byte(rs.getByte(1));
		} else if (resultClass == Double.class || resultClass == double.class) {
			return (T) new Double(rs.getDouble(1));
		} else if (resultClass == String.class) {
			return (T) rs.getString(1);
		} else if (resultClass == BigDecimal.class) {
			return (T) rs.getBigDecimal(1);
		} else if (resultClass == Date.class) {
			return (T) rs.getDate(1);
		} else if (resultClass == byte[].class) {
			return (T) rs.getBytes(1);
		} else {
			try {
				if (Map.class.isAssignableFrom(resultClass)) {
					return (T) loadToMap(rs, new DataMap());
				} else {
					return (T) load(rs, resultClass.newInstance());
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public T load(ResultSet rs, T result) throws SQLException {
		if (result instanceof Model<?>) {
			loadModel(rs, (Model<?>) result);
		} else {
			loadResult(rs, result);
		}
		return result;
	}

	protected DataMap loadToMap(ResultSet rs, DataMap map) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();

		Map<String, Integer> xs = new HashMap<String, Integer>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}
			name = name.toLowerCase();

			Integer n = xs.get(name);
			if (n != null) {
				map.put(name + n, rs.getObject(i));

				xs.put(name, n + 1);
			} else {
				xs.put(name, 1);

				map.put(name, rs.getObject(i));
			}
		}

		return map;
	}

	protected void loadModel(ResultSet rs, Model<?> model) throws SQLException {
		Class<?> clazz = ClassHelper.findClassWithAnnotation(model.getClass(), DB.class);
		if (clazz == null && model.use() == null) {
			model.use(query.getDb());
		}

		model.before(ModelEvent.LOAD);

		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}

			Name nColumn = new Name(false).setName(name);

			FGS fgs = model.field(nColumn.getJavaName());
			if (fgs != null) {
				Object v = rs.getObject(i);
				fgs.setObject(model, v);
			}
		}

		model.after(ModelEvent.LOAD, 0);
	}

	protected T loadResult(ResultSet rs, T result) throws SQLException {
		MetaClass metaClass = ClassHelper.getMetaClass(result.getClass());

		ResultSetMetaData rsmd = rs.getMetaData();

		Map<String, Integer> xs = new HashMap<String, Integer>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String name = rsmd.getColumnLabel(i);
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}

			Integer n = xs.get(name);
			if (n != null) {
				name = name + n;

				xs.put(name, n + 1);
			} else {
				xs.put(name, 1);
			}

			Name nColumn = new Name(false).setName(name);

			FGS fgs = metaClass.getField(nColumn.getJavaName());
			if (fgs != null) {
				Object v = rs.getObject(i);
				fgs.setObject(result, v);
			}
		}
		return result;
	}

	public static void processExchange(Query query, DBExchange exchange) {
		Connection conn = null;
		try {
			exchange.setDbKey(query.getDb().getCfg().getKey());
			exchange.setSql(query.getExecutableSQL());
			
			conn = dsm.getDataSource(query.getDb()).getConnection();
			PreparedStatement pst = conn.prepareStatement(query.getSql());
			SQLHelper.setPreparedParameters(pst, query.getParameters());

			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			MetaTable table = new MetaTable();
			int cc = rsmd.getColumnCount();
			for (int i = 1; i <= cc; i++) {
				String type = TypeHelper.getJavaType(rsmd.getColumnType(i));
				String name = rsmd.getColumnName(i);
				String label = rsmd.getColumnLabel(i);

				MetaColumn column = new MetaColumn();
				String tableName = rsmd.getTableName(i);
				column.setTable(new MetaTable(tableName));

				column.setName(name);

				if (label != null && label.trim().length() > 0) {
					column.setJavaName(JavaBeansHelper.getJavaName(label, false));
				}

				column.setJavaType(type);
				table.addColumn(column);
			}

			renameDuplicatedColumns(table);
 			
			exchange.setTable(table);
			exchange.setErrorString(null);
			
			processMetaTable(exchange);
			
			rs.close();
			pst.close();
		} catch (Exception e) {
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			exchange.setErrorString(s.toString());
		} finally {
			CloseQuietly.close(conn);
		}
	}
	
	private static void processMetaTable(DBExchange exchange){
		Set<String> imps = new LinkedHashSet<String>();
		 
		for (MetaColumn c : exchange.getTable().getColumns()) {
			String tableName = c.getTable().getName();
			MetaTable columnTable = DBMetadata.getTable(exchange.getDbKey(), tableName);
			c.setTable(columnTable);
			if (columnTable != null) {
				MetaColumn cd = columnTable.getColumn(c.getName());
				if (cd != null) {
					c.setAuto(cd.isAuto());
					c.setJavaType(cd.getJavaType());
					c.setJdbcType(cd.getJdbcType());
					c.setKey(cd.isKey());
					c.setLength(cd.getLength());
					c.setNotnull(cd.isNotnull());
					c.setRemarks(cd.getRemarks());
					c.setValue(cd.getValue());

					imps.add(columnTable.getJavaPackage() + "." + columnTable.getJavaName());
					imps.addAll(c.getImports());
				} else {
					c.setTable(null);
				}
			}
		}
	}

	public static void renameDuplicatedColumns(MetaTable table) {
		Map<String, Integer> names = new HashMap<String, Integer>();
		for (MetaColumn c : table.getColumns()) {
			String name = c.getJavaName();

			Integer n = names.get(name);
			if (n != null) {
				c.setJavaName(name + n);

				names.put(name, n + 1);
			} else {
				names.put(name, 1);
			}
		}
	}
}