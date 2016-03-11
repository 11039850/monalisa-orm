<%@page import="java.util.Date"%>
<%@page import="com.tsc9526.monalisa.core.query.Args"%>
<%@ page import="com.tsc9526.monalisa.core.meta.MetaTable"%>
<%@ page import="test.com.tsc9526.monalisa.core.mysql.MysqlDB,com.tsc9526.monalisa.core.query.Query"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%Query q=new Query(); Args args=new Args();%> 
<!-- hello -->
<query package="example" db="<%=MysqlDB.class%>"> 
	<!-- 测试查询A --> 
	<q id="testFindAll_A"> 
		<%{	
			String name     =args.pop();
			String title    =args.pop("");
			String create_by=args.pop("");
		
			Date fromTime=new Date();
			Date endTime =new Date();
			title="x";
			
			name="N%";
		%>
		SELECT * FROM test_table_1 a where name like $name
		<%}%>
	</q>
	
	<!-- 测试查询<B> -->
	<q id="testFindAll_B" >
	<%{
		String name=args.pop(""),title=args.pop(""),create_by=args.pop("");
		
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