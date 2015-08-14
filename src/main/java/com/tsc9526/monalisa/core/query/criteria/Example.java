package com.tsc9526.monalisa.core.query.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.query.dao.Select;

@SuppressWarnings({"rawtypes","unchecked"})
public abstract class Example<X extends Criteria,T extends Model>{
	private List<X> cs=new ArrayList<X>();
	
	private Model<T> model;
	public Example(){	
	}
	
	public Example(Model<T> model){
		this.model=model;
	}

	public Query getQuery(){		
		Query  q=new Query();
		
		List<String> orderBy=new ArrayList<String>();
	 	for(int i=0;i<cs.size();i++){
	 		X c=cs.get(i);
	 		
	 		Query cq=c.getQuery();
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
	 
	 
	public T selectOne(){
		Select<T> select=new Select(model);
		return select.selectOneByExample(this);
	}
 	 
	public List<T> select(){
		Select<T> select=new Select(model);
		return select.selectByExample(this);
	}	
 
	public List<T> select(int limit,int offset){
		Select<T> select=new Select(model);
		return select.selectByExample(limit,offset,this);
	}
	
	public Page<T> selectPage(int limit,int offset){
		Select<T> select=new Select(model);
		return select.selectPageByExample(limit,offset,this);
	}
   
}
