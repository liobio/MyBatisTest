package com.liobio.dao;

import com.liobio.bean.Boss;

/**
 * @author LIOBIO
 * @version 1.0.0
 * @ClassName BossDao.java
 * @createTime 2021/08/20/23:21:00
 * @Description
 */
public interface BossDao {

    public Boss getBossById(Integer id);

    public Boss getBossByIdWithResultMap(Integer id);
}
