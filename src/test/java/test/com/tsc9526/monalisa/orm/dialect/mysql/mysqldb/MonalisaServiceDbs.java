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
 * Auto generated code by monalisa 2.1.2
 *
 */
@Table(
	name="monalisa_service_dbs",
	primaryKeys={"dbs"},
	remarks="",
	indexes={
	}
)
public class MonalisaServiceDbs extends com.tsc9526.monalisa.orm.model.Model<MonalisaServiceDbs> implements test.com.tsc9526.monalisa.orm.dialect.mysql.MysqlDB{
	private static final long serialVersionUID = 1010287989345L;
		 
	public static final $Insert INSERT(){
	 	return new $Insert(new MonalisaServiceDbs());
	}
	
	public static final $Delete DELETE(){
	 	return new $Delete(new MonalisaServiceDbs());
	}
	
	public static final $Update UPDATE(MonalisaServiceDbs model){
		return new $Update(model);
	}		
	
	public static final $Select SELECT(){
	 	return new $Select(new MonalisaServiceDbs());
	}	 	 
	 
	
	/**
	* Simple query with example <br>
	* 
	*/
	public static $Criteria WHERE(){
		return new $Example().createCriteria();
	}
	
	/**
	 * name: <b>monalisa_service_dbs</b> <br>
	 * primaryKeys: "dbs" <br>
	 * remarks: 
	 */ 
	public MonalisaServiceDbs(){
		super("monalisa_service_dbs", "dbs");		
	}		 
	
	
	/**
	 * Constructor use primary keys.<br><br>
	 * name: <b>monalisa_service_dbs</b> <br>
	 * primaryKeys: "dbs" <br>
	 * remarks: <br><br>
	 *
	 * @param dbs  database service name	 
	 */
	public MonalisaServiceDbs(String dbs){
		super("monalisa_service_dbs", "dbs");
		
		this.dbs = dbs;
		fieldChanged("dbs");
		
	}	 
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database service name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
	private String dbs;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database schema name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks)
	private String schema;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
	* <li><B>remarks:</B> JDBC-URL
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks)
	private String url;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database username
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks)
	private String username;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database password
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks)
	private String password;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> status: 1-enable database service, 0-disable
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
	private Integer status;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> create time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
	@Alias("create_time")
	private java.util.Date createTime;	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> update time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
	@Alias("update_time")
	private java.util.Date updateTime;	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database service name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks) 
	public MonalisaServiceDbs setDbs(String dbs){
		this.dbs = dbs;  
		
		fieldChanged("dbs");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database schema name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks) 
	public MonalisaServiceDbs setSchema(String schema){
		this.schema = schema;  
		
		fieldChanged("schema");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
	* <li><B>remarks:</B> JDBC-URL
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks) 
	public MonalisaServiceDbs setUrl(String url){
		this.url = url;  
		
		fieldChanged("url");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database username
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks) 
	public MonalisaServiceDbs setUsername(String username){
		this.username = username;  
		
		fieldChanged("username");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database password
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks) 
	public MonalisaServiceDbs setPassword(String password){
		this.password = password;  
		
		fieldChanged("password");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> status: 1-enable database service, 0-disable
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public MonalisaServiceDbs setStatus(Integer status){
		this.status = status;  
		
		fieldChanged("status");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> create time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public MonalisaServiceDbs setCreateTime(java.util.Date createTime){
		this.createTime = createTime;  
		
		fieldChanged("createTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> update time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public MonalisaServiceDbs setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;  
		
		fieldChanged("updateTime");
		
		return this;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database service name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks) 
	public String getDbs(){
		return this.dbs;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database service name
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database schema name
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks) 
	public String getSchema(){
		return this.schema;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database schema name
	* @param defaultValue  Return the default value if schema is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks) 
	public String getSchema(String defaultValue){
		String r=this.getSchema();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
	* <li><B>remarks:</B> JDBC-URL
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks) 
	public String getUrl(){
		return this.url;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
	* <li><B>remarks:</B> JDBC-URL
	* @param defaultValue  Return the default value if url is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks) 
	public String getUrl(String defaultValue){
		String r=this.getUrl();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database username
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks) 
	public String getUsername(){
		return this.username;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database username
	* @param defaultValue  Return the default value if username is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks) 
	public String getUsername(String defaultValue){
		String r=this.getUsername();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database password
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks) 
	public String getPassword(){
		return this.password;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
	* <li><B>remarks:</B> database password
	* @param defaultValue  Return the default value if password is null.
	*/
	@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks) 
	public String getPassword(String defaultValue){
		String r=this.getPassword();
		
		if(r==null){
			r=defaultValue;
		}
		
		return r;
	}
	
	
	
	
	 	
	
	
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> status: 1-enable database service, 0-disable
	*/
	@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks) 
	public Integer getStatus(){
		return this.status;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> status: 1-enable database service, 0-disable
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
	* <li><B>remarks:</B> status: 1-enable database service, 0-disable
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> create time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks) 
	public java.util.Date getCreateTime(){
		return this.createTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> create time
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
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> update time
	*/
	@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks) 
	public java.util.Date getUpdateTime(){
		return this.updateTime;
 
	}
	
	/**
	* @Column
	* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
	* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
	* <li><B>remarks:</B> update time
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
	
	
	
	
	 	
	
	
	
	
	 
	public static class $Insert extends com.tsc9526.monalisa.orm.dao.Insert<MonalisaServiceDbs>{
		$Insert(MonalisaServiceDbs model){
			super(model);
		}	 
	}	
	
	public static class $Delete extends com.tsc9526.monalisa.orm.dao.Delete<MonalisaServiceDbs>{
		$Delete(MonalisaServiceDbs model){
			super(model);
		}
		 
		
		public int deleteByPrimaryKey(String dbs){
			if(dbs ==null ) return 0;	
			
			this.model.dbs = dbs;
			
			return this.model.delete();				
		}
		
	}
	
	public static class $Update extends com.tsc9526.monalisa.orm.dao.Update<MonalisaServiceDbs>{
		$Update(MonalisaServiceDbs model){
			super(model);
		}		 			 			 		
	}
	
	public static class $Select extends com.tsc9526.monalisa.orm.dao.Select<MonalisaServiceDbs,$Select>{		
		$Select(MonalisaServiceDbs x){
			super(x);
		}	
						 
		
		/**
		* find model by primary keys
		*
		* @return the model associated with the primary keys,  null if not found.
		*/
		public MonalisaServiceDbs selectByPrimaryKey(String dbs){
			if(dbs ==null ) return null;
			
			
			this.model.dbs = dbs;
			
			
			this.model.load();
				 			 	 
			if(this.model.entity()){
				return this.model;
			}else{
				return null;
			}
		}				 
		
		
		
		
				
		/**
		* List result to Map, The map key is primary-key:  dbs
		*/
		public Map<String,MonalisaServiceDbs> selectToMap(String whereStatement,Object ... args){
			List<MonalisaServiceDbs> list=super.select(whereStatement,args);
			
			Map<String,MonalisaServiceDbs> m=new LinkedHashMap<String,MonalisaServiceDbs>();
			for(MonalisaServiceDbs x:list){
				m.put(x.getDbs(),x);
			}
			return m;
		}
	
		/**
		* List result to Map, The map key is primary-key: dbs 
		*/
		public Map<String,MonalisaServiceDbs> selectByExampleToMap($Example example){
			List<MonalisaServiceDbs> list=super.selectByExample(example);
			
			Map<String,MonalisaServiceDbs> m=new LinkedHashMap<String,MonalisaServiceDbs>();
			for(MonalisaServiceDbs x:list){
				m.put(x.getDbs(),x);
			}
			return m;
		}
		
		
		
		public $SelectForExample selectForExample($Example example){
			return new $SelectForExample(example);
		} 	
		
		public class $SelectForExample extends com.tsc9526.monalisa.orm.dao.Select<MonalisaServiceDbs,$Select>.$SelectForExample{
			public $SelectForExample($Example example) {
				super(example); 
			}
			
			
					
			/**
			* List result to Map, The map key is primary-key:  dbs
			*/
			public Map<String,MonalisaServiceDbs> selectToMap(){
				return selectByExampleToMap(($Example)this.example);
			}
			
		}
			
	}
	 
		
	public static class $Example extends com.tsc9526.monalisa.orm.criteria.Example<$Criteria,MonalisaServiceDbs>{
		public $Example(){}
		 
		protected $Criteria createInternal(){
			$Criteria x= new $Criteria(this);
			
			@SuppressWarnings("rawtypes")
			Class clazz=MelpClass.findClassWithAnnotation(MonalisaServiceDbs.class,DB.class);	  			
			com.tsc9526.monalisa.orm.criteria.QEH.getQuery(x).use(dsm.getDBConfig(clazz));
			
			return x;
		}
		
		/**
		* List result to Map, The map key is primary-key: dbs 
		*/
		public Map<String,MonalisaServiceDbs> selectToMap(){			
			List<MonalisaServiceDbs> list=SELECT().selectByExample(this);
			
			Map<String,MonalisaServiceDbs> m=new LinkedHashMap<String,MonalisaServiceDbs>();
			for(MonalisaServiceDbs x:list){
				m.put(x.getDbs(),x);
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
			return MonalisaServiceDbs.SELECT().selectForExample(this.$example);
		}
		
		/**
		* Update records with this example
		*/
		public int update(MonalisaServiceDbs m){			 
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
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database service name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> dbs = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("dbs", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database schema name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> schema = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("schema", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
		* <li><B>remarks:</B> JDBC-URL
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> url = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("url", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database username
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> username = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("username", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database password
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria> password = new com.tsc9526.monalisa.orm.criteria.Field.FieldString<$Criteria>("password", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
		* <li><B>remarks:</B> status: 1-enable database service, 0-disable
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria> status = new com.tsc9526.monalisa.orm.criteria.Field.FieldInteger<$Criteria>("status", this);
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> create time
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> createTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("create_time", this, 93);		 
			
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> update time
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria> updateTime = new com.tsc9526.monalisa.orm.criteria.Field<java.util.Date,$Criteria>("update_time", this, 93);		 
			
	}
	 
	
	
	  
	/**
	* Meta info about table: monalisa_service_dbs
	*/ 
	public static class M{
		public final static String TABLE ="monalisa_service_dbs";
	 	
	 	
		public final static String  dbs$name          = "dbs";
		public final static boolean dbs$key           = true;
		public final static int     dbs$length        = 64;
		public final static int     dbs$decimalDigits = 0;
		public final static String  dbs$value         = "NULL";
		public final static String  dbs$remarks       = "database service name";
		public final static boolean dbs$auto          = false;
		public final static boolean dbs$notnull       = true;
		public final static String  dbs$seq           = "";
		
		
		public final static String  schema$name          = "schema";
		public final static boolean schema$key           = false;
		public final static int     schema$length        = 64;
		public final static int     schema$decimalDigits = 0;
		public final static String  schema$value         = "NULL";
		public final static String  schema$remarks       = "database schema name";
		public final static boolean schema$auto          = false;
		public final static boolean schema$notnull       = true;
		public final static String  schema$seq           = "";
		
		
		public final static String  url$name          = "url";
		public final static boolean url$key           = false;
		public final static int     url$length        = 512;
		public final static int     url$decimalDigits = 0;
		public final static String  url$value         = "NULL";
		public final static String  url$remarks       = "JDBC-URL";
		public final static boolean url$auto          = false;
		public final static boolean url$notnull       = true;
		public final static String  url$seq           = "";
		
		
		public final static String  username$name          = "username";
		public final static boolean username$key           = false;
		public final static int     username$length        = 64;
		public final static int     username$decimalDigits = 0;
		public final static String  username$value         = "NULL";
		public final static String  username$remarks       = "database username";
		public final static boolean username$auto          = false;
		public final static boolean username$notnull       = true;
		public final static String  username$seq           = "";
		
		
		public final static String  password$name          = "password";
		public final static boolean password$key           = false;
		public final static int     password$length        = 64;
		public final static int     password$decimalDigits = 0;
		public final static String  password$value         = "NULL";
		public final static String  password$remarks       = "database password";
		public final static boolean password$auto          = false;
		public final static boolean password$notnull       = true;
		public final static String  password$seq           = "";
		
		
		public final static String  status$name          = "status";
		public final static boolean status$key           = false;
		public final static int     status$length        = 10;
		public final static int     status$decimalDigits = 0;
		public final static String  status$value         = "1";
		public final static String  status$remarks       = "status: 1-enable database service, 0-disable";
		public final static boolean status$auto          = false;
		public final static boolean status$notnull       = true;
		public final static String  status$seq           = "";
		
		
		public final static String  createTime$name          = "create_time";
		public final static boolean createTime$key           = false;
		public final static int     createTime$length        = 19;
		public final static int     createTime$decimalDigits = 0;
		public final static String  createTime$value         = "NULL";
		public final static String  createTime$remarks       = "create time";
		public final static boolean createTime$auto          = false;
		public final static boolean createTime$notnull       = true;
		public final static String  createTime$seq           = "";
		
		
		public final static String  updateTime$name          = "update_time";
		public final static boolean updateTime$key           = false;
		public final static int     updateTime$length        = 19;
		public final static int     updateTime$decimalDigits = 0;
		public final static String  updateTime$value         = "NULL";
		public final static String  updateTime$remarks       = "update time";
		public final static boolean updateTime$auto          = false;
		public final static boolean updateTime$notnull       = false;
		public final static String  updateTime$seq           = "";
		
			
		
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> dbs &nbsp;[<font color=red>KEY</font>|<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database service name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.dbs$name, key=M.dbs$key, auto=M.dbs$auto, seq=M.dbs$seq, notnull=M.dbs$notnull, length=M.dbs$length, decimalDigits=M.dbs$decimalDigits, value=M.dbs$value, remarks=M.dbs$remarks)
		public final static String  dbs                     = "dbs";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> schema &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database schema name
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.schema$name, key=M.schema$key, auto=M.schema$auto, seq=M.schema$seq, notnull=M.schema$notnull, length=M.schema$length, decimalDigits=M.schema$decimalDigits, value=M.schema$value, remarks=M.schema$remarks)
		public final static String  schema                     = "schema";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> url &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 512<br>
		* <li><B>remarks:</B> JDBC-URL
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.url$name, key=M.url$key, auto=M.url$auto, seq=M.url$seq, notnull=M.url$notnull, length=M.url$length, decimalDigits=M.url$decimalDigits, value=M.url$value, remarks=M.url$remarks)
		public final static String  url                     = "url";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> username &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database username
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.username$name, key=M.username$key, auto=M.username$auto, seq=M.username$seq, notnull=M.username$notnull, length=M.username$length, decimalDigits=M.username$decimalDigits, value=M.username$value, remarks=M.username$remarks)
		public final static String  username                     = "username";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> password &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 64<br>
		* <li><B>remarks:</B> database password
		*/
		@Column(table=M.TABLE, jdbcType=12, name=M.password$name, key=M.password$key, auto=M.password$auto, seq=M.password$seq, notnull=M.password$notnull, length=M.password$length, decimalDigits=M.password$decimalDigits, value=M.password$value, remarks=M.password$remarks)
		public final static String  password                     = "password";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> status &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 10 &nbsp;<B>value:</B> 1<br>
		* <li><B>remarks:</B> status: 1-enable database service, 0-disable
		*/
		@Column(table=M.TABLE, jdbcType=4, name=M.status$name, key=M.status$key, auto=M.status$auto, seq=M.status$seq, notnull=M.status$notnull, length=M.status$length, decimalDigits=M.status$decimalDigits, value=M.status$value, remarks=M.status$remarks)
		public final static String  status                     = "status";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> create_time &nbsp;[<font color=red>NOTNULL</font>]
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> create time
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.createTime$name, key=M.createTime$key, auto=M.createTime$auto, seq=M.createTime$seq, notnull=M.createTime$notnull, length=M.createTime$length, decimalDigits=M.createTime$decimalDigits, value=M.createTime$value, remarks=M.createTime$remarks)
		public final static String  createTime                     = "create_time";
		
		/**
		* @Column
		* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> monalisa_service_dbs&nbsp;<B>name:</B> update_time
		* <li>&nbsp;&nbsp;&nbsp;<B>length:</B> 19<br>
		* <li><B>remarks:</B> update time
		*/
		@Column(table=M.TABLE, jdbcType=93, name=M.updateTime$name, key=M.updateTime$key, auto=M.updateTime$auto, seq=M.updateTime$seq, notnull=M.updateTime$notnull, length=M.updateTime$length, decimalDigits=M.updateTime$decimalDigits, value=M.updateTime$value, remarks=M.updateTime$remarks)
		public final static String  updateTime                     = "update_time";
		 
	}
	
}


