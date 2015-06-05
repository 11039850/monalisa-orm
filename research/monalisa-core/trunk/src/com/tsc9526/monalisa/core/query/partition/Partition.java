package com.tsc9526.monalisa.core.query.partition;

import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.dao.Model;

@SuppressWarnings("rawtypes")
public interface Partition<T extends Model> {
	
	public void setup(String tablePrefix,String... args);
	
	public String getTableName(T model);
	
	/**
	 * 编译期校验配置是否正确
	 * @param Model
	 * @return  如果校验正确则返回null, 否则返回错误信息!
	 */
	public String verify(MetaTable table);
}
