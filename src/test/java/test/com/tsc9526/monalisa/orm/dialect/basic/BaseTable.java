package test.com.tsc9526.monalisa.orm.dialect.basic;
 		

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
 * Auto generated code by monalisa 2.2.0
 *
 */
@Table(
	name="test_record",
	primaryKeys={"record_id"},
	remarks="",
	indexes={
		  @Index(name="test_record_pk", type=3, unique=true, fields={"record_id"}) 
	}
)
public class BaseTable extends com.tsc9526.monalisa.orm.model.Model<BaseTable> implements test.com.tsc9526.monalisa.orm.dialect.postgres.PostgresDB{
	private static final long serialVersionUID = 1092897148296L;
		 
	public static final $Insert INSERT(){
	 	return new $Insert(new BaseTable());
	}
	
	public static final $Delete DELETE(){
	 	return new $Delete(new BaseTable());
	}
	
	public static final $Update UPDATE(BaseTable model){
		return new $Update(model);
	}		
	
	public static final $Select SELECT(){
	 	return new $Select(new BaseTable());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static $Criteria WHERE(){
		return new $Example().createCriteria();
	}
	
	/**
	 * name: <b>test_record</b> <br>
	 * primaryKeys: "record_id" <br>
	 * remarks: 
	 */ 
	public BaseTable(){
		super("test_record", "record_id");		
	}		 
	
	
	/**
	 * Constructor use primary keys.<br><br>
	 * name: <b>test_record</b> <br>
	 * primaryKeys: "record_id" <br>
	 * remarks: <br><br>
	 *
	 * @param recordId  自增主键	 
	 */
	public BaseTable(Integer recordId){
		super("test_record", "record_id");
		
		this.recordId = recordId;
		fieldChanged("recordId");
		
	}	 
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks)
	@Alias("record_id")
	private Integer recordId;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
	* <li><B>remarks:</B> 名称
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
	private String name;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
	private String title;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
	@Alias("ts_a")
	private java.util.Date tsA;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
	private Integer version;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
	@Alias("create_time")
	private java.util.Date createTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
	@Alias("create_by")
	private String createBy;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
	@Alias("update_time")
	private java.util.Date updateTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
	@Alias("update_by")
	private String updateBy;	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks) 
	public BaseTable setRecordId(Integer recordId){
		this.recordId = recordId;  
		
		fieldChanged("recordId");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
	* <li><B>remarks:</B> 名称
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public BaseTable setName(String name){
		this.name = name;  
		
		fieldChanged("name");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks) 
	public BaseTable setTitle(String title){
		this.title = title;  
		
		fieldChanged("title");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public BaseTable setTsA(java.util.Date tsA){
		this.tsA = tsA;  
		
		fieldChanged("tsA");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public BaseTable setVersion(Integer version){
		this.version = version;  
		
		fieldChanged("version");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public BaseTable setCreateTime(java.util.Date createTime){
		this.createTime = createTime;  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public BaseTable setCreateBy(String createBy){
		this.createBy = createBy;  
		
		fieldChanged("createBy");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public BaseTable setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public BaseTable setUpdateBy(String updateBy){
		this.updateBy = updateBy;  
		
		fieldChanged("updateBy");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks) 
	public Integer getRecordId(){
		return this.recordId;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
	* <li><B>remarks:</B> 自增主键
	* @param defaultValue  Return the default value if recordId is null.
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks) 
	public Integer getRecordId(Integer defaultValue){
		Integer r=this.getRecordId();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks) 
	public Integer getRecordIdAsInt(Integer defaultValue){
		Number r = getRecordId();
		if(r!=null){
			return r.intValue();
		}else{
			return defaultValue;
		}
	}	  
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
	* <li><B>remarks:</B> 名称
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public String getName(){
		return this.name;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
	* <li><B>remarks:</B> 名称
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks) 
	public String getTitle(){
		return this.title;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(){
		return this.tsA;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	* @param defaultValue  Return the default value if tsA is null.
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks) 
	public java.util.Date getTsA(java.util.Date defaultValue){
		java.util.Date r=this.getTsA();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public Integer getVersion(){
		return this.version;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	* @param defaultValue  Return the default value if version is null.
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public Integer getVersion(Integer defaultValue){
		Integer r=this.getVersion();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks) 
	public Integer getVersionAsInt(Integer defaultValue){
		Number r = getVersion();
		if(r!=null){
			return r.intValue();
		}else{
			return defaultValue;
		}
	}	  
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(){
		return this.createBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(){
		return this.updateBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
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
	
	
	
	
	 	
	
	
	
	
	 
	public static class $Insert extends com.tsc9526.monalisa.orm.dao.Insert<BaseTable>{
		$Insert(BaseTable model){
			super(model);
		}	 
	}	
	
	public static class $Delete extends com.tsc9526.monalisa.orm.dao.Delete<BaseTable>{
		$Delete(BaseTable model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(Integer recordId){
			if(recordId ==null ) return 0;	
			
			this.model.recordId = recordId;
			
			return this.model.delete();				
		}
		
		
		/**
		* Delete by unique key: test_record_pk
		* @param recordId 自增主键	
		*/
		public int deleteByRecordId(Integer recordId){			 
			this.model.recordId=recordId;
						 
			 
			return this.model.delete();
		}			 
		 
	}
	
	public static class $Update extends com.tsc9526.monalisa.orm.dao.Update<BaseTable>{
		$Update(BaseTable model){
			super(model);
		}		 			 			 		
	}
	
	public static class $Select extends com.tsc9526.monalisa.orm.dao.Select<BaseTable,$Select>{		
		$Select(BaseTable x){
			super(x);
		}	
						 
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public BaseTable selectByPrimaryKey(Integer recordId){
			if(recordId ==null ) return null;
			
			
			this.model.recordId = recordId;
			
			
			this.model.load();
				 			 	 
			if(this.model.entity()){
				return this.model;
			}else{
				return null;
			}
		}				 
		
		
		
		
		
		/**
		* Find by unique key: test_record_pk
		* @param recordId 自增主键	
		*/
		public BaseTable selectByRecordId(Integer recordId){	
			$Criteria c=WHERE();
			
			c.recordId.eq(recordId);			 
			 
			return super.selectByKeyExample(c.$example);
		}			 
		
		/**
		* List result to Map, The map key is unique-key: record_id 
		*/
		public Map<Integer,BaseTable> selectToMapWithRecordId(String whereStatement,Object ... args){
			List<BaseTable> list=super.select(whereStatement,args);
			
			Map<Integer,BaseTable> m=new LinkedHashMap<Integer,BaseTable>();
			for(BaseTable x:list){
				m.put(x.getRecordId(),x);
			}
			return m;
		}
		
		/**
		* List result to Map, The map key is unique-key: recordId 
		*/
		public Map<Integer,BaseTable> selectByExampleToMapWithRecordId($Example example){
			List<BaseTable> list=super.selectByExample(example);
			
			Map<Integer,BaseTable> m=new LinkedHashMap<Integer,BaseTable>();
			for(BaseTable x:list){
				m.put(x.getRecordId(),x);
			}
			return m;
		}
		
				
		/**
		* List result to Map, The map key is primary-key:  recordId
		*/
		public Map<Integer,BaseTable> selectToMap(String whereStatement,Object ... args){
			List<BaseTable> list=super.select(whereStatement,args);
			
			Map<Integer,BaseTable> m=new LinkedHashMap<Integer,BaseTable>();
			for(BaseTable x:list){
				m.put(x.getRecordId(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: recordId 
		*/
		public Map<Integer,BaseTable> selectByExampleToMap($Example example){
			List<BaseTable> list=super.selectByExample(example);
			
			Map<Integer,BaseTable> m=new LinkedHashMap<Integer,BaseTable>();
			for(BaseTable x:list){
				m.put(x.getRecordId(),x);
			}
			return m;
		}
		
		
		
		public $SelectForExample selectForExample($Example example){
			return new $SelectForExample(example);
		} 	
		
		public class $SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<BaseTable,$Select>.$SelectForExample{
			public $SelectForExample($Example example) {
				super(example); 
			}
			
			
			/**
			* List result to Map, The map key is unique-key: recordId 
			*/
			public Map<Integer,BaseTable> selectToMapWithRecordId(){
				return selectByExampleToMapWithRecordId(($Example)this.example);
			}
			
					
			/**
			* List result to Map, The map key is primary-key:  recordId
			*/
			public Map<Integer,BaseTable> selectToMap(){
				return selectByExampleToMap(($Example)this.example);
			}
			
		}
			
	}
	 
		
	public static class $Example extends com.tsc9526.monalisa.orm.criteria.Example<$Criteria,BaseTable>{
		public $Example(){}
		 
		protected $Criteria createInternal(){
			$Criteria x= new $Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=MelpClass.findClassWithAnnotation(BaseTable.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		/**
		* List result to Map, The map key is primary-key: recordId 
		*/
		public Map<Integer,BaseTable> selectToMap(){			
			List<BaseTable> list=SELECT().selectByExample(this);
			
			Map<Integer,BaseTable> m=new LinkedHashMap<Integer,BaseTable>();
			for(BaseTable x:list){
				m.put(x.getRecordId(),x);
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
			return BaseTable.SELECT().selectForExample(this.$example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(BaseTable m){			 
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
		* <li><B>remarks:</B> 自增主键
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> recordId = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("record_id", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
		* <li><B>remarks:</B> 名称
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> name = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("name", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> title = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("title", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> tsA = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("ts_a", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> version = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("version", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> createTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("create_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> createBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("create_by", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> updateTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("update_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> updateBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("update_by", this);
			
	}
	 
	
	
	  
	/**
	* Meta info about table: test_record
	*/ 
	public static class M{
		public final static String TABLE ="test_record";
	 	
	 	
		public final static String  recordId$name          = "record_id";
		public final static boolean recordId$key           = true;
		public final static int     recordId$length        = 10;
		public final static int     recordId$decimalDigits = 0;
		public final static String  recordId$value         = "nextval('test_record_record_id_seq'::regclass)";
		public final static String  recordId$remarks       = "自增主键";
		public final static boolean recordId$auto          = true;
		public final static boolean recordId$notnull       = true;
		public final static String  recordId$seq           = "";
		
		
		public final static String  name$name          = "name";
		public final static boolean name$key           = false;
		public final static int     name$length        = 128;
		public final static int     name$decimalDigits = 0;
		public final static String  name$value         = "N0001'::character varying";
		public final static String  name$remarks       = "名称";
		public final static boolean name$auto          = false;
		public final static boolean name$notnull       = true;
		public final static String  name$seq           = "";
		
		
		public final static String  title$name          = "title";
		public final static boolean title$key           = false;
		public final static int     title$length        = 128;
		public final static int     title$decimalDigits = 0;
		public final static String  title$value         = "NULL";
		public final static String  title$remarks       = "";
		public final static boolean title$auto          = false;
		public final static boolean title$notnull       = false;
		public final static String  title$seq           = "";
		
		
		public final static String  tsA$name          = "ts_a";
		public final static boolean tsA$key           = false;
		public final static int     tsA$length        = 29;
		public final static int     tsA$decimalDigits = 6;
		public final static String  tsA$value         = "NULL";
		public final static String  tsA$remarks       = "";
		public final static boolean tsA$auto          = false;
		public final static boolean tsA$notnull       = false;
		public final static String  tsA$seq           = "";
		
		
		public final static String  version$name          = "version";
		public final static boolean version$key           = false;
		public final static int     version$length        = 10;
		public final static int     version$decimalDigits = 0;
		public final static String  version$value         = "0";
		public final static String  version$remarks       = "";
		public final static boolean version$auto          = false;
		public final static boolean version$notnull       = true;
		public final static String  version$seq           = "";
		
		
		public final static String  createTime$name          = "create_time";
		public final static boolean createTime$key           = false;
		public final static int     createTime$length        = 29;
		public final static int     createTime$decimalDigits = 6;
		public final static String  createTime$value         = "NULL";
		public final static String  createTime$remarks       = "";
		public final static boolean createTime$auto          = false;
		public final static boolean createTime$notnull       = true;
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
		public final static int     updateTime$length        = 29;
		public final static int     updateTime$decimalDigits = 6;
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> record_id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> nextval('test_record_record_id_seq'::regclass)<br>
		* <li><B>remarks:</B> 自增主键
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.recordId$name, key=M.recordId$key, auto=M.recordId$auto, seq=M.recordId$seq, notnull=M.recordId$notnull, length=M.recordId$length, decimalDigits=M.recordId$decimalDigits, value=M.recordId$value, remarks=M.recordId$remarks)
		public final static String  recordId                     = "record_id";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128 &nbsp;<B>value:</B> N0001'::character varying<br>
		* <li><B>remarks:</B> 名称
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public final static String  name                     = "name";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> title
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 128<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.title$name, key=M.title$key, auto=M.title$auto, seq=M.title$seq, notnull=M.title$notnull, length=M.title$length, decimalDigits=M.title$decimalDigits, value=M.title$value, remarks=M.title$remarks)
		public final static String  title                     = "title";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> ts_a
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.tsA$name, key=M.tsA$key, auto=M.tsA$auto, seq=M.tsA$seq, notnull=M.tsA$notnull, length=M.tsA$length, decimalDigits=M.tsA$decimalDigits, value=M.tsA$value, remarks=M.tsA$remarks)
		public final static String  tsA                     = "ts_a";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> version &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 0<br>
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.version$name, key=M.version$key, auto=M.version$auto, seq=M.version$seq, notnull=M.version$notnull, length=M.version$length, decimalDigits=M.version$decimalDigits, value=M.version$value, remarks=M.version$remarks)
		public final static String  version                     = "version";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime                     = "create_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> create_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public final static String  createBy                     = "create_by";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 29<br>
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime                     = "update_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> test_record&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public final static String  updateBy                     = "update_by";
		 
	}
	
}


