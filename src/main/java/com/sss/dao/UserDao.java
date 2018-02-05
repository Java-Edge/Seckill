package com.sss.dao;

import com.sss.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author v_shishusheng
 */
@Mapper
@Repository
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(@Param("id") long id);


    @Update("update user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);

}
