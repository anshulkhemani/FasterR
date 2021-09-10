package com.example.fasterr;

public class UserDetailsObject {
    UserDetailsObject()
    {}
    UserDetailsObject(String name, String email, String usertype)
    {
        this.name = name;
        this.email = email;
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsertype() {
        return usertype;
    }

    private String name;
    private String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    private String usertype;

}
