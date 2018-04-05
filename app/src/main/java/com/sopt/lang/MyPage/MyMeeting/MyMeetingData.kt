package com.sopt.lang.MyPage.MyMeeting

/**
 * Created by sec on 2018-01-08.
 */
data class MyMeetingData(
        var wait_icon: Int,        //202가 대기중 approval_state
        var meeting_name: String,  //eeting_title
        var meeting_lang: String, // meeting_lang
        var meeting_master: String, //204가 방장approval_state
        var meeting_purpose: String, //meeting_type
        var meeting_id: Int
)