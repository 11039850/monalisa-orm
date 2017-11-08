package test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb;
 		

import com.tsc9526.monalisa.orm.annotation.DB; 
import com.tsc9526.monalisa.orm.annotation.Table; 
import com.tsc9526.monalisa.orm.annotation.Column; 
import com.tsc9526.monalisa.tools.annotation.Alias; 
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
	name="test_logyyyymm_",
	primaryKeys={"id"},
	remarks="",
	indexes={
	}
)
public class TestLogyyyymm extends com.tsc9526.monalisa.orm.model.Model<TestLogyyyymm> implements test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB{
	private static final long serialVersionUID = 1155425976213L;
		 
	public static final $Insert INSERT(){
	 	return new $Insert(new TestLogyyyymm());
	}
	
	public static final $Delete DELETE(){
	 	return new $Delete(new TestLogyyyymm());
	}
	
	public static final $Update UPDATE(TestLogyyyymm model){
		return new $Update(model);
	}		
	
	public static final $Select SELECT(){
	 	return new $Select(new TestLogyyyymm());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static $Criteria WHERE(){
		return new $Example().createCriteria();
	}
	
	/**
	 * name: <b>test_logyyyymm_</b> <br>
	 * primaryKeys: "id" <br>
	 * remarks: 
	 */ 
	public TestLogyyyymm(){
		super("test_logyyyymm_", "id");		
	}		 
	
	
	/**
	 * Constructor use primary keys.<br><br>
	 * name: <b>test_logyyyymm_</b> <br>
	 * primaryKeys: "id" <br>
	 * remarks: <br><br>
	 *
	 * @param id  primary key	 
	 */
	public TestLogyyyymm(Integer id){
		super("test_logyyyymm_", "id");
		
		this.id = id;
		fieldChanged("id");
		
	}	 
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
	private Integer id;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks)
	@Alias("log_time")
	private java.util.Date logTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
	private String name;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
	@Alias("enum_int_a")
	private EnumIntA enumIntA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
	@Alias("enum_string_a")
	private EnumStringA enumStringA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
	@Alias("create_time")
	private java.util.Date createTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
	@Alias("create_by")
	private String createBy;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
	@Alias("update_time")
	private java.util.Date updateTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
	@Alias("update_by")
	private String updateBy;	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public TestLogyyyymm setId(Integer id){
		this.id = id;  
		
		fieldChanged("id");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks) 
	public TestLogyyyymm setLogTime(java.util.Date logTime){
		this.logTime = logTime;  
		
		fieldChanged("logTime");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public TestLogyyyymm setName(String name){
		this.name = name;  
		
		fieldChanged("name");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public TestLogyyyymm setEnumIntA(EnumIntA enumIntA){
		this.enumIntA = enumIntA;  
		
		fieldChanged("enumIntA");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public TestLogyyyymm setEnumStringA(EnumStringA enumStringA){
		this.enumStringA = enumStringA;  
		
		fieldChanged("enumStringA");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public TestLogyyyymm setCreateTime(java.util.Date createTime){
		this.createTime = createTime;  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public TestLogyyyymm setCreateBy(String createBy){
		this.createBy = createBy;  
		
		fieldChanged("createBy");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public TestLogyyyymm setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public TestLogyyyymm setUpdateBy(String updateBy){
		this.updateBy = updateBy;  
		
		fieldChanged("updateBy");
		
		return this;
	}
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Integer getId(){
		return this.id;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> primary key
	* @param defaultValue  Return the default value if id is null.
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Integer getId(Integer defaultValue){
		Integer r=this.getId();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks) 
	public java.util.Date getLogTime(){
		return this.logTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if logTime is null.
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks) 
	public java.util.Date getLogTime(java.util.Date defaultValue){
		java.util.Date r=this.getLogTime();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	* <li><B>remarks:</B> the name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public String getName(){
		return this.name;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(){
		return this.enumIntA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
	* @param defaultValue  Return the default value if enumIntA is null.
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks) 
	public EnumIntA getEnumIntA(EnumIntA defaultValue){
		EnumIntA r=this.getEnumIntA();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks) 
	public EnumStringA getEnumStringA(){
		return this.enumStringA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if createTime is null.
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(java.util.Date defaultValue){
		java.util.Date r=this.getCreateTime();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(){
		return this.createBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if updateTime is null.
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(java.util.Date defaultValue){
		java.util.Date r=this.getUpdateTime();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(){
		return this.updateBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
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
	 	
	
	
	
	
	 
	public static class $Insert extends com.tsc9526.monalisa.orm.dao.Insert<TestLogyyyymm>{
		$Insert(TestLogyyyymm model){
			super(model);
		}	 
	}	
	
	public static class $Delete extends com.tsc9526.monalisa.orm.dao.Delete<TestLogyyyymm>{
		$Delete(TestLogyyyymm model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(Integer id){
			if(id ==null ) return 0;	
			
			this.model.id = id;
			
			return this.model.delete();				
		}
		
	}
	
	public static class $Update extends com.tsc9526.monalisa.orm.dao.Update<TestLogyyyymm>{
		$Update(TestLogyyyymm model){
			super(model);
		}		 			 			 		
	}
	
	public static class $Select extends com.tsc9526.monalisa.orm.dao.Select<TestLogyyyymm,$Select>{		
		$Select(TestLogyyyymm x){
			super(x);
		}	
						 
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public TestLogyyyymm selectByPrimaryKey(Integer id){
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
		public Map<Integer,TestLogyyyymm> selectToMap(String whereStatement,Object ... args){
			List<TestLogyyyymm> list=super.select(whereStatement,args);
			
			Map<Integer,TestLogyyyymm> m=new LinkedHashMap<Integer,TestLogyyyymm>();
			for(TestLogyyyymm x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,TestLogyyyymm> selectByExampleToMap($Example example){
			List<TestLogyyyymm> list=super.selectByExample(example);
			
			Map<Integer,TestLogyyyymm> m=new LinkedHashMap<Integer,TestLogyyyymm>();
			for(TestLogyyyymm x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
		
		public $SelectForExample selectForExample($Example example){
			return new $SelectForExample(example);
		} 	
		
		public class $SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<TestLogyyyymm,$Select>.$SelectForExample{
			public $SelectForExample($Example example) {
				super(example); 
			}
			
			
					
			/**
			* List result to Map, The map key is primary-key:  id
			*/
			public Map<Integer,TestLogyyyymm> selectToMap(){
				return selectByExampleToMap(($Example)this.example);
			}
			
		}
			
	}
	 
		
	public static class $Example extends com.tsc9526.monalisa.orm.criteria.Example<$Criteria,TestLogyyyymm>{
		public $Example(){}
		 
		protected $Criteria createInternal(){
			$Criteria x= new $Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=MelpClass.findClassWithAnnotation(TestLogyyyymm.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,TestLogyyyymm> selectToMap(){			
			List<TestLogyyyymm> list=SELECT().selectByExample(this);
			
			Map<Integer,TestLogyyyymm> m=new LinkedHashMap<Integer,TestLogyyyymm>();
			for(TestLogyyyymm x:list){
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
			return TestLogyyyymm.SELECT().selectForExample(this.$example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(TestLogyyyymm m){			 
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> primary key
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> id = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("id", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> logTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("log_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		* <li><B>remarks:</B> the name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> name = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("name", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<EnumIntA,$Criteria> enumIntA = new com.tsc9526.monalisa.orm.criteria.Field<EnumIntA,$Criteria>("enum_int_a", this, 4);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<EnumStringA,$Criteria> enumStringA = new com.tsc9526.monalisa.orm.criteria.Field<EnumStringA,$Criteria>("enum_string_a", this, 12);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> createTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("create_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> createBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("create_by", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> updateTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("update_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> updateBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("update_by", this);
			
	}
	 
			
	public static enum EnumIntA{V0,V1}
			
	public static enum EnumStringA{ TRUE, FALSE}
	
	
	  
	/**
	* Meta info about table: test_logyyyymm_
	*/ 
	public static class M{
		public final static String TABLE ="test_logyyyymm_";
	 	
	 	
		public final static String  id$name          = "id";
		public final static boolean id$key           = true;
		public final static int     id$length        = 10;
		public final static int     id$decimalDigits = 0;
		public final static String  id$value         = "NULL";
		public final static String  id$remarks       = "primary key";
		public final static boolean id$auto          = true;
		public final static boolean id$notnull       = true;
		public final static String  id$seq           = "";
		
		
		public final static String  logTime$name          = "log_time";
		public final static boolean logTime$key           = false;
		public final static int     logTime$length        = 19;
		public final static int     logTime$decimalDigits = 0;
		public final static String  logTime$value         = "NULL";
		public final static String  logTime$remarks       = "";
		public final static boolean logTime$auto          = false;
		public final static boolean logTime$notnull       = true;
		public final static String  logTime$seq           = "";
		
		
		public final static String  name$name          = "name";
		public final static boolean name$key           = false;
		public final static int     name$length        = 128;
		public final static int     name$decimalDigits = 0;
		public final static String  name$value         = "NULL";
		public final static String  name$remarks       = "the name";
		public final static boolean name$auto          = false;
		public final static boolean name$notnull       = false;
		public final static String  name$seq           = "";
		
		
		public final static String  enumIntA$name          = "enum_int_a";
		public final static boolean enumIntA$key           = false;
		public final static int     enumIntA$length        = 10;
		public final static int     enumIntA$decimalDigits = 0;
		public final static String  enumIntA$value         = "NULL";
		public final static String  enumIntA$remarks       = "enum field a.  #enum{{V0,V1}}";
		public final static boolean enumIntA$auto          = false;
		public final static boolean enumIntA$notnull       = false;
		public final static String  enumIntA$seq           = "";
		
		
		public final static String  enumStringA$name          = "enum_string_a";
		public final static boolean enumStringA$key           = false;
		public final static int     enumStringA$length        = 64;
		public final static int     enumStringA$decimalDigits = 0;
		public final static String  enumStringA$value         = "NULL";
		public final static String  enumStringA$remarks       = "enum field string. #enum{{ TRUE, FALSE}}";
		public final static boolean enumStringA$auto          = false;
		public final static boolean enumStringA$notnull       = false;
		public final static String  enumStringA$seq           = "";
		
		
		public final static String  createTime$name          = "create_time";
		public final static boolean createTime$key           = false;
		public final static int     createTime$length        = 19;
		public final static int     createTime$decimalDigits = 0;
		public final static String  createTime$value         = "NULL";
		public final static String  createTime$remarks       = "";
		public final static boolean createTime$auto          = false;
		public final static boolean createTime$notnull       = false;
		public final static String  createTime$seq           = "";
		
		
		public final static String  createBy$name          = "create_by";
		public final static boolean createBy$key           = false;
		public final static int     createBy$length        = 64;
		public final static int     createBy$decimalDigits = 0;
		public final static String  createBy$value         = "NULL";
		public final static String  createBy$remarks       = "";
		public final static boolean createBy$auto          = false;
		public final static boolean createBy$notnull       = false;
		public final static String  createBy$seq           = "";
		
		
		public final static String  updateTime$name          = "update_time";
		public final static boolean updateTime$key           = false;
		public final static int     updateTime$length        = 19;
		public final static int     updateTime$decimalDigits = 0;
		public final static String  updateTime$value         = "NULL";
		public final static String  updateTime$remarks       = "";
		public final static boolean updateTime$auto          = false;
		public final static boolean updateTime$notnull       = false;
		public final static String  updateTime$seq           = "";
		
		
		public final static String  updateBy$name          = "update_by";
		public final static boolean updateBy$key           = false;
		public final static int     updateBy$length        = 64;
		public final static int     updateBy$decimalDigits = 0;
		public final static String  updateBy$value         = "NULL";
		public final static String  updateBy$remarks       = "";
		public final static boolean updateBy$auto          = false;
		public final static boolean updateBy$notnull       = false;
		public final static String  updateBy$seq           = "";
		
			
		
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> primary key
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public final static String  id                     = "id";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> log_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.logTime$name, key=M.logTime$key, auto=M.logTime$auto, seq=M.logTime$seq, notnull=M.logTime$notnull, length=M.logTime$length, decimalDigits=M.logTime$decimalDigits, value=M.logTime$value, remarks=M.logTime$remarks)
		public final static String  logTime                     = "log_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> name
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		* <li><B>remarks:</B> the name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public final static String  name                     = "name";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_int_a &nbsp;[<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> enum field a.  #enum{{V0,V1}}
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.enumIntA$name, key=M.enumIntA$key, auto=M.enumIntA$auto, seq=M.enumIntA$seq, notnull=M.enumIntA$notnull, length=M.enumIntA$length, decimalDigits=M.enumIntA$decimalDigits, value=M.enumIntA$value, remarks=M.enumIntA$remarks)
		public final static String  enumIntA                     = "enum_int_a";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> enum_string_a &nbsp;[<font color=red>ENUM</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> enum field string. #enum{{ TRUE, FALSE}}
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.enumStringA$name, key=M.enumStringA$key, auto=M.enumStringA$auto, seq=M.enumStringA$seq, notnull=M.enumStringA$notnull, length=M.enumStringA$length, decimalDigits=M.enumStringA$decimalDigits, value=M.enumStringA$value, remarks=M.enumStringA$remarks)
		public final static String  enumStringA                     = "enum_string_a";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime                     = "create_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> create_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public final static String  createBy                     = "create_by";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime                     = "update_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_logyyyymm_201601&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public final static String  updateBy                     = "update_by";
		 
	}
	
}


