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
package com.tsc9526.monalisa.orm.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.Query;
import com.tsc9526.monalisa.orm.datasource.DBConfig;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@SuppressWarnings("unchecked")
public abstract class Criteria<X extends Criteria<?>> {
	public final static int IGNORE_NONE =0;
	public final static int IGNORE_NULL =1;
	public final static int IGNORE_EMPTY=2;
	
	Query q=new Query();		
	
	private List<String> orderBy=new ArrayList<String>();
	
	private int ignore=IGNORE_NONE;
		
	
	void addOrderByAsc(String field){
		addOrderBy(field+" ASC");
	}
	
	void addOrderByDesc(String field){
		addOrderBy(field+" DESC");
	}
	
	protected void use(DBConfig db){
		q.use(db);
	}
	
	private void addOrderBy(String by){
		if(!orderBy.contains(by)){
			orderBy.add(by);		 
		}	
	}
	
	List<String> getOrderBy(){
		return orderBy;
	}

	public int getIgnore() {
		return ignore;
	}
	
	public X ingoreNull() {
		this.ignore = IGNORE_NULL;		
		return (X)this;
	}
	
	public X ingoreEmpty() {
		this.ignore = IGNORE_EMPTY;		
		return (X)this;
	}		 
}
