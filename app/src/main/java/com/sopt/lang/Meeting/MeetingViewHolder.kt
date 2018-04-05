package com.sopt.lang.Meeting

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-01.
 */
class MeetingViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView){
    var groupImage : ImageView = itemView!!.findViewById(R.id.detail_image)
    var groupTitle : TextView = itemView!!.findViewById(R.id.detail_title)
    var groupLanguage : TextView = itemView!!.findViewById(R.id.detail_language)
    var groupHost : TextView = itemView!!.findViewById(R.id.detail_host)
    var groupPurpose : TextView = itemView!!.findViewById(R.id.detail_purpose)
    var groupLike : ImageButton = itemView!!.findViewById(R.id.detail_group_like_btn)
    // 리사이클러뷰로 돌아갈 아이템에 대한 뷰의 아이디를 다 가지고 옴


}