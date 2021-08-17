package com.liobio.bean;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName Employee.java
 * @createTime 2021/08/16/19:21:00
 * @Description
 */
public class Employee {

    private Integer id;
    private String employee_name;
    private Integer gender;
    private String email;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employee_name='" + employee_name + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
