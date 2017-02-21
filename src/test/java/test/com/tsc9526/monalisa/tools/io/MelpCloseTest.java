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

import com.tsc9526.monalisa.tools.io.MelpClose;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class MelpCloseTest {
	
	
	public void testCloseA(){
		A obj=new A();
		Assert.assertEquals(obj.status,1);
		
		MelpClose.close(obj);
		Assert.assertEquals(obj.status,0);
		
	}
	
	public void testCloseA1(){
		A1 obj=new A1();
		Assert.assertEquals(obj.status,1);
		
		MelpClose.close(obj);
		Assert.assertEquals(obj.status,0);
		
	}
	
	public void testCloseB(){
		B obj=new B();
		Assert.assertEquals(obj.status,1);
		
		MelpClose.close(obj);
		Assert.assertEquals(obj.status,1);
	}
	
	public void testCloseC(){
		C obj=new C();
		Assert.assertEquals(obj.status,1);
		
		MelpClose.close(obj);
		Assert.assertEquals(obj.status,0);
	}
	
	
	public void testCloseAll1(){
		A  x1=new A();
		A1 x2=new A1();
		C  x3=new C();
		
		Assert.assertEquals(x1.status,1);
		Assert.assertEquals(x2.status,1);
		Assert.assertEquals(x3.status,1);
		
		MelpClose.close(x1,x2,x3);
		
		Assert.assertEquals(x1.status,0);
		Assert.assertEquals(x2.status,0);
		Assert.assertEquals(x3.status,0);
	}
	
	
	public void testCloseAll2(){
		Status[] ss=new Status[]{new A(),new A1(),new C()};
		for(Status s:ss){
			Assert.assertEquals(s.status,1);
		}
		
		MelpClose.close((Object[])ss);
		
		for(Status s:ss){
			Assert.assertEquals(s.status,0);
		}
	}
	
	static class Status{
		int status=1;
	}
	
	static class A extends Status{
	 
		public void close(){
			status=0;
		}
	}
	
	static class A1 extends A{
		 
	}
	
	static class B extends Status{
	 	void close(){
			status=0;
		}
	}
	
	static class C extends B{
		public void close(){
			super.close();
		}
	}
}
