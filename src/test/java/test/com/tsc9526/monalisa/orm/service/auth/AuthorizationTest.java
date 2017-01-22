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
package test.com.tsc9526.monalisa.orm.service.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.orm.service.auth.DigestAuth.GigestAuthrization;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class AuthorizationTest {

	public void testParser(){
		String auth=""+/**~!{*/""
			+ "Digest username=\"zzg\", realm=\"Realm\", nc=000001, algorithm=MD5"
		+ "\r\n"/**}*/.trim();
		GigestAuthrization ga=new GigestAuthrization("GET",auth);
		 
		Assert.assertEquals(ga.getUsername(), "zzg");
		Assert.assertEquals(ga.getRealm(), "Realm");
		Assert.assertEquals(ga.getNc(), "000001");
	}
}
