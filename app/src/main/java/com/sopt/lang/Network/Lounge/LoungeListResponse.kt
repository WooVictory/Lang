package com.sopt.lang.Network.Lounge

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by kor on 2018-01-09.
 */
data class LoungeListResponse (

        @SerializedName("data")
        var data : List<LoungeListData>

) : BaseModel()

data class LoungeListData(

        var user_id : String,
        var lounge_id : Int,
        var user_image : String,
        var user_name : String,
        var lounge_time : String,
        var lounge_image : List<String>,
        var lounge_content : String,
        var native_lang : String,
        var hope_lang : String,
        var like_count : Int,
        var comment_count : Int,
        var isLike : Boolean

)