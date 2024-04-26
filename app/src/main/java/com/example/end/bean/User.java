package com.example.end.bean;

public class User implements Comparable<User>{
    int id;
    String tel;
    String passWord;
    String rem;

    public User(int id, String tel, String passWord,String rem) {
        this.id = id;
        this.tel = tel;
        this.passWord = passWord;
        this.rem=rem;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    @Override
    public int compareTo(User user) {
        return this.id-user.id;
    }
}
