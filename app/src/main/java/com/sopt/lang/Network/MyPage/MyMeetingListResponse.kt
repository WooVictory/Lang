package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-08.
 */
data class MyMeetingListResponse (

        @SerializedName("data")
        var data : List<MyMeetingListData>

) : BaseModel()

data class MyMeetingListData(
        var meeting_image : String,
        var meeting_id :Int,
        var meeting_title : String,
        var meeting_lang : String,
        var meeting_type : Int,
        var approval_state : Int,
        var user_name :String

)