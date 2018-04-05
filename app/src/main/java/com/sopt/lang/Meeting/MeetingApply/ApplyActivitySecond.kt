package com.sopt.lang.Meeting.MeetingApply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Meeting.MeetingApplyResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyActivitySecond : AppCompatActivity() {

    var meeting_id : Int=0
    var intro : String?=null
    private var networkService: NetworkService? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_second)

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        // Log.v("test1039", "in second")

        meeting_id = intent.getIntExtra("meeting_id",meeting_id!!)
        //Log.v("apply2",meeting_id.toString())


        val apply_edit = findViewById<EditText>(R.id.apply_edit) as EditText
        val apply_nextbtn2 = findViewById<ImageButton>(R.id.apply_next_btn) as ImageButton
        val activity_apply2 = findViewById<View>(R.id.apply2_frame)

        apply_edit.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (apply_edit.text.toString().length >= 5)
                    apply_nextbtn2.setImageResource(R.drawable.apply_next_on)
            }

            override fun afterTextChanged(editable: Editable) {
                if (apply_edit.text.toString().length < 5)
                    apply_nextbtn2.setImageResource(R.drawable.apply_next_off)
            }
        })

        apply_nextbtn2.setOnClickListener {
            Log.v("edit","edit")
            if (apply_edit.text.toString().length < 5) {
                val t = Toast.makeText(applicationContext, "자기소개를 5자 이상 입력해주세요!", Toast.LENGTH_SHORT)
                t.show()
            } else {

                val intent = Intent(
                        applicationContext,
                        ApplyActivityThird::class.java)
                intro = apply_edit!!.text.toString()
                //   intent.putExtra("intro",intro)
                // intent.putExtra("meeting_id",meeting_id)
                mMeetingApply()
                startActivity(intent)
            }
        }

        activity_apply2.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(apply_edit.windowToken, 0)
        }


//        val params = window.attributes
//
//        params.x = 0
//        params.height = 500
//        params.y = 10
//        params.height = 20
//        this.window.attributes = params

//       window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        Activity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        // window.findViewById<RelativeLayout>(R.id.apply2_frame)
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }
    fun mMeetingApply()
    {
        val meetingApplyResponse = networkService!!.postMeetingApply(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!,meeting_id,intro!!)
        meetingApplyResponse.enqueue(object : Callback<MeetingApplyResponse>{
            override fun onFailure(call: Call<MeetingApplyResponse>?, t: Throwable?) {
                Log.v("test1102", "onFailure "+t.toString())
                Log.v("test1102",SharedPreferencesService.instance!!.getPrefStringData("token", "")!!)
                Log.v("test1102",meeting_id.toString())
                Log.v("test1102",intro)
            }

            override fun onResponse(call: Call<MeetingApplyResponse>?, response: Response<MeetingApplyResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(response!!.body().status.equals("success"))
                    {
                        Log.v("test1102","onResponse"+response!!.body().status)
                    }
                }
            }

        })
    }
}
