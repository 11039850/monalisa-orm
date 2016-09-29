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
package test.com.tsc9526.monalisa.orm.service.action;

import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.mysql.mysqldb.TestRecordV2;

import com.tsc9526.monalisa.orm.service.Response;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class PutActionTest extends AbstractActionTest {
	protected String getRequestMethod(){
		return "PUT";
	}
	
	public void testPutDbTableByPk()throws Exception{
		Assert.assertNotEquals(TestRecordV2.SELECT().selectByPrimaryKey(1).getName(),"ppyyzz001");
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/1");
		req.addParameter("name", "ppyyzz001");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage());
		 
		Assert.assertEquals(TestRecordV2.SELECT().selectByPrimaryKey(1).getName(),"ppyyzz001");
 	}
	
	public void testPutDbTableByMultiKeys()throws Exception{
		Assert.assertNotEquals(TestRecordV2.SELECT().selectByPrimaryKey(2).getName(),"ppyyzz002");
		
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_id=2");
		
		req.addParameter("name", "ppyyzz002");
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200,resp.getMessage()); 
		Assert.assertEquals(TestRecordV2.SELECT().selectByPrimaryKey(2).getName(),"ppyyzz002");
 	}
	
	public void testPutDbTableNone()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_id=2");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),400,resp.getMessage()); 
 	}
	
	public void testPutDbTableRow1()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("name", "new_002");
		 
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),403,resp.getMessage());
 	}
}
