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
package test.com.tsc9526.monalisa.tools.json;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.json.MelpJson;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class JsonTest {

	@Test(invocationCount=5)
	public void testPretty() {
		Map<String,Object> m=new LinkedHashMap<String,Object>();
		
		m.put("n1","v1");
		m.put("n2","v2");
		
		String value=MelpJson.toJsonPretty(m).replace("\r\n","\n").trim();
		String expected=(""+/**~!{*/""
			+ "{"
			+ "\r\n  \"n1\": \"v1\","
			+ "\r\n  \"n2\": \"v2\""
			+ "\r\n}"
		+ "\r\n"/**}*/).trim().replace("\r\n","\n");
		Assert.assertEquals(value, expected);
		
		value = MelpJson.toJson(m).trim();
		expected=(""+/**~!{*/""
			+ "{\"n1\":\"v1\",\"n2\":\"v2\"}"
		+ "\r\n"/**}*/).trim();
		
		Assert.assertEquals(value, expected); 
		
	}
}
