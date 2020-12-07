package com.example.loginregisterfire.Interface;

import com.example.loginregisterfire.Model.Hospital;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Hospital> hospitalList);
    void onBranchLoadFailed(String message);
}
