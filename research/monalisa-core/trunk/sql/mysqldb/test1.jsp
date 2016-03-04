<monalisa> 
	<query id="testFindAll">
		<%
		session.getAttribute("#1");
		
		String name=(String)request.getAttribute("#1");
		String title=request.getParameter("#1");
		String create_by=request.getParameter("#1");
		%>
	
		SELECT * FROM test_table_1
		WHERE 2=1 
		<%if(name != null && name.trim().length()>0)%>AND name ='$name'
		<%if(title!=null)%>AND title like '%$title%'
		<%if(create_by!=null){%>AND create_by ='$create_by'<%}%>
	</query> 
</monalisa>