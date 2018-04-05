package com.sopt.lang.Network

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by johee on 2018-01-12.
 */
data class myInfoResponse(
        @SerializedName("data")
        var data: MyInfoData
) : BaseModel()

data class MyInfoData(
        var user_id: String,
        var user_name: String,
        var user_image: String)