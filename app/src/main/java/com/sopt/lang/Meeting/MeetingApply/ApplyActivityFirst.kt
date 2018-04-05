package com.sopt.lang.Meeting.MeetingApply

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R

class ApplyActivityFirst : AppCompatActivity() {

    var meeting_id : Int=0
    private var networkService: NetworkService? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_first)
        networkService = GlobalApplication.instance!!.networkService

        meeting_id = intent.getIntExtra("meeting_id",meeting_id!!) // meetingDetail로부터 받은 meeting_id
        Log.v("meeting_id_Apply_firtst", meeting_id.toString())



        //화면전환 - 인텐트 날리기(startActivity)
        // 1. 다음 넘어갈 화면 준비(xml, java)
        // 2. androidmanifest.xml에 activity를 등록
        // 3. intent 객체를 만들어서 startactivity 한다.



        val apply_checkbtn = findViewById<ImageButton>(R.id.apply_check_btn) as ImageButton
        val apply_nextbtn1 = findViewById<ImageButton>(R.id.apply_next_btn) as ImageButton

        apply_checkbtn.setOnClickListener {
            apply_checkbtn.isSelected = !apply_checkbtn.isSelected
            if (apply_checkbtn.isSelected) {
                apply_nextbtn1.setImageResource(R.drawable.apply_next_on)
            } else {
                apply_nextbtn1.setImageResource(R.drawable.apply_next_off)
            }
        }


        apply_nextbtn1.setOnClickListener {
            Log.v("clikeListener",meeting_id.toString())

            if (apply_checkbtn.isSelected) { // isSelected인 상태에서만 버튼이 활성화 될 수 있도록
                Log.v("Selecter",meeting_id.toString())

                val intent_next1 : Intent = Intent(this, ApplyActivitySecond::class.java)
                Log.v("intent",intent_next1.toString())
                intent_next1.putExtra("meeting_id",meeting_id)
                startActivity(intent_next1)
                Log.v("intent_next1_check",intent_next1.toString())
            } else {
                val t = Toast.makeText(this, "동의해주세요!", Toast.LENGTH_SHORT)
                t.show()
            }
        }
    }   //end onCreate()


}

