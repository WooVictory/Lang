package com.sopt.lang.Meeting.MeetingApply

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Meeting.MeetingDetailActivity
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService

class ApplyActivityThird : AppCompatActivity() {

    var intro : String?=null
    var meeting_id : Int=0
    private var networkService: NetworkService? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_third)

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        Log.v("third", "intent")

        meeting_id = intent.getIntExtra("meeting_id",meeting_id!!)
        Log.v("meeting_id_apply",meeting_id.toString())
        intro = intent.getStringExtra("intro")
        // meeting_id랑 intro 받고 통신

        val apply_completebtn = findViewById<View>(R.id.apply_completebtn) as ImageButton

        apply_completebtn.setOnClickListener {
            val intent = Intent(applicationContext, MeetingDetailActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
