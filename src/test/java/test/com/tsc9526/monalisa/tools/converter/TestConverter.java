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
package test.com.tsc9526.monalisa.tools.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.dialect.basic.TestSimpleModel;

import com.google.gson.JsonPrimitive;
import com.tsc9526.monalisa.tools.converters.TypeConverter;

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
	
	
	public void testList01(){
		int[] a1=new int[]{1,2,3};
		
		List<?> v1=converter.convert(a1, List.class);
		
		for(int i=0;i<a1.length;i++){
			Assert.assertEquals((int)Double.parseDouble(v1.get(i).toString()),a1[i]);
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
	
	public void test008(){
		 String[] str = {"7","2","9","3.5","34545345","98765432","0.4567"};
		  
		 String[] strArr = converter.convert(str, String[].class);
		 Assert.assertTrue(strArr.length==str.length);	   
		 for(int i=0;i<str.length;i++){
			 Assert.assertEquals(str[i], strArr[i]);
		 }
		    
		 double[] dbArr = converter.convert(str, double[].class);
		 for(int i=0;i<str.length;i++){
			 Assert.assertEquals(Double.parseDouble(str[i]), dbArr[i]);
		 }
	}
	
	public void testEnumIntA(){
		TypeConverter tc=new TypeConverter();
		 
		EnumIntA a0=tc.convert(0, EnumIntA.class);
		Assert.assertEquals(a0, EnumIntA.V0);
		
		EnumIntA a1=tc.convert(1, EnumIntA.class);
		Assert.assertEquals(a1, EnumIntA.V1);
	}
	
	public void testEnumStringA(){
		TypeConverter tc=new TypeConverter();
		 
		EnumStringA a0=tc.convert("V0", EnumStringA.class);
		Assert.assertEquals(a0, EnumStringA.V0);
		
		EnumStringA a1=tc.convert("V1", EnumStringA.class);
		Assert.assertEquals(a1, EnumStringA.V1);
	}
	
	public static enum EnumIntA{V0,V1}
	
	public static enum EnumStringA{V0,V1}
	
	
	public void testMap001(){
		Map<String, Object> from=new HashMap<String, Object>();
		from.put("int",226);
		
		Map<?,?> to=converter.convert(from, Map.class);
		
		Assert.assertEquals(""+to.get("int"),"226");
	}
	
	public void testMap002(){
		TestSimpleModel from=new TestSimpleModel();
		from.setIntField1(226);
		Map<?,?> to=converter.convert(from, Map.class);
		
		Assert.assertEquals(""+to.get("intField1"),"226");
	}
	
	public void testDateToLong(){
		Date time=new Date();
		long v1=converter.convert(time, long.class);
		Assert.assertEquals(v1, time.getTime());
		
		Long v2=converter.convert(time, Long.class);
		Assert.assertEquals(v2.longValue(), time.getTime());
	}
}
