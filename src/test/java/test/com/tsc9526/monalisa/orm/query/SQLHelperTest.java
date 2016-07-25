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
package test.com.tsc9526.monalisa.orm.query;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.tools.helper.SQLHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
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
		Assert.assertEquals(ws.size(),19);
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
