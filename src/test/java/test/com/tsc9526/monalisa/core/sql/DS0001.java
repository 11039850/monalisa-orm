



package test.com.tsc9526.monalisa.core.sql;
 

import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable1;

import test.com.tsc9526.monalisa.core.mysql.mysqldb.TestTable2;

import com.google.gson.JsonObject;

import test.com.tsc9526.monalisa.core.data.ColumnData;

import com.tsc9526.monalisa.core.annotation.Column;

 
/**
 * 
 * @see 
 */
public class DS0001 implements java.io.Serializable{
	private static final long serialVersionUID = 2345146489322L;	
	final static String  FINGERPRINT = "";
	  
	 
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	private Integer id;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	private String name;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	private String title;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
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
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	private Integer id1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	private String name1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	private String title1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	private TestTable2.EnumIntA enumIntA1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	private TestTable2.EnumStringA enumStringA1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	private int[] arrayInt;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	private String[] arrayString;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	private JsonObject json;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	private ColumnData obj;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	private java.util.Date tsA1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	private java.util.Date createTime1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	private String createBy1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	private java.util.Date updateTime1;	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	private String updateBy1;	
	
	
	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public DS0001 setId(Integer id){
		this.id = id;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public DS0001 setName(String name){
		this.name = name;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public DS0001 setTitle(String title){
		this.title = title;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public DS0001 setEnumIntA(TestTable1.EnumIntA enumIntA){
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
	public DS0001 setEnumStringA(TestTable1.EnumStringA enumStringA){
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
	public DS0001 setTsA(java.util.Date tsA){
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
	public DS0001 setCreateTime(java.util.Date createTime){
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
	public DS0001 setCreateBy(String createBy){
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
	public DS0001 setUpdateTime(java.util.Date updateTime){
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
	public DS0001 setUpdateBy(String updateBy){
		this.updateBy = updateBy;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public DS0001 setId1(Integer id1){
		this.id1 = id1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	public DS0001 setName1(String name1){
		this.name1 = name1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	public DS0001 setTitle1(String title1){
		this.title1 = title1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	public DS0001 setEnumIntA1(TestTable2.EnumIntA enumIntA1){
		this.enumIntA1 = enumIntA1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	public DS0001 setEnumStringA1(TestTable2.EnumStringA enumStringA1){
		this.enumStringA1 = enumStringA1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	public DS0001 setArrayInt(int[] arrayInt){
		this.arrayInt = arrayInt;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	public DS0001 setArrayString(String[] arrayString){
		this.arrayString = arrayString;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	public DS0001 setJson(JsonObject json){
		this.json = json;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	public DS0001 setObj(ColumnData obj){
		this.obj = obj;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	public DS0001 setTsA1(java.util.Date tsA1){
		this.tsA1 = tsA1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	public DS0001 setCreateTime1(java.util.Date createTime1){
		this.createTime1 = createTime1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	public DS0001 setCreateBy1(String createBy1){
		this.createBy1 = createBy1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	public DS0001 setUpdateTime1(java.util.Date updateTime1){
		this.updateTime1 = updateTime1;
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public DS0001 setUpdateBy1(String updateBy1){
		this.updateBy1 = updateBy1;
		return this;
	}
	
	
	
	 
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public Integer getId(){
		return this.id;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
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
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public String getName(){
		return this.name;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
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
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public String getTitle(){
		return this.title;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
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
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public TestTable1.EnumIntA getEnumIntA(){
		return this.enumIntA;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
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
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public Integer getId1(){
		return this.id1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
* @param defaultValue  Return the default value if id1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public Integer getId1(Integer defaultValue){
		Integer r=this.getId1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	public String getName1(){
		return this.name1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
* @param defaultValue  Return the default value if name1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	public String getName1(String defaultValue){
		String r=this.getName1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	public String getTitle1(){
		return this.title1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
* @param defaultValue  Return the default value if title1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	public String getTitle1(String defaultValue){
		String r=this.getTitle1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	public TestTable2.EnumIntA getEnumIntA1(){
		return this.enumIntA1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
* @param defaultValue  Return the default value if enumIntA1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	public TestTable2.EnumIntA getEnumIntA1(TestTable2.EnumIntA defaultValue){
		TestTable2.EnumIntA r=this.getEnumIntA1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	public TestTable2.EnumStringA getEnumStringA1(){
		return this.enumStringA1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
* @param defaultValue  Return the default value if enumStringA1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	public TestTable2.EnumStringA getEnumStringA1(TestTable2.EnumStringA defaultValue){
		TestTable2.EnumStringA r=this.getEnumStringA1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	public int[] getArrayInt(){
		return this.arrayInt;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
* @param defaultValue  Return the default value if arrayInt is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	public int[] getArrayInt(int[] defaultValue){
		int[] r=this.getArrayInt();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	public String[] getArrayString(){
		return this.arrayString;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
* @param defaultValue  Return the default value if arrayString is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	public String[] getArrayString(String[] defaultValue){
		String[] r=this.getArrayString();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	public JsonObject getJson(){
		return this.json;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
* @param defaultValue  Return the default value if json is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	public JsonObject getJson(JsonObject defaultValue){
		JsonObject r=this.getJson();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	public ColumnData getObj(){
		return this.obj;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
* @param defaultValue  Return the default value if obj is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	public ColumnData getObj(ColumnData defaultValue){
		ColumnData r=this.getObj();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	public java.util.Date getTsA1(){
		return this.tsA1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if tsA1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	public java.util.Date getTsA1(java.util.Date defaultValue){
		java.util.Date r=this.getTsA1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	public java.util.Date getCreateTime1(){
		return this.createTime1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if createTime1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	public java.util.Date getCreateTime1(java.util.Date defaultValue){
		java.util.Date r=this.getCreateTime1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	public String getCreateBy1(){
		return this.createBy1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if createBy1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	public String getCreateBy1(String defaultValue){
		String r=this.getCreateBy1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	public java.util.Date getUpdateTime1(){
		return this.updateTime1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if updateTime1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	public java.util.Date getUpdateTime1(java.util.Date defaultValue){
		java.util.Date r=this.getUpdateTime1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public String getUpdateBy1(){
		return this.updateBy1;		
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if updateBy1 is null.*/
@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public String getUpdateBy1(String defaultValue){
		String r=this.getUpdateBy1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
		 
}
 
