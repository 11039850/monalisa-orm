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
package test.com.tsc9526.monalisa.orm.dao;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test; 

import com.tsc9526.monalisa.orm.Tx;

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecord;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class UpdateTest {
	
	public void updateByIn(){
		Tx.execute(new Tx.Atom<Integer>() {
			public Integer execute() throws Throwable {
				List<Integer> ids=new ArrayList<Integer>();
				
				int x0=10;
				for(int i=0;i<x0;i++){
					TestRecord r=new TestRecord();
					r.defaults().setName("tmp_name_n"+i).setTitle("tmp_title_"+i);
					r.save();
					
					ids.add(r.getRecordId());
				}
				
				int x1=TestRecord.WHERE().recordId.in(ids).update(new TestRecord().setName("all-name").setTitle("all-title"));
				Assert.assertEquals(x1,x0);
				
				int x2=TestRecord.WHERE().recordId.in(ids).delete();
				Assert.assertEquals(x2,x0);
				
				return 0;
			}
			
		});
		
	}
	
	
}
