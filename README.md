# monalisa-core

      And God said, Let there be light: and there was light.
      

# Main features:
* Introducing database takes only 1 line of code
* Dynamic SQL code generator

# Example
## Example 1
Here is a full example:
```java
    package example.monalisa;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.query.model.Record;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Define database
    public class User extends Record{ //Dynamic table model
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

## Example 2
Here is another formal's example:

Step 1: Define database
```java
    package example.monalisa.db;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.datasource.DBConfig;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Setup database
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
	    	DBGeneratorMain.generateModelClass(Test.class, "src"); //Generate Model classes to directory: src
    	}
    }
```

Step 3: Use the generated model classes
```java
    package example.monalisa.db;

    import example.monalisa.db.test.User;

    public class Example {
    	public static void main(String[] args) {
    		//insert
	    	new User().setName("zzg.zhou").setStatus(1).save();
	
    		//select
    		User user=User.WHERE().name.like("zzg%").status.in(1,2,3).SELECT().selectOne(); //selectPage ...
	
    		//update
    		user.setStatus(3).update();
		
    		//delete
    		user.delete();
    		User.WHERE().name.like("zzg%").delete();
	
    	}
    }
```
