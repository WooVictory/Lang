package com.sopt.lang.Network.MeetingReview

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class MeetingReviewListResponse (
        @SerializedName("data")
        var data : MeetingReviewData
): BaseModel()

data class MeetingReviewData(
        @SerializedName("reviewList")
        var reviewList : List<MeetingReviewList>,

        var average_rating : String
)

data class MeetingReviewList(
        var review_id : Int,
        var review_image : Array<String>,
        var review_rating : String,
        var review_content : String,
        var user_id : String,
        var user_name : String,
        var user_image : String
)