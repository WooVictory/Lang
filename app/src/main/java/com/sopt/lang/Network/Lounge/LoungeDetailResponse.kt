package com.sopt.lang.Network.Lounge

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class LoungeDetailResponse (

        @SerializedName("data")
        var data : LoungeDetailData? = null

) : BaseModel()

data class LoungeDetailData(
        var user_id : String,
        var user_image : String,
        var user_name : String,
        var lounge_time : String,
        var lounge_image : List<String>,
        var native_lang : String,
        var hope_lang : String,
        var lounge_content : String,
        var like_count : Int,
        var comment_count : Int,

        @SerializedName("comments")
        var comments : List<LoungeDetailComment>,

        var isLike : Boolean
){
        constructor() : this("", "", "",
                "", emptyList(), "",
                "", "", 0,0, emptyList<LoungeDetailComment>(),false
        )
}
data class LoungeDetailComment(
        var user_id : String,
        var user_image : String,
        var user_name : String,
        var comment_content : String,
        var comment_time : String
)