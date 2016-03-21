package com.tsc9526.monalisa.core.generator;


import java.io.PrintWriter;
import com.tsc9526.monalisa.core.parser.jsp.JspContext;
import com.tsc9526.monalisa.core.meta.MetaIndex;
import com.tsc9526.monalisa.core.meta.MetaColumn;
import java.util.Set;
import com.tsc9526.monalisa.core.meta.MetaTable;
/**@author zzg.zhou(11039850@qq.com) */
public class DBWriterModel{
	
	String toComments(String remarks){
		return remarks==null?"": remarks.replace("*/","**");
	}
	
	String toJavaString(String s){
		if(s==null)return "";
		
		return s.trim().replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");
	}

	
	String getComments(MetaTable table,MetaColumn c,String align){
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
	public void service(JspContext request,PrintWriter out){
		out.println("");
			out.println("");
			out.println("");
			out.println("");
			
MetaTable    table =(MetaTable)request.getAttribute("table");
Set<?>     imports =(Set<?>)request.getAttribute("imports");
String   modelClass=(String)request.getAttribute("modelClass");
String   dbi       =(String)request.getAttribute("dbi");
		out.println("");
			out.print("package ");
			out.print(table.getJavaPackage());
			out.println(";");
			out.println(" 		");
			for(Object i:imports){ 		out.println("");
			out.print("import ");
			out.print(i);
			out.println(";");
			} 		out.println("");
			out.println(" ");
			out.println("");
			out.println("@Table(");
			out.print("	name=\"");
			out.print(table.getName() );
			out.println("\",");
			out.print("	primaryKeys={");
			for(MetaColumn k:table.getKeyColumns()){		out.print("\"");
			out.print(k==table.getKeyColumns().get(0)?"":", ");
			out.print(k.getName());
			out.print("\"");
			}		out.println("},");
			out.print("	remarks=\"");
			out.print(toJavaString(table.getRemarks()));
			out.println("\",");
			out.println("	indexes={		");
			out.print("		");
			for(MetaIndex index:table.getIndexes()){ 		out.println("");
			out.print("		");
			out.print(index==table.getIndexes().get(0)?"":", ");
			out.print("@Index(name=\"");
			out.print(index.getName());
			out.print("\", type=");
			out.print(index.getType());
			out.print(", unique=");
			out.print(index.isUnique());
			out.print(", fields={");
			for(MetaColumn c:index.getColumns()){ 		out.print(c==index.getColumns().get(0)?"":", ");
			out.print("\"");
			out.print(c.getName());
			out.print("\"");
			}		out.println("})");
			out.print("		");
			} 		out.println("");
			out.println("	}");
			out.println(")");
			out.print("public class ");
			out.print(table.getJavaName());
			out.print(" extends ");
			out.print(modelClass);
			out.print("<");
			out.print(table.getJavaName());
			out.print("> implements ");
			out.print(dbi );
			out.println("{");
			out.print("	private static final long serialVersionUID = ");
			out.print(table.getSerialID());
			out.println("L;");
			out.println("		 ");
			out.println("	public static final Insert INSERT(){");
			out.print("	 	return new Insert(new ");
			out.print(table.getJavaName());
			out.println("());");
			out.println("	}");
			out.println("	");
			out.println("	public static final Delete DELETE(){");
			out.print("	 	return new Delete(new ");
			out.print(table.getJavaName());
			out.println("());");
			out.println("	}");
			out.println("	");
			out.print("	public static final Update UPDATE(");
			out.print(table.getJavaName());
			out.println(" model){");
			out.println("		return new Update(model);");
			out.println("	}		");
			out.println("	");
			out.println("	public static final Select SELECT(){");
			out.print("	 	return new Select(new ");
			out.print(table.getJavaName());
			out.println("());");
			out.println("	}	 	 ");
			out.println("	 ");
			out.println("	");
			out.println("	/**");
			out.println("	* Simple query with example <br>");
			out.println("	* ");
			out.println("	*/");
			out.println("	public static Criteria WHERE(){");
			out.println("		return new Example().createCriteria();");
			out.println("	}");
			out.println("	 ");
			out.print("	public ");
			out.print(table.getJavaName());
			out.println("(){");
			out.print("		super(\"");
			out.print(table.getName());
			out.print("\"");
			for(MetaColumn k:table.getKeyColumns()){		out.print(", \"");
			out.print(k.getName() );
			out.print("\"");
			}		out.println(");		");
			out.println("	}		 ");
			out.println("	");
			out.println("	 ");
			out.print("	");
			if(table.getKeyColumns().size()>0){ 		out.println("");
			out.println("	/**");
			out.println("	 * Constructor use primary keys.");
			out.println("	 *");
			out.print("	");
			for(MetaColumn k:table.getKeyColumns()){		out.println("");
			out.print("	 * @param ");
			out.print(k.getJavaName());
			out.print("  ");
			out.print(toComments(k.getRemarks()) );
			out.println("");
			out.print("	");
			}		out.println("	 ");
			out.println("	 */");
			out.print("	public ");
			out.print(table.getJavaName());
			out.print("(");
			for(MetaColumn k:table.getKeyColumns()){		out.print(k==table.getKeyColumns().get(0)?"":", ");
			out.print(k.getJavaType() );
			out.print(" ");
			out.print(k.getJavaName());
			}		out.println("){");
			out.print("		super(\"");
			out.print(table.getName());
			out.print("\"");
			for(MetaColumn k:table.getKeyColumns()){		out.print(", \"");
			out.print(k.getName() );
			out.print("\"");
			}		out.println(");");
			out.println("	");
			out.print("		");
			for(MetaColumn k:table.getKeyColumns()){		out.println("");
			out.print("		this.");
			out.print(k.getJavaName());
			out.print(" = ");
			out.print(k.getJavaName());
			out.println(";");
			out.print("		fieldChanged(\"");
			out.print(k.getJavaName());
			out.println("\");");
			out.print("		");
			} 		out.println("");
			out.println("	}	 ");
			out.print("	");
			} 		out.println("");
			out.println("	");
			out.println("	 ");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table,f,"	"));
			out.println(" ");
			out.print("	");
			
	String annotation=f.getCode("annotation");
	if(annotation!=null){ 
		for(String a:annotation.split("\n")){
			out.println(a);
		}
	}
			out.println("");
			out.print("	private ");
			out.print(f.getJavaType() );
			out.print(" ");
			out.print(f.getJavaName());
			out.println(";	");
			out.print("	");
			}		out.println("");
			out.println("	");
			out.println("	");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table,f,"	"));
			out.println(" ");
			out.print("	public ");
			out.print(table.getJavaName());
			out.print(" ");
			out.print(f.getJavaNameSet());
			out.print("(");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaName());
			out.println("){");
			out.print("		");
			
		String set=f.getCode("set");
		if(set!=null){ 
			out.println(set);
		}else{
				out.println("");
			out.print("		this.");
			out.print(f.getJavaName());
			out.print(" = ");
			out.print(f.getJavaName());
			out.println(";");
			out.print("		");
			}		out.println("  ");
			out.println("		");
			out.print("		fieldChanged(\"");
			out.print(f.getJavaName());
			out.println("\");");
			out.println("		");
			out.println("		return this;");
			out.println("	}");
			out.println("	");
			out.print("	");
			} 		out.println("");
			out.println("	");
			out.println("	");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table,f,"	"));
			out.println(" ");
			out.print("	public ");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaNameGet());
			out.println("(){");
			out.print("		");
			
		String get=f.getCode("get");
		String value=f.getCode("value");
		if(get!=null){ 
			out.println(get);
		}else if(value!=null){
			out.println("return "+value+";");	
		}else{
			out.println("return this."+f.getJavaName()+";");
		}
				out.println(" ");
			out.println("	}");
			out.println("	");
			out.print("	");
			} 		out.println("");
			out.println("	");
			out.print("	public static class Insert extends com.tsc9526.monalisa.core.query.dao.Insert<");
			out.print(table.getJavaName());
			out.println(">{");
			out.print("		Insert(");
			out.print(table.getJavaName());
			out.println(" model){");
			out.println("			super(model);");
			out.println("		}	 ");
			out.println("	}	");
			out.println("	");
			out.print("	public static class Delete extends com.tsc9526.monalisa.core.query.dao.Delete<");
			out.print(table.getJavaName());
			out.println(">{");
			out.print("		Delete(");
			out.print(table.getJavaName());
			out.println(" model){");
			out.println("			super(model);");
			out.println("		}");
			out.println("		 ");
			out.print("		");
			if(table.getKeyColumns().size()>0){ 		out.println("");
			out.print("		public int deleteByPrimaryKey(");
			for(MetaColumn k:table.getKeyColumns()){		out.print(k==table.getKeyColumns().get(0)?"":", ");
			out.print(k.getJavaType() );
			out.print(" ");
			out.print(k.getJavaName());
			}		out.println("){");
			out.print("			");
			for(MetaColumn k:table.getKeyColumns()){ 		out.println("");
			out.print("			if(");
			out.print(k.getJavaName());
			out.println(" ==null ) return 0;			");
			out.print("			");
			} 		out.println("");
			out.println("						 			 ");
			out.print("			");
			for(MetaColumn k:table.getKeyColumns()){ 		out.println("");
			out.print("			this.model.");
			out.print(k.getJavaName());
			out.print(" = ");
			out.print(k.getJavaName());
			out.println(";");
			out.print("			");
			} 		out.println("");
			out.println("				 			 ");
			out.println("			return this.model.delete();				");
			out.println("		}				 ");
			out.print("		");
			} 		out.println("");
			out.println("		");
			out.println("		 ");
			out.print("		");
			for(MetaIndex index:table.getIndexes()){ 		out.println("");
			out.print("		");
			
			String m="";
			for(MetaColumn c:index.getColumns()){
				m=m+firstUpper(c.getJavaName());
			}
			if(m.equals("PrimaryKey")){
				m= "UKPrimaryKey";
			}
				out.println("");
			out.print("		");
			if(index.isUnique()){ 		out.println("");
			out.println("		/**");
			out.print("		* Delete by unique key: ");
			out.print(index.getName() );
			out.println("");
			out.print("		");
			for(MetaColumn c:index.getColumns()){ 		out.println("");
			out.print("		* @param ");
			out.print(c.getJavaName());
			out.print(" ");
			out.print(toComments(c.getRemarks()) );
			out.println("");
			out.print("		");
			} 		out.println("	");
			out.println("		*/");
			out.print("		public int deleteBy");
			out.print(m);
			out.print("(");
			for(MetaColumn k:index.getColumns()){		out.print(k==index.getColumns().get(0)?"":", ");
			out.print(k.getJavaType() );
			out.print(" ");
			out.print(k.getJavaName());
			}		out.println(")){			 ");
			out.print("			");
			for(MetaColumn k:index.getColumns()){ 		out.println("");
			out.print("			this.model.");
			out.print(k.getJavaName());
			out.print("=");
			out.print(k.getJavaName());
			out.println(";");
			out.print("			");
			} 		out.println("			 ");
			out.println("			 ");
			out.println("			return this.model.delete();");
			out.println("		}			 ");
			out.print("		");
			} 		out.println("		");
			out.println("		");
			out.print("		");
			} 		out.println("");
			out.println("		");
			out.println("	}");
			out.println("	");
			out.print("	public static class Update extends com.tsc9526.monalisa.core.query.dao.Update<");
			out.print(table.getJavaName());
			out.println(">{");
			out.print("		Update(");
			out.print(table.getJavaName());
			out.println(" model){");
			out.println("			super(model);");
			out.println("		}		 			 			 		");
			out.println("	}");
			out.println("	");
			out.print("	public static class Select extends com.tsc9526.monalisa.core.query.dao.Select<");
			out.print(table.getJavaName());
			out.println(",Select>{		");
			out.print("		Select(");
			out.print(table.getJavaName());
			out.println(" x){");
			out.println("			super(x);");
			out.println("		}					 ");
			out.println("		");
			out.print("		");
			if(table.getKeyColumns().size()>0){		out.println("");
			out.println("		/**");
			out.println("		* find model by primary keys");
			out.println("		*");
			out.println("		* @return the model associated with the primary keys,  null if not found.");
			out.println("		*/");
			out.print("		public ");
			out.print(table.getJavaName());
			out.print(" selectByPrimaryKey(");
			for(MetaColumn k:table.getKeyColumns()){		out.print(k==table.getKeyColumns().get(0)?"":", ");
			out.print(k.getJavaType() );
			out.print(" ");
			out.print(k.getJavaName());
			}		out.println("){");
			out.print("			");
			for(MetaColumn k:table.getKeyColumns()){ 		out.println("");
			out.print("			if(");
			out.print(k.getJavaName());
			out.println(" ==null ) return null;			");
			out.print("			");
			} 		out.println("");
			out.println("						");
			out.print("			");
			for(MetaColumn k:table.getKeyColumns()){ 		out.println("");
			out.print("			this.model.");
			out.print(k.getJavaName());
			out.print(" = ");
			out.print(k.getJavaName());
			out.println(";");
			out.print("			");
			} 		out.println("");
			out.println("			this.model.load();");
			out.println("				 			 	 ");
			out.println("			if(this.model.entity()){");
			out.println("				return this.model;");
			out.println("			}else{");
			out.println("				return null;");
			out.println("			}");
			out.println("		}				 ");
			out.print("		");
			}		out.println("");
			out.println("		");
			out.print("		");
			for(MetaIndex index:table.getIndexes()){ 		out.println("");
			out.print("		");
			
			String m="";
			for(MetaColumn c:index.getColumns()){
				m=m+firstUpper(c.getJavaName());
			}
			if(m.equals("PrimaryKey")){
				m= "UKPrimaryKey";
			}
				out.println("");
			out.print("		");
			if(index.isUnique()){ 		out.println("");
			out.println("		/**");
			out.print("		* Find by unique key: ");
			out.print(index.getName() );
			out.println("");
			out.print("		");
			for(MetaColumn c:index.getColumns()){ 		out.println("");
			out.print("		* @param ");
			out.print(c.getJavaName());
			out.print(" ");
			out.print(toComments(c.getRemarks()) );
			out.println("");
			out.print("		");
			} 		out.println("	");
			out.println("		*/");
			out.print("		public ");
			out.print(table.getJavaName());
			out.print(" selectBy");
			out.print(m);
			out.print("(");
			for(MetaColumn k:index.getColumns()){		out.print(k==index.getColumns().get(0)?"":", ");
			out.print(k.getJavaType() );
			out.print(" ");
			out.print(k.getJavaName());
			}		out.println(")){	");
			out.println("			Criteria c=criteria();");
			out.print("			");
			for(MetaColumn k:index.getColumns()){ 		out.println("");
			out.print("			c.");
			out.print(k.getJavaName());
			out.print(".eq(");
			out.print(k.getJavaName());
			out.println(");");
			out.print("			");
			} 		out.println("			 ");
			out.println("			 ");
			out.println("			return super.selectOneByExample(c.example);");
			out.println("		}			 ");
			out.println("		");
			out.println("		 ");
			out.print("		");
			
		if(index.getColumns().size()==1){
			MetaColumn k=index.getColumns().get(0);
				out.println("");
			out.println("		/**");
			out.print("		* List result to Map, The map key is unique-key: ");
			out.print(k.getName() );
			out.println(" ");
			out.println("		*/");
			out.print("		public Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> selectToMapWith");
			out.print(firstUpper(k.getJavaName()));
			out.println("(String whereStatement,Object ... args){");
			out.print("			List<");
			out.print(table.getJavaName());
			out.println("> list=super.select(whereStatement,args);");
			out.println("			");
			out.print("			Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> m=new LinkedHashMap<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println(">();");
			out.print("			for(");
			out.print(table.getJavaName());
			out.println(" x:list){");
			out.print("				m.put(x.");
			out.print(k.getJavaNameGet());
			out.println("(),x);");
			out.println("			}");
			out.println("			return m;");
			out.println("		}");
			out.println("		");
			out.println("		/**");
			out.print("		* List result to Map, The map key is unique-key: ");
			out.print(k.getJavaName());
			out.println(" ");
			out.println("		*/");
			out.print("		public Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> selectByExampleToMapWith");
			out.print(firstUpper(k.getJavaName()));
			out.println("(Example example){");
			out.print("			List<");
			out.print(table.getJavaName());
			out.println("> list=super.selectByExample(example);");
			out.println("			");
			out.print("			Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> m=new LinkedHashMap<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println(">();");
			out.print("			for(");
			out.print(table.getJavaName());
			out.println(" x:list){");
			out.print("				m.put(x.");
			out.print(k.getJavaNameGet());
			out.println("(),x);");
			out.println("			}");
			out.println("			return m;");
			out.println("		}");
			out.print("		");
			}		out.println("");
			out.print("		");
			}		out.println("");
			out.print("		");
			}		out.println("");
			out.println("			");
			out.print("		");
			
		if(table.getKeyColumns().size()==1){
			MetaColumn k=table.getKeyColumns().get(0);
				out.println("		");
			out.println("		/**");
			out.print("		* List result to Map, The map key is primary-key:  ");
			out.print(k.getJavaName());
			out.println("");
			out.println("		*/");
			out.print("		public Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println("> selectToMap(String whereStatement,Object ... args){");
			out.print("			List<");
			out.print(table.getJavaName());
			out.println("> list=super.select(whereStatement,args);");
			out.println("			");
			out.print("			Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> m=new LinkedHashMap<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println(">();");
			out.print("			for(");
			out.print(table.getJavaName());
			out.println(" x:list){");
			out.print("				m.put(x.");
			out.print(k.getJavaNameGet());
			out.println("(),x);");
			out.println("			}");
			out.println("			return m;");
			out.println("		}");
			out.println("	");
			out.println("		/**");
			out.print("		* List result to Map, The map key is primary-key: ");
			out.print(k.getJavaName());
			out.println(" ");
			out.println("		*/");
			out.print("		public Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println("> selectByExampleToMap(Example example){");
			out.print("			List<");
			out.print(table.getJavaName());
			out.println("> list=super.selectByExample(example);");
			out.println("			");
			out.print("			Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> m=new LinkedHashMap<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println(">();");
			out.print("			for(");
			out.print(table.getJavaName());
			out.println(" x:list){");
			out.print("				m.put(x.");
			out.print(k.getJavaNameGet());
			out.println("(),x);");
			out.println("			}");
			out.println("			return m;");
			out.println("		}");
			out.print("		");
			}		out.println("");
			out.println("	}");
			out.println("	 ");
			out.println("		");
			out.print("	public static class Example extends com.tsc9526.monalisa.core.query.criteria.Example<Criteria,");
			out.print(table.getJavaName());
			out.println(">{");
			out.println("		public Example(){}");
			out.println("		 ");
			out.println("		protected Criteria createInternal(){");
			out.println("			Criteria x= new Criteria(this);");
			out.println("			");
			out.println("			@SuppressWarnings(\"rawtypes\")");
			out.print("			Class clazz=ClassHelper.findClassWithAnnotation(");
			out.print(table.getJavaName());
			out.println(".class,DB.class);	  			");
			out.println("			com.tsc9526.monalisa.core.query.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));");
			out.println("			");
			out.println("			return x;");
			out.println("		}");
			out.println("		");
			out.print("		");
			
		if(table.getKeyColumns().size()==1){
			MetaColumn k=table.getKeyColumns().get(0);
				out.println("");
			out.println("		/**");
			out.print("		* List result to Map, The map key is primary-key: ");
			out.print(k.getJavaName());
			out.println(" ");
			out.println("		*/");
			out.print("		public Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println("> selectToMap(){			");
			out.print("			List<");
			out.print(table.getJavaName());
			out.println("> list=SELECT().selectByExample(this);");
			out.println("			");
			out.print("			Map<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.print("> m=new LinkedHashMap<");
			out.print(k.getJavaType());
			out.print(",");
			out.print(table.getJavaName());
			out.println(">();");
			out.print("			for(");
			out.print(table.getJavaName());
			out.println(" x:list){");
			out.print("				m.put(x.");
			out.print(k.getJavaNameGet());
			out.println("(),x);");
			out.println("			}");
			out.println("			return m;");
			out.println("		}");
			out.print("		");
			}		out.println("");
			out.println("		");
			out.println("	}");
			out.println("	");
			out.println("	public static class Criteria extends com.tsc9526.monalisa.core.query.criteria.Criteria<Criteria>{");
			out.println("		");
			out.println("		private Example example;");
			out.println("		");
			out.println("		private Criteria(Example example){");
			out.println("			this.example=example;");
			out.println("		}");
			out.println("		");
			out.println("		/**");
			out.println("		 * Create Select for example");
			out.println("		 */");
			out.println("		public Select.SelectForExample SELECT(){");
			out.print("			return ");
			out.print(table.getJavaName());
			out.println(".SELECT().selectForExample(this.example);");
			out.println("		}");
			out.println("		");
			out.println("		/**");
			out.println("		* Update records with this example");
			out.println("		*/");
			out.print("		public int update(");
			out.print(table.getJavaName());
			out.println(" m){			 ");
			out.println("			return UPDATE(m).updateByExample(this.example);");
			out.println("		}");
			out.println("				");
			out.println("		/**");
			out.println("		* Delete records with this example");
			out.println("		*/		");
			out.println("		public int delete(){");
			out.println("			return DELETE().deleteByExample(this.example);");
			out.println("		}");
			out.println("		");
			out.print("		");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("		");
			out.print(getComments(table, f, "		"));
			out.println("");
			out.print("		");
			if(f.getJavaType().equals("Integer")){ 		out.println("");
			out.print("		public com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria> ");
			out.print(f.getJavaName());
			out.print(" = new com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria>(\"");
			out.print(f.getName());
			out.println("\", this);");
			out.print("		");
			}else if(f.getJavaType().equals("Short")){ 		out.println("");
			out.print("		public com.tsc9526.monalisa.core.query.criteria.Field.FieldShort<Criteria> ");
			out.print(f.getJavaName());
			out.print(" = new com.tsc9526.monalisa.core.query.criteria.Field.FieldShort<Criteria>(\"");
			out.print(f.getName());
			out.println("\", this);");
			out.print("		");
			}else if(f.getJavaType().equals("Long")){ 		out.println("");
			out.print("		public com.tsc9526.monalisa.core.query.criteria.Field.FieldLong<Criteria> ");
			out.print(f.getJavaName());
			out.print(" = new com.tsc9526.monalisa.core.query.criteria.Field.FieldLong<Criteria>(\"");
			out.print(f.getName());
			out.println("\", this); ");
			out.print("		");
			}else if(f.getJavaType().equals("String")){ 		out.println("");
			out.print("		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> ");
			out.print(f.getJavaName());
			out.print(" = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>(\"");
			out.print(f.getName());
			out.println("\", this);");
			out.print("		");
			}else{ 		out.println("");
			out.print("		public com.tsc9526.monalisa.core.query.criteria.Field<");
			out.print(f.getJavaType());
			out.print(",Criteria> ");
			out.print(f.getJavaName());
			out.print(" = new com.tsc9526.monalisa.core.query.criteria.Field<");
			out.print(f.getJavaType());
			out.print(",Criteria>(\"");
			out.print(f.getName());
			out.print("\", this, ");
			out.print(f.getJdbcType());
			out.println(");		 ");
			out.print("		");
			} 		out.println("		");
			out.print("		");
			}		out.println("");
			out.println("	}");
			out.println("	 ");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			
		String em=f.getCode("enum");
		if(em!=null && em.indexOf("{")>=0){
			out.println("		");
			out.print("			public static enum ");
			out.print(em);
			out.println("");
			out.print("	");
			 
		}
			out.println("	 ");
			out.print("	");
			}		out.println("");
			out.println("	 ");
			out.println("	public static class M{");
			out.print("		public final static String TABLE =\"");
			out.print(table.getName());
			out.println("\" ;");
			out.println("		");
			out.print("		");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("		");
			out.print(getComments(table, f, "		"));
			out.println("");
			out.print("		public final static String  ");
			out.print(f.getJavaName());
			out.print("         = \"");
			out.print(f.getName());
			out.println("\" ;");
			out.println("		");
			out.print("		public final static String  ");
			out.print(f.getJavaName());
			out.print("$name    = \"");
			out.print(f.getName());
			out.println("\" ;");
			out.print("		public final static boolean ");
			out.print(f.getJavaName());
			out.print("$key     = ");
			out.print(f.isKey()?"true":"false");
			out.println(";");
			out.print("		public final static int     ");
			out.print(f.getJavaName());
			out.print("$length  = ");
			out.print(f.getLength());
			out.println(";");
			out.print("		public final static String  ");
			out.print(f.getJavaName());
			out.print("$value   = \"");
			out.print(f.getValue()==null?"NULL":toJavaString(f.getValue()));
			out.println("\" ;");
			out.print("		public final static String  ");
			out.print(f.getJavaName());
			out.print("$remarks = \"");
			out.print(toJavaString(f.getRemarks()));
			out.println("\" ;");
			out.print("		public final static boolean ");
			out.print(f.getJavaName());
			out.print("$auto    = ");
			out.print(f.isAuto()?"true":"false");
			out.println(" ;");
			out.print("		public final static boolean ");
			out.print(f.getJavaName());
			out.print("$notnull = ");
			out.print(f.isNotnull()?"true":"false");
			out.println(";");
			out.println("		");
			out.print("		");
			}		out.println("		 ");
			out.println("	}");
			out.println("}");
			out.println("");
			out.println("");
		out.flush();

		}
}