package com.udacity.superduperdrive.cloudstorage.mapper;

import com.udacity.superduperdrive.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT userid FROM USERS WHERE username = #{username}")
    Integer getUserIdByUsername(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insert(User user);

    @Update("UPDATE USERS SET password = #{password}, salt = #{salt} WHERE username = #{username}")
    boolean changePassword(String username, String password, String salt);

}