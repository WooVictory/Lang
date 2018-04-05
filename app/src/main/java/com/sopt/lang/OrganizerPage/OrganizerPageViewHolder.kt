package com.sopt.lang.OrganizerPage

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sopt.lang.R

/**
 * Created by johee on 2018-01-13.
 */
class OrganizerPageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var p_profile_img: ImageView = itemView!!.findViewById<View>(R.id.participant_profile_img) as ImageView
    var p_name: TextView = itemView!!.findViewById<View>(R.id.participant_name) as TextView
    var master: ImageView = itemView!!.findViewById<View>(R.id.meetinginfo_organizer) as ImageView
}