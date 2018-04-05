package com.sopt.lang.Network.Meeting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-09.
 */
data class MeetingDetailDataResponse(

        @SerializedName("data")
        var data : MeetingDetailData
) : BaseModel()

data class MeetingDetailData  (
        @SerializedName("info")
        var info : MeetingDetailInfo,
        @SerializedName("participants")
        var participants : List<MeetingDetailParticipants>,
        @SerializedName("recent_review")
        var recent_review : MeetingDetailRecentReview,
        var average_rating : String,
        var my_state : Int
)
//{
//        constructor() : this( , emptyList()<MeetingDetailParticipants>, object , "")
//}

data class MeetingDetailInfo(
        var meeting_id : Int,
        var hostName : String,

        var hostImage: String,
        var user_id : String,
        var meeting_image : String,
        var meeting_title : String,
        var meeting_lang : String,
        var meeting_intro : String,
        var meeting_type : Int,
        var meeting_start_time : Int,
        var meeting_end_time : Int,
        var meeting_day_of_week : Int,
        var meeting_date : String,
        var meeting_question : String,
        var place_lat : String,
        var place_lng : String,
        var review_count : Int
)

data class MeetingDetailParticipants(
        var user_id : String,
        var user_name : String,
        var user_image : String
)
data class MeetingDetailRecentReview(
        var user_id : String,
        var user_name : String,
        var user_image : String,
        var review_rating : String,
        var review_content : String
)