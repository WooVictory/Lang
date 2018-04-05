package com.sopt.lang.Network.MyPage

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class OtherLoungeListResponse (

        @SerializedName("data")
        var data : List<OtherLoungeListData>

) : BaseModel()

data class OtherLoungeListData(

        var lounge_id : Int,
        var lounge_time : String,
        var lounge_content : String,
        var like_count : Int,
        var comment_count : Int,
        var user_id : String,
        var user_name : String,
        var user_image : String,
        var native_lang : String,
        var hope_lang : String,
        var isLike : Boolean,
        var lounge_image : List<String>

)