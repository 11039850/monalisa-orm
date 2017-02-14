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
package test.com.tsc9526.monalisa.service.actions;

import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.service.Response;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class GetActionTest extends AbstractActionTest {
	
	protected String getRequestMethod(){
		return "GET";
	}
	
	public void testGetDb()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1");
		 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),400);
 	}
	
	public void testGetDbTableNotExist()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2_not_exists");
 
		Response resp=getRespone(req);
		Assert.assertTrue(resp.getStatus()!=200);
 	}
	 
	
	public void testGetDbInvalidName()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_%20");
		
		Response resp=getRespone(req);
		Assert.assertTrue(resp.getStatus()!=200);
 	}
	
	public void testGetDbTable1()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("-column","record_id,name");
		req.addParameter("paging","true");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		Page<DataMap> page=resp.getData();
		
		Assert.assertTrue(page.rows()>1);
		
		DataMap data=page.getRows().get(0);
		Assert.assertNull(data.getString("record_id"));
		Assert.assertNull(data.getString("name"));
		Assert.assertNotNull(data.getString("title"));
		Assert.assertTrue(data.size()>1);
 	}
	
	
	public void testGetDbTable2()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("column","record_id,name");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataTable<DataMap> table=resp.getData();
		
		Assert.assertTrue(table.size()>1);
		
		DataMap data=table.get(0);
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertNull(data.getString("title"));
		Assert.assertTrue(data.size()>1);
 	}
	
	public void testGetDbTableFilter01()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("column","record_id,name");
		req.addParameter("record_id>1","");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataTable<DataMap> table=resp.getData();
		
		Assert.assertTrue(table.size()>0);
		
		DataMap data=table.get(0);
		Assert.assertEquals(data.getString("record_id"),"2");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertNull(data.getString("title"));
 	}
	
	public void testGetDbTableFilter02()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("column","record_id,name");
		req.addParameter("record_id>","1");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataTable<DataMap> table=resp.getData();
		
		Assert.assertTrue(table.size()>0);
		
		DataMap data=table.get(0);
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertNull(data.getString("title"));
 	}
	
	public void testGetDbTableFilter03()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("column","record_id,name");
		req.addParameter("record_id<>1","");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataTable<DataMap> table=resp.getData();
		
		Assert.assertTrue(table.size()>0);
		
		DataMap data=table.get(0);
		Assert.assertEquals(data.getString("record_id"),"2");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertNull(data.getString("title"));
 	}
	
	public void testGetDbTableFilter04()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2");
		req.addParameter("column","record_id,name");
		req.addParameter("name~ns*","");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataTable<DataMap> table=resp.getData();
		
		Assert.assertTrue(table.size()>0);
		
		DataMap data=table.get(0);
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertNull(data.getString("title"));
 	}
	
	public void testGetDbTablePk1()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/1");
		req.addParameter("-column","record_id,name");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		 
		DataMap data=resp.getData();
		Assert.assertEquals(data.getInt("record_id",0),1);
		Assert.assertNull(data.getString("name"));
		Assert.assertNotNull(data.getString("title"));
		Assert.assertTrue(data.size()>2);
 	}
	
	public void testGetDbTablePkWithJavaName()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/recordId=1");
		req.addParameter("column","record_id,name");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		
		DataMap data=resp.getData();
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertEquals(data.size(),2);
	}
	
	public void testGetDbTablePkWithColumnName()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/record_Id=1");
		req.addParameter("column","record_id,name");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		
		DataMap data=resp.getData();
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertEquals(data.size(),2);
	}
	
	public void testGetDbTableWithuQalifier()throws Exception{
		MockHttpServletRequest       req=createRequest("/db1/test_record_v2/`recordId`=1");
		req.addParameter("column","record_id,name");
		
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
		
		DataMap data=resp.getData();
		Assert.assertEquals(data.getString("record_id"),"1");
		Assert.assertNotNull(data.getString("name"));
		Assert.assertEquals(data.size(),2);
	}
}
