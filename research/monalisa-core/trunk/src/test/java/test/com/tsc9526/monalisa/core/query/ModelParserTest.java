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


import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.tools.JsonHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelParserTest {
	 
	public void testParseDate() {
		TestSimpleModel model=new TestSimpleModel();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long t1=System.currentTimeMillis();
		Date now=new Date();
		
		String json="{\"dateField1\":"+t1+", \"date_field2\":\""+sdf.format(now)+"\" }";		
		model.parse(json);
		
		Assert.assertEquals(model.getDateField1().getTime(),t1);
		
		Assert.assertEquals(sdf.format(model.getDateField2()),sdf.format(now));
	}
	
	public void testToJson(){
		TestSimpleModel model=new TestSimpleModel();
		  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long t1=System.currentTimeMillis();
		Date now=new Date();
			
		String json="{\"dateField1\":"+t1+", \"date_field2\":\""+sdf.format(now)+"\" }";		
		model.parse(json);
		
		String xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		xmlString+="<TestSimpleModel>\r\n";
		xmlString+="  <dateField1>"+sdf.format(new Date(t1))+"</dateField1>\r\n";
		xmlString+="  <dateField2>"+sdf.format(model.getDateField2())+"</dateField2>\r\n";
		xmlString+="</TestSimpleModel>";
		Assert.assertEquals(model.toXml(), xmlString);
		
		Assert.assertTrue(model.toJson().indexOf(sdf.format(new Date(t1)))>0);
	}
	
	public void testJsonNull(){
		TestSimpleModel model=new TestSimpleModel();
		
		String json="{\"string_field1\":null, \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertNull(model.getStringField1());
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testJsonArray(){
		TestSimpleModel model=new TestSimpleModel();
		
		String json="{\"string_field1\":[\"a\",\"b\"], \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertEquals(model.getStringField1(),"[\"a\",\"b\"]");
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testMapping1(){
		TestSimpleModel model=new TestSimpleModel();
		 
		String json="{\"s1\":[\"a\",\"b\"], \"s2\":\"123\" }";		
		model.parse(json,"s1=string_field1,s2=string_field2");
		
		Assert.assertEquals(model.getStringField1(),"[\"a\",\"b\"]");
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testMapping2(){
		TestSimpleModel model=new TestSimpleModel();
		 
		String json="{\"s1\":[\"a\",\"b\"], \"s2\":\"123\" }";		
		model.parse(json,"s1=string_field1","s2=string_field2");
		
		Assert.assertEquals(model.getStringField1(),"[\"a\",\"b\"]");
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testArray(){
		TestSimpleModel model=new TestSimpleModel();
		
		String json="{\"array_1\":[\"a\",\"b\"], \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertTrue(model.getArray1().length==2);
		Assert.assertEquals(model.getArray1()[0],"a");
		Assert.assertEquals(model.getArray1()[1],"b");
		Assert.assertEquals(model.getStringField2(),"123");
	}

	public void testArrayJson(){
		TestSimpleModel model=new TestSimpleModel();
		
		String json="{\"array_1\":[{a:1},\"b\"], \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertTrue(model.getArray1().length==2);
		Assert.assertEquals(model.getArray1()[0],"{\"a\":1}");
		Assert.assertEquals(model.getArray1()[1],"b");
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testParseObjectOne(){
		Date now=new Date();
		TestSimpleModel model=new TestSimpleModel();
		TestSimpleObject objectOne=new TestSimpleObject();
		objectOne.setOne("ooo");
		objectOne.setThree(now);
		model.setObjectOne(objectOne);
		
		Query query=model.dialect().insert(model, true);
		String sql=query.getExecutableSQL();		 
		Assert.assertTrue(sql.indexOf("ooo")>0);
		
		TestSimpleObjectTwo objectTwo=new TestSimpleObjectTwo();
		objectTwo.setObj(objectOne);
		objectTwo.setFs("fx");
		String jsonTwo=JsonHelper.getGson().toJson(objectTwo);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		FGS fgs=model.field("object_two");
		fgs.setObject(model, jsonTwo);
		Assert.assertEquals(model.getObjectTwo().getFs(),objectTwo.getFs());
		Assert.assertEquals(sdf.format(model.getObjectTwo().getObj().getThree())
				,sdf.format(objectTwo.getObj().getThree()));
	}
}
