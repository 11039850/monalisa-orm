package test.com.tsc9526.monalisa.core.config;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.ConfigClass;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.datasource.DbProp;
import com.tsc9526.monalisa.core.query.model.Record;

@Test
public class TestDBConfig {

	@DB(configClass=DB01.class)
	public static class DB01 extends ConfigClass{
	 	public Properties getConfigProperties() {
			Properties p=new Properties();
			p.put(DbProp.PROP_DB_URL.getFullKey(),        "jdbc:mysql://127.0.0.1:3306/test_monalisa");
			p.put(DbProp.PROP_DB_USERNAME.getFullKey(),   "monalisa");
			p.put(DbProp.PROP_DB_PASSWORD.getFullKey(),   "monalisa");
			p.put(DbProp.PROP_DB_PARTITIONS.getFullKey(), "test_logyyyymm_{DatePartitionTable(yyyyMM,log_time)}");
			return p;
		}
	}
	
	public void testConfigClass()throws Exception{
		DBConfig db=DBConfig.fromClass(DB01.class);
		
		Record record=new Record("test_table_1");
		record.use(db);
		
		long c= record.SELECT().count();
		System.out.println("Count: "+c);	
	}
}
