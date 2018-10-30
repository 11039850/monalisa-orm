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
package test.com.tsc9526.monalisa.orm.dialect.mysql.cases;

import com.tsc9526.monalisa.orm.datasource.DBConfig;

import test.com.tsc9526.monalisa.orm.dialect.basic.BaseRecordTest;
import test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MysqlRecordTest extends BaseRecordTest implements MysqlDB{ 
	private static final long serialVersionUID = -1974865252589672370L;
  
	@Override
	public DBConfig getDB() {
		return MysqlDB.DB;
	}

	@Override
	public String getInitSqls() {
		return ""+/**~{*/""
				+ "CREATE TABLE IF NOT EXISTS `test_record` ("
				+ "\r\n  `record_id`   int(11) NOT   NULL     AUTO_INCREMENT        COMMENT '唯一主键',"
				+ "\r\n  `name`        varchar(128)  NOT NULL default 'N0001'       COMMENT '名称',"
				+ "\r\n  `title`       varchar(128)  NULL                           COMMENT '标题',"
				+ "\r\n  `ts_a`        datetime      NULL,"
				+ "\r\n  `version`     int(11)       NOT NULL default 0,"
				+ "\r\n  `create_time` datetime      NOT NULL,"
				+ "\r\n  `create_by`   varchar(64)   NULL,"
				+ "\r\n  `update_time` datetime      NULL,"
				+ "\r\n  `update_by`   varchar(64)   NULL,"
				+ "\r\n  "
				+ "\r\n  PRIMARY KEY (`record_id`)"
				+ "\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;"
			 + "\r\n"
				+ "\r\nINSERT INTO test_record(record_id,`name`,`title`,ts_a,create_time)VALUES(1,\"hello\",\"record\",now(),now());"
			+ "\r\n"/**}*/;
	}

	@Override
	public String getCleanSqls() {
		return ""+/**~!{*/""
			+ "DROP TABLE IF EXISTS `test_record`"
		+ "\r\n"/**}*/ ;
	}
	 
}
