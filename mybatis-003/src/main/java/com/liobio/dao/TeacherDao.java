package com.liobio.dao;

import com.liobio.bean.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName TeacherDao.java
 * @createTime 2021/08/21/17:01:00
 * @Description
 */
public interface TeacherDao {

    public Teacher getTeacherById(Integer id);

    public List<Teacher> getTeacherByCondition(Teacher teacher);

    public List<Teacher> getTeacherByIdIn(@Param("ids") List<Integer> ids);

    public List<Teacher> getTeacherByConditionChoose(Teacher teacher);

    public int updateTeacher(Teacher teacher);
}

