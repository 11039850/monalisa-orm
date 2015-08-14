package test.com.tsc9526.monalisa.core.query;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ModelParserTest {

	@SuppressWarnings("deprecation")
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

}
