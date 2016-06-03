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
package test.com.tsc9526.monalisa.core.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.parser.java.Java;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class RegexTest {
	public final static long $VERSION$ = 3L;
	
	public void testRegexVersion(){
		Pattern p=Pattern.compile(Java.REGX_VERSION);
		       
		Matcher m=p.matcher(""+/**~{*/""
			+ "public class a{"
			+ "\r\n	public static final long $VERSION = 2016060310421800L;"
			+ "\r\n}"
		+ "\r\n"/**}*/); 
		  
		Assert.assertTrue(m.find() && m.start()>0);
		 
		m=p.matcher(""+/**~{*/""
			+ "public class a{"
			+ "\r\n	public final static  long $VERSION = 1000;"
			+ "\r\n}"
		+ "\r\n"/**}*/);	
		Assert.assertTrue(m.find() && m.start()>0);
		
		m=p.matcher(""+/**~{*/""
			+ "public class a{"
			+ "\r\n	public static final Long $VERSION = 1000L;"
			+ "\r\n}"
		+ "\r\n"/**}*/);	
		Assert.assertTrue(m.find() && m.start()>0);
		
		
		m=p.matcher(""+/**~{*/""
			+ "public class a{"
			+ "\r\n	public static final Long $VERSION$ = 1000L;"
			+ "\r\n}"
		+ "\r\n"/**}*/);	
		Assert.assertTrue(m.find() && m.start()>0);
	}
}
