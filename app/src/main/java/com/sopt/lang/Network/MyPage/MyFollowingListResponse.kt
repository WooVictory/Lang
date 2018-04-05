package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class MyFollowingListResponse (

        @SerializedName("data")
        var data : List<MyFollowingListData>

) : BaseModel()

data class MyFollowingListData(
        var user_id : String,
        var user_image : String,
        var user_name : String
)