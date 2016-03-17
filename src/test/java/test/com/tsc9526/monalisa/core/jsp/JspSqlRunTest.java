package test.com.tsc9526.monalisa.core.jsp;

import java.io.File;

import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.datatable.DataTable;


public class JspSqlRunTest {
	public static void main(String[] args)throws Exception {
		File sqlFile=new File("sql/mysqldb/test1.jsp");
		long fileTime=0;
	 	
		while(true){
			if(fileTime!=sqlFile.lastModified()){
				if(fileTime>0){
					System.out.println("Reload file: "+sqlFile.getAbsolutePath());
				}
				fileTime=sqlFile.lastModified();
				
				Query query=Query.create("test.com.tsc9526.monalisa.core.sql.Q0001.testFindAll_A","name","","");
				System.out.println(query.getExecutableSQL());
				DataTable<DataMap> rs=query.getList();
				System.out.println("Total results: "+rs.size());
				for(DataMap x:rs){
					System.out.println(x.toString());
				}
			}else{
				Thread.sleep(1000);
			}
		}
	}
}
