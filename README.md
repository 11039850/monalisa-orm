# monalisa-db: Very simple database ORM

      "And God said, Let there be light: and there was light."
      
# Features:
* Using the database takes only 1 line of code
* Support dynamic SQL code auto to result class
* Support written multiple-line syntax in the Java code
* Dynamic load java sql code
* Auto increase the field: final static long $VERSION$=1L;

5 minutes video:

[![IMAGE ALT monalisa-db](http://img.youtube.com/vi/3qpr0J7D7cQ/0.jpg)](http://www.youtube.com/watch?v=3qpr0J7D7cQ)

[YouKu](http://v.youku.com/v_show/id_XMTU0ODk1MzA2MA==.html) 

For more details, please refer to [wiki](https://github.com/11039850/monalisa-db/wiki)

# Example

## Using the database
```java  
	
	@DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root")
    public interface Test {
    	public static DBConfig DB=DBConfig.fromClass(Test.class); 
    }
```

## Multi-lines
```java

	public static void main(String[] args) {
		String lines = ""/**~{
			SELECT * 
				FROM user
				WHERE name="zzg"
		}*/;
		System.out.println(lines);
	}
```

Output will be:

```sql

	SELECT * 
		FROM user
		WHERE name="zzg"
```

## Query Example
```java
     
	//insert
	new User().setName("zzg.zhou").setStatus(1).save();
	
	//parse data from type: Map, json/xml string, JsonObject(Gson), HttpServletRequest, JavaBean
	new User().parse("{'name':'oschina','status':0}").save();
	new User().parse("<data> <name>china01</name><status>1</status> </data>").save();
	
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
	
	
	//transaction
	Tx.execute(new Executable() {
		public int execute() {
			new User().setName("name001").setStatus(1).save();
			new User().setName("name002").setStatus(2).save();
			//... other database operation
			return 0;
		}
	}
```

For more details, please refer to [wiki](https://github.com/11039850/monalisa-db/wiki)

# Maven: 
```xml
	
	<dependency>
		<groupId>com.tsc9526</groupId>
		<artifactId>monalisa-core</artifactId>
		<version>1.0.3</version>
	</dependency>
``` 

    
# TODO list

