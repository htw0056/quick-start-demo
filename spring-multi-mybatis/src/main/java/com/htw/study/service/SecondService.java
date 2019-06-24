package com.htw.study.service;

import com.htw.study.dao.second.SecondUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by htw on 2019/6/23.
 */
@Service
public class SecondService {
    @Autowired
    private SecondUserMapper secondUserMapper;

    public String getUserName(){
        return secondUserMapper.get(2).getName();
    }
}
