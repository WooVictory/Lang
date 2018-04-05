package com.sopt.lang.MyPage.MyLounge

/**
 * Created by johee on 2018-01-09.
 */
data class MyLoungeData (
        var key : Int,
        var writer_pic : String,
        var writer_name : String,
        var uploaded_time : String,
        var uploaded_images : List<String?>?,
        var lounge_text : String,
        var writer_language : String,
        var writer_interest : String,
        var num_of_like : Int,
        var num_of_comments : Int,
        var is_like_on : Boolean
)