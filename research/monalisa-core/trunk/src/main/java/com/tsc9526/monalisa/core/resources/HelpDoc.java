package com.tsc9526.monalisa.core.resources;

import com.tsc9526.monalisa.core.query.criteria.Example;

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
	 * @param offset <br>
	 *   Base 0, the first record is 0
	 * 
	 * @param example <br>
	 *   Use new Model.Example(); <br>
	 *    
	 * @param whereStatement SQL segment, for example:<br>
	 *   <code>
	 *   1. WHERE col_1=? AND col_2=? ORDER BY 1<br>
	 *   2. col_1=? and col_2=? ORDER BY 1<br>
	 *   3. null<br>
	 *   4. ""  <br>
	 *   5. "ORDER BY ..." 
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
