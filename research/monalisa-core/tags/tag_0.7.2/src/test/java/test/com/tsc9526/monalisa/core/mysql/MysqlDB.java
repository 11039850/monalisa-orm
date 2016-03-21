package test.com.tsc9526.monalisa.core.mysql;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;

@DB(
		url="jdbc:mysql://127.0.0.1:3306/test_monalisa", 
		username="monalisa", 
		password="monalisa",
		partitions="test_logyyyymm_{DatePartitionTable(yyyyMM,log_time)}")
public interface MysqlDB {
	public static DBConfig DB=DBConfig.fromClass(MysqlDB.class);
}
