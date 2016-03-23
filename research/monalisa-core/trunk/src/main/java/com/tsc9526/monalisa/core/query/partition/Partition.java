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
package com.tsc9526.monalisa.core.query.partition;

import com.tsc9526.monalisa.core.meta.MetaPartition;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.model.Model;

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
