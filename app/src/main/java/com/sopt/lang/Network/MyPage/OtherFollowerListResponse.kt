package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class OtherFollowerListResponse (
        @SerializedName("data")
        var data : List<OtherFollowerListData>
) : BaseModel()

data class OtherFollowerListData(
        var user_id : String,
        var user_image : String,
        var user_name : String
)