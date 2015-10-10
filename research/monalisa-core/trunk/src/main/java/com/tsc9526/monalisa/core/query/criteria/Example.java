package com.tsc9526.monalisa.core.query.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.query.dao.Model;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public abstract class Example<X extends Criteria,T extends Model<?>>{
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
