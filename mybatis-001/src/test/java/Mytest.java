import com.liobio.bean.Employee;
import com.liobio.dao.EmployeeDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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


    @Test
    public void test1() throws IOException {
        //1、根据全局配置文件创建出一个sqlSessionFactory
        //SqlSessionFactory:是SqlSession工厂，负责创建SqlSession对象
        //SqlSession对象：sql会话---(代表和数据库的一次会话)
        String resource = "mybatis_config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession openSession = sqlSessionFactory.openSession();
        EmployeeDao employeeDao = openSession.getMapper(EmployeeDao.class);
        Employee employee =employeeDao.getEmployeeById(1);
        System.out.println(employee);
    }
}
