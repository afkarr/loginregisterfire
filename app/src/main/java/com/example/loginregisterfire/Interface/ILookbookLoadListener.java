package com.example.loginregisterfire.Interface;

import com.example.loginregisterfire.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener {

    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
