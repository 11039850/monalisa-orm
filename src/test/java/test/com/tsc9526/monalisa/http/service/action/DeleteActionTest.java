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
package test.com.tsc9526.monalisa.http.service.action;

import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.http.Response;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DeleteActionTest extends AbstractActionTest {
	protected String getRequestMethod(){
		return "DELETE";
	}
	
	public void testDeleteDbTableByPk()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/1");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		Assert.assertEquals(resp.getData().toString(),"1");
 	}
	
	public void testDeleteDbTableByMultiKeys()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_id=2");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		Assert.assertEquals(resp.getData().toString(),"1");
 	}
	
	public void testDeleteDbTableByWhere()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("record_id>", "8");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		Assert.assertEquals(resp.getData().toString(),"3");
 	}
	
	@AfterClass
	public void testDeleteAllTable()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),403);
 	}
}
