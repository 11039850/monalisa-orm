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
package test.com.tsc9526.monalisa.orm.datatable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.query.TestSimpleModel;

import com.tsc9526.monalisa.orm.datatable.DataMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DataMapTest {

	public void testAsDataMap(){
		DataMap x=new DataMap();
		x.put("a","a");
		
		DataMap t1=x.as(DataMap.class);
		Assert.assertTrue(t1==x);
	}
	
	public void testAsMap(){
		DataMap x=new DataMap();
		x.put("a","a");
		
		Map<?,?> t1=x.as(Map.class);
		Assert.assertTrue(t1==x);
	}
	
	public void testAsTestSimpleModel(){
		DataMap x=new DataMap();
		x.put("intField1",226);
		
		TestSimpleModel t1=x.as(TestSimpleModel.class);
		Assert.assertEquals(t1.getIntField1().intValue(),226);
	}
	

	public void testIndex()throws Exception {
		SimpleDateFormat yyyyMMddHHmmss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat yyyyMMddHHmm  =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat yyyyMMddHH    =new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat yyyyMMdd      =new SimpleDateFormat("yyyy-MM-dd");
		
		String d1="2006-03-05 15:15:15";
		String d2="2006-03-05 15:15";
		String d3="2006-03-05 15";
		String d4="2006-03-05";
	 
		
		DataMap m=new DataMap();
		m.put("a", "a");
		m.put("B", "b");
		m.put("d1",d1);
		m.put("d2",d2);
		m.put("d3",d3);
		m.put("d4",d4);
		
		Assert.assertEquals("a",m.get(0));
		Assert.assertEquals("b",m.get("b"));
		
		Assert.assertEquals(yyyyMMddHHmmss.parse(d1).getTime(),m.getDate("d1").getTime());
		Assert.assertEquals(yyyyMMddHHmm.parse(d2).getTime(),m.getDate("d2").getTime());
		Assert.assertEquals(yyyyMMddHH.parse(d3).getTime(),m.getDate("d3").getTime());
		Assert.assertEquals(yyyyMMdd.parse(d4).getTime(),m.getDate("d4").getTime());
	}
	
	public void testCaseInsensitive(){
		DataMap row=new DataMap();
		row.put("AbC","aBC");
		
		Map<String,Object> map=(Map<String,Object>)row;
		
		Assert.assertEquals(map.get("abc"),"aBC");
		Assert.assertEquals(map.get("ABC"),"aBC");
		Assert.assertEquals(map.get("Abc"),"aBC");
	}

	
	public void testKeySensitive1(){
		DataMap row=new DataMap();
		row.put("AbC","aBC");
		
		String key=row.keySet().iterator().next();
		Assert.assertEquals(key, "AbC");
	}
	
	public void testKeySensitive2(){
		DataMap row=new DataMap();
		row.put("Abc","aBC");
		
		row.put("ABC","xxx");
		
		String key=row.keySet().iterator().next();
		Assert.assertEquals(key, "Abc");
		
		Assert.assertEquals(row.get(key),"xxx");
	}
	
	public void testNull(){
		DataMap row=new DataMap();
		row.put(null,"aBC");
		
		Assert.assertEquals(row.get(null),"aBC");
		
		String key=row.keySet().iterator().next();
		Assert.assertEquals(key, null);
	}
	
	public void testSerial()throws Exception{
		DataMap row=new DataMap();
		row.put("AbC","aBC");
		
		ByteArrayOutputStream buf=new ByteArrayOutputStream();
		ObjectOutputStream out=new ObjectOutputStream(buf);
		out.writeObject(row);
		out.close();
		
		ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
		DataMap rs=(DataMap)in.readObject();
		
		Assert.assertEquals(rs.getString("abc"),"aBC");
		
		String key=row.keySet().iterator().next();
		Assert.assertEquals(key, "AbC");
		
	}
	
	public void testPathObject()throws Exception{
		DataMap m=DataMap.fromJson("{'f1':{'f2':'yes'}}");
		 
		Assert.assertEquals(m.getPath("f1/f2"), "yes");
	}
	
	
	public void testPathObject2()throws Exception{
		Date time=new Date();
		
		DataMap m=DataMap.fromJson("{'f1':{'f2':'yes'}}");
		
		m.put("f3", new TestSimpleModel().setDateField1(time));
		
		Assert.assertEquals(m.getPath("f1/f2"), "yes");
		Assert.assertEquals(m.getPath("f3/dateField1"), time);
	}
	
	public void testFromXml()throws Exception{
		String xml=""+/**~!{*/""
			+ "<xml>"
			+ "\r\n	<first-name>zzg</first-name>"
			+ "\r\n	<last-name>zhou</last-name>"
			+ "\r\n	<obj1 name=\"zzg\">"
			+ "\r\n		<a1>xxa</a1>"
			+ "\r\n		<b1>xxb</b1>"
			+ "\r\n		<oc title=\"xyz\">"
			+ "\r\n			<xx1>yes</xx1>"
			+ "\r\n			<xx2 label=\"no\"/>"
			+ "\r\n		</oc>"
			+ "\r\n	</obj1>"
			+ "\r\n</xml>"
		+ "\r\n"/**}*/;
		
		DataMap m=DataMap.fromXml(xml);
		
		Assert.assertEquals(m.getString("first-name"),"zzg");
		Assert.assertEquals(m.getString("last-name"),"zhou");
		Assert.assertEquals(m.getString("obj1.name"),"zzg");
		
		DataMap m1=(DataMap)m.get("obj1");
		Assert.assertEquals(m1.getString("a1"),"xxa");
		Assert.assertEquals(m1.getString("b1"),"xxb");
		Assert.assertEquals(m1.getString("oc.title"),"xyz");
		
		DataMap m2=(DataMap)m1.get("oc");
		Assert.assertEquals(m2.getString("xx1"),"yes");
		Assert.assertEquals(m2.getString("xx2"),"");
		Assert.assertEquals(m2.getString("xx2.label"),"no");
		
		Assert.assertEquals(m.getPath("first-name"),"zzg");
		Assert.assertEquals(m.getPath("/first-name"),"zzg");
		
		Assert.assertEquals(m.getPath("/obj1/oc/xx1"),"yes");
		Assert.assertEquals(m.getPath("/obj1/oc/xx2"),"");
		Assert.assertEquals(m.getPath("/obj1/oc/xx2.label"),"no");
	}
	
	public void testFromJson()throws Exception{
		String json=""+/**~!{*/""
			+ "{"
			+ "\r\n	\"first-name\":\"zzg\","
			+ "\r\n	\"last-name\":\"zhou\","
			+ "\r\n	\"obj1.name\":\"zzg\","
			+ "\r\n	\"obj1\":{"
			+ "\r\n	 	\"obj1.name\":\"zzg\","
			+ "\r\n		\"a1\":\"xxa\","
			+ "\r\n		\"b1\":\"xxb\","
			+ "\r\n		\"oc.title\":\"xyz\","
			+ "\r\n		\"oc\":{"
			+ "\r\n			\"xx1\":\"yes\","
			+ "\r\n			\"xx2\":\"\","
			+ "\r\n			\"xx2.label\":\"no\""
			+ "\r\n		}"
			+ "\r\n	}"
			+ "\r\n}"
		+ "\r\n"/**}*/;
		
		DataMap m=DataMap.fromJson(json);
		
		Assert.assertEquals(m.getString("first-name"),"zzg");
		Assert.assertEquals(m.getString("last-name"),"zhou");
		Assert.assertEquals(m.getString("obj1.name"),"zzg");
		
		DataMap m1=(DataMap)m.get("obj1");
		Assert.assertEquals(m1.getString("a1"),"xxa");
		Assert.assertEquals(m1.getString("b1"),"xxb");
		Assert.assertEquals(m1.getString("oc.title"),"xyz");
		
		DataMap m2=(DataMap)m1.get("oc");
		Assert.assertEquals(m2.getString("xx1"),"yes");
		Assert.assertEquals(m2.getString("xx2"),"");
		Assert.assertEquals(m2.getString("xx2.label"),"no");
		
		Assert.assertEquals(m.getPath("first-name"),"zzg");
		Assert.assertEquals(m.getPath("/first-name"),"zzg");
		
		Assert.assertEquals(m.getPath("/obj1/oc/xx1"),"yes");
		Assert.assertEquals(m.getPath("/obj1/oc/xx2"),"");
		Assert.assertEquals(m.getPath("/obj1/oc/xx2.label"),"no");
	}
	
	
	public void testFromJson2()throws Exception{
		String json=""+/**~!{*/""
			+ "{"
			+ "\r\n	\"f0\":1,"
			+ "\r\n	\"f1\":1.5,"
			+ "\r\n	\"f2\":\"ss\","
			+ "\r\n	\"f3\":true,"
			+ "\r\n	\"f4\":null,"
			+ "\r\n	\"f5\":{"
			+ "\r\n	 	\"x1\":13012345678,"
			+ "\r\n		\"x2\":\"xx\","
			+ "\r\n		\"x3\":1234567812345678"
			+ "\r\n	}"
			+ "\r\n}"
		+ "\r\n"/**}*/;
		
		DataMap m=DataMap.fromJson(json);
		
		Assert.assertEquals(m.getString("f0"),"1");
		Assert.assertEquals(m.getString("f1"),"1.5");
		Assert.assertEquals(m.getFloat("f1"),new Float(1.5));
		
		Assert.assertEquals(m.getBoolean("f3"),Boolean.TRUE);
		Assert.assertNull(m.get("f4"));
		
		Assert.assertEquals( ((Number)m.getPath("f5/x1")).longValue(),13012345678L);
	
		Assert.assertEquals(m.getPath("f5/x2"),"xx");
		Assert.assertEquals(((Number)m.getPath("f5/x3")).longValue(),1234567812345678L);
		 
		DataMap m1=(DataMap)m.get("f5");
		Assert.assertEquals(m1.getLong("x3")   ,new Long(1234567812345678L));
		Assert.assertEquals(m1.getString("x3") ,"1234567812345678");
	}
}
