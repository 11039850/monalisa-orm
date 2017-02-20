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
package test.com.tsc9526.monalisa.tools.agent;

import junit.framework.Assert;

import com.tsc9526.monalisa.orm.annotation.Column;
import com.tsc9526.monalisa.orm.annotation.Tx;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TxExample {
	 
	@Tx
	@Column("hello")
	public void withTx()throws Throwable{
		com.tsc9526.monalisa.orm.Tx x=com.tsc9526.monalisa.orm.Tx.getTx();
		Assert.assertNotNull(x);
		
		x=com.tsc9526.monalisa.orm.Tx.begin();
		Assert.assertNull(x); 
	}
	
	public void withoutTx()throws Throwable{
		com.tsc9526.monalisa.orm.Tx x=com.tsc9526.monalisa.orm.Tx.getTx();
		Assert.assertNull(x);
		
		x=com.tsc9526.monalisa.orm.Tx.begin();
		Assert.assertNotNull(x);
		x.doClose();
	}
	
	@Tx
	public int txNesting(){
		com.tsc9526.monalisa.orm.Tx x1=com.tsc9526.monalisa.orm.Tx.getTx();
		Assert.assertNotNull(x1);
		
		com.tsc9526.monalisa.orm.Tx x2=com.tsc9526.monalisa.orm.Tx.begin();
		Assert.assertNull(x2);
		
		String txid=txNesting_inner();
		
		Assert.assertEquals(x1.getTxid(),txid);
		 
		return 1;
	}
	
	@Tx
	public String txNesting_inner(){
		com.tsc9526.monalisa.orm.Tx x1=com.tsc9526.monalisa.orm.Tx.getTx();
		Assert.assertNotNull(x1);
		
		com.tsc9526.monalisa.orm.Tx x2=com.tsc9526.monalisa.orm.Tx.begin();
		Assert.assertNull(x2);
		
		return x1.getTxid();
	}
}
