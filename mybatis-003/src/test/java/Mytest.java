import com.liobio.bean.Teacher;
import com.liobio.dao.TeacherDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

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
            TeacherDao teacherDao = openSession.getMapper(TeacherDao.class);
            Teacher teacherById = teacherDao.getTeacherById(1);
            System.out.println(teacherById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }

    //动态sql 的 Where 便签 和 trim 标签
    @Test
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

    //foreach 标签
    @Test
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

    //使用choose标签
    @Test
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

    //使用set标签
    @Test
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



}
