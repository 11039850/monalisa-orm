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
package test.com.tsc9526.monalisa;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class TestConstants {
	/** Print sql statements to console */
	public final static boolean DEBUG_SQL               = true;
	
	/** Test with mysql db */
	public final static boolean ENABLE_TEST_WITH_MYSQL  = true;
	
	/** Test with oracle db */
	public final static boolean ENABLE_TEST_WITH_ORACLE = false;
	
	
	public final static String database  = "test_monalisa";
	public final static String username  = "monalisa";
	public final static String password  = "monalisa";
	
	public final static String mysqlUrl  = "jdbc:mysql://127.0.0.1:3306/"+database+"?allowMultiQueries=true";
	public final static String oracleUrl = "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL";
}
