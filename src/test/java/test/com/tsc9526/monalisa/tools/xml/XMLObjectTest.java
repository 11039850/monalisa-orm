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
package test.com.tsc9526.monalisa.tools.xml;

import java.io.Serializable;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.xml.XMLObject;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class XMLObjectTest {

	public void testToXml(){
		DataTable<DataMap> data=new DataTable<DataMap>();
		DataMap d1=new DataMap();
		d1.put("address",new Address("GuangDong","ShenZhen","NanShan"));
		data.add(d1);
		
		Response resp=new Response(data);
		XMLObject xml=new XMLObject(resp);
		Assert.assertTrue(xml.toString().indexOf("<province>GuangDong</province>")>0);
	}
	
	static class Response implements Serializable{	 
		private static final long serialVersionUID = 5042617802808490420L;
		int status;
		String message="OK";
		
	 	protected Object data;
	 
		public Response(Object data){
			this.data=data;
		}
	}
	
	static class Address{
		private String province;
		private String city;
		private String street;
		
		public Address(){
			
		}
		public Address(String province, String city, String street) {
			super();
			this.province = province;
			this.city = city;
			this.street = street;
		}
		
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
	}
}
