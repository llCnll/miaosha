package cn.chennan.miaosha.service;

import cn.chennan.miaosha.dao.UserDao;
import cn.chennan.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * @author ChenNan
 * @date 2019/10/23
 **/
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(Integer id){
        User user = userDao.getById(id);
        return user;
    }

    @Transactional
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("cn2");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("cn");
        userDao.insert(u2);

        return true;
    }
}
