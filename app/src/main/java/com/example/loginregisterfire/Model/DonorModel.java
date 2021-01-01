package com.example.loginregisterfire.Model;

public class DonorModel {

    private String donorName;
    private String donorEmail;
    private String hospitalName;
    private String time;
    private String rank;
    private boolean done;

    public DonorModel() {
    }

    public DonorModel(String donorName, String donorEmail, String hospitalName, String time, boolean done, String rank) {
        this.donorName = donorName;
        this.donorEmail = donorEmail;
        this.hospitalName = hospitalName;
        this.time = time;
        this.done = done;
        this.rank = rank;
    }


    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
