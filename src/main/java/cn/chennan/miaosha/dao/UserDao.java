package cn.chennan.miaosha.dao;

import cn.chennan.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author ChenNan
 * @date 2019/10/23
 **/
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into user(id, name) values(#{id}, #{name})")
    void insert(User u1);
}
