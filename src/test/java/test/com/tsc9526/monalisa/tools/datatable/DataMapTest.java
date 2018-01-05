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
package test.com.tsc9526.monalisa.tools.datatable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.dialect.basic.TestSimpleModel;

import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DataMapTest {

	public void testToUrlQuery()throws Exception{
		DataMap m = new DataMap();
		Assert.assertEquals(m.toUrlQuery(),"");
		
		m.put("X",null);
		m.put("a","");
		m.put("B","b 0");
		Assert.assertEquals(m.toUrlQuery(),"X=&a=&B="+URLEncoder.encode("b 0","utf-8"));
		
		m.put("a",new String[]{"a2","a1"});
		Assert.assertEquals(m.toUrlQuery(),"X=&a=a2&a=a1&B="+URLEncoder.encode("b 0","utf-8"));
	}
	
	public void testTree(){
		DataMap m = new DataMap();
		m.put("A","a");
		m.put("c2","c2");
		m.put("C1","C1");
		m.put("b","b");
		
		TreeMap<String, Object> tree = new TreeMap<String, Object>(m);
		String keys =MelpString.join(tree.keySet().toArray(new String[0]),",");
		
		String[] array = new String[]{"A","c2","C1","b"};
		Arrays.sort(array);
		Assert.assertEquals(keys,MelpString.join(array, ","));
	}
	
	public void testLinked(){
		DataMap m = new DataMap();
		m.put("A","a");
		m.put("c2","c2");
		m.put("C1","C1");
		m.put("b","b");
		
		String keys =MelpString.join(m.keySet().toArray(new String[0]),",");
		Assert.assertEquals(keys, "A,c2,C1,b");
		
		String k2 = "";
		for(Entry<String, Object> entry:m.entrySet()){
			if(k2 .length() ==0 ){
				k2 = entry.getKey();
			}else {
				k2+="," + entry.getKey();
			}
		}
		Assert.assertEquals(k2, "A,c2,C1,b");
	}
	
	public void testFromBean(){
		TestSimpleModel t = new TestSimpleModel();
		t.setIntField1(1);
		 
		DataMap m =DataMap.fromBean(t);
		Assert.assertEquals(m.getInt("intField1",0), 1);
	}
	
	public void testEmpty(){
		Map<String, Object> x = new LinkedHashMap<String, Object>(100);
		x.put("A", null);
		x.put("a", new int[0]);
		
		x.put("B1", "b1");
		x.put("b1", null);
		
		x.put("C1", "c1");
		x.put("c1", "");
		
		x.put("D1", "123");
		x.put("d1", "");
		 
		
		x.put("E1", "  ");
		x.put("e1", "456");
		
		x.put("F1", "null");
		x.put("f1", "789");
		
		List<String> xs=new ArrayList<String>();
		xs.add("xyz");
		x.put("G1", xs);
		x.put("g1", new ArrayList<String>());
		
		DataMap r = DataMap.fromMap(x);
		
		int[] a = r.gets("a");
		Assert.assertEquals(a.length,0);
		
		Assert.assertEquals(r.getString("b1"),"b1");
		Assert.assertEquals(r.getString("c1"),"c1");
		
		Assert.assertEquals(r.getString("d1"),"123");
		Assert.assertEquals(r.getString("e1"),"456");
		Assert.assertEquals(r.getString("f1"),"789");
		
		List<String> rs = r.gets("g1");
		Assert.assertEquals(rs.size(),1);
		Assert.assertEquals(rs.get(0),"xyz");
	}
	
	public void testEquals(){
		DataMap a = new DataMap();
		a.put("A", "a1");
		a.put("B", "b1");
		
		DataMap b = new DataMap();
		b.put("b", "b1");
		b.put("a", "a1");
		
		Assert.assertTrue(a.equals(b));
	}
	
	public void testFromMap(){
		Map<String, String> x = new HashMap<String, String>();
		x.put("A", "a");
		 
		DataMap r = DataMap.fromMap(x);
		
		Assert.assertEquals(r.getString("a"),"a"); 
	}
	
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
		x.put("INTFIELD2",227); //upper case
		x.put("string_field1","zzg");
		
		TestSimpleModel t1=x.as(TestSimpleModel.class);
		Assert.assertEquals(t1.getIntField1().intValue(),226);
		Assert.assertEquals(t1.getIntField2().intValue(),227);
		Assert.assertEquals(t1.getStringField1(),"zzg"); 
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
		 
		Assert.assertEquals(m.getByPath("f1/f2"), "yes");
	}
	
	
	public void testPathObject2()throws Exception{
		Date time=new Date();
		
		DataMap m=DataMap.fromJson("{'f1':{'f2':'yes'}}");
		
		m.put("f3", new TestSimpleModel().setDateField1(time));
		
		Assert.assertEquals(m.getByPath("f1/f2"), "yes");
		
		//Test get model's field by field name
		Assert.assertEquals(m.getByPath("f3/dateField1"), time);
		
		//Test get model's field by column name
		Assert.assertEquals(m.getByPath("f3/date_field1"), time);
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
		
		Assert.assertEquals(m.getByPath("first-name"),"zzg");
		Assert.assertEquals(m.getByPath("/first-name"),"zzg");
		
		Assert.assertEquals(m.getByPath("/obj1/oc/xx1"),"yes");
		Assert.assertEquals(m.getByPath("/obj1/oc/xx2"),"");
		Assert.assertEquals(m.getByPath("/obj1/oc/xx2.label"),"no");
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
		
		Assert.assertEquals(m.getByPath("first-name"),"zzg");
		Assert.assertEquals(m.getByPath("/first-name"),"zzg");
		
		Assert.assertEquals(m.getByPath("/obj1/oc/xx1"),"yes");
		Assert.assertEquals(m.getByPath("/obj1/oc/xx2"),"");
		Assert.assertEquals(m.getByPath("/obj1/oc/xx2.label"),"no");
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
		
		Assert.assertEquals( ((Number)m.getByPath("f5/x1")).longValue(),13012345678L);
	
		Assert.assertEquals(m.getByPath("f5/x2"),"xx");
		Assert.assertEquals(((Number)m.getByPath("f5/x3")).longValue(),1234567812345678L);
		 
		DataMap m1=(DataMap)m.get("f5");
		Assert.assertEquals(m1.getLong("x3")   ,new Long(1234567812345678L));
		Assert.assertEquals(m1.getString("x3") ,"1234567812345678");
	}
	

	public void testFromJson3(){
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
			+ "\r\n}	"
		+ "\r\n"/**}*/;
			
		DataMap m=DataMap.fromJson(json);
		
		Assert.assertEquals(m.get("f0"),1L);
		Assert.assertEquals(m.get("f1"),1.5D);
		Assert.assertEquals(m.get("f3"),Boolean.TRUE);
		Assert.assertNull(m.get("f4"));
		Assert.assertEquals(m.getByPath("f5/x1"),13012345678L);
		Assert.assertEquals(m.getByPath("f5/x2"),"xx");
		Assert.assertEquals(m.getByPath("f5/x3"),1234567812345678L);
	}
	
	public void testToJsonPretty(){
		DataMap x=new DataMap();
		x.put("a","a");
		x.put("b",100);
		x.put("c",new Integer(1));
	 	
		Assert.assertEquals(x.toJson(),x.toJson(true));
		
		String x1=x.toJson();
		String x2=(""+/**~!{*/""
				+ "{"
				+ "\r\n  \"a\": \"a\","
				+ "\r\n  \"b\": 100,"
				+ "\r\n  \"c\": 1"
				+ "\r\n}"
		+ "\r\n"/**}*/).trim();
		
		x1=x1.replace("\r","");
		x2=x2.replace("\r","");
		
		
		Assert.assertEquals(x1,x2);
	}
	
	public void testToJson(){
		DataMap x=new DataMap();
		x.put("a","a");
		x.put("b",100);
		x.put("c",new Integer(1));
		
		Assert.assertNotEquals(x.toJson(),x.toJson(false));
		
		String x1=x.toJson(false);
		String x2=(""+/**~!{*/""
			+ "{\"a\":\"a\",\"b\":100,\"c\":1}"
		+ "\r\n"/**}*/).trim();
	 	
		Assert.assertEquals(x1,x2);
	}
	
	public void testArrayByPath(){
		String json=""+/**~!{*/""
			+ "{"
			+ "\r\n	\"f1\":{"
			+ "\r\n		\"f2\":['a','b','c']"
			+ "\r\n	}"
			+ "\r\n}	"
		+ "\r\n"/**}*/;
				
		DataMap m=DataMap.fromJson(json);
		Assert.assertEquals(m.getByPath("f1/f2[1]"), "a");
		Assert.assertEquals(m.getByPath("f1/f2[2]"), "b");
		Assert.assertEquals(m.getByPath("f1/f2[3]"), "c");
		
		Assert.assertEquals(m.getByPath("f1/f2[-1]"), "c");
		Assert.assertEquals(m.getByPath("f1/f2[-2]"), "b");
		Assert.assertEquals(m.getByPath("f1/f2[-3]"), "a");
	}
	
	
	public void testParseFromArray(){
		String json=""+/**~!{*/""
				+ "["
				+ "\r\n	{"
				+ "\r\n		\"f\":'x1'"
				+ "\r\n	},"
				+ "\r\n	{"
				+ "\r\n		\"f\":\"x2\""
				+ "\r\n	}"
				+ "\r\n]	"
			+ "\r\n"/**}*/;
		
		DataMap m=DataMap.fromJson(json);
		Assert.assertEquals(m.size(), 2);
		
		for(int i=1;i<=2;i++){
			DataMap f=m.gets(i-1);
			Assert.assertEquals(f.getString("f"), "x"+i);
		}
	}
	
	
	public void testEmptyBooleanArray(){
		DataMap m =new DataMap();
		m.put("a",new boolean[0]);
		
		Boolean x=m.getBoolean("a");
		Assert.assertFalse(x);
	}
	
	public void testEmptyDateArray(){
		DataMap m =new DataMap();
		m.put("a",new Date[0]);
		
		Date x=m.getDate("a");
		Assert.assertNull(x);
	}
	
	public void testEmptyDoubleArray(){
		DataMap m =new DataMap();
		m.put("a",new double[0]);
		
		Double x=m.getDouble("a");
		Assert.assertNull(x);
	}
	
	public void testEmptyFloatArray(){
		DataMap m =new DataMap();
		m.put("a",new float[0]);
		
		Float x=m.getFloat("a");
		Assert.assertNull(x);
	}
	
	public void testEmptyIntegerArray(){
		DataMap m =new DataMap();
		m.put("a",new int[0]);
		m.put("b",null);
		m.put("c",1);
		m.put("d",new int[]{9});
		
		Assert.assertNull(m.getInteger("a"));
		Assert.assertNull(m.getInteger("b"));
		Assert.assertEquals(m.getInteger("c"),Integer.valueOf(1));
		Assert.assertEquals(m.getInteger("d"),Integer.valueOf(9));
	}
	
	public void testEmptyLongArray(){
		DataMap m =new DataMap();
		m.put("a",new int[0]);
		
		Long x=m.getLong("a");
		Assert.assertNull(x);
	}
	
	public void testEmptyStringArray(){
		DataMap m =new DataMap();
		m.put("a",new String[0]);
		
		String x=m.getString("a");
		Assert.assertEquals(x, "");
	}
}
