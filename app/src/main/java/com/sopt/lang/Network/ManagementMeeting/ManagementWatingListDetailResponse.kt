package com.sopt.lang.Network.ManagementMeeting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class ManagementWatingListDetailResponse (

        @SerializedName("data")
        var data : ManagementWatingListDetailData

) : BaseModel()

data class ManagementWatingListDetailData(
        var user_image : String,
        var user_name : String,
        var native_lang : String,
        var hope_lang : String,
        var apply_intro : String
)