package com.example.loginregisterfire.Model;

public class BookingInformation {
    private String customerName, customerPhone, time, sectionId, sectionName, hospitalId, hospitalName, hospitalAddress;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String sectionId, String sectionName, String hospitalId, String hospitalName, String hospitalAddress, Long slot) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
