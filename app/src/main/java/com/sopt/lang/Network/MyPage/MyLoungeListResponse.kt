package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class MyLoungeListResponse (

        @SerializedName("data")
        var data : List<MyLoungeListData>? = null

) : BaseModel()

data class MyLoungeListData(

        var lounge_id : Int,
        var lounge_time : String,
        var lounge_content : String,
        var like_count : Int,
        var comment_count : Int,
        var user_id : String,
        var is_public : Int,
        var lounge_image : List<String?>?,
        var user_name : String,
        var user_email : String,
        var user_intro : String,
        var user_image : String,
        var device_token : String,
        var native_lang : String,
        var hope_lang : String,
        var following_count : Int,
        var follower_count : Int,
        var isLike : Boolean
)