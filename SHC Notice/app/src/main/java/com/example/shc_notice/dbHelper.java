package com.example.shc_notice;

public class dbHelper {
//sign_page Variables
    String fullname,username,password,acctype;
    public dbHelper() {
    }

    public dbHelper(String fullname, String username, String password,String acctype) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.acctype = acctype;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }
}
