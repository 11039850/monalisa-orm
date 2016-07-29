<%@page import="com.tsc9526.monalisa.orm.meta.MetaIndex"
%><%@page import="com.tsc9526.monalisa.orm.meta.MetaColumn"
%><%@page import="com.tsc9526.monalisa.orm.meta.MetaTable"
%><%@page import="java.util.Set"%><%
MetaTable    table =(MetaTable)request.getAttribute("table");
Set<?>     imports =(Set<?>)request.getAttribute("imports");
String   modelClass=(String)request.getAttribute("modelClass");
String   dbi       =(String)request.getAttribute("dbi");
%>package <%=table.getJavaPackage()%>;
 		
<%for(Object i:imports){%>
import <%=i%>; <%} %>
 
/**
 *
 * Auto generated code by monalisa 1.6.1
 *
 */
@Table(
	name="<%=table.getName() %>",
	primaryKeys={<%for(MetaColumn k:table.getKeyColumns()){%><%=k==table.getKeyColumns().get(0)?"":", "%>"<%=k.getName()%>"<%}%>},
	remarks="<%=toJavaString(table.getRemarks())%>",
	indexes={<%for(MetaIndex index:table.getIndexes()){ %>
		<%=index==table.getIndexes().get(0)?"  ":", "%>@Index(name="<%=index.getName()%>", type=<%=index.getType()%>, unique=<%=index.isUnique()%>, fields={<%for(MetaColumn c:index.getColumns()){ %><%=c==index.getColumns().get(0)?"":", "%>"<%=c.getName()%>"<%}%>}) <%} %>
	}
)
public class <%=table.getJavaName()%> extends <%=modelClass%><<%=table.getJavaName()%>> implements <%=dbi %>{
	private static final long serialVersionUID = <%=table.getSerialID()%>L;
		 
	public static final Insert INSERT(){
	 	return new Insert(new <%=table.getJavaName()%>());
	}
	
	public static final Delete DELETE(){
	 	return new Delete(new <%=table.getJavaName()%>());
	}
	
	public static final Update UPDATE(<%=table.getJavaName()%> model){
		return new Update(model);
	}		
	
	public static final Select SELECT(){
	 	return new Select(new <%=table.getJavaName()%>());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static Criteria WHERE(){
		return new Example().createCriteria();
	}
	 
	public <%=table.getJavaName()%>(){
		super("<%=table.getName()%>"<%for(MetaColumn k:table.getKeyColumns()){%>, "<%=k.getName() %>"<%}%>);		
	}		 
	
	<%if(table.getKeyColumns().size()>0){ %>
	/**
	 * Constructor use primary keys.
	 *<%for(MetaColumn k:table.getKeyColumns()){%>
	 * @param <%=k.getJavaName()%>  <%=toComments(k.getRemarks()) %><%}%>	 
	 */
	public <%=table.getJavaName()%>(<%for(MetaColumn k:table.getKeyColumns()){%><%=k==table.getKeyColumns().get(0)?"":", "%><%=k.getJavaType() %> <%=k.getJavaName()%><%}%>){
		super("<%=table.getName()%>"<%for(MetaColumn k:table.getKeyColumns()){%>, "<%=k.getName() %>"<%}%>);
		<%for(MetaColumn k:table.getKeyColumns()){%>
		this.<%=k.getJavaName()%> = <%=k.getJavaName()%>;
		fieldChanged("<%=k.getJavaName()%>");
		<%} %>
	}	 
	<%} %>
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table,f,"	","\t")%><%
	String annotation=f.getCode("annotation");
	if(annotation!=null){ 
		for(String a:annotation.split("\n")){
			out.println("\t"+a+"\r\n");
		}
	}%>
	private <%=f.getJavaType() %> <%=f.getJavaName()%>;	
	<%}%>
	
	
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table,f,"	","\t")%> 
	public <%=table.getJavaName()%> <%=f.getJavaNameSet()%>(<%=f.getJavaType()%> <%=f.getJavaName()%>){
		<%
		String set=f.getCode("set");
		if(set!=null){ 
			out.println(set);
		}else{%>this.<%=f.getJavaName()%> = <%=f.getJavaName()%>;<%}%>  
		
		fieldChanged("<%=f.getJavaName()%>");
		
		return this;
	}
	
	<%} %>
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table,f,"	","\t")%> 
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(){
		<%
		String get=f.getCode("get");
		String value=f.getCode("value");
		if(get!=null){ 
			out.println(get);
		}else if(value!=null){
			out.println("return "+value+";");	
		}else{
			out.println("return this."+f.getJavaName()+";");
		}%> 
	}
	
	<%=getComments(table,f,"@param defaultValue  Return the default value if "+f.getJavaName()+" is null.","\t")%> 
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(<%=f.getJavaType()%> defaultValue){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	<%} %>
	
	public static class Insert extends com.tsc9526.monalisa.orm.dao.Insert<<%=table.getJavaName()%>>{
		Insert(<%=table.getJavaName()%> model){
			super(model);
		}	 
	}	
	
	public static class Delete extends com.tsc9526.monalisa.orm.dao.Delete<<%=table.getJavaName()%>>{
		Delete(<%=table.getJavaName()%> model){
			super(model);
		}
		 
		<%if(table.getKeyColumns().size()>0){ %>
		public int deleteByPrimaryKey(<%for(MetaColumn k:table.getKeyColumns()){%><%=k==table.getKeyColumns().get(0)?"":", "%><%=k.getJavaType() %> <%=k.getJavaName()%><%}%>){
			<%for(MetaColumn k:table.getKeyColumns()){ %>if(<%=k.getJavaName()%> ==null ) return 0;	
			<%} %>
			<%for(MetaColumn k:table.getKeyColumns()){ %>this.model.<%=k.getJavaName()%> = <%=k.getJavaName()%>;
			<%} %>
			return this.model.delete();				
		}<%} %>
		<%for(MetaIndex index:table.getIndexes()){ %>
		<%
			String m="";
			for(MetaColumn c:index.getColumns()){
				m=m+firstUpper(c.getJavaName());
			}
			if(m.equals("PrimaryKey")){
				m= "UKPrimaryKey";
			}
		%><%if(index.isUnique()){ %>
		/**
		* Delete by unique key: <%=index.getName() %><%for(MetaColumn c:index.getColumns()){ %>
		* @param <%=c.getJavaName()%> <%=toComments(c.getRemarks()) %><%} %>	
		*/
		public int deleteBy<%=m%>(<%for(MetaColumn k:index.getColumns()){%><%=k==index.getColumns().get(0)?"":", "%><%=k.getJavaType() %> <%=k.getJavaName()%><%}%>){			 
			<%for(MetaColumn k:index.getColumns()){ %>this.model.<%=k.getJavaName()%>=<%=k.getJavaName()%>;
			<%} %>			 
			 
			return this.model.delete();
		}			 
		<%} %> <%} %>
	}
	
	public static class Update extends com.tsc9526.monalisa.orm.dao.Update<<%=table.getJavaName()%>>{
		Update(<%=table.getJavaName()%> model){
			super(model);
		}		 			 			 		
	}
	
	public static class Select extends com.tsc9526.monalisa.orm.dao.Select<<%=table.getJavaName()%>,Select>{		
		Select(<%=table.getJavaName()%> x){
			super(x);
		}					 
		<%if(table.getKeyColumns().size()>0){%>
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public <%=table.getJavaName()%> selectByPrimaryKey(<%for(MetaColumn k:table.getKeyColumns()){%><%=k==table.getKeyColumns().get(0)?"":", "%><%=k.getJavaType() %> <%=k.getJavaName()%><%}%>){
			<%for(MetaColumn k:table.getKeyColumns()){ %>if(<%=k.getJavaName()%> ==null ) return null;
			<%} %>
			
			<%for(MetaColumn k:table.getKeyColumns()){ %>this.model.<%=k.getJavaName()%> = <%=k.getJavaName()%>;
			<%} %>
			
			this.model.load();
				 			 	 
			if(this.model.entity()){
				return this.model;
			}else{
				return null;
			}
		}				 
		<%}%>
		
		<%boolean select_to_map=false; %>
		<%for(MetaIndex index:table.getIndexes()){ %>
		<%
			String m="";
			for(MetaColumn c:index.getColumns()){
				m=m+firstUpper(c.getJavaName());
			}
			if(m.equals("PrimaryKey")){
				m= "UKPrimaryKey";
			}
		%><%if(index.isUnique()){ %>
		/**
		* Find by unique key: <%=index.getName() %><%for(MetaColumn c:index.getColumns()){ %>
		* @param <%=c.getJavaName()%> <%=toComments(c.getRemarks()) %><%} %>	
		*/
		public <%=table.getJavaName()%> selectBy<%=m%>(<%for(MetaColumn k:index.getColumns()){%><%=k==index.getColumns().get(0)?"":", "%><%=k.getJavaType() %> <%=k.getJavaName()%><%}%>){	
			Criteria c=WHERE();
			<%for(MetaColumn k:index.getColumns()){ %>
			c.<%=k.getJavaName()%>.eq(<%=k.getJavaName()%>);<%} %>			 
			 
			return super.selectOneByExample(c.example);
		}			 
		<%
		if(index.getColumns().size()==1){
			MetaColumn k=index.getColumns().get(0);
			
			select_to_map=true;
		%>
		/**
		* List result to Map, The map key is unique-key: <%=k.getName() %> 
		*/
		public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectToMapWith<%=firstUpper(k.getJavaName())%>(String whereStatement,Object ... args){
			List<<%=table.getJavaName()%>> list=super.select(whereStatement,args);
			
			Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> m=new LinkedHashMap<<%=k.getJavaType()%>,<%=table.getJavaName()%>>();
			for(<%=table.getJavaName()%> x:list){
				m.put(x.<%=k.getJavaNameGet()%>(),x);
			}
			return m;
		}
		
		/**
		* List result to Map, The map key is unique-key: <%=k.getJavaName()%> 
		*/
		public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectByExampleToMapWith<%=firstUpper(k.getJavaName())%>(Example example){
			List<<%=table.getJavaName()%>> list=super.selectByExample(example);
			
			Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> m=new LinkedHashMap<<%=k.getJavaType()%>,<%=table.getJavaName()%>>();
			for(<%=table.getJavaName()%> x:list){
				m.put(x.<%=k.getJavaNameGet()%>(),x);
			}
			return m;
		}
		<%}%><%}%><%}%>
		<%
		if(table.getKeyColumns().size()==1){
			MetaColumn k=table.getKeyColumns().get(0);
			select_to_map=true;
		%>		
		/**
		* List result to Map, The map key is primary-key:  <%=k.getJavaName()%>
		*/
		public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectToMap(String whereStatement,Object ... args){
			List<<%=table.getJavaName()%>> list=super.select(whereStatement,args);
			
			Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> m=new LinkedHashMap<<%=k.getJavaType()%>,<%=table.getJavaName()%>>();
			for(<%=table.getJavaName()%> x:list){
				m.put(x.<%=k.getJavaNameGet()%>(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: <%=k.getJavaName()%> 
		*/
		public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectByExampleToMap(Example example){
			List<<%=table.getJavaName()%>> list=super.selectByExample(example);
			
			Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> m=new LinkedHashMap<<%=k.getJavaType()%>,<%=table.getJavaName()%>>();
			for(<%=table.getJavaName()%> x:list){
				m.put(x.<%=k.getJavaNameGet()%>(),x);
			}
			return m;
		}
		<%}%>
		
		<%if(select_to_map){ %>
		public SelectForExample selectForExample(Example example){
			return new SelectForExample(example);
		} 	
		
		public class SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<<%=table.getJavaName()%>,Select>.SelectForExample{
			public SelectForExample(Example example) {
				super(example);
			}
			
			<%for(MetaIndex index:table.getIndexes()){ 
				if(index.isUnique() && index.getColumns().size()==1){
					String m="";
					for(MetaColumn c:index.getColumns()){
						m=m+firstUpper(c.getJavaName());
					}
					if(m.equals("PrimaryKey")){
						m= "UKPrimaryKey";
					}
					
					MetaColumn k=index.getColumns().get(0);%>
			/**
			* List result to Map, The map key is unique-key: <%=k.getJavaName()%> 
			*/
			public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectToMapWith<%=firstUpper(k.getJavaName())%>(){
				return selectByExampleToMapWith<%=firstUpper(k.getJavaName())%>((Example)this.example);
			}
			<%}%><%}%>
			<%
			if(table.getKeyColumns().size()==1){
				MetaColumn k=table.getKeyColumns().get(0);
			%>		
			/**
			* List result to Map, The map key is primary-key:  <%=k.getJavaName()%>
			*/
			public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectToMap(){
				return selectByExampleToMap((Example)this.example);
			}
			<%}%>
		}
		<%}%>	
	}
	 
		
	public static class Example extends com.tsc9526.monalisa.orm.criteria.Example<Criteria,<%=table.getJavaName()%>>{
		public Example(){}
		 
		protected Criteria createInternal(){
			Criteria x= new Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=ClassHelper.findClassWithAnnotation(<%=table.getJavaName()%>.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		<%
		if(table.getKeyColumns().size()==1){
			MetaColumn k=table.getKeyColumns().get(0);
		%>
		/**
		* List result to Map, The map key is primary-key: <%=k.getJavaName()%> 
		*/
		public Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> selectToMap(){			
			List<<%=table.getJavaName()%>> list=SELECT().selectByExample(this);
			
			Map<<%=k.getJavaType()%>,<%=table.getJavaName()%>> m=new LinkedHashMap<<%=k.getJavaType()%>,<%=table.getJavaName()%>>();
			for(<%=table.getJavaName()%> x:list){
				m.put(x.<%=k.getJavaNameGet()%>(),x);
			}
			return m;
		}
		<%}%>
		
	}
	
	public static class Criteria extends com.tsc9526.monalisa.orm.criteria.Criteria<Criteria>{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		/**
		 * Create Select for example
		 */
		public Select.SelectForExample SELECT(){
			return <%=table.getJavaName()%>.SELECT().selectForExample(this.example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(<%=table.getJavaName()%> m){			 
			return UPDATE(m).updateByExample(this.example);
		}
				
		/**
		* Delete records with this example
		*/		
		public int delete(){
			return DELETE().deleteByExample(this.example);
		}
		
		/**
		* Append "OR" Criteria  
		*/	
		public Criteria OR(){
			return this.example.or();
		}
		
		<%for(MetaColumn f:table.getColumns()){ %>
		<%=getComments(table, f, "		","\t\t")%>
		<%if(f.getJavaType().equals("Integer")){      %>public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria> <%=f.getJavaName()%> = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<Criteria>("<%=f.getName()%>", this);
		<%}else if(f.getJavaType().equals("Short")){  %>public com.tsc9526.monalisa.orm.criteria.Field.FieldShort<Criteria> <%=f.getJavaName()%> = new com.tsc9526.monalisa.orm.criteria.Field.FieldShort<Criteria>("<%=f.getName()%>", this);
		<%}else if(f.getJavaType().equals("Long")){   %>public com.tsc9526.monalisa.orm.criteria.Field.FieldLong<Criteria> <%=f.getJavaName()%> = new com.tsc9526.monalisa.orm.criteria.Field.FieldLong<Criteria>("<%=f.getName()%>", this); 
		<%}else if(f.getJavaType().equals("String")){ %>public com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria> <%=f.getJavaName()%> = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<Criteria>("<%=f.getName()%>", this);
		<%}else{                                      %>public com.tsc9526.monalisa.orm.criteria.Field<<%=f.getJavaType()%>,Criteria> <%=f.getJavaName()%> = new com.tsc9526.monalisa.orm.criteria.Field<<%=f.getJavaType()%>,Criteria>("<%=f.getName()%>", this, <%=f.getJdbcType()%>);		 
		<%} %>	<%}%>
	}
	 
	<%for(MetaColumn f:table.getColumns()){  
		String em=f.getCode("enum");
		if(em!=null && em.indexOf("{")>=0){ 
	%>		
	public static enum <%=em%>
	<%}%><%}%>
	 
	public static class M{
		public final static String TABLE ="<%=table.getName()%>" ;
		
		<%for(MetaColumn f:table.getColumns()){ %>
		<%=getComments(table, f, "		","\t\t")%>
		public final static String  <%=f.getJavaName()%>         = "<%=f.getName()%>" ;
		
		public final static String  <%=f.getJavaName()%>$name    = "<%=f.getName()%>" ;
		public final static boolean <%=f.getJavaName()%>$key     = <%=f.isKey()?"true":"false"%>;
		public final static int     <%=f.getJavaName()%>$length  = <%=f.getLength()%>;
		public final static String  <%=f.getJavaName()%>$value   = "<%=f.getValue()==null?"NULL":toJavaString(f.getValue())%>" ;
		public final static String  <%=f.getJavaName()%>$remarks = "<%=toJavaString(f.getRemarks())%>" ;
		public final static boolean <%=f.getJavaName()%>$auto    = <%=f.isAuto()?"true":"false"%> ;
		public final static boolean <%=f.getJavaName()%>$notnull = <%=f.isNotnull()?"true":"false"%>;
		
		<%}%>		 
	}
}

<%!
	String toComments(String remarks){
		return remarks==null?"": remarks.replace("*/","**");
	}
	
	String toJavaString(String s){
		if(s==null)return "";
		
		return s.trim().replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");
	}

	
	String getComments(MetaTable table,MetaColumn c,String params,String leftPadding){
		String cname=c.getName();
		
		if(cname!=null && cname.length()>0 && c.getTable()!=null){	
			String r="/**\r\n"+leftPadding;
			r+="* @Column\r\n"+leftPadding; 
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
			r+="\r\n"+leftPadding;
			
			if(c.getLength()>0 || c.getValue()!=null){
				r+="* <li>&nbsp;&nbsp;&nbsp;";
			
				if(c.getLength()>0){
					r+="<B>length:</B> "+c.getLength();
				}
				if(c.getValue()!=null){
					r+=" &nbsp;<B>value:</B> "+toJavaString(c.getValue());
				}
				r+="<br>\r\n"+leftPadding;
			}
			
			if(c.getRemarks()!=null){
				r+="* <li><B>remarks:</B> "+toComments(c.getRemarks())+"\r\n"+leftPadding;
			}
			 
			if(params==null){
				params="";
			}
			params=params.trim();
			if(params.length()>0){
				r+="* "+params+"\r\n"+leftPadding;
			}
			
		 	r+="*/\r\n"+leftPadding;	
		 
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
