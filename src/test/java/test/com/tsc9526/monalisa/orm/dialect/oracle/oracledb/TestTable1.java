package test.com.tsc9526.monalisa.orm.dialect.oracle.oracledb;
 		

import com.tsc9526.monalisa.orm.annotation.DB; 
import com.tsc9526.monalisa.orm.annotation.Table; 
import com.tsc9526.monalisa.orm.annotation.Column; 
import com.tsc9526.monalisa.tools.annotation.Alias; 
import com.tsc9526.monalisa.orm.annotation.Index; 
import com.tsc9526.monalisa.tools.clazz.MelpClass; 
import java.util.List; 
import java.util.Map; 
import java.util.LinkedHashMap; 
 
/**
 *
 * Auto generated code by monalisa 2.1.0
 *
 */
@Table(
	name="TEST_TABLE_1",
	primaryKeys={"ID"},
	remarks="测试表",
	indexes={
		  @Index(name="PK_SEQ_TEST_TABLE_1", type=1, unique=true, fields={"ID"}) 
		, @Index(name="UK_NAME_TITLE", type=1, unique=true, fields={"NAME", "TITLE"}) 
	}
)
public class TestTable1 extends com.tsc9526.monalisa.orm.model.Model<TestTable1> implements test.com.tsc9526.monalisa.orm.dialect.oracle.OracleDB{
	private static final long serialVersionUID = 1367167607640L;
		 
	public static final $Insert INSERT(){
	 	return new $Insert(new TestTable1());
	}
	
	public static final $Delete DELETE(){
	 	return new $Delete(new TestTable1());
	}
	
	public static final $Update UPDATE(TestTable1 model){
		return new $Update(model);
	}		
	
	public static final $Select SELECT(){
	 	return new $Select(new TestTable1());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static $Criteria WHERE(){
		return new $Example().createCriteria();
	}
	
	/**
	 * name: <b>TEST_TABLE_1</b> <br>
	 * primaryKeys: "ID" <br>
	 * remarks: 测试表
	 */ 
	public TestTable1(){
		super("TEST_TABLE_1", "ID");		
	}		 
	
	
	/**
	 * Constructor use primary keys.<br><br>
	 * name: <b>TEST_TABLE_1</b> <br>
	 * primaryKeys: "ID" <br>
	 * remarks: 测试表<br><br>
	 *
	 * @param id  primary key	 
	 */
	public TestTable1(Long id){
		super("TEST_TABLE_1", "ID");
		
		this.id = id;
		fieldChanged("id");
		
	}	 
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
	@Alias("ID")
	private Long id;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
	@Alias("NAME")
	private String name;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
	@Alias("TITLE")
	private String title;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
	@Alias("ENUM_INT_A")
	private EnumIntA enumIntA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
	@Alias("ENUM_STRING_A")
	private EnumStringA enumStringA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
	@Alias("TS_A")
	private java.util.Date tsA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
	@Alias("CREATE_TIME")
	private java.util.Date createTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
	@Alias("CREATE_BY")
	private String createBy;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
	@Alias("UPDATE_TIME")
	private java.util.Date updateTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
	@Alias("UPDATE_BY")
	private String updateBy;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
	@Alias("VERSION")
	private Integer version;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks)
	@Alias("NUMBER_V1")
	private java.math.BigDecimal numberV1;	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public TestTable1 setId(Long id){
		this.id = id;  
		
		fieldChanged("id");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public TestTable1 setName(String name){
		this.name = name;  
		
		fieldChanged("name");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks) 
	public TestTable1 setTitle(String title){
		this.title = title;  
		
		fieldChanged("title");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public TestTable1 setEnumIntA(EnumIntA enumIntA){
		this.enumIntA = enumIntA;  
		
		fieldChanged("enumIntA");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public TestTable1 setEnumStringA(EnumStringA enumStringA){
		this.enumStringA = enumStringA;  
		
		fieldChanged("enumStringA");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public TestTable1 setTsA(java.util.Date tsA){
		this.tsA = tsA;  
		
		fieldChanged("tsA");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public TestTable1 setCreateTime(java.util.Date createTime){
		this.createTime = createTime;  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public TestTable1 setCreateBy(String createBy){
		this.createBy = createBy;  
		
		fieldChanged("createBy");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public TestTable1 setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public TestTable1 setUpdateBy(String updateBy){
		this.updateBy = updateBy;  
		
		fieldChanged("updateBy");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public TestTable1 setVersion(Integer version){
		this.version = version;  
		
		fieldChanged("version");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks) 
	public TestTable1 setNumberV1(java.math.BigDecimal numberV1){
		this.numberV1 = numberV1;  
		
		fieldChanged("numberV1");
		
		return this;
	}
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Long getId(){
		return this.id;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id is null.
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Long getId(Long defaultValue){
		Long r=this.getId();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public String getName(){
		return this.name;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
	* <li><B>remarks:</B> the name
	* @param defaultValue  Return the default value if name is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public String getName(String defaultValue){
		String r=this.getName();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks) 
	public String getTitle(){
		return this.title;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* @param defaultValue  Return the default value if title is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks) 
	public String getTitle(String defaultValue){
		String r=this.getTitle();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(){
		return this.enumIntA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
	* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
	* @param defaultValue  Return the default value if enumIntA is null.
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(EnumIntA defaultValue){
		EnumIntA r=this.getEnumIntA();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public EnumStringA getEnumStringA(){
		return this.enumStringA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
	* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
	* @param defaultValue  Return the default value if enumStringA is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public EnumStringA getEnumStringA(EnumStringA defaultValue){
		EnumStringA r=this.getEnumStringA();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(){
		return this.tsA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	* @param defaultValue  Return the default value if tsA is null.
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(java.util.Date defaultValue){
		java.util.Date r=this.getTsA();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	* @param defaultValue  Return the default value if createTime is null.
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(java.util.Date defaultValue){
		java.util.Date r=this.getCreateTime();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(){
		return this.createBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* @param defaultValue  Return the default value if createBy is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(String defaultValue){
		String r=this.getCreateBy();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
	* @param defaultValue  Return the default value if updateTime is null.
	*/
	@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(java.util.Date defaultValue){
		java.util.Date r=this.getUpdateTime();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(){
		return this.updateBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* @param defaultValue  Return the default value if updateBy is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(String defaultValue){
		String r=this.getUpdateBy();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public Integer getVersion(){
		return this.version;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
	* @param defaultValue  Return the default value if version is null.
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public Integer getVersion(Integer defaultValue){
		Integer r=this.getVersion();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks) 
	public java.math.BigDecimal getNumberV1(){
		return this.numberV1;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* @param defaultValue  Return the default value if numberV1 is null.
	*/
	@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks) 
	public java.math.BigDecimal getNumberV1(java.math.BigDecimal defaultValue){
		java.math.BigDecimal r=this.getNumberV1();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	
	 
	public static class $Insert extends com.tsc9526.monalisa.orm.dao.Insert<TestTable1>{
		$Insert(TestTable1 model){
			super(model);
		}	 
	}	
	
	public static class $Delete extends com.tsc9526.monalisa.orm.dao.Delete<TestTable1>{
		$Delete(TestTable1 model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(Long id){
			if(id ==null ) return 0;	
			
			this.model.id = id;
			
			return this.model.delete();				
		}
		
		
		/**
		* Delete by unique key: PK_SEQ_TEST_TABLE_1
		* @param id primary key	
		*/
		public int deleteById(Long id){			 
			this.model.id=id;
						 
			 
			return this.model.delete();
		}			 
		 
		
		/**
		* Delete by unique key: UK_NAME_TITLE
		* @param name the name
		* @param title 	
		*/
		public int deleteByNameTitle(String name, String title){			 
			this.model.name=name;
			this.model.title=title;
						 
			 
			return this.model.delete();
		}			 
		 
	}
	
	public static class $Update extends com.tsc9526.monalisa.orm.dao.Update<TestTable1>{
		$Update(TestTable1 model){
			super(model);
		}		 			 			 		
	}
	
	public static class $Select extends com.tsc9526.monalisa.orm.dao.Select<TestTable1,$Select>{		
		$Select(TestTable1 x){
			super(x);
		}	
						 
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public TestTable1 selectByPrimaryKey(Long id){
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
		* Find by unique key: PK_SEQ_TEST_TABLE_1
		* @param id primary key	
		*/
		public TestTable1 selectById(Long id){	
			$Criteria c=WHERE();
			
			c.id.eq(id);			 
			 
			return super.selectByKeyExample(c.$example);
		}			 
		
		/**
		* List result to Map, The map key is unique-key: ID 
		*/
		public Map<Long,TestTable1> selectToMapWithId(String whereStatement,Object ... args){
			List<TestTable1> list=super.select(whereStatement,args);
			
			Map<Long,TestTable1> m=new LinkedHashMap<Long,TestTable1>();
			for(TestTable1 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		/**
		* List result to Map, The map key is unique-key: id 
		*/
		public Map<Long,TestTable1> selectByExampleToMapWithId($Example example){
			List<TestTable1> list=super.selectByExample(example);
			
			Map<Long,TestTable1> m=new LinkedHashMap<Long,TestTable1>();
			for(TestTable1 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
		/**
		* Find by unique key: UK_NAME_TITLE
		* @param name the name
		* @param title 	
		*/
		public TestTable1 selectByNameTitle(String name, String title){	
			$Criteria c=WHERE();
			
			c.name.eq(name);
			c.title.eq(title);			 
			 
			return super.selectByKeyExample(c.$example);
		}			 
		
				
		/**
		* List result to Map, The map key is primary-key:  id
		*/
		public Map<Long,TestTable1> selectToMap(String whereStatement,Object ... args){
			List<TestTable1> list=super.select(whereStatement,args);
			
			Map<Long,TestTable1> m=new LinkedHashMap<Long,TestTable1>();
			for(TestTable1 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Long,TestTable1> selectByExampleToMap($Example example){
			List<TestTable1> list=super.selectByExample(example);
			
			Map<Long,TestTable1> m=new LinkedHashMap<Long,TestTable1>();
			for(TestTable1 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
		
		public $SelectForExample selectForExample($Example example){
			return new $SelectForExample(example);
		} 	
		
		public class $SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<TestTable1,$Select>.$SelectForExample{
			public $SelectForExample($Example example) {
				super(example); 
			}
			
			
			/**
			* List result to Map, The map key is unique-key: id 
			*/
			public Map<Long,TestTable1> selectToMapWithId(){
				return selectByExampleToMapWithId(($Example)this.example);
			}
			
					
			/**
			* List result to Map, The map key is primary-key:  id
			*/
			public Map<Long,TestTable1> selectToMap(){
				return selectByExampleToMap(($Example)this.example);
			}
			
		}
			
	}
	 
		
	public static class $Example extends com.tsc9526.monalisa.orm.criteria.Example<$Criteria,TestTable1>{
		public $Example(){}
		 
		protected $Criteria createInternal(){
			$Criteria x= new $Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=MelpClass.findClassWithAnnotation(TestTable1.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Long,TestTable1> selectToMap(){			
			List<TestTable1> list=SELECT().selectByExample(this);
			
			Map<Long,TestTable1> m=new LinkedHashMap<Long,TestTable1>();
			for(TestTable1 x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
	}
	
	public static class $Criteria extends com.tsc9526.monalisa.orm.criteria.Criteria<$Criteria>{
		
		private $Example $example;
		
		private $Criteria($Example example){
			this.$example=example;
		}
		
		/**
		 * Create Select for example
		 */
		public $Select.$SelectForExample SELECT(){
			return TestTable1.SELECT().selectForExample(this.$example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(TestTable1 m){			 
			return UPDATE(m).updateByExample(this.$example);
		}
				
		/**
		* Delete records with this example
		*/		
		public int delete(){
			return DELETE().deleteByExample(this.$example);
		}
		
		/**
		* Append "OR" Criteria  
		*/	
		public $Criteria OR(){
			return this.$example.or();
		}
		
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
		* <li><B>remarks:</B> primary key
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldLong<$Criteria> id = new com.tsc9526.monalisa.orm.criteria.Field.FieldLong<$Criteria>("ID", this); 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
		* <li><B>remarks:</B> the name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> name = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("NAME", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> title = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("TITLE", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
		* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<EnumIntA,$Criteria> enumIntA = new com.tsc9526.monalisa.orm.criteria.Field<EnumIntA,$Criteria>("ENUM_INT_A", this, 3);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
		* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<EnumStringA,$Criteria> enumStringA = new com.tsc9526.monalisa.orm.criteria.Field<EnumStringA,$Criteria>("ENUM_STRING_A", this, 12);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> tsA = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("TS_A", this, 91);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> createTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("CREATE_TIME", this, 91);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> createBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("CREATE_BY", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> updateTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("UPDATE_TIME", this, 91);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> updateBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("UPDATE_BY", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> version = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("VERSION", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.math.BigDecimal,$Criteria> numberV1 = new com.tsc9526.monalisa.orm.criteria.Field<java.math.BigDecimal,$Criteria>("NUMBER_V1", this, 3);		 
			
	}
	 
			
	public static enum EnumIntA{V0,V1}
			
	public static enum EnumStringA{ TRUE, FALSE}
	
	
	  
	/**
	* Meta info about table: TEST_TABLE_1
	*/ 
	public static class M{
		public final static String TABLE ="TEST_TABLE_1";
	 	
	 	
		public final static String  id$name          = "ID";
		public final static boolean id$key           = true;
		public final static int     id$length        = 22;
		public final static int     id$decimalDigits = 0;
		public final static String  id$value         = "NULL";
		public final static String  id$remarks       = "primary key";
		public final static boolean id$auto          = true;
		public final static boolean id$notnull       = true;
		public final static String  id$seq           = "SEQ_TEST_TABLE_1";
		
		
		public final static String  name$name          = "NAME";
		public final static boolean name$key           = false;
		public final static int     name$length        = 128;
		public final static int     name$decimalDigits = 0;
		public final static String  name$value         = "N0001";
		public final static String  name$remarks       = "the name";
		public final static boolean name$auto          = false;
		public final static boolean name$notnull       = true;
		public final static String  name$seq           = "";
		
		
		public final static String  title$name          = "TITLE";
		public final static boolean title$key           = false;
		public final static int     title$length        = 128;
		public final static int     title$decimalDigits = 0;
		public final static String  title$value         = "NULL";
		public final static String  title$remarks       = "";
		public final static boolean title$auto          = false;
		public final static boolean title$notnull       = false;
		public final static String  title$seq           = "";
		
		
		public final static String  enumIntA$name          = "ENUM_INT_A";
		public final static boolean enumIntA$key           = false;
		public final static int     enumIntA$length        = 22;
		public final static int     enumIntA$decimalDigits = 0;
		public final static String  enumIntA$value         = "0";
		public final static String  enumIntA$remarks       = "enum fields A  #enum{{V0,V1}}";
		public final static boolean enumIntA$auto          = false;
		public final static boolean enumIntA$notnull       = true;
		public final static String  enumIntA$seq           = "";
		
		
		public final static String  enumStringA$name          = "ENUM_STRING_A";
		public final static boolean enumStringA$key           = false;
		public final static int     enumStringA$length        = 64;
		public final static int     enumStringA$decimalDigits = 0;
		public final static String  enumStringA$value         = "TRUE";
		public final static String  enumStringA$remarks       = "#enum{{ TRUE, FALSE}}";
		public final static boolean enumStringA$auto          = false;
		public final static boolean enumStringA$notnull       = true;
		public final static String  enumStringA$seq           = "";
		
		
		public final static String  tsA$name          = "TS_A";
		public final static boolean tsA$key           = false;
		public final static int     tsA$length        = 7;
		public final static int     tsA$decimalDigits = 0;
		public final static String  tsA$value         = "NULL";
		public final static String  tsA$remarks       = "";
		public final static boolean tsA$auto          = false;
		public final static boolean tsA$notnull       = true;
		public final static String  tsA$seq           = "";
		
		
		public final static String  createTime$name          = "CREATE_TIME";
		public final static boolean createTime$key           = false;
		public final static int     createTime$length        = 7;
		public final static int     createTime$decimalDigits = 0;
		public final static String  createTime$value         = "NULL";
		public final static String  createTime$remarks       = "";
		public final static boolean createTime$auto          = false;
		public final static boolean createTime$notnull       = true;
		public final static String  createTime$seq           = "";
		
		
		public final static String  createBy$name          = "CREATE_BY";
		public final static boolean createBy$key           = false;
		public final static int     createBy$length        = 64;
		public final static int     createBy$decimalDigits = 0;
		public final static String  createBy$value         = "NULL";
		public final static String  createBy$remarks       = "";
		public final static boolean createBy$auto          = false;
		public final static boolean createBy$notnull       = false;
		public final static String  createBy$seq           = "";
		
		
		public final static String  updateTime$name          = "UPDATE_TIME";
		public final static boolean updateTime$key           = false;
		public final static int     updateTime$length        = 7;
		public final static int     updateTime$decimalDigits = 0;
		public final static String  updateTime$value         = "NULL";
		public final static String  updateTime$remarks       = "";
		public final static boolean updateTime$auto          = false;
		public final static boolean updateTime$notnull       = false;
		public final static String  updateTime$seq           = "";
		
		
		public final static String  updateBy$name          = "UPDATE_BY";
		public final static boolean updateBy$key           = false;
		public final static int     updateBy$length        = 64;
		public final static int     updateBy$decimalDigits = 0;
		public final static String  updateBy$value         = "NULL";
		public final static String  updateBy$remarks       = "";
		public final static boolean updateBy$auto          = false;
		public final static boolean updateBy$notnull       = false;
		public final static String  updateBy$seq           = "";
		
		
		public final static String  version$name          = "VERSION";
		public final static boolean version$key           = false;
		public final static int     version$length        = 11;
		public final static int     version$decimalDigits = 0;
		public final static String  version$value         = "0";
		public final static String  version$remarks       = "";
		public final static boolean version$auto          = false;
		public final static boolean version$notnull       = true;
		public final static String  version$seq           = "";
		
		
		public final static String  numberV1$name          = "NUMBER_V1";
		public final static boolean numberV1$key           = false;
		public final static int     numberV1$length        = 10;
		public final static int     numberV1$decimalDigits = 2;
		public final static String  numberV1$value         = "NULL";
		public final static String  numberV1$remarks       = "";
		public final static boolean numberV1$auto          = false;
		public final static boolean numberV1$notnull       = false;
		public final static String  numberV1$seq           = "";
		
			
		
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ID &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22<br>
		* <li><B>remarks:</B> primary key
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public final static String  id                     = "ID";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NAME &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001<br>
		* <li><B>remarks:</B> the name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public final static String  name                     = "NAME";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TITLE
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
		public final static String  title                     = "TITLE";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_INT_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 22 &nbsp;<B>value:</B> 0<br>
		* <li><B>remarks:</B> enum fields A  #enum{{V0,V1}}
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		public final static String  enumIntA                     = "ENUM_INT_A";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> ENUM_STRING_A &nbsp;[<font color=red>NOTNULL</font>|<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64 &nbsp;<B>value:</B> TRUE<br>
		* <li><B>remarks:</B> #enum{{ TRUE, FALSE}}
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		public final static String  enumStringA                     = "ENUM_STRING_A";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> TS_A &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
		public final static String  tsA                     = "TS_A";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_TIME &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime                     = "CREATE_TIME";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> CREATE_BY
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public final static String  createBy                     = "CREATE_BY";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_TIME
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 7<br>
		*/
		@Column(table=M.TABLE, jdbcType=91, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime                     = "UPDATE_TIME";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> UPDATE_BY
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public final static String  updateBy                     = "UPDATE_BY";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> VERSION &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 11 &nbsp;<B>value:</B> 0<br>
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
		public final static String  version                     = "VERSION";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> TEST_TABLE_1&nbsp;<B>name:</B> NUMBER_V1
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		*/
		@Column(table=M.TABLE, jdbcType=3, name=M.numberV1$name, key=M.numberV1$key, auto=M.numberV1$auto, seq=M.numberV1$seq, notnull=M.numberV1$notnull, length=M.numberV1$length, decimalDigits=M.numberV1$decimalDigits, value=M.numberV1$value, remarks=M.numberV1$remarks)
		public final static String  numberV1                     = "NUMBER_V1";
		 
	}
	
}


