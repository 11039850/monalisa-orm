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
package test.com.tsc9526.monalisa.tools.string;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class MelpStringTest {

	public void testUrlEncode() {
		String s1 = "%123";
		
		String result = MelpString.urlEncodeUtf8(s1);
		Assert.assertEquals(result,"%25123");
		
		String s2 = MelpString.urlDecodeUtf8(result);
		Assert.assertEquals(s2,s1);
		
	}
}
