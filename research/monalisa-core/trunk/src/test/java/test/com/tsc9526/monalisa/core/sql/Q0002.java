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
package test.com.tsc9526.monalisa.core.sql;

import com.tsc9526.monalisa.core.query.Query;

public class Q0002{
	/**
 测试查询A 
	*/
	public final static String testFindAll_A="test.com.tsc9526.monalisa.core.sql.Q0002.testFindAll_A";
	/**
 测试查询A 
	*/
	public static Query testFindAll_A(String name, String title, String create_by){
		 return Query.create(testFindAll_A,name,title,create_by); 
	}

	/**
 测试查询<B> 
	*/
	public final static String testFindAll_B="test.com.tsc9526.monalisa.core.sql.Q0002.testFindAll_B";
	/**
 测试查询<B> 
	*/
	public static Query testFindAll_B(String name, String title, String create_by){
		 return Query.create(testFindAll_B,name,title,create_by); 
	}

}