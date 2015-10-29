package test.com.tsc9526.monalisa.core.query;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.query.Query;

@Test
public class QueryMultiTableTest {

	public void testSelect1() {
		SimpleModel model=new SimpleModel();
		Query query=model.dialect().select(model,",user b on a.id=b.id and a.x=?",1);
		
		String expect="SELECT a.* FROM `simple_model` a ,user b on a.id=b.id and a.x=1";
		Assert.assertEquals(query.getExecutableSQL(),expect);				
	}
	
	public void testSelect2() {
		SimpleModel model=new SimpleModel();
		
		model.include("int_field1","string_field2");
		Query query=model.dialect().select(model,"left join user b on a.id=b.id and a.x=?",1);
		String expect="SELECT a.`auto`, a.`int_field1`, a.`string_field2` FROM `simple_model` a left join user b on a.id=b.id and a.x=1";
		Assert.assertEquals(query.getExecutableSQL(),expect);
	}

}
