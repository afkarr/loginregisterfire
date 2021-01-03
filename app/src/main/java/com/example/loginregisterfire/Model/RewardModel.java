package com.example.loginregisterfire.Model;

public class RewardModel {
    private String rewardDesc;
    private long donationReq;

    public RewardModel() {
    }

    public RewardModel(String rewardDesc, long donationReq) {
        this.rewardDesc = rewardDesc;
        this.donationReq = donationReq;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }

    public double getDonationReq() {
        return donationReq;
    }

    public void setDonationReq(long donationReq) {
        this.donationReq = donationReq;
    }
}
