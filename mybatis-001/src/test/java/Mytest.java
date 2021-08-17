import com.liobio.bean.Employee;
import com.liobio.dao.EmployeeDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName Mytest.java
 * @createTime 2021/08/16/23:32:00
 * @Description
 */
public class Mytest {

    SqlSessionFactory sqlSessionFactory;

    public void initSqlSessionFactory() throws IOException {
        //1、根据全局配置文件创建出一个sqlSessionFactory
        // SqlSessionFactory:是SqlSession工厂，负责创建SqlSession对象
        //SqlSession对象：sql会话---(代表和数据库的一次会话)
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }


    //查询
    @Test
    public void test1() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            Employee employee = employeeDaoImpl.getEmployeeById(1);
            System.out.println(employee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }

    }

    //增添
    @Test
    public void test2() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        //openSession(true)设置自动提交事务
        SqlSession openSession = sqlSessionFactory.openSession(true);
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            int i = employeeDaoImpl.insertEmployee(new Employee(null, "张三", 0, "zs@qq.com"));
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //手动提交
            openSession.commit();
            openSession.close();
        }
    }

    //修改
    @Test
    public void test3() throws IOException {
        initSqlSessionFactory();
        //openSession(true)设置自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            EmployeeDao employeeDaoImpl = sqlSession.getMapper(EmployeeDao.class);
            int i = employeeDaoImpl.updateEmployee(new Employee(2, "廖标", 0, "liobio@qq.com"));
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
    //删除
    @Test
    public void test4() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        //openSession(true)设置自动提交事务
        SqlSession openSession = sqlSessionFactory.openSession(true);
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            int i = employeeDaoImpl.deleteEmployeeById(4);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //手动提交
            openSession.commit();
            openSession.close();
        }
    }


}
