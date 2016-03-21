package com.tsc9526.monalisa.core.query.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.query.Query;

/**
 * Do not use this class
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
					q.add(cq.getSql(),cq.getParameters());
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
