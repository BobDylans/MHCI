package com.example.mhcidemo.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mhcidemo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 使用注解指定SQL查询
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(String username);


    @Select("SELECT * FROM users WHERE username = #{phone}")
    User selectByPhone(String username);
    @Select("SELECT * FROM users")
    List<User> selectAll();

}

