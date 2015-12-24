package test.com.tsc9526.monalisa.core.query;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.tools.SQLHelper;

@Test
public class SQLHelperTest {

	public void testsplitKeyWords1(){
		String w="1=1";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),3);
		Assert.assertEquals(ws.get(0),"1");
		Assert.assertEquals(ws.get(1),"=");
		Assert.assertEquals(ws.get(2),"1");
		
	}
	
	public void testsplitKeyWords2(){
		String w=", user b where s='gg' and \"g\\\"x\"=g and f=\"ff\" and x='x'";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),18);
		Assert.assertTrue(ws.contains("WHERE"));		
	}
	
	public void testsplitKeyWords3(){
		String w=" left join user b on a.x=b.x";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),8);
		Assert.assertTrue(ws.contains("JOIN"));		
	}
	
	public void testsplitKeyWords4(){
		String w="right join\r user b on a.x=b.x";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),8);
		Assert.assertTrue(ws.contains("JOIN"));		
	}
	
	public void testsplitKeyWords5(){
		String w="right join\nuser b on a.x=b.x";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),8);
		Assert.assertTrue(ws.contains("JOIN"));		
	}
	
	public void testsplitKeyWords6(){
		String w="right join\tuser b on a.x=b.x";
		
		List<String> ws=SQLHelper.splitKeyWords(w);		
		Assert.assertEquals(ws.size(),8);
		Assert.assertTrue(ws.contains("JOIN"));		
	}
 
}
