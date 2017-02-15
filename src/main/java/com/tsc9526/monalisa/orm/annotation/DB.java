/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tsc9526.monalisa.orm.datasource.ConfigClass;

/**
 * <code>
 * &#64;DB(url="jdbc:mysql://127.0.0.1:3306/test", username="root", password="root")<br>
 * public interface Test {<br>
 * 	 &nbsp;&nbsp;&nbsp;&nbsp;public final static DBConfig DB=DBConfig.fromClass(Test.class)<br>
 * }	 
 *<br><br> 
 * Call example:<br>
 * Test.DB.select("SELECT * FROM user");<br><br>
 * </code>
 * 
 * <br><br>
 * More properties setup, see：  {@link #configFile}  
 * 
 * @see com.tsc9526.monalisa.main.DBModelGenerateMain
 * 
 * @author zzg.zhou(11039850@qq.com)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DB{
	/**
	 * @return JDBC-URL. This is an example for mysql:<br>
	 * <code>jdbc:mysql://127.0.0.1:3306/world</code><br>
	 *<br>
	 * Multi databases:<br>
	 * <code>jdbc:mysql://[127.0.0.1:3306,127.0.0.1:3307]/world</code><br>
	 *<br>
	 * Host: <code>[name1@host1:port1,name2@host2:port2 ...]</code>
	 */
	String url()      default "";
	
	/**
	 * @return Database driver.  default is： com.mysql.jdbc.Driver
	 */
	String driver()   default "com.mysql.jdbc.Driver";
	
	/**
	 * 
	 * @return Database's catalog. default is ""
	 */
	String catalog()  default "";
	
	/**
	 * 
	 * @return Database's schema. default is ""
	 */
	String schema()   default "";
	
	/**
	 * @return Database's user name. default is: root
	 */
	String username() default "root";
	
	/**
	 * @return Database's password, default is: ""
	 */
	String password() default "";
	 
	/**
	 * default is "", means: disable database service <br>
	 * if set,you can access database via url: $web_app_uri/dbs/$this_dbs_name/$your_table_name
	 * @return name of the database service. 
	 */
	String dbs() default "";
	
	/**
	 * Indicates the table name to generate the model class, default is "%" (means is all tables). <br>
	 * For example: pre_%: all tables with the prefix: "pre_"
	 * @return table’s name
	 */
	String tables()   default "%";
	
	/**
	 * Define partition tables. format: table_prefix{partition_class(arg1,arg2,arg3 ...)}; ...<br>
	 * partition_class: DatePartitionTable, arg1: date format, arg2: date field  <br>
	 * For example, storage of the log table every day:<br>
	 * <code>log_access_{DatePartitionTable(yyyyMMdd,log_time)}</code>
	 * 
	 *<br><br>
	 * Json format:<br>
	 * <code> {prefix:'log_access_', class='DatePartitionTable', args=['yyyyMMdd','log_time']} </code><br> or<br>
	 * <code>[{prefix:'log_access_', class='DatePartitionTable', args=['yyyyMMdd','log_time']},...] </code>
	 * 
	 * @return  the define of partition tables
	 */
	String partitions() default "";
	
	/**
	 * @return Table name's mapping, default is: ""<br>
	 * For example: table_123=ModelX;table123=ModelY 
	 */
	String mapping()          default "";
			
	/**
	 * Specifies the parent class of the mode class. The Class must be inherited from {@link com.tsc9526.monalisa.orm.model.Model} 
	 *
	 * @return parent class, default is ""
	 */
	String modelClass()       default "";
	
	/**
	 * Mode listener,  he Class must be implements {@link com.tsc9526.monalisa.orm.model.ModelListener} 
	 * 
	 * @return mode listener
	 */
	String modelListener()    default "";
	
	
	/**
	 * Data source class, the value can be C3p0DataSource or DruidDataSource or other class which implementations of the class:<br>
	 *  {@link com.tsc9526.monalisa.orm.datasource.PooledDataSource}
	 * 
	 * @see com.tsc9526.monalisa.orm.datasource.PooledDataSource
	 * @return data source class
	 */
	String datasourceClass()  default "";
 	
	/**
	 * Unique identifier for this db, Use the standard JAVA package class naming style, e.g.: x.y.z<br>
	 * The default value is the class name annotated with @DB
	 * 
	 * @return Unique identifier for this db<br>
	 * 
	 */
	String key()      default "";
	
	/**
	 * 
	 * the configuration name. For example: TEST, The prefix of property xxx is: DB.TEST.xxx<br>
	 * Note: "cfg" is a generic configuration name, not as a configuration name. 
	 * 
	 * @return config name of this db
	 */
	String configName()       default "";
	
	/**
	 * Database's properties are stored in this configuration file.<br>
	 * Each of property with prefix: "DB.cfg." <br>
	 * For example: url, the property name is: <br><code> DB.cfg.url = jdbc:mysql://127.0.0.1:3306/world </code> <br><br>
	 * 
	 * Full properties see: {@link com.tsc9526.monalisa.orm.datasource.DbProp} <br><br>
	 * 
	 * Default configuration file is: full class name annotated with @DB<br><br>
	 * For example:
	 * <code><br>
	 * package test; <br> @DB public interface UserDB{}
	 * </code><br> The configuration file will be: test.UserDB <br> <br>
	 * 
	 * Another example: 
	 * <code><br>
	 * package test; <br> @DB(configFile="test.cfg") public interface UserDB{}
	 * </code><br> The configuration file will be: test.cfg <br>
	 * <br>
	 * It's priority is lower than configClass()<br>
	 * @return path of the config file
	 */
	String configFile()       default "";
	
	/**
	 * Database's properties are stored in this class. <br><br>
	 * It's priority is higer than configFile()<br>
	 * @return ConfigClass.class
	 */
	Class<? extends ConfigClass> configClass() default ConfigClass.class; 
} 
