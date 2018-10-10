package test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb;
 		

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
 * Auto generated code by monalisa 2.1.2
 *
 */
@Table(
	name="monalisa_service_dbq",
	primaryKeys={"id"},
	remarks="",
	indexes={
		  @Index(name="ux_ms_dbq_name", type=3, unique=true, fields={"name"}) 
	}
)
public class MonalisaServiceDbq extends com.tsc9526.monalisa.orm.model.Model<MonalisaServiceDbq> implements test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB{
	private static final long serialVersionUID = 1220551108783L;
		 
	public static final $Insert INSERT(){
	 	return new $Insert(new MonalisaServiceDbq());
	}
	
	public static final $Delete DELETE(){
	 	return new $Delete(new MonalisaServiceDbq());
	}
	
	public static final $Update UPDATE(MonalisaServiceDbq model){
		return new $Update(model);
	}		
	
	public static final $Select SELECT(){
	 	return new $Select(new MonalisaServiceDbq());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static $Criteria WHERE(){
		return new $Example().createCriteria();
	}
	
	/**
	 * name: <b>monalisa_service_dbq</b> <br>
	 * primaryKeys: "id" <br>
	 * remarks: 
	 */ 
	public MonalisaServiceDbq(){
		super("monalisa_service_dbq", "id");		
	}		 
	
	
	/**
	 * Constructor use primary keys.<br><br>
	 * name: <b>monalisa_service_dbq</b> <br>
	 * primaryKeys: "id" <br>
	 * remarks: <br><br>
	 *
	 * @param id  自增主键	 
	 */
	public MonalisaServiceDbq(Integer id){
		super("monalisa_service_dbq", "id");
		
		this.id = id;
		fieldChanged("id");
		
	}	 
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
	private Integer id;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
	private String name;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
	private String dbs;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks)
	private String sql;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks)
	private String memo;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> 状态（1-启用，0-禁用）
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
	private Integer status;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks)
	private String style;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks)
	private String graph;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
	@Alias("create_time")
	private java.util.Date createTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
	@Alias("create_by")
	private String createBy;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
	@Alias("update_time")
	private java.util.Date updateTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
	@Alias("update_by")
	private String updateBy;	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public MonalisaServiceDbq setId(Integer id){
		this.id = id;  
		
		fieldChanged("id");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public MonalisaServiceDbq setName(String name){
		this.name = name;  
		
		fieldChanged("name");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks) 
	public MonalisaServiceDbq setDbs(String dbs){
		this.dbs = dbs;  
		
		fieldChanged("dbs");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks) 
	public MonalisaServiceDbq setSql(String sql){
		this.sql = sql;  
		
		fieldChanged("sql");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks) 
	public MonalisaServiceDbq setMemo(String memo){
		this.memo = memo;  
		
		fieldChanged("memo");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> 状态（1-启用，0-禁用）
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public MonalisaServiceDbq setStatus(Integer status){
		this.status = status;  
		
		fieldChanged("status");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks) 
	public MonalisaServiceDbq setStyle(String style){
		this.style = style;  
		
		fieldChanged("style");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks) 
	public MonalisaServiceDbq setGraph(String graph){
		this.graph = graph;  
		
		fieldChanged("graph");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public MonalisaServiceDbq setCreateTime(java.util.Date createTime){
		this.createTime = createTime;  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public MonalisaServiceDbq setCreateBy(String createBy){
		this.createBy = createBy;  
		
		fieldChanged("createBy");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public MonalisaServiceDbq setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public MonalisaServiceDbq setUpdateBy(String updateBy){
		this.updateBy = updateBy;  
		
		fieldChanged("updateBy");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Integer getId(){
		return this.id;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> 自增主键
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
	* <li><B>remarks:</B> 自增主键
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks) 
	public Integer getIdAsInt(Integer defaultValue){
		Number r = getId();
		if(r!=null){
			return r.intValue();
		}else{
			return defaultValue;
		}
	}	  
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks) 
	public String getName(){
		return this.name;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
	* <li><B>remarks:</B> 
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks) 
	public String getDbs(){
		return this.dbs;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if dbs is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks) 
	public String getDbs(String defaultValue){
		String r=this.getDbs();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks) 
	public String getSql(){
		return this.sql;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if sql is null.
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks) 
	public String getSql(String defaultValue){
		String r=this.getSql();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks) 
	public String getMemo(){
		return this.memo;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if memo is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks) 
	public String getMemo(String defaultValue){
		String r=this.getMemo();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> 状态（1-启用，0-禁用）
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public Integer getStatus(){
		return this.status;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> 状态（1-启用，0-禁用）
	* @param defaultValue  Return the default value if status is null.
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public Integer getStatus(Integer defaultValue){
		Integer r=this.getStatus();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> 状态（1-启用，0-禁用）
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public Integer getStatusAsInt(Integer defaultValue){
		Number r = getStatus();
		if(r!=null){
			return r.intValue();
		}else{
			return defaultValue;
		}
	}	  
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks) 
	public String getStyle(){
		return this.style;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if style is null.
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks) 
	public String getStyle(String defaultValue){
		String r=this.getStyle();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks) 
	public String getGraph(){
		return this.graph;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
	* <li><B>remarks:</B> 
	* @param defaultValue  Return the default value if graph is null.
	*/
	@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks) 
	public String getGraph(String defaultValue){
		String r=this.getGraph();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks) 
	public String getCreateBy(){
		return this.createBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> 
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks) 
	public String getUpdateBy(){
		return this.updateBy;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
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
	
	
	
	
	 	
	
	
	
	
	 
	public static class $Insert extends com.tsc9526.monalisa.orm.dao.Insert<MonalisaServiceDbq>{
		$Insert(MonalisaServiceDbq model){
			super(model);
		}	 
	}	
	
	public static class $Delete extends com.tsc9526.monalisa.orm.dao.Delete<MonalisaServiceDbq>{
		$Delete(MonalisaServiceDbq model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(Integer id){
			if(id ==null ) return 0;	
			
			this.model.id = id;
			
			return this.model.delete();				
		}
		
		
		/**
		* Delete by unique key: ux_ms_dbq_name
		* @param name 	
		*/
		public int deleteByName(String name){			 
			this.model.name=name;
						 
			 
			return this.model.delete();
		}			 
		 
	}
	
	public static class $Update extends com.tsc9526.monalisa.orm.dao.Update<MonalisaServiceDbq>{
		$Update(MonalisaServiceDbq model){
			super(model);
		}		 			 			 		
	}
	
	public static class $Select extends com.tsc9526.monalisa.orm.dao.Select<MonalisaServiceDbq,$Select>{		
		$Select(MonalisaServiceDbq x){
			super(x);
		}	
						 
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public MonalisaServiceDbq selectByPrimaryKey(Integer id){
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
		* Find by unique key: ux_ms_dbq_name
		* @param name 	
		*/
		public MonalisaServiceDbq selectByName(String name){	
			$Criteria c=WHERE();
			
			c.name.eq(name);			 
			 
			return super.selectByKeyExample(c.$example);
		}			 
		
		/**
		* List result to Map, The map key is unique-key: name 
		*/
		public Map<String,MonalisaServiceDbq> selectToMapWithName(String whereStatement,Object ... args){
			List<MonalisaServiceDbq> list=super.select(whereStatement,args);
			
			Map<String,MonalisaServiceDbq> m=new LinkedHashMap<String,MonalisaServiceDbq>();
			for(MonalisaServiceDbq x:list){
				m.put(x.getName(),x);
			}
			return m;
		}
		
		/**
		* List result to Map, The map key is unique-key: name 
		*/
		public Map<String,MonalisaServiceDbq> selectByExampleToMapWithName($Example example){
			List<MonalisaServiceDbq> list=super.selectByExample(example);
			
			Map<String,MonalisaServiceDbq> m=new LinkedHashMap<String,MonalisaServiceDbq>();
			for(MonalisaServiceDbq x:list){
				m.put(x.getName(),x);
			}
			return m;
		}
		
				
		/**
		* List result to Map, The map key is primary-key:  id
		*/
		public Map<Integer,MonalisaServiceDbq> selectToMap(String whereStatement,Object ... args){
			List<MonalisaServiceDbq> list=super.select(whereStatement,args);
			
			Map<Integer,MonalisaServiceDbq> m=new LinkedHashMap<Integer,MonalisaServiceDbq>();
			for(MonalisaServiceDbq x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,MonalisaServiceDbq> selectByExampleToMap($Example example){
			List<MonalisaServiceDbq> list=super.selectByExample(example);
			
			Map<Integer,MonalisaServiceDbq> m=new LinkedHashMap<Integer,MonalisaServiceDbq>();
			for(MonalisaServiceDbq x:list){
				m.put(x.getId(),x);
			}
			return m;
		}
		
		
		
		public $SelectForExample selectForExample($Example example){
			return new $SelectForExample(example);
		} 	
		
		public class $SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<MonalisaServiceDbq,$Select>.$SelectForExample{
			public $SelectForExample($Example example) {
				super(example); 
			}
			
			
			/**
			* List result to Map, The map key is unique-key: name 
			*/
			public Map<String,MonalisaServiceDbq> selectToMapWithName(){
				return selectByExampleToMapWithName(($Example)this.example);
			}
			
					
			/**
			* List result to Map, The map key is primary-key:  id
			*/
			public Map<Integer,MonalisaServiceDbq> selectToMap(){
				return selectByExampleToMap(($Example)this.example);
			}
			
		}
			
	}
	 
		
	public static class $Example extends com.tsc9526.monalisa.orm.criteria.Example<$Criteria,MonalisaServiceDbq>{
		public $Example(){}
		 
		protected $Criteria createInternal(){
			$Criteria x= new $Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=MelpClass.findClassWithAnnotation(MonalisaServiceDbq.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		/**
		* List result to Map, The map key is primary-key: id 
		*/
		public Map<Integer,MonalisaServiceDbq> selectToMap(){			
			List<MonalisaServiceDbq> list=SELECT().selectByExample(this);
			
			Map<Integer,MonalisaServiceDbq> m=new LinkedHashMap<Integer,MonalisaServiceDbq>();
			for(MonalisaServiceDbq x:list){
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
			return MonalisaServiceDbq.SELECT().selectForExample(this.$example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(MonalisaServiceDbq m){			 
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> 自增主键
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> id = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("id", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> name = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("name", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> dbs = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("dbs", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> sql = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("sql", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> memo = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("memo", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
		* <li><B>remarks:</B> 状态（1-启用，0-禁用）
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> status = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("status", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> style = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("style", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> graph = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("graph", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> createTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("create_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> createBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("create_by", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> updateTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("update_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> updateBy = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("update_by", this);
			
	}
	 
	
	
	  
	/**
	* Meta info about table: monalisa_service_dbq
	*/ 
	public static class M{
		public final static String TABLE ="monalisa_service_dbq";
	 	
	 	
		public final static String  id$name          = "id";
		public final static boolean id$key           = true;
		public final static int     id$length        = 10;
		public final static int     id$decimalDigits = 0;
		public final static String  id$value         = "NULL";
		public final static String  id$remarks       = "自增主键";
		public final static boolean id$auto          = true;
		public final static boolean id$notnull       = true;
		public final static String  id$seq           = "";
		
		
		public final static String  name$name          = "name";
		public final static boolean name$key           = false;
		public final static int     name$length        = 255;
		public final static int     name$decimalDigits = 0;
		public final static String  name$value         = "NULL";
		public final static String  name$remarks       = "";
		public final static boolean name$auto          = false;
		public final static boolean name$notnull       = true;
		public final static String  name$seq           = "";
		
		
		public final static String  dbs$name          = "dbs";
		public final static boolean dbs$key           = false;
		public final static int     dbs$length        = 64;
		public final static int     dbs$decimalDigits = 0;
		public final static String  dbs$value         = "NULL";
		public final static String  dbs$remarks       = "";
		public final static boolean dbs$auto          = false;
		public final static boolean dbs$notnull       = true;
		public final static String  dbs$seq           = "";
		
		
		public final static String  sql$name          = "sql";
		public final static boolean sql$key           = false;
		public final static int     sql$length        = 65535;
		public final static int     sql$decimalDigits = 0;
		public final static String  sql$value         = "NULL";
		public final static String  sql$remarks       = "";
		public final static boolean sql$auto          = false;
		public final static boolean sql$notnull       = true;
		public final static String  sql$seq           = "";
		
		
		public final static String  memo$name          = "memo";
		public final static boolean memo$key           = false;
		public final static int     memo$length        = 1024;
		public final static int     memo$decimalDigits = 0;
		public final static String  memo$value         = "NULL";
		public final static String  memo$remarks       = "";
		public final static boolean memo$auto          = false;
		public final static boolean memo$notnull       = false;
		public final static String  memo$seq           = "";
		
		
		public final static String  status$name          = "status";
		public final static boolean status$key           = false;
		public final static int     status$length        = 10;
		public final static int     status$decimalDigits = 0;
		public final static String  status$value         = "1";
		public final static String  status$remarks       = "状态（1-启用，0-禁用）";
		public final static boolean status$auto          = false;
		public final static boolean status$notnull       = true;
		public final static String  status$seq           = "";
		
		
		public final static String  style$name          = "style";
		public final static boolean style$key           = false;
		public final static int     style$length        = 65535;
		public final static int     style$decimalDigits = 0;
		public final static String  style$value         = "NULL";
		public final static String  style$remarks       = "";
		public final static boolean style$auto          = false;
		public final static boolean style$notnull       = false;
		public final static String  style$seq           = "";
		
		
		public final static String  graph$name          = "graph";
		public final static boolean graph$key           = false;
		public final static int     graph$length        = 65535;
		public final static int     graph$decimalDigits = 0;
		public final static String  graph$value         = "NULL";
		public final static String  graph$remarks       = "";
		public final static boolean graph$auto          = false;
		public final static boolean graph$notnull       = false;
		public final static String  graph$seq           = "";
		
		
		public final static String  createTime$name          = "create_time";
		public final static boolean createTime$key           = false;
		public final static int     createTime$length        = 19;
		public final static int     createTime$decimalDigits = 0;
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
		public final static boolean createBy$notnull       = true;
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> id &nbsp;[<font color=red>KEY</font>|<font color=red>AUTO</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10<br>
		* <li><B>remarks:</B> 自增主键
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.id$name, key=M.id$key, auto=M.id$auto, seq=M.id$seq, notnull=M.id$notnull, length=M.id$length, decimalDigits=M.id$decimalDigits, value=M.id$value, remarks=M.id$remarks)
		public final static String  id                     = "id";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> name &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 255<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.name$name, key=M.name$key, auto=M.name$auto, seq=M.name$seq, notnull=M.name$notnull, length=M.name$length, decimalDigits=M.name$decimalDigits, value=M.name$value, remarks=M.name$remarks)
		public final static String  name                     = "name";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
		public final static String  dbs                     = "dbs";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> sql &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.sql$name, key=M.sql$key, auto=M.sql$auto, seq=M.sql$seq, notnull=M.sql$notnull, length=M.sql$length, decimalDigits=M.sql$decimalDigits, value=M.sql$value, remarks=M.sql$remarks)
		public final static String  sql                     = "sql";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> memo
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 1024<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.memo$name, key=M.memo$key, auto=M.memo$auto, seq=M.memo$seq, notnull=M.memo$notnull, length=M.memo$length, decimalDigits=M.memo$decimalDigits, value=M.memo$value, remarks=M.memo$remarks)
		public final static String  memo                     = "memo";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
		* <li><B>remarks:</B> 状态（1-启用，0-禁用）
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
		public final static String  status                     = "status";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> style
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.style$name, key=M.style$key, auto=M.style$auto, seq=M.style$seq, notnull=M.style$notnull, length=M.style$length, decimalDigits=M.style$decimalDigits, value=M.style$value, remarks=M.style$remarks)
		public final static String  style                     = "style";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> graph
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 65535<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=-1, name=M.graph$name, key=M.graph$key, auto=M.graph$auto, seq=M.graph$seq, notnull=M.graph$notnull, length=M.graph$length, decimalDigits=M.graph$decimalDigits, value=M.graph$value, remarks=M.graph$remarks)
		public final static String  graph                     = "graph";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime                     = "create_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> create_by &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.createBy$name, key=M.createBy$key, auto=M.createBy$auto, seq=M.createBy$seq, notnull=M.createBy$notnull, length=M.createBy$length, decimalDigits=M.createBy$decimalDigits, value=M.createBy$value, remarks=M.createBy$remarks)
		public final static String  createBy                     = "create_by";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime                     = "update_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbq&nbsp;<B>name:</B> update_by
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> 
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.updateBy$name, key=M.updateBy$key, auto=M.updateBy$auto, seq=M.updateBy$seq, notnull=M.updateBy$notnull, length=M.updateBy$length, decimalDigits=M.updateBy$decimalDigits, value=M.updateBy$value, remarks=M.updateBy$remarks)
		public final static String  updateBy                     = "update_by";
		 
	}
	
}


