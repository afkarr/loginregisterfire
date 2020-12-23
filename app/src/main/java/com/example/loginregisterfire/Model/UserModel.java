package com.example.loginregisterfire.Model;

public class UserModel {

    private String FullName;
    private String Email;
    private long Score;

    public UserModel() {
    }

    public UserModel(String fullName, String email, long score) {
        this.FullName = fullName;
        this.Email = email;
        this.Score = score;
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