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
package test.com.tsc9526.monalisa.core.java;

import java.io.File;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.java.Java;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 * 
 */
@Test
public class JavaTest {
	static final long $VERSION = 2016061618153300L;
	
	public void testParse()throws Exception{
		Java java=new Java(new File("src/test/resources/lab/Demo.java"));
	 
		
		Assert.assertEquals("test.com.tsc9526.monalisa.core.sql.java",java.getPackageName());
		Assert.assertEquals("Demo",java.getName());
		
		long version=java.getVersion();
		Assert.assertTrue(version>0);
		
		long newVersion=java.increaseVersion();
		Assert.assertTrue(newVersion > version );
		 
	}
	
	public void testVersioIncreasing()throws Exception{
		Java java=new Java(""+/**~{*/""
			+ "package test.version ;"
			+ "\r\npublic class Demo{"
			+ "\r\n	public static final long $VERSION = 1;"
			+ "\r\n}"
		+ "\r\n"/**}*/);
	 
		
		Assert.assertEquals("test.version",java.getPackageName());
		Assert.assertEquals("Demo",java.getName());
		Assert.assertEquals(false,java.isNaturalIncreasing());
		
		long version=java.getVersion();
		Assert.assertEquals(1,version);
		
		long newVersion=java.increaseVersion();
		Assert.assertTrue(newVersion>1900010100000000L);
		 
	}
	 
	public void testVersionNaturalIncreasing()throws Exception{
		Java java=new Java(""+/**~{*/""
			+ "package test.version ;"
			+ "\r\npublic class Demo{"
			+ "\r\n	public static final long $VERSION$ = 1;"
			+ "\r\n}"
		+ "\r\n"/**}*/);
	 
		
		Assert.assertEquals("test.version",java.getPackageName());
		Assert.assertEquals("Demo",java.getName());
		Assert.assertEquals(true,java.isNaturalIncreasing());
		
		long version=java.getVersion();
		Assert.assertEquals(1,version);
		
		long newVersion=java.increaseVersion();
		Assert.assertEquals(2,newVersion);
		 
	}
}
