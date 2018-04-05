package com.sopt.lang.Network.Meeting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-08.
 */
data class MeetingListDataResponse(
        @SerializedName("data")
        var data : List<MeetingListData>
) : BaseModel()

data class MeetingListData(
        var meeting_id : Int,
        var meeting_image : String,
        var meeting_lang : String,
        var meeting_type : Int,
        var meeting_title : String,
        var user_name : String,
        var isLike : Boolean
)