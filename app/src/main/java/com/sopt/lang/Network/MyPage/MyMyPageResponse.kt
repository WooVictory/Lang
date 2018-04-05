package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-08.
 */
data class MyMyPageResponse(

    @SerializedName("data")
    var data : MyMyPageData

) : BaseModel()

data class MyMyPageData(
        var user_id : String,
        var user_name : String,
        var user_email : String,
        var user_intro : String,
        var user_image : String,
        var device_token : String,
        var native_lang : String,
        var hope_lang : String,
        var following_count : Int,
        var follower_count : Int,
        var meetingNum : Int,
        var loungeNum : Int
)