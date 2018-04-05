package com.sopt.lang.Meeting.MeetingParticipants

/**
 * Created by sec on 2018-01-06.
 */
data class MeetingParticipantsData(
        var user_name: String,
        var user_image: String,
        var approval_state:Int,
        var user_id :  String,
        var meeting_user_id : Int
)