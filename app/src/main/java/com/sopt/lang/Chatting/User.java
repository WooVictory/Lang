package com.sopt.lang.Chatting;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018-01-13.
 */

public class User {

    private String id;
    private String name;
    private String token;
    private String image;

    public User(String id, String name, String token, String image) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.image = image;
    }

    @Exclude
    public Map<String, Object> userReg() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("token", token);

        return result;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
