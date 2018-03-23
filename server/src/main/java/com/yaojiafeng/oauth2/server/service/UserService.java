package com.yaojiafeng.oauth2.server.service;

import com.yaojiafeng.oauth2.server.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yaojiafeng
 * @create 2018-03-22 下午5:14
 */
@Service
public class UserService {
    public User selectByPrimaryKey(int id) {
        return new User(id, UUID.randomUUID().toString());
    }
}
