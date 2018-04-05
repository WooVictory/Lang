package com.sopt.lang.MyPage.MyMeeting

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sopt.lang.Meeting.MeetingDetailActivity
import com.sopt.lang.R



/**
 * Created by sec on 2018-01-08.
 */
class MyMeetingAdapter(var dataList : ArrayList<MyMeetingData>?, var ctx : Context?) : RecyclerView.Adapter<MyMeetingViewHolder>() {
    private  var onItemClick : View.OnClickListener ?=null

    override fun onBindViewHolder(holder: MyMeetingViewHolder?, position: Int) {
        holder!!.wait_icon.setImageResource(dataList!!.get(position).wait_icon)
        holder!!.meeting_name.setText(dataList!!.get(position).meeting_name)
        holder!!.meeting_master.setText(dataList!!.get(position).meeting_master)
        holder!!.meeting_lang.setText(dataList!!.get(position).meeting_lang)
        holder!!.meeting_purpose.setText(dataList!!.get(position).meeting_purpose)

        holder!!.itemView.setOnClickListener(View.OnClickListener { v ->
            //내 모임 눌렀을 때 모임 상세보기
            val intent = Intent(ctx, MeetingDetailActivity::class.java)
            intent.putExtra("meeting_id",dataList!!.get(position).meeting_id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            ctx!!.startActivity(intent)

        })

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyMeetingViewHolder {

        val mainView : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_mymeeting, parent, false)

        mainView.setOnClickListener ( onItemClick )

        return MyMeetingViewHolder(mainView)
    }

    override fun getItemCount(): Int  = dataList!!.size


}