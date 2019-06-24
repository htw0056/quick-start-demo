package com.htw.study.dao.second;

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
public interface SecondUserMapper {
    User get(@Param("id") Integer id);
}
