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

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
	
	public void testArray03(){
		int[] a1=new int[]{1,2,3};
		
		int[] v1=converter.convert(a1, int[].class);
		
		for(int i=0;i<a1.length;i++){
			Assert.assertEquals(v1[i],a1[i]);
		}
		
	}
	
	public void testArray04(){
		Object[] a1=new Object[]{1,2,3};
		
		int[] v1=converter.convert(a1, int[].class);
		
		for(int i=0;i<a1.length;i++){
			Assert.assertEquals(v1[i],a1[i]);
		}
		
	}
	
	public void testArray05(){
		List<Integer> a1=new ArrayList<Integer>();
		a1.add(1);
		a1.add(2);
		a1.add(3);
		
		int[] v1=converter.convert(a1, int[].class);
		
		for(int i=0;i<a1.size();i++){
			Assert.assertEquals(v1[i],a1.get(i).intValue());
		}
		
	}
	
	public void testArray06(){
		List<String> a1=new ArrayList<String>();
		a1.add("1");
		a1.add("2");
		a1.add("3");
		
		int[] v1=converter.convert(a1, int[].class);
		
		for(int i=0;i<a1.size();i++){
			Assert.assertEquals(v1[i],Integer.parseInt(a1.get(i)));
		}
		
	}
	
	public void testArray07(){
		int v1=converter.convert(new JsonPrimitive(10), int.class);
		Assert.assertEquals(v1,10);
		
		Integer v10=converter.convert(new JsonPrimitive(10), Integer.class);
		Assert.assertEquals(v10,new Integer(10));
		
		long v2=converter.convert(new JsonPrimitive(10L), long.class);
		Assert.assertEquals(v2,10L);
		
		String v3=converter.convert(new JsonPrimitive("10"), String.class);
		Assert.assertEquals(v3,"10");
		
		boolean v4=converter.convert(new JsonPrimitive(true), boolean.class);
		Assert.assertEquals(v4,true);
	}
}
