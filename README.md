# monalisa-orm

> And God said, Let there be light: and there was light.
      
# Features

* Using the database takes only 1 line of code
* Generic ORM functions(CRUD)
* Auto generate DTOs
* Dynamic load java code
* Dynamic load jar if needed
* Easily write multi-line strings in the java code

[Example Project](https://github.com/11039850/monalisa-example)

5 minutes video: [Youtube](http://www.youtube.com/watch?v=3qpr0J7D7cQ) / [YouKu](http://v.youku.com/v_show/id_XMTU0ODk1MzA2MA==.html) 

[For more details](https://github.com/11039850/monalisa-orm/wiki)

# Example

## Using the database
```java  
	
	@DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root")
    public interface TestDB {
    	public static DBConfig DB=DBConfig.fromClass(TestDB.class); 
    }
```

```java
	
	TestDB.DB.select("SELECT * FROM user WHERE name like ?","zzg%");
```	
 
## Query Example
```java

	//insert
	new User().setName("zzg.zhou").setStatus(1).save();
	
	//parse data from type: Map, json/xml string, JsonObject(Gson), HttpServletRequest, JavaBean
	new User().parse("{'name':'oschina','status':0}").save();
	new User().parse("<data> <name>china01</name><status>1</status> </data>").save();
	
	//select
	User.SELECT().selectByPrimaryKey(1);
	
	//SQL: SELECT * FROM `user` WHERE `name` = 'zzg.zhou'
	User.SELECT().selectOne("name=?", "zzg.zhou");
	
	//SQL: SELECT `name`, `status` FROM `user`
	User.SELECT().include("name","status").select();
	 
	Page<User> page=User.WHERE().name.like("zzg%").status.in(1,2,3).SELECT().selectPage(10,0);
	System.out.println(page.getTotalRow());
	
	//SQL: SELECT * FROM `user` WHERE `name` like 'zzg%' AND `status` IN(0, 1)
	for(User x:User.WHERE().name.like("zzg%").status.in(0, 1).SELECT().select()){
		System.out.println(x);
	}
			
	//SQL: SELECT * FROM `user` WHERE (`name` like 'zzg%' AND `status` >= 0) 
	//                             OR (`name` = 'zzg' AND `status` > 1) ORDER BY `status` ASC 
	for(User x:User.WHERE()
				.name.like("zzg%").status.ge(0)
				.OR()
				.name.eq("zzg").status.gt(1)
				.status.asc()
				.SELECT().select()){ //SELECT / delete / update
		System.out.println(x);
	}
	
	 
	//general query
	TestDB.DB.select("SELECT * FROM user WHERE name like ?","zzg%");
	TestDB.DB.createQuery().add("SELECT * FROM user WHERE name like ?","zzg%").getList(User.class);
	 
	Query q=new Query(TestDB.DB);
	DataTable<DataMap> rs=q.add("SELECT * FROM user WHERE name like ?","zzg%")
	 .add(" AND status ").in(1,2,3)
	 .getList();
	for(User x:rs.as(User.class)){
		System.out.println(x);
	}
	
	//DataTable query
	//SQL: SELECT name, count(*) as cnt FROM _THIS_TABLE WHERE status>=0 GROUP BY name ORDER BY name ASC
	DataTable<DataMap> newTable=rs.select("name, count(*) as cnt","status>=0","name ASC","GROUP BY name");
	
	
	//update
	User user=User.SELECT().selectOne("name=?", "zzg.zhou");
	user.setStatus(3).update();
	
	User updateTo=new User().setName("tsc9526");
	User.WHERE().name.like("zzg%").update(updateTo);
	
	
	//transaction
	Tx.execute(new Tx.Atom() {
		public int execute() {
			new User().setName("name001").setStatus(1).save();
			new User().setName("name002").setStatus(2).save();
			//... other database operation
			return 0;
		}
	});
	 
	//Dynamic model: Record
	Record r=new Record("user").use(TestDB.DB);
	r.set("name", "jjyy").set("status",1)
	 .save();
		
	//SQL: SELECT * FROM `user` WHERE (`name` like 'jjyy%' AND `status` >= 0)
	//                             OR (`name` = 'zzg' AND `status` > 1) ORDER BY `status` ASC 
	for(Record x:r.WHERE()
			.field("name").like("jjyy%").field("status").ge(0)
			.OR()
			.field("name").eq("zzg").field("status").gt(1)
			.field("status").asc()
			.SELECT().select()){
		System.out.println(x);
	} 
		
	//SQL: DELETE FROM `user` WHERE `name` like 'jjyy%' AND `status` >= 0
	r.WHERE()
	 .field("name").like("jjyy%").field("status").ge(0)
	 .delete();
		  
	
	//delete
	user.delete();
	
	//SQL: DELETE FROM `user` WHERE `name`='china01'
	User.WHERE().name.eq("china01").delete();
	
	//User.DELETE().deleteAll();     

```

## Auto generate DTOs

```java

	public class UserBlogDao {
		@Select(name="test.result.UserBlogs") //!!! Auto create the class: test.result.UserBlogs
		public List  selectUserBlogs(int user_id){ //!!! Auto replace List -> List<UserBlogs>
			Query q=TestDB.DB.createQuery();
			           
			q.add(""/**~{
					SELECT a.id, a.name, b.title, b.content, b.create_time
						FROM user a, blog b   
						WHERE a.id=b.user_id AND a.id=?		
			}*/, user_id);
			 
			return q.getList(); //!!! Auto replace getList() -> getList<UserBlogs>
		} 
	}
```


## Multi-line strings
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


[For more details](https://github.com/11039850/monalisa-orm/wiki)

# Maven: 
```xml
	
	<dependency>
		<groupId>com.tsc9526</groupId>
		<artifactId>monalisa-orm</artifactId>
		<version>1.7.0</version>
	</dependency>
``` 

    
# TODO list

* Other database's dialect
* Direct access data through HTTP
* Automatic refresh the query cache in the background
* ...


If you have any ideas or you want to help with the development just write me a message.

**zzg zhou**, 11039850@qq.com
