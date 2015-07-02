package ${table.javaPackage};

<#include "functions.ftl">

<#list imports as i>
import ${i};
</#list>
<#if table.keyColumns?size = 1 >
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
</#if>

/**
 * Created by monalisa at ${.now}
 */ 
@Table(name="${table.name}")
public class ${table.javaName} extends ${modelClass}<${table.javaName}> implements ${dbi}{
	private static final long serialVersionUID = ${table.serialID?c}L;
	
	public static final Select SELECT =new Select(new ${table.javaName}());	
	 
	 
	public ${table.javaName}(){
		super();
		
		<#if table.partition??  && table.partition.args??>
		this.partition=new ${table.partition.clazz}();		
		  <#assign args=""/>
		  <#list table.partition.args as x>		     
		    <#assign args='${args}, "${x}"'/>			  
		  </#list>		 
		this.partition.setup("${table.partition.tablePrefix}"${args});		
		<#elseif table.partition??>
		this.partition=new ${table.partition.clazz}();
		this.partition.setup("${table.partition.tablePrefix}");
		</#if>
			
	}
	
	<#if table.keyColumns?size gt 0 >
	/**
	 * Constructor use primary keys.
	 *
	 <#list table.keyColumns as k>
	 * @param ${k.javaName} ${k.remarks}
	 </#list>	 
	 */
	public ${table.javaName}(<#list table.keyColumns as k>${k.javaType} ${k.javaName}<#if k_has_next=true>, </#if></#list>){
		<#list table.keyColumns as k>
		this.${k.javaName} = ${k.javaName};
		</#list>
	}
	
	/**
	* Load by primary keys
	*/
	public ${table.javaName} load(){
		Query query=getDialect().load(this);
		query.use(db());
		
		${table.javaName} r=query.getResult();
		return r;
	}
	</#if>
	
	<#list table.columns as f>
	<@comments table=table c=f align="	"/> 
	<#if f.code.annotation??>
	<#list c.code.annotation?split("\n") as a>
	${a}
	</#list>
	<#if>
	private ${f.javaType} ${f.javaName};	
	
	</#list>
	
	
	<#list table.columns as f>   
	<@comments table=table c=f align="	"/> 
	public ${table.javaName} ${f.javaNameSet}(${f.javaType} ${f.javaName}){
		<#if f.code.set??>
		${f.code.set}
		<#else{
		this.${f.javaName} = ${f.javaName};		
		}
		return this;
	}
		
	</#list>
	
	
	<#list table.columns as f>   
	<@comments table=table c=f align="	"/> 
	public ${f.javaType} ${f.javaNameGet}(){
		<#if f.code.get??>
		${f.code.get}
		<#elseif f.code.value??>
		if(this.${f.javaName}==null){
			return ${f.code.value};		
		}
		return this.${f.javaName};		 		
		</#if>	
	}
		
	</#list>
	
	
	public static class Select extends com.tsc9526.monalisa.core.query.dao.Select<${table.javaName}>{		
		public Select(${table.javaName} x){
			super(x);
		}
		
		<#if table.keyColumns?size gt 0 >
		public ${table.javaName} selectByPrimaryKey(<#list table.keyColumns as k>${k.javaType} ${k.javaName}<#if k_has_next=true>, </#if> </#list>){
			${table.javaName} model = new ${table.javaName}();
			<#list table.keyColumns as k>
			model.${k.javaName} = ${k.javaName};
			</#list>
				 			 
			Query query=model.getDialect().load(model);
			query.use(model.db());
			return (${table.javaName})query.getResult();					
		}				 
		</#if>
		
		<#if table.keyColumns?size = 1 >
		<#assign k=table.keyColumns[0]>
		/**
		* List result to Map, The map key is primary-key: ${k.name} 
		*/
		public Map<${k.javaType},${table.javaName}> selectToMap(String whereStatement,Object ... args){
			List<${table.javaName}> list=super.select(whereStatement,args);
			
			Map<${k.javaType},${table.javaName}> m=new LinkedHashMap<${k.javaType},${table.javaName}>();
			for(${table.javaName} x:list){
				m.put(x.${k.javaNameGet}(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: ${k.name} 
		*/
		public Map<${k.javaType},${table.javaName}> selectByExampleToMap(Example example){
			List<${table.javaName}> list=super.selectByExample(example);
			
			Map<${k.javaType},${table.javaName}> m=new LinkedHashMap<${k.javaType},${table.javaName}>();
			for(${table.javaName} x:list){
				m.put(x.${k.javaNameGet}(),x);
			}
			return m;
		}
		</#if>
	}
	
	public static Criteria createCriteria(){
		return new Example(new ${table.javaName}()).createCriteria();
	}
	
		
	public static class Example extends com.tsc9526.monalisa.core.query.criteria.Example<Criteria,${table.javaName}>{
		public Example(){}
		public Example(${table.javaName} model) {
			super(model);
		}
		
		protected Criteria createInternal(){
			Criteria x= new Criteria(this);
			
			Class<?> clazz=ClassHelper.findClassWithAnnotation(${table.javaName}.class,DB.class);
			DBConfig db=dsm.getDBConfig(clazz);
			  
			x.getQuery().use(db);
			return x;
		}
		
		<#if table.keyColumns?size = 1 >
		<#assign k=table.keyColumns[0]>
		/**
		* List result to Map, The map key is primary-key: ${k.name} 
		*/
		public Map<${k.javaType},${table.javaName}> selectToMap(){
			List<${table.javaName}> list=super.select();
			
			Map<${k.javaType},${table.javaName}> m=new LinkedHashMap<${k.javaType},${table.javaName}>();
			for(${table.javaName} x:list){
				m.put(x.${k.javaNameGet}(),x);
			}
			return m;
		}
		</#if>
		
	}
	
	public static class Criteria extends com.tsc9526.monalisa.core.query.criteria.Criteria{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		public Example getExample(){
			return this.example;
		}
		
		<#list table.columns as f>
		<@comments table=table c=f align="		"/>
		<#if f.javaType ="Integer">
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria> ${f.javaName} = new com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria>("${f.name}", this);
		<#elseif f.javaType= "Short">
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldShort<Criteria> ${f.javaName} = new com.tsc9526.monalisa.core.query.criteria.Field.FieldShort<Criteria>("${f.name}", this);
		<#elseif f.javaType= "Long">
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldLong<Criteria> ${f.javaName} = new com.tsc9526.monalisa.core.query.criteria.Field.FieldLong<Criteria>("${f.name}", this); 
		<#elseif f.javaType= "String">
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> ${f.javaName} = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("${f.name}", this);
		<#else>
		public com.tsc9526.monalisa.core.query.criteria.Field<${f.javaType},Criteria> ${f.javaName} = new com.tsc9526.monalisa.core.query.criteria.Field<${f.javaType},Criteria>("${f.name}", this);
		</#if>		
		
		</#list>
		 
	}
	 
	
	public static class Metadata{
		public final static String table ="${table.name}" ;
		
		<#list table.columns as f>
		public final static String  ${f.javaName}$name    = "${f.name}" ;
		public final static boolean ${f.javaName}$key     = ${f.key?string("true","false")} ;
		public final static int     ${f.javaName}$length  = ${f.length?c} ;
		public final static String  ${f.javaName}$value   = "<#if f.value??>${f.value}<#else>NULL</#if>" ;
		public final static String  ${f.javaName}$remarks = "${f.remarks!?replace('"','\\"')?replace('\r','\\r')?replace('\n','\\n')}" ;
		public final static boolean ${f.javaName}$auto    = ${f.auto?string("true","false")} ;
		public final static boolean ${f.javaName}$notnull = ${f.notnull?string("true","false")} ;
		
		</#list>		 
	}
}
