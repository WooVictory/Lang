package com.sopt.lang.MyPage.YourMeeting

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sopt.lang.R

/**
 * Created by kor on 2018-01-11.
 */


class YourMeetingViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var meeting_title : TextView = itemView!!.findViewById<TextView>(R.id.your_meeting_name) as TextView
    var meeting_lang : TextView = itemView!!.findViewById<TextView>(R.id.your_meeting_lang) as TextView
    var meeting_purpose : TextView = itemView!!.findViewById<TextView>(R.id.your_meeting_purpose) as TextView
    var meeting_master : TextView = itemView!!.findViewById<TextView>(R.id.your_meeting_master) as TextView

}