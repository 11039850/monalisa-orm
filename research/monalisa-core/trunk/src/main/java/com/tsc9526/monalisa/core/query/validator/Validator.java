package com.tsc9526.monalisa.core.query.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

public class Validator {
	/**
	 * 校验字段数据的是否合法.
	 * 
	 *  @return 不合法的字段列表{字段名: 错误信息}. 如果没有错误, 则为空列表.
	 */
	public List<String> validate(Model<?> m){
		List<String> result=new ArrayList<String>();
		
		for(FGS fgs:m.fields()){
			Column c=fgs.getField().getAnnotation(Column.class);
			Object v=fgs.getObject(this);
			
			if(c.notnull() && "NULL".equals(c.value()) && v==null){
				result.add(fgs.getFieldName()+": CAN NOT BE NULL");
			}else{			
				if(v!=null){
					Regex regex=fgs.getField().getAnnotation(Regex.class);
					if(regex!=null){
						if(v.toString().matches(regex.value())==false){
							String msg=regex.message();
							if(msg==null || msg.length()==0){
								msg="NOT MATCH REGEX: "+regex.value();
							}
							result.add(fgs.getFieldName()+": "+msg);
						}
					}
					
					Max max=fgs.getField().getAnnotation(Max.class);
					if(max!=null){
						Long x=(Long)ConvertUtils.convert(v,Long.class);
						if(x > max.value()){
							String msg=max.message();
							if(msg==null || msg.length()==0){
								msg="CAN NOT > "+max.value();
							}
							result.add(fgs.getFieldName()+": "+msg);						
						}
					}
					
					Min min=fgs.getField().getAnnotation(Min.class);
					if(min!=null){
						Long x=(Long)ConvertUtils.convert(v,Long.class);
						if(x < min.value()){
							String msg=min.message();
							if(msg==null || msg.length()==0){
								msg="CAN NOT < "+min.value();
							}
							result.add(fgs.getFieldName()+": "+msg);						
						}
					}
				}
			}
		}
		
		return result;
	}

}
