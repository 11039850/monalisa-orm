package com.tsc9526.monalisa.core.generator;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.annotation.Table;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.resources.Freemarker;
import com.tsc9526.monalisa.core.tools.ClassHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DBTableGeneratorByTpl {
	private MetaTable table;
	private String modelClass;
	private String dbi;

	public DBTableGeneratorByTpl(MetaTable table, String modelClass, String dbi) {
		this.table = table;
		this.modelClass = modelClass;
		this.dbi = dbi;
	}

	public void generate(OutputStream os) throws Exception {
		generate(new OutputStreamWriter(os, "utf-8"));
	}

	public void generate(Writer w) {
		try {
			Configuration cfg = Freemarker.getFreemarkConfiguration();
			Template modelTpl = cfg.getTemplate("model.ftl");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("table", table);
			data.put("modelClass", modelClass);
			data.put("dbi", dbi);

			Set<String> imports = new LinkedHashSet<String>();
			imports.add(DB.class.getName());
			imports.add(Table.class.getName());
			imports.add(Column.class.getName());
			imports.add(ClassHelper.class.getName());
			if (table.getKeyColumns().size() > 0) {
				imports.add(Query.class.getName());
			}
			for (MetaColumn c : table.getColumns()) {
				imports.addAll(c.getImports());
			}
			data.put("imports", imports);

			modelTpl.process(data, w);
			w.flush();
			w.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MetaTable getTable() {
		return table;
	}

	public void setTable(MetaTable table) {
		this.table = table;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getDbi() {
		return dbi;
	}

	public void setDbi(String dbi) {
		this.dbi = dbi;
	}
}
