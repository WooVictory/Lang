package com.sopt.lang.Meeting.MeetingCreate

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.sopt.lang.R
import kotlinx.android.synthetic.main.activity_meeting_create2.*

class MeetingCreateActivity2 : AppCompatActivity() {

    internal lateinit var my: RelativeLayout
    internal lateinit var wish: RelativeLayout
    internal lateinit var my_text: TextView
    internal lateinit var wish_text: TextView
    internal lateinit var complete: ImageView
    internal var myValue = ""
    var meetingType : Int = 0
    var meetingTitle : String = "1"
    var meetingIntro : String = "1"
    var imgUrl : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_create2)

        imgUrl = getIntent().getStringExtra("imgUrl")


        // 뒤로가기 버튼
        meeting2_create_back_btn!!.setOnClickListener {
            finish()
        }


        meeting2_create_communication_btn.setOnClickListener {

        }
        meeting2_create_communication_btn!!.setOnClickListener {
            meeting2_create_communication_btn.isSelected = !meeting2_create_communication_btn.isSelected
            if (meeting2_create_communication_btn.isSelected) {
                meetingType = 101
//                meeting2_create_communication_btn.setImageResource(R.drawable.meetingcreate_select)
                meeting2_create_communication_btn.isSelected = !meeting2_create_communication_btn.isSelected

            }
        }
            meeting2_create_party_btn!!.setOnClickListener {
                meeting2_create_party_btn.isSelected = !meeting2_create_party_btn.isSelected
                if (meeting2_create_party_btn.isSelected) {
                    meetingType = 102
//                    meeting2_create_party_btn.setImageResource(R.drawable.meetingcreate_select)
                    meeting2_create_party_btn.isSelected = !meeting2_create_party_btn.isSelected

                }
            }


                /*      } else {
                          meeting2_create_communication_btn.setImageResource(R.drawable.meetingcreate_unselect)
                          meeting2_create_party_btn.isClickable = true
                      }
                  }
                  meeting2_create_party_btn!!.setOnClickListener {
                      meeting2_create_party_btn.isSelected = !meeting2_create_party_btn.isSelected
                      if (meeting2_create_party_btn.isSelected) {
                          meetingType = 102
                          meeting2_create_party_btn.setImageResource(R.drawable.meetingcreate_select)
                          meeting2_create_communication_btn.isClickable = false
                      } else {
                          meeting2_create_party_btn.setImageResource(R.drawable.meetingcreate_unselect)
                          meeting2_create_communication_btn.isClickable = true
                      }
                  }*/

        // 다음 버튼
        meeting2_create_next_btn!!.setOnClickListener {
            var intent : Intent = Intent(this, MeetingCreateActivity3::class.java)
            intent.putExtra("imgUrl",imgUrl)
            Log.v("meeting2 ", imgUrl!!)
            intent.putExtra("meetingTitle",meeting_Title.text.toString())
            Log.v("920",meeting_Title.text.toString())
            intent.putExtra("meetingIntro",meeting_intro.text.toString())
            intent.putExtra("meetingType",meetingType)
            intent.putExtra("myValue",meeting_lang.text.toString())
            startActivity(intent)

        }

        // 언어 선택
        meeting_lang_select!!.setOnClickListener {
            dlg()
        }


    }

    fun dlg() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.language_dialog, null)
        val group_join_ok = dialogView.findViewById<TextView>(R.id.join_ok) as TextView
        val group_join_cancle = dialogView.findViewById<TextView>(R.id.join_cancle) as TextView
        val group = dialogView.findViewById<RadioGroup>(R.id.language_radio) as RadioGroup

        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        val buider = AlertDialog.Builder(this@MeetingCreateActivity2) //AlertDialog.Builder 객체 생성
        buider.setTitle("My Language") //Dialog 제목
        buider.setView(dialogView) //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        //설정한 값으로 AlertDialog 객체 생성
        val dialog = buider.create()
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false)//없어지지 않도록 설정
        //Dialog 보이기
        dialog.show()

        Log.d("1","1")

        group!!.setOnCheckedChangeListener { group, checkedId ->
            Log.d("2","2")
            when (checkedId) {
                R.id.kor ->
                    myValue = "한국어"

                R.id.eng ->
                    myValue = "English"

                R.id.jap ->
                    myValue = "漢字"

                R.id.cha ->
                    myValue = "漢語"

                R.id.esp ->
                    myValue = "Español"
            }
        }
        group_join_ok!!.setOnClickListener{
            meeting_lang.text = myValue
            meeting2_create_next_btn.setImageResource(R.drawable.apply_next_on)
            dialog.dismiss()
        }
        group_join_cancle!!.setOnClickListener{
            meeting_lang.text=""
            meeting2_create_next_btn.setImageResource(R.drawable.apply_next_off)
            dialog.dismiss()
        }

    }

}


