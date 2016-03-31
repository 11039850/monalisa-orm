# monalisa-core: Very simple database ORM

      "And God said, Let there be light: and there was light."
      

# Main features:
## Basic
* Introducing database takes only 1 line of code
* Using the JSP syntax writing SQL  without JSP container or library
* Dynamic SQL code Real time compile

## Generator
* Model classes
* Dynamic SQL code to result class

# Introduce
## Maven: 
```xml
	
	<dependency>
		<groupId>com.tsc9526</groupId>
		<artifactId>monalisa-core</artifactId>
		<version>0.7.6</version>
	</dependency>
```

# Example
## Example 1

Annotation for a class/interface, add the following code:
```java  
	
	@DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") 
```

Here is a full java code:
```java
    
    package example.monalisa;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.query.model.Record;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Define database
    public class User extends Record{ //Dynamic table model, name is "user"
	    public static void main(String[] args) {
		    User user=new User();
		    user.setName("zzg"); 
		
		    user.set("status", 1); //set undefined fields in this class
	    	user.save(); //save to database
		
    		System.out.println(user.get("id"));  //get the user id(auto increment field), output will be 1
    	}
	
    	private String name;
    	public String getName() {
    		return name;
    	}
    	public void setName(String name) {
    		this.name = name;
    	}
    }
```

Table:
``` sql 
		
		CREATE TABLE `user` (
		  `id` int(11) NOT NULL AUTO_INCREMENT,
		  `name` varchar(64) NOT NULL,
		  `passwd` varchar(64) DEFAULT NULL,
		  `status` int(11) DEFAULT NULL,
		  PRIMARY KEY (`id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## Example 2
Here is another formal's example:

Step 1: Define database
```java
    
    package example.monalisa.db;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.datasource.DBConfig;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Define database
    public interface Test {
    	public static DBConfig DB=DBConfig.fromClass(Test.class); 
    }
```
Step 2: Run code generator(If you use the Eclipse plug-in, you can omit this step, Plug-in automatically generated code)
```java

	package example.monalisa.db;

	import com.tsc9526.monalisa.core.generator.DBGeneratorMain;

	public class ModelGenerator {
    	public static void main(String[] args) {
    		//Generate Model classes to directory: src/main/java OR src
	    	DBGeneratorMain.generateModelClass(Test.class); 
    	}
    }
```

Step 3: Use the generated model classes
```java
    
    package example.monalisa.db;
    
	import com.tsc9526.monalisa.core.query.Query;	
	import example.monalisa.db.test.User;
	
	public class Example {
		public static void main(String[] args) {
			//insert
			new User().setName("zzg.zhou").setStatus(1).save();
		
			//parse model. data type may be: Map, json/xml string, HttpServletRequest, JavaBean
			String data="{'name':'oschina','status':0}";
			new User().parse(data).save();
			
			//select
			User.SELECT().selectOne("name=?", "zzg.zhou");
			User.SELECT().selectByPrimaryKey(1);
			User user=User.WHERE().name.like("zzg%").status.in(1,2,3).SELECT().selectOne(); //selectPage ...
			 
			//general query
			Test.DB.select("SELECT * FROM user WHERE name like ?","zzg%");
			Test.DB.createQuery().add("SELECT * FROM user WHERE name like ?","zzg%").getList(User.class);
			
			Query q=new Query(Test.DB);
			q.add("SELECT * FROM user WHERE name like ?","zzg%")
			 .add(" AND status ").in(1,2,3);
			
			
			//update
			user.setStatus(3).update();
			User updateTo=new User().setName("tsc9526");
			User.WHERE().name.like("zzg%").update(updateTo);
			
			//delete
			user.delete();
			User.DELETE().deleteAll();
		}
	}


```

# Code generator

## java command

Call DBGeneratorMain in your java project.

```java

	package example.monalisa.db;

	import com.tsc9526.monalisa.core.generator.DBGeneratorMain;

	public class ModelGenerator {
    	public static void main(String[] args) {
    		//Generate Model classes to directory: src/main/java OR src
	    	DBGeneratorMain.generateModelClass(Test.class); 
	    	
	    	//Generate SQL result classes and interface from sql files
	    	DBGeneratorMain.generateSqlQueryClass();
    	}
    }
```
## maven compile
```xml
		
		<plugins>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.6</source>
					<target>1.6</target>
					<annotationProcessors>
						<annotationProcessor>
							com.tsc9526.monalisa.core.processor.DBAnnotationProcessor
						</annotationProcessor>
					</annotationProcessors>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<version>2.6</version>
				<executions>
					<execution>
						<id>copy-resources</id>
						<phase>compile</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<encoding>UTF-8</encoding>
							<outputDirectory>${basedir}/target/classes</outputDirectory>
							<resources>
								<resource>
									<directory>target/generated-sources/annotations</directory>
									<excludes>
										<exclude>**/*.java</exclude>
									</excludes>
									<filtering>false</filtering>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
```

## eclipse plugin (coming soon ...)


# Dynamic SQL Files
* Create sql file： demo.jsp (suffix with ".jsp" or ".mql"), place it into project folder: sql
* Open with jsp editor, for example:
```jsp
	
	<%@page import="example.monalisa.db.Test"%>
	<%@page import="com.tsc9526.monalisa.core.query.Args"%>
	<%@page import="com.tsc9526.monalisa.core.query.Query"%>
	<%@page language="java" pageEncoding="utf-8"%>
	
	<%
		//Definition of the 2 parameters only for IDE editing
		Query q=new Query(); 
		Args args=new Args();
	%> 
	<query namespace="example.monalisa.sql.Demo" db="<%=Test.class%>"> 
		<q id="findUserByName" resultClass="ResultDemo"> 
			<%{	
				String name     =args.pop();
			%>
				SELECT * FROM user 
				<%if(name==null){%>
					WHERE name='zzg'
				<%}else{%> 
					WHERE name like $name
				<%}%>	
			<%}%>
		</q>
		<q id="findAllUsers">
			SELECT * FROM user
		</q> 
	</query>
```
* Run DBGeneratorMain.generateSqlQueryClass()
* Invoke generator code like this: 
```java

	Query q=example.monalisa.sql.Demo.findUserByName("zzg"); 
	ResultDemo r=q.getResult(ResultDemo.class);
	...
```

# TODO list

