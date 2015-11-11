package test.com.tsc9526.monalisa.core.query;

import java.io.ByteArrayOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.generator.DBTableGenerator;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.query.model.Model;

@Test
public class TemplateTest {

	 
	public void testGenerateModel()throws Exception{
		MetaTable mTable=new MetaTable("test_table");
		String modelClass=Model.class.getName();
		String dbi="test.dbi";
		mTable.setJavaPackage("test");
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		DBTableGenerator tpl=new DBTableGenerator(mTable,modelClass,dbi);
		tpl.generate(bos);
		
		Assert.assertTrue(bos.size()>500);
		
	}
}
