package com.meiye.bo.user;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserBoMapper {
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    UserBo getUserById(Long id);

    @Select("SELECT * FROM t_user")
    public List<UserBo> getUserList();

    @Insert("insert into t_user(name,age) values(#{name}, #{age})")
    public int add(UserBo user);

    @Update("UPDATE t_user SET username = #{user.name} , age = #{user.age} WHERE id = #{id}")
    public int update(@Param("id") Long id, @Param("user") UserBo user);

    @Delete("DELETE from t_user where id = #{id} ")
    public int delete(Long id);
}
