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
package test.com.tsc9526.monalisa.orm.query.datatable;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.query.TestSimpleModel;

import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.datatable.DataTable;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class DataTableJoinTest {
	private DataTable<?>[] createDataTables(){
		DataTable<TestSimpleModel> left=new DataTable<TestSimpleModel>();
		TestSimpleModel v1=new TestSimpleModel()
			.setIntField1(11).setStringField1("s11")
			.setIntField2(12).setStringField2("s12");
		left.add(v1);
		
		TestSimpleModel v2=new TestSimpleModel()
			.setIntField1(21).setStringField1("s21")
			.setIntField2(22).setStringField2("s22");
		left.add(v2);
		 
		TestSimpleModel v3=new TestSimpleModel()
		.setIntField1(31).setStringField1("s31")
		.setIntField2(32).setStringField2("s32");
		left.add(v3);
		 
		DataTable<DataMap> right = new DataTable<DataMap>();
		DataMap row = new DataMap();
		row.put("intField1", 11);
		row.put("stringField1", "11stringA");
		right.add(row);
		
		row = new DataMap();
		row.put("intField1", 11);
		row.put("stringField1", "11stringB");
		right.add(row);
		
		return new DataTable[]{left,right};
	}
	
	
	public void testJoinInner(){
		DataTable<?>[] dataTables=createDataTables();
		DataTable<?> left=dataTables[0];
		DataTable<?> right=dataTables[1];
		
		DataTable<DataMap> rs=left.join(right, "intField1", "intField1");
		Assert.assertEquals(rs.size(),2);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(0).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(0).getString("stringField11"),"11stringA");
		
		Assert.assertEquals(rs.get(1).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(1).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(1).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(1).getString("stringField11"),"11stringB");
	}
	
	public void testJoinFull(){
		DataTable<?>[] dataTables=createDataTables();
		DataTable<?> left=dataTables[0];
		DataTable<?> right=dataTables[1];
		
		DataTable<DataMap> rs=left.joinFull(right, "intField1", "intField1");
		 
		Assert.assertEquals(rs.size(),6);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(0).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(0).getString("stringField11"),"11stringA");
		
		Assert.assertEquals(rs.get(1).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(1).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(1).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(1).getString("stringField11"),"11stringB");
	}
	
	public void testJoinRight(){
		DataTable<?>[] dataTables=createDataTables();
		DataTable<?> left=dataTables[0];
		DataTable<?> right=dataTables[1];
		
		DataTable<DataMap> rs=left.joinRight(right, "intField1", "intField1");
		Assert.assertEquals(rs.size(),2);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(0).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(0).getString("stringField11"),"11stringA");
		
		Assert.assertEquals(rs.get(1).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(1).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(1).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(1).getString("stringField11"),"11stringB");
	}
	
	public void testJoinLeft(){
		DataTable<?>[] dataTables=createDataTables();
		DataTable<?> left=dataTables[0];
		DataTable<?> right=dataTables[1];
		
		
		DataTable<DataMap> rs=left.joinLeft(right, "intField1", "intField1");
		Assert.assertEquals(rs.size(),4);
		Assert.assertEquals(rs.get(0).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(0).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(0).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(0).getString("stringField11"),"11stringA");
		
		Assert.assertEquals(rs.get(1).getInt("intField1",0),11);
		Assert.assertEquals(rs.get(1).getInt("intField11",0),11);
		Assert.assertEquals(rs.get(1).getString("stringField1"),"s11");
		Assert.assertEquals(rs.get(1).getString("stringField11"),"11stringB");
		
		Assert.assertEquals(rs.get(2).getInt("intField1",0),21);
		Assert.assertEquals(rs.get(2).getInt("intField11",0),0);
		Assert.assertEquals(rs.get(2).getString("stringField1"),"s21");
		Assert.assertEquals(rs.get(2).getString("stringField11"),null);
		
		Assert.assertEquals(rs.get(3).getInt("intField1",0),31);
		Assert.assertEquals(rs.get(3).getInt("intField11",0),0);
		Assert.assertEquals(rs.get(3).getString("stringField1"),"s31");
		Assert.assertEquals(rs.get(3).getString("stringField11"),null);
		
	}
	
	public void testJoinEscapedTime(){
		long t1=System.currentTimeMillis();
		
		DataTable<TestSimpleModel> left=new DataTable<TestSimpleModel>();
		for(int i=0;i<1000;i++){
			left.add(new TestSimpleModel().setIntField1(i).setStringField1("s"+i).setIntField2(i*2).setStringField2("s"+(i*2)));
		}
		System.out.println("Init left table: "+left.size()+", use time: "+(System.currentTimeMillis()-t1));
		
		t1=System.currentTimeMillis();
		DataTable<DataMap> right = new DataTable<DataMap>();
		for(int i=0;i<2000;i++){
			DataMap row = new DataMap();
			row.put("intField1", i*3);
			row.put("stringField1", "BStringA"+(i*5));
			right.add(row);
		}
		System.out.println("Init right table: "+right.size()+", use time: "+(System.currentTimeMillis()-t1));
		
		t1=System.currentTimeMillis();
		DataTable<DataMap> rs=left.join(right, "intField1", "intField1");
		
		System.out.println("Total: "+rs.size()+", use time: "+(System.currentTimeMillis()-t1));
	}
}
