package com.liobio.dao;

import com.liobio.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName EmployeeDao.java
 * @createTime 2021/08/16/19:29:00
 * @Description
 */
public interface EmployeeDao {

    //key是记录的主键，value就是记录封装好的对象
    //@MapKey根据数据库里面的哪个字段作为key来查询封装value
    @MapKey("id")
    public Map<Integer,Employee> getAllEmployeeByIdReturnMap();

    public Map<String,Object> getEmployeeByIdReturnMap(Integer id);

    public List<Employee> getAllEmployee();

    public Employee getEmployeeById(Integer id);

    public Employee getEmployeeByIdAndName(@Param("id")Integer id,@Param("employee_name") String employee_name);

    public Employee getEmployeeByIdAndNameWithMap( Map<String ,Object> map);

    public int updateEmployee(Employee employee);

    public int deleteEmployeeById(Integer id);

    public  int insertEmployee(Employee employee);


}
