# 一、MyBatis简介

- MyBatis 是支持定制化 SQL、存储过程以及高级映射的优秀的持久层框架。
- MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。

- MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的Java对象）映射成数据库中的记录.


## 1、历史

- 原是Apache的一个开源项目iBatis, 2010年6月这个项目由Apache Software Foundation 迁移到了Google Code，随着开发团队转投Google Code旗下， iBatis3.x正式更名为MyBatis ，代码于2013年11月迁移到Github（下载地址见后）。
- iBatis一词来源于“internet”和“abatis”的组合，是一个基于Java的持久层框架。 iBatis提供的持久层框架包括SQL Maps和Data Access Objects（DAO）


## 2、为什么要使用MyBatis？

- MyBatis是一个【半自动化】的持久化层框架。
- JDBC


> –SQL夹在Java代码块里，耦合度高导致硬编码内伤
>
> –维护不易且实际开发需求中sql是有变化，频繁修改的情况多见

- Hibernate和JPA

> –长难复杂SQL，对于Hibernate而言处理也不容易
>
> –内部自动生产的SQL，不容易做特殊优化。
>
> –基于全映射的全自动框架，大量字段的POJO进行部分映射时比较困难。导致数据库性能下降。

- 对开发人员而言，核心sql还是需要自己优化

- sql和java编码分开，功能边界清晰，一个专注业务、一个专注数。


## 3、去哪里找MyBatis？

•https://github.com/mybatis/mybatis-3/

![img](https://img-blog.csdnimg.cn/20201215204746975.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzMjg0NDY5,size_16,color_FFFFFF,t_70)

# 二、MyBatis-HelloWorld

​	HelloWorld简单版

> –1创建一张测试表
>
> –2创建对应的javaBean
>
> –3创建mybatis配置文件，
>
> - 写配置(两个,全局配置文件(指导mybatis运行的) , dao接口的实现文件(描述dao中每个方法怎么工作) )
> - 第一个配置文件; (称为mybatis的全局配置文件,指导mybatis如何正确运行,比如连接向哪个数据库)
> - 第二个配置文件: (编写每一一个方法都如何向数据库发送sq|语句,如何执行。。。。相当于接口的实现类)
>   	将mapper的namespace属性改为接口的全类名
>   	配置细节
>
> -4创建sql映射文件
>
> -5在全局配置文件注册
>
> –6测试

## 1、创建一张测试表

![image-20210816233024074](C:\Users\LIOBIO\AppData\Roaming\Typora\typora-user-images\image-20210816233024074.png)

## 2、创建对应的javaBean与dao接口

Employee:

```java
public class Employee {
    private Integer id;
    private String empName;
    private Integer gender;
    private String email;
    //省略有参无参、get/set()、toString()
}    
```


EmployeeDao接口：

```java
public interface EmployeeDao {//按照id查询员工
	public Employee getEmpById(Integer id);
}
```
## 3、创建mybatis全局配置文件

mybatis_config.xml

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--配置连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis_achang"/>
                <property name="username" value="root"/>
                <property name="password" value="00000"/>
            </dataSource>
        </environment>
    </environments>
    
    <mappers>
        <mapper resource="org/mybatis/example/BlogMapper.xml"/>
    </mappers>
</configuration>
```
## 4、创建sql映射文件

xml配置文件版的 dao接口的实现描述，

public Employee getEmpById(Integer id);

mapper标签
	namespace属性：名称空间；写接口的全类名
select标签：用来定义一个查询操作
    id属性：方法名；相当于对于某个方法的实现
    resultType属性：方法运行后的返回值类型（查询操作必须指定）
    #{属性名}：代表取出传递过来的某个参数的值

EmployeeDao.xml:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.achang.dao.EmployeeDao">
    <select id="getEmpById" resultType="com.achang.bean.Employee">
        select * from t_employee where id = #{id}
    </select>
    <!--增删改不用写返回值类型-->
    <update id="updateEmp" >
        update t_employee set empname = #{empName},gender = #{gender},email = #{email} where id = #{id}
    </update>
    <delete id="deleteEmpById">
        delete from t_employee where id = #{id}
    </delete>
    <insert id="insertEmp">
        insert into t_employee(empname,gender,email)
                values(#{empName},#{gender},#{email})
    </insert>
</mapper>

```

我们写的dao接口的实现文件, mybatis默认是不知道的,需要在全局配置文件中注册

## 5、在全局配置文件注册

在mybatis_config.xml中配置:

```xml
<!--mappers标签：引入自己编写的每一个接口的xml实现文件
		resource属性：引入xml实现文件的位置
-->
<mappers>
	<mapper resource="EmployeeDao.xml"/>
</mappers>
```

在此注册让Mybatis知道我们的dao实现xml文件在哪里

## 6、测试

完整的查询语句测试：


```java
@Test
public void test1() throws IOException {
    //1、根据全局配置文件创建出一个sqlSessionFactory
    //SqlSessionFactory:是SqlSession工厂，负责创建SqlSession对象
    //SqlSession对象：sql会话---(代表和数据库的一次会话)
    String resource = "mybatis_config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    Employee employee = null;
    SqlSession openSession = null;
    try {
        //2、获取和数据库的一次会话；与getConnection()；拿到一条连接
        sqlSessionFactory.openSession();
        //3、使用SqlSession操作数据库，获取到dao接口的实现
        EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
        //4、拿到dao接口impl实现类后，调用相对于的方法即可
        employee = employeeDaoImpl.getEmpById(1);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        openSession.close();
    }
    System.out.println(employee);//Employee{id=1, empName='admin', gender=0, email='admin@qq.com'}
}

```


## 7,增删改查语句PLUS：

*注意!!!注意!!!注意!!!*

增删改需要设置事务

1. 要在方法中最后try-catch-finally关闭资源，增删改需要手动提交事务

2. openSession(true)；设置是否自动提交事务，true为自动提交，false为不自动提交（例子：修改方法）

   ![image-20210817215737384](C:\Users\LIOBIO\AppData\Roaming\Typora\typora-user-images\image-20210817215737384.png)


```java
public class EmployeeTest {
	//工厂在类中创建一次即可
    SqlSessionFactory sqlSessionFactory;
    //每次调用自动的执行，一次会话一次连接
    @Before
    public void initSqlSessionFactory() throws IOException {
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //查询
    @Test
    public void test1() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeDao mapper = sqlSession.getMapper(EmployeeDao.class);
        Employee empById = mapper.getEmpById(3);
        System.out.println(empById);
    }

    //修改
    @Test
    public void test2() throws IOException {
        //openSession(true)设置自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        int updateEmp = 0;
        try {
            EmployeeDao mapper = sqlSession.getMapper(EmployeeDao.class);
            updateEmp = mapper.updateEmp(new Employee(3, "欧尼", 1, "achang@qq.com"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        System.out.println(updateEmp);
    }

    //新增
    @Test
    public void test3() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int insertEmp = 0;
        try {
            EmployeeDao mapper = sqlSession.getMapper(EmployeeDao.class);
            insertEmp = mapper.insertEmp(new Employee(null, "我我", 0, "sisi@qq.com"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        System.out.println(insertEmp);

    }

    //删除
    @Test
    public void test4() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        boolean empById = false;
        try {
            EmployeeDao mapper = sqlSession.getMapper(EmployeeDao.class);
            empById = mapper.deleteEmpById(8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        System.out.println(empById);

    }

}

```

## 8、细节

两个文件：

1. 全局配置文件：mybatis-config.xml；指导mybatis正确运行的一些全局设置
2. SQL映射文件：EmployeeDao.xml；相当于是对Dao接口的一个实现描述细节；

细节：

1. 获取到的是接口的代理对象；mybatis自动创建的
2. SqlSessionFactory和SqlSession；


> SqlSessionFactory：创建SqlSession对象，Factory只new一次就行
>
> SqlSession：相当于connection，是和数据库进行交互的，也是一次和数据库的一次会话，就应该创建一个新的的SqlSession，一次与数据库连接，就创建一次会话

# 三、全局配置文件

**实例：**

mybatis_config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis_achang"/>
                <property name="username" value="root"/>
                <property name="password" value="00000"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="EmployeeDao.xml"/>
    </mappers>
</configuration>

```

MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置和属性信息

且配置文档的结构有规定先后顺序

```dtd
<!ELEMENT configuration (properties?, settings?, typeAliases?, typeHandlers?, objectFactory?, objectWrapperFactory?, reflectorFactory?, plugins?, environments?, databaseIdProvider?, mappers?)>
```

![image-20210819215140656](C:\Users\LIOBIO\AppData\Roaming\Typora\typora-user-images\image-20210819215140656.png)

## 1、properties（属性）

作用：通过properties标签引入外部内容

properties标签：和Spring的context：property-placeholder；引用外部配置文件
resource属性：从类路径下引入
url属性：引用磁盘路径或网络路径

dbconfig.properties：

```properties
username=root
password=123456
url=jdbc:mysql://localhost:3306/mybatis
driver=com.mysql.jdbc.Driver
```

mybatis_config.xml

通过${ }动态取出配置文件中的内容

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="dbconfig.properties"/>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!--${}取出配置文件中的值-->
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="EmployeeDao.xml"/>
    </mappers>

</configuration>

```

## 2、settings（设置）

settings是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。

| 设置名                   | 描述                                                         | 有效值        | 默认值 |
| ------------------------ | ------------------------------------------------------------ | ------------- | ------ |
| mapUnderscoreToCamelCase | 是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。 | true \| false | False  |

```xml
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```

3、typeAliases（类型别名）
推荐还是使用全类名!!!

类型别名：为常用的类型起别名
        typeAlias属性：就是为一个javaBean起别名；别名默认就是类名（不区分大小写），配置文件中就可以用别名了
                alias属性：指定一个别名
        package属性：批量起别名
                name属性：指定一个包名，默认别名就是类名
                @alias()注解:起别名

```xml
<typeAliases>
    <typeAlias type="com.liobio.bean.Employee" alias="emp"/>//起别名
    <package name="com.liobio.bean"/>//批量起别名
</typeAliases>
```

常见的 Java 类型内建的类型别名。它们都是不区分大小写的，为了应对原始类型的命名重复，采取了特殊的命名风格。

| 别名       | 映射的类型 |
| :--------- | :--------- |
| _byte      | byte       |
| _long      | long       |
| _short     | short      |
| _int       | int        |
| _integer   | int        |
| _double    | double     |
| _float     | float      |
| _boolean   | boolean    |
| string     | String     |
| byte       | Byte       |
| long       | Long       |
| short      | Short      |
| int        | Integer    |
| integer    | Integer    |
| double     | Double     |
| float      | Float      |
| boolean    | Boolean    |
| date       | Date       |
| decimal    | BigDecimal |
| bigdecimal | BigDecimal |
| object     | Object     |
| map        | Map        |
| hashmap    | HashMap    |
| list       | List       |
| arraylist  | ArrayList  |
| collection | Collection |
| iterator   | Iterator   |

## 5、plugins（插件）

插件是MyBatis提供的一个非常强大的机制，我们可以通过插件来修改MyBatis的一些核心行为。插件通过动态代理机制，可以介入四大对象的任何一个方法的执行。
•Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)

•ParameterHandler (getParameterObject, setParameters)

•ResultSetHandler (handleResultSets, handleOutputParameters)

•StatementHandler (prepare, parameterize, batch, update, query)


## 6、environments（环境）

default属性：默认使用哪个环境；填写某个environment标签的id
environment标签：配置一个具体的环境；每一个环境都需要一个事务管理器和数据源
	id属性：当前环境的唯一标识
	transactionManager标签：事务管理器后来数据源、事务控制管理都Spring来做

```xml
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <!--${}取出配置文件中的值-->
            <property name="driver" value="${driver}"/>
            <property name="url" value="${url}"/>
            <property name="username" value="${username}"/>
            <property name="password" value="${password}"/>
        </dataSource>
    </environment>
</environments>

```

## 7、databaseIdProvider（数据库厂商标识）

作用：mybatis用来考虑数据库移植性的-

name属性：数据库厂商标识  
value属性：给数据库厂商标识起别名 MYSQL、Oracle、SQL Server；
	

mybatis_config.xml

```xml
<databaseIdProvider type="DB_VENDOR">
    <property name="MYSQL" value="mysql"/>
    <property name="SQL Server" value="sqlserver"/>
    <property name="Oracle" value="oracle"/>
</databaseIdProvider>

```

EmployeeDao.xml

databaseId属性：选择数据库厂商别名

```xml
<!--能精确匹配就精确匹配，不能就模糊匹配-->
<select id="getEmpById" resultType="com.liobio.bean.bean.Employee">
    select * from t_employee where id = #{id}
</select>
<select id="getEmpById" resultType="com.liobio.bean.Employee" databaseId="mysql">
    select * from t_employee where id = #{id}
</select>
<select id="getEmpById" resultType="com.liobio.bean.Employee" databaseId="oracle">
    select * from t_employee where id = #{id}
</select>
```

## 8.mappers（映射器）

既然 MyBatis 的行为已经由上述元素配置完了，我们现在就要来定义 SQL 映射语句了。 但首先，我们需要告诉 MyBatis 到哪里去找到这些语句。 在自动查找资源方面，Java 并没有提供一个很好的解决方案，所以最好的办法是直接告诉 MyBatis 到哪里去找映射文件。 你可以使用相对于类路径的资源引用，或完全限定资源定位符（包括 `file:///` 形式的 URL），或类名和包名等。

class属性：引用接口全类名,可以将xml和dao接口放在同一个文件目录下，并文件名和接口名相同
             resource属性：在类路径下找sql映射文件
             url属性：从磁盘和网络路径引用sql映射文件
    						配合使用：重要的dao写配置；简单的用头注解搞定
package标签：批量注册；要求xml和dao类接口在同一个文件夹下且名字相同（可以使用设置资源文件）

name属性：dao所在的包名

例如：

```xml
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>
<!-- 使用完全限定资源定位符（URL） -->
<mappers>
  <mapper url="file:///var/mappers/AuthorMapper.xml"/>
  <mapper url="file:///var/mappers/BlogMapper.xml"/>
  <mapper url="file:///var/mappers/PostMapper.xml"/>
</mappers>
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```

# 四、SQL映射文件

SQL 映射文件只有很少的几个顶级元素（按照应被定义的顺序列出）：

- `cache` – 该命名空间的缓存配置。
- `cache-ref` – 引用其它命名空间的缓存配置。
- `resultMap` – 描述如何从数据库结果集中加载对象，是最复杂也是最强大的元素。
- `parameterMap` – 老式风格的参数映射。此元素已被废弃，并可能在将来被移除！请使用行内参数映射。文档中不会介绍此元素。
- `sql` – 可被其它语句引用的可重用语句块。
- `insert` – 映射插入语句。
- `update` – 映射更新语句。
- `delete` – 映射删除语句。
- `select` – 映射查询语句。

## 1、增删改标签

> —insert – 映射插入语句
> —update – 映射更新语句
> —delete – 映射删除语句

**id要对应实现的方法名**

```xml
<select id="getEmpById" resultType="com.liobio.bean.Employee" >
    select * from t_employee where id = #{id}
</select>
```

![image-20210820214336989](image-20210820214336989.png)

**数据库支持主键**：

dao.xml

```xml
</select>
<!--让MyBatis自动的将自增的id赋值给传入的employee对象的id属性
        useGeneratedKeys属性：开启自动赋值id功能
        keyProperty属性：将刚才自增的id封装给那个属性
-->
<insert id="insertEmp" useGeneratedKeys="true" keyProperty="id">
    insert into t_employee(empname,gender,email)values(#{empName},#{gender},#{email})
</insert>

```

**数据库不支持主键：selectKey**

![image-20210820214311683](image-20210820214311683.png)

```xml
<!--查询主键
order="BEFORE":
在核心Sq1语句之前先运行一个查询sq1查到id; 将查到的i d赋值给javaBe
-->
<selectKey order= "BEFORE" resultType="integerl" keyPrselect ="id">
         select max(id)+1 from t_ employee
</selectKey>
INSERT INTO t_ employee( id , empname , gender, email)
VALUES(#{id},#{empName},#gender,,#{email})


```

通过order属性设置运行顺序，keyProperty属性来设置查询后结果赋值给javabean的哪个对象

然后通过useGeneratedKeys属性设置打开获取主键，keyProperty属性设置javabean的id属性接收结果值

```xml
<insert id="insertEmployee" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO t_ employee( empname, gender , email)
    VALUES(#{empName} , #{gender}, #{emai1})
</insert>                                                                     

```

## 2、参数（Parameters）传递

> 1、单个参数
> 	基本类型：
> 		取值：#{随便写}
> 2、多个参数：
> 		取值：#{参数名}是无效的的
> 			0,1（参数索引）或者param1，param2（第几个参数paramN）
> 	原因：
> 		只要传入多个参数，mybatis会自动的将这些参数封装到一个map中；封装时使用的key就是参数的索引和参数的第几个标识
> 		@param：为参数指定封装map时的key；命名参数
> 		我们可以告诉mybatis，封装参数map的时候别乱来
> 3、传入map
> 		封装多个参数为map，直接传递
> 4、传入bean
> 		取值：#{bean属性名}

## 3、参数处理

无论传入什么参数都要能正确的取出值;

### key/属性名

#{key }取值的时候可以设置一些规则:
id=#{id, jdbcType=INT};
javaType、jdbcType、mode、numericScale、resultMap、typeHandler、jdbc
只有jdbcType才可能是需要被指定的;默认不指定jdbcType; mysql没问题; oracle没问题;
万一传入的数据是null;mysql插入null没问题; oracle不知道nu11到底是什么类型; 

参数也可以指定一个特殊的数据类型:
*#{property,javaType= int , jdbcType=NUMERIC}*
*#{height , javaType=double, jdbcType=NUMERIC, numericScale=2}*
**javaType**通常可以从参数对象中来去确定

**如果null被当作值来传递，对于所有可能为空的列,jdbcType 需要被设置**

对于数值类型，还可以设置小数点后保留的位数:

**mode属性**允许指定IN, OUT或INOUT参数。如果参数为OUT或INOUT,参数对象属性的真实值将会被改变,就像在获取输出参数时所期望的那样。

### #{ }和￥{ }的区别

实际上在mybatis中:两种取值方式: 
#{属性名}:是参数预编译的方式，参数的位置都是用?替代，参数后来都是预编译设置进去的;安全，不会有sql注入
${属性名}:不是参数预编译，而是直接和sql语句进行拼串;不安全: 

一般都是使用#{}，安全，在不支持参数预编译的位置要进行取值就使用${ }	

### 查询返回集合

dao.java

```java
 public List<Employee> getAllEmployee();
```

dao.xml

```xml
<!--    public List<Employee> getAllEmployee();-->
<!--    resultType="":如果返回的是集合，写的是集合里面元素的类型-->
    <select id="getAllEmployee" resultType="com.liobio.bean.Employee">
        select *
        from t_employee
    </select>
```

test.java

```java
   public void test3() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            List<Employee>  employee = employeeDaoImpl.getAllEmployee();
            for (Employee allemployee: employee
                 ) {
                System.out.println(allemployee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }
```



### 查询返回Map

#### 单条记录返回Map

dao.java

```java
public interface EmployeeDao {
    /****
     *  列名为Key，值为value
     */
    public Map<String,Object> getEmpByIdReturnMap(Integer id);
}
```

dao.xml

```xml
<!--resultType属性中的map已经被mybatis自动写入别名为map了
列名作为key,值作为value-->
<select id="getEmpByIdReturnMap" resultType="map">
    select * from t_employee where id = #{id}
</select>
```

test.java

```java
public void test4() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            Map<String, Object> employeeByIdReturnMap = employeeDaoImpl.getEmployeeByIdReturnMap(1);
            System.out.println(employeeByIdReturnMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }
```



#### 多条记录返回Map

通过@MapKey()注解来告诉mybatis数据库中哪个字段作为key主键来，封装value

dao.java

```java
public interface EmployeeDao {
    //key是记录的主键，value就是记录封装好的对象
    //@MapKey根据数据库里面的哪个字段作为key来查询封装value
    @MapKey("id")
    public Map<Integer,Employee> getEmpsByIdReturnMap();
}
```

dao.xml

```xml
<!--查询多个的情况下，resultType属性写value封装的元素类型-->
<select id="getEmpsByIdReturnMap" resultType="com.liobio.bean.Employee">
    select * from t_employee
</select>
```

test.java

```java
    @Test
    public void test5() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可

            Map<Integer, Employee> allEmployeeByIdReturnMap = employeeDaoImpl.getAllEmployeeByIdReturnMap();
            System.out.println(allEmployeeByIdReturnMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }
```

#### 自定义封装规则resultMap

1. resultMap标签自定义封装
   1. type属性：指定引入哪个javabaen与数据库封装对应
   2. id属性：指定这个自定义封装的id，便于其他引用
      1. id标签：指定主键
      2. result标签：指定其他封装对象
         1. property属性：指定javabean属性名
         2. column属性：指定数据库字段名

​	

数据库表

![image-20210821001135614](image-20210821001135614.png)

Boss.java

```java
public class Boss {
    private Integer id;
    private String bName;
    private Integer bAge;
    private Integer bGender;
    }
```

BossDao.xml

```xml
<!--  自己定义每一列数据和javaBean的映射规则-->
<resultMap id="MyBoss" type="com.liobio.bean.Boss">
    <!--    指定主键列的对应规则;-->
    <!--    column=  :指定哪一列是主键列 -->
    <!--    property :指定哪个属性封装id这一列数据-->
    <id property="id" column="id"></id>

    <!--    普通列-->
    <result property="bName" column="bossName"/>
    <result property="bAge" column="bossAge"/>
    <result property="bGender" column="bossGender"/>
</resultMap>
<!--    查出数据封装结果的时候，使用resultMap自定义的规则-->
<select id="getBossByIdWithResultMap" resultMap="MyBoss">
    select *
    from t_boss
    where id = #{id}
</select>
<!--    Boss{id=1, bName='qwe', bAge=12, bGender=1}-->
```

## 4、联合查询

### 1、级联查询

sql语句

```sql
SELECT k.id,k.`keyname`,k.`lockId`,l.`id` lid,l.`lockName` 
FROM t_key k 
LEFT JOIN t_lock l 
ON k.`lockId`=l.`id`
WHERE k.`id`=1
```

![image-20210821160754543](image-20210821160754543.png)

bean.key lock对象

```java
public class Lock {
    private Integer id;//锁编号
    private String LockName;//锁名
    //省略get/set/toString/构造器
}

public class Key {
    private Integer id;//钥匙编号
    private String keyName;//钥匙名
    private Lock lock;//当前钥匙能开哪把锁
    //省略get/set/toString/构造器
}

```

keyDao

```java
public interface keyDao {
//将钥匙和锁信息一起查出
    public Key getKeyById(Integer id);
}
```

KeyDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liobio.dao.keyDao">
    <select id="getKeyById" resultMap="myKey">
        select k.id,k.`keyname`,k.`lockId`,l.`id` lid,l.`lockName` from t_key k left join t_lock l on k.`lockId`=l.`id`where k.`id`=1
    </select>
    <!--自定义封装规则-->
    <resultMap id="myKey" type="com.liobio.bean.Key">
        <id column="id" property="id"/>
        <result column="keyname" property="keyName"/>
        <!--级联配置查询-->
        <result column="lid" property="lock.id"/>
        <result column="lock.LockName" property="lockName"/>
    </resultMap>
</mapper>

```

test.java

```java
public class EmployeeDaoTest {
    SqlSessionFactory factory;
    @Before
    public void initSqlSessionFactory() throws IOException {
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //keyDao测试
    @Test
    public void test6(){
        SqlSession sqlSession = factory.openSession();
        keyDao mapper = sqlSession.getMapper(keyDao.class);
        Key keyById = mapper.getKeyById(2);
        System.out.println(keyById);
        //Key{id=1, keyName='1号钥匙', lock=Lock{id=1, LockName='null'}}
    }
}

```

### 2、联合查询association

KeyDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liobio.dao.keyDao">
    <select id="getKeyById" resultMap="myKey">
        select k.id,k.`keyname`,k.`lockId`,l.`id` lid,l.`lockName` from t_key k left join t_lock l on k.`lockId`=l.`id`where k.`id`=#{id}
    </select>
    <resultMap id="myKey" type="com.liobio.bean.Key">
        <id column="id" property="id"/>
        <result column="keyname" property="keyName"/>
        <!--若这个对象的属性是一个对象，自定义规则；可使用association标签定义联合查询

         private Integer id;//钥匙编号
         private String keyName;//钥匙名
         private Lock lock;//当前钥匙能开哪把锁

                property属性：指定要联合查询的对象
                javaType属性：指定这个javabean属性的类型的全类名
        -->

        <association property="lock" javaType="com.liobio.bean.Lock">
            <!--定义lock属性对于这个Lock对象如何封装
                property属性：指定对于javabean对象的属性
                column属性：指定数据库查询结果的字段名
            -->
            <id property="id" column="id"></id>
            <result property="LockName" column="lockName"></result>
        </association>
    </resultMap>
</mapper>

```

test.java

```java
public class EmployeeDaoTest {
    SqlSessionFactory factory;
    @Before
    public void initSqlSessionFactory() throws IOException {
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //keyDao测试
    @Test
    public void test6(){
        SqlSession sqlSession = factory.openSession();
        keyDao mapper = sqlSession.getMapper(keyDao.class);
        Key keyById = mapper.getKeyById(2);
        System.out.println(keyById);
        //Key{id=1, keyName='1号钥匙', lock=Lock{id=1, LockName='1号锁'}}
    }
}

```

### 3、Collection查询

sql语句

```sql
SELECT k.id,k.`keyname`,k.`lockId`,l.`id` lid,l.`lockName` 
FROM t_key k 
LEFT JOIN t_lock l 
ON k.`lockId`=l.`id`
WHERE k.`id`=3
```

bean对象

```java
public class Lock {
    private Integer id;//锁编号
    private String LockName;//锁名
    private List<Key> keys//很多把钥匙可以开这把锁
    //省略get/set/toString/构造器
}
public class Key {
    private Integer id;//钥匙编号
    private String keyName;//钥匙名
    private Lock lock;//当前钥匙能开哪把锁
    //省略get/set/toString/构造器
}

```

LockDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liobio.dao.LockDao">
    <select id="getLockById" resultMap="myLock">
        select k.kid,k.`keyname`,k.`lockId`,l.`id` id,l.`lockName` from t_key k left join t_lock l on k.`lockId`=l.`id`where k.`id`=#{id}
    </select>
    
    <resultMap id="myLock" type="com.liobio.bean.Lock">
        <id column="id" property="id"/>
        <result column="keyname" property="keyName"/>
        <!-- 
		collection：定义集合元素的封装
			property：指定bean对象属性中哪个集合对象
			ofType：指定bean对象属性的对象集合里面元素的类型
		-->
        <collection property="keys" ofType="com.liobio.bean.Key">
            <id property="id" column="kid"></id>
            <result property="keyName" column="keyname"></result>
        </collection>
    </resultMap>
    
</mapper>

```

test.java

```java
public class EmployeeDaoTest {
    SqlSessionFactory factory;
    @Before
    public void initSqlSessionFactory() throws IOException {
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //LockDao测试
    @Test
    public void test7(){
        SqlSession sqlSession = factory.openSession();
        keyDao mapper = sqlSession.getMapper(keyDao.class);
        Lock lockById = mapper.getLockById(3);
        System.out.println(keyById);
        //lock对象为null是因为没自定义配置association；需要则可以在resultMap中继续配置association来继续套娃
        //Key{id=3, keyName='303号钥匙1', lock=null}
       // Key{id=4, keyName='303号钥匙2', lock=null}
       // Key{id=5, keyName='303号钥匙3', lock=null}
    }
}

```

## 5、分步查询【不推荐】

虽然sql简单，但是会有大量性能浪费，虽然可以用过设置来减少性能浪费，但还是不推荐使用

### 1、association-分段查询

```xml
<resultMap type="com.liobio.bean.Lock" id= "myLock3">
	<id column="id" property="id"/>
	<result column="LockName"property="LockName "/>
	<association property="key
		select="com.liobio.dao.KeyMapper.getKeyById”
		column="key_id">
	</ association>
</resultMap>

```

select：调用目标的方法查询当前属性的值【方法全类名】

column：将指定字段值传入select属性调用的目标方法中

### 2、association-分段查询&延迟加载

开启延迟加载和属性按需加载

```xml
<settings>
	<setting name= "LazyLoadingEnabLed" value="true"/>
	<setting name="aggressiveLazyLoading" value="faLse"/>
</settings>
```

### 3、Collection-集合类型&嵌套结果集

```xml
<select id="getDeptById" resultMap= "MyDept">
	SELECT d.id d_id,d.dept_name d_deptName, 
	e.id e_id,e.last_name e_lastName,e.email e_email,
	e.gender e_gender,e.dept_ id e_deptId
	FROM department d
	LEFT JOIN employee e ON e.'dept_id'='d.id'
	WHERE d.'id'=#{id}
</select>

```

```xml
<resultMap type= "com.liobio.bean.Department"id= "MyDept">
	<id column= "d_id" property="id"/>
	<result column= "d_ deptName" property= "deptName"/ >
	<collection property= "emps" ofType= "com.liobio.bean.EmpLoyee"
		columnPrefix="e_">
		<id column= "id" property="id"/>
		<result column= "LastName" property= "LastName "/ >
		<result column= "email" property= "email"/>
		<result column= "gender" property= "gender"/ >
	</collection>
</ resultMap>

```

### 4、Collection-分步查询&延迟加载

```xml
<resultMap type= "com.atguigu.bean.Department" id= "MyDeptStep">
	<id column= "id" property= "id"/>
	<result column= "dept_ name" property= "deptName"/>
	<collection property= "emps"
		select="com.atguigu.dao.EmpLoyeeMapper.getEmpsByDeptId"
		column="id">
	</collection>
</resultMap>

```

# 五、动态SQL

动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。

使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。

如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

bean.Teacher.java

```java
public class Teacher {

    private Integer id;
    private String name;
    private String course;
    private String address;
    private Date birth;
}

```

dao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liobio.dao.TeacherDao">

    <resultMap id="teacherMap" type="com.liobio.bean.Teacher">
        <id property="id" column="id"/>
        <result property="name" column="teacher_name"/>
        <result property="course" column="course"/>
        <result property="address" column="address"/>
        <result property="birth" column="birth_date"/>
    </resultMap>
```



## 1、if标签

dao.java

```java
public List<Teacher> getTeacherByCondition(Teacher teacher);
```

dao.xml

```xml
<select id="getTeacherByCondition" resultMap="teacherMap">
        SELECT * FROM t_teacher WHERE
        <!--test属性：指定编写判断条件
            id!=null：取出传入的javabean属性中id的值，判断其是否为空
        -->
        <if test="id!=null">
            id > #{id} AND
        </if>
        <if test="name!=null and !name.equals(&quot;&quot;) ">
            teacherName like #{name} AND
        </if>
        <if test="birth!=null">
            birth_date &lt; #{birth}
        </if>
    </select>
```

test.java

```java
 public void test2() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher = new Teacher();
            teacher.setId(1);
            teacher.setName("%a%");
            teacher.setAddress("%地址%");
            List<Teacher> teacherByCondition = mapper.getTeacherByCondition(teacher);
            for (Teacher temp:teacherByCondition){
                System.out.println(temp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```



## 2、where标签

dao.java

```java
public List<Teacher> getTeacherByCondition(Teacher teacher);
```

dao.xml

```xml

<select id="getTeacherByCondition" resultMap="teacherMap">
    SELECT * FROM t_teacher
    <!--where标签：可以帮我们去除掉前面的and-->
    <where>
            <if test="id!=null">
                id > #{id}
            </if>
            <if test="name!=null and !name.equals(&quot;&quot;) ">
                AND teacherName like #{name}
            </if>
            <if test="address!=null and !address.equals(&quot;&quot;)">-->
                AND address like #{address}-->
            </if>-->
    </where>
</select>
```

test.java

```java
 public void test2() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher = new Teacher();
            teacher.setId(1);
            teacher.setName("%a%");
            teacher.setAddress("%地址%");
            List<Teacher> teacherByCondition = mapper.getTeacherByCondition(teacher);
            for (Teacher temp:teacherByCondition){
                System.out.println(temp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```



## 3、trim标签

如果 *where* 元素与你期望的不太一样，你也可以通过自定义 trim 元素来定制 *where* 元素的功能

dao.java

```java
public List<Teacher> getTeacherByCondition(Teacher teacher);
```

dao.xml

```xml
<select id="getTeacherByCondition" resultMap="teacherMap">
    SELECT * FROM t_teacher
	<!--截取字符串
        prefix属性：为下面的sql整体添加一个前缀
        prefixOverrides属性：取出整体字符串钱多多余的字符
        suffix：为整体添加一个后缀
        suffixOverrides：后面哪个多了可以去掉
    -->
    <trim prefix="where" prefixOverrides="AND | OR">
            <if test="id!=null">
                And id > #{id}
            </if>
            <if test="name!=null and !name.equals(&quot;&quot;)">
                AND teacher_name like #{name}
            </if>
            <if test="address!=null and !address.equals(&quot;&quot;)">
                AND address like #{address}
            </if>
    </trim>
</select>
```

test.java

```java
 public void test2() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher = new Teacher();
            teacher.setId(1);
            teacher.setName("%a%");
            teacher.setAddress("%地址%");
            List<Teacher> teacherByCondition = mapper.getTeacherByCondition(teacher);
            for (Teacher temp:teacherByCondition){
                System.out.println(temp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```



## 4、foreach遍历元素

动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）

*foreach* 元素的功能非常强大，它允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。它也允许你指定开头与结尾的字符串以及集合项迭代之间的分隔符。这个元素也不会错误地添加多余的分隔符，看它多智能！

**提示** 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 *foreach*。当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

dao.java

```java
public List<Teacher> getTeacherByIdIn(@Param("ids") List<Integer> ids);
```

dao.xml

```xml
    <select id="getTeacherByIdIn" resultMap="teacherMap">
        SELECT * FROM t_teacher WHERE id IN
        <!--foreach遍历集合
            collection属性：指定要遍历集合的key
            close属性：以书什么结束
            item属性：每次遍历的元素，命名任意，方便引用
            index属性：
                如果遍历的是一个list，指定变量保存的当前元素的索引
                    item为值
                如果遍历的是一个map，指定变量保存的当前元素的key
                    item为value值
            open属性：以什么开始
            separator属性：每次遍历元素的分隔符
        -->
        <foreach collection="ids" open="(" close=")" item="id_item"  separator=",">
            #{id_item}
        </foreach>
    </select>
```

test.java

```java
 public void test3() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            List<Teacher> teacherByIdIn = mapper.getTeacherByIdIn(Arrays.asList(1,2,4));
            for (Teacher temp:teacherByIdIn){
                System.out.println(temp);
                //Teacher{id=1, name='qqq', course='语文', address='地址一', birth=2021-01-01}
                //Teacher{id=2, name='www', course='数学', address='地址二', birth=2022-02-02}
                //Teacher{id=4, name='aaa', course='物理', address='地址四', birth=2024-04-04}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```



## 5、choose标签

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

dao.java

```java
public List<Teacher> getTeacherByConditionChoose(Teacher teacher);
```

dao.xml

```xml
<select id="getTeacherByConditionChoose" resultMap="teacherMap">
        select * from t_teacher
        <where>
--             when标签：设置情况；满足后其他情况跳过
--
--             otherwise标签：当所有都不情况都不满足时，就执行此标签
--
--             类似ifelse-else
            <choose>
                <when test="id!=null">
                    id=#{id}
                </when>
                <when test="name!=null and name.equals(&quot;&quot;)">
                    teacher_name=#{name}
                </when>
                <when test="address!=null and !address.equals(&quot;&quot;)">
                    birth_date=#{address}
                </when>
                <otherwise>
                    1=1
                </otherwise>
            </choose>
        </where>
    </select>
```

test.java

```java
public void test4() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher = new Teacher();
            teacher.setId(1);
            teacher.setName("%a%");
            teacher.setAddress("%地址%");
            List<Teacher> teacherByConditionChoose = mapper.getTeacherByConditionChoose(teacher);
            for (Teacher temp:teacherByConditionChoose){
                System.out.println(temp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```



## 6、set标签

dao.java

```java
 public int updateTeacher(Teacher teacher);
```

dao.xml

```xml
<update id="updateTeacher">
        UPDATE t_teacher
        <set>
            <if test="name!=null and !name.equals(&quot;&quot;)">
                teacher_name=#{name},
            </if>
            <if test="course!=null and !course.equals(&quot;&quot;)">
                course=#{course},
            </if>
            <if test="address!=null and !address.equals(&quot;&quot;)">
                address=#{address},
            </if>
        </set>
        <where>
            id=#{id}
        </where>
    </update>
```

test.java

```java
 public void test5() throws IOException {
        initSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher = new Teacher();
            teacher.setId(1);
            teacher.setName("liobio");
            teacher.setAddress("home");
            int i = mapper.updateTeacher(teacher);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

![image-20210821230424769](image-20210821230424769.png)

## 7、include标签

dao.java

```java
public Teacher getTeacherById(Integer id);
```

dao.xml

```xml

<!--    抽取可重用的sql语句-->
    <sql id="selectSql">
        SELECT *
        FROM t_teacher
    </sql>
    <select id="getTeacherById" resultMap="teacherMap">
        <include refid="selectSql"></include>
        WHERE id = #{id}
    </select>

```

test.java

```java
    //查询
    @Test
    public void test1() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            TeacherDao teacherDao = openSession.getMapper(TeacherDao.class);
            Teacher teacherById = teacherDao.getTeacherById(1);
            System.out.println(teacherById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }
```



## 8、bind标签【不推荐】

bind 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。比如：

```xml
<select id= "getEmpByLastNameLike" resultType= "com.liobio.bean.Employee">
<bind name= "myLastName" value="'%'+_LastName+'%'"/>
select * from employee where last_name like #{ myLastName }
</select>

```

但是后期更改匹配规则麻烦

## 9、OGNL表达式

**OGNL**（ **Object Graph Navigation Language** ）对象图导航语言（类似级联属性）

这是一种强大的表达式语言，通过它可以非常方便的来操作对象属性。类似于我们的EL，SpEL等

![image-20210821234905685](image-20210821234905685.png)

![image-20210821234923176](image-20210821234923176.png)



# 六、缓存机制

MyBatis 包含一个非常强大的查询缓存特性,它可以非常方便地配置和定制。缓存可以极大的提升查询效率

缓存：暂时的存储一些数据；加快系统的查询速度

MyBatis系统中默认定义了两级缓存。

**一级缓存**：线程级别的缓存；本地缓冲；SqlSession级别的缓存

**二级缓存**：全局范围的缓存；除过当前缓冲；SqlSession能用以外其他也而已使用

1、默认情况下，只有一级缓存（SqlSession级别的缓存，也称为本地缓存）开启。

2、二级缓存需要手动开启和配置，他是基于namespace级别的缓存。

3、为了提高扩展性。MyBatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存

## 1、一级缓存

只要之前查询过的数据，mybatis就会保存在一个缓存中（Map）;下次获取直接从缓冲中拿

一级缓存(local cache), 即本地缓存, 作用域默认为sqlSession。当 Session flush 或 close 后, 该 Session 中的所有 Cache 将被清空。

本地缓存不能被关闭, 但可以调用 clearCache() 来清空本地缓存, 或者改变缓存的作用域.

### 1、一级缓存简单使用

```java
public class DaoTest {
    SqlSessionFactory factory;
    @Before
    public void initSqlSessionFactory() throws IOException {
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    //体会一级缓存
    @Test
    public void test1(){
        SqlSession sqlSession = factory.openSession();
        Teacher teacherById;
        try {
            TeacherDao mapper = sqlSession.getMapper(TeacherDao.class);
            Teacher teacher1 = mapper.getTeacherById(1);
            System.out.println(teacher1);
            System.out.println("====================");
            Teacher teacher2 = mapper.getTeacherById(1);
            System.out.println(teacher2);
            System.out.println(teacher1==teacher2);//true
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }

}

```

```
只发送给数据库一次数据查询，且两次获取到的数据内存地址相同
```

### 2、一级缓存失效的情况

#### **a、不同的SqlSession，使用不同的一级缓存；他会存储在自己的SqlSession的Map中**

> 只有在同一个SqlSession期间查询到的数据会保存到这个SqlSession中的缓存中
>
> 下次使用SqlSession查询会从缓存中拿

```java
    @Test
    public void test2(){
        //1、第一个会话
        SqlSession sqlSession1 = factory.openSession();
        TeacherDao teacherDao = sqlSession1.getMapper(TeacherDao.class);
        Teacher teacher1 = teacherDao.getTeacherById(1);
        System.out.println(teacher1);

        //2、第二个会话
        SqlSession sqlSession2 = factory.openSession();
        TeacherDao teacherDao2 = sqlSession2.getMapper(TeacherDao.class);
        Teacher teacher2 = teacherDao2.getTeacherById(1);
        System.out.println(teacher2);

        System.out.println(teacher1==teacher2);//false

        //3、关闭两个会话
        sqlSession1.close();
        sqlSession2.close();
    }

```

结果：

```
两次会话，分别都会想数据库发送sql语句请求查询，并判断返回结果的地址值不同
```

#### **b、同一个方法，不同的参数，由于可能之前没查询过，还会发送新的sql查询请求**

```java
@Test
public void test2(){
    //一次会话
    SqlSession sqlSession1 = factory.openSession();
    TeacherDao teacherDao = sqlSession1.getMapper(TeacherDao.class);
    Teacher teacher1 = teacherDao.getTeacherById(1);
    Teacher teacher2 = teacherDao.getTeacherById(2);
    System.out.println(teacher1);
    System.out.println(teacher2);
    sqlSession1.close();
}

```

```
相同的sql语句，当时发送的参数不同，也会发送sql请求
```

#### **c、在这个sqlsession期间执行上任何一次增删改操作，增删改操作会把缓存清空**

```java
    @Test
    public void test2(){
        //1、一次会话
        SqlSession sqlSession1 = factory.openSession();
        TeacherDao teacherDao = sqlSession1.getMapper(TeacherDao.class);
        Teacher teacher1 = teacherDao.getTeacherById(1);
        System.out.println(teacher1);
        System.out.println("=====================");

        //执行任何一个增删改操作
        Teacher teacher = new Teacher();
        teacher.setId(3);
        teacher.setName("刘备");
        teacherDao.updateTeacher(teacher);


        System.out.println("=====================");
        Teacher teacher2 = teacherDao.getTeacherById(1);
        System.out.println(teacher2);
        sqlSession1.commit();
        sqlSession1.close();

    }

```

```
中间的增删改数据会清空缓存
```

#### **d、手动清空一级缓存**

**clearCache()**：清空当前sqlsession的一级缓存

```java
    @Test
    public void test2(){
        //1、一次会话
        SqlSession sqlSession1 = factory.openSession();
        TeacherDao teacherDao = sqlSession1.getMapper(TeacherDao.class);
        Teacher teacher1 = teacherDao.getTeacherById(1);
        System.out.println(teacher1);
        System.out.println("=====================");

        //手动清空缓存
        System.out.println("手动清空缓存");
        //clearCache()清空当前sqlsession的一级缓存
        sqlSession1.clearCache();

        System.out.println("=====================");
        Teacher teacher2 = teacherDao.getTeacherById(1);
        System.out.println(teacher2);
        sqlSession1.commit();
        sqlSession1.close();

    }

```

每次查询，先看一级缓存中有没有相关数据，如果没有再去发送新的sql

每一个sqlsession都拥有自己的一级缓存

## 2、二级缓存

- 二级缓存(second level cache)，全局作用域缓存
- **二级缓存在SqlSession关闭或提交**之后才会生效
- 二级缓存默认不开启，需要手动配置
- MyBatis提供二级缓存的接口以及实现，缓存实现要求POJO实现Serializable接口

### 1、开启二级缓存

在mybatis全局配置文件中下配置

```xml
<settings>
    <!--开启全局缓存-->
    <setting name="cacheEnabled" value="true"/>
</settings>

```

给需要使用缓存的dao功能配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liobio.dao.TeacherDao">
    <!--使用二级缓存-->
    <cache></cache>
</mapper>

```

test.java

```java
    @Test
    public void test3(){
        SqlSession sqlSession1 = factory.openSession();
        SqlSession sqlSession2 = factory.openSession();
        TeacherDao teacherDao1 = sqlSession1.getMapper(TeacherDao.class);
        TeacherDao teacherDao2 = sqlSession2.getMapper(TeacherDao.class);

        //第一个dao查询1号teacher信息
        Teacher teacherDao1TeacherById = teacherDao1.getTeacherById(1);
        System.out.println(teacherDao1TeacherById);
        //第二个dao查询1号teacher信息
        Teacher teacherDao2TeacherById = teacherDao2.getTeacherById(1);
        System.out.println(teacherDao2TeacherById);

        //对比两个查询结果
        System.out.println(teacherDao1TeacherById==teacherDao2TeacherById);//

    }

```

### 2、**缓存的查询顺序**

1. 一级缓存和二级缓存不会有同一个数据

   1. 二级缓存中的数据：一级缓存关闭了就将数据放入二级缓存
   2. 一级缓存中的数据：二级缓存中没有的数据，就会放在一级缓存，一级缓存也没有就去查数据库；查完放一级缓存中

2. 任何时候都是先看二级缓存，再看一级缓存；如果大家都没有，就去查询数据库

   

### 3、**查询原理流程图**

![image-20210822231138113](image-20210822231138113.png)

每一个Dao有他自己的二级缓存，每次关闭会话后一级缓存会将数据传给当前Dao的二级缓存保存

每次查询都从他的Dao中先查询二级缓存，没有就去查询这次会话的一级缓存，没有就去数据库查询

## 3、缓存的设置

### 1、缓存在全局xml配置属性

**eviction=“FIFO：缓存回收策略：**

> LRU – 最近最少使用的：移除最长时间不被使用的对象。
>
> FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
>
> SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。
>
> WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。
>

> 默认的是 LRU。
>

**flushInterval：刷新间隔，单位毫秒**
默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新

**size：引用数目，正整数**
代表缓存最多可以存储多少个对象，太大容易导致内存溢出

***eadOnly：只读，true/false**
true：只读缓存；

​	会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。

false：读写缓存

​	会返回缓存对象的拷贝（通过序列化）。这会慢一些，但是安全，因此默认是 false。

### 2、缓存在dao.xml配置表标签的属性

**1、全局setting的cacheEnable：**

​	–配置二级缓存的开关。一级缓存一直是打开的。

2、select标签的useCache属性：

​	–配置这个select是否使用二级缓存。一级缓存一直是使用的

**3、sql标签的flushCache属性：**

​	–增删改默认flushCache=true。sql执行以后，会同时清空一级和二级缓存。查询默认flushCache=false。

4、sqlSession.clearCache()：

​	–只是用来清除一级缓存。

**5、当在某一个作用域 (一级缓存Session/二级缓存Namespaces) 进行了 C/U/D 操作后，默认该作用域下所有 select 中的缓存将被clear。**

## 4、第三方缓存整合

### 1、导包

​	ehcache-core-2.6.8.jar

​	mybatis-ehcache-1.0.3.jar

​	slf4j-api-1.7.21.jar

​	slf4j-log4j12-1.7.21.jar

### 2、ehcache要工作需要一个配置文件：

​	文件名叫ehcache.xml

​	放在类路径的根目录下

### 3、在sql映射文件中配置使用自定义缓存

通过type属性引入EhcacheCache的全类名

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

或者缓存引用，通过namespace属性：引入和那个dao接口使用同一个缓存机制

```xml
<cache-ref namespace="com.liobio.dao.TeacherDao"/>
```

