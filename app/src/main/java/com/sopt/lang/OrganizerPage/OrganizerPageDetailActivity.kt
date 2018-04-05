package com.sopt.lang.OrganizerPage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.ManagementMeeting.ManagementWatingListDetailData
import com.sopt.lang.Network.ManagementMeeting.ManagementWatingListDetailResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_organizer_page_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizerPageDetailActivity : AppCompatActivity() {
    private var networkService: NetworkService? = null

    private var WatingListDetailData : ManagementWatingListDetailData ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_page_detail)
        var userId : String = intent.getStringExtra("key")



        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { finish() }

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)


        Log.v("디테일 아이디",userId)

        val waitResponse = networkService!!.getWaitingList(SharedPreferencesService.instance!!.getPrefStringData("token")!!, userId!!)
        waitResponse.enqueue(object : Callback<ManagementWatingListDetailResponse> {
            override fun onResponse(call: Call<ManagementWatingListDetailResponse>?, response: Response<ManagementWatingListDetailResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        WatingListDetailData = response!!.body().data
                        Glide.with(applicationContext).load(WatingListDetailData!!.user_image).apply(RequestOptions.circleCropTransform())
                                .into(profile_img_awaiter)
                        name_awaiter.setText(WatingListDetailData!!.user_name)
                        my_lang.setText(WatingListDetailData!!.native_lang)
                        hope_lang.setText(WatingListDetailData!!.hope_lang)
                        introduce.setText(WatingListDetailData!!.apply_intro)
                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<ManagementWatingListDetailResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })









    }
}