package com.sopt.lang.Chatting.Recommend;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRecommendData {
    int profile;
    public String name;
    public String myLang;
    public String wishLang;

    public  ChattingRecommendData(int profile, String name,String myLang,String wishLang){
        this.profile=profile;
        this.name = name;
        this.myLang = myLang;
        this.wishLang = wishLang;
    }
}
