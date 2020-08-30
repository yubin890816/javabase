package com.yubin.io.otherstream;

import java.io.Serializable;

/**
 * @author YUBIN
 * @create 2020-08-30
 */
public class Person implements Serializable {

    private static final long serialVersionUID = -6604635418783885699L;

    private Integer id;

    private String userName;

    transient private String password;

    public Person(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
