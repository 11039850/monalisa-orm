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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.Query;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class QueryMultiTableTest {

	public void testSelect1() {
		TestSimpleModel model=new TestSimpleModel();
		Query query=model.dialect().select(model,",user b on a.id=b.id and a.x=?",1);
		
		String expect="SELECT a.* FROM `simple_model` a ,user b on a.id=b.id and a.x=1";
		Assert.assertEquals(query.getExecutableSQL(),expect);				
	}
	
	public void testSelect2() {
		TestSimpleModel model=new TestSimpleModel();
		
		model.include("int_field1","string_field2");
		Query query=model.dialect().select(model,"left join user b on a.id=b.id and a.x=?",1);
		String expect="SELECT a.`auto`, a.`int_field1`, a.`string_field2` FROM `simple_model` a left join user b on a.id=b.id and a.x=1";
		Assert.assertEquals(query.getExecutableSQL(),expect);
	}

}
