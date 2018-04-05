package com.sopt.lang.Meeting.MeetingParticipants

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Meeting.MeetingDetailDataResponse
import com.sopt.lang.Network.Meeting.MeetingDetailParticipants
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetingParticipantsActivity : AppCompatActivity() {

    private var networkService: NetworkService? = null

//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    private var participants_list : RecyclerView? = null
    private var participants_datas : List<MeetingDetailParticipants>? = null
    private var participants_adapter : MeetingParticipantsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_participants)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        var meeting_id : Int? = null
        meeting_id = getIntent().getIntExtra("key", 0)

        mMeetingDetail(meeting_id!!)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        participants_list = findViewById<View>(R.id.recycler_view_participants) as RecyclerView
        participants_list!!.layoutManager = LinearLayoutManager(this)

        mMeetingDetail(meeting_id)

        var underln_textview : TextView = findViewById<View>(R.id.meeting_review_text_toolbar) as TextView
        val content = SpannableString("모임 후기")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        underln_textview.text = content

/*        underln_textview.setOnClickListener{
            val intent = Intent(this, MeetingReviewActivity::class.java)
            startActivity(intent)
        }*/
    }

    fun mMeetingDetail(meeting_id : Int){
        val meetingDetailResponse = networkService!!.getMeetingDetail(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!,meeting_id!!)
        meetingDetailResponse.enqueue(object : Callback<MeetingDetailDataResponse> {
            override fun onResponse(call: Call<MeetingDetailDataResponse>?, response: Response<MeetingDetailDataResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(response!!.body().status.equals("success"))
                    {
                        participants_datas = response!!.body().data.participants

                        participants_adapter = MeetingParticipantsAdapter(participants_datas, this@MeetingParticipantsActivity, response!!.body().data.info.user_id)
                        participants_list!!.adapter = participants_adapter
                    }
                }
            }

            override fun onFailure(call: Call<MeetingDetailDataResponse>?, t: Throwable?) {
            }

        })
    }

}