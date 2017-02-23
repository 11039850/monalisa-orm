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
package test.com.tsc9526.monalisa.tools.datatable;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.tools.datatable.DataTableTest.TestUserAreaRank;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassHelper;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.datatable.Page;
import com.tsc9526.monalisa.tools.string.MelpJson;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class PageTest {
	public void testPages() {
		DataTable<Object> table = new DataTable<Object>();
		 
		//创建测试数据
		ClassHelper mc=MelpClass.getClassAssist(TestUserAreaRank.class);
		for(int userId=1;userId<=6;userId++){
			Object row=new TestUserAreaRank();

			mc.getField("user").setObject(row, userId);
			mc.getField("area").setObject(row,"guangdong-"+(userId%2));
			if(userId!=3){
				mc.getField("rank").setObject(row,90+userId);
			}
			
			table.add(row);
		}
		
		Page<Object> page=table.getPage(5,0);
		Assert.assertEquals(page.getPage(), 1);
		Assert.assertEquals(page.getTotal(), 2);
		Assert.assertEquals(page.getRecords(), 6);
		Assert.assertEquals(page.getSize(),5);
		Assert.assertEquals(page.rows(),5);
		 
		page=table.getPage(5,5);
		Assert.assertEquals(page.getPage(), 2);
		Assert.assertEquals(page.getTotal(), 2);
		Assert.assertEquals(page.getRecords(), 6);
		Assert.assertEquals(page.getSize(),5);
		Assert.assertEquals(page.rows(),1);
	}
	
	
	public void testToJsonObj() {
		DataTable<Object> table = new DataTable<Object>();
		 
		//创建测试数据
		ClassHelper mc=MelpClass.getClassAssist(TestUserAreaRank.class);
		for(int userId=1;userId<=6;userId++){
			Object row=new TestUserAreaRank();

			mc.getField("user").setObject(row, userId);
			mc.getField("area").setObject(row,"guangdong-"+(userId%2));
			if(userId!=3){
				mc.getField("rank").setObject(row,90+userId);
			}
			
			table.add(row);
		}
		
		Page<Object> page=table.getPage(6,0);
		Assert.assertEquals(page.getPage(), 1);
		Assert.assertEquals(page.getTotal(), 1);
		Assert.assertEquals(page.getRecords(), 6);
		Assert.assertEquals(page.getSize(),6);
		Assert.assertEquals(page.rows(),6);
		
		String json=page.toJson();
		JsonParser parser=new JsonParser();
		JsonObject root=parser.parse(json).getAsJsonObject();
		
		Assert.assertEquals(root.get("page").getAsInt(),1);
		Assert.assertEquals(root.get("total").getAsInt(),1);
		Assert.assertEquals(root.get("records").getAsInt(),6);
		Assert.assertEquals(root.get("size").getAsInt(),6);
	 	 
		JsonElement list=root.get("rows");
		String target=new GsonBuilder().serializeNulls().create().toJson(list);
		Assert.assertEquals(target,table.toJson());
		
		System.out.println(MelpJson.getGson().toJson(page));
		 
	}
	
	
	  
}
