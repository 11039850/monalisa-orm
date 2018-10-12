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

/**
 * !!! Do not use this class !!! 
 *
 * @author zzg.zhou(11039850@qq.com)
 */
public class QEH {

	public static Query getQuery(Criteria<?> criteria){	
		return criteria.q;
	}
	
	public static Query getQuery(Example<? extends Criteria<?>,?> example){		
		Query  q=new Query();
		
		List<String> orderBy=new ArrayList<String>();
	 	for(int i=0;i<example.cs.size();i++){
	 		Criteria<?> c=example.cs.get(i);
	 		
	 		Query cq=c.q;
	 		if(cq.isEmpty()==false){	 			
				if(q.isEmpty()){
					if(example.cs.size()>1){
						q.add("(");
						q.add(cq.getSql(),cq.getParameters());
						q.add(")");
					}else{
						q.add(cq.getSql(),cq.getParameters());
					}
				}else{
					q.add(" OR (");
					q.add(cq.getSql(),cq.getParameters());
					q.add(")");
				}				 
	 		}
	 		
	 		for(String by:c.getOrderBy()){
	 			if(!orderBy.contains(by)){ 
	 				orderBy.add(by);
	 			}
	 		}
		}
	 	
		if(orderBy.size()>0){
			q.add(" ORDER BY ");
			for(int i=0;i<orderBy.size();i++){
				if(i>0){
					q.add(", ");
				}
				q.add(orderBy.get(i));
			}
		}
		
		return q;
	}	 

}
