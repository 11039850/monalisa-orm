




package test.com.tsc9526.monalisa.core.mysql.mysqldb;
 		

import com.tsc9526.monalisa.core.annotation.DB;

import com.tsc9526.monalisa.core.annotation.Table;

import com.tsc9526.monalisa.core.annotation.Column;

import com.tsc9526.monalisa.core.tools.ClassHelper;

import com.google.gson.JsonObject;

import test.com.tsc9526.monalisa.core.data.ColumnData;

import java.util.List;

import java.util.Map;

import java.util.LinkedHashMap;

 

@Table(
	name="test_table_2",
	primaryKeys={"id"},
	remarks="",
	indexes={		
		
	}
)
public class TestTable2 extends com.tsc9526.monalisa.core.query.model.Model<TestTable2> implements test.com.tsc9526.monalisa.core.mysql.MysqlDB{
	private static final long serialVersionUID = 1435231704241L;
		 
	public static final Insert INSERT(){
	 	return new Insert(new TestTable2());
	}
	
	public static final Delete DELETE(){
	 	return new Delete(new TestTable2());
	}
	
	public static final Update UPDATE(TestTable2 model){
		return new Update(model);
	}		
	
	public static final Select SELECT(){
	 	return new Select(new TestTable2());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static Criteria WHERE(){
		return new Example().createCriteria();
	}
	 
	public TestTable2(){
		super("test_table_2", "id");		
	}		 
	
	 
	
	/**
	 * Constructor use primary keys.
	 *
	
	 * @param id  唯一主键
		 
	 */
	public TestTable2(Integer id){
		super("test_table_2", "id");
	
		
		this.id = id;
		fieldChanged("id");
		
	}	 
	
	
	 
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks) 
	
	private Integer id;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks) 
	
	private String name;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks) 
	
	private String title;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	
	private EnumIntA enumIntA;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	
	private EnumStringA enumStringA;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks) 
	
	private int[] arrayInt;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks) 
	
	private String[] arrayString;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks) 
	
	private JsonObject json;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks) 
	
	private ColumnData obj;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks) 
	
	private java.util.Date tsA;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks) 
	
	private java.util.Date createTime;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks) 
	
	private String createBy;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	
	private java.util.Date updateTime;	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	
	private String updateBy;	
	
	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks) 
	public TestTable2 setId(Integer id){
		
		this.id = id;
		  
		
		fieldChanged("id");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks) 
	public TestTable2 setName(String name){
		
		this.name = name;
		  
		
		fieldChanged("name");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks) 
	public TestTable2 setTitle(String title){
		
		this.title = title;
		  
		
		fieldChanged("title");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public TestTable2 setEnumIntA(EnumIntA enumIntA){
		
		this.enumIntA = enumIntA;
		  
		
		fieldChanged("enumIntA");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public TestTable2 setEnumStringA(EnumStringA enumStringA){
		
		this.enumStringA = enumStringA;
		  
		
		fieldChanged("enumStringA");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks) 
	public TestTable2 setArrayInt(int[] arrayInt){
		
		this.arrayInt = arrayInt;
		  
		
		fieldChanged("arrayInt");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks) 
	public TestTable2 setArrayString(String[] arrayString){
		
		this.arrayString = arrayString;
		  
		
		fieldChanged("arrayString");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks) 
	public TestTable2 setJson(JsonObject json){
		
		this.json = json;
		  
		
		fieldChanged("json");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks) 
	public TestTable2 setObj(ColumnData obj){
		
		this.obj = obj;
		  
		
		fieldChanged("obj");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks) 
	public TestTable2 setTsA(java.util.Date tsA){
		
		this.tsA = tsA;
		  
		
		fieldChanged("tsA");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks) 
	public TestTable2 setCreateTime(java.util.Date createTime){
		
		this.createTime = createTime;
		  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks) 
	public TestTable2 setCreateBy(String createBy){
		
		this.createBy = createBy;
		  
		
		fieldChanged("createBy");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public TestTable2 setUpdateTime(java.util.Date updateTime){
		
		this.updateTime = updateTime;
		  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public TestTable2 setUpdateBy(String updateBy){
		
		this.updateBy = updateBy;
		  
		
		fieldChanged("updateBy");
		
		return this;
	}
	
	
	
	
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks) 
	public Integer getId(){
		return this.id;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
* @param defaultValue  Return the default value if id is null.*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks) 
	public Integer getId(Integer defaultValue){
		Integer r=this.getId();
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
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks) 
	public String getName(){
		return this.name;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
* @param defaultValue  Return the default value if name is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks) 
	public String getName(String defaultValue){
		String r=this.getName();
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
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks) 
	public String getTitle(){
		return this.title;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
* @param defaultValue  Return the default value if title is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks) 
	public String getTitle(String defaultValue){
		String r=this.getTitle();
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
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(){
		return this.enumIntA;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
* @param defaultValue  Return the default value if enumIntA is null.*/
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(EnumIntA defaultValue){
		EnumIntA r=this.getEnumIntA();
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
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public EnumStringA getEnumStringA(){
		return this.enumStringA;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
* @param defaultValue  Return the default value if enumStringA is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public EnumStringA getEnumStringA(EnumStringA defaultValue){
		EnumStringA r=this.getEnumStringA();
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
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks) 
	public int[] getArrayInt(){
		return this.arrayInt;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
* @param defaultValue  Return the default value if arrayInt is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks) 
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
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks) 
	public String[] getArrayString(){
		return this.arrayString;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
* @param defaultValue  Return the default value if arrayString is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks) 
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
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks) 
	public JsonObject getJson(){
		return this.json;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
* @param defaultValue  Return the default value if json is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks) 
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
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks) 
	public ColumnData getObj(){
		return this.obj;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
* @param defaultValue  Return the default value if obj is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks) 
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
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(){
		return this.tsA;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if tsA is null.*/
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(java.util.Date defaultValue){
		java.util.Date r=this.getTsA();
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
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if createTime is null.*/
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(java.util.Date defaultValue){
		java.util.Date r=this.getCreateTime();
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
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(){
		return this.createBy;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if createBy is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(String defaultValue){
		String r=this.getCreateBy();
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
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if updateTime is null.*/
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(java.util.Date defaultValue){
		java.util.Date r=this.getUpdateTime();
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
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(){
		return this.updateBy;
 
	}
	
	/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
* @param defaultValue  Return the default value if updateBy is null.*/
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(String defaultValue){
		String r=this.getUpdateBy();
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	public static class Insert extends com.tsc9526.monalisa.core.query.dao.Insert<TestTable2>{
		Insert(TestTable2 model){
			super(model);
		}	 
	}	
	
	public static class Delete extends com.tsc9526.monalisa.core.query.dao.Delete<TestTable2>{
		Delete(TestTable2 model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(Integer id){
			
			if(id ==null ) return 0;			
			
						 			 
			
			this.model.id = id;
			
				 			 
			return this.model.delete();				
		}				 
		
		
		 
		
		
	}
	
	public static class Update extends com.tsc9526.monalisa.core.query.dao.Update<TestTable2>{
		Update(TestTable2 model){
			super(model);
		}		 			 			 		
	}
	
	public static class Select extends com.tsc9526.monalisa.core.query.dao.Select<TestTable2,Select>{		
		Select(TestTable2 x){
			super(x);
		}					 
		
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public TestTable2 selectByPrimaryKey(Integer id){
			
			if(id ==null ) return null;			
			
						
			
			this.model.id = id;
			
			this.model.load();
				 			 	 
			if(this.model.entity()){
				return this.model;
			}else{
				return null;
			}
		}				 
		
		
		
			
				
		/**
		* List result to Map, The map key is primary-key:  id
		*/
		public Map<Integer,TestTable2> selectToMap(String whereStatement,Object ... args){
			List<TestTable2> list=super.select(whereStatement,args);
			
			Map<Integer,TestTable2> m=new LinkedHashMap<Integer,TestTable2>();
			for(TestTable2 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,TestTable2> selectByExampleToMap(Example example){
			List<TestTable2> list=super.selectByExample(example);
			
			Map<Integer,TestTable2> m=new LinkedHashMap<Integer,TestTable2>();
			for(TestTable2 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
	}
	 
		
	public static class Example extends com.tsc9526.monalisa.core.query.criteria.Example<Criteria,TestTable2>{
		public Example(){}
		 
		protected Criteria createInternal(){
			Criteria x= new Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=ClassHelper.findClassWithAnnotation(TestTable2.class,DB.class);	  			
			com.tsc9526.monalisa.core.query.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,TestTable2> selectToMap(){			
			List<TestTable2> list=SELECT().selectByExample(this);
			
			Map<Integer,TestTable2> m=new LinkedHashMap<Integer,TestTable2>();
			for(TestTable2 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
	}
	
	public static class Criteria extends com.tsc9526.monalisa.core.query.criteria.Criteria<Criteria>{
		
		private Example example;
		
		private Criteria(Example example){
			this.example=example;
		}
		
		/**
		 * Create Select for example
		 */
		public Select.SelectForExample SELECT(){
			return TestTable2.SELECT().selectForExample(this.example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(TestTable2 m){			 
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
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria> id = new com.tsc9526.monalisa.core.query.criteria.Field.FieldInteger<Criteria>("id", this);
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> name = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("name", this);
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> title = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("title", this);
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<EnumIntA,Criteria> enumIntA = new com.tsc9526.monalisa.core.query.criteria.Field<EnumIntA,Criteria>("enum_int_a", this, 4);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<EnumStringA,Criteria> enumStringA = new com.tsc9526.monalisa.core.query.criteria.Field<EnumStringA,Criteria>("enum_string_a", this, 12);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<int[],Criteria> arrayInt = new com.tsc9526.monalisa.core.query.criteria.Field<int[],Criteria>("array_int", this, 12);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<String[],Criteria> arrayString = new com.tsc9526.monalisa.core.query.criteria.Field<String[],Criteria>("array_string", this, 12);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<JsonObject,Criteria> json = new com.tsc9526.monalisa.core.query.criteria.Field<JsonObject,Criteria>("json", this, 12);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<ColumnData,Criteria> obj = new com.tsc9526.monalisa.core.query.criteria.Field<ColumnData,Criteria>("obj", this, 12);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria> tsA = new com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria>("ts_a", this, 93);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria> createTime = new com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria>("create_time", this, 93);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> createBy = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("create_by", this);
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria> updateTime = new com.tsc9526.monalisa.core.query.criteria.Field<java.util.Date,Criteria>("update_time", this, 93);		 
				
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks)
		
		public com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria> updateBy = new com.tsc9526.monalisa.core.query.criteria.Field.FieldString<Criteria>("update_by", this);
				
		
	}
	 
	
		 
	
		 
	
		 
	
			
			public static enum EnumIntA{V0,V1}
		 
	
			
			public static enum EnumStringA{ TRUE, FALSE}
		 
	
		 
	
		 
	
		 
	
		 
	
		 
	
		 
	
		 
	
		 
	
		 
	
	 
	public static class M{
		public final static String TABLE ="test_table_2" ;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
* <li><B>remarks:</B> 唯一主键
*/
@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, notnull=M.id$notnull, length=M.id$length, value=M.id$value, remarks=M.id$remarks)
		public final static String  id         = "id" ;
		
		public final static String  id$name    = "id" ;
		public final static boolean id$key     = true;
		public final static int     id$length  = 10;
		public final static String  id$value   = "NULL" ;
		public final static String  id$remarks = "唯一主键" ;
		public final static boolean id$auto    = true ;
		public final static boolean id$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
* <li><B>remarks:</B> 名称
*/
@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, notnull=M.name$notnull, length=M.name$length, value=M.name$value, remarks=M.name$remarks)
		public final static String  name         = "name" ;
		
		public final static String  name$name    = "name" ;
		public final static boolean name$key     = false;
		public final static int     name$length  = 128;
		public final static String  name$value   = "N0001" ;
		public final static String  name$remarks = "名称" ;
		public final static boolean name$auto    = false ;
		public final static boolean name$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> title
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
* <li><B>remarks:</B> 标题
*/
@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, notnull=M.title$notnull, length=M.title$length, value=M.title$value, remarks=M.title$remarks)
		public final static String  title         = "title" ;
		
		public final static String  title$name    = "title" ;
		public final static boolean title$key     = false;
		public final static int     title$length  = 128;
		public final static String  title$value   = "NULL" ;
		public final static String  title$remarks = "标题" ;
		public final static boolean title$auto    = false ;
		public final static boolean title$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
* <li><B>remarks:</B> 枚举字段A  #enum{{V0,V1}}
*/
@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, notnull=M.enumIntA$notnull, length=M.enumIntA$length, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		public final static String  enumIntA         = "enum_int_a" ;
		
		public final static String  enumIntA$name    = "enum_int_a" ;
		public final static boolean enumIntA$key     = false;
		public final static int     enumIntA$length  = 10;
		public final static String  enumIntA$value   = "0" ;
		public final static String  enumIntA$remarks = "枚举字段A  #enum{{V0,V1}}" ;
		public final static boolean enumIntA$auto    = false ;
		public final static boolean enumIntA$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, notnull=M.enumStringA$notnull, length=M.enumStringA$length, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		public final static String  enumStringA         = "enum_string_a" ;
		
		public final static String  enumStringA$name    = "enum_string_a" ;
		public final static boolean enumStringA$key     = false;
		public final static int     enumStringA$length  = 64;
		public final static String  enumStringA$value   = "TRUE" ;
		public final static String  enumStringA$remarks = "#enum{{ TRUE, FALSE}}" ;
		public final static boolean enumStringA$auto    = false ;
		public final static boolean enumStringA$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_int
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 整形数组 #array{int}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayInt$name, key=M.arrayInt$key, auto=M.arrayInt$auto, notnull=M.arrayInt$notnull, length=M.arrayInt$length, value=M.arrayInt$value, remarks=M.arrayInt$remarks)
		public final static String  arrayInt         = "array_int" ;
		
		public final static String  arrayInt$name    = "array_int" ;
		public final static boolean arrayInt$key     = false;
		public final static int     arrayInt$length  = 256;
		public final static String  arrayInt$value   = "NULL" ;
		public final static String  arrayInt$remarks = "整形数组 #array{int}" ;
		public final static boolean arrayInt$auto    = false ;
		public final static boolean arrayInt$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> array_string
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 256<br>
* <li><B>remarks:</B> 字符串数组 #array{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.arrayString$name, key=M.arrayString$key, auto=M.arrayString$auto, notnull=M.arrayString$notnull, length=M.arrayString$length, value=M.arrayString$value, remarks=M.arrayString$remarks)
		public final static String  arrayString         = "array_string" ;
		
		public final static String  arrayString$name    = "array_string" ;
		public final static boolean arrayString$key     = false;
		public final static int     arrayString$length  = 256;
		public final static String  arrayString$value   = "NULL" ;
		public final static String  arrayString$remarks = "字符串数组 #array{}" ;
		public final static boolean arrayString$auto    = false ;
		public final static boolean arrayString$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> json
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Json #json{}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.json$name, key=M.json$key, auto=M.json$auto, notnull=M.json$notnull, length=M.json$length, value=M.json$value, remarks=M.json$remarks)
		public final static String  json         = "json" ;
		
		public final static String  json$name    = "json" ;
		public final static boolean json$key     = false;
		public final static int     json$length  = 1024;
		public final static String  json$value   = "NULL" ;
		public final static String  json$remarks = "Json #json{}" ;
		public final static boolean json$auto    = false ;
		public final static boolean json$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> obj
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
* <li><B>remarks:</B> Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}
*/
@Column(table=M.TABLE, jdbcType=12, name=M.obj$name, key=M.obj$key, auto=M.obj$auto, notnull=M.obj$notnull, length=M.obj$length, value=M.obj$value, remarks=M.obj$remarks)
		public final static String  obj         = "obj" ;
		
		public final static String  obj$name    = "obj" ;
		public final static boolean obj$key     = false;
		public final static int     obj$length  = 1024;
		public final static String  obj$value   = "NULL" ;
		public final static String  obj$remarks = "Object #json{test.com.tsc9526.monalisa.core.data.ColumnData}" ;
		public final static boolean obj$auto    = false ;
		public final static boolean obj$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> ts_a &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, notnull=M.tsA$notnull, length=M.tsA$length, value=M.tsA$value, remarks=M.tsA$remarks)
		public final static String  tsA         = "ts_a" ;
		
		public final static String  tsA$name    = "ts_a" ;
		public final static boolean tsA$key     = false;
		public final static int     tsA$length  = 19;
		public final static String  tsA$value   = "NULL" ;
		public final static String  tsA$remarks = "" ;
		public final static boolean tsA$auto    = false ;
		public final static boolean tsA$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, notnull=M.createTime$notnull, length=M.createTime$length, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime         = "create_time" ;
		
		public final static String  createTime$name    = "create_time" ;
		public final static boolean createTime$key     = false;
		public final static int     createTime$length  = 19;
		public final static String  createTime$value   = "NULL" ;
		public final static String  createTime$remarks = "" ;
		public final static boolean createTime$auto    = false ;
		public final static boolean createTime$notnull = true;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> create_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, notnull=M.createBy$notnull, length=M.createBy$length, value=M.createBy$value, remarks=M.createBy$remarks)
		public final static String  createBy         = "create_by" ;
		
		public final static String  createBy$name    = "create_by" ;
		public final static boolean createBy$key     = false;
		public final static int     createBy$length  = 64;
		public final static String  createBy$value   = "NULL" ;
		public final static String  createBy$remarks = "" ;
		public final static boolean createBy$auto    = false ;
		public final static boolean createBy$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_time
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, notnull=M.updateTime$notnull, length=M.updateTime$length, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime         = "update_time" ;
		
		public final static String  updateTime$name    = "update_time" ;
		public final static boolean updateTime$key     = false;
		public final static int     updateTime$length  = 19;
		public final static String  updateTime$value   = "NULL" ;
		public final static String  updateTime$remarks = "" ;
		public final static boolean updateTime$auto    = false ;
		public final static boolean updateTime$notnull = false;
		
		
		/**
* @Column
* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_table_2&nbsp;<B>name:</B> update_by
* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
* <li><B>remarks:</B> 
*/
@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, notnull=M.updateBy$notnull, length=M.updateBy$length, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public final static String  updateBy         = "update_by" ;
		
		public final static String  updateBy$name    = "update_by" ;
		public final static boolean updateBy$key     = false;
		public final static int     updateBy$length  = 64;
		public final static String  updateBy$value   = "NULL" ;
		public final static String  updateBy$remarks = "" ;
		public final static boolean updateBy$auto    = false ;
		public final static boolean updateBy$notnull = false;
		
				 
	}
}


