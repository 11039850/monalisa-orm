package test.com.tsc9526.monalisa.core.sql;
import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable1;
import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable2;
import com.tsc9526.monalisa.core.annotation.Column;
/**
 * Created by monalisa at 2016-3-17 19:13:05
 * 
 * @see 
 */
public class DS0001 implements java.io.Serializable{
	private static final long serialVersionUID = 2048901119452L;	
	final static String  FINGERPRINT = "";
	
	 
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.id$name ,   key=TestTable1.M.id$key ,   auto=TestTable1.M.id$auto ,   notnull=TestTable1.M.id$notnull ,   length=TestTable1.M.id$length ,   value=TestTable1.M.id$value ,   remarks=TestTable1.M.id$remarks    )
	private Integer TestTable1$id;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.name$name ,   key=TestTable1.M.name$key ,   auto=TestTable1.M.name$auto ,   notnull=TestTable1.M.name$notnull ,   length=TestTable1.M.name$length ,   value=TestTable1.M.name$value ,   remarks=TestTable1.M.name$remarks    )
	private String TestTable1$name;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.title$name ,   key=TestTable1.M.title$key ,   auto=TestTable1.M.title$auto ,   notnull=TestTable1.M.title$notnull ,   length=TestTable1.M.title$length ,   value=TestTable1.M.title$value ,   remarks=TestTable1.M.title$remarks    )
	private String TestTable1$title;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.enumIntA$name ,   key=TestTable1.M.enumIntA$key ,   auto=TestTable1.M.enumIntA$auto ,   notnull=TestTable1.M.enumIntA$notnull ,   length=TestTable1.M.enumIntA$length ,   value=TestTable1.M.enumIntA$value ,   remarks=TestTable1.M.enumIntA$remarks    )
	private TestTable1.EnumIntA TestTable1$enumIntA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.enumStringA$name ,   key=TestTable1.M.enumStringA$key ,   auto=TestTable1.M.enumStringA$auto ,   notnull=TestTable1.M.enumStringA$notnull ,   length=TestTable1.M.enumStringA$length ,   value=TestTable1.M.enumStringA$value ,   remarks=TestTable1.M.enumStringA$remarks    )
	private TestTable1.EnumStringA TestTable1$enumStringA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.tsA$name ,   key=TestTable1.M.tsA$key ,   auto=TestTable1.M.tsA$auto ,   notnull=TestTable1.M.tsA$notnull ,   length=TestTable1.M.tsA$length ,   value=TestTable1.M.tsA$value ,   remarks=TestTable1.M.tsA$remarks    )
	private java.util.Date TestTable1$tsA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.createTime$name ,   key=TestTable1.M.createTime$key ,   auto=TestTable1.M.createTime$auto ,   notnull=TestTable1.M.createTime$notnull ,   length=TestTable1.M.createTime$length ,   value=TestTable1.M.createTime$value ,   remarks=TestTable1.M.createTime$remarks    )
	private java.util.Date TestTable1$createTime;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.createBy$name ,   key=TestTable1.M.createBy$key ,   auto=TestTable1.M.createBy$auto ,   notnull=TestTable1.M.createBy$notnull ,   length=TestTable1.M.createBy$length ,   value=TestTable1.M.createBy$value ,   remarks=TestTable1.M.createBy$remarks    )
	private String TestTable1$createBy;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.updateTime$name ,   key=TestTable1.M.updateTime$key ,   auto=TestTable1.M.updateTime$auto ,   notnull=TestTable1.M.updateTime$notnull ,   length=TestTable1.M.updateTime$length ,   value=TestTable1.M.updateTime$value ,   remarks=TestTable1.M.updateTime$remarks    )
	private java.util.Date TestTable1$updateTime;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.updateBy$name ,   key=TestTable1.M.updateBy$key ,   auto=TestTable1.M.updateBy$auto ,   notnull=TestTable1.M.updateBy$notnull ,   length=TestTable1.M.updateBy$length ,   value=TestTable1.M.updateBy$value ,   remarks=TestTable1.M.updateBy$remarks    )
	private String TestTable1$updateBy;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.id$name ,   key=TestTable2.M.id$key ,   auto=TestTable2.M.id$auto ,   notnull=TestTable2.M.id$notnull ,   length=TestTable2.M.id$length ,   value=TestTable2.M.id$value ,   remarks=TestTable2.M.id$remarks    )
	private Integer TestTable2$id;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.name$name ,   key=TestTable2.M.name$key ,   auto=TestTable2.M.name$auto ,   notnull=TestTable2.M.name$notnull ,   length=TestTable2.M.name$length ,   value=TestTable2.M.name$value ,   remarks=TestTable2.M.name$remarks    )
	private String TestTable2$name;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.title$name ,   key=TestTable2.M.title$key ,   auto=TestTable2.M.title$auto ,   notnull=TestTable2.M.title$notnull ,   length=TestTable2.M.title$length ,   value=TestTable2.M.title$value ,   remarks=TestTable2.M.title$remarks    )
	private String TestTable2$title;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.enumIntA$name ,   key=TestTable2.M.enumIntA$key ,   auto=TestTable2.M.enumIntA$auto ,   notnull=TestTable2.M.enumIntA$notnull ,   length=TestTable2.M.enumIntA$length ,   value=TestTable2.M.enumIntA$value ,   remarks=TestTable2.M.enumIntA$remarks    )
	private TestTable2.EnumIntA TestTable2$enumIntA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.enumStringA$name ,   key=TestTable2.M.enumStringA$key ,   auto=TestTable2.M.enumStringA$auto ,   notnull=TestTable2.M.enumStringA$notnull ,   length=TestTable2.M.enumStringA$length ,   value=TestTable2.M.enumStringA$value ,   remarks=TestTable2.M.enumStringA$remarks    )
	private TestTable2.EnumStringA TestTable2$enumStringA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.tsA$name ,   key=TestTable2.M.tsA$key ,   auto=TestTable2.M.tsA$auto ,   notnull=TestTable2.M.tsA$notnull ,   length=TestTable2.M.tsA$length ,   value=TestTable2.M.tsA$value ,   remarks=TestTable2.M.tsA$remarks    )
	private java.util.Date TestTable2$tsA;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.createTime$name ,   key=TestTable2.M.createTime$key ,   auto=TestTable2.M.createTime$auto ,   notnull=TestTable2.M.createTime$notnull ,   length=TestTable2.M.createTime$length ,   value=TestTable2.M.createTime$value ,   remarks=TestTable2.M.createTime$remarks    )
	private java.util.Date TestTable2$createTime;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.createBy$name ,   key=TestTable2.M.createBy$key ,   auto=TestTable2.M.createBy$auto ,   notnull=TestTable2.M.createBy$notnull ,   length=TestTable2.M.createBy$length ,   value=TestTable2.M.createBy$value ,   remarks=TestTable2.M.createBy$remarks    )
	private String TestTable2$createBy;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.updateTime$name ,   key=TestTable2.M.updateTime$key ,   auto=TestTable2.M.updateTime$auto ,   notnull=TestTable2.M.updateTime$notnull ,   length=TestTable2.M.updateTime$length ,   value=TestTable2.M.updateTime$value ,   remarks=TestTable2.M.updateTime$remarks    )
	private java.util.Date TestTable2$updateTime;	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.updateBy$name ,   key=TestTable2.M.updateBy$key ,   auto=TestTable2.M.updateBy$auto ,   notnull=TestTable2.M.updateBy$notnull ,   length=TestTable2.M.updateBy$length ,   value=TestTable2.M.updateBy$value ,   remarks=TestTable2.M.updateBy$remarks    )
	private String TestTable2$updateBy;	
	
	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.id$name ,   key=TestTable1.M.id$key ,   auto=TestTable1.M.id$auto ,   notnull=TestTable1.M.id$notnull ,   length=TestTable1.M.id$length ,   value=TestTable1.M.id$value ,   remarks=TestTable1.M.id$remarks    )
	public DS0001 setTestTable1$id(Integer TestTable1$id){
		this.TestTable1$id = TestTable1$id;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.name$name ,   key=TestTable1.M.name$key ,   auto=TestTable1.M.name$auto ,   notnull=TestTable1.M.name$notnull ,   length=TestTable1.M.name$length ,   value=TestTable1.M.name$value ,   remarks=TestTable1.M.name$remarks    )
	public DS0001 setTestTable1$name(String TestTable1$name){
		this.TestTable1$name = TestTable1$name;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.title$name ,   key=TestTable1.M.title$key ,   auto=TestTable1.M.title$auto ,   notnull=TestTable1.M.title$notnull ,   length=TestTable1.M.title$length ,   value=TestTable1.M.title$value ,   remarks=TestTable1.M.title$remarks    )
	public DS0001 setTestTable1$title(String TestTable1$title){
		this.TestTable1$title = TestTable1$title;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.enumIntA$name ,   key=TestTable1.M.enumIntA$key ,   auto=TestTable1.M.enumIntA$auto ,   notnull=TestTable1.M.enumIntA$notnull ,   length=TestTable1.M.enumIntA$length ,   value=TestTable1.M.enumIntA$value ,   remarks=TestTable1.M.enumIntA$remarks    )
	public DS0001 setTestTable1$enumIntA(TestTable1.EnumIntA TestTable1$enumIntA){
		this.TestTable1$enumIntA = TestTable1$enumIntA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.enumStringA$name ,   key=TestTable1.M.enumStringA$key ,   auto=TestTable1.M.enumStringA$auto ,   notnull=TestTable1.M.enumStringA$notnull ,   length=TestTable1.M.enumStringA$length ,   value=TestTable1.M.enumStringA$value ,   remarks=TestTable1.M.enumStringA$remarks    )
	public DS0001 setTestTable1$enumStringA(TestTable1.EnumStringA TestTable1$enumStringA){
		this.TestTable1$enumStringA = TestTable1$enumStringA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.tsA$name ,   key=TestTable1.M.tsA$key ,   auto=TestTable1.M.tsA$auto ,   notnull=TestTable1.M.tsA$notnull ,   length=TestTable1.M.tsA$length ,   value=TestTable1.M.tsA$value ,   remarks=TestTable1.M.tsA$remarks    )
	public DS0001 setTestTable1$tsA(java.util.Date TestTable1$tsA){
		this.TestTable1$tsA = TestTable1$tsA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.createTime$name ,   key=TestTable1.M.createTime$key ,   auto=TestTable1.M.createTime$auto ,   notnull=TestTable1.M.createTime$notnull ,   length=TestTable1.M.createTime$length ,   value=TestTable1.M.createTime$value ,   remarks=TestTable1.M.createTime$remarks    )
	public DS0001 setTestTable1$createTime(java.util.Date TestTable1$createTime){
		this.TestTable1$createTime = TestTable1$createTime;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.createBy$name ,   key=TestTable1.M.createBy$key ,   auto=TestTable1.M.createBy$auto ,   notnull=TestTable1.M.createBy$notnull ,   length=TestTable1.M.createBy$length ,   value=TestTable1.M.createBy$value ,   remarks=TestTable1.M.createBy$remarks    )
	public DS0001 setTestTable1$createBy(String TestTable1$createBy){
		this.TestTable1$createBy = TestTable1$createBy;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.updateTime$name ,   key=TestTable1.M.updateTime$key ,   auto=TestTable1.M.updateTime$auto ,   notnull=TestTable1.M.updateTime$notnull ,   length=TestTable1.M.updateTime$length ,   value=TestTable1.M.updateTime$value ,   remarks=TestTable1.M.updateTime$remarks    )
	public DS0001 setTestTable1$updateTime(java.util.Date TestTable1$updateTime){
		this.TestTable1$updateTime = TestTable1$updateTime;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.updateBy$name ,   key=TestTable1.M.updateBy$key ,   auto=TestTable1.M.updateBy$auto ,   notnull=TestTable1.M.updateBy$notnull ,   length=TestTable1.M.updateBy$length ,   value=TestTable1.M.updateBy$value ,   remarks=TestTable1.M.updateBy$remarks    )
	public DS0001 setTestTable1$updateBy(String TestTable1$updateBy){
		this.TestTable1$updateBy = TestTable1$updateBy;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.id$name ,   key=TestTable2.M.id$key ,   auto=TestTable2.M.id$auto ,   notnull=TestTable2.M.id$notnull ,   length=TestTable2.M.id$length ,   value=TestTable2.M.id$value ,   remarks=TestTable2.M.id$remarks    )
	public DS0001 setTestTable2$id(Integer TestTable2$id){
		this.TestTable2$id = TestTable2$id;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.name$name ,   key=TestTable2.M.name$key ,   auto=TestTable2.M.name$auto ,   notnull=TestTable2.M.name$notnull ,   length=TestTable2.M.name$length ,   value=TestTable2.M.name$value ,   remarks=TestTable2.M.name$remarks    )
	public DS0001 setTestTable2$name(String TestTable2$name){
		this.TestTable2$name = TestTable2$name;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.title$name ,   key=TestTable2.M.title$key ,   auto=TestTable2.M.title$auto ,   notnull=TestTable2.M.title$notnull ,   length=TestTable2.M.title$length ,   value=TestTable2.M.title$value ,   remarks=TestTable2.M.title$remarks    )
	public DS0001 setTestTable2$title(String TestTable2$title){
		this.TestTable2$title = TestTable2$title;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.enumIntA$name ,   key=TestTable2.M.enumIntA$key ,   auto=TestTable2.M.enumIntA$auto ,   notnull=TestTable2.M.enumIntA$notnull ,   length=TestTable2.M.enumIntA$length ,   value=TestTable2.M.enumIntA$value ,   remarks=TestTable2.M.enumIntA$remarks    )
	public DS0001 setTestTable2$enumIntA(TestTable2.EnumIntA TestTable2$enumIntA){
		this.TestTable2$enumIntA = TestTable2$enumIntA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.enumStringA$name ,   key=TestTable2.M.enumStringA$key ,   auto=TestTable2.M.enumStringA$auto ,   notnull=TestTable2.M.enumStringA$notnull ,   length=TestTable2.M.enumStringA$length ,   value=TestTable2.M.enumStringA$value ,   remarks=TestTable2.M.enumStringA$remarks    )
	public DS0001 setTestTable2$enumStringA(TestTable2.EnumStringA TestTable2$enumStringA){
		this.TestTable2$enumStringA = TestTable2$enumStringA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.tsA$name ,   key=TestTable2.M.tsA$key ,   auto=TestTable2.M.tsA$auto ,   notnull=TestTable2.M.tsA$notnull ,   length=TestTable2.M.tsA$length ,   value=TestTable2.M.tsA$value ,   remarks=TestTable2.M.tsA$remarks    )
	public DS0001 setTestTable2$tsA(java.util.Date TestTable2$tsA){
		this.TestTable2$tsA = TestTable2$tsA;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.createTime$name ,   key=TestTable2.M.createTime$key ,   auto=TestTable2.M.createTime$auto ,   notnull=TestTable2.M.createTime$notnull ,   length=TestTable2.M.createTime$length ,   value=TestTable2.M.createTime$value ,   remarks=TestTable2.M.createTime$remarks    )
	public DS0001 setTestTable2$createTime(java.util.Date TestTable2$createTime){
		this.TestTable2$createTime = TestTable2$createTime;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.createBy$name ,   key=TestTable2.M.createBy$key ,   auto=TestTable2.M.createBy$auto ,   notnull=TestTable2.M.createBy$notnull ,   length=TestTable2.M.createBy$length ,   value=TestTable2.M.createBy$value ,   remarks=TestTable2.M.createBy$remarks    )
	public DS0001 setTestTable2$createBy(String TestTable2$createBy){
		this.TestTable2$createBy = TestTable2$createBy;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.updateTime$name ,   key=TestTable2.M.updateTime$key ,   auto=TestTable2.M.updateTime$auto ,   notnull=TestTable2.M.updateTime$notnull ,   length=TestTable2.M.updateTime$length ,   value=TestTable2.M.updateTime$value ,   remarks=TestTable2.M.updateTime$remarks    )
	public DS0001 setTestTable2$updateTime(java.util.Date TestTable2$updateTime){
		this.TestTable2$updateTime = TestTable2$updateTime;
		return this;
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.updateBy$name ,   key=TestTable2.M.updateBy$key ,   auto=TestTable2.M.updateBy$auto ,   notnull=TestTable2.M.updateBy$notnull ,   length=TestTable2.M.updateBy$length ,   value=TestTable2.M.updateBy$value ,   remarks=TestTable2.M.updateBy$remarks    )
	public DS0001 setTestTable2$updateBy(String TestTable2$updateBy){
		this.TestTable2$updateBy = TestTable2$updateBy;
		return this;
	}
		
	
	
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.id$name ,   key=TestTable1.M.id$key ,   auto=TestTable1.M.id$auto ,   notnull=TestTable1.M.id$notnull ,   length=TestTable1.M.id$length ,   value=TestTable1.M.id$value ,   remarks=TestTable1.M.id$remarks    )
	public Integer getTestTable1$id(){
		return this.TestTable1$id;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.name$name ,   key=TestTable1.M.name$key ,   auto=TestTable1.M.name$auto ,   notnull=TestTable1.M.name$notnull ,   length=TestTable1.M.name$length ,   value=TestTable1.M.name$value ,   remarks=TestTable1.M.name$remarks    )
	public String getTestTable1$name(){
		return this.TestTable1$name;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.title$name ,   key=TestTable1.M.title$key ,   auto=TestTable1.M.title$auto ,   notnull=TestTable1.M.title$notnull ,   length=TestTable1.M.title$length ,   value=TestTable1.M.title$value ,   remarks=TestTable1.M.title$remarks    )
	public String getTestTable1$title(){
		return this.TestTable1$title;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4,  name=TestTable1.M.enumIntA$name ,   key=TestTable1.M.enumIntA$key ,   auto=TestTable1.M.enumIntA$auto ,   notnull=TestTable1.M.enumIntA$notnull ,   length=TestTable1.M.enumIntA$length ,   value=TestTable1.M.enumIntA$value ,   remarks=TestTable1.M.enumIntA$remarks    )
	public TestTable1.EnumIntA getTestTable1$enumIntA(){
		return this.TestTable1$enumIntA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.enumStringA$name ,   key=TestTable1.M.enumStringA$key ,   auto=TestTable1.M.enumStringA$auto ,   notnull=TestTable1.M.enumStringA$notnull ,   length=TestTable1.M.enumStringA$length ,   value=TestTable1.M.enumStringA$value ,   remarks=TestTable1.M.enumStringA$remarks    )
	public TestTable1.EnumStringA getTestTable1$enumStringA(){
		return this.TestTable1$enumStringA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.tsA$name ,   key=TestTable1.M.tsA$key ,   auto=TestTable1.M.tsA$auto ,   notnull=TestTable1.M.tsA$notnull ,   length=TestTable1.M.tsA$length ,   value=TestTable1.M.tsA$value ,   remarks=TestTable1.M.tsA$remarks    )
	public java.util.Date getTestTable1$tsA(){
		return this.TestTable1$tsA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.createTime$name ,   key=TestTable1.M.createTime$key ,   auto=TestTable1.M.createTime$auto ,   notnull=TestTable1.M.createTime$notnull ,   length=TestTable1.M.createTime$length ,   value=TestTable1.M.createTime$value ,   remarks=TestTable1.M.createTime$remarks    )
	public java.util.Date getTestTable1$createTime(){
		return this.TestTable1$createTime;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.createBy$name ,   key=TestTable1.M.createBy$key ,   auto=TestTable1.M.createBy$auto ,   notnull=TestTable1.M.createBy$notnull ,   length=TestTable1.M.createBy$length ,   value=TestTable1.M.createBy$value ,   remarks=TestTable1.M.createBy$remarks    )
	public String getTestTable1$createBy(){
		return this.TestTable1$createBy;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93,  name=TestTable1.M.updateTime$name ,   key=TestTable1.M.updateTime$key ,   auto=TestTable1.M.updateTime$auto ,   notnull=TestTable1.M.updateTime$notnull ,   length=TestTable1.M.updateTime$length ,   value=TestTable1.M.updateTime$value ,   remarks=TestTable1.M.updateTime$remarks    )
	public java.util.Date getTestTable1$updateTime(){
		return this.TestTable1$updateTime;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12,  name=TestTable1.M.updateBy$name ,   key=TestTable1.M.updateBy$key ,   auto=TestTable1.M.updateBy$auto ,   notnull=TestTable1.M.updateBy$notnull ,   length=TestTable1.M.updateBy$length ,   value=TestTable1.M.updateBy$value ,   remarks=TestTable1.M.updateBy$remarks    )
	public String getTestTable1$updateBy(){
		return this.TestTable1$updateBy;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id [<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10					
	* <li><B>remarks:</B> 唯一主键			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.id$name ,   key=TestTable2.M.id$key ,   auto=TestTable2.M.id$auto ,   notnull=TestTable2.M.id$notnull ,   length=TestTable2.M.id$length ,   value=TestTable2.M.id$value ,   remarks=TestTable2.M.id$remarks    )
	public Integer getTestTable2$id(){
		return this.TestTable2$id;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128		 	&nbsp;<B>value:</B> N0001 <br> 			
	* <li><B>remarks:</B> 名称			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.name$name ,   key=TestTable2.M.name$key ,   auto=TestTable2.M.name$auto ,   notnull=TestTable2.M.name$notnull ,   length=TestTable2.M.name$length ,   value=TestTable2.M.name$value ,   remarks=TestTable2.M.name$remarks    )
	public String getTestTable2$name(){
		return this.TestTable2$name;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128					
	* <li><B>remarks:</B> 标题			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.title$name ,   key=TestTable2.M.title$key ,   auto=TestTable2.M.title$auto ,   notnull=TestTable2.M.title$notnull ,   length=TestTable2.M.title$length ,   value=TestTable2.M.title$value ,   remarks=TestTable2.M.title$remarks    )
	public String getTestTable2$title(){
		return this.TestTable2$title;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10		 	&nbsp;<B>value:</B> 0 <br> 			
	* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4,  name=TestTable2.M.enumIntA$name ,   key=TestTable2.M.enumIntA$key ,   auto=TestTable2.M.enumIntA$auto ,   notnull=TestTable2.M.enumIntA$notnull ,   length=TestTable2.M.enumIntA$length ,   value=TestTable2.M.enumIntA$value ,   remarks=TestTable2.M.enumIntA$remarks    )
	public TestTable2.EnumIntA getTestTable2$enumIntA(){
		return this.TestTable2$enumIntA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64		 	&nbsp;<B>value:</B> TRUE <br> 			
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.enumStringA$name ,   key=TestTable2.M.enumStringA$key ,   auto=TestTable2.M.enumStringA$auto ,   notnull=TestTable2.M.enumStringA$notnull ,   length=TestTable2.M.enumStringA$length ,   value=TestTable2.M.enumStringA$value ,   remarks=TestTable2.M.enumStringA$remarks    )
	public TestTable2.EnumStringA getTestTable2$enumStringA(){
		return this.TestTable2$enumStringA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.tsA$name ,   key=TestTable2.M.tsA$key ,   auto=TestTable2.M.tsA$auto ,   notnull=TestTable2.M.tsA$notnull ,   length=TestTable2.M.tsA$length ,   value=TestTable2.M.tsA$value ,   remarks=TestTable2.M.tsA$remarks    )
	public java.util.Date getTestTable2$tsA(){
		return this.TestTable2$tsA;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time [<font color=red>NOTNULL</font>] <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.createTime$name ,   key=TestTable2.M.createTime$key ,   auto=TestTable2.M.createTime$auto ,   notnull=TestTable2.M.createTime$notnull ,   length=TestTable2.M.createTime$length ,   value=TestTable2.M.createTime$value ,   remarks=TestTable2.M.createTime$remarks    )
	public java.util.Date getTestTable2$createTime(){
		return this.TestTable2$createTime;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.createBy$name ,   key=TestTable2.M.createBy$key ,   auto=TestTable2.M.createBy$auto ,   notnull=TestTable2.M.createBy$notnull ,   length=TestTable2.M.createBy$length ,   value=TestTable2.M.createBy$value ,   remarks=TestTable2.M.createBy$remarks    )
	public String getTestTable2$createBy(){
		return this.TestTable2$createBy;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93,  name=TestTable2.M.updateTime$name ,   key=TestTable2.M.updateTime$key ,   auto=TestTable2.M.updateTime$auto ,   notnull=TestTable2.M.updateTime$notnull ,   length=TestTable2.M.updateTime$length ,   value=TestTable2.M.updateTime$value ,   remarks=TestTable2.M.updateTime$remarks    )
	public java.util.Date getTestTable2$updateTime(){
		return this.TestTable2$updateTime;		
	}
		
	/**
	* @Column 
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by <br>   
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64					
	* <li><B>remarks:</B> 			 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12,  name=TestTable2.M.updateBy$name ,   key=TestTable2.M.updateBy$key ,   auto=TestTable2.M.updateBy$auto ,   notnull=TestTable2.M.updateBy$notnull ,   length=TestTable2.M.updateBy$length ,   value=TestTable2.M.updateBy$value ,   remarks=TestTable2.M.updateBy$remarks    )
	public String getTestTable2$updateBy(){
		return this.TestTable2$updateBy;		
	}
		
}