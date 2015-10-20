package com.tsc9526.monalisa.core.query.partition;

import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.dao.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("rawtypes")
public interface Partition<T extends Model> {
	
	/**
	 * 根据分区定义获取Model对应的表名
	 *  
	 * @param mp
	 * @param model
	 * @return
	 */
	public String getTableName(MetaPartition mp,T model);
	
	/**
	 * 编译期校验配置是否正确
	 * @param mp     分区定义
	 * @param table  表信息
	 * @return  如果校验正确则返回null, 否则返回错误信息!
	 */
	public String verify(MetaPartition mp,MetaTable table);
}
