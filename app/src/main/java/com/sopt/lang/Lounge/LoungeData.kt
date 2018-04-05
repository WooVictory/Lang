package com.sopt.lang.Lounge

/**
 * Created by sec on 2018-01-07.
 */
data class LoungeData (
        var key : Int,
        var writer_pic : String,
        var writer_name : String,
        var uploaded_time : String,
        var uploaded_images : Array<String>,
        var lounge_text : String,
        var writer_language : String,
        var writer_interest : String,
        var num_of_like : Int,
        var num_of_comments : Int,
        var is_like_on : Boolean
)