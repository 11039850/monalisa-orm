<%@ page import="com.tsc9526.monalisa.core.meta.MetaTable"%>
<%@ page import="test.com.tsc9526.monalisa.core.mysql.MysqlDB,com.tsc9526.monalisa.core.query.Query"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%Query q=new Query();%> 
<query package="monalisa.query" db="<%=MysqlDB.class%>"> 

	<!-- 测试查询A --> 
	<q id="testFindAll_A">
		<%{	
			MetaTable table =q.dynamic();
			String title    =q.dynamic("");
			String create_by=q.dynamic("");
		
			String name=trim(table.getName());
		%>
		SELECT * FROM test_table_1 
		WHERE 2 > 1 AND name <%q.in(name);%> AND
		create_time > $fromTime AND create_time < $endTime
		<%if(name != null && name.trim().length()>0)%>AND name = $name
		<%if(title!=null)%>AND title like $title
		<%if(create_by!=null){%>AND create_by = $create_by <%}%>
		<%}%>
	</q>
	
	<!-- 测试查询<B> -->
	<q id="testFindAll_B" >
	<%{
		String name=q.dynamic(""),title=q.dynamic(""),create_by=q.dynamic("");
		
		q.add("SELECT * FROM test_table_1");
	%>
		WHERE 2=1
		
		<%if(name.length()>0)%>AND name = <%=name%>
		<%if(title!=null)%>AND title like $title
		<%if(create_by!=null){%>AND create_by =$create_by<%}%>
	
	<%}%>
	</q>	
</query>
<%!
	public String trim(String a){
		return a==null?"":a.trim();
	}
%>