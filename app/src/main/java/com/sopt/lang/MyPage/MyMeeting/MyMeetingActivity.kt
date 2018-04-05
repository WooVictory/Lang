package com.sopt.lang.MyPage.MyMeeting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.MyPage.MyMeetingListData
import com.sopt.lang.Network.MyPage.MyMeetingListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyMeetingActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
    }

    private var my_meeting_list : RecyclerView ?= null
    private var my_meeting_datas : ArrayList<MyMeetingData> ?= null
    private  var adapter : MyMeetingAdapter?=null

    private var networkService: NetworkService? = null
    private var myMeeting_data: List<MyMeetingListData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_meeting)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        my_meeting_list = findViewById<View>(R.id.my_meeting) as RecyclerView
        my_meeting_list!!.layoutManager = LinearLayoutManager(this)
        my_meeting_datas = ArrayList<MyMeetingData>()

        val myMeetingResponse = networkService!!.getMyMeetingList(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        myMeetingResponse.enqueue(object : Callback<MyMeetingListResponse> {
            override fun onResponse(call: Call<MyMeetingListResponse>?, response: Response<MyMeetingListResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
//                        GlobalApplication.instance!!.makeToast("성공")
                        myMeeting_data = response!!.body().data
// response!!.body().data!![i-1].meeting_image

                        //meeting purpose
                        //100 전체, 101 언어교류, 102 파티
                        //유저상태 202 대기중일 때 아이콘하기

                        var size: Int = response!!.body().data!!.size
                        my_meeting_datas!!.clear()

                        //drawable mymeeting_wait

                        for (i in 1..size) {
                            var wait_icon: Int
                            var purpose: String
                            if (response!!.body().data!![i - 1].approval_state == 202) {
                                wait_icon = R.drawable.mymeeting_wait
                            } else {
                                wait_icon = 0
                            }

                            if (response!!.body().data!![i - 1].meeting_type == 100) {
                                purpose = "전체"
                            } else if (response!!.body().data!![i - 1].meeting_type == 101) {
                                purpose = "언어교류"
                            } else {
                                purpose ="파티"
                            }

                            my_meeting_datas!!.add(MyMeetingData(
                                    wait_icon,
                                    response!!.body().data!![i - 1].meeting_title,
                                    response!!.body().data!![i - 1].meeting_lang,
                                    "주최자 : "+response!!.body().data!![i - 1].user_name,
                                    purpose,
                                    response!!.body().data!![i-1].meeting_id))

                            //user_id가 주최자 이름이 들어가야함..
                            adapter = MyMeetingAdapter(my_meeting_datas, application)
                            // adapter!!.setOnItemClickListener(this)

                            my_meeting_list!!.adapter = adapter
                        }

                        //my_meeting_datas!!.add(MyMeetingData(R.drawable.mymeeting_wait, "홍대 Language Exchange", "English", "주최자 : 조희", "언어교류"));
                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<MyMeetingListResponse>?, t: Throwable?) {
                Log.v("444", t.toString())
                Log.v("ssssssssss", t.toString())
                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })



    }
}
