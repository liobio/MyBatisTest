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