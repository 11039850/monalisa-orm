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
package test.com.tsc9526.monalisa.tools.template;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.template.VarTemplate;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class VarTemplateTest {
	private  Map<String,String> createVariables(){
		HashMap<String, String> x= new HashMap<String, String>();
		x.put("v1", "x1");
		x.put("v2", "x2");
		x.put("v3", "x3");
		return x;
	}
	 
	public void testWithException(){
		VarTemplate var=new VarTemplate(createVariables());
		
		var.setThrowExceptionOnVarNotFound(true);
		String templateString="$v4/v5/v6";
		try{
			var.getValue(templateString);
			Assert.fail("Var: v4 not found, bu not exception!");
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().indexOf("v4")>0);
		}
	}
	
	public void testVar1(){
		VarTemplate var=new VarTemplate(createVariables());
		
		String templateString="${v1}$v2/v5/v6";
		String x1=var.getValue(templateString);
		Assert.assertEquals("x1x2/v5/v6",x1);
	 	 
	}
	
	public void testVar2(){
		VarTemplate var=new VarTemplate(createVariables());
		
		String templateString="${v1}$v2/$v4/v5/v6";
		String x1=var.getValue(templateString);
		Assert.assertEquals("x1x2//v5/v6",x1);
	 	 
	}
	
	
	public void testVar41(){
		VarTemplate var=new VarTemplate(createVariables());
		
		String templateString="$v4/v5/v6";
		String x1=var.getValue(templateString);
		Assert.assertEquals("/v5/v6",x1);
		
		var.setNullVar("/");
		String x2=var.getValue(templateString);
		Assert.assertEquals("//v5/v6",x2);
	}
	
	public void testVar42(){
		VarTemplate var=new VarTemplate(createVariables());
		
		String templateString="${v4}/v5/v6";
		String x1=var.getValue(templateString);
		Assert.assertEquals("/v5/v6",x1);
		
		var.setNullVar("/");
		String x2=var.getValue(templateString);
		Assert.assertEquals("//v5/v6",x2);
	}
	
	
}
