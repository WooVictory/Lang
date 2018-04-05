package com.sopt.lang.Network.Alarm

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-09.
 */
data class AlarmMyDataResponse(
        @SerializedName("data")
        var data : List<AlarmMyData>
) : BaseModel()

data class AlarmMyData(
        var user_id : String,
        var user_image : String,
        var my_notification_id : Int,
        var lounge_id : Int,
        var noti_content : String,
        var noti_time : String,
        var noti_read_check : Int
)