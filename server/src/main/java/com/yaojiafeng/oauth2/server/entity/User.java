package com.yaojiafeng.oauth2.server.entity;

/**
 * @author yaojiafeng
 * @create 2018-03-22 下午5:14
 */
public class User {
    private int id;
    private String uname;

    public User(int id, String uname) {
        this.id = id;
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
