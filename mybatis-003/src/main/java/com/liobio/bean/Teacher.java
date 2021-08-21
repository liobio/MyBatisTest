package com.liobio.bean;

import java.sql.Date;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName Teacher.java
 * @createTime 2021/08/21/16:52:00
 * @Description
 */
public class Teacher {

    private Integer id;
    private String name;
    private String course;
    private String address;
    private Date birth;

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", address='" + address + '\'' +
                ", birth=" + birth +
                '}';
    }

    public Teacher() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Teacher(Integer id, String name, String course, String address, Date birth) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.address = address;
        this.birth = birth;
    }
}
