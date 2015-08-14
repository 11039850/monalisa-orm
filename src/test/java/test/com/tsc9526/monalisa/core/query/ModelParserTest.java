package test.com.tsc9526.monalisa.core.query;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ModelParserTest {

	 
	public void testParseDate() {
		SimpleModel model=new SimpleModel();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long t1=System.currentTimeMillis();
		Date now=new Date();
		
		String json="{\"dateField1\":"+t1+", \"date_field2\":\""+sdf.format(now)+"\" }";		
		model.parse(json);
		
		Assert.assertEquals(model.getDateField1().getTime(),t1);
		
		Assert.assertEquals(sdf.format(model.getDateField2()),sdf.format(now));
	}
	
	public void testJsonNull(){
		SimpleModel model=new SimpleModel();
		
		String json="{\"string_field1\":null, \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertNull(model.getStringField1());
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testJsonArray(){
		SimpleModel model=new SimpleModel();
		
		String json="{\"string_field1\":[\"a\",\"b\"], \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertEquals(model.getStringField1(),"[\"a\",\"b\"]");
		Assert.assertEquals(model.getStringField2(),"123");
	}
	
	public void testArray(){
		SimpleModel model=new SimpleModel();
		
		String json="{\"array_1\":[\"a\",\"b\"], \"string_field2\":\"123\" }";		
		model.parse(json);
		
		Assert.assertTrue(model.getArray1().length==2);
		Assert.assertEquals(model.getArray1()[0],"a");
		Assert.assertEquals(model.getArray1()[1],"b");
		Assert.assertEquals(model.getStringField2(),"123");
	}

}
