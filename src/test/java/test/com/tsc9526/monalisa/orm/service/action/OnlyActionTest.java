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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.mysql.MysqlDB;

import com.tsc9526.monalisa.orm.service.DBS;
import com.tsc9526.monalisa.orm.service.Response;
import com.tsc9526.monalisa.orm.service.action.Action;
import com.tsc9526.monalisa.orm.service.action.ActionArgs;
import com.tsc9526.monalisa.orm.service.action.DefaultActionLocate;
import com.tsc9526.monalisa.orm.service.action.DeleteAction;
import com.tsc9526.monalisa.orm.service.action.GetAction;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
@SuppressWarnings("unused")
public class OnlyActionTest extends AbstractActionTest{

	@BeforeClass
	public void setup(){
		DBS.remove("db1");
		DBS.add("db1",MysqlDB.DB,new DefaultActionLocate() {
			public Action onGetAction(ActionArgs args){
				return new GetAction(args);
			}
		 	
			public Action onDeleteAction(ActionArgs args){
				//Disable delete table without filters
				return new DeleteAction(args){
					public Response deleteTableRowsAll() {
						return new Response(403,"Delete table disabled without filters, "+args.getTable());
					}
				};
			}
		});
	}
	
	public void testGet()throws Exception{
		MockHttpServletRequest       req=createRequest("GET","/`db1`");
	 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
	}
	
	public void testDeleteAll()throws Exception{
		MockHttpServletRequest  req=createRequest("DELETE","/db1/test_record_v2");
	 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),403);
	}
	
	public void testDeleteByPk()throws Exception{
		MockHttpServletRequest  req=createRequest("DELETE","/db1/test_record_v2/1");
	 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),200);
	}
	
	public void testPost()throws Exception{
		MockHttpServletRequest  req=createRequest("POST","/db1/test_record_v2/1");
	 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),403);
	}
	
	public void testPut()throws Exception{
		MockHttpServletRequest  req=createRequest("PUT","/db1/test_record_v2/1");
	 	
		Response resp=getRespone(req);
		Assert.assertEquals(resp.getStatus(),403);
	}
}
