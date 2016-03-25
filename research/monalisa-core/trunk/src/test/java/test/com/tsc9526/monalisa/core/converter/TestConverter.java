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
package test.com.tsc9526.monalisa.core.converter;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.core.converters.TypeConverter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class TestConverter {
	private TypeConverter converter=new TypeConverter();
	
	public void testArray001(){
		int[] a1=new int[]{1,2,3};
		
		Object[] v1=converter.convert(a1, Object[].class);
		
		for(int i=0;i<a1.length;i++){
			Assert.assertEquals(v1[i],a1[i]);
		}
		
	}
	
	public void testArray002(){
		boolean[] a1=new boolean[]{true,false,Boolean.TRUE};
		
		Object[] v1=converter.convert(a1, Object[].class);
		
		for(int i=0;i<a1.length;i++){
			Assert.assertEquals(v1[i],a1[i]);
		}
		
	}
}
