package com.hsuhau.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String uid;
    private String uname;
    private Integer age;
    private String img;

    public User() {
    }

    public User(String uid, String uname, Integer age, String img) {
        this.uid = uid;
        this.uname = uname;
        this.age = age;
        this.img = img;
    }
}