	一般做数据库相关开发的, 除非学习用, 否则很少有人愿意直接使用JDBC， 而通常都会使用一些框架和库。而且开源市场上的选择也比较多，
	就我个人接触到的就有：Hibernate，Mybatis，JdbcTemplate，DbUtils，ActiveRecord，JavaLite等等。 这些框架能大幅的提高开发效率，对于一些基本表的操作来说，虽然各有差异，但总的来说已经够用了。
	然而稍微复杂点的应用总会碰到需要动态拼接SQL的问题 ，这里要讨论的不是采用何种方式来拼接SQL的问题，而是对动态SQL查询结果的映射， 也就是所谓DTO类的生成问题。 
	通常我们在写完SQL的查询代码后， 还需要写一个对应的DTO类，将查询的数据映射到DTO，以便于后续的程序能够更好的使用这些数据。
	当然，为了图省事，也常常会把数据存储在像Map这样的结构中. 不过, 这种方式虽然很轻便，但是会带来几个比重要的潜在问题：
	*调用者需要知道Map里面的每个key的名称，会给程序员带来所谓的记忆负担
	*过重的记忆负担，就会导致系统的逻辑复杂， 理解困难，维护更困难。
	*SQL语句的更改导致Key发生变化后，很难发现问题，需要程序员非常小心的处理这些更改。 
 
	如果想要避免Map带来的问题，我们就得手工编写一个DTO类，然后再将查询结果映射到DTO，现在的框架基本上都能很轻松的做到。虽然DTO能够减轻程序员的记忆负担，使得我们的软件理解和维护起来要轻松很多，
	然而，天下没有免费的午餐，书写这些DTO是很枯燥乏味的，特别是字段很多的时候更是如此；
	而且，DTO写完后，如果后续SQL查询的字段出现变更，还必须要记得回来再修改这个对应的DTO。这和轻量级的Map相比又确实太麻烦了！
	 
	如果有一种方法在我们写完SQL查询后，就能够自动的处理这些SQL语句，帮助我们生成相应的DTO，并且当SQL修改后还能够自动的更新这个DTO。
	这样，一方面解决了需要我们手工书写DTO的麻烦； 另一方面，当我们修改SQL导致某个字段发生更改时， 由于自动生成的DTO也会同步修改， 
	这时如果有外部代码引用到这个字段，那么编译器就会立即给出提示：引用错误的字段。  使得问题一产生就能立即发现，避免了很多潜在的问题。
	
	想象总是完美的，现实总是残酷的! 那么，到底能否实现上面的想法呢，我们先初步分析一下可行性： 
	要做到自动产生DTO,其核心就是要拿到SQL查询所对应的每个列以及每个列的数据类型。有了列名和数据类型，就很容易写一个方法来产生DTO了。
	我们知道，在一般情况下，SQL查询写完之后，包括调用存储过程和那些根据调用参数来动态拼接的SQL(通常是动态组装过滤/分组/排序/连接表等)，其查询结果的字段部分都是相对固定的。
	当然，也有极少情况下会碰到字段都不确定的SQL查询，不过在这种极端情况下，即使手工也没法写DTO了，反倒是用Map更合适, 我们这里不做讨论。
	
	一种方案是直接分析SQL代码中SELECT部分的字段， 不过这个方案的局限性很大:
	*对于拼接的SQL，分析难度比较大
	*字段的类型也无法判断
	*SELECT * ...； SELECT a.*,b.name ...； CALL statement 这样常见的查询基本上无法处理。 
	上述方案对像Mybatis这种采用配置文件(xml)来写SQL的方式,似乎有些可行性，我没有具体试验过，但估计面临的困难不会少。
	
	  
	另一种方案是运行包含SQL的代码:
	我们知道JDBC执行一个SQL查询，会返回一个ResultSet对象，通过该对象中方法getMetaData()，能够得到这次查询的一些元数据：如列名称，列类型，以及该列所属的表名等, 这些信息就已经足够我们来产生需要的那个结果类了。
	那么，怎么才能够拿到这个ResultSet呢？ 对于那些固定的SQL语句还稍微好说点，我们拿到这个固定的SQL，然后调用JDBC，就能获取到该SQL的MetaData，然后就可以很容易的根据这些信息来产生DTO。
	但是，对于那些复杂的牵涉到一系列根据参数来动态产生的SQL查询，在参数设置好前是无法直接运行的，也就无法得到MetaData，得不到MetaData我们就无法往下一步走。
	怎么办？
	前面已经讨论了，即便是动态SQL，无论输入什么样的参数，虽然执行的SQL语句可能不一样，但是最终产生结果的列却是固定的。 我们当前需要解决的问题不正是要获取这些固定的列信息吗？ 
	至于实际上执行的是什么样的SQL，那是运行环境的事情，和我们现在编辑阶段要解决的问题没有什么关系。
	到了这里，聪明的你一定会想到，既然如此，那我们就构造一系列默认的参数值，这些参数并没有实际用处，仅仅是为了让我们的SQL代码得以正常运行，这样就可以拿到需要的MetaData，至于能否查询到数据无关紧要。
	好了，终于拿到了MetaData，接下来生成DTO的事情就很简单了：
	ResultSetMetaData rsmd = rs.getMetaData()；
	int cc = rsmd.getColumnCount()；
	for (int i = 1； i <= cc； i++) {
		String fieldType = TypeHelper.getJavaType(rsmd.getColumnType(i))； //JDBC数据类型转Java类型
		String fieldName = rsmd.getColumnLabel(i)； //as 别名
		if (fieldName == null || fieldName.trim().length() < 1) {
			fieldName = rsmd.getColumnName(i)；
		}
		
		writeToJava("private "+fieldType+" "+fieldName+"；")；
		...
	}

	等等，这里忽略一个细节，我们编写的SQL代码，通常有2种存在形式，一是直接在Java代码中， 另外一种是放在配置文件中。 
	配置文件形式和在Java代码中的写SQL这2种形式哪种更好，这里暂时不做细究，我会单独再找地方来讨论。
	对于配置文件这种形式，由于里面的主要任务主要就是组装SQL， 这种情况下，按照约定的文件格式，还是能够比较容易的分析出要运行的SQL语句，然后按上面的思路执行就可以了。 
	
	但是对于在Java代码中拼接的SQL， 要想得到MetaData，就比较困难了。接下来，我们主要讨论下在Java代码中写SQL的情况下，如何来自动产生DTO。
	基本思路还是前面提到的，就是构造出一系列默认的参数，然后调用Class的里面的方法，最后通过截获SQL相关的调用来得到MetaData。 怎么截获？ 
	
	
	
	monalisa-db实现了对数据库的一些操作，其Eclipse插件同时也带来了以下几个便利：
	*对@DB注解的接口，在文件保存时 ，就可以立即自动的生成基本表的CRUD操作
	
	*对@Select注解的方法，在文件保存时 ，就可以立即自动的生成结果类
	
	*代码中书写SQL，最头痛的事情之一就是 换行连接，字符转义的问题。 使得大段的SQL代码写起来很麻烦，看着也不舒服。多行字符串书写将会非常轻松
	System.out.println(""/**~{
	    SELECT * 
	    	FROM user
	    	WHERE name="zzg"
	}*/)；
	
	将会输出：
	SELECT * 
	    FROM user
	    WHERE name="zzg"
	    
	关于多行语法的更多细节可以参考: [这里](https://github.com/11039850/monalisa-db/wiki/Multiple-line-syntax)    
	
	
	*代码中的SQL类可以当着配置文件一样看待，能够动态更新。
	和一般的ClassLoader实现的动态加载不同，特别是对web应用，会导致整个web应用重启，然后重新初始化，启动缓慢；它采用Java的agentmain机制，实现代码的热替换，更新非常迅速。
	 
	
	
	为了方便开发，理想的情况下是SQL文件一保存，就能够立即的生成结果类， 这样还有一个特别的好处， 就是我们在编辑的时候，就能够知道写的SQL有没有问题。要实现这一点
	 
	接下来重点讨论下monalisa-db的这个插件具体是如何实现自动生成结果类的：
	要实现自动运行SQL代码，并自动生成DTO类的话，还存在相当的一些挑战：
	*如何标识方法：
		 通过@Select注解来标识一个方法，指示该方法输出的是DTO对象（ 记住: 由于这个时候DTO类还没有产生，因此约定使用泛值来书写该方法）
		  通常函数的返回值有三种类型， 其和 查询结果的对应关系如下：
		//1. List查询 （DataTable是一个扩展了选择/过滤/聚合/分组/连接等功能的List）
	    //@Select public DataTable   method_name(...){... return Query.getList()；   }    或
	    //@Select public List        method_name(...){... return Query.getList()；   }    
	    //
	    //
	    //2. Page查询
	    //@Select public Page   method_name(...){... return Query.Page()；      }
	    //
	    //3. 单条记录
	    //@Select public Object method_name(...){... return Query.getResult()； }
	
		
	*如何定义DTO的类名
	 	为了最简化的处理这个问题，@Select默认采用定义该方法的全路径类名做为包名，Result+方法名作为类名。 当然如果不想使用默认的类名，也可以通过name参数来指定生成的类名
	
	*如何执行代码：
		执行代码的关键是构造一批能够调用@Select标识方法的合适参数。为了简化问题，默认情况下会按如下规则进行构造：
		数字型参数，其值为0. 例如： @Select public Object getUserBlog(int id){....}  默认将构造一个int id=0； 的参数来执行该方法。
		字符串参数，其值为""： @Select public Object getUserBlog(String id){....}  默认将构造一个String id=""； 的参数来执行该方法。
		布尔型参数，其值为：false @Select public Object getUserBlog(boolean id){....}  默认将构造一个boolean id=false； 的参数来执行该方法。
		数组型参数，其值为：类型[0]  @Select public Object getUserBlog(int[] id){....}  默认将构造一个int[] id=new int[0]； 的参数来执行该方法。
		对象型参数，其值为： new 类型()； 例如： @Select public Object getUserBlog(User user){....}  默认将构造一个User user=new User()； 的参数来执行该方法。 
		
		
		当然，对于一些简单参数的情况下，上面缺省参数的方法基本上都能够奏效。 但是，对于复杂些的参数，（比如参数是一个接口，又或者是一个需要动态连接的表名） ，默认构造出的参数可能会导致程序无法执行。
		但是，怎么才能够让我们的代码生成器能够正确执行这个方法呢？ 好像确实没有什么更好的办法，只好把这个问题交给程序员来处理了: @Select提供了另外一个参数: build, 该参数返回一段调用参数的初始化代码，
		以帮助代码生成器执行这个方法。例如：
		@Select(build="int tableNo=1；") 
		public Object getUserBlog(int tableNo){....}
		通过build参数来告诉代码生成器, 不要使用默认的方式构造参数：tableNo，而是使用 build里面的定义。通过这种半自动化的方式， 对于复杂的参数，我都可以在build中对参数进行初始化。
	
	*如何生成DTO 	
		通过前面的工作，代码生成器终于可以自动的执行@Select代码了，但是，我们如何来获取生成DTO需要的信息呢？ 首先类名，我们已经知道了，现在关键是获取属性名称和数据类型。
		其实，在 “如何标识方法” 中，我们已经约定了函数的返回，都必须通过Query函数的getList/getPage/getResult 来返回查询结果。 只需要简单的拦截这几个方法的调用就能够得到执行的SQL，并最终得到生成DTO类的信息。	
			
	*如何修改代码：
		DTO产生了，接下来的问题就是要自动将@Select的代码的返回值修改为DTO类型。这里牵涉到Eclipse插件中用于修改正在编辑Java文件的接口：ICleanUpFix, 有兴趣的可以去了解下。
	
	*如何处理SQL的变更：
	 由于@Select的处理比较长，需要经历编译->运行(中间还需要有数据库操作)->生成DTO(编译)->更新类（编译），
	 如果DAO类包含的方法很多， 每次保存都把所有的@Select方法执行一遍，会消耗很长的时间，而且也没有必要，导致系统响应缓慢。 理想的方法是，只处理那些有修改过的@Select方法。因此，在生成DTO时我们需要增加一个静态字段：FINGERPRINT
	 用于标识生成该类的指纹信息，每次在决定是否处理@Select方法时，先计算下@Select方法的指纹值，和当前DTO类的指纹值进行比较，如果不一样则开始处理，否则跳过。 
	 这样即使在编辑一个包含大量@Select方法的类时， 和一个普通类的响应速度就没有什么差异了。
	 
	 
	 
	一个简单例子：
	public class UserBlogDao {                                      >>>>>>保存后>>>>>>              public class UserBlogDao {
			@Select(name="test.result.UserBlogs")                                                           @Select(name="test.result.UserBlogs")
			public List  selectUserBlogs(int user_id){                   自动修改返回类型                   public List<UserBlogs>  selectUserBlogs(int user_id){                                
				Query q=TestDB.DB.createQuery()；                                                              		Query q=TestDB.DB.createQuery()；                                                 
		                                                                                              		                                                                                 
				q.add(""/**~{                                                                                 		q.add(""/**~{                                                                    
						SELECT a.id,a.name,b.title, b.content,b.create_time             				                         SELECT a.id,a.name,b.title, b.content,b.create_time
							FROM user a, blog b                                                                     					FROM user a, blog b                                                        
							WHERE a.id=b.user_id AND a.id=?                                                         					WHERE a.id=b.user_id AND a.id=?                                            
				}*/, user_id)；                                                                                		}*/, user_id)；                                                                   
		                                                                                              		                                                                                 
				                                 		                     
				return q.getList()；                                        自动修改返回方法                     	return q.getList(UserBlogs.class)；                                               
			}                                                                                               } 
	}																																																}
	                                                                                 
																																   自动产生DTO:
																																            		                  package test.result；
																																            		                  
																																            		                  public class UserBlogs{
																																            		                  		final static String  FINGERPRINT = "000003AFEB1DE7DD"；
																																            		                   		
	                                                                                                		private int id；
	                                                                                                		private String name；
	                                                                                                		private title；
	                                                                                                		private String content；
	                                                                                                		private createTime；  
	                                                                                                    
	                                                                                                    //get/set ...
	                                                                                                }
	                                                                                                
	
	控制台输出：
	2016-06-27 17:00:31 [I] ****** Starting generate result classes from: test.dao.UserBlogDao ******	
	2016-06-27 17:00:31 [I] Create class: test.result.UserBlogs, from: [selectUserBlogs(int)]
	SELECT a.id,a.name,b.title, b.content,b.create_time
		FROM user a, blog b    
		WHERE a.id=b.user_id AND a.id=0

	
	