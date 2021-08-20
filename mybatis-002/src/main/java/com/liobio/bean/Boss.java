package com.liobio.bean;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName Boss.java
 * @createTime 2021/08/20/23:17:00
 * @Description
 */
public class Boss {
    private Integer id;
    private String bName;
    private Integer bAge;
    private Integer bGender;

    @Override
    public String toString() {
        return "Boss{" +
                "id=" + id +
                ", bName='" + bName + '\'' +
                ", bAge=" + bAge +
                ", bGender=" + bGender +
                '}';
    }

    public Boss() {
    }

    public Boss(Integer id, String bName, Integer bAge, Integer bGender) {
        this.id = id;
        this.bName = bName;
        this.bAge = bAge;
        this.bGender = bGender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public Integer getbAge() {
        return bAge;
    }

    public void setbAge(Integer bAge) {
        this.bAge = bAge;
    }

    public Integer getbGender() {
        return bGender;
    }

    public void setbGender(Integer bGender) {
        this.bGender = bGender;
    }
}
