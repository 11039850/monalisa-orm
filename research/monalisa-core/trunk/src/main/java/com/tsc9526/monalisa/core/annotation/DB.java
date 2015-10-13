package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target; 

/**
 * 
 * <b>第一步: 定义数据库连接信息, <font color=red>并保存!</font></b> <br>
 * <code>
 * package example; <br>
 * <br>
 * import com.tsc9526.monalisa.annotation.DB; <br>
 * import com.tsc9526.monalisa.core.datasource.DBConfig; <br>
 *
 * <br>
 * <p>@DB(url="jdbc:mysql://127.0.0.1:3306/world", username="root", password="root") <br>
 * public interface DBWorld { <br>
 *   //方便后面的查询使用: Query q=new Query().use(DBWorld.DB); ... <br>
 * 	 public final static DBConfig DB=DBConfig.fromClass(DBWorld.class)
 * }	 
 * </p>
 * </code>
 * 
 * <b>第二步: 访问数据库</b> <br>
 * <code>
 * package example; <br>
 * <br>
 * import example.dbworld.*; <br>
 * <br>
 * public class Example{  <br>
 * &nbsp;&nbsp; public void TestTableX(){ <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; TableX x1=new TableX(); <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; x1.setXXX(...); <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; //... <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; db.save(); <br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; TableX x2=TableX.SELECT().selectByPrimary(100); <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; System.out.println(x2); <br>
 * &nbsp;&nbsp;	} <br>
 * }<br>
 * </code>
 * 
 * 
 * @author zzg.zhou(11039850@qq.com)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DB{
	/**
	 * @return JDBC-URL链接. 例如mysql: <br>
	 * <code>jdbc:mysql://127.0.0.1:3306/world </code><br>
	 * <br>
	 * 多个数据库例子: <br>
	 * <code>jdbc:mysql://[127.0.0.1:3306,127.0.0.1:3307]/world </code><br>
	 * <br>
	 * Host格式: <code>[name1@host1:port1,name2@host2:port2 ...]</code>
	 */
	String url()      default "";
	
	/**
	 * @return 数据库驱动.  默认为mysql： com.mysql.jdbc.Driver
	 */
	String driver()   default "com.mysql.jdbc.Driver";
	
	/**
	 * 数据库Catalog
	 * @return
	 */
	String catalog()  default "";
	
	/**
	 * 数据库Schema
	 * @return
	 */
	String schema()   default "";
	
	/**
	 * @return 数据库连接用户名, 默认值: root
	 */
	String username() default "root";
	
	/**
	 * @return 数据库连接密码, 默认值: ""
	 */
	String password() default "";
	 
	/**
	 * @return 操作的表名, 默认值: "%": 表示所有的表 <br>
	 * 例如: pre_%: 表示所有以"pre_"为前缀的表 
	 */
	String tables()   default "%";
	
	/**
	 * @return  分号分隔的分区表. 格式: 表名前缀{分区类(参数,...)}; ... <br>
	 * 其中有个默认日期分区类为: DatePartitionTable, 参数1: 日期格式, 参数2: 日期字段名 
	 * 例如按天存储的日志表定义: <br>
	 * <code>log_access_{DatePartitionTable(yyyyMMdd,log_time)}</code>
	 * 
	 * <br><br>
	 * 或Json格式定义:<br>
	 * <code> {prefix:'log_access_', class='DatePartitionTable', args=['yyyyMMdd','log_time']} </code><br> or<br>
	 * <code>[{prefix:'log_access_', class='DatePartitionTable', args=['yyyyMMdd','log_time']},...] </code>
	 */
	String partitions() default "";
	
	/**
	 * @return 表名映射, 默认值: "" <br>
	 * 可以通过这个参数对个别的表指明特别的命名, 格式: Table=Class;Table=Class ... <br>
	 * 例如: table_123=TableX;table123=TableY 
	 */
	String mapping()          default "";
			
	/**
	 * 指定表模型的父类Class,  该Class须继承于 {@link com.tsc9526.monalisa.core.query.dao.Model} 
	 * @return
	 */
	String modelClass()       default "";
	
	/**
	 * 指定表模型监听类,  该类须实现 {@link com.tsc9526.monalisa.core.query.dao.ModelListener} 
	 * @return
	 */
	String modelListener()    default "";
	
	
	/**
	 * 数据源Class
	 * @return
	 */
	String datasourceClass()  default "";
 	
	/**
	 * 指定数据库配置唯一名称, 需使用标准的JAVA包类命名风格: x.y.z<br>
	 * 如果使用configFile配置文件, 则会读取系统属性: "DB@"+key()  作为配置文件的根路径
	 * 
	 * @return 默认值为包含@DB注解的类名<br>
	 * 
	 */
	String key()      default "";
	
	/**
	 * 
	 * 如指定值为: TEST, 则配置前缀为: DB.TEST.xxx <br>
	 * cfg 为通用配置项名, 不能作为配置名. 
	 * 
	 * @return
	 */
	String configName()       default "";
	
	/**
	 * 属性存在于该配置文件中, 配置项前缀为: DB.  <br>
	 * 下列属性除外:<br>
	 * <b>configFile,  configName, key</b> <br><br>
	 * 例如: url属性的配置项为: DB.url  <br>
	 * 
	 * 
	 * @return
	 */
	String configFile()       default "";
} 
