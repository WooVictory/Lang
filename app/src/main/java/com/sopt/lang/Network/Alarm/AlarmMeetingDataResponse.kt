package com.sopt.lang.Network.Alarm

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-09.
 */
data class AlarmMeetingDataResponse(
        @SerializedName("data")
        var data : List<AlarmMeetingData>
) : BaseModel()

data class AlarmMeetingData(
        var meeting_noti_id : Int,
        var meeting_id : Int,
        var user_id : String,
        var noti_content : String,
        var noti_time : String,
        var noti_read_check : Int
)