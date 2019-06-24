package com.htw.study.dao.first;

import com.htw.study.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by htw on 2019/6/23.
 */
@Component
@Mapper
public interface FirstUserMapper {
    @Select("select id,name from user where id = #{id}")
    User get(@Param("id") Integer id);
}
