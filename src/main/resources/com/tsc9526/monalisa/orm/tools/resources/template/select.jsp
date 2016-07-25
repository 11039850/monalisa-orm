<%@page import="com.tsc9526.monalisa.orm.meta.MetaColumn"%>
<%@page import="java.util.Set"%>
<%@page import="com.tsc9526.monalisa.orm.meta.MetaTable"%>
<%
MetaTable    table =(MetaTable)request.getAttribute("table");
Set<?>     imports =(Set<?>)request.getAttribute("imports");
String   fingerprint=(String)request.getAttribute("fingerprint");
String   see        =(String)request.getAttribute("see");
%>
package <%=table.getJavaPackage()%>;
 
<%for(Object i:imports){ %>
import <%=i%>;
<%} %>
 
/**
 * 
 * @see <%=see%>
 */
public class <%=table.getJavaName()%> implements java.io.Serializable{
	private static final long serialVersionUID = <%=table.getSerialID()%>L;	
	final static String  FINGERPRINT = "<%=fingerprint %>";
	  
	 
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	") %>
	private <%=f.getJavaType()%> <%=f.getJavaName()%>;	
	
	<%}%>
	
	
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	") %>
	public <%=table.getJavaName()%> <%=f.getJavaNameSet()%>(<%=f.getJavaType()%> <%=f.getJavaName()%>){
		this.<%=f.getJavaName()%> = <%=f.getJavaName()%>;
		return this;
	}
	
	<%}%>
	
	 
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	") %>
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(){
		return this.<%=f.getJavaName()%>;		
	}
	
	<%=getComments(table, f, "@param defaultValue  Return the default value if "+f.getJavaName()+" is null.") %>
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(<%=f.getJavaType()%> defaultValue){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	<%}%>
		 
}
 
<%!
	String toComments(String remarks){
		return remarks==null?"": remarks.replace("*/","**");
	}
	
	String toJavaString(String s){
		if(s==null)return "";
		
		return s.trim().replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");
	}

	
	String getComments(MetaTable table,MetaColumn c,String params){
		String cname=c.getName();
		
		if(cname!=null && cname.length()>0 && c.getTable()!=null){	
			String r="/**\r\n";
			r+="* @Column\r\n"; 
			r+="* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> "+c.getTable().getName()+"&nbsp;<B>name:</B> "+cname;
			
			if(c.isKey() || c.isAuto() || c.isNotnull() || c.isEnum()){
				boolean b=false;
				r+=" &nbsp;[";
				if(c.isKey()){
					r+=(b?"|":"")+"<font color=red>KEY</font>";
					b=true;
				}
				if(c.isAuto()){
					r+=(b?"|":"")+"<font color=red>AUTO</font>";
					b=true;
				}
				if(c.isNotnull()){
					r+=(b?"|":"")+"<font color=red>NOTNULL</font>";
					b=true;
				}
				if(c.isEnum()){
					r+=(b?"|":"")+"<font color=red>ENUM</font>";
					b=true;
				}
				r+="]";
			}
			r+="\r\n";
			
			if(c.getLength()>0 || c.getValue()!=null){
				r+="* <li>&nbsp;&nbsp;&nbsp;";
			
				if(c.getLength()>0){
					r+="<B>length:</B> "+c.getLength();
				}
				if(c.getValue()!=null){
					r+=" &nbsp;<B>value:</B> "+toJavaString(c.getValue());
				}
				r+="<br>\r\n";
			}
			
			if(c.getRemarks()!=null){
				r+="* <li><B>remarks:</B> "+toComments(c.getRemarks())+"\r\n";
			}
			 
			if(params==null){
				params="";
			}
			params=params.trim();
			if(params.length()>0){
				r+="* "+params;
			}
			
		 	r+="*/\r\n";	
		 
		 	String f=c.getTable().getJavaName()+".M.";
		 	if(c.getTable().getJavaPackage().equals(table.getJavaPackage())){
		 		f="M.";
		 	}
			 	
			String[] names=new String[]{"name","key","auto","notnull","length","value","remarks"};
			
			r+="@Column(table="+f+"TABLE, jdbcType="+c.getJdbcType();
			for(String n:names){
				String colname=c.nameToJava();
				int p=colname.indexOf("$");
				if(p>0){
					colname=colname.substring(p+1);
				}
			
				r+=", "+n+"="+f+colname+"$"+n;
			}
			r+=")";
			
			return r;
		}else{
			return "";
		}
	}

	String firstUpper(String s){
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}

	String html(Object v){
		if(v==null){
			return "";
		}else{
			return v.toString().trim();
		}
	}
%>