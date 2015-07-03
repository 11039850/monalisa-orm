package test.com.tsc9526.monalisa.core.query;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

@Test
public class ModelEnumTest {
  
  public void testParseRemark()throws Exception {
	  SimpleModel model=new SimpleModel();
	  MetaClass mc= ClassHelper.getMetaClass(model.getClass());
	  mc.getField("status").setObject(model, StatusA.ERROR);
	  
	  Assert.assertEquals(model.getStatus(),StatusA.ERROR);
	  
	  mc.getField("status").setObject(model, "OK");
	  Assert.assertEquals(model.getStatus(),StatusA.OK);
	  
	  mc.getField("status").setObject(model, 2);
	  Assert.assertEquals(model.getStatus(),StatusA.NOT_FOUND);
  }
}
