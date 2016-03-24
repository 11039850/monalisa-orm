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
package test.com.tsc9526.monalisa.core.query;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.datatable.DataTable;
import com.tsc9526.monalisa.core.query.model.Model;
import com.tsc9526.monalisa.core.query.model.Record;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@DB(url="jdbc:mysql://127.0.0.1:3306/jy_market", username="jy_market", password="jy_market")
public class RecordTest extends Model<RecordTest>{ 
	private static final long serialVersionUID = -1974865252589672370L;

	String title;
	
	public RecordTest() {
		 super("gift");
	}
	
	
	public static void main(String[] args) {
		String h="1234,";
		String[] xs=h.trim().split(",|;|\\|");
		for(String x:xs){
			System.out.println(x);
		}
		
		
		DBConfig db=DBConfig.fromClass(RecordTest.class);
		
		DataTable<DataMap> r1=db.select("select count(*) from gift");
		System.out.println(r1.get(0).getInteger(0));
		
		Record tx=new Record("gift");
		tx.use(db);
		if(tx.entity()){throw new RuntimeException();};
		
		tx.set("gift_id", 1);
		tx.load();
		if(!tx.entity()){throw new RuntimeException();};
		
		System.out.println("=="+tx);
		
		Record tx2=new Record("gift_code");
		tx2.use(db);
		for(FGS fgs:tx2.fields()){
			System.out.println("=="+fgs.getFieldName());
		}
		if(tx2.field("package_name")!=null){
			throw new RuntimeException("Error dynamic model!");
		}
		
		
		
		Map<String, Object> hs=new LinkedHashMap<String, Object>();
		hs.put("x",new RecordTest());
		hs.put("y",new RecordTest());
		Object c=hs.values();
		System.out.println(c);
		Object c2=hs.values();
		System.out.println(c2);
	 	
		RecordTest m=new RecordTest();
		m.set("package_name", "package_name");
		
		m.title="xxx";
		
		m.set("icon_path", "icon_path");
		m.set("description", "description");
		m.set("begin_time", new Date());
		m.set("end_time", new Date());
		m.save();
		
		System.out.println("Create gift: "+m.get("gift_id"));
		
		RecordTest t=new RecordTest();
		t.set("gift_id", m.get("gift_id"));
		t.load();
		System.out.println("loaded: "+t);
		
		
		System.out.println("NEW: "+new RecordTest());
		
		int times=10000;
		long l1=System.currentTimeMillis();
		for(int i=0;i<times;i++){
			RecordTest x=new RecordTest();			 
			x.changedFields();
		}
		long l2=System.currentTimeMillis();
		System.out.println("Use time: "+(l2-l1)+" ms");		
		
		Record sm=new Record("gift").use(db);		
		for(Record x:sm.WHERE()
				.field("package_name").like("package_name")
				.field("giftId").gt(10)
				.forSelect().select()){
			System.err.println(x.toString());
		}
	}

}
