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
package test.com.tsc9526.monalisa.core.query;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.meta.MetaColumn;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class MetaColumnTest {
  
  public void testParseRemark()throws Exception {
	  MetaColumn c=new MetaColumn();
	  c.setRemarks(
			  "#annotation"
			  +"\r\n{ "
			  +"\r\n@regex(\"[0-9]+\",\"Error message!\")"
			  +"\r\n@min(10)"
			  +"\r\n@max(256)"
			  +"\r\n}"
			  +"\r\n"			 
			  +"\r\n#bool{}"
			  
			  +"\r\n#value{"
			  +"\r\nx+y"
  			  +"\r\n}");
	  
	  Assert.assertEquals(c.getCode("Bool"),"");
	  Assert.assertEquals(c.getJavaType(),"Boolean");
	  
	  c.setRemarks("#enum{com.xx.Status}");
	  Assert.assertEquals(c.getJavaType(),"Status");
	  
	  c.setRemarks("#json{com.xx.Js}");
	  Assert.assertEquals(c.getJavaType(),"Js");
  }
}
