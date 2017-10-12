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
package test.com.tsc9526.monalisa.tools.io;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.io.MelpFile;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class MelpFileTest {

	public void testCombinePath(){
		Assert.assertEquals(MelpFile.combinePath("/tmp","path")  ,"/tmp/path");
		Assert.assertEquals(MelpFile.combinePath("/tmp/","/path"),"/tmp/path");
		Assert.assertEquals(MelpFile.combinePath("/tmp//","path"),"/tmp/path");
		Assert.assertEquals(MelpFile.combinePath("/tmp//","path/"),"/tmp/path/");
		
		Assert.assertEquals(MelpFile.combinePath("tmp//","path/"),"tmp/path/");
		Assert.assertEquals(MelpFile.combinePath("tmp","path/")  ,"tmp/path/");
		Assert.assertEquals(MelpFile.combinePath("tmp","path")   ,"tmp/path");
		
		Assert.assertEquals(MelpFile.combinePath()   ,"");
		Assert.assertEquals(MelpFile.combinePath("")   ,"");
		Assert.assertEquals(MelpFile.combinePath("","")   ,"");
		
		Assert.assertEquals(MelpFile.combinePath("/")   ,"/");
		Assert.assertEquals(MelpFile.combinePath("/","/a","")   ,"/a");
		Assert.assertEquals(MelpFile.combinePath("","/","")   ,"");
	}
}
