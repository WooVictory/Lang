package com.sopt.lang.Meeting.MeetingCreate

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.R
import kotlinx.android.synthetic.main.activity_meeting_create5.*

class MeetingCreateActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_create5)


        meeting5_create_confirm_btn!!.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
