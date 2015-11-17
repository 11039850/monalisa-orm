package test.com.tsc9526.monalisa.core.query;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelEnumTest {
  
  public void testParseRemark()throws Exception {
	  TestSimpleModel model=new TestSimpleModel();
	  MetaClass mc= ClassHelper.getMetaClass(model.getClass());
	  mc.getField("status").setObject(model, StatusA.ERROR);
	  
	  Assert.assertEquals(model.getStatus(),StatusA.ERROR);
	  
	  mc.getField("status").setObject(model, "OK");
	  Assert.assertEquals(model.getStatus(),StatusA.OK);
	  
	  mc.getField("status").setObject(model, 2);
	  Assert.assertEquals(model.getStatus(),StatusA.NOT_FOUND);
	  
	  
	  mc.getField("statusB").setObject(model, 1);
	  Assert.assertEquals(model.getStatusB(),StatusB.B1);
	  
	  mc.getField("statusB").setObject(model, 2);
	  Assert.assertEquals(model.getStatusB(),StatusB.B2);
	  
	  mc.getField("statusB").setObject(model, "B3");
	  Assert.assertEquals(model.getStatusB(),StatusB.B3);
	  
  }
}
