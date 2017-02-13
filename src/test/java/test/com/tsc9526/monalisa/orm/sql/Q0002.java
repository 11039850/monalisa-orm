package test.com.tsc9526.monalisa.orm.sql;

import com.tsc9526.monalisa.orm.Query;

import com.tsc9526.monalisa.orm.compiler.SQLResourceManager;

public class Q0002{
	private static SQLResourceManager SQLRM=SQLResourceManager.getInstance();
	/**
 测试查询A 
	*/
	public final static String testFindAll_A="test.com.tsc9526.monalisa.orm.sql.Q0002.testFindAll_A";
	/**
 测试查询A 
	*/
	public static Query testFindAll_A(String name, String title, String create_by){
		 return SQLRM.createQuery(testFindAll_A,name,title,create_by); 
	}

	/**
 测试查询<B> 
	*/
	public final static String testFindAll_B="test.com.tsc9526.monalisa.orm.sql.Q0002.testFindAll_B";
	/**
 测试查询<B> 
	*/
	public static Query testFindAll_B(String name, String title, String create_by){
		 return SQLRM.createQuery(testFindAll_B,name,title,create_by); 
	}

}