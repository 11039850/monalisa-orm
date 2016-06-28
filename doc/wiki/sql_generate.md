	一般做数据库相关开发, 除非学习, 否则很少有人愿意直接使用JDBC。本来Java代码就比较啰嗦了，而直接用JDBC写代码之啰嗦简直有些丧心病狂！所以在实际开发过程中，我们通常都会使用一些框架/库来帮助我们操作数据库。而且开源市场上的选择也比较多，就我个人接触到的有：Hibernate，Mybatis，JdbcTemplate，DbUtils，ActiveRecord，JavaLite等等。 这些框架都能大幅的提高开发效率，对于一些基本CRUD操作来说，虽然各有差异，但总的来说基本够用了。
	  
	然而对于稍微复杂点的应用来说，总免不了碰到要动态拼接SQL的问题 。基本上各种框架都会有一套自己拼接动态SQL的方法， 我们这里不去比较各种拼接方法的优劣，而是想要讨论下SQL查询的映射问题， 即DTO的生成问题。
	 
	通常我们在写完SQL的查询代码后， 还需要一个对应的DTO，将数据库中查询出的数据映射到DTO，以便于调用的程序能够更好的使用这些数据。
	当然，为了图省事，有时也会把数据直接存储在像Map这样的数据结构中。 不过, Map这种方式虽然很轻便，但是会带来几个比重要的潜在问题：
	*调用者需要记住Map里面每个key的名称，这就会给程序员带来一些所谓的记忆负担
	*过重的记忆负担，就会导致系统的逻辑复杂， 理解困难，维护更困难。
	*SQL查询的更改导致Key发生变化后，很难发现问题，需要程序员非常小心的处理这些更改。 
 
	如果想要避免Map带来的这些问题，我们需要为每个查询都单独编写一个DTO。尽管书写这些DTO没有什么难度，但是非常枯燥乏味，特别是字段很多的时候更是如此；
	并且，如果SQL查询的字段出现更改，也必须要记得回来修改这个DTO。单独编写DTO虽然减轻了Map带来的部分问题，但是也额外增加了新的工作量。
	
	如果有一种方法能够在SQL查询（包括动态拼接的SQL）编写完成后，就能自动的做到下面2点就非常完美了：
	1. 根据SQL代码，直接生成对应的DTO
	2. 变更SQL代码，自动修改对应的DTO
	这样，一方面解决了手工书写DTO的麻烦； 另一方面，当修改SQL导致某个字段发生更改时， 由于自动生成的DTO也会同步修改， 
	那么在外部代码有引用到这个字段的地方，编译器就会立即给出错误提示！  使得问题一产生就能立即发现，避免了很多潜在的问题。
	
	想象总是完美的，现实总是残酷的! 那么，到底能否实现上面的想法呢，我们先初步分析一下可行性： 
	要做到自动产生DTO,其核心就是要拿到SQL查询所对应的每个列名及其数据类型。有了列名和数据类型，就能很容易写一个方法来产生DTO了。
	我们知道，在一般情况下，SQL查询写完之后，包括调用存储过程和那些根据调用参数来动态拼接的SQL，虽然最终运行的SQL可能不尽相同，但是其查询结果的字段部分都是相对固定的。
	当然，也有极少情况下会碰到字段都不确定的查询，不过在这种极端情况下，即使手工也没法写DTO了，反倒是用Map更合适, 我们这里不做讨论。
	那么，怎么才能拿到列名和类型呢？
	
	一种方案是分析SQL代码中SELECT部分的字段， 不过其局限性比较大:
	*对于拼接的SQL代码，分析难度比较大
	*字段的类型也难以判断
	*SELECT * ...; CALL statement 这样常见的查询方式分析起来难度也比较大。 
	上述方案对像Mybatis这种采用配置文件(xml)来写SQL的方式,似乎有些可行性，我没有具体试验过，但估计面临的困难不会少。
	
	  
	另一种方案是想办法直接运行包含SQL的这些代码:
	我们知道JDBC执行一个SQL查询，会返回ResultSet对象，通过该对象中的方法getMetaData()，能够得到这次查询的一些元数据：如列名称，列类型，以及该列所在的表名等, 这些信息就已经足够我们来产生需要的那个类了。
	那么，怎么才能够运行这些包含SQL的代码呢？ 对于那些固定的SQL语句还稍微好说点，我们拿到这个固定的SQL，调用JDBC就能拿到MetaData，然后就可以很容易的根据这些信息来生成DTO。
	但是，对于那些复杂的牵涉到根据一系列参数来动态产生的SQL查询，在参数设置好前是无法直接运行的，也就无法得到MetaData，得不到MetaData我们就无法生成DTO。
	怎么办？
	前面已经讨论了，即便是动态SQL，无论输入什么样的参数，虽然执行的SQL语句可能不一样，但是最终产生结果列却是固定的。 我们当前需要解决的问题不正是要获取这些列信息吗？  既然如此，那我们就构造一系列默认的参数值。这些参数并没有实际用处，仅仅是为了让我们正在编辑SQL代码得以正常运行，以便拿到需要的MetaData，至于能否查询到数据并不紧要。
	通常我们编写的SQL代码，有2种存在形式：一是直接在Java代码中， 另外一种是放在配置文件中。这里不讨论哪种形式更好，以后我会单独再找地方来讨论。
	
	这里主要讨论的是在Java代码中拼接的SQL， 如何实现一个代码生成器来自动生成这些DTO：
	
	要全自动化的解决这个问题，我们先来看看这个代码生成器所要面临的一些挑战：
	*如何标识一段需要生成DTO的SQL代码
		 首先，我们需要为代码生成器标识出这段代码，以便于代码生成器可以的运行这段代码。通常情况下，我们的数据接口都是方法级别的，因此我们可以通过对方法进行注解，用注解来标识这个方法要返回一个DTO对象是个不错的选择。
	*如何定义DTO的类名
		一种很容易想到的方法就是通过SQL代码所在的类名+方法名自动组合出一个名称, 当然也要允许程序员指定一个名字。 
	*如何执行代码：
		执行代码的关键是构造一批能够调用标识方法的合适参数。当然首先需要对标识的方法进行代码分析，提取方法参数名及类型。代码分析可以用类似JavaCC这样的工具，或者一些语法分析器，这里不做细究。下面主要探讨下默认参数的构造：
		为了简化问题，默认情况下我们可以按如下规则进行构造：
		数字型参数，默认值为：0			例如：public Object find(int arg){...}     默认将构造 int arg=0;  
		字符串参数，默认值为：""        例如：public Object find(String arg){...}  默认将构造 String arg="";  
		布尔型参数，默认值为：false     例如：public Object find(boolean arg){...} 默认将构造 boolean arg=false;  
		数组型参数，默认值为：类型[0]    例如：public Object find(int[] arg){...}   默认将构造 int[] arg=new int[0];  
		对象型参数，默认值为： new 类型() 例如：public Object find(User arg){...}    默认将构造 User arg=new User();  
		 
		当然，对于一些简单参数的情况下，上面构造规则基本上都能够奏效。 但是，对于有些参数：比如参数是一个接口，又或者是一个需要动态连接的表名，默认构造出的参数就会导致程序无法执行。
		但是，怎么才能够让我们的代码生成器能够继续执行下去呢？ 好像确实没有什么能自动处理的办法，只好把这个问题交给程序员来处理了。
		我们可以在注解上提供一个参数,该参数主要完成对默认规则下无法初始化的参数进行设置; 当然，这个参数中的初始化代码也可以覆盖默认规则,以便于我们在编辑阶段就可以测试执行不同的SQL流程。
		
	*如何生成DTO 	
		经过以上一系列的处理，我们终于能自动的把包含SQL查询代码的方法运行起来了。不过，现在我们还没得到想要的MetaData，无法生成DTO。
		一种可能的方式是包装一个JDBC，截获本次方法调用时执行的SQL查询， 但面临的问题是，如果方法中有多次查询就比较麻烦了。
		另一种方式依赖于框架的支持，可以截获到方法的return语句，获取其执行的SQL语句， 有了SQL语句，生成DTO就没有什么难度了。
		
	*如何修改代码：
		为了尽量减少程序员的工作，我们的代码生成器在生成完DTO后， 还需要将方法的返回值自动修改成这个DTO类。
	
	*如何处理SQL的变更：
		一种简单的做法是：一旦有某个SQL代码发生变化，就把所有的DTO都按照前面的方法重新生成一遍。 不过，很显然当查询方法很多的时候，DTO代码生成的过程将缓慢到难以忍受。
		另外一种做法是：我们在生成DTO时增加一个指纹字段，其值可以用SQL代码中所包含的信息来产生,例如：代码长度+代码的hashCode。 
		代码生成器在决定是否需要处理这个方法前，先计算该方法的指纹和存在于DTO里面的指纹进行比较，
		如果相同就跳过，否则就认为本方法的SQL发生了变更，需要更新DTO。
	
	到此为止，基本上DTO代码生成器的主要障碍都有了相应的处理办法。最后，用一个具体的实现来做个简单的示例：
	
	[monalisa-db](https://github.com/11039850/monalisa-db)是一个ORM框架，通过@DB(jdbc_url,username,password)注解来引入数据库，实现了对数据库的一些基本操作。 monalisa-eclipse是一个对应的Eclipse插件(源码正在整理中，后面也会提交到github上)，它可以：
	*@DB注解的接口，在文件保存时 ，就可以自动的生成表的CRUD操作
	*@Select注解的方法，在文件保存时 ，就可以自动生成DTO
	*很轻松的书写多行字符串
	插件安装后，多行语法不需要设置，但@DB需要对工程设置， @Select需要对Eclipse进行设置， 具体设置可以: [参考这里](https://github.com/11039850/monalisa-db/wiki/Code-Generator#eclipse%E6%8F%92%E4%BB%B6)
	  
	顺便多说一下：在Java代码中书写SQL，非常令人讨厌的一件事情就是Java语言中字符串的连接问题。使得大段的SQL代码中间要插很多的换行/转义符号，写起来很麻烦，看着也不舒服。monalisa-eclipse插件顺便解决了多行字符串的书写问题。
	例如：
	System.out.println(""/**~{
	    SELECT * 
	    	FROM user
	    	WHERE name="zzg"
	}*/);
	
	将会输出：
	SELECT * 
	    FROM user
	    WHERE name="zzg"
	    
	为了快速书写，可以在Eclipse中把多行字符串的语法设置为一个代码模板。关于多行语法的更多细节可以: [参考这里](https://github.com/11039850/monalisa-db/wiki/Multiple-line-syntax)  
	
	
	自动生成DTO示例，完整的工程可以： [参考这里](https://github.com/11039850/monalisa-example)
	package test.dao;
	
	public class UserBlogDao {
	    final static long $VERSION$= 18L; //!!! 版本号, 每次保存为自动 +1
	
	    //@Select 注解指示该方法需自动生成结果类
	    //默认类名: Result + 方法名， 默认包名：数据访问类的包名+"."+数据访问类的名称(小写)
	    //可选参数：name 指定生成结果类的名称，如果未指定该参数，则采用默认类名
	    @Select(name="test.result.UserBlogs") 
	
	    //!!! 保存后会自动修改该函数的返回值为： List -> List<UserBlogs>
	    //第一次编写时，由于结果类还不存在, 为了保证能够编译正常,
	    //函数的返回值 和 查询结果要用 泛值 替代, 保存后，插件会自动修改.
	    //函数的返回值 和 查询结果 泛值的对应关系分三类如下：
	    //1. List查询
	    //public DataTable   method_name(...){... return Query.getList();   }    或
	    //public List        method_name(...){... return Query.getList();   }    
	    //
	    //2. Page查询
	    //public Page   method_name(...){... return Query.Page();      }
	    //
	    //3. 单条记录
	    //public Object method_name(...){... return Query.getResult(); }
	    //
	    public List  selectUserBlogs(int user_id){ 
	        Query q=TestDB.DB.createQuery();
	
	        q.add(""/**~{
	                SELECT a.id,a.name,b.title, b.content,b.create_time
	                    FROM user a, blog b   
	                    WHERE a.id=b.user_id AND a.id=?
	        }*/, user_id);	
	        
	        return q.getList(); 
	    } 
	}
	
	上述代码保存后，就会自动生成DTO类：test.result.UserBlogs, 并对方法的返回值进行自动修改：
	public List<UserBlogs>  selectUserBlogs(int user_id){ 
		...
		return q.getList(UserBlogs.class); 
	}
  	 
	当然，如果对selectUserBlogs方法做了任何的修改（包括只是加了一个空格），保存文件后，插件也会自动更新UserBlogs。
	
	
	同时，也会在Eclipse的控制台看到下面的信息，方便我们调试：
	2016-06-27 17:00:31 [I] ****** Starting generate result classes from: test.dao.UserBlogDao ******	
	2016-06-27 17:00:31 [I] Create class: test.result.UserBlogs, from: [selectUserBlogs(int)]
	SELECT a.id,a.name,b.title, b.content,b.create_time
		FROM user a, blog b    
		WHERE a.id=b.user_id AND a.id=0
		
 
	 