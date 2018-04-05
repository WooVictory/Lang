package com.sopt.lang.MyPage.MyMeeting

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-08.
 */
class MyMeetingViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var wait_icon : ImageView = itemView!!.findViewById<ImageView>(R.id.meeting_wait) as ImageView
    var meeting_name : TextView = itemView!!.findViewById<TextView>(R.id.my_meeting_name) as TextView
    var meeting_master : TextView = itemView!!.findViewById<TextView>(R.id.my_meeting_master) as TextView
    var meeting_lang : TextView = itemView!!.findViewById<TextView>(R.id.my_meeting_lang) as TextView
    var meeting_purpose : TextView = itemView!!.findViewById<TextView>(R.id.m_meeting_purpose) as TextView

}