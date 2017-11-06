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
package test.com.tsc9526.monalisa.orm.dialect.oracle.cases;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import test.com.tsc9526.monalisa.orm.dialect.oracle.OracleDB;
import test.com.tsc9526.monalisa.orm.dialect.oracle.oracledb.TestTable1;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TableTest {
	static AtomicInteger x=new AtomicInteger();
	
	public static void main(String[] args)throws Exception {
		System.out.println( TestTable1.SELECT().count() );
		
		TestTable1.DELETE().deleteAll();
		 
		ExecutorService pool = Executors.newFixedThreadPool(1);
		long ts=System.currentTimeMillis();
		for(int i=0;i<10;i++){
			pool.submit(new Runnable() {
				@Override
				public void run() {
					test1();
					//test2();
				}
			});
		}
		
		pool.shutdown();
		pool.awaitTermination(10,TimeUnit.MINUTES);
		
		System.out.println(  TestTable1.WHERE().title.like("T%").SELECT().select(5,5).format() );
		
		System.out.println("Use time: "+(System.currentTimeMillis() - ts));
	}
	
	static void test2(){
		try{
			Connection conn=OracleDB.DB.getDataSource().getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT SEQ_TEST_TABLE_1.nextVal FROM dual");
			ResultSet rs=pst.executeQuery();
			rs.next();
			long seq=rs.getLong(1);
			rs.close();
			pst.close();
			 
			TestTable1 table1=new TestTable1();
			table1.defaults();
			table1.setId(new BigDecimal(seq));
			table1.setTitle("T-"+x.addAndGet(1));
			String sql=OracleDB.DB.getDialect().insert(table1, false).getExecutableSQL();
			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			pst.close();
			
			
			conn.close();
			
			System.out.println("TableID:  "+table1.getId()+", TITLE: "+table1.getTitle());
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	static void test1(){
		TestTable1 table1=new TestTable1();
		table1.defaults();
		table1.setTitle("T-"+x.addAndGet(1));
		table1.save();
		System.out.println("TableID:  "+table1.getId()+", TITLE: "+table1.getTitle());
	}
}
