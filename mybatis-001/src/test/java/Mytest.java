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

        Employee employee = new Employee();
        //2、获取和 数据库的一次会话；与getConnection()；拿到一条连接
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3、使用SqlSession操作数据库，获取到dao接口的实现
            EmployeeDao employeeDaoImpl = openSession.getMapper(EmployeeDao.class);
            //4、拿到dao接口impl实现类后，调用相对于的方法即可
            employee = employeeDaoImpl.getEmployeeById(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
        System.out.println(employee);
    }

}
