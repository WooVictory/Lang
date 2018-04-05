package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-08.
 */
data class OtherMeetingListResponse (

        @SerializedName("data")
        var data : List<OtherMeetingListData>

) : BaseModel()

data class OtherMeetingListData(
        var meeting_image : String,
        var meeting_id : Int,
        var meeting_title : String,
        var meeting_lang : String,
        var meeting_type : String,
        var meeting_start_time : String,
        var user_name : String
)