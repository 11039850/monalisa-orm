<#-- 字段, GET/SET 方法的注释 -->
<#macro comments table c align> 
<#if c.name?? && c.name!='' && c.table??>
${align!}/**
${align!}* @Column 
${align!}* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> ${c.table.name}&nbsp;<B>name:</B> ${c.name} <#compress> 
	<#assign x=[] />	
	<#if c.key>     <#assign x=x+['KEY'] />     </#if>	
	<#if c.auto>    <#assign x=x+['AUTO'] />    </#if>	
	<#if c.notnull> <#assign x=x+['NOTNULL'] /> </#if> 
	<#if x?size gt 0 > [<#list x as k><font color=red>${k}</font><#if k_has_next=true>|</#if></#list>] </#if><br> </#compress>   
${align!}<#if c.length gt 0 >* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> ${c.length}	</#if>	<#if c.value??> 	&nbsp;<B>value:</B> ${c.value?html} <br> </#if>			
${align!}<#if c.remarks?? >* <li><B>remarks:</B> ${c.remarks?html?replace('*/','**')}	</#if>		 
${align!}*/
	<#assign f=c.table.javaName+".Metadata." />
	<#if c.table.javaPackage == table.javaPackage >
		<#assign f="Metadata." />
	</#if>	
	<#assign names=["name","key","auto","notnull","length","value","remarks"] />	
${align!}@Column(table=${f}TABLE, jdbcType=${c.jdbcType}, <#list names as n><#assign colname=c.nameToJava() /><#assign p=colname?index_of('$') /><#if p gt 0> <#assign colname=colname?substring(p+1) /> </#if> ${n}=${f}${colname}$${n} <#if n_has_next=true>, </#if> </#list>  )
</#if>
</#macro> 