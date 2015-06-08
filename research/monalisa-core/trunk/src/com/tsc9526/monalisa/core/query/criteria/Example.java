package com.tsc9526.monalisa.core.query.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.query.Query;

public abstract class Example<X extends Criteria>{
	private List<X> cs=new ArrayList<X>();
	 	
	public Query getQuery(){		
		Query  q=new Query();
		
		List<String> orderBy=new ArrayList<String>();
	 	for(int i=0;i<cs.size();i++){
	 		X c=cs.get(i);
	 		
	 		Query cq=c.getQuery();
	 		if(cq.isEmpty()==false){
	 			boolean empty=q.isEmpty();
				if(!empty){
					q.add(" OR (");
				}
							
				q.add(cq.getSql(),cq.getParameters());
				
				if(!empty){
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
	
	public X createCriteria(){
		X criteria= createInternal();
		if(cs.size()==0){
			cs.add(criteria);								
		}
		return criteria;
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
}
