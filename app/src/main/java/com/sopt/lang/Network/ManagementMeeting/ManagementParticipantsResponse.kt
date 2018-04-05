package com.sopt.lang.Network.ManagementMeeting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class ManagementParticipantsResponse(

        @SerializedName("data")
        var data : List<ManagementParticipantsData>

) : BaseModel()

data class ManagementParticipantsData(
        var meeting_user_id : Int,
        var user_id : String,
        var user_name : String,
        var user_image : String,
        var approval_state : Int
)