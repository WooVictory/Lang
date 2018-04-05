package com.sopt.lang.MyPage.MyMeeting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.YourMeeting.YourMeetingAdapter
import com.sopt.lang.Network.MyPage.OtherMeetingListData
import com.sopt.lang.Network.MyPage.OtherMeetingListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_your_meeting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YourMeetingActivity : AppCompatActivity(), View.OnClickListener {
    private var networkService: NetworkService? = null
    private var requestManager: RequestManager? = null

//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"


    override fun onClick(p0: View?) {
    }

    private var your_meeting_datas : List<OtherMeetingListData> ?= null
    private  var adapter : YourMeetingAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_meeting)

        networkService = GlobalApplication.instance!!.networkService

        var otherUserId : String? = null
        otherUserId = getIntent().getStringExtra("key")

        var otherUserName : String? = null
        otherUserName = getIntent().getStringExtra("user_name")

        mYourMeeting(otherUserId, otherUserName)

        your_meeting_list!!.layoutManager = LinearLayoutManager(this)
    }

    fun mYourMeeting(otherUserId : String, otherUserName : String){
        val yourMeetingListResponse : Call<OtherMeetingListResponse> = networkService!!.postOtherMeetingList(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, otherUserId)

        yourMeetingListResponse.enqueue(object : Callback<OtherMeetingListResponse>
        {
            override fun onFailure(call: Call<OtherMeetingListResponse>?, t: Throwable?) {
                Toast.makeText(this@YourMeetingActivity, "실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<OtherMeetingListResponse>?, response: Response<OtherMeetingListResponse>?) {
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
                    Log.v("모임1","모임1")

                    your_meeting_title.setText(otherUserName + "의 모임")
                    your_mmeting_subtitle.setText(otherUserName + "의 모임")
                    if(response!!.body().data ==null){
                        Log.v("모임 널","널")
                    }else{
                        your_meeting_datas = response!!.body().data
                        adapter = YourMeetingAdapter(your_meeting_datas!!,this@YourMeetingActivity)
                        your_meeting_list!!.adapter = adapter
                    }

                } else {
                    Toast.makeText(this@YourMeetingActivity, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
