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
package com.tsc9526.monalisa.orm.tools.resources;

import com.tsc9526.monalisa.orm.criteria.Example;

/**
 * Only for java doc <code>@see</code>
 *   
 * @author zzg.zhou(11039850@qq.com)
 */
public interface HelpDoc {
	/**
	 * @param limit 
	 *   The max number of records for this query
	 *    
	 * @param offset  
	 *   Base 0, the first record is 0
	 * 
	 * @param example  
	 *   Use new Model.Example(); <br>
	 *    
	 * @param whereStatement SQL segment, for example:<br>
	 *   <code>
	 *   1. WHERE col_1=? AND col_2=? ORDER BY 1<br>
	 *   2. col_1=? and col_2=? ORDER BY 1<br>
	 *   3. null<br>
	 *   4. ""  <br>
	 *   5. "ORDER BY ..." <br>
	 *   6. ", table2 b WHERE a.id=b.id ..." <br>
	 *   7. "LEFT JOIN table2 b ON a.id=b.id "<br>
	 *   </code>
	 *   
	 * @param args <br>
	 *   Padding the corresponding <code>?</code> in whereStatement
	 * 
	 * @return 
	 *   When insert/update/delete, indicate the number of affected rows<br>
     * 
	 */
	public int helpQuery(int limit,int offset,Example<?,?> example,String whereStatement,Object ... args);
 	 
}
