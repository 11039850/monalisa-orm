package test.com.tsc9526.monalisa.plugin.mysqldbgen;
 

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;
import com.tsc9526.monalisa.orm.annotation.Column;
  
/**
 * Auto generated code by monalisa 2.0.0-SNAPSHOT
 *
 * @see test.com.tsc9526.monalisa.plugin.MysqlDBGen#findOne(int)
 */
public class ResultFindOne implements java.io.Serializable{
	private static final long serialVersionUID = 1276666885255L;	
	final static String  FINGERPRINT = "000000EEF89650FF";
	  
	 
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	private Integer id;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	private String name;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	private String title;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	private TestTable1.EnumIntA enumIntA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	private TestTable1.EnumStringA enumStringA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	private java.util.Date tsA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	private java.util.Date createTime;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	private String createBy;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	private java.util.Date updateTime;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	private String updateBy;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	private Integer version;	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public ResultFindOne setId(Integer id){
		this.id = id;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public ResultFindOne setName(String name){
		this.name = name;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public ResultFindOne setTitle(String title){
		this.title = title;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public ResultFindOne setEnumIntA(TestTable1.EnumIntA enumIntA){
		this.enumIntA = enumIntA;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	public ResultFindOne setEnumStringA(TestTable1.EnumStringA enumStringA){
		this.enumStringA = enumStringA;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	public ResultFindOne setTsA(java.util.Date tsA){
		this.tsA = tsA;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	public ResultFindOne setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	public ResultFindOne setCreateBy(String createBy){
		this.createBy = createBy;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	public ResultFindOne setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	public ResultFindOne setUpdateBy(String updateBy){
		this.updateBy = updateBy;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public ResultFindOne setVersion(Integer version){
		this.version = version;
		return this;
	}
	
	
	
	 
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public Integer getId(){
		return this.id;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public Integer getId(Integer defaultValue){
		Integer r=this.getId();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public String getName(){
		return this.name;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	* @param defaultValue  Return the default value if name is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public String getName(String defaultValue){
		String r=this.getName();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public String getTitle(){
		return this.title;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	* @param defaultValue  Return the default value if title is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public String getTitle(String defaultValue){
		String r=this.getTitle();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public TestTable1.EnumIntA getEnumIntA(){
		return this.enumIntA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	* @param defaultValue  Return the default value if enumIntA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public TestTable1.EnumIntA getEnumIntA(TestTable1.EnumIntA defaultValue){
		TestTable1.EnumIntA r=this.getEnumIntA();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	public TestTable1.EnumStringA getEnumStringA(){
		return this.enumStringA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	* @param defaultValue  Return the default value if enumStringA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	public TestTable1.EnumStringA getEnumStringA(TestTable1.EnumStringA defaultValue){
		TestTable1.EnumStringA r=this.getEnumStringA();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	public java.util.Date getTsA(){
		return this.tsA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if tsA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	public java.util.Date getTsA(java.util.Date defaultValue){
		java.util.Date r=this.getTsA();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	public java.util.Date getCreateTime(){
		return this.createTime;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createTime is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	public java.util.Date getCreateTime(java.util.Date defaultValue){
		java.util.Date r=this.getCreateTime();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	public String getCreateBy(){
		return this.createBy;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createBy is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	public String getCreateBy(String defaultValue){
		String r=this.getCreateBy();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	public java.util.Date getUpdateTime(){
		return this.updateTime;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateTime is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	public java.util.Date getUpdateTime(java.util.Date defaultValue){
		java.util.Date r=this.getUpdateTime();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	public String getUpdateBy(){
		return this.updateBy;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateBy is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	public String getUpdateBy(String defaultValue){
		String r=this.getUpdateBy();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public Integer getVersion(){
		return this.version;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	* @param defaultValue  Return the default value if version is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public Integer getVersion(Integer defaultValue){
		Integer r=this.getVersion();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
		 
}
 
