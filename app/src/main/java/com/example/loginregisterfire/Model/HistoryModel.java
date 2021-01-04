package com.example.loginregisterfire.Model;

public class HistoryModel {

    private String donorName;
    private String donorEmail;
    private String hospitalName;
    private String time;
    private boolean done;

    public HistoryModel() {
    }

    public HistoryModel(String donorName, String donorEmail, String hospitalName, String time, boolean done) {
        this.donorName = donorName;
        this.donorEmail = donorEmail;
        this.hospitalName = hospitalName;
        this.time = time;
        this.done = done;
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
}
