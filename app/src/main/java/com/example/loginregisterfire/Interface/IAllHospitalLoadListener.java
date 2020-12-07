package com.example.loginregisterfire.Interface;

import java.util.List;

public interface IAllHospitalLoadListener {
    void onAllHospitalLoadSuccess(List<String> areaNameList);
    void onAllHospitalLoadFailed(String message);
}
