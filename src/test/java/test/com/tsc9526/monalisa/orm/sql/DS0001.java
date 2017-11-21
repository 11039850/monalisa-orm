package test.com.tsc9526.monalisa.orm.sql;
 

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable1;
import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestTable2;
import com.google.gson.JsonObject;
import test.com.tsc9526.monalisa.orm.data.ColumnData;
import com.tsc9526.monalisa.orm.annotation.Column;
  
/**
 * Auto generated code by monalisa 2.1.1-SNAPSHOT
 *
 * @see 
 */
public class DS0001 implements java.io.Serializable{
	private static final long serialVersionUID = 2505485319463L;	
	final static String  FINGERPRINT = "";
	  
	 
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, seq=TestTable1.M.id$seq, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, decimalDigits=TestTable1.M.id$decimalDigits, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	private Integer id;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, seq=TestTable1.M.name$seq, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, decimalDigits=TestTable1.M.name$decimalDigits, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	private String name;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, seq=TestTable1.M.title$seq, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, decimalDigits=TestTable1.M.title$decimalDigits, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	private String title;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, seq=TestTable1.M.enumIntA$seq, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, decimalDigits=TestTable1.M.enumIntA$decimalDigits, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	private TestTable1.EnumIntA enumIntA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, seq=TestTable1.M.enumStringA$seq, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, decimalDigits=TestTable1.M.enumStringA$decimalDigits, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	private TestTable1.EnumStringA enumStringA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, seq=TestTable1.M.tsA$seq, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, decimalDigits=TestTable1.M.tsA$decimalDigits, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	private java.util.Date tsA;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, seq=TestTable1.M.createTime$seq, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, decimalDigits=TestTable1.M.createTime$decimalDigits, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	private java.util.Date createTime;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, seq=TestTable1.M.createBy$seq, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, decimalDigits=TestTable1.M.createBy$decimalDigits, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	private String createBy;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, seq=TestTable1.M.updateTime$seq, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, decimalDigits=TestTable1.M.updateTime$decimalDigits, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	private java.util.Date updateTime;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, seq=TestTable1.M.updateBy$seq, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, decimalDigits=TestTable1.M.updateBy$decimalDigits, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	private String updateBy;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, seq=TestTable1.M.version$seq, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, decimalDigits=TestTable1.M.version$decimalDigits, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	private Integer version;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	private Integer id1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, seq=TestTable2.M.name$seq, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, decimalDigits=TestTable2.M.name$decimalDigits, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	private String name1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, seq=TestTable2.M.title$seq, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, decimalDigits=TestTable2.M.title$decimalDigits, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	private String title1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, seq=TestTable2.M.enumIntA$seq, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, decimalDigits=TestTable2.M.enumIntA$decimalDigits, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	private TestTable2.EnumIntA enumIntA1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, seq=TestTable2.M.enumStringA$seq, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, decimalDigits=TestTable2.M.enumStringA$decimalDigits, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	private TestTable2.EnumStringA enumStringA1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of int. #array{int}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, seq=TestTable2.M.arrayInt$seq, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, decimalDigits=TestTable2.M.arrayInt$decimalDigits, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	private int[] arrayInt;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of string. #array{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, seq=TestTable2.M.arrayString$seq, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, decimalDigits=TestTable2.M.arrayString$decimalDigits, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	private String[] arrayString;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object. #json{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, seq=TestTable2.M.json$seq, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, decimalDigits=TestTable2.M.json$decimalDigits, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	private JsonObject json;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object with given class.  #json{test.com.tsc9526.monalisa.orm.data.ColumnData}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, seq=TestTable2.M.obj$seq, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, decimalDigits=TestTable2.M.obj$decimalDigits, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	private ColumnData obj;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, seq=TestTable2.M.tsA$seq, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, decimalDigits=TestTable2.M.tsA$decimalDigits, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	private java.util.Date tsA1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, seq=TestTable2.M.createTime$seq, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, decimalDigits=TestTable2.M.createTime$decimalDigits, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	private java.util.Date createTime1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, seq=TestTable2.M.createBy$seq, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, decimalDigits=TestTable2.M.createBy$decimalDigits, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	private String createBy1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, seq=TestTable2.M.updateTime$seq, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, decimalDigits=TestTable2.M.updateTime$decimalDigits, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	private java.util.Date updateTime1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, seq=TestTable2.M.updateBy$seq, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, decimalDigits=TestTable2.M.updateBy$decimalDigits, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	private String updateBy1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> v1 &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.v1$name, key=TestTable2.M.v1$key, auto=TestTable2.M.v1$auto, seq=TestTable2.M.v1$seq, notnull=TestTable2.M.v1$notnull, length=TestTable2.M.v1$length, decimalDigits=TestTable2.M.v1$decimalDigits, value=TestTable2.M.v1$value, remarks=TestTable2.M.v1$remarks)
	private Integer v1;	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	private Integer id2;	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, seq=TestTable1.M.id$seq, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, decimalDigits=TestTable1.M.id$decimalDigits, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public DS0001 setId(Integer id){
		this.id = id;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, seq=TestTable1.M.name$seq, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, decimalDigits=TestTable1.M.name$decimalDigits, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public DS0001 setName(String name){
		this.name = name;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, seq=TestTable1.M.title$seq, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, decimalDigits=TestTable1.M.title$decimalDigits, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public DS0001 setTitle(String title){
		this.title = title;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, seq=TestTable1.M.enumIntA$seq, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, decimalDigits=TestTable1.M.enumIntA$decimalDigits, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, seq=TestTable1.M.enumStringA$seq, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, decimalDigits=TestTable1.M.enumStringA$decimalDigits, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, seq=TestTable1.M.tsA$seq, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, decimalDigits=TestTable1.M.tsA$decimalDigits, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, seq=TestTable1.M.createTime$seq, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, decimalDigits=TestTable1.M.createTime$decimalDigits, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, seq=TestTable1.M.createBy$seq, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, decimalDigits=TestTable1.M.createBy$decimalDigits, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, seq=TestTable1.M.updateTime$seq, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, decimalDigits=TestTable1.M.updateTime$decimalDigits, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, seq=TestTable1.M.updateBy$seq, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, decimalDigits=TestTable1.M.updateBy$decimalDigits, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	public DS0001 setUpdateBy(String updateBy){
		this.updateBy = updateBy;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, seq=TestTable1.M.version$seq, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, decimalDigits=TestTable1.M.version$decimalDigits, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public DS0001 setVersion(Integer version){
		this.version = version;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public DS0001 setId1(Integer id1){
		this.id1 = id1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, seq=TestTable2.M.name$seq, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, decimalDigits=TestTable2.M.name$decimalDigits, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	public DS0001 setName1(String name1){
		this.name1 = name1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, seq=TestTable2.M.title$seq, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, decimalDigits=TestTable2.M.title$decimalDigits, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	public DS0001 setTitle1(String title1){
		this.title1 = title1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, seq=TestTable2.M.enumIntA$seq, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, decimalDigits=TestTable2.M.enumIntA$decimalDigits, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, seq=TestTable2.M.enumStringA$seq, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, decimalDigits=TestTable2.M.enumStringA$decimalDigits, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	public DS0001 setEnumStringA1(TestTable2.EnumStringA enumStringA1){
		this.enumStringA1 = enumStringA1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of int. #array{int}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, seq=TestTable2.M.arrayInt$seq, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, decimalDigits=TestTable2.M.arrayInt$decimalDigits, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	public DS0001 setArrayInt(int[] arrayInt){
		this.arrayInt = arrayInt;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of string. #array{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, seq=TestTable2.M.arrayString$seq, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, decimalDigits=TestTable2.M.arrayString$decimalDigits, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	public DS0001 setArrayString(String[] arrayString){
		this.arrayString = arrayString;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object. #json{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, seq=TestTable2.M.json$seq, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, decimalDigits=TestTable2.M.json$decimalDigits, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	public DS0001 setJson(JsonObject json){
		this.json = json;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object with given class.  #json{test.com.tsc9526.monalisa.orm.data.ColumnData}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, seq=TestTable2.M.obj$seq, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, decimalDigits=TestTable2.M.obj$decimalDigits, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, seq=TestTable2.M.tsA$seq, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, decimalDigits=TestTable2.M.tsA$decimalDigits, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, seq=TestTable2.M.createTime$seq, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, decimalDigits=TestTable2.M.createTime$decimalDigits, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, seq=TestTable2.M.createBy$seq, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, decimalDigits=TestTable2.M.createBy$decimalDigits, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, seq=TestTable2.M.updateTime$seq, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, decimalDigits=TestTable2.M.updateTime$decimalDigits, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, seq=TestTable2.M.updateBy$seq, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, decimalDigits=TestTable2.M.updateBy$decimalDigits, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public DS0001 setUpdateBy1(String updateBy1){
		this.updateBy1 = updateBy1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> v1 &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.v1$name, key=TestTable2.M.v1$key, auto=TestTable2.M.v1$auto, seq=TestTable2.M.v1$seq, notnull=TestTable2.M.v1$notnull, length=TestTable2.M.v1$length, decimalDigits=TestTable2.M.v1$decimalDigits, value=TestTable2.M.v1$value, remarks=TestTable2.M.v1$remarks)
	public DS0001 setV1(Integer v1){
		this.v1 = v1;
		return this;
	}
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public DS0001 setId2(Integer id2){
		this.id2 = id2;
		return this;
	}
	
	
	
	 
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, seq=TestTable1.M.id$seq, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, decimalDigits=TestTable1.M.id$decimalDigits, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
	public Integer getId(){
		return this.id;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.id$name, key=TestTable1.M.id$key, auto=TestTable1.M.id$auto, seq=TestTable1.M.id$seq, notnull=TestTable1.M.id$notnull, length=TestTable1.M.id$length, decimalDigits=TestTable1.M.id$decimalDigits, value=TestTable1.M.id$value, remarks=TestTable1.M.id$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, seq=TestTable1.M.name$seq, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, decimalDigits=TestTable1.M.name$decimalDigits, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
	public String getName(){
		return this.name;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	* @param defaultValue  Return the default value if name is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.name$name, key=TestTable1.M.name$key, auto=TestTable1.M.name$auto, seq=TestTable1.M.name$seq, notnull=TestTable1.M.name$notnull, length=TestTable1.M.name$length, decimalDigits=TestTable1.M.name$decimalDigits, value=TestTable1.M.name$value, remarks=TestTable1.M.name$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, seq=TestTable1.M.title$seq, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, decimalDigits=TestTable1.M.title$decimalDigits, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
	public String getTitle(){
		return this.title;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	* @param defaultValue  Return the default value if title is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.title$name, key=TestTable1.M.title$key, auto=TestTable1.M.title$auto, seq=TestTable1.M.title$seq, notnull=TestTable1.M.title$notnull, length=TestTable1.M.title$length, decimalDigits=TestTable1.M.title$decimalDigits, value=TestTable1.M.title$value, remarks=TestTable1.M.title$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, seq=TestTable1.M.enumIntA$seq, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, decimalDigits=TestTable1.M.enumIntA$decimalDigits, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
	public TestTable1.EnumIntA getEnumIntA(){
		return this.enumIntA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	* @param defaultValue  Return the default value if enumIntA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.enumIntA$name, key=TestTable1.M.enumIntA$key, auto=TestTable1.M.enumIntA$auto, seq=TestTable1.M.enumIntA$seq, notnull=TestTable1.M.enumIntA$notnull, length=TestTable1.M.enumIntA$length, decimalDigits=TestTable1.M.enumIntA$decimalDigits, value=TestTable1.M.enumIntA$value, remarks=TestTable1.M.enumIntA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, seq=TestTable1.M.enumStringA$seq, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, decimalDigits=TestTable1.M.enumStringA$decimalDigits, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
	public TestTable1.EnumStringA getEnumStringA(){
		return this.enumStringA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	* @param defaultValue  Return the default value if enumStringA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.enumStringA$name, key=TestTable1.M.enumStringA$key, auto=TestTable1.M.enumStringA$auto, seq=TestTable1.M.enumStringA$seq, notnull=TestTable1.M.enumStringA$notnull, length=TestTable1.M.enumStringA$length, decimalDigits=TestTable1.M.enumStringA$decimalDigits, value=TestTable1.M.enumStringA$value, remarks=TestTable1.M.enumStringA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, seq=TestTable1.M.tsA$seq, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, decimalDigits=TestTable1.M.tsA$decimalDigits, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
	public java.util.Date getTsA(){
		return this.tsA;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if tsA is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.tsA$name, key=TestTable1.M.tsA$key, auto=TestTable1.M.tsA$auto, seq=TestTable1.M.tsA$seq, notnull=TestTable1.M.tsA$notnull, length=TestTable1.M.tsA$length, decimalDigits=TestTable1.M.tsA$decimalDigits, value=TestTable1.M.tsA$value, remarks=TestTable1.M.tsA$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, seq=TestTable1.M.createTime$seq, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, decimalDigits=TestTable1.M.createTime$decimalDigits, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
	public java.util.Date getCreateTime(){
		return this.createTime;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createTime is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.createTime$name, key=TestTable1.M.createTime$key, auto=TestTable1.M.createTime$auto, seq=TestTable1.M.createTime$seq, notnull=TestTable1.M.createTime$notnull, length=TestTable1.M.createTime$length, decimalDigits=TestTable1.M.createTime$decimalDigits, value=TestTable1.M.createTime$value, remarks=TestTable1.M.createTime$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, seq=TestTable1.M.createBy$seq, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, decimalDigits=TestTable1.M.createBy$decimalDigits, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
	public String getCreateBy(){
		return this.createBy;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createBy is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.createBy$name, key=TestTable1.M.createBy$key, auto=TestTable1.M.createBy$auto, seq=TestTable1.M.createBy$seq, notnull=TestTable1.M.createBy$notnull, length=TestTable1.M.createBy$length, decimalDigits=TestTable1.M.createBy$decimalDigits, value=TestTable1.M.createBy$value, remarks=TestTable1.M.createBy$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, seq=TestTable1.M.updateTime$seq, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, decimalDigits=TestTable1.M.updateTime$decimalDigits, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
	public java.util.Date getUpdateTime(){
		return this.updateTime;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateTime is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=93, name=TestTable1.M.updateTime$name, key=TestTable1.M.updateTime$key, auto=TestTable1.M.updateTime$auto, seq=TestTable1.M.updateTime$seq, notnull=TestTable1.M.updateTime$notnull, length=TestTable1.M.updateTime$length, decimalDigits=TestTable1.M.updateTime$decimalDigits, value=TestTable1.M.updateTime$value, remarks=TestTable1.M.updateTime$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, seq=TestTable1.M.updateBy$seq, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, decimalDigits=TestTable1.M.updateBy$decimalDigits, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
	public String getUpdateBy(){
		return this.updateBy;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateBy is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=12, name=TestTable1.M.updateBy$name, key=TestTable1.M.updateBy$key, auto=TestTable1.M.updateBy$auto, seq=TestTable1.M.updateBy$seq, notnull=TestTable1.M.updateBy$notnull, length=TestTable1.M.updateBy$length, decimalDigits=TestTable1.M.updateBy$decimalDigits, value=TestTable1.M.updateBy$value, remarks=TestTable1.M.updateBy$remarks)
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
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, seq=TestTable1.M.version$seq, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, decimalDigits=TestTable1.M.version$decimalDigits, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public Integer getVersion(){
		return this.version;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_1&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	* @param defaultValue  Return the default value if version is null.*/
	@Column(table=TestTable1.M.TABLE, jdbcType=4, name=TestTable1.M.version$name, key=TestTable1.M.version$key, auto=TestTable1.M.version$auto, seq=TestTable1.M.version$seq, notnull=TestTable1.M.version$notnull, length=TestTable1.M.version$length, decimalDigits=TestTable1.M.version$decimalDigits, value=TestTable1.M.version$value, remarks=TestTable1.M.version$remarks)
	public Integer getVersion(Integer defaultValue){
		Integer r=this.getVersion();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public Integer getId1(){
		return this.id1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
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
	* <li><B>remarks:</B> the name
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, seq=TestTable2.M.name$seq, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, decimalDigits=TestTable2.M.name$decimalDigits, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
	public String getName1(){
		return this.name1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	* @param defaultValue  Return the default value if name1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.name$name, key=TestTable2.M.name$key, auto=TestTable2.M.name$auto, seq=TestTable2.M.name$seq, notnull=TestTable2.M.name$notnull, length=TestTable2.M.name$length, decimalDigits=TestTable2.M.name$decimalDigits, value=TestTable2.M.name$value, remarks=TestTable2.M.name$remarks)
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
	* <li><B>remarks:</B> the title
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, seq=TestTable2.M.title$seq, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, decimalDigits=TestTable2.M.title$decimalDigits, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
	public String getTitle1(){
		return this.title1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the title
	* @param defaultValue  Return the default value if title1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.title$name, key=TestTable2.M.title$key, auto=TestTable2.M.title$auto, seq=TestTable2.M.title$seq, notnull=TestTable2.M.title$notnull, length=TestTable2.M.title$length, decimalDigits=TestTable2.M.title$decimalDigits, value=TestTable2.M.title$value, remarks=TestTable2.M.title$remarks)
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
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, seq=TestTable2.M.enumIntA$seq, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, decimalDigits=TestTable2.M.enumIntA$decimalDigits, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
	public TestTable2.EnumIntA getEnumIntA1(){
		return this.enumIntA1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	* @param defaultValue  Return the default value if enumIntA1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.enumIntA$name, key=TestTable2.M.enumIntA$key, auto=TestTable2.M.enumIntA$auto, seq=TestTable2.M.enumIntA$seq, notnull=TestTable2.M.enumIntA$notnull, length=TestTable2.M.enumIntA$length, decimalDigits=TestTable2.M.enumIntA$decimalDigits, value=TestTable2.M.enumIntA$value, remarks=TestTable2.M.enumIntA$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, seq=TestTable2.M.enumStringA$seq, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, decimalDigits=TestTable2.M.enumStringA$decimalDigits, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
	public TestTable2.EnumStringA getEnumStringA1(){
		return this.enumStringA1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	* @param defaultValue  Return the default value if enumStringA1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.enumStringA$name, key=TestTable2.M.enumStringA$key, auto=TestTable2.M.enumStringA$auto, seq=TestTable2.M.enumStringA$seq, notnull=TestTable2.M.enumStringA$notnull, length=TestTable2.M.enumStringA$length, decimalDigits=TestTable2.M.enumStringA$decimalDigits, value=TestTable2.M.enumStringA$value, remarks=TestTable2.M.enumStringA$remarks)
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
	* <li><B>remarks:</B> array of int. #array{int}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, seq=TestTable2.M.arrayInt$seq, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, decimalDigits=TestTable2.M.arrayInt$decimalDigits, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
	public int[] getArrayInt(){
		return this.arrayInt;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of int. #array{int}
	* @param defaultValue  Return the default value if arrayInt is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayInt$name, key=TestTable2.M.arrayInt$key, auto=TestTable2.M.arrayInt$auto, seq=TestTable2.M.arrayInt$seq, notnull=TestTable2.M.arrayInt$notnull, length=TestTable2.M.arrayInt$length, decimalDigits=TestTable2.M.arrayInt$decimalDigits, value=TestTable2.M.arrayInt$value, remarks=TestTable2.M.arrayInt$remarks)
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
	* <li><B>remarks:</B> array of string. #array{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, seq=TestTable2.M.arrayString$seq, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, decimalDigits=TestTable2.M.arrayString$decimalDigits, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
	public String[] getArrayString(){
		return this.arrayString;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
	* <li><B>remarks:</B> array of string. #array{}
	* @param defaultValue  Return the default value if arrayString is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.arrayString$name, key=TestTable2.M.arrayString$key, auto=TestTable2.M.arrayString$auto, seq=TestTable2.M.arrayString$seq, notnull=TestTable2.M.arrayString$notnull, length=TestTable2.M.arrayString$length, decimalDigits=TestTable2.M.arrayString$decimalDigits, value=TestTable2.M.arrayString$value, remarks=TestTable2.M.arrayString$remarks)
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
	* <li><B>remarks:</B> Json object. #json{}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, seq=TestTable2.M.json$seq, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, decimalDigits=TestTable2.M.json$decimalDigits, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
	public JsonObject getJson(){
		return this.json;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object. #json{}
	* @param defaultValue  Return the default value if json is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.json$name, key=TestTable2.M.json$key, auto=TestTable2.M.json$auto, seq=TestTable2.M.json$seq, notnull=TestTable2.M.json$notnull, length=TestTable2.M.json$length, decimalDigits=TestTable2.M.json$decimalDigits, value=TestTable2.M.json$value, remarks=TestTable2.M.json$remarks)
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
	* <li><B>remarks:</B> Json object with given class.  #json{test.com.tsc9526.monalisa.orm.data.ColumnData}
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, seq=TestTable2.M.obj$seq, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, decimalDigits=TestTable2.M.obj$decimalDigits, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
	public ColumnData getObj(){
		return this.obj;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> Json object with given class.  #json{test.com.tsc9526.monalisa.orm.data.ColumnData}
	* @param defaultValue  Return the default value if obj is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.obj$name, key=TestTable2.M.obj$key, auto=TestTable2.M.obj$auto, seq=TestTable2.M.obj$seq, notnull=TestTable2.M.obj$notnull, length=TestTable2.M.obj$length, decimalDigits=TestTable2.M.obj$decimalDigits, value=TestTable2.M.obj$value, remarks=TestTable2.M.obj$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, seq=TestTable2.M.tsA$seq, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, decimalDigits=TestTable2.M.tsA$decimalDigits, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
	public java.util.Date getTsA1(){
		return this.tsA1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if tsA1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.tsA$name, key=TestTable2.M.tsA$key, auto=TestTable2.M.tsA$auto, seq=TestTable2.M.tsA$seq, notnull=TestTable2.M.tsA$notnull, length=TestTable2.M.tsA$length, decimalDigits=TestTable2.M.tsA$decimalDigits, value=TestTable2.M.tsA$value, remarks=TestTable2.M.tsA$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, seq=TestTable2.M.createTime$seq, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, decimalDigits=TestTable2.M.createTime$decimalDigits, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
	public java.util.Date getCreateTime1(){
		return this.createTime1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createTime1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.createTime$name, key=TestTable2.M.createTime$key, auto=TestTable2.M.createTime$auto, seq=TestTable2.M.createTime$seq, notnull=TestTable2.M.createTime$notnull, length=TestTable2.M.createTime$length, decimalDigits=TestTable2.M.createTime$decimalDigits, value=TestTable2.M.createTime$value, remarks=TestTable2.M.createTime$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, seq=TestTable2.M.createBy$seq, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, decimalDigits=TestTable2.M.createBy$decimalDigits, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
	public String getCreateBy1(){
		return this.createBy1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createBy1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.createBy$name, key=TestTable2.M.createBy$key, auto=TestTable2.M.createBy$auto, seq=TestTable2.M.createBy$seq, notnull=TestTable2.M.createBy$notnull, length=TestTable2.M.createBy$length, decimalDigits=TestTable2.M.createBy$decimalDigits, value=TestTable2.M.createBy$value, remarks=TestTable2.M.createBy$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, seq=TestTable2.M.updateTime$seq, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, decimalDigits=TestTable2.M.updateTime$decimalDigits, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
	public java.util.Date getUpdateTime1(){
		return this.updateTime1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateTime1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=93, name=TestTable2.M.updateTime$name, key=TestTable2.M.updateTime$key, auto=TestTable2.M.updateTime$auto, seq=TestTable2.M.updateTime$seq, notnull=TestTable2.M.updateTime$notnull, length=TestTable2.M.updateTime$length, decimalDigits=TestTable2.M.updateTime$decimalDigits, value=TestTable2.M.updateTime$value, remarks=TestTable2.M.updateTime$remarks)
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
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, seq=TestTable2.M.updateBy$seq, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, decimalDigits=TestTable2.M.updateBy$decimalDigits, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public String getUpdateBy1(){
		return this.updateBy1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateBy1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=12, name=TestTable2.M.updateBy$name, key=TestTable2.M.updateBy$key, auto=TestTable2.M.updateBy$auto, seq=TestTable2.M.updateBy$seq, notnull=TestTable2.M.updateBy$notnull, length=TestTable2.M.updateBy$length, decimalDigits=TestTable2.M.updateBy$decimalDigits, value=TestTable2.M.updateBy$value, remarks=TestTable2.M.updateBy$remarks)
	public String getUpdateBy1(String defaultValue){
		String r=this.getUpdateBy1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> v1 &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.v1$name, key=TestTable2.M.v1$key, auto=TestTable2.M.v1$auto, seq=TestTable2.M.v1$seq, notnull=TestTable2.M.v1$notnull, length=TestTable2.M.v1$length, decimalDigits=TestTable2.M.v1$decimalDigits, value=TestTable2.M.v1$value, remarks=TestTable2.M.v1$remarks)
	public Integer getV1(){
		return this.v1;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> v1 &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> version
	* @param defaultValue  Return the default value if v1 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.v1$name, key=TestTable2.M.v1$key, auto=TestTable2.M.v1$auto, seq=TestTable2.M.v1$seq, notnull=TestTable2.M.v1$notnull, length=TestTable2.M.v1$length, decimalDigits=TestTable2.M.v1$decimalDigits, value=TestTable2.M.v1$value, remarks=TestTable2.M.v1$remarks)
	public Integer getV1(Integer defaultValue){
		Integer r=this.getV1();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public Integer getId2(){
		return this.id2;		
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id2 is null.*/
	@Column(table=TestTable2.M.TABLE, jdbcType=4, name=TestTable2.M.id$name, key=TestTable2.M.id$key, auto=TestTable2.M.id$auto, seq=TestTable2.M.id$seq, notnull=TestTable2.M.id$notnull, length=TestTable2.M.id$length, decimalDigits=TestTable2.M.id$decimalDigits, value=TestTable2.M.id$value, remarks=TestTable2.M.id$remarks)
	public Integer getId2(Integer defaultValue){
		Integer r=this.getId2();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	
	
		 
}
 
