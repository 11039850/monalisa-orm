package com.tsc9526.monalisa.core.query;

import java.io.Serializable;
import java.util.List;

import com.tsc9526.monalisa.core.query.datatable.DataTable;


/**
 * 页面数据
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -33321L;
	
	private DataTable<T> list=new DataTable<T>();	
	 
	private long pageNo   = 1;				 
	private long pageSize = 10;				 
	private long totalPage= 0;				 
	private long totalRow = 0;				 
 
	public Page(){
	}
	
	/**
	 * 构造页面数据对象
	 * 
	 * @param list    本页的数据集合
	 * @param total   总记录数
	 * @param limit   页面大小
	 * @param offset  本记录起始位置（0 表示第一条）
	 */
	public Page(List<T> list,long total, long limit , long offset) {
		if(list!=null){
			if(list instanceof DataTable){
				this.list = (DataTable<T>)list;
			}else{
				this.list = new DataTable<T>(list);
			}
		}else {
			this.list=null;
		}
		
		this.totalRow   = total;
		this.pageSize   = limit;
		
		this.pageNo = 1 + offset/limit;
		
		this.totalPage = (int) (this.totalRow / this.pageSize);
		if (this.totalRow % this.pageSize != 0) {
			this.totalPage++;
		}
	}
	
	/**
	 *  
	 * @return 一个非null的数据集
	 */
	public DataTable<T> getList() {
		return list;
	}	
	 
	/**
	 * 
	 * @return 页面号， 1表示第1页 ...
	 */
	public long getPageNo() {
		return pageNo;
	}
	
 
	/**
	 * 
	 * @return 页面大小
	 */
	public long getPageSize() {
		return pageSize;
	}
	 
	/**
	 * 
	 * @return 总页数
	 */
	public long getTotalPage() {
		return totalPage;
	}
		 
	/**
	 * 
	 * @return 总记录数
	 */
	public long getTotalRow() {
		return totalRow;
	}
}


