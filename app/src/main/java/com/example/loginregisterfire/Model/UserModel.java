package com.example.loginregisterfire.Model;

public class UserModel {

    private String FullName;
    private String Email;
    private long Score;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public UserModel() {
    }

    public UserModel(String fullName, String email, long score, String url) {
        this.FullName = fullName;
        this.Email = email;
        this.Score = score;
        this.url = url;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public long getScore() {
        return Score;
    }

    public void setScore(long score) {
        Score = score;
    }
}