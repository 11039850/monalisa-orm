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
package test.com.tsc9526.monalisa.orm.service.servlet;

import org.junit.Assert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.orm.service.DBS;
import com.tsc9526.monalisa.orm.service.servlet.MonalisaServlet;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class MonalisaServletTest {
	@BeforeClass
	public void init(){
		DBS.add("db1",MysqlDB.DB);
	}
	
	public void testGetDbTablePk1()throws Exception{
		MonalisaServlet ms=new MonalisaServlet();
		
		MockHttpServletRequest       req=new MockHttpServletRequest("GET","/db1/test_record/1");
		req.addParameter("-columns","record_id,name");
		
		MockHttpServletResponse resp=new MockHttpServletResponse(); 
		
		ms.service(req, resp);
		
		String body=resp.getContentAsString();
		JsonObject http=new JsonParser().parse(body).getAsJsonObject();
		Assert.assertEquals(200,http.get("status").getAsInt());
		
		System.out.println(body);
		
		JsonObject js=http.get("data").getAsJsonObject();
		Assert.assertEquals(1,js.get("record_id").getAsInt());
		
		
	}
	
	public void testGetDbTablePk2()throws Exception{
		MonalisaServlet ms=new MonalisaServlet();
		
		MockHttpServletRequest       req=new MockHttpServletRequest("GET","/db1/test_record/recordId=1");
		req.addParameter("columns","record_id,name");
		
		MockHttpServletResponse resp=new MockHttpServletResponse(); 
		
		ms.service(req, resp);
		
		String body=resp.getContentAsString();
		JsonObject http=new JsonParser().parse(body).getAsJsonObject();
		Assert.assertEquals(200,http.get("status").getAsInt());
		
		JsonObject js=http.get("data").getAsJsonObject();
		Assert.assertEquals(1,js.get("record_id").getAsInt());
		
		System.out.println(body);
	}
	 
}
