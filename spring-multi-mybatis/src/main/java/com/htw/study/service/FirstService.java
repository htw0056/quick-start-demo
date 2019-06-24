package com.htw.study.service;

import com.htw.study.dao.first.FirstUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by htw on 2019/6/23.
 */
@Service
public class FirstService {
    @Autowired
    private FirstUserMapper firstUserMapper;

    public String getUserName() {
        return firstUserMapper.get(1).getName();
    }
}
