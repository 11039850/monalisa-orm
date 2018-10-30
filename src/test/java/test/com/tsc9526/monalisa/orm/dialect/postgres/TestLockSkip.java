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
package test.com.tsc9526.monalisa.orm.dialect.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TestLockSkip {
	static String url = "jdbc:postgresql://127.0.0.1:5432/testdb";
    static String usr = "postgres";
    static String psd = "postgres9526";
    static BlockingQueue<Connection> queue;
    static AtomicInteger x = new AtomicInteger();
    
    static AtomicInteger OK = new AtomicInteger();
    
	public static void main(String[] args) throws Exception{
		int nThreads = 100;
		int nCount   = 5000;
		if(args.length>0) {
			nThreads =Integer.parseInt(args[0].trim());
		}
		
		if(args.length>1) {
			nCount =Integer.parseInt(args[1].trim());
		}
		
		queue = new LinkedBlockingQueue<Connection>(nThreads);
		for(int i=0;i<nThreads;i++) {
			queue.offer(getConnection());
		}
		 
		ExecutorService es=Executors.newFixedThreadPool(nThreads);
	 	
		for(int i=0;i<nCount*2;i++) {
			final int uid =  1000000+i;
			es.submit(new Runnable() {
				@Override
				public void run() {
					try {
						lock(uid);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		es.shutdown();
		es.awaitTermination(30,TimeUnit.MINUTES);
		
		List<Connection> cs = new ArrayList<Connection>();
		queue.drainTo(cs);
		for(Connection conn:cs) {
			conn.close();
		}
		
		System.out.println("OK: "+OK.get()+", total: "+nCount+", threads: "+nThreads);
	}
	 
	
	static void lock(int uid)throws Exception {
			Connection conn = queue.poll(1, TimeUnit.MINUTES);
			
			String sql=""+/**~!{*/""
				+ "UPDATE test_lock"
				+ "\r\n		SET    s1 = 1"
				+ "\r\n		WHERE  padcode = ("
				+ "\r\n						 SELECT padcode"
				+ "\r\n						 FROM   test_lock"
				+ "\r\n						 WHERE  s1 = 0"
				+ "\r\n						 LIMIT  1"
				+ "\r\n						 FOR    UPDATE SKIP LOCKED"
				+ "\r\n		)"
				+ "\r\n		RETURNING *"
			+ "\r\n"
			+ "\r\n"/**}*/;
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			int n = x.incrementAndGet();
			String index = (""+(100000+n)).substring(1);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String h = simpleDateFormat.format(new Date());
			if(rs.next()) {
				String padcode = rs.getString("padcode");
				OK.incrementAndGet();
				
				System.out.println(h+" OK ["+index+"] "+uid+":"+padcode);
			}else {
				System.out.println(h+" FA ["+index+"] "+uid);
			}
			rs.close();
			st.close();
			
			queue.offer(conn);
	}
	 
	
	private static Connection getConnection() throws Exception{
		 Class.forName("org.postgresql.Driver");

		 Connection conn= DriverManager.getConnection(url, usr, psd);
		 conn.setAutoCommit(true);
		 return conn;
	}

	
	static void insertTestData() throws Exception {
		Connection conn = getConnection();
		try {
			Statement st = conn.createStatement();
	
			for(int i=100;i<5000;i++) {
				st.executeUpdate("insert into test_lock(id,padcode,s1)values("+i+",'VM"+i+"',0 )");
			}
			 
			st.close();
		}finally {
			if(conn!=null)conn.close();
		}
	}
	          
 
	 
}
