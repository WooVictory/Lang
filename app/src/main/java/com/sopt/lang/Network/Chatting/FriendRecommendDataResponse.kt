package com.sopt.lang.Network.Chatting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-09.
 */
data class FriendRecommendDataResponse(
        @SerializedName("data")
        var data : List<FriendRecommendData>
) : BaseModel()

data class FriendRecommendData(
        var user_id : Int,
        var user_image : String,
        var user_name : String,
        var native_lang : String,
        var hope_lang : String
)