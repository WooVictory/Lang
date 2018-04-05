package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class MyFollowerListResponse(

        @SerializedName("data")
        var data: List<MyFollowerListData>

) : BaseModel()

data class MyFollowerListData(
        var user_id: String,
        var user_image: String,
        var user_name: String
)