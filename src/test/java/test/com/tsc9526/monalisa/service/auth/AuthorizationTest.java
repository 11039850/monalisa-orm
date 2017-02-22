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
package test.com.tsc9526.monalisa.service.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.service.auth.DigestAuth.DigestAuthrization;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class AuthorizationTest {

	public void testParser1(){
		String auth=""+/**~!{*/""
			+ "Digest username=\"zzg\", realm=\"Realm\", nc=000001, algorithm=MD5"
		+ "\r\n"/**}*/.trim();
		DigestAuthrization ga=new DigestAuthrization("GET",auth);
		
		Assert.assertTrue(ga.isDigest());
		
		Assert.assertEquals(ga.getUsername(), "zzg");
		Assert.assertEquals(ga.getRealm(), "Realm");
		Assert.assertEquals(ga.getNc(), "000001");
	}
	
	public void testParser2(){
		String auth=""+/**~!{*/""
			+ "Digest username=\"monalisa\", realm=\"Realm\", nonce=\"y73-IY8ihMw=\", uri=\"/monalisa-web/dbs/db1/user,blog/user.id=blog.user_id?page=1\", algorithm=MD5, response=\"a3c7b21d5f3e3c25c3dab02be6422844\", qop=auth, nc=00000001, cnonce=\"96a42609bd561e34\""
		+ "\r\n"/**}*/.trim();
		DigestAuthrization ga=new DigestAuthrization("GET",auth);
		 
		Assert.assertTrue(ga.isDigest());
		
		Assert.assertEquals(ga.getUsername(), "monalisa");
		Assert.assertEquals(ga.getRealm(), "Realm");
		Assert.assertEquals(ga.getNc(), "00000001");
		Assert.assertEquals(ga.getCnonce(), "96a42609bd561e34");
		Assert.assertEquals(ga.getQop(), "auth");
		Assert.assertEquals(ga.getUri(), "/monalisa-web/dbs/db1/user,blog/user.id=blog.user_id?page=1");
	}
	
	}
