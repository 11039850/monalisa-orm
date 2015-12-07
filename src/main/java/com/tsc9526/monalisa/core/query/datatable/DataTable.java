package com.tsc9526.monalisa.core.query.datatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;
import com.tsc9526.monalisa.core.tools.ClassHelper.MetaClass;

 /**
  *  
  * @author zzg.zhou(11039850@qq.com)
  */
public class DataTable<E> extends ArrayList<E> { 
	private static final long serialVersionUID = 6839964505006290332L;
	
	private List<DataColumn> headers=new ArrayList<DataColumn>();
		
	public DataTable() {		
	}
	
	public DataTable(Collection<? extends E> cs) {
		super(cs);
	}
	
	public DataTable<DataRow> select(String columns,String where,String orderBy,String groupBy){
		return null;
	}	 
	
	public DataTable<DataRow> join(DataTable<?> rightTable, String leftFieldName,String rightFieldName){
		return null;
	}

	public DataTable<DataRow> where(String where,String orderBy){
		return select("*",where,orderBy,null);
	}
	
	public DataTable<DataRow> join(DataTable<?> rightTable, String joinFieldName){
		return join(rightTable, joinFieldName,joinFieldName);
	}
	
	public synchronized List<DataColumn> getHeaders() {
		if(headers.size()==0 && this.size()>0){
			Object v=this.get(0);
			if(v!=null){
				if(v instanceof Map){
					int c=((Map<?,?>)v).size();
					for(int i=0;i<c;i++){
						headers.add(new DataColumn("c"+i));
					}
				}else{
					if(v.getClass().isPrimitive() || v.getClass().getName().startsWith("java.")){
						headers.add(new DataColumn("c0"));
					}else{					
						MetaClass mc=ClassHelper.getMetaClass(v.getClass());
						for(FGS fgs:mc.getFields()){
							headers.add(new DataColumn(fgs.getFieldName()));
						}
					}
				}
			}
		}
		return headers;
	}

	public void setHeaders(List<DataColumn> headers) {
		this.headers = headers;
	}

}
