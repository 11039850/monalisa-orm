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
package test.com.tsc9526.monalisa.orm.dialect.basic;
 
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.model.Record;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.string.MelpDate;
 
 
/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class BaseRecordTest extends Model<BaseRecordTest>{ 
	private static final long serialVersionUID = -1974865252589672370L;

	String title;
	int version;
	
	public BaseRecordTest() {
		 super("test_record");
	}
	
	public String getTitle() {
		return title;
	}

	public BaseRecordTest setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public int getVersion() {
		return version;
	}
	
	public BaseRecordTest setVersion(int version) {
		this.version = version;
		return this;
	}
 
	
	public abstract DBConfig  getDB();
	public abstract String    getInitSqls(); 
	public abstract String    getCleanSqls(); 
	
	@BeforeClass
	public void setUp(){
		for(String sql:getCleanSqls().split(";")) {
			sql = sql.trim();
			if(sql.length()>1) {
				getDB().execute(sql);
			}
		}
		
		for(String sql:getInitSqls().split(";")) {
			sql = sql.trim();
			if(sql.length()>1) {
				getDB().execute(sql);
			}
		}
	}
	
	@AfterClass
	public void tearDown(){
		 
	}
	  
	protected BaseRecordTest createRecord()throws Exception{
		BaseRecordTest tx = this.getClass().newInstance(); 
		Assert.assertEquals(getDB(), tx.db());
		Assert.assertEquals(tx.entity(),false); 
		return tx;
	}
	 

	@Test
	public void testRecordAutoTime()throws Exception{
		BaseRecordTest tx = createRecord();
		tx.setTitle("TX");
		tx.set("name","Test-Tx");
		tx.set("ts_a", new Date());
		tx.save();
		
		int id=tx.get("record_id");
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertTrue(id>1);
	 
		BaseRecordTest tx2=createRecord().set("record_id", id).load();
		Assert.assertEquals(tx.getTitle(),tx2.getTitle());
		Assert.assertTrue(tx2.get("create_time")!=null);
		
		Assert.assertEquals(tx.update(),1);
		tx2=createRecord().set("record_id", id).load();
		Assert.assertTrue(tx2.get("update_time")!=null);
	}
	
	@Test
	public void testRecordDelete() throws Exception{
		String flag = "test-delete";
		
		Record record =new Record("test_record").use(getDB());
		long x = record.WHERE().field("title").eq(flag).SELECT().count();
		Assert.assertEquals(x,0);
		
		BaseRecordTest tx = createRecord();
		int r = tx.defaults().setTitle(flag).save();
		Assert.assertEquals(r,1);
		x = record.WHERE().field("title").eq(flag).SELECT().count();
		Assert.assertEquals(x,1);
		
		Assert.assertEquals(tx.delete(),1);
		x = record.WHERE().field("title").eq(flag).SELECT().count();
		Assert.assertEquals(x,0);
	}
	
	@Test
	public void testRecordDateTime()throws Exception{
		String ts = "2018-10-30 11:11:11";
		
		BaseRecordTest tx = createRecord();
		tx.defaults();
		tx.set("ts_a", MelpDate.toDate(ts));
		tx.save();
		
		int id=tx.get("record_id");
		BaseRecordTest tx2=createRecord().set("record_id", id).load(); 
		
		Date tsa=tx2.get("ts_a");
		Assert.assertEquals(MelpDate.toTime(tsa),ts);
	}
	
	@Test
	public void testRecordChangeFields()throws Exception{
		BaseRecordTest m = createRecord();
		m.set("name", "package_name");
		m.title="xxx";
		m.save();
		  
		int times=10000;
		long l1=System.currentTimeMillis();
		for(int i=0;i<times;i++){
			BaseRecordTest x = createRecord();
			x.changedFields();
		}
		long l2=System.currentTimeMillis();
		System.out.println("Changed fields: "+times+", use time: "+(l2-l1)+" ms");		
		
		Assert.assertTrue( (l2-l1) <1000);
	}
	
	

	@Test
	public void testRecordToMap()throws Exception{
		BaseRecordTest tx = createRecord();
		tx.set("record_id", 1);
		tx.load();
		Assert.assertEquals(tx.entity(),true);
		
		DataMap m1 = tx.toMap(false);
		Assert.assertEquals(1, m1.getInt("record_id",0));
		
		DataMap m2 = tx.toMap(true);
		Assert.assertEquals(1, m2.getInt("recordId",0));
	}
	
	@Test
	public void testRecordSelect()throws Exception{
		Record sm=new Record("test_record").use(getDB());
		DataTable<Record> rs=sm.WHERE()
		.field("title").like("record")
		.field("record_id").gt(0)
		.SELECT().select();
		
		Assert.assertTrue(rs.size()>0);
	}

	
	@Test
	public void testRecordLoad()throws Exception{
		Record tx=new Record("test_record").use(getDB());
		Assert.assertEquals(getDB(), tx.db());
		Assert.assertTrue(tx.fields().size()>1);
		Assert.assertTrue(tx.field("title")!=null);
		Assert.assertEquals(tx.entity(),false); 
		 	
		tx.set("record_id", 1);
		tx.load();
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertEquals(tx.get("title"),"record"); 
		
		DataTable<DataMap> rs=getDB().select("select count(*) from test_record");
		Assert.assertTrue(rs.size()>0);
	}
	
	@Test
	public void testRecordSaveAutoIncrease()throws Exception{
		Record tx=new Record("test_record").use(getDB()); 
		tx.set("name",  "ns001");
		tx.set("title", "title001");
		tx.save();
		
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertTrue(tx.getInt("record_id",0)>0);  
	}
	
	@Test
	public void testRecordTimestampLong()throws Exception{
		long ts=System.currentTimeMillis();
		
		Record tx=new Record("test_record").use(getDB()); 
		tx.set("name",  "ns001");
		tx.set("title", "title001");
		tx.set("ts_a", ts);
		tx.save();
		
		Assert.assertEquals(tx.entity(),true); 
		Assert.assertTrue(tx.get("record_id")!=null);  
		
		Assert.assertEquals(tx.getDate("ts_a").getTime()/1000,ts/1000); 
	}
	
	@Test
	public void testSaveOrUpdate()throws Exception{
		Record r = getDB().createRecord("test_record","record_id");
	 	r.set("title","save_update_001");
	 	Assert.assertEquals(r.saveOrUpdate(),1);
		
		int record_id = r.getInt("record_id", 0);
		Assert.assertTrue(record_id>0);
		 
		Assert.assertEquals(r.WHERE().field("title").eq("save_update_001").SELECT().select().size(),1);
		
		Record r2 = getDB().createRecord("test_record","record_id");
		r2.set("record_id",record_id);
		r2.set("title","save_update_002");
		Assert.assertTrue(r2.saveOrUpdate()<=2); //MySQL INSERT ... ON DUPLICATE KEY UPDATE ... returns:  either 1 or 2
		
		Assert.assertEquals(r.WHERE().field("title").eq("save_update_001").SELECT().select().size(),0);
		Assert.assertEquals(r.WHERE().field("title").eq("save_update_002").SELECT().select().size(),1);
	}
	
	@Test
	public void testSelectLimit()throws Exception{
		for(int i=0;i<10;i++) {
			Record r = getDB().createRecord("test_record","record_id");
			r.set("name","limit-"+i);
			r.save();
		}
		
		Record record = getDB().createRecord("test_record","record_id");
		DataTable<Record> rs1 = record.WHERE().field("name").like("limit%").field("name").asc().SELECT().select(2, 0);
		Assert.assertEquals(rs1.get(0).get("name"),"limit-0");  
		Assert.assertEquals(rs1.get(1).get("name"),"limit-1");  
		Assert.assertEquals(rs1.size(),2);
		
		 
		DataTable<Record> rs2 = record.WHERE().field("name").like("limit%").field("name").asc().SELECT().select(2, 1);
		Assert.assertEquals(rs2.get(0).get("name"),"limit-1");  
		Assert.assertEquals(rs2.get(1).get("name"),"limit-2");  
		Assert.assertEquals(rs2.size(),2);		 
	}
	
	@Test
	public void testTableFormat()throws Exception{
		Record r = getDB().createRecord("test_record","record_id");
		String content = r.SELECT().select(2, 0).format();
		Assert.assertTrue(content.length()>1);
		  
		Assert.assertTrue(content.indexOf("title")>=0);
		Assert.assertTrue(content.indexOf("\n")>=0);
	}
	
	
	@Test
	public void testByVersion()throws Exception{
		BaseRecordTest x = createRecord();
		x.defaults().set("name","update-by-version-0");
		x.setTitle("REMOVE-0");
		x.setVersion(1);
		int r=x.save();
		Assert.assertEquals(1,r);
		
		Record record = getDB().createRecord("test_record","record_id");
		 
		Record r1 = record.SELECT().selectOne("record_id =? ",x.get("record_id"));
		Record r2 = record.SELECT().selectOne("record_id =? ",x.get("record_id"));
		
		r=r1.set("update_by","zzg-1").updateByVersion();
		Assert.assertEquals(1,r);
		
		r=r2.set("update_by","zzg-2").updateByVersion();
		Assert.assertEquals(0,r);
		
		Record rx = record.SELECT().selectOne("record_id =? ",x.get("record_id"));
		Assert.assertEquals(rx.get("update_by"),"zzg-1");
	}
	
}
