package com.liobio.dao;

import com.liobio.bean.Employee;
import org.apache.ibatis.annotations.Param;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName EmployeeDao.java
 * @createTime 2021/08/16/19:29:00
 * @Description
 */
public interface EmployeeDao {

    public Employee getEmployeeById(Integer id);

    public Employee getEmployeeByIdAndName(@Param("id")Integer id,@Param("employee_name") String employee_name);

    public int updateEmployee(Employee employee);

    public int deleteEmployeeById(Integer id);

    public  int insertEmployee(Employee employee);


}
