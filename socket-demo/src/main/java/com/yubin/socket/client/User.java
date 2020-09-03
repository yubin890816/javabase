package com.yubin.socket.client;

import java.io.Serializable;

/**
 * @author YUBIN
 * @create 2020-09-03
 */
public class User implements Serializable {

    private static final long serialVersionUID = -6341545366469706725L;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
