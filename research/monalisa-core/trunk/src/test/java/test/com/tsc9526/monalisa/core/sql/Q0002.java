package test.com.tsc9526.monalisa.core.sql;

import com.tsc9526.monalisa.core.query.Query;

public class Q0002{
	/**
 测试查询A 
	*/
	public final static String testFindAll_A="test.com.tsc9526.monalisa.core.sql.Q0002.testFindAll_A";
	/**
 测试查询A 
	*/
	public static Query testFindAll_A(String name, String title, String create_by){
		 return Query.create(testFindAll_A,name,title,create_by); 
	}

	/**
 测试查询<B> 
	*/
	public final static String testFindAll_B="test.com.tsc9526.monalisa.core.sql.Q0002.testFindAll_B";
	/**
 测试查询<B> 
	*/
	public static Query testFindAll_B(String name, String title, String create_by){
		 return Query.create(testFindAll_B,name,title,create_by); 
	}

}