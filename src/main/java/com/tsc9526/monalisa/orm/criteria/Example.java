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

import com.tsc9526.monalisa.orm.model.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Example<X extends Criteria<?>,T extends Model<?>>{
	List<X> cs=new ArrayList<X>();
		
	public Example(){	
	}
 	 
	public X or() {
		X criteria = createInternal();
	    cs.add(criteria);
	    return criteria;
	}
	
	public void or(X criteria) {
        cs.add(criteria);
    }
	
	protected abstract X createInternal(); 
	
	public X createCriteria(){
		X criteria= createInternal();
		if(cs.size()==0){
			cs.add(criteria);								
		}
		return criteria;
	}
}
