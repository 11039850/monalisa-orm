<%@page import="com.tsc9526.monalisa.core.meta.MetaTable"%>
<%@page import="test.com.tsc9526.monalisa.core.mysql.MysqlDB,com.tsc9526.monalisa.core.query.DataMap"%>
<%@page language="java" pageEncoding="utf-8"%>

<!-- 默认数据库： test.com.tsc9526.monalisa.core.mysql.MysqlDB -->
<package name="monalisa.query" db="test.com.tsc9526.monalisa.core.mysql.MysqlDB"/> 

<!-- 测试查询A -->
<q id="testFindAll_A" db="<%=MysqlDB.class%>">
<%{
	MetaTable table;
	String title;
	String create_by;
	
	String name=table.getName();
%>
	SELECT * FROM test_table_1
	WHERE 2=1 
	<%if(name != null && name.trim().length()>0)%>AND name ='$name'
	<%if(title!=null)%>AND title like '%$title%'
	<%if(create_by!=null){%>AND create_by ='$create_by'<%}%>
<%}%>
</q>

<!-- 测试查询<B> -->
<q id="testFindAll_B">
<%{
	String name="",title="",create_by="";
	
	if(name==null)name="";
	name=name.trim();
%>
	SELECT * FROM test_table_1
	WHERE 2=1
	<%if(name.length()>0)%>AND name ='$name'
	<%if(title!=null)%>AND title like '%$title%'
	<%if(create_by!=null){%>AND create_by ='$create_by'<%}%>

<%}%>
</q>