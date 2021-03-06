import com.liobio.bean.Boss;
import com.liobio.bean.Employee;
import com.liobio.dao.BossDao;
import com.liobio.dao.EmployeeDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName Mytest.java
 * @createTime 2021/08/19/18:39:00
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
    //多个参数
    @Test
    public void test1() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            Employee employee = employeeDaoImpl.getEmployeeByIdAndName(2,"廖标");
            System.out.println(employee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }

    }
    //将数据封装层map进行参数传递
    @Test
    public void test2() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可

            Map<String ,Object> map= new HashMap<String ,Object>();
            map.put("id",1);
            map.put("employee_name","admin");
            Employee employee = employeeDaoImpl.getEmployeeByIdAndNameWithMap(map);
            System.out.println(employee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }
    //查询返回集合
    @Test
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
    //查询返回map
    @Test
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

    @Test
    public void test6() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
           BossDao bossDao = openSession.getMapper(BossDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            Boss bossById = bossDao.getBossById(1);
            System.out.println(bossById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }

    }
    @Test
    public void test7() throws IOException {
        initSqlSessionFactory();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            BossDao bossDao = openSession.getMapper(BossDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            Boss bossById = bossDao.getBossByIdWithResultMap(1);
            System.out.println(bossById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }

    }

}
