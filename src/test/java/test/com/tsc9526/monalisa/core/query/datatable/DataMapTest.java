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
package test.com.tsc9526.monalisa.core.query.datatable;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.core.query.TestSimpleModel;

import com.tsc9526.monalisa.core.query.datatable.DataMap;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DataMapTest {

	public void testAsDataMap(){
		DataMap x=new DataMap();
		x.put("a","a");
		
		DataMap t1=x.as(DataMap.class);
		Assert.assertTrue(t1==x);
	}
	
	public void testAsMap(){
		DataMap x=new DataMap();
		x.put("a","a");
		
		Map<?,?> t1=x.as(Map.class);
		Assert.assertTrue(t1==x);
	}
	
	public void testAsTestSimpleModel(){
		DataMap x=new DataMap();
		x.put("intField1",226);
		
		TestSimpleModel t1=x.as(TestSimpleModel.class);
		Assert.assertEquals(t1.getIntField1().intValue(),226);
	}
}
